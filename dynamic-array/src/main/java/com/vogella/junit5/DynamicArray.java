package com.vogella.junit5;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class DynamicArray<E> implements Iterable<E> {

    private static final int DEFAULT_CAPACITY = 16;

    private int capacity;
    private int size;
    private Object[] elements;

    //constructor
    public DynamicArray(final int capacity) {
        this.size = 0;
        this.capacity = capacity;
        this.elements = new Object[this.capacity];
    }

    //No-args constructor
    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * Adds an element to the array If full, creates a copy array twice the size
     * of the current one
     */
    public void add(final E element) {
        if (this.size == this.elements.length) {
            this.elements =
                Arrays.copyOf(this.elements, newCapacity(2 * this.capacity));
        } 

        this.elements[this.size] = element;
        size++;
    }

    //Places element of type <E> at the desired index
    public void put(final int index, E element) {
        this.elements[index] = element;
    }

    //get method for element at a given index returns null if the index is empty
    public E get(final int index) {
        return getElement(index);
    }

    //Removes an element from the array
    public E remove(final int index) {
        final E oldElement = getElement(index);
        fastRemove(this.elements, index);

        if (this.capacity > DEFAULT_CAPACITY && size * 4 <= this.capacity) {
            this.elements =
                Arrays.copyOf(this.elements, newCapacity(this.capacity / 2));
        }
        return oldElement;
    }

    //get method for size field
    public int getSize() {
        return this.size;
    }

    //isEmpty helper method
    public boolean isEmpty() {
        return this.size == 0;
    }

    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    private void fastRemove(final Object[] elements, final int index) {
        final int newSize = this.size - 1;

        if (newSize > index) {
            System.arraycopy(
                elements,
                index + 1,
                elements,
                index,
                newSize - index
            );
        }

        elements[this.size = newSize] = null;
    }

    private E getElement(final int index) {
        return (E) this.elements[index];
    }

    private int newCapacity(int capacity) {
        this.capacity = capacity;
        return this.capacity;
    }

    //returns a String representation of this object
    @Override
    public String toString() {
        return Arrays.toString(
            Arrays.stream(this.elements).filter(Objects::nonNull).toArray()
        );
    }

    //Creates and returns a new Dynamic Array Iterator
    @Override
    public Iterator iterator() {
        return new DynamicArrayIterator();
    }

    private class DynamicArrayIterator implements Iterator<E> {

        private int cursor;

        @Override
        public boolean hasNext() {
            return this.cursor != size;
        }

        @Override
        public E next() {
            if (this.cursor > DynamicArray.this.size) {
                throw new NoSuchElementException();
            }

            if (this.cursor > DynamicArray.this.elements.length) {
                throw new ConcurrentModificationException();
            }

            final E element = DynamicArray.this.getElement(this.cursor);
            this.cursor++;

            return element;
        }

        @Override
        public void remove() {
            if (this.cursor < 0) {
                throw new IllegalStateException();
            }

            DynamicArray.this.remove(this.cursor);
            this.cursor--;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);

            for (int i = 0; i < DynamicArray.this.size; i++) {
                action.accept(DynamicArray.this.getElement(i));
            }
        }
    }

    /**
     * This class is the driver for the DynamicArray<E> class it tests a variety
     * of methods and prints the output
     */
    public static void main(String[] args) {
        DynamicArray<String> names = new DynamicArray<>();
        names.add("Peubes");
        names.add("Marley");

        for (String name : names) {
            System.out.println(name);
        }

        names.stream().forEach(System.out::println);

        System.out.println(names);

        System.out.println(names.getSize());

        names.remove(0);

        for (String name : names) {
            System.out.println(name);
        }
    }
}