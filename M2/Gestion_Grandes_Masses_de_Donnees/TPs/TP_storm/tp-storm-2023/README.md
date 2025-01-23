

# GGMD - Traitement de flux via *Apache STORM*

L'objectif de ce TP est de vous familiariser avec les approches 'workflows' de traitement des flux via les topologies Apache STORM et de vous sensibiliser aux problèmes de congestions d'opérateurs qui peuvent apparaître en fonction de l'évolution du débit du flux en entrée.


## Préliminaire

### Organisation du TP
Ce TP se déroule en trois temps: 

Le premier temps consiste à prendre en main Apache STORM. 
Pour cela, vous pourrez exécuter la requête continue qui vous est proposée *via* la topologie *TopologyT1* qui vous est fournie.

Une fois cette prise en main faite, vous allez pouvoir créer vos propres requêtes.
Il s'agit de la partie sur la course des tortues.
L'objectif est de voir différents types d'opérateurs avec ou sans état et avec ou sans fenêtrage. Le débit du flux en entrée restera normalement gérable pour votre application.

La dernière partie du TP, s'intéresse au calibrage d'Apache STORM pour retarder la congestion éventuelle de certains opérateurs du fait de l'augmentation du débit du flux en entrée. 
Il s'agit de la partie sur la course de lièvres.

L'objectif est de voir comment il est possible de configurer Apache STORM pour modifier le degré de parallélisme de certains opérateurs ainsi que l'utilisation de ressources supplémentaires.


Pour réaliser ce TP, vous avez besoin de récupérer sur TOMUSS:

- l'adresse IP de votre VM principale : *STORM_IP_Princ*
- l'adresse IP de vos VM secondaire : *STORM_IP_Sec*
- votre numéro de dossard : *STORM_Dossard*

