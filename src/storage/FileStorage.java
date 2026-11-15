package storage;

import model.Product;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileStorage {
    private final Path filePath;

    public FileStorage(String fileName) {
        this.filePath = Paths.get(fileName);
    }

    public void save(List<Product> products) {
        List<String> lines = new ArrayList<>();
        for (Product p : products) {
            lines.add(p.getId() + "," + clean(p.getName()) + "," + clean(p.getCategory())
                    + "," + p.getPrice() + "," + p.getQuantity());
        }
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);   // creates the data folder if missing
            }
            Files.write(filePath, lines);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save data: " + e.getMessage(), e);
        }
    }

    public List<Product> load() {
        List<Product> products = new ArrayList<>();
        if (!Files.exists(filePath)) {
            return products;   // first run: nothing to load yet
        }
        try {
            for (String line : Files.readAllLines(filePath)) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length != 5) {
                    System.out.println("Skipping bad line: " + line);
                    continue;
                }
                try {
                    products.add(new Product(
                            Integer.parseInt(parts[0].trim()),
                            parts[1].trim(),
                            parts[2].trim(),
                            Double.parseDouble(parts[3].trim()),
                            Integer.parseInt(parts[4].trim())));
                } catch (IllegalArgumentException e) {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read data: " + e.getMessage(), e);
        }
        return products;
    }

    // Commas would break the CSV format, so replace them
    private String clean(String text) {
        return text.replace(",", " ");
    }
}