import java.util.Iterator;

/**
 * Name: MyQuadraticHashSet.java
 * Description: HashSet using quadratic probing. Implements the MySet interface and tables a load factor and sizes the set can be.
 * @author Will St. Onge
 */

public class MyQuadraticHashSet<E> implements MySet<E> {

    private int size, capacity, sizesIndex;
    private int[] sizes;
    private double loadFactor;
    private Object[] table;
    private final Object REMOVED = new Object();

    public MyQuadraticHashSet(double loadFactor, int[] sizes) {
        this.sizes = sizes;
        this.loadFactor = loadFactor;
        this.capacity = sizes[0];
        this.table = new Object[capacity];
        this.sizesIndex = 0;
    }

    @Override
    public boolean contains(E e) {
        int i = probeIndex(e.hashCode(), 0, capacity), count = 1;

        while(!e.equals(table[i]) && table[i] != null) {
            i = probeIndex(e.hashCode(), count, capacity);
            count++;
        }
        return e.equals(table[i]);
    }

    @Override
    public boolean add(E e) {
        if(contains(e))
            return false;
        if(size > capacity * loadFactor)
            resize();
        int i = probeIndex(e.hashCode(), 0, capacity), count = 1;

        while(table[i] != null && table[i] != REMOVED) {
            i = probeIndex(e.hashCode(), count, capacity);
            count++;
        }
        table[i] = e;
        size++;
        return true;
    }

    @Override
    public boolean remove(E e) {
        if(!contains(e))
            return false;
        int i = probeIndex(e.hashCode(), 0, capacity), count = 1;

        while(!e.equals(table[i]) && table[i] != REMOVED) {
            i = probeIndex(e.hashCode(), count, capacity);
            count++;
        }
        table[i] = null;
        size--;

        return true;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        Object[] prevTable = table;
        capacity = sizes[sizesIndex += 1];
        size = 0;
        table = new Object[capacity];

        for(Object obj : prevTable)
            if(obj != null)
                add((E)obj);
    }

    @Override
    public void clear() {
        for (int i = 0; i < capacity; i++)
            table[i] = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException();
    }

    private int probeIndex(int hashCode, long modifier, int tableLength) {
        return (int)((hashCode % tableLength + tableLength + modifier * modifier) % tableLength);
    }
}