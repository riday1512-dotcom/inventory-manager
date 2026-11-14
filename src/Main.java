import model.Product;

public class Main {
    public static void main(String[] args) {
        Product p1 = new Product(101, "Parle-G", "Snacks", 10.0, 50);
        System.out.println(p1);

        p1.setPrice(-5);
    }
}