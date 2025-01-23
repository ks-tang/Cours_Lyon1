# -*- coding: utf-8 -*-
from pyspark import SparkContext
from pyspark.streaming import StreamingContext
import sys
import json
import pprint
 	
compte = "Nom_Compte"
tailleParcours = 254

#########################################################################
#Question 2 : Conversion JSON
def convert_to_json(record):
    return {
        "id": record[1],
        "top": record[2],
        "position": record[3],
        "nbAvant": record[4],
        "nbTour": record[5],
		"distance": int(record[5])*tailleParcours + int(record[3]) #Question 3 : Distance parcourue
    }

#Question 4 : Podium



########################################################################


if __name__ == "__main__":

	# variables
	appName = "TortoiseListener_GrpXX"
	myTortoiseID = 0
	sourceIP = "192.168.76.188"
	sourcePort = 9001
	window = 10    #fenêtre de 10 secondes

	# creation des contextes
	sc = SparkContext("local[*]", appName)
	ssc = StreamingContext(sc, window)

	# récupération des données
	dstream = ssc.socketTextStream(sourceIP, sourcePort)
	
	# transformations des données
	tortoises = dstream.map(lambda l : list(map(lambda c: c.strip(), l.split(";"))))

	######################################################################

	# Question 1 : Filtrage
	tortoises = tortoises.filter(lambda x: len(x)>1 and x[1] == "4")

	#Question 2 : Conversion JSON
	json_data = tortoises.map(convert_to_json)
	json_data.pprint()

	#Question 4 : Podium
	

	#######################################################################
	
	# affichage des données
	#tortoises.pprint()

    # lancement
	ssc.start()             
	ssc.awaitTermination()   
	

