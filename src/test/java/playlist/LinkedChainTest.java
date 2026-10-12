package playlist;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for LinkedChain, following the Spec Section 7 chain table with
 * fixture X = [a, b, c].
 *
 * @author Kaustubh Pasumarthi
 * @version October 2026
 */
public class LinkedChainTest
{
    private LinkedChain<String> x;     // [a, b, c]
    private LinkedChain<String> empty; // []

    /**
     * Builds X = [a, b, c] and an empty chain before each test.
     */
    @BeforeEach
    public void setUp()
    {
        x = new LinkedChain<String>();
        x.addToEnd("a");
        x.addToEnd("b");
        x.addToEnd("c");
        empty = new LinkedChain<String>();
    }

    /**
     * Asserts that a chain holds exactly the expected entries, and that
     * size, isEmpty, toString, toArray and get all agree.
     */
    private static void assertChain(LinkedChain<String> chain,
        String... expected)
    {
        assertEquals(expected.length, chain.size());
        assertEquals(expected.length == 0, chain.isEmpty());
        assertArrayEquals(expected, chain.toArray(new String[chain.size()]));
        assertEquals(java.util.Arrays.toString(expected), chain.toString());
        for (int i = 0; i < expected.length; i++)
        {
            assertEquals(expected[i], chain.get(i));
        }
        assertThrows(IndexOutOfBoundsException.class,
            () -> chain.get(expected.length));
    }

    // ----------------------------------------------------------------
    // add

    @Test
    public void testAddPutsEntryAtFront()
    {
        assertTrue(x.add("z"));
        assertChain(x, "z", "a", "b", "c");
    }

    @Test
    public void testAddToEmpty()
    {
        assertTrue(empty.add("a"));
        assertChain(empty, "a");
    }

    @Test
    public void testAddNullThrowsAndLeavesChainUnchanged()
    {
        assertThrows(IllegalArgumentException.class, () -> x.add(null));
        assertChain(x, "a", "b", "c");
    }

    // ----------------------------------------------------------------
    // addToEnd

    @Test
    public void testAddToEndAppends()
    {
        assertTrue(x.addToEnd("z"));
        assertChain(x, "a", "b", "c", "z");
    }

    @Test
    public void testAddToEndOnEmpty()
    {
        assertTrue(empty.addToEnd("a"));
        assertChain(empty, "a");
    }

    @Test
    public void testAddToEndNullThrows()
    {
        assertThrows(IllegalArgumentException.class,
            () -> x.addToEnd(null));
        assertChain(x, "a", "b", "c");
    }

    // ----------------------------------------------------------------
    // remove()

    @Test
    public void testRemoveFirst()
    {
        assertEquals("a", x.remove());
        assertChain(x, "b", "c");
    }

    @Test
    public void testRemoveFirstOnEmptyReturnsNull()
    {
        assertNull(empty.remove());
        assertChain(empty);
    }

    @Test
    public void testRemoveFirstOnOneElementChainEmptiesIt()
    {
        empty.add("a");
        assertEquals("a", empty.remove());
        assertChain(empty);
        assertNull(empty.remove());
    }

    // ----------------------------------------------------------------
    // remove(T)

    @Test
    public void testRemoveEntryMiddle()
    {
        assertTrue(x.remove("b"));
        assertChain(x, "a", "c");
    }

    @Test
    public void testRemoveEntryFirst()
    {
        assertTrue(x.remove("a"));
        assertChain(x, "b", "c");
    }

    @Test
    public void testRemoveEntryLast()
    {
        assertTrue(x.remove("c"));
        assertChain(x, "a", "b");
        x.addToEnd("d");
        assertChain(x, "a", "b", "d");
    }

    @Test
    public void testRemoveEntryAbsent()
    {
        assertFalse(x.remove("q"));
        assertChain(x, "a", "b", "c");
    }

    @Test
    public void testRemoveEntryNullReturnsFalse()
    {
        assertFalse(x.remove(null));
        assertChain(x, "a", "b", "c");
        assertFalse(empty.remove(null));
    }

    @Test
    public void testRemoveEntryOnEmpty()
    {
        assertFalse(empty.remove("a"));
        assertChain(empty);
    }

    @Test
    public void testRemoveEntryRemovesOnlyFirstDuplicate()
    {
        x.addToEnd("a");
        x.addToEnd("b");
        assertTrue(x.remove("b"));
        assertChain(x, "a", "c", "a", "b");
        assertTrue(x.remove("a"));
        assertChain(x, "c", "a", "b");
    }

    @Test
    public void testRemoveEntryOnlyElement()
    {
        empty.add("a");
        assertTrue(empty.remove("a"));
        assertChain(empty);
    }

    // ----------------------------------------------------------------
    // clear

    @Test
    public void testClear()
    {
        x.clear();
        assertChain(x);
    }

    @Test
    public void testClearOnEmpty()
    {
        empty.clear();
        assertChain(empty);
    }

    // ----------------------------------------------------------------
    // get

