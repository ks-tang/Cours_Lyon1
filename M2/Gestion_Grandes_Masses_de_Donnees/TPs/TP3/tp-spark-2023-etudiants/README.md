# GGMD - TP Big Data sur données astronomiques : Apache Spark

A noter qu'aucun rendu de ce TP n'est attendu mais que les notions vues dans ce TP seront évaluées par un QCM. 

## Partie 1 : Prise en main du framework Apache Spark


L'objectif de cette partie est de prendre en main le _framework_ [Spark](http://spark.apache.org).
Ce TP est à réaliser en Python (version 2.7).


### Jeu de données

Dans ce TP, on travaillera sur un jeu de données issue d'observations astronomiques issues du projet [PetaSky](http://com.isima.fr/Petasky).
On considèrera essentiellement deux relations: SOURCE et OBJECT.
SOURCE contient des données observationnelles.
OBJECT contient des informations sur les objets célestes associés aux observations.
Ces relations sont fournies sous forme de fichiers CSV compressés sans entête, dont les valeurs sont séparées par `,`.
Les valeurs nulles sont représentées par les quatre lettres `NULL` qui remplacent alors la valeur.
On dispose également de deux fichiers SQL contenant des informations de schéma.
Ces deux relations comportent de nombreux attributs.
Même si les attributs d'intérêt sont mentionnés dans le TP, il sera nécessaire de connaître leur position dans les lignes.
On préfèrera une méthode où les positions des attributs sont déterminées automatiquement à partir des fichiers schéma SQL.

Fichiers utiles:

- [Source.sql](samples/Source.sql)
- [Object.sql](samples/Object.sql)
- [source-sample](samples/source-sample)
- [object-sample](samples/object-sample)

### Accès au cluster Hadoop

Chacun dispose d'un compte sur le cluster Hadoop (sur la machine indiquée dans [tomuss](https://tomuss.univ-lyon1.fr), case `VM_TP3`.
Le login est votre login étudiant.

> Dans la suite du TP, il faut systématiquement remplacer:
>
> - `p1234567` par votre login étudiant.
> - `192.168.73.xxx` par l'adresse de la machine mentionnée su TOMUSS.

Il faut se connecter en utilisant la clé SSH fournie dans la cellule `Cle_SSH` de l'UE sur [tomuss](http://tomusss.univ-lyon1.fr).
Attention, il faut changer les droits pour que la clé ne soit lisible que par vous (en utilisant `chmod go-rwx fichier-cle-ssh`).

```shell
ssh -i fichier-cle-ssh p1234567@192.168.73.xxx
```

Pour copier un fichier sur la machine `192.168.73.xxx` on peut utiliser la commande suivante:

```shell
scp -i fichier-cle-ssh fichier-a-copier p1234567@192.168.73.xxx:
```

Pour éviter d'ajouter `-i fichier-cle-ssh`, il est possible d'utiliser ssh-agent via la commande suivante:

```shell
ssh-add fichier-cle-ssh
```

> Dans la suite du TP la machine `192.168.73.xxx` sera référencée en tant que `master`.

### HDFS

Avant de commencer à écrire des requêtes Spark, il est nécessaire de se familiariser avec les commandes d'accès au HDFS.

L'accès aux commandes permettant d'accéder au HDFS se fera sur la machine `master`

Lister le contenu d'un répertoire

```shell
hdfs dfs -ls /le/repertoire/a/lister
```

Copier un fichier vers HDFS

```shell
hdfs dfs -copyFromLocal fichier-a-copier /destination/dans/hdfs
```

Copier un fichier depuis HDFS

```shell
hdfs dfs -copyToLocal /fichier/dans/hdfs fichier-local
```

Lire le contenu d'un fichier depuis HDFS

```shell
hdfs dfs -cat /fichier/dans/hdfs
```

à combiner avec, par exemple `| less`

> Remarque: dans HDFS, les fichiers avec un nom relatifs sont traités dans HDFS comme s'il étaient référencés à partir du répertoire `/user/p1234567`

> Remarque: dans le système de fichier Linux, votre repertoire utilisateur est le la forme `/home/p1234567` alors que dans le système de fichier HDFS, il est de la forme `/user/p1234567`.

La commande suivante affiche toutes commandes disponibles pour interagir avec HDFS:

```shell
hdfs dfs
```

#### Exercice 1

