import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainApplication extends JFrame {
    private JButton manageAnimalsButton, manageAdoptersButton, processAdoptionButton;

    public MainApplication() {
        setTitle("Kitten and Puppy Adoption System");
        setSize(300, 200);
        setLocationRelativeTo(null); // Center the window on the screen
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Initialize buttons
        manageAnimalsButton = new JButton("Manage Animals");
        manageAdoptersButton = new JButton("Manage Adopters");
        processAdoptionButton = new JButton("Process Adoption");

        // Panel to hold buttons
        JPanel panel = new JPanel();
        panel.add(manageAnimalsButton);
        panel.add(manageAdoptersButton);
        panel.add(processAdoptionButton);

        add(panel);

        // Button Actions
        manageAnimalsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new AnimalManagement().setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainApplication.this,
                            "Error opening Animal Management: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        manageAdoptersButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new AdopterManagement().setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainApplication.this,
                            "Error opening Adopter Management: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        processAdoptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    new AdoptionProcess().setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(MainApplication.this,
                            "Error opening Adoption Process: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        // Launch the application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new MainApplication().setVisible(true));
    }
}
