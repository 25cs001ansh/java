import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service Layer: Manages Item business logic and persistence using an ArrayList.
 * Demonstrates validation and exception throwing for invalid actions.
 */
public class ItemService {
    // Internal storage using ArrayList
    private final List<Item> items = new ArrayList<>();

    /**
     * Adds an item to the inventory.
     * 
     * @param item the item to add
     * @throws IllegalArgumentException if the item is null or an item with the same ID already exists
     */
    public void addItem(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Cannot add null item.");
        }
        if (existsById(item.getId())) {
            throw new IllegalArgumentException("Item with ID '" + item.getId() + "' already exists.");
        }
        items.add(item);
    }

    /**
     * Returns the total count of items in the inventory.
     * 
     * @return current number of items
     */
    public int getCount() {
        return items.size();
    }

    /**
     * Retrieves an unmodifiable copy of all items.
     * 
     * @return list of items
     */
    public List<Item> getAllItems() {
        return Collections.unmodifiableList(new ArrayList<>(items));
    }

    /**
     * Checks if an item exists by its ID.
     * 
     * @param id the item ID
     * @return true if found, false otherwise
     */
    public boolean existsById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds an item by ID.
     * 
     * @param id the item ID to look up
     * @return the found Item
     * @throws IllegalArgumentException if the ID is invalid or the item is not found
     */
    public Item findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Item ID cannot be null or empty.");
        }
        for (Item item : items) {
            if (item.getId().equalsIgnoreCase(id.trim())) {
                return item;
            }
        }
        throw new IllegalArgumentException("Item with ID '" + id + "' not found.");
    }

    /**
     * Removes an item from the inventory by ID.
     * 
     * @param id the ID of the item to remove
     * @throws IllegalArgumentException if the item does not exist
     */
    public void removeItem(String id) {
        Item item = findById(id); // will throw IllegalArgumentException if not found or id invalid
        items.remove(item);
    }

    /**
     * Updates the price of an existing item.
     * 
     * @param id the ID of the item
     * @param newPrice the updated price
     * @throws IllegalArgumentException if the price is invalid or the item does not exist
     */
    public void updatePrice(String id, double newPrice) {
        if (newPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative: " + newPrice);
        }
        Item item = findById(id);
        item.setPrice(newPrice);
    }

    /**
     * Calculates the total inventory value (price * quantity for each item).
     * 
     * @return sum of all item values
     */
    public double calculateTotalValue() {
        double total = 0.0;
        for (Item item : items) {
            total += item.getPrice() * item.getQuantity();
        }
        return total;
    }

    /**
     * Clears all items in the inventory.
     */
    public void clear() {
        items.clear();
    }
}
