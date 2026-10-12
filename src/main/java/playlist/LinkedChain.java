package playlist;

/**
 * A generic chain of linked nodes. Use it as the storage inside your
 * own classes, and extend or modify it as your project needs.
 *
 * This class owns storage only: nodes, the head reference, and the
 * count. It contains no application logic. A class that needs a
 * collection should hold a LinkedChain field and call its methods.
 *
 * add() places the new entry at the FRONT of the chain, so entries added
 * with add() come back from toArray() in reverse order of insertion.
 * addToEnd() preserves insertion order.
 *
 * Contract (Spec Section 3): no bound on T; entries are never null;
 * duplicates are allowed; search uses equals() and acts on the first
 * match. add, addToEnd and addAt throw IllegalArgumentException on null.
 * remove(T), contains, count and indexOf treat null as "not found".
 *
 * Time complexity, where n is the number of entries:
 *   add              O(1)
 *   addToEnd         O(n)
 *   addAt            O(n)
 *   remove()         O(1)
 *   remove(T)        O(n)
 *   removeAt         O(n)
 *   clear            O(1)
 *   get              O(n)
 *   indexOf          O(n)
 *   contains         O(n)
 *   count            O(n)
 *   size             O(1)
 *   isEmpty          O(1)
 *   toArray          O(n)
 *   toString         O(n)
 *
 * @author CS 2114 Staff
 * @version September 2026
 *
 * @param <T> the type of entries stored in the chain
 */
public class LinkedChain<T>
{
    private Node<T> firstNode;   // reference to the first node, or null
    private int numberOfEntries; // number of nodes in the chain

    /**
     * Creates an empty chain.
     */
    public LinkedChain()
    {
        firstNode = null;
        numberOfEntries = 0;
    }

    /**
     * Adds a new entry to the front of this chain. O(1).
     *
     * @param newEntry the object to be added; must not be null
     * @return true
     * @throws IllegalArgumentException if newEntry is null
     */
    public boolean add(T newEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Adds a new entry to the end of this chain. O(n): walks to the
     * last node.
     *
     * @param newEntry the object to be added; must not be null
     * @return true
     * @throws IllegalArgumentException if newEntry is null
     */
    public boolean addToEnd(T newEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Inserts a new entry so that it lands at the given position.
     * Entries at or after that position shift back by one. O(n): walks
     * to the node before index.
     *
     * @param index the zero-based position, 0 to size() inclusive
     * @param newEntry the object to be added; must not be null
     * @throws IndexOutOfBoundsException if index is negative or
     *         greater than size()
     * @throws IllegalArgumentException if newEntry is null
     */
    public void addAt(int index, T newEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes and returns the first entry in this chain, if possible.
     * O(1).
     *
     * @return the removed entry, or null if the chain is empty
     */
    public T remove()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes the first occurrence of a given entry from this chain,
     * using equals(). O(n).
     *
     * @param anEntry the entry to be removed; null is never found
     * @return true if the removal was successful, or false if anEntry
     *         is absent or null
     */
    public boolean remove(T anEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes and returns the entry at the given position. O(n).
     *
     * @param index the zero-based position
     * @return the removed entry
     * @throws IndexOutOfBoundsException if index is negative or
     *         index is greater than or equal to size()
     */
    public T removeAt(int index)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Removes all entries from this chain. O(1).
     */
    public void clear()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the entry at the given position without removing it. O(n):
     * walks from the head, so calling get(i) in a loop is O(n^2); use
     * toArray instead to visit every entry.
     *
     * @param index the zero-based position
     * @return the entry at that position
     * @throws IndexOutOfBoundsException if index is negative or
     *         index is greater than or equal to size()
     */
    public T get(int index)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Finds the position of the first entry equal to a given entry.
     * O(n).
     *
     * @param anEntry the entry to locate; null is never found
     * @return the zero-based index of the first match, or -1 if
     *         anEntry is absent or null
     */
    public int indexOf(T anEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Tests whether this chain contains a given entry. O(n).
     *
     * @param anEntry the entry to locate; null is never found
     * @return true if the chain contains anEntry, or false if not
     */
    public boolean contains(T anEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Counts the number of times a given entry appears in this chain.
     * O(n): full scan.
     *
     * @param anEntry the entry to be counted; null is never found
     * @return the number of times anEntry appears in the chain
     */
    public int count(T anEntry)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Gets the number of entries currently in this chain. O(1).
     *
     * @return the integer number of entries in the chain
     */
    public int size()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Sees whether this chain is empty. O(1).
     *
     * @return true if the chain has no entries, or false if not
     */
    public boolean isEmpty()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Copies every entry in this chain into the given array, first entry
     * first. Usage: chain.toArray(new Song[chain.size()]). O(n).
     *
     * @param values an array of the correct type to fill; it must have
     *               length at least size()
     * @return the array containing all entries in the chain
     * @throws IllegalArgumentException if values is null or too small
     */
    public T[] toArray(T[] values)
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * Returns a readable listing of the chain, first entry first,
     * e.g. "[a, b, c]", or "[]" when empty. O(n).
     *
     * @return a string representation of this chain
     */
    @Override
    public String toString()
    {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    /**
     * A node in the chain. Holds one entry and a link to the next node.
     *
     * @param <E> the type of data held in the node
     */
    private static class Node<E>
    {
        private E data;        // entry stored in this node
        private Node<E> next;  // link to the next node, or null

        /**
         * Creates a node with the given data and no next node.
         *
         * @param dataPortion the entry to store
         */
        private Node(E dataPortion)
        {
            this(dataPortion, null);
        }

        /**
         * Creates a node with the given data and next link.
         *
         * @param dataPortion the entry to store
         * @param nextNode the node that follows this one
         */
        private Node(E dataPortion, Node<E> nextNode)
        {
            data = dataPortion;
            next = nextNode;
        }
    }
}
