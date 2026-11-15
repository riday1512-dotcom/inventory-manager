import exception.OutOfStockException;
import exception.ProductNotFoundException;
import model.Product;
import service.InventoryService;
import storage.DatabaseStorage;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final InventoryService service = new InventoryService(
            new DatabaseStorage("jdbc:mysql://localhost:3306/inventory_db",
                    "root", System.getenv("DB_PASSWORD")));

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");

            try {
                switch (choice) {
                    case 1 -> addProduct();
                    case 2 -> printProducts(service.getAllProducts());
                    case 3 -> searchByName();
                    case 4 -> updatePrice();
                    case 5 -> sellProduct();
                    case 6 -> deleteProduct();
                    case 7 -> showLowStock();
                    case 8 -> showTotalValue();
                    case 0 -> running = false;
                    default -> System.out.println("Invalid choice. Try again.");
                }
            } catch (ProductNotFoundException | OutOfStockException | IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        System.out.println("Goodbye!");
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("===== INVENTORY MANAGER =====");
        System.out.println("1. Add product");
        System.out.println("2. View all products");
        System.out.println("3. Search by name");
        System.out.println("4. Update price");
        System.out.println("5. Sell product");
        System.out.println("6. Delete product");
        System.out.println("7. Low-stock report");
        System.out.println("8. Total inventory value");
        System.out.println("0. Exit");
    }

    private static void addProduct() {
        int id = readInt("Id: ");
        String name = readText("Name: ");
        String category = readText("Category: ");
        double price = readDouble("Price: ");
        int quantity = readInt("Quantity: ");
        service.addProduct(new Product(id, name, category, price, quantity));
        System.out.println("Product added.");
    }

    private static void searchByName() {
        String keyword = readText("Search keyword: ");
        printProducts(service.searchByName(keyword));
    }

    private static void updatePrice() {
        int id = readInt("Product id: ");
        double price = readDouble("New price: ");
        service.updatePrice(id, price);
        System.out.println("Price updated.");
    }

    private static void sellProduct() {
        int id = readInt("Product id: ");
        int quantity = readInt("Quantity to sell: ");
        service.sellProduct(id, quantity);
        System.out.println("Sale recorded.");
    }

    private static void deleteProduct() {
        int id = readInt("Product id: ");
        service.deleteProduct(id);
        System.out.println("Product deleted.");
    }

    private static void showLowStock() {
        int threshold = readInt("Show products with quantity below: ");
        printProducts(service.getLowStockProducts(threshold));
    }

    private static void showTotalValue() {
        double total = service.getTotalInventoryValue();
        System.out.println("Total inventory value: Rs." + total);
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("No products found.");
            return;
        }
        for (Product p : products) {
            System.out.println(p);
        }
    }

    // ---------- Input helpers ----------

    private static String readText(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            try {
                return Integer.parseInt(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            try {
                return Double.parseDouble(readText(prompt));
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}