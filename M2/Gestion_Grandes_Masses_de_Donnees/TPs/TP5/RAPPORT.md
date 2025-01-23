# GGMD - TP5 : Traitement de flux via Apache STORM

### Sujet du TP
[Lien vers sujet TP5 - Apache STORM](README.md)

### Liens utiles
- [Sujet CM2](https://perso.univ-lyon1.fr/nicolas.lumineau/ue/ggmd/cm/GGMD-CM-PartieFlux.pdf)

## Informations VM

- Fichier `ggmd-etus.key` sur Tomuss à mettre dans le répertoire home/.ssh.
- Mettre les droits 600 avec *chmod*.
- Adresse IP principale sur Tomuss : `192.168.246.240`
- Adresse IP secondaire sur Tomuss : `192.168.246.245`
- Se connecter en SSH à la VM principale :
```
ssh -i .ssh/ggmd-etus.key ubuntu@192.168.246.240
```
- Se connecter en SSH à la VM secondaire :
```
ssh -i .ssh/ggmd-etus.key ubuntu@192.168.246.245
```
- Copier le fichier .jar de ma machine vers la VM
```bash
cd /mnt/c/Users/Cecilia/Documents/GITHUB/CECILIA/ggmd-2024/TP5

# Copie le fichier jar
scp -i ~/.ssh/ggmd-etus.key target/*.jar ubuntu@192.168.246.240:~/jars
```

## Partie 1 : Prise en main d'Apache STORM

- Compiler le projet
```
mvn clean compile assembly:single
```
- Envoyer le .jar sur le VM avec scp (cf. [Informations VM](#informations-vm))
- Lancer Zookeeper
```
cd /home/ubuntu/apache-zookeeper-3.8.3-bin/bin

./zkServer.sh start

./zkCli.sh -server 127.0.0.1:2181
```
- Lancer Apache Storm
```
cd /home/ubuntu/apache-storm-2.5.0/bin

./storm nimbus

./storm ui

./storm supervisor
```
- Lancer la topologie (1 : les tortues, 2 : les équipes, 3 : les lièvres)
```
cd /home/ubuntu/apache-storm-2.5.0/bin

./storm jar /home/ubuntu/jars/stormTP-0.1-jar-with-dependencies.jar stormTP.topology.TopologyT1 1
```
- URL de l'UI : http://192.168.246.240:8080/