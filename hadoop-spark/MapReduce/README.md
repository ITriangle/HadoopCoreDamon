## maven

### pom
核心依赖看具体的版本，就可能发生变化

### package
生成 jar 包，需要在 pom.xml 中配置相应的类入库和生成独立 jar 包的插件

```bash
mvn clean
mvn package
```

## 执行

### 环境变量
声明配置文件

```bash
export HADOOP_HOME=/usr/local/hadoop/
export HADOOP_CONF_DIR=/etc/hadoop/conf
```

### 命令
向 hadoop 集群提交任务

```bash
hadoop  jar /Project_Path/MapReduce/target/MapReduce-1.0-SNAPSHOT.jar /user/triangle/wordcount/input /user/triangle/wordcount/output
```
