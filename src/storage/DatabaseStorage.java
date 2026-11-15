package storage;

import model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseStorage implements ProductStorage {
    private final String url;
    private final String user;
    private final String password;

    public DatabaseStorage(String url, String user, String password) {
        if (password == null) {
            throw new IllegalStateException("DB_PASSWORD environment variable is not set");
        }
        this.url = url;
        this.user = user;
        this.password = password;
    }

    @Override
    public List<Product> load() {
        String sql = "SELECT id, name, category, price, quantity FROM products";
        List<Product> products = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("category"),
                        rs.getDouble("price"),
                        rs.getInt("quantity")));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not load products: " + e.getMessage(), e);
        }
        return products;
    }

    @Override
    public void save(List<Product> products) {
        String insertSql = "INSERT INTO products (id, name, category, price, quantity) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            conn.setAutoCommit(false);   // start a transaction
            try (Statement clear = conn.createStatement();
                 PreparedStatement insert = conn.prepareStatement(insertSql)) {

                clear.executeUpdate("DELETE FROM products");
                for (Product p : products) {
                    insert.setInt(1, p.getId());
                    insert.setString(2, p.getName());
                    insert.setString(3, p.getCategory());
                    insert.setDouble(4, p.getPrice());
                    insert.setInt(5, p.getQuantity());
                    insert.addBatch();
                }
                insert.executeBatch();
                conn.commit();           // make all changes permanent together
            } catch (SQLException e) {
                conn.rollback();         // undo everything if any step failed
                throw e;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Could not save products: " + e.getMessage(), e);
        }
    }
}