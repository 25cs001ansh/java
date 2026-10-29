import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Unit Tests for ItemService using JUnit 4.
 * Demonstrates:
 * 1. Adding an item increases the item count.
 * 2. Invalid actions (null item, duplicate ID, non-existent search/delete) throw IllegalArgumentException.
 */
public class ItemServiceTest {

    private ItemService service;

    @Before
    public void setUp() {
        service = new ItemService();
    }

    /**
     * Test 1: Verifies that adding items increases the count correctly.
     */
    @Test
    public void testAddIncreasesCount() {
        assertEquals("Initial count should be 0", 0, service.getCount());

        Item item1 = new Item("ITM101", "Mechanical Keyboard", 2499.00, 5);
        service.addItem(item1);
        assertEquals("Count should be 1 after adding one item", 1, service.getCount());

        Item item2 = new Item("ITM102", "Wireless Mouse", 899.00, 10);
        service.addItem(item2);
        assertEquals("Count should be 2 after adding two items", 2, service.getCount());
    }

    /**
     * Test 2: Verifies that adding a null item throws IllegalArgumentException.
     */
    @Test
    public void testAddNullItemThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.addItem(null);
        });
    }

    /**
     * Test 3: Verifies that adding a duplicate item (same ID) throws IllegalArgumentException.
     */
    @Test
    public void testAddDuplicateItemThrowsException() {
        Item item1 = new Item("ITM201", "USB Flash Drive 64GB", 450.00, 15);
        service.addItem(item1);

        Item duplicateItem = new Item("ITM201", "USB Flash Drive 128GB", 750.00, 10);

        // Attempting to add an item with the existing ID ITM201 must throw
        assertThrows(IllegalArgumentException.class, () -> {
            service.addItem(duplicateItem);
        });
    }

    /**
     * Test 4: Verifies that searching for a non-existent item throws IllegalArgumentException.
     */
    @Test
    public void testFindNonExistentItemThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.findById("NON_EXISTENT_ID");
        });
    }

    /**
     * Test 5: Verifies that removing a non-existent item throws IllegalArgumentException.
     */
    @Test
    public void testRemoveNonExistentItemThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            service.removeItem("DOES_NOT_EXIST");
        });
    }

    /**
     * Test 6: Verifies that removing an existing item decreases the count.
     */
    @Test
    public void testRemoveDecreasesCount() {
        Item item = new Item("ITM301", "Gaming Headset", 1800.00, 3);
        service.addItem(item);
        assertEquals(1, service.getCount());

        service.removeItem("ITM301");
        assertEquals("Count should be 0 after removing the item", 0, service.getCount());
    }

    /**
     * Main method to allow running JUnit tests directly from terminal or IDE.
     */
    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   Running Practical 14 Unit Tests (JUnit 4)      ");
        System.out.println("==================================================");

        Result result = JUnitCore.runClasses(ItemServiceTest.class);

        System.out.println("Total tests run   : " + result.getRunCount());
        System.out.println("Successful tests  : " + (result.getRunCount() - result.getFailureCount()));
        System.out.println("Failed tests      : " + result.getFailureCount());
        System.out.println("Execution time    : " + result.getRunTime() + " ms");

        if (result.wasSuccessful()) {
            System.out.println("\n[PASS] ALL TESTS PASSED SUCCESSFULLY!");
        } else {
            System.out.println("\n[FAIL] SOME TESTS FAILED:");
            for (Failure failure : result.getFailures()) {
                System.out.println(" - " + failure.getTestHeader() + ": " + failure.getMessage());
            }
            System.exit(1);
        }
    }
}
