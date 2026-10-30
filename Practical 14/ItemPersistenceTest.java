import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Advanced Learners Test: Verifies that saving and reloading inventory data persists all items unchanged.
 */
public class ItemPersistenceTest {

    private Path tempFile;
    private ItemService service;

    @Before
    public void setUp() throws IOException {
        tempFile = Files.createTempFile("inventory_test_", ".dat");
        service = new ItemService();
    }

    @After
    public void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    public void testSaveAndReloadUnchanged() throws Exception {
        // 1. Add sample items to service
        Item i1 = new Item("ITM001", "Mechanical Keyboard", 2499.0, 5);
        Item i2 = new Item("ITM002", "Gaming Mouse", 1299.5, 8);
        Item i3 = new Item("ITM003", "USB Flash Drive", 499.0, 20);

        service.addItem(i1);
        service.addItem(i2);
        service.addItem(i3);

        assertEquals(3, service.getCount());

        // 2. Save items to file
        service.saveToFile(tempFile);
        assertTrue("Data file must exist on disk", Files.exists(tempFile));
        assertTrue("Data file must not be empty", Files.size(tempFile) > 0);

        // 3. Reload into a completely fresh ItemService instance
        ItemService freshService = new ItemService();
        assertEquals("Fresh service should initially be empty", 0, freshService.getCount());

        freshService.loadFromFile(tempFile);

        // 4. Verify all items and counts are preserved unchanged
        assertEquals("Loaded count must match original count", 3, freshService.getCount());

        List<Item> loadedItems = freshService.getAllItems();
        assertEquals("Item 1 must match", i1, loadedItems.get(0));
        assertEquals("Item 2 must match", i2, loadedItems.get(1));
        assertEquals("Item 3 must match", i3, loadedItems.get(2));

        // Verify specific field data
        Item loaded2 = freshService.findById("ITM002");
        assertEquals("Gaming Mouse", loaded2.getName());
        assertEquals(1299.5, loaded2.getPrice(), 0.001);
        assertEquals(8, loaded2.getQuantity());

        System.out.println("[PASS] testSaveAndReloadUnchanged: All items saved and reloaded unchanged!");
    }

    public static void main(String[] args) {
        Result result = JUnitCore.runClasses(ItemPersistenceTest.class);
        System.out.println("\n--- Persistence JUnit Test Summary ---");
        System.out.println("Tests run: " + result.getRunCount());
        System.out.println("Failures : " + result.getFailureCount());
        for (Failure f : result.getFailures()) {
            System.err.println("Failure: " + f.toString());
        }
        if (result.wasSuccessful()) {
            System.out.println("ALL TESTS PASSED!");
        }
    }
}

