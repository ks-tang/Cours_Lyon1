# -*- coding: utf-8 -*-
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
import sys
import json
import pprint

compte = "Nom_Compte"

if __name__ == "__main__":

    # variables
    appName = "TortoiseListener_Grp30"
    myTortoiseID = 4
    totalCells = 254
    sourceIP = "192.168.76.188"
    sourcePort = 9001
    window = 10    # fenêtre de 10 secondes

    # creation des contextes
    sc = SparkContext("local[*]", appName)
    ssc = StreamingContext(sc, window)

    # récupération des données
    dstream = ssc.socketTextStream(sourceIP, sourcePort)

    # transformations des données
    all_tortoises = dstream.map(lambda l: list(map(lambda c: c.strip(), l.split(";"))))

    # ------------------------------------------------------------------------------------ #

    # 1. filtrer les tuples qui concernent ma tortue
    my_tortoise = all_tortoises.filter(lambda line: len(line) > 1 and line[1] == str(myTortoiseID))

    # 2. convertir les données du flux en objet JSON
    all_tortoises = all_tortoises.filter(lambda line: len(line) > 1).map(lambda line: json.dumps({
        'id': int(line[1]),
        'top': int(line[2]),
        'position': int(line[3]),
        'nbAvant': int(line[4]),
        'nbTour': int(line[5]),
        # 3. calculer la distance parcourue par notre tortue
        'distance': int(line[5]) * totalCells + int(line[3])
    }))

    my_tortoise = all_tortoises.filter(lambda t: json.loads(t)['id'] == 4)

    # 4. retourner le podium individuel et temporaire
    sorted_tortoises = all_tortoises.transform(lambda at: at.sortBy(lambda t: json.loads(t)['distance'], ascending=False))

    top3_tortoises = sorted_tortoises.transform(lambda st: st.take(3))

        # Formater le résultat en JSON
#     podium_result = {'1er': [t['id'] for t in top3_tortoises],
#                     '2eme': [],
#                     '3eme': []}

    # ------------------------------------------------------------------------------------ #

    # affichage des données
    my_tortoise.pprint()
    top3_tortoises.pprint()

    # lancement
    ssc.start()
    ssc.awaitTermination()
