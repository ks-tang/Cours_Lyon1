import os
import sys
import pickle
import pandas as pd
from imblearn.over_sampling import SMOTE
from sklearn.decomposition import PCA
from sklearn.ensemble import RandomForestClassifier
from sklearn.naive_bayes import GaussianNB
from sklearn.neural_network import MLPClassifier
from sklearn.metrics import confusion_matrix
from sklearn.metrics import f1_score
from sklearn.metrics import balanced_accuracy_score

_DIR_ = os.path.dirname(os.path.abspath(__file__))


####
# FONCTION (privé) DE CALCUL DES MODELES
####
def fit(model, X_tr, y_tr):
    model.fit(X_tr, y_tr)
    return model

def predict(model, X_te):
    Y_pred = model.predict(X_te)
    return Y_pred

def fit_predict(model, X_tr, y_tr, X_te):
    model = fit(model, X_tr, y_tr)
    Y_pred = predict(model, X_te)
    return model, Y_pred

def add_score_dict(dict_model, model_name, y_true, Y_pred):
    dict_model[model_name]['confusion_matrix'] = confusion_matrix(y_true, Y_pred)
    dict_model[model_name]['f1_score'] = f1_score(y_true, Y_pred)
    dict_model[model_name]['balanced_accuracy_score'] = balanced_accuracy_score(y_true, Y_pred)
    return dict_model

def run_model(dict_model, model_name, model, X_tr, y_tr, X_te, y_te):
    model, Y_pred = fit_predict(model, X_tr, y_tr, X_te)
    dict_model = add_score_dict(dict_model, model_name, y_te, Y_pred)
    return dict_model

def classement_model(dict_model, metric):
    return sorted(dict_model.items(), key=lambda x: x[1][metric], reverse=True)

def best_model(dict_model, metric):
    return classement_model(dict_model, metric)[0]

####
# FONCTION (public) DE CALCUL DES MODELES
####
def compute_model_all(dict_model, X_tr, y_tr, X_te, y_te):
    nb = len(dict_model)
    for model_name, model in dict_model.items():
        print("Calcul de : ", model_name, " (", nb, " restant(s))")
        dict_model = run_model(dict_model, model_name, model['model'], X_tr, y_tr, X_te, y_te)
    return dict_model

def compute_model_one(dict_model, model_name, X_tr, y_tr, X_te, y_te):
    print("Calcul de : ", model_name)
    dict_model = run_model(dict_model, model_name, dict_model[model_name]['model'], X_tr, y_tr, X_te, y_te)
    return dict_model

####
# TRAITEMENT DES DONNEES
####
def normalize_data(X_train, X_test):
    X_train /= 255
    X_test /= 255
    return X_train, X_test

def smote_data(X_train, y_train):
    sm = SMOTE(sampling_strategy='minority')
    X_train, y_train = sm.fit_resample(X_train, y_train)
    return X_train, y_train

def pca_data(X_train, X_test, loadPCA=False, withExport=False):
    if loadPCA:
        script_dir = os.path.dirname(__file__)
        pca_file_path = os.path.join(script_dir, '../artifacts/pca.pkl')
        with open(pca_file_path, 'rb') as f:
            pca = pickle.load(f)
    else:
        pca = PCA(n_components=0.95)
        pca.fit(X_train)
        if withExport:
            script_dir = os.path.dirname(__file__)
            pca_file_path = os.path.join(script_dir, '../artifacts/pca.pkl')
            with open(pca_file_path, 'wb') as f:
                pickle.dump(pca, f)
    X_train = pca.transform(X_train)
    X_test = pca.transform(X_test)
    return X_train, X_test

####
# CHARGEMENT DES DONNEES
####
def traitement_data(X_train, y_train, X_test, y_test, listType, loadPCA=False, exportPCA=False):
    # Cas vide
    if len(listType) == 0:
        return X_train, y_train, X_test, y_test
    
    # Traitement des données
    if 'norm' in listType:
        X_train, X_test = normalize_data(X_train, X_test)
    if 'pca' in listType:
        X_train, X_test = pca_data(X_train, X_test, loadPCA, exportPCA)
    if 'smote' in listType:
        X_train, y_train = smote_data(X_train, y_train)

    return X_train, y_train, X_test, y_test

def load_file_To_DataFrame(file):
    script_dir = os.path.dirname(__file__)
    file_path = os.path.join(script_dir, file)
    try:
        df = pd.read_csv(file_path)
    except:
        print("Erreur lors du chargement de : ", file_path)
        sys.exit(1)
    return df

def split_X_Y(df):
    # Séparation des features et des labels
    X = df.drop('target', axis=1)
    y = df['target']
    return X, y

def export_DfXY_Csv(X, y, file_path):
    fp = file_path
    script_dir = os.path.dirname(__file__)
    file_path = os.path.join(script_dir, file_path)
    df_X = pd.DataFrame(X)
    df_y = pd.DataFrame(y)
    df = pd.concat([df_X, df_y], axis=1)
    df.to_csv(file_path, index=False)
    print("Export de : ", fp, " réussi !")