Vous aurez également à cloner le projet *tp-storm-2023* sur la forge (https://forge.univ-lyon1.fr/ggmd/tp-storm-2023.git) pour récupérer votre première topologie qui se connecte au flux d'entrée et... qui regarde les tuples passer. 


### Recommandation
Le déploiement de vos topologies sur le cluster STORM peut prendre du temps.
La gestion des logs distribués et le caractère peu ergonomique de l'interface web peuvent être un frein au débugage de votre code.
Il est fortement recommander de regrouper vos méthodes au sein d'une classe (par exemple *TortoiseManager*) et d'effectuer des tests unitaires nécessaires pour valider votre code avant de le déployer. 


### Modalités de rendu
Pour ce TP, il ne vous est demandé de fournir un bref rapport sur lequel aparaîtra au moins les impressions écran de l'interface *Storm ui* avec apparant : 
 - le graphe de visualisation 
 - l'url de la page
Ce rapport devra être déposer sur TOMUSS avant 23h59 le *03/12/2023* .



## Partie 1 : Prise en main d'Apache STORM

### Brefs rappels sur Apache STORM
Il vous est rappelé ci-dessous quelques éléments/concepts importants pour comprendre le fonctionnement d'une application reposant sur Apache STORM.

La requête continue que vous souhaitez mettre en place pour interroger un flux est représentée par une *topologie*. Cette topologie est un *workflow* d'opérateurs qui vont s'enchaîner pour générer le flux de sortie de votre topologie.
Ces opérateurs sont appelés *spout* quand ils servent de connecteur aux flux d'entrée et *bolt* quand ils servent à implémenter un traitement. La coordination de la ou des topologies se fait grâce à un *nimbus* qu'il faut voir comme un *master*. Ce nimbus gère les allocations des opérateurs sur les noeuds de traitements appelés *supervisors*. Pour gérer la coordination, le master a besoin de monitorer ce qui se passe au niveau des supervisors, pour cela il exploite les informations issues de *zookeeper*. Vous, en tant qu'utilisateur/développeur, vous avez besoin de visualiser l'activité de votre application, pour cela, Apache STORM propose une interface web appelée *ui*.

Ainsi, pour lancer une application Apache STORM, vous avez besoin de lancer :
- une instance de **zookeper** pour le monitoring. (potentiellement déjà fait)
- une instance de **ui** pour la visualisation. (potentiellement déjà fait)
- une instance de **nimbus** pour la coordination
- une instance de **supervisor** pour l'execution
- une instance **topologie** comprenant un ou plusieurs *spouts* et *bolts* qui exprime une requête


### Mise en place d'une topologie

#### Environnement
Vous trouverez la documentation sur Apache STORM à l'adresse: 
[https://storm.apache.org/releases/2.5.0/Tutorial.html](https://storm.apache.org/releases/2.5.0/Tutorial.html)

Dans ce TP vous disposez d'une VM sur le cloud du département dont l'adresse IP qui vous a été attribué en début de séance et spécifiée sur TOMUSS dans la cellule **STORM_IP_Princ**.

Pour vous connecter à votre VM vous utiliserez une connexion *ssh* avec la clé fournies sur TOMUSS :

`ssh -i <keyPath>/pedabdcloud ubuntu@192.168.***.***`

Comme il va être nécessaire de lancer plusieurs applications, il est conseillé d'être méthodique dans votre gestion des terminaux.


Dans le répertoire */home/ubuntu/apache-zookeeper-3.8.3-bin/* de votre VM, vous avez le répertoire *bin* contenant les fichiers de l'application de monitoring ZOOKEEPER.


Dans le répertoire */home/ubuntu/apache-storm-2.5.0/bin/* de votre VM, vous avez les fichiers de l'application de Apache STORM.

Dans le répertoire */home/ubuntu/apache-storm-2.5.0/logs/workers-artifacts/xxxxx/6700/* de votre VM, vous avez les fichiers logs contenant les traces d'exécution de votre topologie xxxxx.

En local sur votre machine, vous pouvez cloner le projet tp-storm-2023 (https://forge.univ-lyon1.fr/ggmd/tp-storm-2023.git)

Le *pom.xml* de votre projet est configuré pour générer une topologie STORM dans le programme *stormTP-0.1-jar-with-dependencies.jar* que vous trouverez dans le répertoire *target_. Après toute modification de vos classes définissant votre topologie, il sera nécessaire de générer le *stormTP-0.1-jar-with-dependencies.jar* via la commande *'mvn clean complie assembly:single'* puis de transférer le .jar générée sur votre VM (via *scp*).  


#### Paramètres de configuration


##### Ports et IP multicast
Comme le montre la Figure 1, pour pouvoir configurer votre VM, vous avez besoin:
- de l'adresse IP de votre VM
- de l'adresse IP multicast d'émission du flux de votre course. Il s'agit de l'adresse :
	- 224.0.0.1 sur le port 9001, pour le flux de tortues (flux n°1)
	- 224.0.0.2 sur le port 9002, pour le flux d'équipes  (flux n°2)
	- 224.0.0.3 sur le port 9003, pour le flux de lièvres (flux n°3) 



##### STORM
En ce qui concerne la configuration de STORM, elle est spécifiée dans le fichier *./lib/apache-storm-2.0.5/conf/storm.yaml*.\\
La documentation est disponible ici : [http://storm.apache.org/releases/2.5.0/Setting-up-a-Storm-cluster.html](http://storm.apache.org/releases/2.5.0/Setting-up-a-Storm-cluster.html)


#### Lancement des applications


##### Lancement de STORM
- Dans le répertoire *./lib/zookeeper-3.3.6/bin*, exécuter:
	 `./zkServer.sh start`

Pour vous assurer que le serveur est bien lancé, vous pouvez exécuter `./zkCli.sh -server 127.0.0.1:2181`

- Dans le répertoire *./lib/apache-storm-1.0.2/bin*,
	- pour lancer votre master Nimbus, exécuter:
		`./storm nimbus`

	- pour lancer votre interface de monitoring de Storm sur la page **http://<votreIP>:8080**, exécuter:
	 `./storm ui`

	- pour lancer un noeud de traitement sur votre VM, exécuter :
	 `./storm supervisor`


##### Lancement de votre topologie

Comme indiqué précédemment, vous avez une topologie qui vous est proposée dans le fichier */home/ubuntu/stormTP-0.1-jar-with-dependencies.jar*. Cette topologie est composée de 3 opérateurs :

- *StreamSimSpout* qui écoute le port 9001 sur votre IP multicast du flux d'entrée et bufferise les messages UDP reçus.
- *TestStatelessBolt* qui ne fait que transmettre le message reçu à l'opérateur suivant.
- *ExitBolt* qui émet à l'adresse IP multicast du flux de sortie sur le port 9002 les messages reçus.



Pour lancer cette topologie, exécuter dans le répertoire *apache-storm-2.5.0/bin/*:
	`./storm jar /home/ubuntu/jars/stormTP-0.1-jar-with-dependencies.jar`
	`	stormTP.topology.TopologyT1 <X>`

où *<X>* correspond au nouméro de flux écouté (1: les tortues, 2: les équipes, 3 les lièvres). 

**Remarque** : Vous disposez dans le package *stormTP.operator.test* de quatre exemples de classes implémentant respectivement un opérateur *stateless*,  *stateful*, *stateless* avec fenêtrage, *stateful* avec fenêtrage. 

##### Lancement du monitoring
Pour vous assurer que tout se passe correctement, vous disposez l'interface *ui* sur le port 8080. a noter qu'il est nécessaire de rafraichir la page pour avoir les informations et qu'il est nécessaire souvent d'attendre un peu pour que les données soient actualisées)

Sur la page d'accueil et dans la section "Topology summary", vous devez avoir *topoT1* qui apparaît. En cliquant sur *topoT1*, vous accédez à l'interface dédiée à cette topologie.



**Bilan**
A ce niveau du TP, vous devez pourvoir voir:
- le trafic entre les trois opérateurs (*MasterInputStreamSpout*, *NoFilterBolt*, *ExitBolt*) de votre topologie via l'interface ui de STORM





## Partie 2 : Course des tortues

Pour cette partie du TP, nous considérons une pseudo piste d'athlétisme. Cette piste est composée de couloirs fragmentés en cellule. Chaque couloir est composée de 254 cellules numérotées. Cette piste est le terrain d'une course des tortues. Une tortue ne peut que rester sur la cellule où elle se trouve ou avancer sur la cellule suivante.
L'évolution de la course est décrite par un flux de données dont le schéma est (id, top, position, nbAvant, nbApres, nbTotal) avec:
- **id** un entier correspondant au dossard de la tortue qui l'identifie,
- **top** un entier qui indique le numéro d'observation des tortues sur la piste,
- **cellule** un entier qui correspond à la cellule courante où se trouve la tortue (Attention, la position ne permet pas de déterminer le classement de la tortue, car la piste est circulaire et qu'une tortue peut avoir au moins un tour d'avance), 
- **nbDevant** un entier qui indique le nombre de tortues se trouvant devant la tortue dans le classement,
- **tour** indique le nombre de tour de la tortue courante.


**Remarque importante**: pour ne pas gaspiller les ressources (CPU, RAM) de votre VM, et sauf contre indication, il sera important de stopper les topologies précédentes avant de lancer une nouvelle topologie (cf bouton KILL sur l'interface UI de STORM) : Mettre 0 comme valeur dans le dialogue et valider pour arrêter la topologie immédiatement.

A noter que vous pouvez également exécuter depuis la console `./storm kill xxx` où xxx correspond au nom de votre topologie (e.g., topoT1).


Pour le développement de votre tolpologie, vous pourrez effectué `mvn clean compile assembly:single` pour générer votre *.jar* qu'il vous restera à déposer sur votre VM.


#### Filtrer sa tortue
Il s'agit ici d'implémenter un opérateur *stateless*.
Définir un bolt, nommé "MyTortoiseBolt" qui récupère dans le flux la tortue qui vous a été attribuée en début de séance. Les tuples retournés par ce bolt ont pour schéma (id, top, nom, position, nbAvant, nbApres, nbTotal). Ce qui correspond au schéma en entrée augmenté du nom de la tortue (correspondant à nomBinôme1-nomBinôme2) qui vous a été attribuée.

Par exemple, si vous avez le dossard 1 et que vous êtes le binôme 'Yves Atrovite' et 'Ella Paltan',  à partir de l'objet JSON reçu:

`{ tortoises:[ `
`{"id":0,"top":23,"cellule":4,"nbDevant":8,"nbDerriere":1,"tour":0}, `
`{"id":1,"top":23,"cellule":11,"nbDevant":4,"nbDerriere":4,"tour":0}, `
`{"id":2,"top":23,"cellule":15,"nbDevant":1,"nbDerriere":7,"tour":0}, `
`{"id":3,"top":23,"cellule":5,"nbDevant":7,"nbDerriere":2,"tour":0},  `
`{"id":4,"top":23,"cellule":11,"nbDevant":4,"nbDerriere":4,"tour":0}, ` 
`{"id":5,"top":23,"cellule":14,"nbDevant":3,"nbDerriere":6,"tour":0}, `
`{"id":6,"top":23,"cellule":24,"nbDevant":0,"nbDerriere":9,"tour":0},`
`{"id":7,"top":23,"cellule":8,"nbDevant":6,"nbDerriere":3,"tour":0},  `
`{"id":8,"top":23,"cellule":15,"nbDevant":1,"nbDerriere":7,"tour":0}, `
`{"id":9,"top":23,"cellule":1,"nbDevant":9,"nbDerriere":0,"tour":0}`
` ] }`

vous devez produire l'objet JSON:
`{"id":1,"top":23, "nom":"Paltan-Atrovite", "cellule":11, "nbDevant":4, "tour":0}`

A partir de *ExitBolt*, créer le bolt *Exit2Bolt* qui prend en entrée des tuples de schéma (id, top, nom, cellule, nbDevant, tour) et qui produit en sortie un tuple de schéma (json) dont la valeur retournée correspond l'objet JSON attendu.

Définir la topologie TopologyT2, qui permettra de tester votre bolt "MyTortoiseBolt" avec *MasterInputStreamSpout*  et *Exit2Bolt*. 

Tester votre topologie TopologyT2 (après avoir arrêté votre topologie TopologyT1).



#### Calcul du rang
Il s'agit ici d'implémenter un opérateur *stateless*.
Définir un bolt, nommé "GiveRankBolt" qui détermine le classement de votre tortue sur la piste. Les tuples retournés par ce bolt ont pour schéma (id, top, nom, rang). L'*id* correspond à l'identifiant de votre tortue, le *top* correspond au top d'observation de votre tortue, le *nom* correspond au nom de votre tortue et le *rang* correspond à la chaîne de caractère indiquant le rang de la tortue. En cas d'égalité, le rang des tortues *exae quo* sera suffixé par le mot 'ex'.

A partir de *ExitBolt*, créer le bolt *Exit3Bolt* qui prend en entrée des tuples de schéma (id, top, nom, rang, nbTotal) et qui produit en sortie un tuple de schéma (json) dont la valeur retournée correspond l'objet JSON attendu.

Ainsi pour le tuple reçu :
`(1, 5, 'Toto',2 ,0 ,9)`,
vous devrez générer un tuple :
`(1, 5, 'Toto', '1')`. 

Pour le tuple reçu :
`(1, 10, 'Toto', 6, 2, 6)`, 
vous devrez générer un tuple : 
`(1, 10, 'Toto', '3ex')`.


Définir la topologie TopologyT3, qui permettra de tester votre bolt "GiveRankBolt" avec *MasterInputStreamSpout*, *MyTortoiseBolt*  et *Exit3Bolt*. 

Tester votre topologie TopologyT3 (après avoir arrêté votre topologie TopologyT2).


#### Affectation des points bonus
Il s'agit ici d'implémenter un opérateur *stateful*.
Définir un bolt, nommé "ComputeBonusBolt" qui calcule le nombre de points bonus cumulés par votre tortue. Les tuples retournés par ce bolt ont pour schéma (id, top, nom, score). L'affectation des points bonus se fait le de la manière suivante: tous les 15 tops, le classement de la tortues est transformé en point correspondant au nombre total de participants moins le rang dans le classement. Ainsi, pour 10 participants, le ou les premiers auront 9 points supplémentaires, le ou les seconds auront 8 points supplémentaires et ainsi de suite.

A partir de *ExitBolt*, créer le bolt *Exit4Bolt* qui prend en entrée des tuples de schéma (id, top, nom, points) et qui produit en sortie un tuple de schéma (json) dont la valeur retournée correspond l'objet JSON attendu.

Ainsi pour les tuples reçus :
`{(1, 15, 'Toto', '1', 10), (1, 30, 'Toto', '3ex', 10), (1, 45, 'Toto', '2', 10), (1, 60, 'Toto', '3', 10)}` 
vous devrez générer les tuples :
`(1, 15, 'Toto', 9), (1, 30, 'Toto', 15), (1, 45, 'Toto', 23) puis (1, 60, 'Toto', 30)`


Définir la topologie TopologyT4, qui permettra de tester votre bolt "ComputeBonusBolt" avec *MasterInputStreamSpout*, *MyTortoiseBolt*, *GiveRankBolt* et *Exit4Bolt*.

Tester votre topologie TopologyT4 (après avoir arrêté votre topologie TopologyT3).


#### Vitesse moyenne
Il s'agit ici d'implémenter un opérateur *stateless* avec fenêtrage.
Définir un bolt, nommé "SpeedBolt, qui détermine la vitesse moyenne de la tortue exprimée en cellule par top calculée sur 10 tops et ce, tous les 5 tuples reçus. Les tuples retournés par ce bolt ont pour schéma (id, nom, tops, vitesse), avec **tops** une chaîne de caractères de la forme "t<sub>i</sub>-t<sub>i+9</sub>" où t<sub>i</sub> et t<sub>i+9</sub> correspondent respectivement au premier et au dernier top considérés dans le calcul.

Ainsi pour les tuples reçus :
`{ (1,1,'Toto',1,0,9,0); (1,2,'Toto',1,0,9,0); (1,3,'Toto',1,0,9,0);(1,4,'Toto',2,0,9,0); (1,5,'Toto',3,0,9,0); (1,6,'Toto',3,0,9,0); (1,7,'Toto',3,0,9,0); (1,8,'Toto',4,0,9,0); (1,9,'Toto',4,0,9,0); (1,10,'Toto',4,0,9,0); (1,11,'Toto',4,0,9,0); (1,12,'Toto',5,0,9,0); (1,13,'Toto',5,0,9,0); (1,14,'Toto',5,0,9,0); (1,15,'Toto',6,0,9,0); }`

vous devez générer les tuples `(1, 'Toto', '1-10', 0.40)` puis `(1, 'Toto', '6-15', 0.3)`


A partir de *ExitBolt*, créer le bolt *Exit5Bolt* qui prend en entrée des tuples de schéma (id, top, nom, vitesse) et qui produit en sortie un tuple de schéma (json) dont la valeur retournée correspond l'objet JSON attendu.

Définir la topologie TopologyT5, qui permettra de tester votre bolt "SpeedBolt" avec *MasterInputStreamSpout* et *Exit5Bolt*. 

Tester votre topologie TopologyT5 (après avoir arrêté votre topologie TopologyT4).

#### Evolution du rang
Il s'agit ici d'implémenter un opérateur *stateful* avec fenêtrage.
Définir un bolt, nommé "RankEvolutionBolt", qui détermine l'évolution du rang ("En progression", "Constant" ou "En régression") de la tortue sur une fenêtre de 10 secondes.  Les tuples retournés par ce bolt ont pour schéma (id, nom, tops, evolution).
L'**id** correspond à l'identifiant de la tortue, le **nom** correspond au nom de votre tortue, le **tops** correspond à la concaténation du plus petit top observé dans la fenêtre avec le plus grand top observé dans la même fenêtre et le **evolution** correspond à la chaîne de caractères indiquant si la tortue est :

- en progression, pour traduire qu'il a gagné au moins une place dans le classement au bout de 30 secondes
- constant, pour traduire qu'il a la même place au bout de 30 secondes
- en régression,  pour traduire qu'il a perdu au moins une place dans le classement au bout de 30 secondes


Ainsi pour les tuples reçus pendant 30 secondes :
`{ (1,1,'Toto','1ex'); (1,2,'Toto','1'); (1,3,'Toto','1'); (1,4,'Toto','2'); (1,5,'Toto','1'); (1,6,'Toto','1') }`
vous devez générer un tuple `(1, 'Toto', '1-6', 'Constant')`;

A partir de *ExitBolt*, créer le bolt *Exit6Bolt* qui prend en entrée des tuples de schéma (id, top, nom, points) et qui produit en sortie un tuple de schéma (json) dont la valeur retournée correspond l'objet JSON attendu.

Définir la topologie TopologyT6, qui permettra de tester votre bolt "RankEvolutionBolt" avec *MasterInputStreamSpout*, *MyTortoiseBolt*, *GiveRankBolt* et *Exit6Bolt*. 

Tester votre topologie TopologyT6 (après avoir arrêté votre topologie TopologyT5).



## Partie 3 : Course des lièvres
Pour cette partie du TP, nous conservons le contexte de course mais cette fois les tortues sont remplacées par des lapins (Flux 3 : adresse 224.0.0.3 sur le port 9003). 
Dans ce contexte, les observations sont bien plus nombreuses et fréquentes.
Dans la partie précédente, les ressources étaient suffisantes pour absorber le débit du flux. Dans cette partie, ce ne sera plus le cas.
Certains opérateurs vont se retrouver dans un état de congestion, c'est-à-dire que le nombre de tuples à traiter est plus important que le nombre de tuples qu'il est en capacité de traiter.
Pour résoudre ce problème, vous allez dans un premier temps modifier le degré de parallélisme de l'opérateur congestionné pour avoir plusieurs *thread* et ainsi exécuter l'opérateur en parallèle.
Ensuite, vous évaluerez l'impact d'ajouter un *supervisor* supplémentaire. 

### Podium
Afin que les performances réseaux ne biaisent pas les tests que vous aurez à effectuer, vous utiliserez le spout *HareSpout* qui vous est fourni dans le package  *stormTP.operator*. Les objets JSON émis par ce spout sont quasi identiques à ceux émis par le *MasterInputStreamSpout* avec un nom en plus.


Nous allons dans un premier temps définir un bolt qui effectue une opération coûteuse.
Définir un bolt de type 'stateless', nommé "ComputePodiumBolt", qui pour chaque tuple reçu, détermine le podium correspondant (c'est-à-dire, les trois meilleurs rangs).
Dans le cas où plusieurs lièvres sont sur la même marche du podium, les noms s'afficheront triés par ordre alphabétique. 
Les tuples émis par ce bolt ont pour schéma (json) et représentent des objets JSON de la forme:

`{ "top":"long", "marcheP1":[{"nom":"string"}], "marcheP2":[{"nom":"string"}], "marcheP3":[{"nom":"string"}]}`

Par exemple, à partir de l'objet JSON reçu:
`{ "rabbits":`
`[ {"id":0,"top":123,"nom":"RogerRabbit","cellule":4,"nbDevant":8,"tour":0},`
` {"id":1,"top":123,"nom":"BugsBunny","cellule":11,"nbDevant":4,"tour":0},`
` {"id":2,"top":123,"nom":"Panpan","cellule":15,"nbDevant":1,"tour":0},`
` {"id":3,"top":123,"nom":"Caerbannog","cellule":5,"nbDevant":7,"tour":0},`
` {"id":4,"top":123,"nom":"Oswald","cellule":11,"nbDevant":4,"tour":0},`
` {"id":5,"top":123,"nom":"Jojo","cellule":14,"nbDevant":3,"tour":0},`
` {"id":6,"top":123,"nom":"Coco","cellule":248,"nbDevant":0,"tour":0},`
` {"id":7,"top":123,"nom":"JudyHopps","cellule":8,"nbDevant":6,"tour":0},`
` {"id":8,"top":123,"nom":"LapinBlanc","cellule":15,"nbDevant":1,"tour":0},`
` {"id":9,"top":123,"nom":"Basil","cellule":1,"nbDevant":9,"tour":0}`
` ] }`

vous devez produire l'objet JSON:
`{"top":"123","marcheP1":[{"nom":"Coco"}],"marcheP2":[{"nom":"LapinBlanc"},{"nom":"Panpan"}],"marcheP3":[{"nom":"Jojo"}] }`


Lancer la topologie TopologyE1 pour tester votre bolt "ComputePodiumBolt" avec *HareSpout* et *ExitInLogBolt*.

En regardant dans l'**ui** l'affichage de votre topologie, que remarquez-vous sur l'état du noeud correspondant au bolt "ComputePodiumBolt" (si vous ne voyez rien d'anormal... attendez un peu! Des 'failed' devraient apparaître).   

### Déphasage des tuples
Pour tester les performances de notre bolt "ComputePodiumBolt", nous allons calculer le nombre de tuples déphasés  (i.e. qui n'ont pas pu être traité dans le temps imparti). 
Pour cela, ajouter une instruction dans la méthode **fail** du spout pour pouvoir journaliser un message du type:

 `"[PodiumFail] Failure (msg: x ) after y seconds"`

avec x le numéro du message source de l'échec et y le temps en seconde depuis le lancement de la topologie. 

Vous pourrez récupérer l'information via un grep dans les différents fichiers worker.log des répertoires 6700, 6701, 6702 et 6703 dans logs/workers-artifacts/ttt où ttt correspond au nom de votre topologie.

Par la suite, nous partons du principe que la topologie est congestionné à partir de 10 échecs observés.


En combien de temps votre topologie est congestionnée?


### Gestion de la congestion

#### Modification du parallélisme
Dans un premier temps, nous allons modifier le parallélisme de l'opérateur "ComputePodiumBolt".
Modifier votre topologie TopologyE1, pour associer 4 workers et 4 Executors à votre opérateur.


Qu'observez-vous? 

#### Ajout d'un supervisor distant
Pour finir, configurer le storm du répertoire "forOthers" qui vous a été attribué comme VM secondaire (*"STORM_IP_Sec"* dans TOMUSS). 
Pour cela, modifier le conf/storm.yaml du slave et affecter le nom complet de votre VM (stormXXX.novalocal) au *nimbus.seeds*. Il est aussi nécessaire de modifier le */etc/hosts* du slave pour déclarer votre VM. Il sera également nécessaire de modifier le /etc/hosts de votre VM pour déclarer le slave.
Lancer un supervisor qui sera connecté à votre nimbus. Vérifier sur l'ui que le nouveau supervisor est bien répertorié dans la section "Summary supervior".
 
Modifier votre topologie *TopologyE1* pour qu'elle puissent s'exécuter sur 8 workers (et non plus 4).

Qu'observez-vous? 


=>=>=>=>=>=>F=>=>=>=>=>=>\

=>=>=>=>=>I=>=>=>=>=>=>=>=|=>=>=>=>FIN

=>=>N=>=>=>=>=>=>=>=>=>=>/

