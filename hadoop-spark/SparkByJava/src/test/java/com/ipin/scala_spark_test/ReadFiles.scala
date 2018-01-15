package com.ipin.scala_spark_test

/* SimpleApp.scala */

import org.apache.spark.{SparkConf, SparkContext}

object ReadFiles {
  def main(args: Array[String]) {
    /**
      * file 本地文件
      */
    val logFile = "file:///usr/local/spark/README.md" // Should be some file on your system

    /**
      * 默认或者hdfs为hdfs上的文件
      */
    //val logFile = "hdfs:///user/wl/pom.xml" // Should be some file on your system
    //val logFile = "/user/wl/pom.xml" // Should be some file on your system
    val conf = new SparkConf()
      .setAppName("Simple Application")
      .setMaster("local").setSparkHome("/usr/local/spark")


    val allConf = conf.getAll
    println("***********")
    println(allConf)
    println("***********")

    val sc = new SparkContext(conf)

    sc.getConf.getAll.foreach(println)

    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")
    sc.stop()
  }
}
