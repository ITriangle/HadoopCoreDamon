package com.it.java_spark_test;

import org.apache.hadoop.security.UserGroupInformation;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;


import java.io.Serializable;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

public class JavaSparkHiveExample {

    // $example on:spark_hive$
    public static class Info implements Serializable {
        private String inc_addr;
        private String inc_province_id;
        private String inc_province;
        private String inc_city_id;
        private String inc_city;
        private String inc_region_id;
        private String inc_region;

        public String getInc_addr() {
            return inc_addr;
        }

        public void setInc_addr(String inc_addr) {
            this.inc_addr = inc_addr;
        }

        public String getInc_province_id() {
            return inc_province_id;
        }

        public void setInc_province_id(String inc_province_id) {
            this.inc_province_id = inc_province_id;
        }

        public String getInc_province() {
            return inc_province;
        }

        public void setInc_province(String inc_province) {
            this.inc_province = inc_province;
        }

        public String getInc_city_id() {
            return inc_city_id;
        }

        public void setInc_city_id(String inc_city_id) {
            this.inc_city_id = inc_city_id;
        }

        public String getInc_city() {
            return inc_city;
        }

        public void setInc_city(String inc_city) {
            this.inc_city = inc_city;
        }

        public String getInc_region_id() {
            return inc_region_id;
        }

        public void setInc_region_id(String inc_region_id) {
            this.inc_region_id = inc_region_id;
        }

        public String getInc_region() {
            return inc_region;
        }

        public void setInc_region(String inc_region) {
            this.inc_region = inc_region;
        }
    }
    // $example off:spark_hive$

    public static class Record implements Serializable {
        private int key;
        private String value;

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }



    public static void main2(String[] args){
        String warehouseLocation = "spark-warehouse";
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                //.config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .config("hive.metastore.uris","thrift://192.168.1.84:9083")
                .getOrCreate();



        spark.sql("CREATE TABLE IF NOT EXISTS aux_location_dict (loc_id string , loc_dict_name  string,id_level int ,src  string, etl_date string)");
        spark.sql("CREATE TABLE IF NOT EXISTS  ref_location(v_location_id  int, loc_id   string,   loc_name     string , loc_full_name  string , id_level string,  pos_in_vec  string,  loc_level   string,  src  string,etl_date  string )");
        spark.sql("");


    }



    public static void main1(String[] args) {



        // $example on:spark_hive$
        // warehouseLocation points to the default location for managed databases and tables
        String warehouseLocation = "spark-warehouse";
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                //.config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .config("hive.metastore.uris","thrift://192.168.1.84:9083")
                .getOrCreate();



        spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)");
        spark.sql("LOAD DATA LOCAL INPATH './src/data/kv1.txt' INTO TABLE src");

        // Queries are expressed in HiveQL
        spark.sql("SELECT * FROM src").show();
        // +---+-------+
        // |key|  value|
        // +---+-------+
        // |238|val_238|
        // | 86| val_86|
        // |311|val_311|
        // ...

        // Aggregation queries are also supported.
        spark.sql("SELECT COUNT(*) FROM src").show();
        // +--------+
        // |count(1)|
        // +--------+
        // |    500 |
        // +--------+

        // The results of SQL queries are themselves DataFrames and support all normal functions.
        Dataset<Row> sqlDF = spark.sql("SELECT key, value FROM src WHERE key < 10 ORDER BY key");

