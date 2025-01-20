# LIFAP4 Conception et Developpement d'Applications
# TD MODULE IMAGE

Page de l'UE : http://licence-info.univ-lyon1.fr/LIFAP4



## Organisation de l'archive

- **bin** : ce dossier possède les 3 programmes **exemple**, **test** et **affichage** qu'on pourra exécuter grâce au makefile

- **data** : les images sauvées et lues seront placées dans le dossier data

- **doc** : contient image.doxy qui est le fichier de configuration de doxygen

- **obj** : ce dossier contient les fichiers objets 
 
- **src**    : les fichiers sources (.h et .cpp) sont placés dans le dossier src

- **makefile** : le fichier makefile permet de compiler les 3 programmes **exemple**, **test** et **affichage**

- **README** : README.md est un fichier texte expliquant l'organisation de l'archive, la librairie

  
## Objectif du code
Le code permet de générer et de sauvegarder des images mises par l'utilisateur.

## Comment utiliser le code
Il faut utiliser les éxécutables qui se trouvent dans le dossier bin donc les éxécutables **exemple**, **test** et **affichage**

Pour les éxécuter, il faut aller dans le dossier **bin** et écrire la commande ./l'éxécutablevoulu. (Par exemple ./test)

## Déroulement du développement de l'application
Le développement de l'application a commencé juste après le 1er TD. 

Tout d'abord, il fallait qu'on découvre le sujet tous ensemble afin de le comprendre, de savoir comment se répartir les tâches et de s'organiser niveau temps.

Avant de commencer la première séance, il fallait choisir notre éditeur de code, nous avons tous les 3 choisis Code:Blocks.

Lors de cette première séance, on a travaillé la partie 1 ensemble, le début de cette partie était plus de la traduction en C++ qu'autre chose et une correction de bugs dans les fonctions d'entrées/sorties. Pas trop problématique.

Ensuite, à partir de là on a travaillé chacun de notre côté et regrouper/ajouter ce qu'on faisait dans un Git privé.

Enfin lorsque tout s'est finalisé, il fallait s'occuper de l'archive dans laquelle on allait mettre tous les fichiers, il fallait donc l'organiser.

**Voici les difficultés rencontrées et solutions lors de chaque partie :**

**Dans la partie 2**, le problème était surtout qu'on arrivait pas à faire marcher Valgrind. Donc on a installé une machine virtuelle au lieu de passer par WSL et cela fonctionnait. 

Pas de difficulté particulière en dehors de la mise en marche de Valgrind. La machine virtuelle utilisée est Virtualbox avec distribution Linux Ubuntu.

**Dans la partie 3**, les problèmes venaient avant tout de la compréhension de SDL étant une bibliothèque qu'on ne maîtrisait pas vraiment et de la différence entre surface et texture.

## Contributeurs et contacts
**GROS Loric : loric.gros@etu.univ-lyon1.fr p1908872**

**TANG Kévin : kevin.tang@etu.univ-lyon1.fr p1501263**
 
**TRAN David : david.tran@etu.univ-lyon1.fr p1911682**


