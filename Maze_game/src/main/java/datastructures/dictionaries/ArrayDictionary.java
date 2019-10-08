package datastructures.dictionaries;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */

    //private Pair<K, V>[] pairs;
    Pair<K, V>[] pairs;

    // You may add extra fields or helper methods though!

    private int size;
    private static final int DEFAULT_INITIAL_CAPACITY = 10;
    public ArrayDictionary() {
        this(DEFAULT_INITIAL_CAPACITY );
    }

    //public ArrayDictionary() { // when we have nothing
    // pairs = makeArrayOfPairs(100);
    // size = 0; //no contents
    // }
    public ArrayDictionary(int initialCapacity) {
        pairs = makeArrayOfPairs(initialCapacity);
        size = 0;
    }

    private Pair<K, V>[] copyContents(Pair<K, V>[] array) {
        Pair<K, V>[] largerArray = makeArrayOfPairs(array.length*2);
        for (int i = 0; i < array.length; i++) {
            largerArray[i]  = array[i];
        }
        return largerArray;
    }

    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        /*
        It turns out that creating arrays of generic objects in Java is complicated due to something
        known as "type erasure."

        We've given you this helper method to help simplify this part of your assignment. Use this
        helper method as appropriate when implementing the rest of this class.

        You are not required to understand how this method works, what type erasure is, or how
         arrays and generics interact. Do not modify this method in any way.
        */
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    public V get(K key) throws NoSuchKeyException {
        for (int i = 0; i < size; i++){
            if (pairs[i].key == key || pairs[i].key.equals(key)) {
                return pairs[i].value;
            }
        }
        throw new NoSuchKeyException();
    }

    @Override
    public V put(K key, V value) {
        if (!containsKey(key)) {
            Pair<K, V> newPair = new Pair<>(key, value);
            if (this.size >= pairs.length){
                this.pairs = copyContents(pairs);
            }
            pairs[size] = newPair;
            size++;
            return null;
        } else {
            V oldValue = get(key);
            for (int i = 0; i < size; i++) {
                if (pairs[i].key == null){
                    if (pairs[i].key == key) {
                        pairs[i].value = value;
                    }
                } else if (pairs[i].key == key || pairs[i].key.equals(key)) {
                    pairs[i].value = value;
                }
            }
            return oldValue;
        }
    }

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            return null;
        }
        if (size == 1) {
            V value = pairs[0].value;
            size--;
            pairs[0] = null;
            return value;
        } else {
            V oldValue;
            for (int i = 0; i < size; i++) {
                if (pairs[i].key == null) {
                    if (pairs[i].key == key) {
                        oldValue = pairs[i].value;
                        pairs[i] = pairs[size-1];
                        pairs[size-1] = null;
                        size--;
                        return oldValue;
                    }
                }else if (pairs[i].key == key || pairs[i].key.equals(key)) {
                    oldValue = pairs[i].value;
                    pairs[i] = pairs[size-1];
                    pairs[size-1] = null;
                    size--;
                    return oldValue;
                }
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size; i++) {
            if (pairs[i].key == null){
                if (pairs[i].key == key){
                    return true;
                }
            }else if (pairs[i].key == key || pairs[i].key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator(pairs);
    }

    @Override
    public String toString() {
        //return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return IDictionary.toString(this);
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;

        }

        @Override
        public String toString() {
            return String.format("%s=%s", this.key, this.value);
        }
    }

    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        // You'll need to add some fields
        Pair<K, V>[] next;
        int counter;

        public ArrayDictionaryIterator(Pair<K, V>[] pairs) {
            this.next = pairs;
            this.counter = 0;
        }

        @Override
        public boolean hasNext() {
            return counter < next.length && next[counter] != null;
        }

        @Override
        public KVPair<K, V> next() throws NoSuchElementException {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            KVPair<K, V> nextPair = new KVPair<>(next[counter].key, next[counter].value);
            counter++;

            return nextPair;
        }
    }
}
