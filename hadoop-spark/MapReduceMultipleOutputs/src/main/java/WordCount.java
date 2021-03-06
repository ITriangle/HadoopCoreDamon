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
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import javax.lang.model.type.NullType;

public class WordCount {

    private static String delimiter_fields = "\u0001";
    private static String delimiter_record = "\u0002";
    //private static int keyLength = 24 + 24 + 1 + delimiter_fields.length() * 2;
    private static int keyLength = 24;
    private static String partitionName = "";

    /**
     * map:字符串的拆解为 key-value
     */
    public static class TokenizerMapper extends Mapper<Object, Text, Text, Text> {

        private Text key = new Text();
        private Text value = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {

            // 拆分读取的字段
            String fields = value.toString();
            // 拼装key：value
            int lastDelimiterIndex = fields.lastIndexOf(delimiter_fields);
            int fieldsLength = fields.length();
            String sch_major_key = fields.substring(lastDelimiterIndex + 1);


            String sch_major_value = fields.substring(0,lastDelimiterIndex);

            System.out.println("Mapper====>key" + sch_major_key);
            System.out.println("Mapper====>val" + sch_major_value);

            this.key.set(sch_major_key);
            this.value.set(sch_major_value);
            context.write(this.key, this.value);
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
        private MultipleOutputs<NullWritable, Text> mos;

        @Override
        public void setup(Context context) {
            mos = new MultipleOutputs<NullWritable, Text>(context);
        }

        public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

            for (Text val : values) {

                result.set(val);

                //最基本的输出，注释掉就没有了
                //context.write(NullWritable.get(), result);

                //context.write(key, result);
                //mos.write("text", key, result,key.toString());

                //按照key 分目录存放文件
                mos.write(NullWritable.get(), result, partitionName + "=" + key.toString() + '/');
                //mos.write("text", result, new Text("Hello"));

                //System.out.println("Reducer====>key" + key.toString());
                //System.out.println("Reducer====>val" + str_value);
            }


        }

        @Override
        public void cleanup(Context context) throws IOException, InterruptedException {
            mos.close();
        }
    }


    /**
     * 三个参数：输入路径，输出路径，分区key的名称
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //获取分区表的key值
        partitionName = args[2].trim();

        Configuration conf = new Configuration();
        conf.set("mapred.textoutputformat.separator", "");//设定分隔符
        conf.set("mapreduce.output.fileoutputformat.compress", "false");//压缩设置


        Job job = Job.getInstance(conf, "PartitionByKey");
        job.setJarByClass(WordCount.class);

        //job.setNumReduceTasks(1);

        job.setMapperClass(TokenizerMapper.class);
        //job.setCombinerClass(TextSumCombiner.class);
        job.setReducerClass(TextSumReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);


        //增加输出的名录
        LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);
        MultipleOutputs.addNamedOutput(job, "text", TextOutputFormat.class, TextOutputFormat.class, Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }


    /**
     * 打包命令
     * mvn package
     *
     * hadoop 提交程序命令
     * hadoop  jar /home/wl/PublicGitRepo/HadoopCoreDamon/hadoop-spark/MapReduceMultipleOutputs/target/MapReduce-1.0-SNAPSHOT.jar /user/triangle/cv_job_input /user/triangle/cv_job_output_2 job_start_year
     *
     *
     */
}