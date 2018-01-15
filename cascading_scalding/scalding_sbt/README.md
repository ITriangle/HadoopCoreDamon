- 创建独立的 jar 包
 
```
sbt clean
sbt assembly

```

- 执行运行的程序

```
hdfs dfs -mkdir ./scalding/output2

hadoop jar /home/wl/WLWork/cascading_scalding/scalding_sbt/target/scala-2.10/scalding_sbt-assembly-1.0.jar com.twitter.scalding.Tool com.matthewrathbone.scalding.Main --hdfs --input1 /user/wanglong/scalding/input1 --input2 /user/wanglong/scalding/input2 --output /user/wanglong/scalding/output2

```