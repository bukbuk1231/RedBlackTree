public class RedBlackTree {

    private RBNode root;
    private int size;
    private final boolean RED = true, BLACK = false;

    public RedBlackTree() {
        root = null;
        size = 0;
    }

    public void insert(int newVal) {
        // Create the new node and color it as red
        RBNode newNode = new RBNode(newVal);
        setColor(newNode, RED);

        if (root == null) {
            // case 1: empty tree
            root = newNode;
            setColor(root, BLACK);
        } else {
            // case 2: non-empty tree, find the leaf position for the new node
            RBNode prev = null, cur = root;
            while (cur != null) {
                prev = cur;
                if (cur.val >= newVal) {
                    cur = cur.left;
                } else {
                    cur = cur.right;
                }
            }
            if (prev.val >= newVal) {
                prev.left = newNode;
                prev.left.parent = prev;
            } else {
                prev.right = newNode;
                prev.right.parent = prev;
            }
        }
        rebalance(newNode);
        size++;
    }

    private void rebalance(RBNode n) {
        while (n != null && n != root && n.parent.color == RED) {
            RBNode parent = n.parent;
            RBNode grand = parentOf(n.parent);
            if (parentOf(n) == leftOf(parentOf(parentOf(n)))) {
                RBNode uncle = rightOf(parentOf(parentOf(n)));
                if (colorOf(uncle) == RED) {
                    setColor(uncle, BLACK);
                    setColor(parent, BLACK);
                    setColor(grand, RED);
                    n = grand;
                } else {
                    if (n == parent.right) {
                        rotateLeft(parent);
                        n = parent;
                        parent = n.parent;
                    }
                    rotateRight(grand);
                    swapColor(parent, grand);
                    n = parent;
                }
            } else {
                RBNode uncle = leftOf(parentOf(parentOf(n)));
                if (colorOf(uncle) == RED) {
                    setColor(uncle, BLACK);
                    setColor(parent, BLACK);
                    setColor(grand, RED);
                    n = grand;
                } else {
                    if (n == parent.left) {
                        rotateRight(parent);
                        n = parent;
                        parent = n.parent;
                    }
                    rotateLeft(grand);
                    swapColor(parent, grand);
                    n = parent;
                }
            }
        }
        root.color = BLACK;
    }

    public void insertAll(int[] vals) {
        for (int val : vals) {
            insert(val);
            size++;
        }
    }

    public int size() {
        return size;
    }

    public int height() {
        return height(root);
    }

    private int height(RBNode root) {
        if (root == null)
            return 0;
        int left = height(root.left);
        int right = height(root.right);
        return Math.max(left, right) + 1;
    }

    private void rotateLeft(RBNode n) {
        if (n == null) {
            return;
        }

        RBNode r = n.right;
        n.right = r.left;
        if (r.left != null) {
            r.left.parent = n;
        }
        r.parent = n.parent;
        if (n.parent == null) {
            root = r;
        } else if (n.parent.left == n) {
            n.parent.left = r;
        } else {
            n.parent.right = r;
        }
        r.left = n;
        n.parent = r;
    }

    private void rotateRight(RBNode n) {
        if (n == null) {
            return;
        }

        RBNode l = n.left;
        n.left = l.right;
        if (l.right != null) {
            l.right.parent = n;
        }
        l.parent = n.parent;
        if (n.parent == null) {
            root = l;
        } else if (n.parent.right == n) {
            n.parent.right = l;
        } else {
            n.parent.left = l;
        }
        l.left = n;
        n.parent = l;
    }

    private void setColor(RBNode node, boolean color) {
        if (node != null) {
            node.color = color;
        }
    }

    private RBNode parentOf(RBNode node) {
        return node == null ? null : node.parent;
    }

    private RBNode leftOf(RBNode node) { return node == null ? null : node.left; }

    private RBNode rightOf(RBNode node) { return node == null ? null : node.right; }

    private boolean colorOf(RBNode node) {
        // null nodes are always black
        return node == null ? BLACK : node.color;
    }

    private void swapColor(RBNode n1, RBNode n2) {
        boolean tmp = colorOf(n1);
        setColor(n1, colorOf(n2));
        setColor(n2, tmp);
    }

    public String serialize() {
        StringBuilder res = new StringBuilder();
        dfs(res, root);
        res.deleteCharAt(res.length() - 1);
        return res.toString();
    }

    public String toString() {
        String[] array = serialize().split(" ");
        if (size() == 0) {
            return "[]";
        } else {
            StringBuilder res = new StringBuilder("[");
            for (int i = 0; i < array.length - 1; i++) {
                res.append(array[i]);
                res.append(", ");
            }
            res.append(array[array.length - 1]);
            res.append(']');
            return res.toString();
        }
    }

    private void dfs(StringBuilder res, RBNode root) {
        if (root == null) {
            res.append("null ");
            return;
        }
        res.append(root.val);
        res.append(" ");
        dfs(res, root.left);
        dfs(res, root.right);
    }

    class RBNode {

        int val;
        RBNode left, right, parent;
        boolean color;

        RBNode(int val) {
            this.val = val;
            left = right = parent = null;
            color = RED;
        }
    }
}

