import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by wl on 11/7/17.
  */
object MapReduceTest {

  def main(args: Array[String]): Unit = {

    /**
      * file 本地文件
      */
    //    val logFile = "file:///usr/local/spark/README.md" // Should be some file on your system
    val logFile = "file:/home/wl/WLWork/Hadoop/Spark/spark_test/spark_scala_test/data/test" // Should be some file on your system

    val conf = new SparkConf()
      .setAppName("Simple Application")
      .setMaster("local").setSparkHome("/usr/local/spark")


    val allConf = conf.getAll
    val sc = new SparkContext(conf)

    println("***********")
    sc.getConf.getAll.foreach(println)
    println("***********")

    val logData = sc.textFile(logFile, 2).cache()


    val numAs = logData.filter(line => line.contains("a")).count()
    val numBs = logData.filter(line => line.contains("b")).count()
    println(s"Lines with a: $numAs, Lines with b: $numBs")





    val counts = logData.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)
    println(s"counts : $counts")
    counts.foreach(println)

    val mapValue = logData.map(line => (line.split(" ",1),line))
    println(s"mapValue : $mapValue")

    mapValue.collect().foreach(value => println(value.toString))



    sc.stop()

  }

}
