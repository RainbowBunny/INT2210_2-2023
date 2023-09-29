import java.util.Iterator;

import edu.princeton.cs.algs4.StdOut;

public class Deque<Item> implements Iterable<Item> {
    private class Node {
        private Item item;
        private Node next = null;
        private Node prev = null;

        public Node() {
            item = null;
        }

        public Node(Item item) {
            this.item = item;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }

            Item result = current.item;
            current = current.next;
            return result;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int currentSize;
    private Node head;
    private Node tail;

    // construct an empty deque
    public Deque() {
        currentSize = 0;
        head = null;
        tail = null;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size() == 0;
    }

    // return the number of items on the deque
    public int size() {
        return currentSize;
    }

    private void createFirstElement(Item item) {
        Node node = new Node(item);
        head = node;
        tail = node;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        currentSize++;
        if (size() == 1) {
            createFirstElement(item);
            return;
        }

        Node node = new Node(item);
        node.next = head;
        head.prev = node;
        head = node;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        currentSize++;
        if (size() == 1) {
            createFirstElement(item);
            return;
        }

        Node node = new Node(item);
        node.prev = tail;
        tail.next = node;
        tail = node;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        currentSize--;
        Item res = head.item;
        if (size() == 0) {
            head = null;
            tail = null;
        } else {
            head.next.prev = null;
            head = head.next;
        }
        return res;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (size() == 0) {
            throw new java.util.NoSuchElementException();
        }
        currentSize--;
        Item res = tail.item;
        if (size() == 0) {
            head = null;
            tail = null;
        } else {
            tail.prev.next = null;
            tail = tail.prev;
        }
        return res;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.addLast(2);
        deque.addFirst(3);
        deque.addLast(4);
        
        StdOut.println("Current size: " + deque.size());
        StdOut.println("Remove First: " + deque.removeFirst());
        StdOut.println("Remove Last: " + deque.removeLast());
        Iterator<Integer> cIterator = deque.iterator();
        StdOut.println("Is last item? " + cIterator.hasNext());
        StdOut.println("Next item: " + cIterator.next());

        for (Integer i : deque) {
            StdOut.println(i);
        }

        deque.addLast(5);
        StdOut.println(deque.size());
        for (Integer i : deque) {
            StdOut.println(i);
        }
    }
}