- lister le contenu du répertoire `/user/p1234567`
- copier le présent énoncé (_i.e._ `README.md` de votre machine vers `master`)
- copier ensuite ce fichier `README.md` de votre compte sur `master` vers le répertoire `/user/p1234567` dans le HDFS
- vérifier que le fichier a bien été copié en listant le contenu du répertoire, puis en affichant le contenu du fichier `README.md` après sa copie dans HDFS

### Premiers pas avec Spark


Sur la machine `master`, réaliser le tutoriel [Quick start](http://spark.apache.org/docs/2.2.0/quick-start.html) jusqu'à la partie Caching inclue.
Le fichier `README.md` à utiliser est celui que vous avez copié dans le HDFS.
Attention, les résultats seront différents de ceux du tutoriel car le fichier n'est pas le même (c'est celui de l'énoncé du TP).

Copier le fichier `README.md` dans votre répertoire HDFS, puis reprendre le début du tutoriel en accédant cette fois si au fichier dans le HDFS (supprimer le fichier du répertoire Linux courant après l'avoir copié dans HDFS pour être sûr(e) de soi).
Pour indiquer à Spark d'accéder à un fichier dans HDFS au lieu d'un fichier dans le système de fichier standard, il suffit de précéder le nom du fichier de `hdfs://`.
On peut par exemple utiliser `hdfs:///user/p1234567/README.md`.

Remarques:

- Il ne faut pas mettre `./bin/` avant `pyspark`
- Il y a une erreur dans le tutoriel Python: pour `wordCounts`, il faut remplace `.as` par `.name`
- Ignorer les avertissements concernant "Error while looking for metadata directory" et "lineage"
- Le shell que vous utilisez se connecte par défaut sur le cluster Spark.

#### Application utilisant Spark

Si ce n'est pas déjà fait, créer un nouveau projet sur la [forge Lyon 1](https://forge.univ-lyon1.fr), cloner ce projet, puis dans le répertoire obtenu récupérez le projet du TP:

```shell
git pull https://forge.univ-lyon1.fr/ggmd/tp-spark-2023-etudiants.git
```


Sur votre machine, continuer le [tutoriel](http://spark.apache.org/docs/2.2.0/quick-start.html#self-contained-applications) avec les modifications suivantes:

- Remplacer le `"YOUR_SPARK_HOME/README.md"` par `"hdfs:///user/p1234567/README.md"`
- Copier le fichier Python (_e.g._ `python/SparkTPApp1.py`) sur `master`
  ```
  spark-submit /home/p1234567/SparkTPApp1.py
  ```

Attention, contrairement à ce qui est indiqué dans le code, il ne faut pas mettre `()` après `builder`.
Attention, il faut supprimer l'appel `.master(master)`.

#### Tester localement avant de déployer

Avant de déployer une tâche Spark pouvant prendre du temps sur le cluster, il est bon de pouvoir tester son fonctionnement local.

Le fichier `python/test/test_spark_sample.py` donne des exemples de tests unitaires. Le premier test nécessite de se trouver au sein du cluster pour l'exécuter. Pour lancer les tests, il faut avoir installé `pytest` (typiquement via `pip`). Il suffit ensuite de lancer `pytest` depuis le répertoire `python`.

- Sur votre machine les tests se lancent via `pytest` dans le répertoire `python` du projet. Les tests spark ne sont pas exécutés.
- Sur `master`, après avoir copié tout le répertoire `python` depuis votre machine, lancer `pytest --on-spark`, depuis le répertoire `python`. Tous les tests sont exécutés, y compris les tests spark.
- Le test spark nécessite que le fichier `samples/object-sample` soit présent dans HDFS, il afut donc créer le répertoire sample (`hdfs dfs -mkdir samples`) et le copier (`hdfs dfs -copyFromLocal object-sample samples/object-sample`) avant de lancer le test la première fois.

Quelques liens :

- [pytest](https://docs.pytest.org/en/latest/)
- [pytest-spark](https://pypi.org/project/pytest-spark/)

#### Exercice

Créer un test unitaire pour l'application `SparkTPApp1`.
Pour cela, il faut séparer la fonctionnalité de mise en place du contexte Spark du calcul fait sur les données en plaçant ce dernier dans une fonction ou une méthode séparée.

## Fichiers CSV et correspondance attribut - index

Avant de traiter les données d'astronomie, il est nécessaire de se doter de fonctions utilitaires qui vont simplifier la suite des opérations.
En particulier, il est utile de construire une liste d'attributs pour Source et Object, ainsi que de connaître la position de chacun des attributs.
Pour cela, on va lire le fichier SQL contenant le schéma, en utilisant les remarques suivantes:

- Le fichier contient un attribut par ligne.
- La première et la dernière ligne du fichier ne concernent pas les attributs.
- Dans une ligne, le premier mot est le nom de l'attribut.

### Exercice

Créer une classe Python `PetaSkySchema` contenant une fonction de lecture de schéma qui:

- prend un nom de fichier en argument
- lit le fichier
- produit la liste ordonnée des attributs

Ajouter à cet objet

- deux valeurs: la liste des attributs de Object et celle des attributs de Source
- pour chaque attribut utilisé dans le TP, une valeur donnant son index

> Ne pas passer trop de temps sur cet exercice: s'il ne fonctionne pas rapidement, déclarer directement les index des attributs d'intérêt en comptant les lignes à la main dans les fichiers SQL.

## Lecture d'un fichier CSV et compte d'occurrences

Pour la suite de ce TP, on utilisera plutôt les RDD que les DataFrames. Les Dataframes sont plus rapides et mieux typés (donc plus sûrs), et sont à préférer en temps normal. Les RDD permettent cependant de mieux de comprendre le traitement en flux via des transformations qui est sous-jacent à Spark.

Créer une application Spark `SparkTPApp2` qui lit depuis le HDFS un fichier csv issu de Source dont le nom est passé en ligne de commande, puis affiche pour chaque valeur d'`objectId` le nombre de ligne qui la contient.

Cela revient à la requête SQL suivante:

```sql
SELECT object_id, count(*)
FROM source
WHERE object_id IS NOT NULL
GROUP BY object_id
```

Écrire un test Spark pour vérifier votre fonction de calcul sur `samples/source-sample` avant de lancer l'application sur le cluster.

Modifier le programme pour prendre un deuxième argument optionnel qui indique un fichier HDFS dans lequel écrire le résultat (au format CSV).

Copier le fichier `/tp-data/Source/Source-001` dans un sous-répertoire `Source` de votre répertoire HDFS et tester en indiquant ce fichier à lire.

Vérifier ce qui est créé dans HDFS.
Est-ce ce à quoi on s'attend?
Expliquer le comportement de Spark.
Essayer de lancer le travail en passant en argument le répertoire HDFS `Source` au lieu du fichier `Source-001` directement, déviner et vérifier le comportement de Spark.
Ajouter le fichier `Source-002` dans le HDFS, également dans `Source`, puis relancer le job Spark et vérifier qu'on obtient plus de résultats.

Supprimer les fichiers résultats.

### Remarques

- Les valeurs `NULL` pour `object_id` ne doivent pas être prises en compte.
- Pour passer des arguments à une application lors de sa soumission avec `spark-submit`, il suffit de les ajouter à la fin de la commande.
- Attention `countByKey` ne fonctionne que pour de petites collections, mais la documentation de la fonction donne une indication pour les grandes collections via `map` et `reduceByKey`.



## Code mystère

L'objectif de cette partie est de tester votre capacité à comprendre un code non commenté.

Après avoir consulté le fichier `SparkApp3`:
- Exprimer en langage naturel ce que fait la fonctionnalité
- commenter le code 


## Partie 2 : Partitionnement

Dans cette partie on va pousser plus loin l'utilisation de Spark et HDFS en travaillant
sur un volume de données plus important. On souhaite en particulier pouvoir
rapidement sélectionner des sources correspondant à une zone du ciel proche d'un
certain point. Il s'agit donc d'une sorte d'index basé sur la position des
sources sur la sphère céleste. On s'appuiera pour cela sur les coordonnées
`DECL`
([déclinaison](<https://fr.wikipedia.org/wiki/D%C3%A9clinaison_(astronomie)>)) et
`RA` ([ascension droite](https://fr.wikipedia.org/wiki/Ascension_droite)). On
souhaite être capable de découper la sphère celeste en pseudo rectangles
correspondant à chacun à une paire d'intervalles: un pour `DECL` et un autre
pour `RA`. On créera ensuite un répertoire par rectangle et on rangera les
différentes observations (lignes dans les fichiers `Source`) dans le répertoire
correspondant à son rectangle. Les répertoires vont ainsi former une sorte
d'index: pour accéder à une observation à partir de ses coordonnées, on choisira
d'abord le bon rectangle, puis il restera a lire uniquement les observations de
ce rectangle au lieu de lire toutes les observations.


## Données

On commencera par tester le fonctionnement du code sur le _source-sample_.
Une fois celui-ci fonctionnel, on pourra tester sur le fichier `hdfs:///tp-data/Source/Source-001.csv`.

ATTENTION: tuez votre job si son temps d'exécution dépasse 15min (vous n'êtes pas tous seuls sur le cluster).

## Mise en oeuvre

Le travail à faire peut être découpé en plusieurs étapes:

1. Découper le ciel en _n_ bandes correspondant à un intervalle de `DECL`. Par
   exemple, supposons que les valeurs de `DECL` s'étalent de _-0.3_ à _0.7_ on
   peut découper la sphère en 20 bandes de d'épaisseur _0.05_ chacune. Pour
   cela, on attribue un numéro à chaque bande et on ajoute à chaque source un
   attribut `part_decl` contenant le numéro de la bande dans laquelle sera
   placée cette observation. Remarque: pour pouvoir attribuer un numéro de bande
   à une valeur de `DECL`, il faut connaître en plus les valeurs min et max de
   `DECL` ainsi que le nombre total de bandes désiré.
2. Redécouper chaque bande en rectangles (cases) correspondant à des intervalles de
   `RA`. Comme il y aura plus d'observations dans les bandes près de l'équateur
   que dans les bandes près des pôles, il est peu pertinent de fixer la
   taille de l'intervalle de manière identique sur toutes les bandes. On va à la
   place adapter cette taille pour chaque bande. Pour cela on fixe un nombre
   objectif _k_ d'observations par rectangle. On ajustera le nombre de
   rectangles selon le nombre d'observations comptées dans la bande. On
   supposera que les observations sont réparties de manière uniforme au sein
   d'une bande (mais on prendra bien en compte les valeurs min et max de `RA` au
   sein de la bande). Si _m_ est le nombre de rectangles dans une bande et _p_
   le nombre d'observations dans la bande complète, on veut que _k(m-1) ≤ p ≤
   km_. On ajoute alors à chaque observation un attribut `part_ra` indiquant le
   numéro du rectangle au sein de la bande.
   Remarque: on peut, au choix, placer les attributs directement dans le tableau de valeurs de chaque observation, ou bien construire une structure de la forme `((part_decl, part_ra), observation)` ce qui permet d'utiliser la paire `(part_decl, part_ra)` comme clé dans des opérations `xxxByKey`.
3. Sauvegarder le contenu de chaque rectangle dans un répertoire HDFS dont le
   nom comprendra le numéro de la bande (`part_decl`) et celui du rectangle dans
   la bande (`part_ra`). Par exemple, si `part_decl` vaut _3_ et `part_ra` vaut _6_, le répertoire pourrait s'appeler `indexed/3-6/`.

   Pour cela, on peut constituer un RDD pour chaque rectangle par filtrage, puis sauver ce RDD. On prendra soin de faire que le contenu des fichiers de ces répertoires soit au format csv comme pour les fichiers `Source` initiaux.
   Le défaut de cette approche est qu'il est nécessaire de filtrer la collection des sources pour _chaque_ fichier de sortie. Si cette approche est utilisée, il est important de lancer le partionnement sur une source de taille modérée, par exemple `hdfs:///tp-data/Source/Source-001.csv`, sinon le temps d'écriture sera trop long (plusieurs heures).


4. Créer et sauvegarder sur HDFS un RDD contenant pour chaque rectangle ses
   "coordonnées", c'est-à-dire les valeurs min et max de RA et DECL pour les
   observations de ce rectangle, ainsi que le nombre d'observations qui ont été
   placées concrètement dans ce rectangle.
5. Créer un programme qui effectue le partionnement et qui prendra en
   particulier en paramètre le répertoire / le fichier des sources à lire, un
   répertoire de sortie, le nombre de bandes _n_ et le nombre objectif _k_
   d'observations voulu par rectangle.
6. Après avoir effectué un partionnement, comparer le nombre d'observations dans
   les rectangles au nombre objectif _k_ et commenter sur la pertinence de la
   répartition.
