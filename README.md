# The Reading Room - JavaFX Bookstore Application

# Overview
The Reading Room is a JavaFX-based GUI application simulating an interactive bookstore system. 
The application enables user and admin roles to manage book shopping, ordering, profile information, and bookstore inventory — all within a desktop environment using MVC architecture and persistent storage (SQLite).

  ---
# Features Implemented

## User Functionality

- **User Registration & Login**
  - Create new profile with unique username
  - Login with password validation

- **User Dashboard**
  - Personalized greeting using first name
  - Displays **Top 5 Bestselling Books** based on sold copies

- **Profile Management**
  - Edit first name, last name, and password
  - Username is immutable once created

- **Shopping Cart**
  - Add books with selected quantities
  - Modify or remove items
  - Availability checks with stock validation logic

- **Checkout with Payment Validation**
  - Fake credit card data collection:
    - 16-digit card number
    - Future expiry date
    - 3-digit CVV
  - Displays total cost before confirmation
  - Generates unique order number upon successful checkout

- **Order History**
  - View all past orders (reverse chronological)
  - See order time, total, and item details

- **Export Orders to CSV**
  - Select specific orders and destination
  - Export orders to CSV for external viewing

- **Log Out**
  - Ends session and returns to login view

---

### Admin Functionality

- **Pre-Initialized Admin Account**
- Username: admin
- Password: reading_admin

- **Inventory Management**
- View full catalog with title, author, price, stock & sold counts
- Update stock quantity (increase/decrease)

- **Role Enforcement**
- Admin functions locked from regular users and vice versa

---

## Architecture

### MVC Pattern

- **Model**: Domain objects (Book, Order, User, etc.) in `model/`
- **View**: JavaFX `.fxml` files in `resources/view/`
- **Controller**: Event handling and logic in `controller/`
- **DAO Layer**: Interfaces & implementations for DB interactions in `dao/`
- **Database**: SQLite connection handled by `Database.java` (Singleton)

---

## Testing & Design Principles

- ✔ Followed **SOLID Principles** across model and controller logic
- ✔ Implemented **DAO Interface Pattern** for User & Book entities
- ✔ Applied **Singleton Pattern** in `Database.java` for DB connection reuse
- ✔ Unit tests implemented for key classes (minimum 5)
- ✔ Designed to accommodate future extensibility (e.g., limits on book purchases)

---

## Technologies Used

- **Java 22** (Zulu)
- **JavaFX** (GUI toolkit)
- **SQLite** (via JDBC)
- **Scene Builder** (FXML design)
- **JUnit** (for unit testing)

---

## Running the Application

1. Install [Zulu JDK 22.0.2 or later](https://www.azul.com/downloads/?package=jdk)
2. Import project into an IDE like IntelliJ or Eclipse
3. Ensure `application.db` is present at root
4. Run from the main entry point (JavaFX application launcher)
5. Use Scene Builder if editing `.fxml` files

---

## Author

Developed by:  
**Jaikanth Sellappan**  

---
