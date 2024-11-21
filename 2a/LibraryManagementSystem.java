import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class ItemNotAvailableException extends Exception {
    public ItemNotAvailableException(String message) {
        super(message);
    }
}

abstract class LibraryItem {
    private String title;
    private String itemID;
    private boolean isAvailable;

    // Constructor
    public LibraryItem(String title, String itemID) {
        this.title = title;
        this.itemID = itemID;
        this.isAvailable = true; // All items are available by default
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getItemID() {
        return itemID;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // Setters
    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    // Abstract methods
    public abstract void borrow() throws ItemNotAvailableException;

    public abstract void returnItem();

    // Common method
    @Override
    public String toString() {
        return "ItemID: " + itemID + ", Title: " + title + ", Available: " + isAvailable;
    }
}

// Subclass: Book
class Book extends LibraryItem {
    private String author;
    private String genre;

    // Constructor
    public Book(String title, String itemID, String author, String genre) {
        super(title, itemID);
        this.author = author;
        this.genre = genre;
    }

    @Override
    public void borrow() throws ItemNotAvailableException {
        if (!isAvailable()) {
            throw new ItemNotAvailableException("Book '" + getTitle() + "' is not available for borrowing.");
        }
        setAvailable(false);
        System.out.println("Book borrowed: " + getTitle());
    }

    @Override
    public void returnItem() {
        setAvailable(true);
        System.out.println("Book returned: " + getTitle());
    }

    @Override
    public String toString() {
        return super.toString() + ", Author: " + author + ", Genre: " + genre;
    }
}

// Subclass: DVD
class DVD extends LibraryItem {
    private String director;
    private int duration; // in minutes

    // Constructor
    public DVD(String title, String itemID, String director, int duration) {
        super(title, itemID);
        this.director = director;
        this.duration = duration;
    }

    @Override
    public void borrow() throws ItemNotAvailableException {
        if (!isAvailable()) {
            throw new ItemNotAvailableException("DVD '" + getTitle() + "' is not available for borrowing.");
        }
        setAvailable(false);
        System.out.println("DVD borrowed: " + getTitle());
    }

    @Override
    public void returnItem() {
        setAvailable(true);
        System.out.println("DVD returned: " + getTitle());
    }

    @Override
    public String toString() {
        return super.toString() + ", Director: " + director + ", Duration: " + duration + " mins";
    }
}

// Library class
class Library {
    private List<LibraryItem> items;

    // Constructor
    public Library() {
        items = new ArrayList<>();
    }

    // Add item to the library
    public void addItem(LibraryItem item) {
        items.add(item);
    }

    // Search items by title
    public List<LibraryItem> search(String title) {
        return items.stream()
                .filter(item -> item.getTitle().equalsIgnoreCase(title))
                .collect(Collectors.toList());
    }

    // Search items by ID
    public LibraryItem searchByID(String itemID) {
        return items.stream()
                .filter(item -> item.getItemID().equalsIgnoreCase(itemID))
                .findFirst()
                .orElse(null);
    }

    // Display all items grouped by type
    public void displayAllItems() {
        System.out.println("Books:");
        items.stream().filter(item -> item instanceof Book).forEach(System.out::println);

        System.out.println("\nDVDs:");
        items.stream().filter(item -> item instanceof DVD).forEach(System.out::println);
    }

    // Borrow item
    public void borrowItem(String itemID) {
        try {
            LibraryItem item = searchByID(itemID);
            if (item == null) {
                System.out.println("Item with ID " + itemID + " not found.");
                return;
            }
            item.borrow();
        } catch (ItemNotAvailableException e) {
            System.out.println(e.getMessage());
        } finally {
            System.out.println("Borrow operation completed.");
        }
    }

    // Return item
    public void returnItem(String itemID) {
        LibraryItem item = searchByID(itemID);
        if (item == null) {
            System.out.println("Item with ID " + itemID + " not found.");
            return;
        }
        item.returnItem();
        System.out.println("Return operation completed.");
    }
}

// Main class
public class LibraryManagementSystem {
    public static void main(String[] args) {
        Library library = new Library();

        // Adding items to the library
        library.addItem(new Book("The Great Gatsby", "B001", "F. Scott Fitzgerald", "Fiction"));
        library.addItem(new Book("1984", "B002", "George Orwell", "Dystopian"));
        library.addItem(new DVD("Inception", "D001", "Christopher Nolan", 148));
        library.addItem(new DVD("The Matrix", "D002", "Wachowskis", 136));

        // Display all items
        System.out.println("All items in the library:");
        library.displayAllItems();

        // Search by title
        System.out.println("\nSearching for '1984':");
        List<LibraryItem> searchResults = library.search("1984");
        searchResults.forEach(System.out::println);

        // Search by ID
        System.out.println("\nSearching for item with ID 'D001':");
        LibraryItem item = library.searchByID("D001");
        System.out.println(item);

        // Borrow an item
        System.out.println("\nBorrowing 'The Great Gatsby':");
        library.borrowItem("B001");

        // Try borrowing the same item again
        System.out.println("\nTrying to borrow 'The Great Gatsby' again:");
        library.borrowItem("B001");

        // Return an item
        System.out.println("\nReturning 'The Great Gatsby':");
        library.returnItem("B001");

        // Display all items again
        System.out.println("\nAll items in the library after borrow and return:");
        library.displayAllItems();
    }
}
