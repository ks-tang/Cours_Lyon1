# Arbres autoéquilibrants : AVL

Les arbres binaires de recherche sont une structure de données pour répondre à
des problèmes de dictionnaire, et permettent de maintenir un ensemble dynamique
de valeurs trié. En terme de complexité, ils ont un comportement du même ordre
que celui des skip-list. Ces arbres sont en particulier utilisés dans la plupart
des implémentation de la librairie standard en C++ pour les conteneurs `set` et
`map`.

Les arbres binaires de recherche ne sont efficaces que s'ils sont équilibrés.
Malheureusement, lors d'une séquence d'insertion de valeurs déjà triées, l'arbre
obtenu par simple insertion se retrouve très déséquilibré, et offre finalement
des complexités similaires à celles d'une liste chaînée. Plusieurs stratégies
ont donc été élaborées pour maintenir les arbres équilibrés au fur et à mesure
des insertions. Les plus célèbres sont les AVL et les arbres Rouge-Noirs.

Votre travail consiste à partir de l'implémentation des arbres binaires de
recherche vue en cours, et à rajouter la stratégie d'autoéquilibrage des AVL.

## Déroulement du TP

### Récupération du code de base et compilation

Clonez ce dépôt comme d'habitude, puis rendez vous dans le répertoire `Code`. En
plus du Makefile fourni habituellement, un fichier premake4.lua vous permet de
générer des projets codeblocks pour compiler le code. Sous linux en salle TP,
premake est installé, utilisez

```
premake4 codeblocks
```

pour générer un fichier projet codeblocks. Sous windows la procédure n'est pas
testée. Il vous faudra a priori [télécharger
premake 4](https://premake.github.io/download.html) et l'extraire dans le
répertoire du dépôt, puis lancer le script `premake.bat` pour générer un fichier
codeblocks.

### Calcul des hauteurs

Le rééquilibrage des AVL cherche à minimiser la hauteurs des nœuds (le nombre de
nœuds sur le plus long chemin depuis le nœud jusqu'à une feuille). Commencez
donc par rajouter un champ `hauteur` à la structure `Noeud`. Modifiez ensuite
la fonction d'insertion pour vous assurer qu'au fur et à mesure des insertions,
les hauteurs restent valides.

Il vous sera aussi très utile de créer une fonction auxiliaire `hauteur_noeud`
qui prend en paramètre l'adresse d'un nœud, et vérifie si le noeud est
`nullptr`. Si tel est le cas cette fonction renverra `0`, sinon elle renverra la
hauteur stockée dans le nœud. Cette fonction vous permettra ainsi de gérer
facilement tous les cas d'enfants manquants, car vous pourrez sans distinction
appeler la fonction sur les deux enfants d'un nœud sans vous soucier de savoir
s'ils existent ou non.

### Rotation

Implémentez ensuite les deux fonctions de rotation : `rotation_gauche` et
`rotation_droite`. Ces fonction n'ont pas besoin d'être des méthodes des classes
`Arbre` ou `Noeud`. La fonction rotation prend en paramètre l'adresse du nœud
qui est à la racine de la rotation, réalise la rotation, et renvoie ensuite
l'adresse du noeud qui finit à la racine après la rotation.

Assurez vous que les hauteurs sont correctement recalculées après chaque
rotation. Il n'est pas nécessaire de recalculer intégralement les hauteurs en
descendant dans les arbres, mais simplement de recalculer les hauteurs des nœuds
qui ont changé d'enfants, dans le bon ordre.

Dans un premier temps, vous pouvez rajouter des méthodes dans la classe arbre
pour réaliser des rotations sur la racine, et vous assurer que votre
implémentation est correcte et que votre arbre ne perd pas de branches.

### Application des rotations

Muni des rotations il est désormais possible de rééquilibrer les arbres. Dans la
fonction d'insertion, après avoir réalisé l'appel récursif pour insérer la
valeur donnée dans l'un des enfants. En considérant les hauteurs de ses enfants
après l'insertion, un nœud peut calculer la différence entre la hauteur de
gauche et celle de droite, puis déterminer s'il est en déséquilibre. Lorsqu'un
déséquilibre est détecté, il est alors nécessaire de déterminer quel cas de
rééquilibrage est à appliquer parmis les 4 vus en TD. Une fois le cas déterminé,
appliquez les rotations.

Dans le fichier de test vérifiez que l'insertion d'une séquence de nombre triés
aboutit à un arbre qui reste équilibré, et ne perd aucune valeur en cours de
route. Bravo, vos AVL sont fonctionnels.

## Rappel sur le principe des AVL

Le principe de l'équilibrage d'un arbre binaire de recherche consiste à faire en
sorte de maîtriser la hauteur de l'arbre, car cette hauteur correspond à la
complexité du pire des cas lors d'une recherche ou d'une insertion. Les AVL sont
une stratégie pour maîtriser cette hauteur. Cette stratégie interdit qu'au
niveau d'un nœud de l'arbre, la différence de hauteur entre l'enfant gauche et
l'enfant droit soit de plus de 1 (en valeur absolue). Lorsque le déséquilibre
dépasse ce seuil, des rotations sont appliquées.

### Opération de rotation

La rotation consiste à réarranger localement la hauteur de l'arbre au niveau
d'un nœud pour déplacer une partie de ses nœuds de la droite vers la gauche ou
inversement. L'intérêt de cette modification est qu'elle préserve la propriété
d'arbre binaire de recherche : les descendant gauches d'un nœud sont plus petits
que le nœud, et les descendants droits sont plus grands que le nœud. Ainsi un
arbre binaire de rehcerche reste un arbre binaire de recherche après n'importe
quelle rotation appliquée sur n'importe quel nœud.

Le principe des rotation est de réarranger les nœuds selon le schéma suivant.

#### Rotation droite

![rotation gauche](Sujet/rotation_gauche.png)

#### Rotation gauche

![rotation droite](Sujet/rotation_droite.png)

### Cas de rééquilibrage

En calculant le déséquilibre comme la hauteur de gauche moins celle de droite,
on peut montrer qu'en cas de déséquilibre 2 ou -2, on tombe nécessairement dans
l'un des quatre cas suivants, et il suffit d'appliquer les rotations associées
pour revenir à un déséquilibre acceptable.

#### Déséquilibre à gauche de l'enfant gauche

![gauche-gauche](Sujet/gauche_gauche.png)

#### Déséquilibre à droite de l'enfant gauche

![gauche-droite](Sujet/gauche_droite.png)

#### Déséquilibre à droite de l'enfant droit

![droite-droite](Sujet/droite_droite.png)

#### Déséquilibre à gauche de l'enfant droit

![droite-gauche](Sujet/droite_gauche.png)

### Complexité finale

En maîtrisant le déséquilibre de l'arbre, les AVL s'assurent que dans le
meilleur comme dans le pire des cas, la hauteur d'un AVL contenant n nœuds
est de l'ordre de $`\log{n}`$. Les opérations d'insertion et de recherche auront
donc cette complexité dans le pire des cas. Pour la recherche, la complexité
devient constante dans le meilleur cas, car la valeur cherchée peut être à la
racine de l'arbre. Pour l'insertion, étant donné qu'elle ajoute une feuille, et
descend donc l'arbre jusqu'en bas, elle est donc toujours logarithmique.