def processessing_Data_from_file(pathTrain, pathTest, listType=[], loadPCA=False, exportPCA=True):
    # Chargement des données
    print("Chargement des données... 1/4")
    ref_data = load_file_To_DataFrame(pathTrain)
    test_data = load_file_To_DataFrame(pathTest)
    
    # Séparation des features et des labels
    print("Séparation des features et des labels... 2/4")
    X_train, y_train = split_X_Y(ref_data)
    X_test, y_test = split_X_Y(test_data)

    # Traitement des données
    print("Traitement des données... 3/4")
    X_train, y_train, X_test, y_test = traitement_data(X_train, y_train, X_test, y_test, listType, loadPCA, exportPCA)

    # Export des données
    print("Export des données... 4/4")
    file_name = os.path.splitext(pathTrain)[0] + "_pca.csv"
    export_DfXY_Csv(X_train, y_train, file_name)
    file_name = os.path.splitext(pathTest)[0] + "_pca.csv"
    export_DfXY_Csv(X_test, y_test, file_name)


def load_data_RefProd(pathTrain, pathTest, loadProd=True):
    # Chargement des données
    train_data = load_file_To_DataFrame(pathTrain)
    test_data = load_file_To_DataFrame(pathTest)
    test_data = test_data.drop(['prediction'], axis=1, errors='ignore')

    # On ne charge les données de production que si nécessaire
    if loadProd:
        prod_data = load_file_To_DataFrame('../data/prod_data.csv')
        prod_data = prod_data.drop(['prediction'], axis=1, errors='ignore')
    
        # On divise les données de production en données 
        # de test et d'entrainement
        prod_test = prod_data.sample(frac=0.2)
        prod_train = prod_data.drop(prod_test.index)

        # Concaténation des données de référence et de production 
        train_data = pd.concat([train_data, prod_train])
        test_data = pd.concat([test_data, prod_test])
    
    # Séparation des features et des labels
    X_train, y_train = split_X_Y(train_data)
    X_test, y_test = split_X_Y(test_data)

    return X_train, y_train, X_test, y_test

####
# GESTION DES MODELES
####
def model_better_previous(dict_model, model_name, metric):
    script_dir = os.path.dirname(__file__)
    score_file_path = os.path.join(script_dir, '../artifacts/' + 'model_score.txt')
    try:
        d={}
        with open(score_file_path) as f:
            for line in f:
                (key, val) = line.split(",")
                d[key] = float(val)
        score = d[metric]
    except:
        print("Erreur lors de la lecture du fichier : ", score_file_path)
        score = -1

    if dict_model[model_name][metric] > score:
        print("Nouveau modèle meilleur que l'ancien : ", dict_model[model_name][metric], " > ", score, " (+", dict_model[model_name][metric] - score, ")")
        print("Amélioration de : ", dict_model[model_name][metric] - score)
        return True
    else:
        print("Ancien modèle meilleur que le nouveau : ", dict_model[model_name][metric], " < ", score, " (-", score - dict_model[model_name][metric], ")")
        return False
        

def export_model(dict_model, model_name, name, X_test, y_test):
    # Export du score
    script_dir = os.path.dirname(__file__)
    score_file_path = os.path.join(script_dir, '../artifacts/' + name + '_score.txt')
    with open(score_file_path, 'w') as f:
        f.write("f1_score," + str(dict_model[model_name]['f1_score'])+"\n")
        f.write("balanced_accuracy_score," + str(dict_model[model_name]['balanced_accuracy_score']))

    # Export du modèle
    script_dir = os.path.dirname(__file__)
    model_file_path = os.path.join(script_dir, '../artifacts/' + name + '.pkl')
    with open(model_file_path, 'wb') as f:
        pickle.dump(dict_model[model_name]['model'], f)

    # Ajout de la prédiction sur les données de test
    y_pred = predict(dict_model[model_name]['model'], X_test)
    ref_test = pd.DataFrame(X_test)
    ref_test['target'] = y_test
    ref_test['prediction'] = y_pred
    ref_test.to_csv(os.path.join(script_dir, '../data/ref_data_Test_pca.csv'), index=False)


def create_model(debug=False):
    # Dans le cadre de l'API, on ne conserve que le MLPClassifier
    # avec les meilleurs paramètres trouvés par GRIDSEARCH afin
    # de pas relancer un script de plusieurs heures.
    dict_model = {}
    #dict_model['naiveBayes'] = {'model' : GaussianNB()}
    #dict_model['randomForest'] = {'model' :  RandomForestClassifier(n_estimators=150)}
    dict_model['MLP'] = {'model' : MLPClassifier(verbose=debug, hidden_layer_sizes=(100, 100, 100), max_iter=100, activation='logistic', learning_rate='adaptive', solver='adam')}
    
    # Score par défaut avant le calcul
    for model_name, _ in dict_model.items():
        dict_model[model_name]['confusion_matrix'] = None
        dict_model[model_name]['f1_score'] = -1
        dict_model[model_name]['balanced_accuracy_score'] = -1

    return dict_model

##########
# SCRIPT CREATION FICHIER AVEC PCA
##########
# processessing_Data_from_file("../data/ref_data.csv", "../data/ref_data_Test.csv", ['norm','pca','smote'],False,True)



##########
# SCRIPT ENTRAINEMENT MODELE
##########
# dict_model = create_model()
# X_train, y_train, X_test, y_test = load_data_RefProd('../data/ref_data_pca.csv', 
#                                                         '../data/ref_data_Test_pca.csv',
#                                                         False)
# dict_model = compute_model_all(dict_model, X_train, y_train, X_test, y_test)
# print("Dict_model : ", dict_model)
# print("Best model : ", best_model(dict_model, 'balanced_accuracy_score'))
# export_model(dict_model, 
#             best_model(dict_model, 'balanced_accuracy_score')[0], 
#             'model',
#             X_test, 
#             y_test)
