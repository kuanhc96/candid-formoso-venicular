package datastructures.priorityqueues;

import datastructures.EmptyContainerException;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;


import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeapPriorityQueue<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    /*
    You MUST use this field to store the contents of your heap.
    You may NOT rename this field or change its type: we will be inspecting it in our secret tests.
    */
    T[] heap;
    IDictionary<T, Integer> indices;
    int numElements;

    // Feel free to add more fields and constants.

    public ArrayHeapPriorityQueue() {
        heap = makeArrayOfT(10);
        indices = new ChainedHashDictionary<>();
        numElements = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type `T`.
     * <p>
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        /*
        This helper method is basically the same one we gave you in `ArrayDictionary` and
        `ChainedHashDictionary`.

        As before, you do not need to understand how this method works, and should not modify it in
         any way.
        */
        return (T[]) (new Comparable[arraySize]);
    }

    @Override
    public T removeMin() {
        if (numElements == 0) {
            throw new EmptyContainerException();
        }

        T min = heap[0];
        heap[0] = heap[numElements - 1];
        heap[numElements - 1] = null;
        numElements--;
        percolate(0);
        indices.remove(heap[0]);
        return min;
    }

    @Override
    public T peekMin() {
        if (numElements == 0) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (indices.containsKey(item)) {
            throw new InvalidElementException();
        }

        if (numElements + 1 > heap.length) {
            heap = doubleHeapSize();
        }
        heap[numElements] = item;
        numElements++;
        indices.put(item, numElements - 1);
        percolate(numElements - 1);
    }

    @Override
    public boolean contains(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        return indices.containsKey(item);
    }

    @Override
    public void remove(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (!indices.containsKey(item)) {
            throw new InvalidElementException();
        }
        for (int i = 0; i < numElements; i++) {
           indices.put(heap[i], i);
        }

        int index = indices.get(item);
        indices.remove(heap[index]);
        heap[index] = heap[numElements - 1];
        heap[numElements - 1] = null;
        numElements--;
        if (index != numElements) { // Avoid last element deletion
            percolate(index);
        }
    }

    @Override
    public void replace(T oldItem, T newItem) {
        if (oldItem == null || newItem == null) {
            throw new IllegalArgumentException();
        }

        if (indices.containsKey(newItem) || !indices.containsKey(oldItem)) {
            throw new InvalidElementException();
        }
        for (int i = 0; i < numElements; i++) {
           indices.put(heap[i], i);
        }
        int index = indices.get(oldItem);
        heap[index] = newItem;
        indices.remove(oldItem);
        indices.put(newItem, index);
        percolate(index);
    }

    @Override
    public int size() {
        return numElements;
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * upwards from a given index, if necessary.
     */
    private void percolateUp(int index) {
        int parent = (int) Math.floor((index - 1.0) / NUM_CHILDREN);
        if (parent >= 0) {
            if (heap[parent].compareTo(heap[index]) > 0) {
                swap(parent, index);
                percolateUp(parent);
            }
        }
    }

    /**
     * A method stub that you may replace with a helper method for percolating
     * downwards from a given index, if necessary.
     */
    private void percolateDown(int index) {
        if (index < numElements - 1) {
            T[] next4 = makeArrayOfT(NUM_CHILDREN);
            for (int i = 1; i < NUM_CHILDREN + 1; i++) {
                int next = index * NUM_CHILDREN + i;
                if (next < heap.length) {
                    next4[i - 1] = heap[next];
                }
            }
            if (next4[0] != null) {
                T min = next4[0];
                int next = 1;
                for (int i = 2; i < next4.length + 1; i++) {
                    if (next4[i - 1] != null && min.compareTo(next4[i - 1]) > 0) {
                        min = next4[i - 1];
                        next = i;
                    }
                }
                if (heap[index].compareTo(heap[index* NUM_CHILDREN + next]) > 0) {
                    swap(index, index* NUM_CHILDREN + next);
                    percolateDown(index * NUM_CHILDREN + next);
                    //percolateDown(index);
                }


            }
        }
    }


    /**
     * A method stub that you may replace with a helper method for determining
     * which direction an index needs to percolate and percolating accordingly.
     */
    private void percolate(int index) {
        percolateUp(index);
        percolateDown(index);
    }

    /**
     * A method stub that you may replace with a helper method for swapping
     * the elements at two indices in the `heap` array.
     */
    private void swap(int a, int b) {
        T temp = heap[a];
        heap[a] = heap[b];
        heap[b] = temp;
    }

    private T[] doubleHeapSize() {
        T[] newHeap = makeArrayOfT(heap.length * 2);
        for (int i = 0; i < heap.length; i++) {
            newHeap[i] = heap[i];
        }
        return newHeap;
    }

    @Override
    public String toString() {
        return IPriorityQueue.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        return new ArrayHeapIterator<>(this.heap, this.size());
    }

    private static class ArrayHeapIterator<T extends Comparable<T>> implements Iterator<T> {
        private final T[] heap;
        private final int size;
        private int index;

        ArrayHeapIterator(T[] heap, int size) {
            this.heap = heap;
            this.size = size;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return this.index < this.size;
        }

        @Override
        public T next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            T output = heap[this.index];
            this.index++;
            return output;
        }
    }
}
