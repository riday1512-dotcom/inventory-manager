package service;

import exception.ProductNotFoundException;
import model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}