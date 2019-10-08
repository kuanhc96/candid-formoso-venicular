package datastructures.lists;

//import com.sun.java.util.jar.pack.ConstantPool;
import datastructures.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * Note: For more info on the expected behavior of your methods:
 * @see IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    /*
    Warning:
    You may not rename these fields or change their types.
    We will be inspecting these in our secret tests.
    You also may not add any additional fields.

    Note: The fields below intentionally omit the "private" keyword. By leaving off a specific
    access modifier like "public" or "private" they become package-private, which means anything in
    the same package can access them. Since our tests are in the same package, they will be able
    to test these properties directly.
     */
    Node<T> front;
    Node<T> back;
    int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (size() == 0) {
            Node<T> newNode = new Node<T>(null, item, null);
            front = newNode;
            back = newNode;
        }else {
            Node<T> current = back;

            Node<T> newNode = new Node<T>(current, item, null);
            current.next = newNode;
            back = newNode;
        }
        size++;

    }

    @Override
    public T remove() throws EmptyContainerException {
        if (size() > 0) {

            Node<T> current = front;
            if (size() == 1) {

                front = null;
                back = null;
                size--;
            } else {

                while (current.next != null) {
                    current = current.next;
                }
                back = current.prev;
                back.next = null;
                current.prev = null;
                size--;
            }
            return current.data;
        } else {
            throw new EmptyContainerException();
        }

    }

    @Override
    public T get(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> current;
        if(index<= size()/2) {
            current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = back;
            for(int i = size()-1; i>index; i--) {
                current = current.prev;
            }
        }

        return current.data;
    }

    @Override
    public T set(int index, T item) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {


            if (size() == 1) {
                Node<T> current = front;
                Node<T> newNode = new Node<T>(item);
                front = newNode;
                back = newNode;
                return current.data;
            } else {
                if (index == 0) {
                    Node<T> current = front;
                    Node<T> newNode = new Node<T>(null, item, front.next);
                    front = newNode;
                    current.next.prev = front;
                    return current.data;
                } else if (index == size() - 1) {
                    Node<T> current = back;
                    Node<T> newNode = new Node<T>(back.prev, item, null);
                    back = newNode;
                    current.prev.next = back;
                    return current.data;
                } else {
                    Node<T> current;
                    Node<T> current3;
                    if(index<=size()/2) {
                        current = front;
                        for (int i = 0; i < index - 1; i++) {
                            current = current.next;
                        }
                        current3 = current.next;
                        Node<T> current2 = current.next.next;
                        Node<T> newNode = new Node<T>(current, item, current2);
                        current.next = newNode;
                        current2.prev = newNode;
                    } else {
                        current = back;
                        for (int i = size()-1; i > index + 1; i--) {
                            current = current.prev;
                        }
                        current3 = current.prev;
                        Node<T> current2 = current.prev.prev;
                        Node<T> newNode = new Node<T>(current2, item, current);
                        current.prev = newNode;
                        current2.next = newNode;
                    }
                    return current3.data;
                }
            }
        }
    }

    @Override
    public void insert(int index, T item) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size() + 1) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> newNode = new Node<T>(item);
            if (size() == 0) {
                front = newNode;
                back = newNode;
            } else {

                if (index == 0) { // Check later
                    Node<T> current = front;
                    front = newNode;
                    front.next = current;
                    current.prev = front;
                } else if (index == size()) {
                    Node<T> current = back;
                    back = newNode;
                    back.prev = current;
                    current.next = back;
                } else {
                    if (index < size()/2) {
                        Node<T> current = front;
                        for (int i = 0; i < index-1; i++) {
                            current = current.next;
                        }
                        Node<T> current2 = current.next;
                        current.next = newNode;
                        current.next.next = current2;
                        current2.prev = current.next;
                        current.next.prev = current;
                    } else {
                        Node<T> current = back;
                        for (int j = size() - 1; j > index; j--) {
                            current = current.prev;
                        }
                        Node<T> current2 = current.prev;
                        current.prev = newNode;
                        current.prev.prev = current2;
                        current2.next = current.prev;
                        current.prev.next = current;
                    }
                }
            }
            size++;
        }

    }

    @Override
    public T delete(int index) throws IndexOutOfBoundsException {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        } else {
            Node<T> current;
            if (index == 0) {
                current = front;
                front = front.next;
                if (front != null) {
                    front.prev = null;
                }
                if(back.prev == null) {
                    back = null;
                }
                size--;
                return current.data;
            } else if (index == size() - 1) {
                current = back;
                back = back.prev;
                if (back != null) {
                    back.next = null;
                }
                size--;
                return current.data;
            } else {
                if(index<=size()/2) {
                    current = front;
                    for (int i = 0; i < index - 1; i++) {

                        current = current.next;
                    }
                    Node<T> current2 = current.next;
                    current.next = current.next.next;
                    current.next.prev = current;
                    size--;
                    return current2.data;
                } else{
                    current = back;
                    for (int i = size()-1; i > index + 1; i--) {
                        current = current.prev;
                    }
                    Node<T> current2 = current.prev;
                    current.prev = current.prev.prev;
                    current.prev.next = current;
                    size--;
                    return current2.data;
                }

            }

        }


    }

    @Override
    public int indexOf(T item)  {
        if (size() == 0) {
            return -1;
        } else {
            //int index = 0;
            int index = 0;
            Node<T> current = front;
            while (current != null) {
                if (current.data == null) {
                    if (current.data == item) {
                        return index;
                    }
                }else if (current.data == item || current.data.equals(item)){
                    return index;
                }
                index++;
                current = current.next;
            }
            return -1;
        }
    }


    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }

    @Override
    public String toString() {
        //return super.toString();

        /*
        After you've implemented the iterator, comment out the line above and uncomment the line
        below to get a better string representation for objects in assertion errors and in the
        debugger.
        */

        return IList.toString(this);
    }

    @Override
    public Iterator<T> iterator() {
        /*
        Note: we have provided a part of the implementation of an iterator for you. You should
        complete the methods stubs in the DoubleLinkedListIterator inner class at the bottom of
        this file. You do not need to change this method.
        */
        return new DoubleLinkedListIterator<>(this.front);
    }

    static class Node<E> {
        // You may not change the fields in this class or add any new fields.
        final E data;
        Node<E> prev;
        Node<E> next;

        Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> next;

        public DoubleLinkedListIterator(Node<T> front) {
            // You do not need to make any changes to this constructor.
            this.next = front;
        }

        /**
         * Returns `true` if the iterator still has elements to look at;
         * returns `false` otherwise.
         */
        public boolean hasNext() {
            return next != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() throws NoSuchElementException {
            if (this.next == null) {
                throw new NoSuchElementException();
            }
            T result =  this.next.data;
            this.next = this.next.next;

            return result;
        }
    }
}
