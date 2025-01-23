# Projet Machine Learning 1 : Prédiction d'anomalies dans un jeu de données
Auteur : Bourbon Elodie, Vincent Yann, Tang Kevin, Mercier Loris

Notre projet a pour objectif de détecter des avions (=anomalies)


![notre site](/cifar_img/capture_site.png)

## Execution du projet

- Veuillez tout d'abord lancer docker sur votre machine personnelle
- Ensuite, veuillez lancer les commandes suivantes dans l'ordre indiqué :

```bash
docker compose -f serving/docker-compose.yml up --build --force-recreate
```
```bash
docker compose -f webapp/docker-compose.yml up --build --force-recreate   
```
```bash
docker compose -f reporting/docker-compose.yml up --build --force-recreate
```

- Retrouver l'interface du projet à l'adresse suivante : http://localhost:8081/
- Retrouver l'interface de reporting à l'adresse suivante : http://localhost:8082/
- Retrouver l'interface de serving à l'adresse suivante : http://localhost:8080/docs


## Fonctionnement du projet
- Les utilisateurs uploadent une image de leur choix sur l'interface web
- L'utilisateur est ensuite invité à renseigner un feedback afin d'améliorer le modèle

**Tous les 10 feedbacks, le modèle est ré-entrainé avec l'ensemble des données (référence + production). Ce nouveau modèle n'est mis en production que si ses performances dépassent celles du précédent modèle.**



## Création des fichiers/modèles initiaux

- Pour executer la PCA initiale (créant les fichiers *ref_data_pca.csv* et *ref_data_Test_pca.csv*) :
    - Décommenter la ligne suivant " SCRIPT CREATION FICHIER AVEC PCA" dans le fichier *scripts/trainmodel.py*
    - Lancer le script *scripts/trainmodel.py*

- Pour executer le modèle initial (créant le fichier *model.pkl*) :
    - Décommenter la ligne suivant " SCRIPT ENTRAINEMENT MODELE" dans le fichier *scripts/trainmodel.py*
    - Lancer le script *scripts/trainmodel.py*

Remarque 1 : Notre meilleur modèle est un MLPClassifier. Nous avons ensuite appliqué un GridSearch pour trouver ses meilleurs paramètres.

Remarque 2 : Le modèle est mis à jour tous les 10 feedbacks en cas de meilleure performance.