package com.it.scala_spark_test

import java.security.PrivilegedAction

import org.apache.hadoop.security.UserGroupInformation
import org.apache.spark.sql.SparkSession

/**
  * Created by wl on 17-6-19.
  */
object SparkHiveByHdfs {

  def main(args: Array[String]): Unit = {
    /**
      * 因为集群Hadoop 上所有的文件系统需要 hdfs 用户的权限，所以，需要切换到 hdfs 用户登录。
      */
    UserGroupInformation.createRemoteUser("hdfs").doAs(new PrivilegedAction[Any]() {

      override def run: Any = {
        main_real1(args)
        null
      }
    })
  }



  def main_real1(args: Array[String]) = {
    val spark = SparkSession.builder()
      .appName("Java Spark Hive Example")
      .enableHiveSupport //.config("spark.sql.warehouse.dir", warehouseLocation)
      .config("hive.metastore.uris", "thrift://192.168.1.84:9083")
      .enableHiveSupport()
      .getOrCreate()

    val sqlDF = spark.sql("select * from student_tmp")
    sqlDF.show()
  }



}
