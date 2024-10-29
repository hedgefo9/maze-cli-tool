package backend.academy.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RandomizedSetTest {
    private RandomizedSet<Integer> set;

    @BeforeEach
    void setUp() {
        set = new RandomizedSet<>();
    }

    @Test
    void testAddElement() {
        assertTrue(set.add(1), "Should return true on adding a new element.");
        assertTrue(set.contains(1), "Set should contain the added element.");
        assertEquals(1, set.size(), "Set should contain only one element.");
        assertFalse(set.add(1), "Should return false when adding an existing element.");
    }

    @Test
    void testRemoveElement() {
        assertEquals(0, set.size(), "Set should be empty after creation.");
        set.add(1);
        assertTrue(set.remove(1), "Should return true on removing an existing element.");
        assertFalse(set.contains(1), "Set should not contain a removed element.");
        assertFalse(set.remove(1), "Should return false when trying to remove a non-existing element.");
        assertEquals(0, set.size(), "Set should be empty.");
    }

    @Test
    void testGetRandomSingleElement() {
        set.add(1);
        assertEquals(1, set.getRandom(),
            "With only one element, getRandom should always return that element.");
    }

    @Test
    void testGetRandomMultipleElements() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertEquals(3, set.size(), "Set should contain 3 elements.");

        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 0; i < 30; i++) {
            possibleValues.add(set.getRandom());
        }

        assertTrue(possibleValues.contains(1), "Random values should contain 1.");
        assertTrue(possibleValues.contains(2), "Random values should contain 2.");
        assertTrue(possibleValues.contains(3), "Random values should contain 3.");
    }

    @Test
    void testIsEmpty() {
        assertTrue(set.isEmpty(), "New set should be empty.");
        set.add(1);
        assertFalse(set.isEmpty(), "Set should not be empty after adding an element.");
        set.remove(1);
        assertTrue(set.isEmpty(), "Set should be empty after removing all elements.");
    }

    @Test
    void testRandomElementAfterRemoval() {
        set.add(1);
        set.add(2);
        set.add(3);
        assertEquals(3, set.size(), "Set should contain 3 elements.");
        set.remove(2);
        assertEquals(2, set.size(), "Set should contain 2 elements after removing 1 element.");

        Set<Integer> possibleValues = new HashSet<>();
        for (int i = 0; i < 30; i++) {
            possibleValues.add(set.getRandom());
        }

        assertTrue(possibleValues.contains(1), "Random values should still contain 1 after removing 2.");
        assertTrue(possibleValues.contains(3), "Random values should still contain 3 after removing 2.");
        assertFalse(possibleValues.contains(2), "Random values should not contain removed element 2.");
    }

    @Test
    void testMultipleAddAndRemove() {
        assertTrue(set.add(1), "Should allow adding element 1.");
        assertTrue(set.add(2), "Should allow adding element 2.");
        assertTrue(set.add(3), "Should allow adding element 3.");
        assertTrue(set.remove(2), "Should allow removing element 2.");
        assertFalse(set.contains(2), "Set should not contain element 2 after removal.");

        assertTrue(set.contains(1), "Set should contain element 1.");
        assertTrue(set.contains(3), "Set should contain element 3.");

        assertTrue(set.add(2), "Should allow re-adding element 2 after removal.");
        assertTrue(set.contains(2), "Set should contain element 2 after re-adding.");
    }

    @Test
    void testRandomWhenEmpty() {
        assertNull(set.getRandom(), "getRandom should return null when set is empty.");
    }
}
