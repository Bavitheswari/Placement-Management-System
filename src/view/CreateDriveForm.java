package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import dao.DriveDAO;
import model.Drive;
import util.UIConstants;

public class CreateDriveForm extends JPanel {

    private final JTextField txtCompanyId;
    private final JTextField txtDate;
    private final JTextField txtCgpa;

    public CreateDriveForm() {
        setName("Create Placement Drive");
        setBackground(UIConstants.BACKGROUND);
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Padded form card container
        JPanel formCard = new JPanel();
        formCard.setBackground(Color.WHITE);
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));
        formCard.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(30, 40, 30, 40)));
        formCard.setPreferredSize(new Dimension(450, 420));

        JLabel title = new JLabel("Create Placement Drive");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(UIConstants.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Schedule a hiring drive for registered companies");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Inputs Grid Layout
        JPanel grid = new JPanel(new GridLayout(3, 2, 15, 15));
        grid.setOpaque(false);
        grid.setBorder(new EmptyBorder(25, 0, 25, 0));

        JLabel lblCompanyId = new JLabel("Company ID");
        lblCompanyId.setFont(UIConstants.BOLD_FONT);
        txtCompanyId = new JTextField();
        txtCompanyId.putClientProperty("JTextField.placeholderText", "e.g. 1");

        JLabel lblDate = new JLabel("Drive Date");
        lblDate.setFont(UIConstants.BOLD_FONT);
        txtDate = new JTextField();
        txtDate.putClientProperty("JTextField.placeholderText", "YYYY-MM-DD");

        JLabel lblCgpa = new JLabel("Minimum CGPA");
        lblCgpa.setFont(UIConstants.BOLD_FONT);
        txtCgpa = new JTextField();
        txtCgpa.putClientProperty("JTextField.placeholderText", "e.g. 7.5");

        grid.add(lblCompanyId);
        grid.add(txtCompanyId);
        grid.add(lblDate);
        grid.add(txtDate);
        grid.add(lblCgpa);
        grid.add(txtCgpa);

        JButton btnCreate = new JButton("Create Placement Drive");
        btnCreate.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnCreate.setBackground(UIConstants.BUTTON);
        btnCreate.setForeground(Color.WHITE);
        btnCreate.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCreate.setFocusPainted(false);
        btnCreate.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnCreate.addActionListener(e -> createDrive());

        formCard.add(title);
        formCard.add(Box.createVerticalStrut(5));
        formCard.add(subtitle);
        formCard.add(grid);
        formCard.add(btnCreate);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(formCard, gbc);
    }

    private void createDrive() {
        if (txtCompanyId.getText().trim().isEmpty() ||
            txtDate.getText().trim().isEmpty() ||
            txtCgpa.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all placement drive fields.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int companyIdVal;
        double cgpaVal;
        try {
            companyIdVal = Integer.parseInt(txtCompanyId.getText().trim());
            cgpaVal = Double.parseDouble(txtCgpa.getText().trim());
            if (companyIdVal <= 0 || cgpaVal < 0.0 || cgpaVal > 10.0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Company ID (integer) and CGPA (0.0 to 10.0).",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Basic date format validation (YYYY-MM-DD)
        String dateStr = txtDate.getText().trim();
        if (!dateStr.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter the drive date in the YYYY-MM-DD format.",
                    "Input Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            Drive drive = new Drive(
                    companyIdVal,
                    dateStr,
                    cgpaVal
            );

            DriveDAO dao = new DriveDAO();
            boolean result = dao.createDrive(drive);

            if (result) {
                JOptionPane.showMessageDialog(
                        this,
                        "Placement drive scheduled successfully!",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                // Clear fields
                txtCompanyId.setText("");
                txtDate.setText("");
                txtCgpa.setText("");
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to create drive. Check if the Company ID is valid.",
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

