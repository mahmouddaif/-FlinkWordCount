import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;

public class WordCount {
  
  public static void main(String[] args) throws Exception {
    
    // set up the execution environment
    final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
    
    
    DataSet<String> text = env.readTextFile("input.txt");
    
    DataSet<Tuple2<String, Integer>> counts = 
        // split up the lines in pairs (2-tuples) containing: (word,1)
        text.flatMap(new LineSplitter())
        // group by the tuple field "0" and sum up tuple field "1"
        .groupBy(0)
        .aggregate(Aggregations.SUM, 1)
        ;

    // emit result
    counts.print();
      counts.writeAsText("output.txt").setParallelism(1);
    
    // execute program
    env.execute("WordCount Example");
  }
}
