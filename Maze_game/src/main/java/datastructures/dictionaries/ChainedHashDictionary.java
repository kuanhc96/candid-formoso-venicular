package datastructures.dictionaries;


import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You'll need to define reasonable default values for each of the following three fields
    private static final double DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD = 0.8; // resize when 80% full
    private static final int DEFAULT_INITIAL_CHAIN_COUNT = 10;
    private static final int DEFAULT_INITIAL_CHAIN_CAPACITY = 10;

    /*
    Warning:
    You may not rename this field or change its type.
    We will be inspecting it in our secret tests.

    Note: The field below intentionally omits the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" it becomes package-private, which means anything in
    the same package can access it. Since our tests are in the same package, they will be able
    to test this property directly.
     */
    IDictionary<K, V>[] chains; // our array dictionary

    // You're encouraged to add extra fields (and helper methods) though!
    private double resizingLoadFactorThreshold;
    private int initialChainCount;
    private int chainInitialCapacity;
    private int numElements;


    public ChainedHashDictionary() {
        this(DEFAULT_RESIZING_LOAD_FACTOR_THRESHOLD, DEFAULT_INITIAL_CHAIN_COUNT, DEFAULT_INITIAL_CHAIN_CAPACITY);
    }

    public ChainedHashDictionary(double resizingLoadFactorThreshold, int initialChainCount, int chainInitialCapacity) {
        numElements = 0;
        this.resizingLoadFactorThreshold = resizingLoadFactorThreshold;
        this.initialChainCount = initialChainCount;
        this.chainInitialCapacity = chainInitialCapacity;
        this.chains = makeArrayOfChains(initialChainCount);
    }

    /**
     * This method will return a new, empty array of the given size that can contain
     * `IDictionary<K, V>` objects.
     * <p>
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int arraySize) {
        /*
        Note: You do not need to modify this method. See `ArrayDictionary`'s `makeArrayOfPairs`
        method for more background on why we need this method.
        */
        return (IDictionary<K, V>[]) new IDictionary[arraySize];
    }

    @Override
    public V get(K key) throws NoSuchElementException { // get for chainedhashdictionary
        int keyHash = generateHashCode(key, chains.length);
        if (chains[keyHash] != null) {
            if (!chains[keyHash].containsKey(key)) {
                throw new NoSuchKeyException(); // get for arraydictionary
            }
            return chains[keyHash].get(key);
        }
        throw new NoSuchKeyException();
    }

    @Override
    public V put(K key, V value) {
        if (needResize()) {
            resize();
        }
        int keyHash = generateHashCode(key, chains.length);

        if (chains[keyHash] == null) {
            chains[keyHash] = new ArrayDictionary<>(chainInitialCapacity);
        }
        if (!chains[keyHash].containsKey(key)) {
            numElements++;
        }
        return chains[keyHash].put(key, value);
    }


    // test whether or not adding another node will cause resizing.
    // need to be called before putting anything into dictionary
    private boolean needResize() {

        return numElements / initialChainCount >= resizingLoadFactorThreshold;
    }

    // how to resize
    private void resize() {
        IDictionary<K, V>[] largerChains = makeArrayOfChains(chains.length * 2);
        int newSize = chains.length * 2;
        for (IDictionary<K, V> dict : chains) {
            if (dict != null) {
                for (KVPair<K, V> node : dict) {
                    int keyHash = generateHashCode(node.getKey(), newSize);
                    //keyHash turns out to be the index of the new hash table
                    if (largerChains[keyHash] == null) {
                        largerChains[keyHash] = new ArrayDictionary<>(chainInitialCapacity);
                    }
                    largerChains[keyHash].put(node.getKey(), node.getValue());
                }
            }
        }
        chains = largerChains;
        initialChainCount = chains.length;
    }


    @Override
    public V remove(K key) {
        V returnVal;
        int keyHash = generateHashCode(key, chains.length);
        if (chains[keyHash] != null) {

            if (chains[keyHash].size() == 1) {
                numElements--;

                returnVal = chains[keyHash].remove(key);
                chains[keyHash] = null;
                return returnVal;
            } else if (chains[keyHash].containsKey(key)) {
                numElements--;
                returnVal = chains[keyHash].remove(key);
                return returnVal;
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(K key) {
        int keyHash = generateHashCode(key, chains.length);

        if (chains[keyHash] != null) {
            return chains[keyHash].containsKey(key);
        }

        return false;
    }

    @Override
    public int size() {
        return numElements;
    }

    private int generateHashCode(K key, int length) {
        if (key != null && key != "") {
            return Math.abs(key.hashCode()) % length;
        } else {
            return 0;
        }
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    @Override
    public String toString() {
        return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        // return IDictionary.toString(this);
    }

    /*
    See the assignment webpage for tips and restrictions on implementing this iterator.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        // You may add more fields and constructor parameters
        private int outerCounter; // for indices counter


        private Iterator<KVPair<K, V>> itr;


        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.outerCounter = 0;
            this.chains = chains;

            this.itr = null;
        }

        @Override
        public boolean hasNext() {
            //return hasNextHelper();

            for (int i = outerCounter; i < chains.length; i++) {
                if (chains[i] != null && itr == null) {
                    itr = chains[i].iterator();
                }
                if (itr != null) {
                    if (itr.hasNext()) {
                        return true;
                    } else {
                        itr = null;
                    }
                }
                if (i == chains.length - 1) {
                    if (itr != null) {
                        return itr.hasNext();
                    }
                }
                outerCounter++;
            }
            return false;



        }


        @Override
        public KVPair<K, V> next() {
            if (this.hasNext()) {
                return itr.next();
            } else {
                throw new NoSuchElementException();
            }
        }

        private boolean hasNextHelper() {

            if (outerCounter == chains.length) {
                if (itr == null) {
                    return false;
                } else {
                    return itr.hasNext();
                }
            } else {
                if (itr != null) {
                    if (itr.hasNext()) {
                        return itr.hasNext();
                    } else {
                        itr = null;
                        outerCounter++;
                        return hasNextHelper();
                    }
                } else {
                    if (chains[outerCounter] != null) {
                        itr = chains[outerCounter].iterator();
                        return itr.hasNext();
                    } else {
                        outerCounter++;
                        return hasNextHelper();
                    }
                }
            }

        }

    }
}

