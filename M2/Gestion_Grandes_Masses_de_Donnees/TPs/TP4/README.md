# GGMD - TP4 : Traitement de flux via Spark Streaming

### Sujet du TP
[Lien vers sujet TP4 - Spark Streaming](https://forge.univ-lyon1.fr/ggmd/ggmd-tps-etudiants/-/blob/main/GGMD_TP4_SparkStreaming.md)

### Liens utiles
- [Sujet CM2](https://perso.univ-lyon1.fr/nicolas.lumineau/ue/ggmd/cm/GGMD-CM-PartieFlux.pdf)

## Informations VM

- Fichier `p1908025.pem` sur Tomuss à mettre dans le répertoire home (renommé `ggmd-spark-tp3.pem`).
- Mettre les droits 600 avec *chmod*.
- Adresse IP sur Tomuss : `192.168.76.141`
- Se connecter en SSH avec cette commande :
```
ssh -i .ssh/ggmd-spark-tp3.pem p1908025@192.168.76.141
```
- Copier les fichiers de ma machine vers la VM
```bash
cd /mnt/c/Users/Cecilia/Documents/GITHUB/CECILIA/ggmd-2024/TP4

# Copie les fichiers python
scp -i ~/.ssh/ggmd-spark-tp3.pem *.py p1908025@192.168.76.141:~/TP4
```
- Copier les fichiers de la VM vers ma machine
```bash
cd /mnt/c/Users/Cecilia/Documents/GITHUB/CECILIA/ggmd-2024/TP4

# Copie les fichiers log
scp -i ~/.ssh/ggmd-spark-tp3.pem p1908025@192.168.76.141:~/TP4/*.log .
```

## Contexte

- On a une piste d'athlétisme avec une course de tortues
- Piste d'athlétisme
  - 35 couloirs
  - 1 couloir est fragmentée en 254 cellules
- Course de tortues
  - Une tortue peut au plus avancer de 2 cellules entre chaque top
- **FLUX 1** - Évolution de la course décrite par le schéma : `(id, top, position, nbAvant, nbTour)`
    - **id** : *entier*, numéro de la tortue
    - **top** : *entier*, numéro d'observation de la tortue
    - **position** : *entier*, cellule courante de la tortue
    - **nbAvant** : *entier*, nombre de tortues devant cette tortue
    - **nbTour** : *entier*, nombre de tours effectués par la tortue
- **FLUX 2** - Affectation des tortues aux équipes décrite par le schéma : `(tortoise, team)`
  - **tortoise** : *entier*, numéro de la tortue
  - **team** : *string*, nom de l'équipe

## Ajout de nouvelles fonctionnalités Python

- Retranscrire les données du flux dans un fichier log
```bash
cd ~/TP4

# Écrit les résultats du flux
spark-submit /home/p1908025/TP4/tortoiseListener.py > res_tortoiseListener.log
```
- Surveiller les changments dans le fichier de log
```bash
cd ~/TP4

# Surveille le fichier de log
tail -f -n35 res_tortoiseListener.log
```