package redblacktree;

import java.util.NoSuchElementException;

public class RedBlackTree<K extends Comparable<? super K>, V> {

  Node<K, V> root;

  public RedBlackTree() {
    this.root = null;
  }

  RedBlackTree(Node<K, V> root) {
    this.root = root;
  }

  public void put(K key, V value) {

    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);

    Node<K, V> parent = pair.getX();
    Node<K, V> current = pair.getY();

    if (current != null) { // found an existing key
      current.setValue(value);
      return;
    }

    if (parent == null) { // empty tree
      root = new Node<K, V>(key, value, Colour.BLACK);
      return;
    }
    /* create a new key */
    int comparison = key.compareTo(parent.getKey());
    Node<K, V> newNode = new Node<K, V>(key, value, Colour.RED);
    if (comparison < 0) {
      parent.setLeft(newNode);
    } else {
      assert comparison > 0;
      parent.setRight(newNode);
    }

    insertCaseOne(newNode);

  }

  private void insertCaseOne(Node<K, V> current) {
    if (current.getParent() == null) {
      current.setBlack();
    } else {
      insertCaseTwo(current);
    }
  }

  private void insertCaseTwo(Node<K, V> current) {
    if (!current.getParent().isBlack()) {
      insertCaseThree(current);
    }
  }

  private void insertCaseThree(Node<K, V> current) {
    if (current.hasUncle() && current.getUncle().isRed()) {
      current.getParent().setBlack();
      current.getUncle().setBlack();
      current.getGrandparent().setRed();
      insertCaseOne(current.getGrandparent());
    } else {
      insertCaseFour(current);
    }
  }

  private void insertCaseFour(Node<K, V> current) {
    Node<K, V> grandparent = current.getGrandparent();
    Node<K, V> parent = current.getParent();
    if (parent.equals(grandparent.getLeft()) && current.equals(parent.getRight())) {
      parent.rotateLeft();
      insertCaseFive(parent);
    } else if (parent.equals(grandparent.getRight()) && current.equals(parent.getLeft())) {
      parent.rotateRight();
      insertCaseFive(parent);
    } else {
     insertCaseFive(current);
    }
  }

  private void insertCaseFive(Node<K, V> current) {
    boolean isRoot = current.getGrandparent().getParent() == null;
    Node<K, V> grandParent = current.getGrandparent();
    Node<K, V> parent = current.getParent();
    parent.setBlack();
    grandParent.setRed();
    Node<K, V> topNode = root;
    if (current.equals(parent.getLeft())) {
      topNode = grandParent.rotateRight();
    }
    if (current.equals(parent.getRight())) {
     topNode = grandParent.rotateLeft();
    }
    if (isRoot) {
      root = topNode;
    }
  }

  private Tuple<Node<K, V>, Node<K, V>> findNode(K key) {
    Node<K, V> current = root;
    Node<K, V> parent = null;

    while (current != null) {
      parent = current;

      int comparison = key.compareTo(current.getKey());
      if (comparison < 0) {
        current = current.getLeft();
      } else if (comparison == 0) {
        break;
      } else {
        assert comparison > 0;
        current = current.getRight();
      }
    }
    return new Tuple<Node<K, V>, Node<K, V>>(parent, current);
  }

  public boolean contains(K key) {
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    return pair.getY() != null;
  }

  public V get(K key) {
    Tuple<Node<K, V>, Node<K, V>> pair = findNode(key);
    Node<K, V> current = pair.getY();
    if (current == null) {
      throw new NoSuchElementException();
    }
    return current.getValue();
  }

  public void clear() {
    this.root = null;
  }

  public String toString() {
    return "RBT " + root + " ";
  }
}
