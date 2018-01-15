package com.ipin.scala_spark_test

import org.apache.spark.sql.SparkSession

/**
  * Created by wl on 17-6-19.
  */
object SparkHiveByDefault {


  def main(args: Array[String]): Unit = {
    val spark = SparkSession
      .builder()
      .appName("Spark Hive Example")
      .config("hive.metastore.uris", "thrift://192.168.1.84:9083")
      //    .config("spark.home","/usr/local/spark")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.getConf.getAll.foreach(println)
    //  println(spark.sparkContext.getConf.get("spark.home"))

//    val sqlDF = spark.sql("select * from student_tmp")
    val sqlDF = spark.sql("load data local inpath '/home/wl/WLWork/sync_pg_to_hive/gaokao/test/gaokao.sch_recruit' into table gaokao.sch_recruit")
//    sqlDF.show()

    spark.close()
  }
}
