package storage;

import model.Product;

import java.util.List;

public interface ProductStorage {
    List<Product> load();
    void save(List<Product> products);
}