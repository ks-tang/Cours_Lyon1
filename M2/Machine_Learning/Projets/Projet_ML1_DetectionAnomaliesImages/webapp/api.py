import streamlit as st
import requests
import base64
from io import BytesIO
from PIL import Image

# Create the webapp
st.title("Prédiction d'anomalies dans des images")
st.write("Ce modèle a été entrainé sur le jeu de données CIFAR10.")
st.write("Il est capable de détecter des avions (=anomalies) dans des images.")
# Upload the image
option = st.radio("Comment voulez-vous charger l'image ?", ('Upload', 'URL'))

# Déclaration variable globale
if 'feedback' not in st.session_state:
    st.session_state['feedback'] = 0

if 'prediction' not in st.session_state:
    st.session_state['prediction'] = None

def show_feedback(encoded_file, pred):
    st.write(":pencil2: Votre feedback : ")
    target = st.radio("Est-ce une bonne prédiction ?", ('oui', 'non'))
    target = True if target == 'oui' else False
    # XNOR entre la prédiction et la cible pour avoir le bon label
    target = int(not(bool(pred ^ target))) 
    if(st.button("Envoyer")):
        url = "http://serving-api:8080/feedback"
        data = {'file': encoded_file,
                'prediction': pred,
                'target': int(target)}       
        r = requests.post(url, json=data)
        if(r.status_code == 200):
            st.write(":white_check_mark: Merci du feedback !!!")
        else:
            st.write(":x: Une erreur est survenue lors de l'envoi du feedback !")
        st.session_state['feedback'] = False

def predict_anomaly(encoded_file):
    if(st.button("Predict")):
        url = "http://serving-api:8080/predict"
        data = {'file': encoded_file}       
        r = requests.post(url, json=data)
        r = r.json()
        st.session_state['prediction'] = r['prediction']
        if(r['prediction'] == 1):
            st.write("**Anomalie détectée !**")
            st.write("Fiabilité : ", r['proba'][1],"%")     
        else:
            st.write("**Pas d'anomalie détectée !**")
            st.write("Fiabilité : ", r['proba'][0],"%")
        st.session_state['feedback'] = True
        # show_feedback(encoded_file, r['prediction'])
        # st.write("STOOP")
    if(st.session_state['feedback']):
        show_feedback(encoded_file, st.session_state['prediction'])

def display_image(img):
    st.image(img, width=400)

def encode_file(file):
    return base64.b64encode(file).decode('utf-8')

if option == 'Upload':
    uploaded_file = st.file_uploader("Choisir une image...", type=["jpg", "jpeg"])
    if uploaded_file is not None:
        display_image(uploaded_file)
        encoded_file = encode_file(uploaded_file.read())
        predict_anomaly(encoded_file)

elif option == 'URL':
    url = st.text_input('Entrer l\'URL:')
    if url:
        try:
            response = requests.get(url)
            img = Image.open(BytesIO(response.content))
            display_image(img)
            encoded_file = encode_file(response.content)
            predict_anomaly(encoded_file)
        except:
            st.write("URL invalide !")
