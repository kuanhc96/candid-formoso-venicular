package datastructures.disjointsets;

import datastructures.dictionaries.ArrayDictionary;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;

/**
 * @see IDisjointSets for more details.
 */
public class ArrayDisjointSets<T> implements IDisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    int[] pointers;
    int size;
    IDictionary<T, Integer> nodes;
    //int capacity;

    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public ArrayDisjointSets() {
        // TODO: your code here
        // throw new NotYetImplementedException();
        pointers = new int[10];
        size = 0;
        // pointers[0] = 0;
        nodes = new ChainedHashDictionary<>();
        //capacity = 10000;
    }

    @Override
    public void makeSet(T item) {
        // TODO: your code here
        // throw new NotYetImplementedException();
        if (nodes.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        // pointers[size + 1] = size + 1;
        if (needsResize()) {
            resize();
        }
        pointers[size] = -1;
        nodes.put(item, size);
        size++;
    }

    private boolean needsResize() {

        return size + 1 > pointers.length; //capacity;
    }

    private void resize() {
        //capacity = capacity*2;
        int[] largerPointers = new int[pointers.length * 2];
        for (int i = 0; i < size; i++) {
            largerPointers[i] = pointers[i];
        }
        pointers = largerPointers;
    }

    @Override
    public int findSet(T item) {
        // TODO: your code here
        // throw new NotYetImplementedException();
        if (!nodes.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int pointer = nodes.get(item);
        int next = findNextPointer(pointer);
        return next;
    }

    private int findNextPointer(int pointer) {
        //if (pointer == pointers[pointer] || pointers[pointer] < 0) {
        if (pointers[pointer] < 0) {
            return pointer;
        } else {
            int root = findNextPointer(pointers[pointer]);


            pointers[pointer] = root;
            return root;
        }
    }


    @Override
    public boolean union(T item1, T item2) {
        // TODO: your code here
        // throw new NotYetImplementedException();
        if (!nodes.containsKey(item1) || !nodes.containsKey(item2)) {
            throw new IllegalArgumentException();
        }
        int pointer2 = nodes.get(item2);
        int root1 = findSet(item1);
        int root2 = findSet(item2);
        if (root1 == root2) {
            return false;
        } else {
            if (Math.abs(pointers[root2]) <= Math.abs(pointers[root1])) {
                pointers[root2] = root1;
                if (pointers[root1] < 0) { //check rank
                    pointers[root1] = pointers[root1] - 1;
                }
            } else {
                pointers[root1] = pointers[root2];
                pointers[root2] = root1;
                pointers[pointer2] = root1;

            }
            return true;
        }


    }
}

