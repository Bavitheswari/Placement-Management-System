package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import dao.CompanyDAO;
import model.Company;
import util.UIConstants;
import util.components.AnimatedCardPanel;
import util.components.AnimatedButton;

public class AddCompanyPanel extends JPanel {

    private final JTextField txtName;
    private final JTextField txtLocation;
    private final JTextField txtPackage;
    private final JTextField txtRole;

    public AddCompanyPanel() {
        setName("Add Company");
        setBackground(UIConstants.BACKGROUND);
        setLayout(new GridBagLayout());

        
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Animated 3D form card container
        AnimatedCardPanel formCard = new AnimatedCardPanel(new Color(41, 128, 185));
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setPreferredSize(new Dimension(460, 460));

        JLabel title = new JLabel("Register New Company");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Add company parameters to the hiring board");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(new Color(200, 220, 240));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inputs Grid Layout
        JPanel grid = new JPanel(new GridLayout(4, 2, 15, 15));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel lblName = new JLabel("Company Name");
        lblName.setFont(UIConstants.BOLD_FONT);
        lblName.setForeground(Color.WHITE);
        txtName = new JTextField();
        txtName.putClientProperty("JTextField.placeholderText", "e.g. Google LLC");

        JLabel lblLoc = new JLabel("Location");
        lblLoc.setFont(UIConstants.BOLD_FONT);
        lblLoc.setForeground(Color.WHITE);
        txtLocation = new JTextField();
        txtLocation.putClientProperty("JTextField.placeholderText", "e.g. Bangalore, India");

        JLabel lblPkg = new JLabel("Package (LPA)");
        lblPkg.setFont(UIConstants.BOLD_FONT);
        lblPkg.setForeground(Color.WHITE);
        txtPackage = new JTextField();
        txtPackage.putClientProperty("JTextField.placeholderText", "e.g. 18.5");

        JLabel lblRole = new JLabel("Job Role");
        lblRole.setFont(UIConstants.BOLD_FONT);
        lblRole.setForeground(Color.WHITE);
        txtRole = new JTextField();
        txtRole.putClientProperty("JTextField.placeholderText", "e.g. Software Engineer");

        grid.add(lblName);
        grid.add(txtName);
        grid.add(lblLoc);
        grid.add(txtLocation);
        grid.add(lblPkg);
        grid.add(txtPackage);
        grid.add(lblRole);
        grid.add(txtRole);

        AnimatedButton btnAdd = new AnimatedButton("Register Company", new Color(22, 160, 133));
        btnAdd.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnAdd.addActionListener(e -> addCompany());

        formCard.add(title);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(subtitle);
        formCard.add(grid);
        formCard.add(btnAdd);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(formCard, gbc);
    }

    private void addCompany() {
        if (txtName.getText().trim().isEmpty() ||
            txtLocation.getText().trim().isEmpty() ||
            txtPackage.getText().trim().isEmpty() ||
            txtRole.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all company fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double pkgVal;
        try {
            pkgVal = Double.parseDouble(txtPackage.getText().trim());
            if (pkgVal <= 0.0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid positive decimal value for Package.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Company company = new Company(
                    txtName.getText().trim(),
                    txtLocation.getText().trim(),
                    pkgVal,
                    txtRole.getText().trim()
            );

            CompanyDAO dao = new CompanyDAO();
            boolean result = dao.addCompany(company);

            if (result) {
                JOptionPane.showMessageDialog(
                        this,
                        "Company registered successfully on the board!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                // Clear fields
                txtName.setText("");
                txtLocation.setText("");
                txtPackage.setText("");
                txtRole.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to add company. Please try again.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred: " + e.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

