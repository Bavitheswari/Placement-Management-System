package view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import dao.StudentDAO;
import model.Student;
import util.UIConstants;

public class StudentRegistrationForm extends JFrame {

    private JTextField txtName;
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JTextField txtDepartment;
    private JTextField txtCGPA;
    private JTextField txtPhone;

    public StudentRegistrationForm() {
        setTitle("Student Registration - Placement Management System");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIConstants.BACKGROUND);

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel card = new JPanel();
        card.setPreferredSize(new Dimension(500, 600));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(UIConstants.CARD_BG);
        
        card.setBorder(new CompoundBorder(
                new LineBorder(UIConstants.BORDER, 1, true),
                new EmptyBorder(30, 40, 30, 40)));

        JLabel title = new JLabel("Create Student Account");
        title.setFont(UIConstants.TITLE_FONT);
        title.setForeground(UIConstants.TEXT_DARK);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Join the placement portal to connect with recruiters");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Fields Panel with grid structure for clean labels/inputs alignment
        JPanel fieldsPanel = new JPanel(new GridLayout(6, 2, 15, 12));
        fieldsPanel.setOpaque(false);
        fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        // Name
        JLabel lblName = new JLabel("Full Name");
        lblName.setFont(UIConstants.BOLD_FONT);
        txtName = new JTextField();
        txtName.putClientProperty("JTextField.placeholderText", "e.g. John Doe");

        // Email
        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(UIConstants.BOLD_FONT);
        txtEmail = new JTextField();
        txtEmail.putClientProperty("JTextField.placeholderText", "e.g. john@university.edu");

        // Password
        JLabel lblPassword = new JLabel("Password");
        lblPassword.setFont(UIConstants.BOLD_FONT);
        txtPassword = new JPasswordField();
        txtPassword.putClientProperty("JTextField.placeholderText", "\u2022\u2022\u2022\u2022\u2022\u2022\u2022\u2022");

        // Department
        JLabel lblDept = new JLabel("Department");
        lblDept.setFont(UIConstants.BOLD_FONT);
        txtDepartment = new JTextField();
        txtDepartment.putClientProperty("JTextField.placeholderText", "e.g. Computer Science");

        // CGPA
        JLabel lblCgpa = new JLabel("Current CGPA");
        lblCgpa.setFont(UIConstants.BOLD_FONT);
        txtCGPA = new JTextField();
        txtCGPA.putClientProperty("JTextField.placeholderText", "e.g. 8.5");

        // Phone
        JLabel lblPhone = new JLabel("Phone Number");
        lblPhone.setFont(UIConstants.BOLD_FONT);
        txtPhone = new JTextField();
        txtPhone.putClientProperty("JTextField.placeholderText", "e.g. 9876543210");

        fieldsPanel.add(lblName);
        fieldsPanel.add(txtName);
        fieldsPanel.add(lblEmail);
        fieldsPanel.add(txtEmail);
        fieldsPanel.add(lblPassword);
        fieldsPanel.add(txtPassword);
        fieldsPanel.add(lblDept);
        fieldsPanel.add(txtDepartment);
        fieldsPanel.add(lblCgpa);
        fieldsPanel.add(txtCGPA);
        fieldsPanel.add(lblPhone);
        fieldsPanel.add(txtPhone);

        JButton btnRegister = new JButton("REGISTER ACCOUNT");
        btnRegister.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnRegister.setBackground(UIConstants.BUTTON);
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setFocusPainted(false);
        btnRegister.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnRegister.setBackground(UIConstants.BUTTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnRegister.setBackground(UIConstants.BUTTON);
            }
        });

        btnRegister.addActionListener(e -> registerStudent());

        // Back to Login Link
        JButton btnLoginLink = new JButton("Already have an account? Sign In");
        btnLoginLink.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnLoginLink.setForeground(UIConstants.BUTTON);
        btnLoginLink.setBorderPainted(false);
        btnLoginLink.setContentAreaFilled(false);
        btnLoginLink.setFocusPainted(false);
        btnLoginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnLoginLink.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        btnLoginLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btnLoginLink.setForeground(UIConstants.BUTTON_HOVER);
            }
            @Override
            public void mouseExited(MouseEvent e) {
                btnLoginLink.setForeground(UIConstants.BUTTON);
            }
        });

        btnLoginLink.addActionListener(e -> {
            dispose();
            new StudentLoginForm();
        });

        card.add(title);
        card.add(Box.createVerticalStrut(5));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(fieldsPanel);
        card.add(Box.createVerticalStrut(30));
        card.add(btnRegister);
        card.add(Box.createVerticalStrut(20));
        card.add(btnLoginLink);

        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(card, gbc);

        add(mainPanel);
        setVisible(true);
    }

    private void registerStudent() {
        // Validation checks
        if (txtName.getText().trim().isEmpty() ||
            txtEmail.getText().trim().isEmpty() ||
            new String(txtPassword.getPassword()).trim().isEmpty() ||
            txtDepartment.getText().trim().isEmpty() ||
            txtCGPA.getText().trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in all the registration fields.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        double cgpaVal;
        try {
            cgpaVal = Double.parseDouble(txtCGPA.getText().trim());
            if (cgpaVal < 0.0 || cgpaVal > 10.0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid CGPA between 0.0 and 10.0.",
                    "Validation Error",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            // studentId will be auto-generated by DB, so we pass 0 here as placeholder
            Student student = new Student(
                    0, 
                    txtName.getText().trim(),
                    txtEmail.getText().trim(),
                    new String(txtPassword.getPassword()).trim(),
                    txtDepartment.getText().trim(),
                    cgpaVal,
                    txtPhone.getText().trim()
            );

            StudentDAO dao = new StudentDAO();
            boolean result = dao.registerStudent(student);

            if (result) {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration completed successfully! You can now log in.",
                        "Success",
                        JOptionPane.INFORMATION_MESSAGE
                );
                dispose();
                new StudentLoginForm();
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Registration failed. The email address might already be registered.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "An error occurred: " + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

