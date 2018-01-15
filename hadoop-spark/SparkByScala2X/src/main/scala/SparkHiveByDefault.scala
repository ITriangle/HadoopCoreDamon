import org.apache.spark.sql.Row
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql
/**
  * Created by wl on 17-6-19.
  */
object SparkHiveByDefault extends App{


  val spark = SparkSession
    .builder()
    .appName("Spark Hive Example")
    .config("hive.metastore.uris", "thrift://192.168.1.84:9083")
//    .config("spark.home","/usr/local/spark")
    .enableHiveSupport()
    .getOrCreate()

  spark.sparkContext.getConf.getAll.foreach(println)
//  println(spark.sparkContext.getConf.get("spark.home"))

  val sqlDF = spark.sql("select * from student_tmp")
  sqlDF.show()

  spark.close()
}
