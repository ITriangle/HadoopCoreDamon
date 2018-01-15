name := "scalding_sbt"

version := "1.0"

scalaVersion := "2.10.3"


resolvers ++= Seq(
  //"Local Maven Repository" at "file:///home/wl/.m2/repository",
  "cloudera Maven Repo" at "https://repository.cloudera.com/artifactory/cloudera-repos/",
  "conjars Maven Repo"  at "http://conjars.org/repo"
  //"Twitter Maven Repo"  at "http://maven.twttr.com"
)

libraryDependencies ++= Seq(
  "org.scala-lang"         % "scala-library"          % "2.10.3",
  "com.twitter"            % "scalding-core_2.10"     % "0.12.0",
  "org.scalatest"          % "scalatest_2.10"         % "3.0.0-M9",
  "org.apache.hadoop"      % "hadoop-core"            % "2.0.0-mr1-cdh4.3.1",
  "org.apache.hadoop"      % "hadoop-client"          % "2.0.0-mr1-cdh4.3.1",
  "cascading"              % "cascading-hadoop"       % "2.7.0",
  "cascading"              % "cascading-hadoop2-mr1"  % "2.7.0",
  "cascading"              % "cascading-platform"     % "2.7.0-wip-31",
  "cascading"              % "cascading-core"         % "2.7.0",
  "cascading"              % "cascading-local"        % "2.7.0",
  "cascading"              % "cascading-xml"          % "2.7.0"


)

assemblyMergeStrategy in assembly := {

  //case PathList("overview.html") => MergeStrategy.rename
  case PathList(ps @ _*) if ps.last endsWith ".html" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}