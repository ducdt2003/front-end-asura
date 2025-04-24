import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class AvgElectricity {

    // Mapper
import java.io.IOException;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.mapreduce.Mapper;

public class AvgConsumptionMapper extends Mapper<Object, Text, Text, DoubleWritable> {
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] parts = value.toString().split(",");
            if (parts.length >= 14) {
                String year = parts[0];
                double avg = Double.parseDouble(parts[13]); // chỉ số trung bình năm
                context.write(new Text(year), new DoubleWritable(avg));
            }
        }
    }

// Reducer
public static class ElectricityReducer extends Reducer<Text, DoubleWritable, Text, DoubleWritable> {
    public void reduce(Text key, Iterable<DoubleWritable> values, Context context)
            throws IOException, InterruptedException {
        for (DoubleWritable val : values) {
            if (val.get() > 30) {
                context.write(key, val);
            }
        }
    }

    }

    // Driver
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Electricity Average Over 30");

        job.setJarByClass(AvgElectricity.class);
        job.setMapperClass(ElectricityMapper.class);
        job.setReducerClass(ElectricityReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(DoubleWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0])); // Input: sample.txt on HDFS
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // Output: e.g., /output/BT1_MSV

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SalesByCountry {

    // Mapper
    public static class CountryMapper extends Mapper<Object, Text, Text, IntWritable> {
        private final static IntWritable one = new IntWritable(1);
        private Text country = new Text();

        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            String[] fields = value.toString().split(",");
            if (fields.length > 7 && !fields[7].equals("Country")) { // Bỏ dòng tiêu đề
                country.set(fields[7]);
                context.write(country, one);
            }
        }
    }

    // Reducer
    public static class CountryReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        public void reduce(Text key, Iterable<IntWritable> values, Context context)
                throws IOException, InterruptedException {
            int sum = 0;
            for (IntWritable val : values) {
                sum += val.get(); // đếm số lượng sản phẩm
            }
            context.write(key, new IntWritable(sum));
        }
    }

    // Driver
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Sales By Country");

        job.setJarByClass(SalesByCountry.class);
        job.setMapperClass(CountryMapper.class);
        job.setCombinerClass(CountryReducer.class); // dùng reducer làm combiner để tối ưu
        job.setReducerClass(CountryReducer.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job, new Path(args[0])); // Input: SalesJan2009.csv on HDFS
        FileOutputFormat.setOutputPath(job, new Path(args[1])); // Output: e.g., /output/BT2_MSV

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
