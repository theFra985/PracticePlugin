/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.gson.internal.LinkedTreeMap.LinkedTreeMapIterator
 */
package com.google.gson.internal;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

public final class LinkedTreeMap<K, V>
extends AbstractMap<K, V>
implements Serializable {
    private static final Comparator<Comparable> NATURAL_ORDER = new Comparator<Comparable>(){

        @Override
        public int compare(Comparable comparable, Comparable comparable2) {
            return comparable.compareTo(comparable2);
        }
    };
    Comparator<? super K> comparator;
    Node<K, V> root;
    int size = 0;
    int modCount = 0;
    final Node<K, V> header = new Node();
    private LinkedTreeMap<K, V> entrySet;
    private LinkedTreeMap<K, V> keySet;

    public LinkedTreeMap() {
        this(NATURAL_ORDER);
    }

    public LinkedTreeMap(Comparator<? super K> comparator) {
        this.comparator = comparator != null ? comparator : NATURAL_ORDER;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public V get(Object object) {
        Node<K, V> node = this.findByObject(object);
        return node != null ? (V)node.value : null;
    }

    @Override
    public boolean containsKey(Object object) {
        return this.findByObject(object) != null;
    }

    @Override
    public V put(K k, V v) {
        if (k == null) {
            throw new NullPointerException("key == null");
        }
        Node<K, V> node = this.find(k, true);
        Object v2 = node.value;
        node.value = v;
        return v2;
    }

    @Override
    public void clear() {
        this.root = null;
        this.size = 0;
        ++this.modCount;
        Node<K, V> node = this.header;
        node.prev = node;
        node.next = node.prev;
    }

    @Override
    public V remove(Object object) {
        Node<K, V> node = this.removeInternalByKey(object);
        return node != null ? (V)node.value : null;
    }

    Node<K, V> find(K k, boolean bl) {
        Node<K, V> node;
        Object object;
        Comparator<K> comparator = this.comparator;
        Node<K, V> node2 = this.root;
        int n = 0;
        if (node2 != null) {
            object = comparator == NATURAL_ORDER ? (Comparable)k : null;
            do {
                int n2 = n = object != null ? object.compareTo(node2.key) : comparator.compare(k, node2.key);
                if (n == 0) {
                    return node2;
                }
                Node node3 = node = n < 0 ? node2.left : node2.right;
                if (node == null) break;
                node2 = node;
            } while (true);
        }
        if (!bl) {
            return null;
        }
        object = this.header;
        if (node2 == null) {
            if (comparator == NATURAL_ORDER && !(k instanceof Comparable)) {
                throw new ClassCastException(k.getClass().getName() + " is not Comparable");
            }
            node = new Node<K, V>(node2, k, (Node<K, V>)object, object.prev);
            this.root = node;
        } else {
            node = new Node<K, V>(node2, k, (Node<K, V>)object, object.prev);
            if (n < 0) {
                node2.left = node;
            } else {
                node2.right = node;
            }
            this.rebalance(node2, true);
        }
        ++this.size;
        ++this.modCount;
        return node;
    }

    Node<K, V> findByObject(Object object) {
        try {
            return object != null ? this.find(object, false) : null;
        }
        catch (ClassCastException var2_2) {
            return null;
        }
    }

    Node<K, V> findByEntry(Map.Entry<?, ?> entry) {
        Node<K, V> node = this.findByObject(entry.getKey());
        boolean bl = node != null && this.equal(node.value, entry.getValue());
        return bl ? node : null;
    }

    private boolean equal(Object object, Object object2) {
        return object == object2 || object != null && object.equals(object2);
    }

    void removeInternal(Node<K, V> node, boolean bl) {
        if (bl) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node.parent;
        if (node2 != null && node3 != null) {
            Node node5 = node2.height > node3.height ? node2.last() : node3.first();
            this.removeInternal(node5, false);
            int n = 0;
            node2 = node.left;
            if (node2 != null) {
                n = node2.height;
                node5.left = node2;
                node2.parent = node5;
                node.left = null;
            }
            int n2 = 0;
            node3 = node.right;
            if (node3 != null) {
                n2 = node3.height;
                node5.right = node3;
                node3.parent = node5;
                node.right = null;
            }
            node5.height = Math.max(n, n2) + 1;
            this.replaceInParent(node, node5);
            return;
        }
        if (node2 != null) {
            this.replaceInParent(node, node2);
            node.left = null;
        } else if (node3 != null) {
            this.replaceInParent(node, node3);
            node.right = null;
        } else {
            this.replaceInParent(node, null);
        }
        this.rebalance(node4, false);
        --this.size;
        ++this.modCount;
    }

    Node<K, V> removeInternalByKey(Object object) {
        Node<K, V> node = this.findByObject(object);
        if (node != null) {
            this.removeInternal(node, true);
        }
        return node;
    }

    private void replaceInParent(Node<K, V> node, Node<K, V> node2) {
        Node node3 = node.parent;
        node.parent = null;
        if (node2 != null) {
            node2.parent = node3;
        }
        if (node3 != null) {
            if (node3.left == node) {
                node3.left = node2;
            } else {
                assert (node3.right == node);
                node3.right = node2;
            }
        } else {
            this.root = node2;
        }
    }

    private void rebalance(Node<K, V> node, boolean bl) {
        Node<K, V> node2 = node;
        while (node2 != null) {
            int n;
            Node node3;
            Node node4;
            int n2;
            int n3;
            Node node5;
            int n4;
            Node node6 = node2.left;
            int n5 = node6 != null ? node6.height : 0;
            int n6 = n5 - (n3 = (node5 = node2.right) != null ? node5.height : 0);
            if (n6 == -2) {
                node4 = node5.left;
                n2 = node4 != null ? node4.height : 0;
                n4 = n2 - (n = (node3 = node5.right) != null ? node3.height : 0);
                if (n4 == -1 || n4 == 0 && !bl) {
                    this.rotateLeft(node2);
                } else {
                    assert (n4 == 1);
                    this.rotateRight(node5);
                    this.rotateLeft(node2);
                }
                if (bl) {
                    break;
                }
            } else if (n6 == 2) {
                node4 = node6.left;
                n2 = node4 != null ? node4.height : 0;
                n4 = n2 - (n = (node3 = node6.right) != null ? node3.height : 0);
                if (n4 == 1 || n4 == 0 && !bl) {
                    this.rotateRight(node2);
                } else {
                    assert (n4 == -1);
                    this.rotateLeft(node6);
                    this.rotateRight(node2);
                }
                if (bl) {
                    break;
                }
            } else if (n6 == 0) {
                node2.height = n5 + 1;
                if (bl) {
                    break;
                }
            } else {
                assert (n6 == -1 || n6 == 1);
                node2.height = Math.max(n5, n3) + 1;
                if (!bl) break;
            }
            node2 = node2.parent;
        }
    }

    private void rotateLeft(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node3.left;
        Node node5 = node3.right;
        node.right = node4;
        if (node4 != null) {
            node4.parent = node;
        }
        this.replaceInParent(node, node3);
        node3.left = node;
        node.parent = node3;
        node.height = Math.max(node2 != null ? node2.height : 0, node4 != null ? node4.height : 0) + 1;
        node3.height = Math.max(node.height, node5 != null ? node5.height : 0) + 1;
    }

    private void rotateRight(Node<K, V> node) {
        Node node2 = node.left;
        Node node3 = node.right;
        Node node4 = node2.left;
        Node node5 = node2.right;
        node.left = node5;
        if (node5 != null) {
            node5.parent = node;
        }
        this.replaceInParent(node, node2);
        node2.right = node;
        node.parent = node2;
        node.height = Math.max(node3 != null ? node3.height : 0, node5 != null ? node5.height : 0) + 1;
        node2.height = Math.max(node.height, node4 != null ? node4.height : 0) + 1;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        LinkedTreeMap<K, V> linkedTreeMap = this.entrySet;
        LinkedTreeMap<K, V> linkedTreeMap2 = linkedTreeMap != null ? linkedTreeMap : (this.entrySet = new EntrySet());
        return linkedTreeMap2;
    }

    @Override
    public Set<K> keySet() {
        LinkedTreeMap<K, V> linkedTreeMap = this.keySet;
        LinkedTreeMap<K, V> linkedTreeMap2 = linkedTreeMap != null ? linkedTreeMap : (this.keySet = new KeySet());
        return linkedTreeMap2;
    }

    private Object writeReplace() {
        return new LinkedHashMap(this);
    }

    final class KeySet
    extends AbstractSet<K> {
        KeySet() {
        }

        @Override
        public int size() {
            return LinkedTreeMap.this.size;
        }

        @Override
        public Iterator<K> iterator() {
            return new LinkedTreeMap<K, V>(){

                public K next() {
                    return this.nextNode().key;
                }
            };
        }

        @Override
        public boolean contains(Object object) {
            return LinkedTreeMap.this.containsKey(object);
        }

        @Override
        public boolean remove(Object object) {
            return LinkedTreeMap.this.removeInternalByKey(object) != null;
        }

        @Override
        public void clear() {
            LinkedTreeMap.this.clear();
        }

    }

    class EntrySet
    extends AbstractSet<Map.Entry<K, V>> {
        EntrySet() {
        }

        @Override
        public int size() {
            return LinkedTreeMap.this.size;
        }

        @Override
        public Iterator<Map.Entry<K, V>> iterator() {
            return new com.google.gson.internal.LinkedTreeMap.LinkedTreeMapIterator<Map.Entry<K, V>>(){

                public Map.Entry<K, V> next() {
                    return this.nextNode();
                }
            };
        }

        @Override
        public boolean contains(Object object) {
            return object instanceof Map.Entry && LinkedTreeMap.this.findByEntry((Map.Entry)object) != null;
        }

        @Override
        public boolean remove(Object object) {
            if (!(object instanceof Map.Entry)) {
                return false;
            }
            Node node = LinkedTreeMap.this.findByEntry((Map.Entry)object);
            if (node == null) {
                return false;
            }
            LinkedTreeMap.this.removeInternal(node, true);
            return true;
        }

        @Override
        public void clear() {
            LinkedTreeMap.this.clear();
        }

    }

    private abstract class LinkedTreeMapIterator<T>
    implements Iterator<T> {
        Node<K, V> next;
        Node<K, V> lastReturned;
        int expectedModCount;

        LinkedTreeMapIterator() {
            this.next = LinkedTreeMap.this.header.next;
            this.lastReturned = null;
            this.expectedModCount = LinkedTreeMap.this.modCount;
        }

        @Override
        public final boolean hasNext() {
            return this.next != LinkedTreeMap.this.header;
        }

        final Node<K, V> nextNode() {
            Node<K, V> node = this.next;
            if (node == LinkedTreeMap.this.header) {
                throw new NoSuchElementException();
            }
            if (LinkedTreeMap.this.modCount != this.expectedModCount) {
                throw new ConcurrentModificationException();
            }
            this.next = node.next;
            this.lastReturned = node;
            return this.lastReturned;
        }

        @Override
        public final void remove() {
            if (this.lastReturned == null) {
                throw new IllegalStateException();
            }
            LinkedTreeMap.this.removeInternal(this.lastReturned, true);
            this.lastReturned = null;
            this.expectedModCount = LinkedTreeMap.this.modCount;
        }
    }

    static final class Node<K, V>
    implements Map.Entry<K, V> {
        Node<K, V> parent;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> next;
        Node<K, V> prev;
        final K key;
        V value;
        int height;

        Node() {
            this.key = null;
            this.next = this.prev = this;
        }

        Node(Node<K, V> node, K k, Node<K, V> node2, Node<K, V> node3) {
            this.parent = node;
            this.key = k;
            this.height = 1;
            this.next = node2;
            this.prev = node3;
            node3.next = this;
            node2.prev = this;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public V getValue() {
            return this.value;
        }

        @Override
        public V setValue(V v) {
            V v2 = this.value;
            this.value = v;
            return v2;
        }

        @Override
        public boolean equals(Object object) {
            if (object instanceof Map.Entry) {
                Map.Entry entry = (Map.Entry)object;
                return (this.key == null ? entry.getKey() == null : this.key.equals(entry.getKey())) && (this.value == null ? entry.getValue() == null : this.value.equals(entry.getValue()));
            }
            return false;
        }

        @Override
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ (this.value == null ? 0 : this.value.hashCode());
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public Node<K, V> first() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.left;
            while (node2 != null) {
                node = node2;
                node2 = node.left;
            }
            return node;
        }

        public Node<K, V> last() {
            Node<K, V> node = this;
            Node<K, V> node2 = node.right;
            while (node2 != null) {
                node = node2;
                node2 = node.right;
            }
            return node;
        }
    }

}

