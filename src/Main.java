import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.*;

public class Main {
    // 1. Database Credentials (UPDATE THIS BEFORE RUNNING!)
    private static final String URL = "jdbc:mysql://localhost:3306/library_system_db";
    private static final String USER = "root";
    private static final String PASSWORD = "YOUR_PASSWORD_HERE";

    public static void main(String[] args) {
        // Native OS Look and Feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Library Management - Pro Edition");
        frame.setSize(450, 530); // Made slightly taller for the new button
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Main Container with Padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);
        frame.add(mainPanel);

        // Professional Title
        JLabel titleLabel = new JLabel("Central Library");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(44, 62, 80));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("System Admin Dashboard");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(titleLabel);
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // 3. Create Buttons
        JButton btnAddMember = createStyledButton("👤 Add New Member");
        JButton btnAddBook = createStyledButton("📚 Add New Book");
        JButton btnViewBooks = createStyledButton("📖 View Available Books"); // NEW BUTTON
        JButton btnIssueBook = createStyledButton("📤 Issue a Book");
        JButton btnReturnBook = createStyledButton("📥 Return a Book");
        JButton btnExit = createStyledButton("🚪 Secure Exit");

        // Add buttons with spacing
        mainPanel.add(btnAddMember); mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnAddBook);   mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnViewBooks); mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnIssueBook); mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnReturnBook);mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(btnExit);

        // 4. Attach Actions
        btnAddMember.addActionListener(e -> addMemberGUI(frame));
        btnAddBook.addActionListener(e -> addBookGUI(frame));
        btnViewBooks.addActionListener(e -> viewAvailableBooksGUI(frame)); // NEW ACTION
        btnIssueBook.addActionListener(e -> issueBookGUI(frame));
        btnReturnBook.addActionListener(e -> returnBookGUI(frame));
        btnExit.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    // --- HELPER METHOD FOR PROFESSIONAL BUTTONS ---
    private static JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(new Color(44, 62, 80)); // Dark slate text
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(300, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // --- GUI LOGIC METHODS ---

    private static void addMemberGUI(JFrame parent) {
        JTextField nameField = new JTextField();
        JTextField emailField = new JTextField();
        Object[] message = { "Full Name:", nameField, "Email Address:", emailField };

        if (JOptionPane.showConfirmDialog(parent, message, "Add Member", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO members (full_name, email) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, nameField.getText());
                pstmt.setString(2, emailField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(parent, "✅ Member Added Successfully!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "❌ Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void addBookGUI(JFrame parent) {
        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        Object[] message = { "Book Title:", titleField, "Author:", authorField };

        if (JOptionPane.showConfirmDialog(parent, message, "Add Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            String sql = "INSERT INTO books (title, author) VALUES (?, ?)";
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, titleField.getText());
                pstmt.setString(2, authorField.getText());
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(parent, "✅ Book Added Successfully to Inventory!");

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "❌ Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void viewAvailableBooksGUI(JFrame parent) {
        String sql = "SELECT book_id, title, author FROM books WHERE is_available = TRUE";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            StringBuilder bookList = new StringBuilder();
            bookList.append(String.format("%-5s | %-30s | %-20s\n", "ID", "Title", "Author"));
            bookList.append("--------------------------------------------------------------\n");

            boolean hasBooks = false;
            while (rs.next()) {
                hasBooks = true;
                bookList.append(String.format("%-5d | %-30s | %-20s\n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author")));
            }

            if (!hasBooks) {
                bookList.append("No books are currently available in the library.");
            }

            JTextArea textArea = new JTextArea(bookList.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

            JScrollPane scrollPane = new JScrollPane(textArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));

            JOptionPane.showMessageDialog(parent, scrollPane, "Available Inventory", JOptionPane.INFORMATION_MESSAGE);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "❌ Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void issueBookGUI(JFrame parent) {
        JTextField emailField = new JTextField();
        JTextField bookIdField = new JTextField();
        Object[] message = { "Member Email:", emailField, "Book ID:", bookIdField };

        if (JOptionPane.showConfirmDialog(parent, message, "Issue Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {

                int memberId = -1;
                PreparedStatement getMember = conn.prepareStatement("SELECT member_id FROM members WHERE email = ?");
                getMember.setString(1, emailField.getText());
                ResultSet rsMember = getMember.executeQuery();
                if (rsMember.next()) {
                    memberId = rsMember.getInt("member_id");
                } else {
                    JOptionPane.showMessageDialog(parent, "❌ Member not found.", "Failed", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                PreparedStatement checkBook = conn.prepareStatement("SELECT is_available FROM books WHERE book_id = ?");
                checkBook.setInt(1, Integer.parseInt(bookIdField.getText()));
                ResultSet rsBook = checkBook.executeQuery();

                if (rsBook.next() && rsBook.getBoolean("is_available")) {
                    PreparedStatement updateBook = conn.prepareStatement("UPDATE books SET is_available = FALSE WHERE book_id = ?");
                    updateBook.setInt(1, Integer.parseInt(bookIdField.getText()));
                    updateBook.executeUpdate();

                    PreparedStatement insertRecord = conn.prepareStatement("INSERT INTO borrow_records (book_id, member_id) VALUES (?, ?)");
                    insertRecord.setInt(1, Integer.parseInt(bookIdField.getText()));
                    insertRecord.setInt(2, memberId);
                    insertRecord.executeUpdate();

                    JOptionPane.showMessageDialog(parent, "✅ Book Issued Successfully!");
                } else {
                    JOptionPane.showMessageDialog(parent, "❌ Book is already issued or ID doesn't exist.", "Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "❌ Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private static void returnBookGUI(JFrame parent) {
        JTextField bookIdField = new JTextField();
        Object[] message = { "Book ID being returned:", bookIdField };

        if (JOptionPane.showConfirmDialog(parent, message, "Return Book", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE) == JOptionPane.OK_OPTION) {
            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
                int bookId = Integer.parseInt(bookIdField.getText());

                PreparedStatement updateBook = conn.prepareStatement("UPDATE books SET is_available = TRUE WHERE book_id = ?");
                updateBook.setInt(1, bookId);
                int updated = updateBook.executeUpdate();

                if (updated > 0) {
                    PreparedStatement updateRecord = conn.prepareStatement("UPDATE borrow_records SET return_date = CURRENT_DATE WHERE book_id = ? AND return_date IS NULL");
                    updateRecord.setInt(1, bookId);
                    updateRecord.executeUpdate();

                    JOptionPane.showMessageDialog(parent, "✅ Book Returned Successfully!");
                } else {
                    JOptionPane.showMessageDialog(parent, "❌ Book ID not found.", "Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(parent, "❌ Error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}