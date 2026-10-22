package org.xmlet.htmlapifaster;

import java.util.Iterator;

/**
 * An item of a collection and its position, as forEachIndexedOf passes it to the item template.
 *
 * @param <E> The type of the item
 * @author Bernardo Pereira
 */
public final class Indexed<E> {

    private final E item;
    private final int index;

    private Indexed(E item, int index) {
        this.item = item;
        this.index = index;
    }

    /**
     * @return The item
     */
    public E item() {
        return item;
    }

    /**
     * @return The position of the item, starting at 0
     */
    public int index() {
        return index;
    }

    /**
     * Pairs each item with its position as the iteration reaches it, without copying the items.
     */
    static <E> Iterable<Indexed<E>> over(Iterable<E> items) {
        return () -> new Iterator<Indexed<E>>() {
            private final Iterator<E> it = items.iterator();
            private int index = 0;

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public Indexed<E> next() {
                return new Indexed<>(it.next(), index++);
            }
        };
    }
}
