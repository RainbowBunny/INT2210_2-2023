import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    int queueSize;
    Item[] items;

    // construct an empty randomized queue
    public RandomizedQueue() {
        queueSize = 0;
        items = (Item[]) new Object[1];
    }

    private void swap(int i, int j) {
        Item tempItem = items[i];
        items[j] = items[i];
        items[i] = tempItem;
    }

    private void shuffle(int turn) {
        for (int i = 0; i < turn; i++) {
            int l = StdRandom.uniformInt(0, queueSize - 1);
            int r = StdRandom.uniformInt(0, queueSize - 1);
            swap(l, r);
        }
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return queueSize == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return queueSize;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if ((queueSize & -queueSize) == queueSize && queueSize > 0) {
            items = (Item[]) new Object[2 * queueSize];
        }
        items[queueSize++] = item;
        shuffle(4);
    }

    // remove and return a random item
    public Item dequeue() {
        if (queueSize == 0) {
            throw new java.util.NoSuchElementException();
        }
        shuffle(4);
        return items[queueSize--];
    }

    // return a random item (but do not remove it)
    public Item sample() {
        return items[StdRandom.uniformInt(0, queueSize - 1)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        
    }

    // unit testing (required)
    public static void main(String[] args) {

    }

}