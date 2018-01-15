
## Compiling
``` bash
mvn compile
```

## Testing
``` bash
mvn test
```

## Running
``` bash
mvn assembly:single # this will bundle dependencies, we actually don't have any, but good to set up.
hadoop jar target/scala-scalding-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool main.scala.com.matthewrathbone.scalding.Main --hdfs --input1 /path/to/input1 --input2 /path/to/input2 --output /path/to/output
```


- `hdfs dfs -mkdir ./scalding`
- `hdfs dfs -mkdir ./scalding/input1`
- `hdfs dfs -mkdir ./scalding/input2`
- `hdfs dfs -mkdir ./scalding/output`
- `hdfs dfs -put /home/wl/WLWork/Tmp_Git/hadoop-framework-examples-master/scalding/transactions_test.txt ./scalding/input2`
- `hdfs dfs -put /home/wl/WLWork/Tmp_Git/hadoop-framework-examples-master/scalding/users_test.txt ./scalding/input1`


- `hadoop jar /home/wl/WLWork/Tmp_Git/hadoop-framework-examples-master/scalding/target/scala-scalding-1.0-SNAPSHOT-jar-with-dependencies.jar com.twitter.scalding.Tool main.scala.com.matthewrathbone.scalding.Main --hdfs --input1 /user/wanglong/scalding/input1 --input2 /user/wanglong/scalding/input2 --output /user/wanglong/scalding/output`