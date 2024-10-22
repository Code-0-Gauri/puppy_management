import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.regex.Pattern;

public class AdopterManagement extends JFrame {
    private JTextField firstNameField, lastNameField, emailField, phoneField, addressField;
    private JButton addAdopterButton;

    public AdopterManagement() {
        setTitle("Adopter Management");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI Components
        firstNameField = new JTextField(15);
        lastNameField = new JTextField(15);
        emailField = new JTextField(15);
        phoneField = new JTextField(10);
        addressField = new JTextField(15);

        addAdopterButton = new JButton("Add Adopter");

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("First Name:"));
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        panel.add(lastNameField);

        panel.add(new JLabel("Email:"));
        panel.add(emailField);

        panel.add(new JLabel("Phone Number:"));
        panel.add(phoneField);

        panel.add(new JLabel("Address:"));
        panel.add(addressField);

        panel.add(new JLabel(""));  // Empty label to align the button
        panel.add(addAdopterButton);

        add(panel);

        // Add Adopter Button Action Listener
        addAdopterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    addAdopter();
                }
            }
        });
    }

    private boolean validateFields() {
        String emailPattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        String phonePattern = "\\d{10}";

        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() ||
                emailField.getText().isEmpty() || phoneField.getText().isEmpty() ||
                addressField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Pattern.matches(emailPattern, emailField.getText())) {
            JOptionPane.showMessageDialog(this, "Invalid email format!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        if (!Pattern.matches(phonePattern, phoneField.getText())) {
            JOptionPane.showMessageDialog(this, "Phone number must be 10 digits!", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void addAdopter() {
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();

        String sql = "INSERT INTO Adopters (FirstName, LastName, Email, PhoneNumber, Address) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, email);
            ps.setString(4, phone);
            ps.setString(5, address);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Adopter added successfully!");
            clearFields();  // Clear input fields after successful insertion
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding adopter: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdopterManagement().setVisible(true));
    }
}
