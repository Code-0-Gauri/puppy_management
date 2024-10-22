import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AnimalManagement extends JFrame {
    private JTextField nameField, breedField, ageField;
    private JComboBox<String> typeCombo, genderCombo;
    private JButton addAnimalButton;

    public AnimalManagement() {
        setTitle("Kitten and Puppy Adoption - Animal Management");
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // UI Components
        nameField = new JTextField(15);
        breedField = new JTextField(15);
        ageField = new JTextField(5);

        String[] types = {"Kitten", "Puppy"};
        typeCombo = new JComboBox<>(types);

        String[] genders = {"Male", "Female"};
        genderCombo = new JComboBox<>(genders);

        addAnimalButton = new JButton("Add Animal");

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Animal Name:"));
        panel.add(nameField);

        panel.add(new JLabel("Breed:"));
        panel.add(breedField);

        panel.add(new JLabel("Age:"));
        panel.add(ageField);

        panel.add(new JLabel("Type:"));
        panel.add(typeCombo);

        panel.add(new JLabel("Gender:"));
        panel.add(genderCombo);

        panel.add(new JLabel("")); // Empty label for alignment
        panel.add(addAnimalButton);

        add(panel);

        // Add Animal Button Action Listener
        addAnimalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateFields()) {
                    addAnimal();
                }
            }
        });
    }

    private boolean validateFields() {
        try {
            String name = nameField.getText();
            String breed = breedField.getText();
            int age = Integer.parseInt(ageField.getText());

            if (name.isEmpty() || breed.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Name and Breed cannot be empty!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

            if (age < 0 || age > 10) {
                JOptionPane.showMessageDialog(this, "Age must be between 0 and 10!",
                        "Validation Error", JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Age must be a valid number!",
                    "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private void addAnimal() {
        String name = nameField.getText();
        String breed = breedField.getText();
        int age = Integer.parseInt(ageField.getText());
        String type = (String) typeCombo.getSelectedItem();
        String gender = (String) genderCombo.getSelectedItem();
        String status = "Available";
        java.sql.Date dateAdded = new java.sql.Date(new java.util.Date().getTime());

        String sql = "INSERT INTO Animals (Name, Type, Breed, Age, Gender, Status, DateAdded) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setString(2, type);
            ps.setString(3, breed);
            ps.setInt(4, age);
            ps.setString(5, gender);
            ps.setString(6, status);
            ps.setDate(7, dateAdded);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Animal added successfully!");
            clearFields();

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding animal: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        nameField.setText("");
        breedField.setText("");
        ageField.setText("");
        typeCombo.setSelectedIndex(0);
        genderCombo.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AnimalManagement().setVisible(true));
    }
}
