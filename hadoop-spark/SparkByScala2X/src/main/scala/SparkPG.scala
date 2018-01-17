import org.apache.spark
import org.apache.spark.sql.expressions.Aggregator
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.SparkSession


/**
  * Created by wl on 17-6-23.
  */
object SparkPG extends App {

  // Note: JDBC loading and saving can be achieved via either the load/save or jdbc methods
  val spark = SparkSession
    .builder()
    .appName("Spark SQL data sources example")
    .config("spark.some.config.option", "some-value")
    .getOrCreate()


  // Loading data from a JDBC source
  val jdbcDF = spark.read
    .format("jdbc")
    .option("url", "jdbc:postgresql://localhost/itbase")
    .option("dbtable", "public.people")
    .option("user", "triangle")
    .option("password", "triangle")
    .load()

  // This import is needed to use the $-notation
  import spark.implicits._

  val df = jdbcDF;

  // Print the schema in a tree format
  df.printSchema()

  // Select only the "name" column
  df.select("name").show()

  // Select everybody, but increment the age by 1
  df.select($"name", $"age" + 1).show()

  // Select people older than 21
  df.filter($"age" > 21).show()

  // Count people by age
  df.groupBy("age").count().show()


  // Register the DataFrame as a SQL temporary view
  df.createOrReplaceTempView("people_tmp")

  val sqlDF = spark.sql("SELECT * FROM people_tmp")
  sqlDF.show()


//  // Saving data to a JDBC source
//  jdbcDF.write
//    .format("jdbc")
//    .option("url", "jdbc:postgresql:dbserver")
//    .option("dbtable", "schema.tablename")
//    .option("user", "username")
//    .option("password", "password")
//    .save()


}
