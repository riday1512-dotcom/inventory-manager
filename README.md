# Inventory Manager (Java)

A console-based inventory management system for small shops, built to replace
notebook stock tracking. Add products, record sales, search stock, and get
low-stock alerts, with data saved automatically between runs.

## Why I built this
Many small shops still track stock in notebooks or from memory. That makes it
easy to miss that an item is running low, to lose track of what sold, and to
guess at how much stock is worth. I built this console application as a simple
replacement: it records products and sales, blocks sales that exceed the
available stock, and shows low-stock items at a glance. I chose this problem
because it let me practice Java fundamentals on something with real rules,
like validation, error handling, and saving data.

## Features
- Add, view, search, update, and delete products
- Record sales with stock validation (no selling more than available)
- Low-stock report with a custom threshold
- Total inventory value
- Data saved to a CSV file and loaded on startup
- Input validation and clear error messages (no crashes on bad input)

## Screenshots
![Menu](screenshots/menu.png)
![Demo](screenshots/demo.png)

## Tech
Java 17+ (tested on JDK 27), collections (HashMap), streams, custom exceptions,
file I/O (java.nio), Git

## Project structure
```
src/
├── Main.java                 console menu (input/output only)
├── model/Product.java        data and validation
├── service/InventoryService  business logic
├── storage/FileStorage       CSV persistence
└── exception/                ProductNotFoundException, OutOfStockException
```

## Design
```mermaid
classDiagram
    Main --> InventoryService
    InventoryService --> FileStorage
    InventoryService --> Product
    InventoryService ..> ProductNotFoundException
    InventoryService ..> OutOfStockException
```
Each layer has one job: `Main` talks to the user, `InventoryService` holds the
rules, `FileStorage` handles saving. This makes it easy to replace the storage
with a database later.

## How to run
```
git clone https://github.com/riday1512-dotcom/inventory-manager.git
cd inventory-manager
javac -d out -sourcepath src src/Main.java
java -cp out Main
```
Data is saved to `data/products.csv`, which is created automatically.

## What I learned
- [Encapsulation: validating data in one place so invalid products can't exist]
- [A real bug you hit and how you fixed it]
- [One more thing, in your own words]

## Next steps
- Move storage from CSV to MySQL using JDBC
- Rebuild as a REST API with Spring Boot

## How I built this
I used Claude as a mentor while learning: for explanations, project planning,
and code review. I wrote, tested, and debugged the code myself, including
fixing setup and compiler errors along the way.
## Running the MySQL version
1. Install MySQL and run `schema.sql` to create the database and table.
2. Download the MySQL Connector/J jar and place it in a `lib/` folder.
3. Set your database password as an environment variable (PowerShell):
   ```
   $env:DB_PASSWORD = "your_password"
   ```
4. Compile and run:
   ```
   javac -d out -cp "lib/*" -sourcepath src src/Main.java
   java -cp "out;lib/*" Main
   ```

## Design upgrade
`InventoryService` depends on a `ProductStorage` interface, so storage moved
from a CSV file to MySQL without changing the business logic. The database
layer uses `PreparedStatement` (protection against SQL injection) and
transactions (all changes succeed together or are rolled back). The password
is read from an environment variable and never stored in the code.
