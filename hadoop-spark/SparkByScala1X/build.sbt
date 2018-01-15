name := "postgresql"

version := "1.0"

scalaVersion := "2.11.8"

//Spark 依赖
val sparkVersion = "1.6.1"
//val sparkVersion = "2.1.0"
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion ,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion,
  "org.apache.spark" %% "spark-yarn" % sparkVersion
//"org.apache.spark" %% "spark-mllib" % sparkVersion,
//  "org.apache.spark" %% "spark-streaming" % sparkVersion,
//  "org.apache.spark" %% "spark-streaming-twitter" % sparkVersion
)
libraryDependencies += "org.apache.hadoop" % "hadoop-client" % "2.7.3"
libraryDependencies += "org.apache.hadoop" % "hadoop-yarn-client" % "2.7.3"

// 添加源代码编译或者运行期间使用的依赖
libraryDependencies += "ch.qos.logback" % "logback-core" % "1.0.0"
libraryDependencies += "org.postgresql" % "postgresql" % "9.3-1102-jdbc41"
//libraryDependencies += "org.apache.spark" %% "spark-core" % "2.1.1"
//libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.1.0"
//libraryDependencies += "org.apache.spark" % "spark-core_2.10" % "1.1.0"