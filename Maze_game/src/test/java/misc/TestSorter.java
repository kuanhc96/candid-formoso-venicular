package misc;

import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static datastructures.lists.IListMatcher.listContaining;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * See spec for details on what kinds of tests this class should include.
 */
@Tag("project3")
@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class TestSorter extends BaseTest {
    @Test
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }

        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(15, 16, 17, 18, 19)));
    }

    @Test
    void whenKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(0, list);
        assertThat(top.isEmpty(), is(true));
    }


    @Test
    void listAlreadySorted() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 10; i >=0; i--) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertThat(top, is(listContaining(6, 7, 8, 9, 10)));
    }
}

