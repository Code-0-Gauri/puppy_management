import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class AdoptionProcess extends JFrame {
    private JComboBox<String> adopterComboBox, animalComboBox;
    private JButton processAdoptionButton;

    public AdoptionProcess() {
        setTitle("Adoption Process");
        setSize(400, 200);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        adopterComboBox = new JComboBox<>();
        animalComboBox = new JComboBox<>();
        processAdoptionButton = new JButton("Process Adoption");

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Select Adopter:"));
        panel.add(adopterComboBox);

        panel.add(new JLabel("Select Animal:"));
        panel.add(animalComboBox);

        panel.add(new JLabel("")); // Empty label to align the button
        panel.add(processAdoptionButton);

        add(panel);

        loadAdopters();
        loadAvailableAnimals();

        processAdoptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processAdoption();
            }
        });
    }

    private void loadAdopters() {
        String sql = "SELECT AdopterID, FirstName, LastName FROM Adopters";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int adopterID = rs.getInt("AdopterID");
                String adopterName = rs.getString("FirstName") + " " + rs.getString("LastName");
                adopterComboBox.addItem(adopterID + " - " + adopterName);
            }
        } catch (SQLException ex) {
            showError("Error loading adopters: " + ex.getMessage());
        }
    }

    private void loadAvailableAnimals() {
        String sql = "SELECT AnimalID, Name, Type FROM Animals WHERE Status = 'Available'";
        try (Connection con = DatabaseConnection.getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int animalID = rs.getInt("AnimalID");
                String animalName = rs.getString("Name") + " (" + rs.getString("Type") + ")";
                animalComboBox.addItem(animalID + " - " + animalName);
            }
        } catch (SQLException ex) {
            showError("Error loading available animals: " + ex.getMessage());
        }
    }

    private void processAdoption() {
        String selectedAdopter = (String) adopterComboBox.getSelectedItem();
        String selectedAnimal = (String) animalComboBox.getSelectedItem();

        if (selectedAdopter == null || selectedAnimal == null) {
            JOptionPane.showMessageDialog(this, "Please select both an adopter and an animal.",
                    "Incomplete Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int adopterID = Integer.parseInt(selectedAdopter.split(" ")[0]);
        int animalID = Integer.parseInt(selectedAnimal.split(" ")[0]);

        String sqlAdopt = "INSERT INTO Adoptions (AdopterID, AnimalID, AdoptionDate) VALUES (?, ?, ?)";
        String sqlUpdateAnimal = "UPDATE Animals SET Status = 'Adopted' WHERE AnimalID = ?";

        java.sql.Date adoptionDate = new java.sql.Date(new java.util.Date().getTime());

        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement psAdopt = con.prepareStatement(sqlAdopt);
             PreparedStatement psUpdateAnimal = con.prepareStatement(sqlUpdateAnimal)) {

            // Insert adoption record
            psAdopt.setInt(1, adopterID);
            psAdopt.setInt(2, animalID);
            psAdopt.setDate(3, adoptionDate);
            psAdopt.executeUpdate();

            // Update animal status to 'Adopted'
            psUpdateAnimal.setInt(1, animalID);
            psUpdateAnimal.executeUpdate();

            JOptionPane.showMessageDialog(this, "Adoption successfully processed!");

            // Refresh animal list and clear selection
            animalComboBox.removeAllItems();
            loadAvailableAnimals();
            adopterComboBox.setSelectedIndex(0);

        } catch (SQLException ex) {
            showError("Error processing adoption: " + ex.getMessage());
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Database Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdoptionProcess().setVisible(true));
    }
}
