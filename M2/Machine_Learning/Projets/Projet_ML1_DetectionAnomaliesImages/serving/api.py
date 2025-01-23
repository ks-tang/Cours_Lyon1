import base64
import pickle
import os
import sys
import csv
import numpy as np
import pandas as pd
from io import BytesIO
from fastapi import BackgroundTasks, FastAPI
from pydantic import BaseModel
from PIL import Image
# Import trainmodel
try:
    script_dir = os.path.dirname(__file__)
    sys.path.append(os.path.join(script_dir, '../scripts/'))
    import trainmodel as tm
except ImportError:
    print("Erreur dans l'importation de trainmodel.py")


###################################
# ---------------------------------
# ------- Encodeur d'Image --------
# ---------------------------------
###################################
def read_b64Img(img_b64):
    return Image.open(BytesIO(img_b64))

def encode_img(img):
    img = img.resize((32, 32))    
    r,g,b = img.split()
    r = np.array(r).reshape(-1)
    g = np.array(g).reshape(-1)
    b = np.array(b).reshape(-1)
    tab = np.concatenate((np.array(r), np.array(g), np.array(b)))
    return tab.reshape(1, -1)

def apply_pca(img_encode):
    # Chargement du modèle
    script_dir = os.path.dirname(__file__)
    pca_file_path = os.path.join(script_dir, '../artifacts/pca.pkl')
    with open(pca_file_path, 'rb') as f:
        pca = pickle.load(f)
    # Ajout des noms de colonnes
    feature_names = [str(i) for i in range(3072)]
    img_encode_df = pd.DataFrame(img_encode, columns=feature_names)
    img_encode_df.head()
    # Application de la PCA
    img_pca = pca.transform(img_encode_df)
    return img_pca

def preprocessing_input_img(img_data):
    decoded_file = base64.b64decode(img_data)   
    image = read_b64Img(decoded_file)
    img_encode = encode_img(image)
    img_pca = apply_pca(img_encode)
    return img_pca
###################################
# ---------------------------------
# ------- Corps de requête --------
# ---------------------------------
###################################
class img_body(BaseModel):
    file: str

class feedback_body(BaseModel):
    file: str
    prediction: int
    target: int



###################################
# ---------------------------------
# ----- Mise à jour du modèle -----
# ---------------------------------
###################################
def updateModel():
    print("MISE A JOUR DU MODELE EN COURS...")
    # Grâce à notre première phase de test, on sait que le 
    # MLPClassifier est le meilleur modèle.
    # Nous conservons les paramètres rendu par le GRIDSEARCH.
    # Les données étant volumineuses, nous conservons ce modèle
    # sans réeffectuer toute la phase de test de différents modèles.
    dict_model = tm.create_model()

    # On choisi de normalisées les données et d'appliquer un rééchantillonage
    print("Chargement des données... 1/3")
    X_train, y_train, X_test, y_test = tm.load_data_RefProd('../data/ref_data_pca.csv', 
                                                            '../data/ref_data_Test_pca.csv',
                                                            True)

    # On calcule le modèle directement avec MLP (voir explications ci-dessus)
    print("Calcul des modèles en cours... 2/3")
    dict_model = tm.compute_model_all(dict_model, X_train, y_train, X_test, y_test)

    # On exporte le meilleur modèle en fonction de la balance accuracy
    # qui est forcément MLP puisqu'il est le seul calculé !
    print("Export du modèle... 3/3")
    if tm.model_better_previous(dict_model, tm.best_model(dict_model, 'balanced_accuracy_score')[0],'balanced_accuracy_score'):
        tm.export_model(dict_model, 
                        tm.best_model(dict_model, 'balanced_accuracy_score')[0], 
                        'model',
                        X_test, 
                        y_test)    
        print("Nouveau modèle en production !")
    else:
        print("Ancien modèle conservé !")
        print("MISE A JOUR DU MODELE TERMINEE !")    


###################################
# ---------------------------------
# ------ CREATION DE l'API --------
# ---------------------------------
###################################

app = FastAPI()

# Seuil de mise à jour du modèle
K = 10

# Gestion des requêtes /predict
@app.post('/predict')
def predict(input_data: img_body):
    # Chargement du modèle
    script_dir = os.path.dirname(__file__)
    # model_file_path = os.path.join(script_dir, '../artifacts/model.pkl')
    model_file_path = os.path.join(script_dir, '../artifacts/model.pkl')

    with open(model_file_path, 'rb') as f:
        model = pickle.load(f)

    # Prétraitement de l'image
    img_pca = preprocessing_input_img(input_data.file)

    # Prédictions
    classe = model.predict(img_pca)
    proba = model.predict_proba(img_pca)

    return {'prediction': classe.tolist()[0],
            'proba': proba.tolist()[0]}

# Gestion des requêtes feedback
@app.post('/feedback')
def feedback(input_data: feedback_body, background_tasks: BackgroundTasks):
    # Prétraitement de l'image
    img_pca = preprocessing_input_img(input_data.file)

    # On récupère la prédiction et la cible
    pred = input_data.prediction
    target = input_data.target

    # On enregistre les données dans le fichier prod_data.csv
    script_dir = os.path.dirname(__file__)
    prod_data_file_path = os.path.join(script_dir, '../data/prod_data.csv')

    with open(prod_data_file_path, 'a', newline='') as f:
        writer = csv.writer(f)

        # On récupère le nombre de lignes du fichier (+1 car on va ajouter une ligne)
        nb_lines = sum(1 for line in open(prod_data_file_path)) + 1

        # Ajout d'en-tête si le fichier est vide
        if os.stat(prod_data_file_path).st_size == 0:
            nb_col = len(img_pca[0].tolist())
            writer.writerow([str(i) for i in range(nb_col)] + ['target', 'prediction'])

        writer.writerow(img_pca[0].tolist() + [target, pred])

        # Mise à jour du modèle si le nombre de lignes est du seuil K
        if nb_lines>0 and nb_lines%K==0:
            background_tasks.add_task(updateModel)
    
    return {'feedback': 'ok'}