        // The items in DaraFrames are of type Row, which lets you to access each column by ordinal.
        Dataset<String> stringsDS = sqlDF.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {
                return "Key: " + row.get(0) + ", Value: " + row.get(1);
            }
        }, Encoders.STRING());
        stringsDS.show();
        // +--------------------+
        // |               value|
        // +--------------------+
        // |Key: 0, Value: val_0|
        // |Key: 0, Value: val_0|
        // |Key: 0, Value: val_0|
        // ...



        // You can also use DataFrames to create temporary views within a SparkSession.
        List<Record> records = new ArrayList<>();
        for (int key = 1; key < 100; key++) {
            Record record = new Record();
            record.setKey(key);
            record.setValue("val_" + key);
            records.add(record);
        }
        Dataset<Row> recordsDF = spark.createDataFrame(records, Record.class);
        recordsDF.createOrReplaceTempView("records");

        // Queries can then join DataFrames data with data stored in Hive.
        spark.sql("SELECT * FROM records r JOIN src s ON r.key = s.key").show();
        // +---+------+---+------+
        // |key| value|key| value|
        // +---+------+---+------+
        // |  2| val_2|  2| val_2|
        // |  2| val_2|  2| val_2|
        // |  4| val_4|  4| val_4|
        // ...
        // $example off:spark_hive$

        spark.stop();
    }

    public static void main3(String[] args){

        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                //.config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .config("hive.metastore.uris","thrift://192.168.1.84:9083")
                .getOrCreate();

        Dataset<Row> sqlDF = spark.sql("select value from address limit 10");
        sqlDF.show();

        final Dataset<String> stringsDS = sqlDF.map(new MapFunction<Row, String>() {
            @Override
            public String call(Row row) throws Exception {

                String addr  = row.get(0).toString().replaceAll(" ","");

                return addr + "\t" +  getID(addr);
            }
        }, Encoders.STRING());
        stringsDS.show();

//        stringsDS.write().format("parquet").save("./address.parquet");
//        stringsDS.write().save("file:///home/wl/data/hadoop/address.txt");
//        stringsDS.write().text("file:///home/wl/data/hadoop/address.txt");


//        List<String> rowList = (List<String>) stringsDS.javaRDD();

        JavaRDD<String> rowList = stringsDS.javaRDD();

        rowList.saveAsTextFile("file:///home/wl/data/hadoop/address.txt");
//        rowList.repartition(1).saveAsTextFile("file:///home/wl/data/hadoop/address.txt");

//        stringsDS.rdd().saveAsTextFile("file:///home/wl/data/hadoop/address.txt");
//        stringsDS.rdd().repartition().saveAsTextFile("file:///home/wl/data/hadoop/address.txt");
    }

    public static String getID(String addr){
//        String id = null;


        return  "123456123456";
    }

    public static void main5(String[] args) {


        // $example on:spark_hive$
        // warehouseLocation points to the default location for managed databases and tables
        String warehouseLocation = "spark-warehouse";
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                //.config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .config("hive.metastore.uris", "thrift://192.168.1.84:9083")
                .getOrCreate();


//        spark.sql("CREATE TABLE IF NOT EXISTS src (key INT, value STRING)");
        spark.sql("load data local inpath '/home/wl/WLWork/sync_pg_to_hive/gaokao/test/gaokao.sch_recruit' into table gaokao.sch_recruit");

        // Queries are expressed in HiveQL
        spark.sql("SELECT * FROM gaokao.sch_recruit").show();
    }

    public static void main(final String[] args){

        /**
         * 因为集群Hadoop 上所有的文件系统需要 hdfs 用户的权限，所以，需要切换到 hdfs 用户登录。
         */
        UserGroupInformation.createRemoteUser("hdfs").doAs(new PrivilegedAction<Object>() {
            public Object run(){
                main5(args);
                return null;
            }
        });

//        main5(args);
//        main4(args);
    }

    public static void main4(String[] args){
        SparkSession spark = SparkSession
                .builder()
                .appName("Java Spark Hive Example")
                //.config("spark.sql.warehouse.dir", warehouseLocation)
                .enableHiveSupport()
                .config("hive.metastore.uris","thrift://192.168.1.84:9083")
                .getOrCreate();

        Tuple2<String, String>[]  allConf = spark.sparkContext().getConf().getAll();


        for (Tuple2<String,String> conf: allConf
             ) {
            System.out.println(conf);

        }

        Dataset<Row> sqlDF = spark.sql("select * from student_tmp");
        sqlDF.show();
    }



}
