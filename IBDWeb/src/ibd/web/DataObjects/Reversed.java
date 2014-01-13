package ibd.web.DataObjects;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Allows you to iterate through a list in reverse. Use as follows:
 * List<String> someStrings = getSomeStrings();
 * for (String s : reversed(someStrings)) {
 *     doSomethingWith(s);
 * }
 * @see http://stackoverflow.com/questions/1098117/can-one-do-a-for-each-loop-in-java-in-reverse-order
 * @author Allan
 * 
 * @param <T>
 */
public class Reversed<T> implements Iterable<T> {
    private final List<T> original;

    public Reversed(List<T> original) {
        this.original = original;
    }

    public Iterator<T> iterator() {
        final ListIterator<T> i = original.listIterator(original.size());

        return new Iterator<T>() {
            public boolean hasNext() { return i.hasPrevious(); }
            public T next() { return i.previous(); }
            public void remove() { i.remove(); }
        };
    }

    public static <T> Reversed<T> reversed(List<T> original) {
        return new Reversed<T>(original);
    }
}