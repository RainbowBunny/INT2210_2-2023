import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdOut;

public class RandomWord {
    public static void main(String[] args) {
        int wordCount = 0;
        String championWord = "";
        while (!StdIn.isEmpty()) {
            wordCount += 1;
            String currentCandidate = StdIn.readString();
            if (StdRandom.bernoulli(1.0 / wordCount)) {
                championWord = currentCandidate;
            }
        }
        StdOut.println(championWord);
    }    
}
