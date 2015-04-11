import java.io.IOException;
import java.util.StringTokenizer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapreduce.Job;

// Class to be turned into an HDFS jar file for task
public class WordCount {


	public static void main(String[] args) {
		WordCount wc = new WordCount();
		wc.configureJob(args[0], args[1]);
	}

	public void configureJob(String inputPath, String outputPath) {

		Configuration conf = new Configuration();

		Job job = new Job(conf, "Word Count"); 

		// Set job methods
		
		// Job jar file name
		job.setJarByClass(WordCount.class);
		
		// MapPhase
		// Mapper component
		job.setMapperClass(TokenizerMapper.class);
		// Combiner class
		job.setCombinerClass(IntSumReducer.class);
		
		// ReducePhase
		// Reducer class
		job.setReducerClass(IntSumReducer.class);
		
		// Output classes
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(IntWritable.class);

		// Input and Output paths as main args[]
		try {
			FileInputFormat.addInputPath(job, new Path(inputPath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileOutputFormat.setOutputPath(job, new Path(outputPath));
		
		// Wait for completion
		try {
			System.exit(job.waitForCompletion(true) ? 0 : 1);
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	// Map Phase extension
	public static class TokenizerMapper extends
			Mapper<Object, Text, Text, IntWritable> {

		// IntWritable group for intermediate key/value pairs
		private final static IntWritable one = new IntWritable(1);

		// Byte level Object to be counted
		private Text word = new Text();

		// Map the Text objects into key/value pairs for an intermediate
		// group. Output to context group of "one"
		public void map(Object key, Text value, Context context)
				throws IOException, InterruptedException {

			// Simple tokenizer with whitespace as delimiter
			StringTokenizer itr = new StringTokenizer(value.toString());

			while (itr.hasMoreTokens()) {

				// Set Text value for this token
				word.set(itr.nextToken());

				// Write Text object to Context to key/value pair with value 1
				// ("hadoop", 1)
				context.write(word, one);
			}
		}
	}

	// Shuffle and Sort Phase
	
	// Reduce Phase extension
	public static class IntSumReducer extends
			Reducer<Text, IntWritable, Text, IntWritable> {

		// Group output for final result
		private IntWritable result = new IntWritable();

		// Reduce the intermediate data of context groups
		// to a result set
		public void reduce(Text key, Iterable<IntWritable> values,
				Context context) throws IOException, InterruptedException {

			int sum = 0;

			// Combiner Phase ??
			for (IntWritable val : values) {
				
				// Return IntWritable value to result sum value 
				sum += val.get();
			}

			// Set IntWritable values to total sum
			result.set(sum);

			// Write 
			context.write(key, result);
		}
	}
}