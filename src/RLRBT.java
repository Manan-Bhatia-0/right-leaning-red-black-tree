public class RLRBT<Key extends Comparable<Key>, Value> {
    private Node root;
    private int N;

    // CONSTRUCTOR 
    public RLRBT() {
        this.root = null;
        this.N = 0;
    }

    // PUBLIC METHODS 

    //
    // insert a new (key, val) into tree
    // or replace value of existing key
    //
    public void insert(Key key, Value val) {
        //TO BE IMPLEMENTED
        Node node = new Node(key, val);
        node.left = null;
        node.right = null;
        node.height = 0;
        if (isEmpty()) {
            root = node;
            N++;
            root.height = 0;
            root.isRed = false;
            return;
        }
        root = insertNode(root, key, val);
        node.isRed = false;
        setHeight(root, node);
        root.isRed = false;
    }

    //
    // get the value associated with the given key;
    // return null if key doesn't exist
    //
    public Value get(Key key) {
        if (isEmpty() || (key == null)) {
            return null;
        }
        Node temp = this.root;
        while (temp != null) {
            if (temp.key.compareTo(key) == 0) {
                return temp.val;
            }
            if (temp.key.compareTo(key) > 0) {
                if (temp.left == null) {
                    return null;
                } else {
                    temp = temp.left;
                }
            }
            if (temp.key.compareTo(key) < 0) {
                if (temp.right == null) {
                    return null;
                } else {
                    temp = temp.right;
                }
            }
        }
        return null;
    }

    //
    // return true if the tree
    // is empty and false
    // otherwise
    //
    public boolean isEmpty() {
        return root == null;
    }

    //
    // return the number of Nodes
    // in the tree
    //
    public int size() {
        return N;
    }

    //
    // returns the height of the tree
    //
    public int height() {
        return height(root);
    }

    //
    // returns the height of node
    // with given key in the tree;
    // return -1 if the key does
    // not exist in the tree
    //
    public int height(Key key) {
        //TO BE IMPLEMENTED
        if (isEmpty()) {
            return -1;
        }
        Node node = getNode(key);
        if (node == null) {
            return -1;
        }
        //System.out.printf("key: %s, node: %s\n", key.toString(), node.toString());
        if (isChildNode(node)) {
            node.height = 0;
            return node.height;
        }
        node.height = getMaxHeight(node);
        //  System.out.printf("height of node: %s = %d\n", node.toString(), node.height);
        return (node.height);
    }

    private void setHeight(Node node, Node temp) {
        if (node == null) {
            return;
        }
        if (node.equals(temp)) {
            //node.height = height(node.key);
            //System.out.println(node.toString());
            return;
        }

        if (node.left == null && node.right == null) {
            return;
        }
        node.height = height(node.key);
        node.isVisited = false;
        if (node.left != null && node.left.isVisited) {
            //node.isVisited = false;
            setHeight(node.left, temp);
        }
        if (node.right != null && node.right.isVisited) {
            //node.isVisited = false;
            setHeight(node.right, temp);
        }
    }



    private int getMaxHeight(Node node) {
        int leftHeight = 0;
        int rightHeight = 0;
        if (node.left != null) {
            leftHeight = getMaxHeight(node.left);
        }
        if (node.right != null) {
            rightHeight = getMaxHeight(node.right);
        }
        if (node.right == null && node.left == null) {
            return Math.max(leftHeight, rightHeight);
        }

        else {
            return 1 + Math.max(leftHeight, rightHeight);
        }
    }

    private int getMaxBlackHeight(Node node) {
        int leftHeight = 0;
        int rightHeight = 0;
        if (node.left != null) {
            leftHeight = getMaxBlackHeight(node.left);
        }
        if (node.right != null) {
            rightHeight = getMaxBlackHeight(node.right);
        }
        if (!node.isRed)
            return 1 + Math.max(leftHeight, rightHeight);
        else
            return Math.max(leftHeight, rightHeight);
    }


    // returns true if the given node is a child node
    private boolean isChildNode(Node node) {
        return ((node.left == null) && (node.right == null));
    }

    // similar to get(Key key) except it returns the node with the given key
    private Node getNode(Key key) {
        //TO BE IMPLEMENTED
        if (isEmpty() || (key == null)) {
            return null;
        }

        Node temp = this.root;
        while (temp != null) {
            if (temp.key.compareTo(key) == 0) {
                return temp;
            }
            if (temp.key.compareTo(key) > 0) {
                if (temp.left == null) {
                    return null;
                } else {
                    temp = temp.left;
                }
            }
            if (temp.key.compareTo(key) < 0) {
                if (temp.right == null) {
                    return null;
                } else {
                    temp = temp.right;
                }
            }
        }
        return null;
    }

    private Node insertNode(Node node, Key key, Value val) {
        if (node == null) {
            N++;
            return new Node(key, val);
        }

        if (key.compareTo(node.key) == 0) {
            node.val = val;
        }
        else if (key.compareTo(node.key) < 0) {
            node.isVisited = true;
            node.left = insertNode(node.left, key, val);
        }

        else {
            node.isVisited = true;
            node.right = insertNode(node.right, key, val);
        }

        if (node.left != null && node.left.isRed) {
            node = rotateRight(node);
        }

        if ((node.right != null && node.right.isRed) && node.right.right != null && (node.right.right.isRed)) {
            node = rotateLeft(node);
        }

        if ((node.left != null && node.left.isRed) && node.right != null && (node.right).isRed) {
            colorFlip(node);
        }

        return node;
    }


    //
    // return String representation of tree
    // level by level
    //
    public String toString() {
        String ret = "Level 0: ";
        Pair x = null;
        Queue<Pair> queue = new Queue<Pair>(N);
        int level = 0;
        queue.enqueue(new Pair(root, level));

        while (!queue.isEmpty()) {
            x = queue.dequeue();
            Node n = x.node;

            if (x.depth > level) {
                level++;
                ret += "\nLevel " + level + ": ";
            }

            if (n != null) {
                ret += "|" + n.toString() + "|";
                queue.enqueue(new Pair(n.left, x.depth + 1));
                queue.enqueue(new Pair(n.right, x.depth + 1));
            } else
                ret += "|null|";
        }

        ret += "\n";
        return ret;
    }

    //
    // return the black height of the tree
    //
    public int blackHeight() {
        //return blackHeight(root);
        if (isEmpty()) {
            return 0;
        }
        if (size() == 1) {
            return 0;
        }
        return getMaxBlackHeight(root) - 1;
    }



    // PRIVATE METHODS

    //
    // swap colors of two Nodes
    //
    private void swapColors(Node x, Node y) {
        if (x.isRed == y.isRed)
            return;

        boolean temp = x.isRed;
        x.isRed = y.isRed;
        y.isRed = temp;
    }

    //
    // rotate a link to the right
    //
    private Node rotateRight(Node x) {
        Node temp = x.left;
        x.left = temp.right;
        temp.right = x;
        swapColors(x, temp);
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        temp.height = Math.max(height(temp.left), x.height) + 1;
        return temp;
    }

    //
    // rotate a link to the left
    //
    private Node rotateLeft(Node x) {
        Node temp = x.right;
        x.right = temp.left;
        temp.left = x;
        swapColors(x, temp);
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        temp.height = Math.max(height(temp.right), x.height) + 1;
        return temp;
    }

    //
    // color flip
    //
    private Node colorFlip(Node x) {
        if (x.left == null || x.right == null)
            return x;

        if (x.left.isRed == x.right.isRed) {
            x.left.flip();
            x.right.flip();
            x.flip();
        }

        return x;
    }

    //
    // return the neight of Node x
    // or -1 if x is null
    //
    private int height(Node x) {
        if (x == null)
            return -1;

        return x.height;
    }

    // NODE CLASS
    public class Node {
        Key key;
        Value val;
        Node left, right;
        int height;
        boolean isRed;
        boolean isVisited = false;

        public Node(Key key, Value val) {
            this.key = key;
            this.val = val;
            this.isRed = true;
        }

        public String toString() {
            return "(" + key + ", " + val + "): "
                    + height + "; " + (this.isRed ? "R" : "B");
        }

        public void flip() {
            this.isRed = !this.isRed;
        }
    }

    // PAIR CLASS
    public class Pair {
        Node node;
        int depth;

        public Pair(Node node, int depth) {
            this.node = node;
            this.depth = depth;
        }
    }
}