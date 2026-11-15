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
    }
}