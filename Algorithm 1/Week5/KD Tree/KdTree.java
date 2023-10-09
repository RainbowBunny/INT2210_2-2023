import java.util.ArrayList;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree {
    private static final boolean XCORD = true;

    private int size;
    private Node rootNode;

    private class Node {
        Point2D key;
        Node left, right;
        boolean layer;
        public Node() {
            key = null;
            left = null;
            right = null;
        }

        public Node(Point2D key, boolean layer) {
            this.key = key;
            this.layer = layer;
        }
    }

    public KdTree() {
        rootNode = null;
        size = 0;
    }

    public boolean isEmpty() {
        return rootNode == null;
    }

    public int size() {
        return size;
    }

    private Node put(Node h, Point2D key, boolean layer) {
        if (h == null) {
            size++;
            return new Node(key, layer);
        }

        int cmp;
        if (layer == XCORD) {
            cmp = Point2D.X_ORDER.compare(key, h.key);
        } else {
            cmp = Point2D.Y_ORDER.compare(key, h.key);
        }

        if (cmp == 0) {
            if (layer == XCORD) {
                cmp = Point2D.Y_ORDER.compare(key, h.key);
            } else {
                cmp = Point2D.X_ORDER.compare(key, h.key);
            }
        }
        
        if (cmp < 0) {
            h.left = put(h.left, key, layer ^ true);
        } else if (cmp > 0) {
            h.right = put(h.right, key, layer ^ true);
        }

        return h;
    }

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        rootNode = put(rootNode, p, XCORD);
    }

    private boolean contains(Node h, Point2D key, boolean layer) {
        if (h == null) {
            return false;
        }

        int cmp;
        if (layer == XCORD) {
            cmp = Point2D.X_ORDER.compare(key, h.key);
        } else {
            cmp = Point2D.Y_ORDER.compare(key, h.key);
        }

        if (cmp == 0) {
            if (layer == XCORD) {
                cmp = Point2D.Y_ORDER.compare(key, h.key);
            } else {
                cmp = Point2D.X_ORDER.compare(key, h.key);
            }
        }

        if (cmp < 0) {
            return contains(h.left, key, layer ^ true);
        } else if (cmp > 0) {
            return contains(h.right, key, layer ^ true);
        }
        return true;
    }

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return contains(rootNode, p, XCORD);
    }

    private void draw(Node h, boolean layer) {
        if (h == null) {
            return;
        }
        if (layer == XCORD) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(h.key.x(), 0, h.key.x(), 1);
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(0, h.key.y(), 1, h.key.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.point(h.key.x(), h.key.y());
        draw(h.left, layer ^ true);
        draw(h.right, layer ^ true);
    }

    public void draw() {
        draw(rootNode, XCORD);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        Queue<RectHV> region = new Queue<>();
        Queue<Node> nodes = new Queue<>();
        ArrayList<Point2D> resultList = new ArrayList<>();
        if (isEmpty()) {
            return resultList;
        }

        region.enqueue(new RectHV(0, 0, 1, 1));
        nodes.enqueue(rootNode);

        while (!region.isEmpty()) {
            RectHV currentRegion = region.dequeue();
            Node currentNode = nodes.dequeue();
            Point2D splitNode = currentNode.key;
            if (rect.contains(splitNode)) {
                resultList.add(splitNode);
            }
            RectHV leftRegion, rightRegion;
            if (currentNode.layer == XCORD) {
                leftRegion = new RectHV(currentRegion.xmin(), currentRegion.ymin(), splitNode.x(), currentRegion.ymax());
                rightRegion = new RectHV(splitNode.x(), currentRegion.ymin(), currentRegion.xmax(), currentRegion.ymax());
            } else {
                leftRegion = new RectHV(currentRegion.xmin(), currentRegion.ymin(), currentRegion.xmax(), splitNode.y());
                rightRegion = new RectHV(currentRegion.xmin(), splitNode.y(), currentRegion.xmax(), currentRegion.ymax());
            }

            if (leftRegion.intersects(rect) && currentNode.left != null) {
                region.enqueue(leftRegion);
                nodes.enqueue(currentNode.left);
            }

            if (rightRegion.intersects(rect) && currentNode.right != null) {
                region.enqueue(rightRegion);
                nodes.enqueue(currentNode.right);
            }
        }

        return resultList;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (rootNode == null) {
            return null;
        }
        
        Queue<RectHV> region = new Queue<>();
        Queue<Node> nodes = new Queue<>();
        Point2D resulPoint = null;
        double resultDistance = Double.POSITIVE_INFINITY;
        region.enqueue(new RectHV(0, 0, 1, 1));
        nodes.enqueue(rootNode);

        while (!region.isEmpty()) {
            RectHV currentRegion = region.dequeue();
            Node currentNode = nodes.dequeue();
            Point2D splitNode = currentNode.key;

            if (resultDistance > p.distanceSquaredTo(splitNode)) {
                resulPoint = splitNode;
                resultDistance = p.distanceSquaredTo(splitNode);
            }

            RectHV leftRegion, rightRegion;
            if (currentNode.layer == XCORD) {
                leftRegion = new RectHV(currentRegion.xmin(), currentRegion.ymin(), splitNode.x(), currentRegion.ymax());
                rightRegion = new RectHV(splitNode.x(), currentRegion.ymin(), currentRegion.xmax(), currentRegion.ymax());
            } else {
                leftRegion = new RectHV(currentRegion.xmin(), currentRegion.ymin(), currentRegion.xmax(), splitNode.y());
                rightRegion = new RectHV(currentRegion.xmin(), splitNode.y(), currentRegion.xmax(), currentRegion.ymax());
            }

            if (leftRegion.contains(p)) {
                if (currentNode.left != null && leftRegion.distanceSquaredTo(p) < resultDistance) {
                    region.enqueue(leftRegion);
                    nodes.enqueue(currentNode.left);
                }

                if (currentNode.right != null && rightRegion.distanceSquaredTo(p) < resultDistance) {
                    region.enqueue(rightRegion);
                    nodes.enqueue(currentNode.right);
                }
            } else {
                if (currentNode.right != null && rightRegion.distanceSquaredTo(p) < resultDistance) {
                    region.enqueue(rightRegion);
                    nodes.enqueue(currentNode.right);
                }

                if (currentNode.left != null && leftRegion.distanceSquaredTo(p) < resultDistance) {
                    region.enqueue(leftRegion);
                    nodes.enqueue(currentNode.left);
                }
            }
            
        }
        return resulPoint;
    }
}