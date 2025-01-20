Projet Snake

par le Groupe Korew :
P1501263 Kévin TANG 
P1911682 David TRAN
P1908872 Loric GROS

Disponible sur GitLab à l'adresse suivante :
https://forge.univ-lyon1.fr/p1908872/snake
Project ID : 18413

Utilisation :
Une fois dans le répertoire "snake"
tapez dans la console : "make clean"
puis : "make"
et enfin pour lancer le jeu : "bin/snake"
A ce moment, vous pourrez choisir le mode de jeu (1/2 joueurs) en tapant 1 ou 2.


Règles du jeu :
On incarne un serpent qui mange tout sur son passage.
Le but est d'obtenir le score le plus élevé possible en mangeant de délicieuses pommes.
Il existe d'autres objets qui peuvent l'aider (ou pas) sur son chemin :
un poison qui le fait mourir directement,
un rétrécisseur qui fait diminuer la taille (être trop petit n'est pas une bonne idée non plus)
et un ralentisseur qui ralentie la vitesse du serpent.
Attention aussi de ne pas foncer sur une partie de son corps, se manger soi-même ça fait très mal !
Et enfin, en dehors du terrain c'est le vide ! Attention à ne pas tomber.

En mode 2 joueurs, le but est d'obtenir le score le plus élevé face à votre adversaire.

Commandes :
Pour que le serpent puisse se remplir la panse,
vous devrez le diriger avec les touches :
Z (haut)
Q (gauche)
S (bas)
D (droite)
En mode 2 joueurs, le deuxième joueur utilise les flèches directionnelles.

Organisation de l'archive :
un fichier makefile
un répertoire bin qui contient l'exécutable
un répertoire data qui contient les fichiers images nécessaires au jeu
un répertoire doc qui contient la documentation doxygen, les diagrammes des classes et de Gantt et notre powerpoint de présentation
un répertoire obj qui contient les fichiers objets
un répertoire src qui contient le code source du jeu
