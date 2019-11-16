package util;

import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: sulcanto
 * Date: 5/25/13
 * Time: 3:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class CyclicStack<T> {
    ArrayBlockingQueue<T> queue;

    public CyclicStack(int capacity) {
        this.queue = new ArrayBlockingQueue<T>(capacity);
    }

    public boolean add(T element) {
        if (queue.remainingCapacity() == 0) {
            queue.remove();
        }
        return queue.add(element);
    }

    public T peek() {
        T val = null;
        for (Iterator<T> it = queue.iterator(); it.hasNext(); ) {
            val = it.next();
        }
        return val;
    }

    public T first() {
        return iterator().next();
    }

    public T last() {
        return peek();
    }

    public int size() {
        return queue.size();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public Iterator<T> iterator() {
        return queue.iterator();
    }
}
