import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private int queueSize;
    private Item[] items;
    int itemSize = 1;

    private void swap(int left, int right) {
        Item tmpItem = items[left];
        items[left] = items[right];
        items[right] = tmpItem;
    }

    // construct an empty randomized queue
    public RandomizedQueue() {
        queueSize = 0;
        items = (Item[]) new Object[1];
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
            Item[] tempItem = (Item[]) new Object[2 * queueSize];
            for (int i = 0; i < queueSize; i++) {
                tempItem[i] = items[i];
            }
            items = tempItem;
        }
        items[queueSize++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (queueSize == 0) {
            throw new java.util.NoSuchElementException();
        }
        int index = StdRandom.uniformInt(0, queueSize);
        swap(index, queueSize - 1);
        return items[--queueSize];
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (queueSize == 0) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniformInt(0, queueSize)];
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int seedA, seedB, iterationCount = 0;

        public boolean hasNext() {
            return iterationCount < queueSize;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Item result = items[(seedA * iterationCount + seedB) % queueSize];
            iterationCount++;
            return result;
        }

        private void setSeedA(int newSeedA) {
            seedA = newSeedA;
        }

        private void setSeedB(int newSeedB) {
            seedB = newSeedB;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int gcd(int x, int y) {
        if (x == 0 || y == 0) {
            return x + y;
        } else {
            return gcd(y, x % y);
        }
    }

    private int getSeedA() {
        while (true) {
            int currentSeed = StdRandom.uniformInt(0, queueSize);
            if (gcd(currentSeed, queueSize) == 1) {
                return currentSeed;
            }
        }
    }

    private int getSeedB() {
        return StdRandom.uniformInt(0, queueSize);
    }

    private void shuffle() {
        int NUMBER_OF_SUFFLE = 4;
        for (int i = 0; i < NUMBER_OF_SUFFLE; i++) {
            swap(StdRandom.uniformInt(0, queueSize), StdRandom.uniformInt(0, queueSize));
        }
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        shuffle();
        RandomizedQueueIterator randomizedQueueIterator = new RandomizedQueueIterator();
        if (queueSize > 0) {
            randomizedQueueIterator.setSeedA(getSeedA());
            randomizedQueueIterator.setSeedB(getSeedB());
        }
        return randomizedQueueIterator;
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
        for (int i = 0; i < 4; i++) {
            queue.enqueue(i);
        }
        for (int i = 0; i < 4; i++) {
            StdOut.println("Sampling a random element: " + queue.sample());
        }

        StdOut.println("Removing a random element: " + queue.dequeue());
        StdOut.println("Remaining size: " + queue.size());

        Iterator<Integer> it = queue.iterator();
        for (Integer item : queue) {
            StdOut.println(item);
        }

        for (Integer item : queue) {
            StdOut.println(item);
        }
    }
}