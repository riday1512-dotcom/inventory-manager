import exception.OutOfStockException;
import exception.ProductNotFoundException;
import model.Product;
import service.InventoryService;

public class Main {
    public static void main(String[] args) {
        InventoryService service = new InventoryService();

        service.addProduct(new Product(101, "Parle-G", "Snacks", 10.0, 50));
        service.addProduct(new Product(102, "Maggi", "Instant Food", 14.0, 30));

        for (Product p : service.getAllProducts()) {
            System.out.println(p);
        }

        try {
            service.getProduct(999);
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Search 'mag': " + service.searchByName("mag"));

        service.sellProduct(101, 20);
        System.out.println("After sale: " + service.getProduct(101));

        try {
            service.sellProduct(101, 500);
        } catch (OutOfStockException e) {
            System.out.println("Error: " + e.getMessage());
        }

        service.updatePrice(101, 12.0);
        System.out.println("After price update: " + service.getProduct(101));

        try {
            service.updatePrice(101, -3);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        System.out.println("Low stock (<40): " + service.getLowStockProducts(40));

        service.deleteProduct(102);
        System.out.println("After delete:");
        for (Product p : service.getAllProducts()) {
            System.out.println(p);
        }

        try {
            service.deleteProduct(102);
        } catch (ProductNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}