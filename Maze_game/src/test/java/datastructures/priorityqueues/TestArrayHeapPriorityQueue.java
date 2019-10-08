package datastructures.priorityqueues;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import datastructures.EmptyContainerException;
import misc.BaseTest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestArrayHeapPriorityQueue extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeapPriorityQueue<>();
    }

    /**
     * A helper method for accessing the private array inside an `ArrayHeapPriorityQueue`.
     */
    protected static <T extends Comparable<T>> Comparable<T>[] getArray(IPriorityQueue<T> heap) {
        return ((ArrayHeapPriorityQueue<T>) heap).heap;
    }

    @Test
    void testEmptySize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Comparable<Integer>[] array = getArray(heap);
        assertThat(heap.size(), is(0));
    }

    @Test
    void testSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 1000; i++) {
            heap.add(i);
            assertThat(heap.size(), is(i + 1));
        }
    }

    @Test
    void testEmptyContains() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        Comparable<Integer>[] array = getArray(heap);
        assertThat(heap.contains(1), is(false));
    }

    @Test
    void testContains() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 1000; i++) {
            heap.add(i);
        }

        for (int i = 0; i < 1000; i++) {
            assertThat(heap.contains(i), is(true));
        }
    }

    @Test
    void testEmptyRemoveMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        assertThrows(EmptyContainerException.class, heap::removeMin);
    }

    @Test
    void testRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        IPriorityQueue<Integer> heap2 = this.makeInstance();
        List<Integer> array = new LinkedList<Integer>(Arrays.asList(0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        List<Integer> array2 = new LinkedList<Integer>(Arrays.asList(0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        Collections.shuffle(array);
        for (int i = 0; i < array.size(); i++) {
            heap.add(array.get(i));
            heap2.add(array2.get(i));
        }

        for (int i = 0; i < array.size(); i++) {
            assertThat(heap.removeMin(), is(heap2.removeMin()));
        }
    }

    @Test
    void testEmptyPeekMinThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();

        assertThrows(EmptyContainerException.class, heap::peekMin);
    }

    @Test
    void testPeekMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        List<Integer> array = new LinkedList<Integer>(Arrays.asList(-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        Collections.shuffle(array);
        for (int i = 0; i < array.size(); i++) {
            heap.add(array.get(i));
        }

        for (int i = 0; i < array.size(); i++) {
            assertThat(heap.peekMin(), is(-32));
        }
    }

    @Test
    void testEmptyRemoveThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        //heap.remove(1);
        assertThrows(InvalidElementException.class, () -> heap.remove(1));
    }

    @Test
    void testRemoveThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        List<Integer> array = new LinkedList<Integer>(Arrays.asList(-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        int[] notInArray = {32, 15, 10, -1, -2, -4, -7, -11, -17, -44, -56, -78};
        Collections.shuffle(array);
        for (int i = 0; i < array.size(); i++) {
            heap.add(array.get(i));
        }

        for (int element : notInArray) {
            //heap.remove(element);
            assertThrows(InvalidElementException.class, () -> heap.remove(element));
        }
        //heap.remove(null);
        assertThrows(IllegalArgumentException.class, () -> heap.remove(null));
    }

    @Test
    void testRemove() {

        IPriorityQueue<Integer> heap = this.makeInstance();
        IPriorityQueue<Integer> heap2 = this.makeInstance();
        List<Integer> array = new LinkedList<Integer>(Arrays.asList(-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        List<Integer> array2 = new LinkedList<Integer>(Arrays.asList(-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        Collections.shuffle(array);
        for (int i = 0; i < array.size(); i++) {
            heap.add(array.get(i));
            heap2.add(array2.get(i));
            assertThat(heap.contains(array.get(i)), is(true));
            assertThat(heap2.contains(array2.get(i)), is(true));
        }


        for (int i = 0; i < array.size(); i++) {
            heap.remove(array.get(i));
            heap2.remove(array.get(i));
            assertThat(heap.contains(array.get(i)), is(false));
            assertThat(heap2.contains(array.get(i)), is(false));
        }
        assertThat(heap.size(), is(0));
        assertThat(heap2.size(), is(0));

    }

    @Test
    void testEmptyReplaceThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        //heap.replace(1,2);
        assertThrows(InvalidElementException.class, () -> heap.replace(1, 2));
    }

    @Test
    void testReplaceThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        List<Integer> array = new LinkedList<Integer>(Arrays.asList(-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        Collections.shuffle(array);
        int[] notInArray = {32, 15, 10, -1, -2, -4, -7, -11, -17, -44, -56, -78, -80};
        int[] inArray = {-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78};
        for (int i = 0; i < array.size(); i++) {
            heap.add(array.get(i));
        }


        assertThrows(IllegalArgumentException.class, () -> heap.replace(-32, null));


        assertThrows(IllegalArgumentException.class, () -> heap.replace(null, 1));

        for (int element : notInArray) {

            assertThrows(InvalidElementException.class, () -> heap.replace(element, 1));
        }

        int i = 0;
        for (int element : inArray) {
            int old = array.get(i);

            assertThrows(InvalidElementException.class, () -> heap.replace(old, element));
            i++;
        }
    }

    @Test
    void testReplace() {


        IPriorityQueue<Integer> heap = this.makeInstance();
        IPriorityQueue<Integer> heap2 = this.makeInstance();
        List<Integer> array = new LinkedList<Integer>(Arrays.asList(-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78));
        List<Integer> notInArray =
                new LinkedList<Integer>(Arrays.asList(32, 15, 10, -1, -2, -4, -7, -11, -17, -44, -56, -78, -80));
        Collections.shuffle(array);

        for (int i = 0; i < array.size(); i++) {
            heap.add(array.get(i));
            heap2.add(notInArray.get(i));
        }

        for (int i = 0; i < array.size(); i++) {
            heap.replace(array.get(i), notInArray.get(i));
            assertThat(heap.contains(notInArray.get(i)), is(true));
            assertThat(heap.size(), is(array.size()));
        }

        for (int i = 0; i < array.size(); i++) {
            assertThat(heap.removeMin(), is(heap2.removeMin()));
        }
    }


    @Test
    void testAddEmptyInternalArray() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.add(3);
        Comparable<Integer>[] array = getArray(heap);
        assertThat(array[0], is(3));
    }

    @Test
    void testAddThrowsException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int[] array = {-32, -15, -6, 0, 2, 4, 6, 7, 11, 17, 44, 56, 78};

        assertThrows(IllegalArgumentException.class, () -> heap.add(null));

        for (int i = 0; i < array.length; i++) {
            heap.add(array[i]);
        }

        for (int element : array) {
            assertThrows(InvalidElementException.class, () -> heap.add(element));
        }
    }

    @Test
    void testUpdateDecrease() {
        IPriorityQueue<IntPair> heap = this.makeInstance();
        for (int i = 1; i <= 5; i++) {
            heap.add(new IntPair(i, i));
        }

        heap.replace(new IntPair(2, 2), new IntPair(0, 0));

        assertThat(heap.removeMin(), is(new IntPair(0, 0)));
        assertThat(heap.removeMin(), is(new IntPair(1, 1)));
    }

    @Test
    void testUpdateIncrease() {
        IntPair[] values = IntPair.createArray(new int[][]{{0, 0}, {2, 2}, {4, 4}, {6, 6}, {8, 8}});
        IPriorityQueue<IntPair> heap = this.makeInstance();

        for (IntPair value : values) {
            heap.add(value);
        }

        IntPair newValue = new IntPair(5, 5);
        heap.replace(values[0], newValue);

        assertThat(heap.removeMin(), is(values[1]));
        assertThat(heap.removeMin(), is(values[2]));
        assertThat(heap.removeMin(), is(newValue));
    }


}