    @Test
    public void testGet()
    {
        assertEquals("a", x.get(0));
        assertEquals("b", x.get(1));
        assertEquals("c", x.get(2));
    }

    @Test
    public void testGetOutOfRange()
    {
        assertThrows(IndexOutOfBoundsException.class, () -> x.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> x.get(3));
        assertThrows(IndexOutOfBoundsException.class, () -> empty.get(0));
    }

    // ----------------------------------------------------------------
    // contains

    @Test
    public void testContains()
    {
        assertTrue(x.contains("a"));
        assertTrue(x.contains("b"));
        assertTrue(x.contains("c"));
    }

    @Test
    public void testContainsAbsentOrNull()
    {
        assertFalse(x.contains("q"));
        assertFalse(x.contains(null));
        assertFalse(empty.contains("a"));
        assertFalse(empty.contains(null));
    }

    // ----------------------------------------------------------------
    // count

    @Test
    public void testCountWithDuplicates()
    {
        LinkedChain<String> aba = new LinkedChain<String>();
        aba.addToEnd("a");
        aba.addToEnd("b");
        aba.addToEnd("a");
        assertEquals(2, aba.count("a"));
        assertEquals(1, aba.count("b"));
    }

    @Test
    public void testCountAbsentOrNull()
    {
        assertEquals(0, x.count("q"));
        assertEquals(0, x.count(null));
        assertEquals(0, empty.count("a"));
    }

    // ----------------------------------------------------------------
    // size / isEmpty

    @Test
    public void testSize()
    {
        assertEquals(3, x.size());
        assertEquals(0, empty.size());
    }

    @Test
    public void testIsEmpty()
    {
        assertFalse(x.isEmpty());
        assertTrue(empty.isEmpty());
    }

    // ----------------------------------------------------------------
    // toArray

    @Test
    public void testToArrayExactSize()
    {
        String[] target = new String[3];
        String[] result = x.toArray(target);
        assertSame(target, result);
        assertArrayEquals(new String[] {"a", "b", "c"}, result);
    }

    @Test
    public void testToArrayLargerArrayLeavesTailUntouched()
    {
        String[] target = {"1", "2", "3", "4"};
        assertArrayEquals(new String[] {"a", "b", "c", "4"},
            x.toArray(target));
    }

    @Test
    public void testToArrayEmpty()
    {
        assertArrayEquals(new String[0], empty.toArray(new String[0]));
    }

    @Test
    public void testToArrayNullOrTooSmall()
    {
        assertThrows(IllegalArgumentException.class, () -> x.toArray(null));
        assertThrows(IllegalArgumentException.class,
            () -> x.toArray(new String[2]));
        assertThrows(IllegalArgumentException.class,
            () -> empty.toArray(null));
    }

    @Test
    public void testToArrayAfterAddIsReverseOrder()
    {
        empty.add("a");
        empty.add("b");
        empty.add("c");
        assertArrayEquals(new String[] {"c", "b", "a"},
            empty.toArray(new String[3]));
    }

    // ----------------------------------------------------------------
    // toString

    @Test
    public void testToString()
    {
        assertEquals("[a, b, c]", x.toString());
        assertEquals("[]", empty.toString());
        empty.add("a");
        assertEquals("[a]", empty.toString());
    }

    // ----------------------------------------------------------------
    // addAt

    @Test
    public void testAddAtMiddle()
    {
        x.addAt(1, "z");
        assertChain(x, "a", "z", "b", "c");
    }

    @Test
    public void testAddAtZero()
    {
        x.addAt(0, "z");
        assertChain(x, "z", "a", "b", "c");
    }

    @Test
    public void testAddAtSizeAppends()
    {
        x.addAt(3, "z");
        assertChain(x, "a", "b", "c", "z");
    }

    @Test
    public void testAddAtZeroOnEmpty()
    {
        empty.addAt(0, "a");
        assertChain(empty, "a");
    }

    @Test
    public void testAddAtOutOfRange()
    {
        assertThrows(IndexOutOfBoundsException.class,
            () -> x.addAt(4, "z"));
        assertThrows(IndexOutOfBoundsException.class,
            () -> x.addAt(5, "z"));
        assertThrows(IndexOutOfBoundsException.class,
            () -> x.addAt(-1, "z"));
        assertThrows(IndexOutOfBoundsException.class,
            () -> empty.addAt(1, "z"));
        assertChain(x, "a", "b", "c");
        assertChain(empty);
    }

    @Test
    public void testAddAtNull()
    {
        assertThrows(IllegalArgumentException.class,
            () -> x.addAt(1, null));
        assertThrows(IllegalArgumentException.class,
            () -> x.addAt(0, null));
        assertThrows(IllegalArgumentException.class,
            () -> x.addAt(3, null));
        assertChain(x, "a", "b", "c");
    }

    // ----------------------------------------------------------------
    // removeAt

    @Test
    public void testRemoveAtLast()
    {
        assertEquals("c", x.removeAt(2));
        assertChain(x, "a", "b");
        x.addToEnd("d");
        assertChain(x, "a", "b", "d");
    }

