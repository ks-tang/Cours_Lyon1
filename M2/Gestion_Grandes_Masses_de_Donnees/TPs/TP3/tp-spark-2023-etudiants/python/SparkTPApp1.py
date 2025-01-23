# -*- coding: utf-8 -*-
# import sys

# args = sys.args

"""SimpleApp.py"""
from pyspark.sql import SparkSession

logFile = "hdfs:///user/p1908025/README.md"  # Should be some file on your system
spark:SparkSession = SparkSession.builder.appName("Simple Application").getOrCreate()
spark.sparkContext.setLogLevel("ERROR")
logData = spark.read.text(logFile).cache()

numAs = logData.filter(logData.value.contains('a')).count()
numBs = logData.filter(logData.value.contains('b')).count()

print("Lines with a: %i, lines with b: %i" % (numAs, numBs))

spark.stop()
