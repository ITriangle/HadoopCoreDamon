import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class WordCount {

    private static String delimiter_fields = "\u0001";
    private static String delimiter_record = "\u0002";
    private static int keyLength = 24 + 24 + 1 + delimiter_fields.length() * 2;

    /**
     * map:字符串的拆解为 key-value
     */
    public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {

        private Text key = new Text();
        private Text value = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            // 拆分读取的字段
            String[] fields = value.toString().split(delimiter_fields);
            // 拼装key：value
            String sch_major_key = fields[0].trim()
                    + delimiter_fields + fields[1].trim()
                    + delimiter_fields + fields[2].trim();
            String sch_major_value = fields[3].trim()
                    + delimiter_fields + fields[4].trim()
                    + delimiter_fields + fields[5].trim()
                    + delimiter_fields + fields[6].trim();

            //System.out.println("Mapper====>key" + sch_major_key);
            //System.out.println("Mapper====>val" + sch_major_value);

            this.key.set(sch_major_key);
            this.value.set(sch_major_value);
            context.write(this.key, value);
        }
    }


    /**
     * combiner:相当于 reduce，提前做一些聚合的工作，减少数据的传输量和 reduce 的数量
     */
    public static class TextSumCombiner extends Reducer<Text, Text, Text, Text> {
        private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String str_value = "";

            //System.out.println("Combiner====>key" + key.toString());
            for (Text val : values) {

                /**
                 * value 中会包含 key 值，实际的value值为：key+value
                 */
                //System.out.println("Combiner====>val" + val.toString());

                String value_list = val.toString().substring(keyLength);
                str_value = str_value.trim() + delimiter_record + value_list.trim();

            }


            result.set(str_value);
            context.write(key, result);

        }
    }

    /**
     * reducer:处理同一 key 的 value，不需要 key 输出，就将类型设置为 NullWritable
     */
    public static class TextSumReducer extends Reducer<Text, Text, NullWritable, Text> {
        private Text result = new Text();

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            String str_value = "";

            for (Text val : values) {

                str_value = str_value.trim() + delimiter_record + val.toString().trim();
            }

            String value_list = str_value.substring(delimiter_record.length());


            result.set(value_list);

            context.write(NullWritable.get(), result);

            //System.out.println("Reducer====>key" + key.toString());
            //System.out.println("Reducer====>val" + str_value);


        }
    }


    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", "");//设定分隔符
        conf.set("mapreduce.output.fileoutputformat.compress", "false");//压缩设置


        Job job = Job.getInstance(conf, "key count");
        job.setJarByClass(WordCount.class);
        job.setNumReduceTasks(1);

        job.setMapperClass(TokenizerMapper.class);
        job.setCombinerClass(TextSumCombiner.class);
        job.setReducerClass(TextSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


    /**
     * 打包命令
     * mvn package
     *
     * hadoop 提交程序命令
     * hadoop  jar /home/wl/WLWork/hadoop-spark/MapReduce/target/MapReduce-1.0-SNAPSHOT.jar /user/triangle/wordcount/input /user/triangle/wordcount/output
     *
     *
     */
}