    @Test
    public void testRemoveAtFirst()
    {
        assertEquals("a", x.removeAt(0));
        assertChain(x, "b", "c");
    }

    @Test
    public void testRemoveAtMiddle()
    {
        assertEquals("b", x.removeAt(1));
        assertChain(x, "a", "c");
    }

    @Test
    public void testRemoveAtOnlyElement()
    {
        empty.add("a");
        assertEquals("a", empty.removeAt(0));
        assertChain(empty);
    }

    @Test
    public void testRemoveAtOutOfRange()
    {
        assertThrows(IndexOutOfBoundsException.class, () -> x.removeAt(3));
        assertThrows(IndexOutOfBoundsException.class, () -> x.removeAt(-1));
        assertChain(x, "a", "b", "c");
    }

    @Test
    public void testRemoveAtOnEmpty()
    {
        assertThrows(IndexOutOfBoundsException.class,
            () -> empty.removeAt(0));
        assertChain(empty);
    }

    // ----------------------------------------------------------------
    // indexOf

    @Test
    public void testIndexOf()
    {
        assertEquals(0, x.indexOf("a"));
        assertEquals(1, x.indexOf("b"));
        assertEquals(2, x.indexOf("c"));
    }

    @Test
    public void testIndexOfWithDuplicatesReturnsFirst()
    {
        LinkedChain<String> aba = new LinkedChain<String>();
        aba.addToEnd("a");
        aba.addToEnd("b");
        aba.addToEnd("a");
        assertEquals(0, aba.indexOf("a"));
        aba.removeAt(0);
        assertEquals(1, aba.indexOf("a"));
    }

    @Test
    public void testIndexOfAbsentOrNull()
    {
        assertEquals(-1, x.indexOf("q"));
        assertEquals(-1, x.indexOf(null));
        assertEquals(-1, empty.indexOf("a"));
        assertEquals(-1, empty.indexOf(null));
    }

    // ----------------------------------------------------------------
    // invariants after mixed operations

    @Test
    public void testMixedOperationsKeepCountConsistent()
    {
        x.add("z");            // [z, a, b, c]
        x.addAt(2, "y");       // [z, a, y, b, c]
        x.removeAt(4);         // [z, a, y, b]
        x.remove("z");         // [a, y, b]
        x.addToEnd("a");       // [a, y, b, a]
        x.remove();            // [y, b, a]
        x.addAt(3, "w");       // [y, b, a, w]
        assertFalse(x.remove("q"));
        assertChain(x, "y", "b", "a", "w");
    }

    @Test
    public void testEmptyAfterClearBehavesLikeNew()
    {
        x.clear();
        assertNull(x.remove());
        assertFalse(x.remove("a"));
        assertEquals(-1, x.indexOf("a"));
        assertThrows(IndexOutOfBoundsException.class, () -> x.removeAt(0));
        x.addToEnd("p");
        x.add("o");
        assertChain(x, "o", "p");
    }

    @Test
    public void testEmptyAfterRemovingLastBehavesLikeNew()
    {
        x.removeAt(2);
        x.remove("a");
        x.remove();
        assertChain(x);
        x.addToEnd("p");
        x.addAt(1, "q");
        assertChain(x, "p", "q");
    }

    // ----------------------------------------------------------------
    // type safety

    /**
     * A custom type whose equals compares only the id, so two distinct
     * objects can be equal.
     */
    private static final class Item
    {
        private final int id;
        private final String label;

        Item(int id, String label)
        {
            this.id = id;
            this.label = label;
        }

        @Override
        public boolean equals(Object other)
        {
            return other instanceof Item && ((Item)other).id == id;
        }

        @Override
        public int hashCode()
        {
            return Integer.hashCode(id);
        }

        @Override
        public String toString()
        {
            return label;
        }
    }

    @Test
    public void testCustomTypeUsesEquals()
    {
        LinkedChain<Item> items = new LinkedChain<Item>();
        Item first = new Item(1, "one");
        items.addToEnd(first);
        items.addToEnd(new Item(2, "two"));
        items.addToEnd(new Item(1, "uno"));

        Item lookalike = new Item(1, "other");
        assertTrue(items.contains(lookalike));
        assertEquals(2, items.count(lookalike));
        assertEquals(0, items.indexOf(lookalike));
        assertEquals("[one, two, uno]", items.toString());

        assertTrue(items.remove(lookalike));
        assertEquals("[two, uno]", items.toString());
        assertEquals(1, items.indexOf(first));
        assertFalse(items.contains(new Item(3, "three")));

        Item[] array = items.toArray(new Item[items.size()]);
        assertEquals("two", array[0].toString());
        assertEquals("uno", array[1].toString());
    }

    @Test
    public void testStringChainReturnsStringsWithoutCasts()
    {
        String first = x.get(0);
        String removed = x.removeAt(0);
        String[] rest = x.toArray(new String[x.size()]);
        assertEquals("a", first);
        assertEquals("a", removed);
        assertArrayEquals(new String[] {"b", "c"}, rest);
    }
}
