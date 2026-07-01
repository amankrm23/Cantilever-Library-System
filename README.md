# 📚 Cantilever Library Management System

A robust, console-to-GUI desktop application designed to manage library operations. Built using **Java Swing** for a modern Windows-native interface and **MySQL** for relational data persistence.

## 🚀 Key Features
* **Member Management:** Securely register new library members.
* **Inventory Control:** Add new books to the library system.
* **Live Inventory Tracking:** View a dynamically formatted table of all currently available books.
* **Transaction Handling:** Issue books (marks them unavailable) and return books (marks them available) using SQL relational updates.
* **Professional UI:** Features a custom-styled, padding-optimized graphical interface utilizing the native OS Look-and-Feel.

## 🛠️ Tech Stack
* **Frontend:** Java Swing (AWT/SwingComponents)
* **Backend:** Java (JDK)
* **Database:** MySQL
* **Connectivity:** JDBC (MySQL Connector/J)

## 🗄️ Database Architecture
This system utilizes a relational database with foreign key constraints to maintain data integrity:
1.  `books`: Stores `book_id`, `title`, `author`, and `is_available` status.
2.  `members`: Stores `member_id`, `full_name`, and `email`.
3.  `borrow_records`: A transactional table tracking which member borrowed which book and the respective dates.

## ⚙️ Setup Instructions
1. Clone this repository to your local machine.
2. Ensure you have **MySQL** installed and running.
3. Run the SQL table creation script provided in the database schema to set up `library_system_db`.
4. Import the `mysql-connector-j-9.7.0.jar` file into your IDE's module dependencies.
5. In `Main.java`, update the `PASSWORD` variable with your local MySQL root password.
6. Compile and run `Main.java` to launch the GUI dashboard.

7. ## 💾 Database Schema (Run this before starting)
To test this application locally, run the following SQL script in your MySQL environment to recreate the database and tables:

```sql
CREATE DATABASE library_system_db;
USE library_system_db;

CREATE TABLE books (
    book_id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(150) NOT NULL,
    is_available BOOLEAN DEFAULT TRUE
);

CREATE TABLE members (
    member_id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE borrow_records (
    record_id INT AUTO_INCREMENT PRIMARY KEY,
    book_id INT,
    member_id INT,
    borrow_date DATE DEFAULT (CURRENT_DATE),
    return_date DATE,
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);
