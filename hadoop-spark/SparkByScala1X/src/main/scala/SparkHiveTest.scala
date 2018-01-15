import java.security.PrivilegedAction

import org.apache.hadoop.security.UserGroupInformation
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.hive.HiveContext

/**
  * Created by wl on 17-6-19.
  */
object SparkHiveTest extends App {

  /**
    * 因为集群Hadoop 上所有的文件系统需要 hdfs 用户的权限，所以，需要切换到 hdfs 用户登录。
    */
  UserGroupInformation.createRemoteUser("hdfs").doAs(new PrivilegedAction[Any]() {

    override def run: Any = {
      main_real(args)
      null
    }
  })

  def main_real(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("Simple Application")

    //    System.setProperty("hive.metastore.uris", "thrift://192.168.1.84:9083")

    val sc = new SparkContext(conf)

    // sc is an existing SparkContext.
    val hiveContext = new HiveContext(sc)


    hiveContext.setConf("hive.metastore.uris", "thrift://192.168.1.84:9083")


    // Queries are expressed in HiveQL
    hiveContext.sql("select * from student_tmp").collect().foreach(println)
  }


}

