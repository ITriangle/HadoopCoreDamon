import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf


import org.apache.spark.sql.SQLContext

object SparkPGTest extends App {

  val conf = new SparkConf().setAppName("Simple Application")
  val sc = new SparkContext(conf)
  val sqlContext = new SQLContext(sc)

  val db_url = "jdbc:postgresql://localhost:5432/itbase?user=triangle&password=triangle"
  val schema_tablename = "public.people"
  val jdbcDF = sqlContext.read.format("jdbc").options(Map(
    "driver" -> "org.postgresql.Driver",
    "url" -> db_url,
    "dbtable" -> schema_tablename)).load()

  val df = jdbcDF

  // Show the content of the DataFrame
  df.show()

  // Print the schema in a tree format
  df.printSchema()

  // Select only the "name" column
  df.select("name").show()

  // Select everybody, but increment the age by 1
  df.select(df("name"), df("age") + 1).show()

  // Select people older than 21
  df.filter(df("age") > 21).show()

  // Count people by age
  df.groupBy("age").count().show()

  /**
    * 同样还是支持join,agg等操作
    */

  //register it as a table
  df.registerTempTable("people")

  // SQL statements can be run by using the sql methods provided by sqlContext.
  val teenagers = sqlContext.sql("SELECT name, age FROM people WHERE age >= 13 AND age <= 19")

  // The results of SQL queries are DataFrames and support all the normal RDD operations.
  // The columns of a row in the result can be accessed by field index:
  teenagers.map(t => "Name: " + t(0)).collect().foreach(println)

  // or by field name:
  teenagers.map(t => "Name: " + t.getAs[String]("name")).collect().foreach(println)

  // row.getValuesMap[T] retrieves multiple columns at once into a Map[String, T]
  teenagers.map(_.getValuesMap[Any](List("name", "age"))).collect().foreach(println)
}
