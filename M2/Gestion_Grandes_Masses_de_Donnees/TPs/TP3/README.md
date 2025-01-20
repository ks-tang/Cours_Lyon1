# GGMD - TP3 : Big Data sur données astronomiques : Apache Spark

### Sujet du TP
[Lien vers sujet TP3 - Apache Spark](https://forge.univ-lyon1.fr/ggmd/tp-spark-2023-etudiants)

### Liens utiles
- [Sujet CM1 Partie BDR](https://perso.univ-lyon1.fr/nicolas.lumineau/ue/ggmd/cm/GGMD-CM-PartieBDR.pdf)

## Informations VM

- Fichier `p1908025.pem` sur Tomuss à mettre dans le répertoire home (renommé `ggmd-spark-tp3.pem`).
- Mettre les droits 600 avec *chmod*.
- Adresse IP sur Tomuss : `192.168.76.141`
- Se connecter en SSH avec cette commande :
```
ssh -i .ssh/ggmd-spark-tp3.pem p1908025@192.168.76.141
```

## HDFS

- Lister le contenu du répertoire `/user/p1234567`
```
hdfs dfs -ls /user/p1908025
```
- Copier le présent énoncé (i.e. README.md de votre machine vers master)
```
scp -i ~/.ssh/ggmd-spark-tp3.pem README.md p1908025@192.168.76.141:
```
- Copier ensuite ce fichier README.md de votre compte sur master vers le répertoire `/user/p1234567` dans le HDFS
```
hdfs dfs -copyFromLocal README.md /user/p1908025
```
- Vérifier que le fichier a bien été copié en listant le contenu du répertoire, puis en affichant le contenu du fichier README.md après sa copie dans HDFS
```
hdfs dfs -cat /user/p1908025/README.md
```

## Premiers pas avec Spark

- Suivre le tuto sur master jusqu'à *Caching* : https://spark.apache.org/docs/2.2.0/quick-start.html
```
textFile = spark.read.text("README.md")
```
- Suivre le tuto avec le fichier sur HDFS
```
textFile = spark.read.text("hdfs:///user/p1908025/README.md")
```
- Copier le fichier Python (e.g. python/SparkTPApp1.py) sur master
```
scp -i ~/.ssh/ggmd-spark-tp3.pem python/SparkTPApp1.py p1908025@192.168.76.141:
```
- Lancer l'application
```
spark-submit SparkTPApp1.py
```

## Fichiers CSV et correspondance attribut - index

- Fonction utilitaire dans `TP3/tp-spark-2023-etudiants/samples/PetaSkySchema.py`
- Sur master, dans le dossier `samples/`, lancer la commande
```
python PetaSkySchema.py
```

## Lecture d'un fichier CSV et compte d'occurrences

- Copier les répertoires en local vers master
```
scp -ri ~/.ssh/ggmd-spark-tp3.pem python p1908025@192.168.76.141:
```
