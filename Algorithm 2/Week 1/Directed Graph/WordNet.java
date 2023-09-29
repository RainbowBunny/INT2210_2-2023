import java.util.ArrayList;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Merge;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.DirectedCycle;

public class WordNet {
    private int[] index;
    private ArrayList<String> synsets, synsets2;
    private ArrayList<Integer> indexes;
    private Digraph reverseGraph;
    private SAP sap;
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }

        readSynsetsData(synsets);
        readHypernymsData(hypernyms);
        sap = new SAP(reverseGraph);
        checkRootedDAG();
    }

    private void readSynsetsData(String synsetsFile) {
        synsets = new ArrayList<>();
        synsets2 = new ArrayList<>();
        indexes = new ArrayList<>();
        In synsetsIn = new In(synsetsFile);
        while (synsetsIn.hasNextLine()) {
            String[] nextLine = synsetsIn.readLine().split(",");
            String[] wordLists = nextLine[1].split(" ");
            synsets.add(wordLists[0]);
            for (int j = 0; j < wordLists.length; j++) {
                synsets2.add(wordLists[j]);
                indexes.add(Integer.parseInt(nextLine[0]));
            }
        }
        index = Merge.indexSort(this.synsets2.toArray(String[]::new));
    }

    private void readHypernymsData(String hypernyms) {
        reverseGraph = new Digraph(synsets.size());
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String[] nextLine = hypernymsIn.readLine().split(",");
            if (nextLine.length < 1) {
                throw new IllegalArgumentException("Wrong format of hypernyms file!");
            }

            int currentVertex = Integer.parseInt(nextLine[0]);
            for (int adj = 1; adj < nextLine.length; adj++) {
                reverseGraph.addEdge(currentVertex, Integer.parseInt(nextLine[adj]));
            }
        }
    }

    private void checkRootedDAG() {
        DirectedCycle directedCycle = new DirectedCycle(reverseGraph);
        if (directedCycle.cycle() != null) {
            throw new IllegalArgumentException();
        }
        int i = 0, root = 0;
        while (i < indexes.size()) {
            for (int j = i; j <= indexes.size(); j++) {
                if (j == indexes.size() || !synsets2.get(index[j]).equals(synsets2.get(index[i]))) {
                    int count = 0;
                    for (int k = i; k < j; k++) {
                        count += reverseGraph.outdegree(indexes.get(k));
                    }
                    if (count == 0) {
                        root++;
                    }
                    i = j; 
                    break;
                }
            }
        }
        if (root != 1) {
            throw new IllegalArgumentException();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synsets;
    }

    private int position(String word) {
        int lo = 0, hi = synsets2.size() - 1;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            int compare = synsets2.get(index[mid]).compareTo(word);
            if (compare < 0) {
                hi = mid - 1;
            } else {
                lo = mid;
            }
        }
        if (synsets2.get(index[lo]).equals(word)) {
            return lo;
        }
        return -1; 
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        return position(word) != -1;
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        int positionA = position(nounA), positionB = position(nounB);
        if (positionA == -1 || positionB == -1) {
            throw new IllegalArgumentException();
        }
        
        ArrayList<Integer> A = new ArrayList<>(), B = new ArrayList<>();
        A.add(indexes.get(index[positionA]));
        while (positionA + 1 < index.length && synsets.get(index[positionA]).equals(synsets.get(index[positionA + 1]))) {
            positionA++;
            A.add(indexes.get(index[positionA]));
        }

        B.add(indexes.get(index[positionB]));
        while (positionB + 1 < index.length && synsets.get(index[positionB]).equals(synsets.get(index[positionB + 1]))) {
            positionB++;
            B.add(indexes.get(index[positionB]));
        }
        return sap.length(A, B);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        int positionA = position(nounA), positionB = position(nounB);
        if (positionA == -1 || positionB == -1) {
            throw new IllegalArgumentException();
        }
        
        ArrayList<Integer> A = new ArrayList<>(), B = new ArrayList<>();
        A.add(positionA);
        while (positionA + 1 < index.length && synsets.get(index[positionA]).equals(synsets.get(index[positionA + 1]))) {
            positionA++;
            A.add(positionA);
        }

        B.add(positionB);
        while (positionB + 1 < index.length && synsets.get(index[positionB]).equals(synsets.get(index[positionB + 1]))) {
            positionB++;
            B.add(positionB);
        }
        return synsets2.get(sap.length(A, B));
    }

    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet(args[0], args[1]);
        StdOut.println(wordNet.position("thing"));
        StdOut.println(wordNet.position("Lactaid"));
        StdOut.println(wordNet.distance("thing", "Lactaid"));
    }
}