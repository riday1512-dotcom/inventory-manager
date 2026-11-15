package service;

import exception.OutOfStockException;
import exception.ProductNotFoundException;
import model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InventoryService {
    private final Map<Integer, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        if (products.containsKey(product.getId())) {
            throw new IllegalArgumentException("Product with id " + product.getId() + " already exists");
        }
        products.put(product.getId(), product);
    }

    public Product getProduct(int id) {
        Product product = products.get(id);
        if (product == null) {
            throw new ProductNotFoundException(id);
        }
        return product;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products.values());
    }

    public void deleteProduct(int id) {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException(id);
        }
        products.remove(id);
    }

    public List<Product> searchByName(String keyword) {
        return products.values().stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Product> searchByCategory(String category) {
        return products.values().stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .collect(Collectors.toList());
    }

    public void sellProduct(int id, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Sale quantity must be positive");
        }
        Product product = getProduct(id);
        if (product.getQuantity() < quantity) {
            throw new OutOfStockException(product.getName(), product.getQuantity());
        }
        product.setQuantity(product.getQuantity() - quantity);
    }

    public void updatePrice(int id, double newPrice) {
        Product product = getProduct(id);
        product.setPrice(newPrice);
    }

    public List<Product> getLowStockProducts(int threshold) {
        return products.values().stream()
                .filter(p -> p.getQuantity() < threshold)
                .collect(Collectors.toList());
    }
    public double getTotalInventoryValue() {
    return products.values().stream()
            .mapToDouble(p -> p.getPrice() * p.getQuantity())
            .sum();
}
}