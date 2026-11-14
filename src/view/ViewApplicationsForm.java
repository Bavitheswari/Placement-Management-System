package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import dao.ApplicationDAO;
import dao.StudentDAO;
import util.UIConstants;
import java.awt.*;
import java.sql.ResultSet;

public class ViewApplicationsForm extends JPanel {

    private final JTextField txtApplicationId;
    private final JButton btnApprove;
    private final JButton btnReject;
    private final JButton btnResume;
    private final JTable table;
    private final DefaultTableModel model;

    public ViewApplicationsForm() {
        setName("View Applications");
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(0, 15));

        JLabel titleLabel = new JLabel("Hiring Applications Board");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(UIConstants.TEXT_DARK);
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Application ID",
                "Student Name",
                "Target Company",
                "Current Status"
        };

        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(model);
        table.setFillsViewportHeight(true);

        // Control Panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        controlPanel.setOpaque(false);
        controlPanel.setBorder(new EmptyBorder(5, 0, 5, 0));

        JLabel lblId = new JLabel("Selected Application ID: ");
        lblId.setFont(UIConstants.BOLD_FONT);
        lblId.setForeground(UIConstants.TEXT_DARK);

        txtApplicationId = new JTextField(8);
        txtApplicationId.setPreferredSize(new Dimension(80, 36));
        txtApplicationId.setFont(UIConstants.BOLD_FONT);
        txtApplicationId.setHorizontalAlignment(JTextField.CENTER);
        txtApplicationId.setEditable(false); // Populated by table selection

        btnApprove = new JButton("Approve");
        btnApprove.setBackground(UIConstants.APPROVED);
        btnApprove.setForeground(Color.WHITE);
        btnApprove.setFont(UIConstants.BOLD_FONT);
        btnApprove.setPreferredSize(new Dimension(100, 36));
        btnApprove.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnReject = new JButton("Reject");
        btnReject.setBackground(UIConstants.REJECTED);
        btnReject.setForeground(Color.WHITE);
        btnReject.setFont(UIConstants.BOLD_FONT);
        btnReject.setPreferredSize(new Dimension(100, 36));
        btnReject.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        btnResume = new JButton("View Resume");
        btnResume.setBackground(UIConstants.BUTTON);
        btnResume.setForeground(Color.WHITE);
        btnResume.setFont(UIConstants.BOLD_FONT);
        btnResume.setPreferredSize(new Dimension(130, 36));
        btnResume.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        controlPanel.add(lblId);
        controlPanel.add(txtApplicationId);
        controlPanel.add(btnApprove);
        controlPanel.add(btnReject);
        controlPanel.add(btnResume);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(241, 245, 249)));

        add(scrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Sync table row click with the text field
        table.getSelectionModel().addListSelectionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                txtApplicationId.setText(table.getValueAt(selectedRow, 0).toString());
            }
        });

        // Action Listeners
        btnResume.addActionListener(e -> viewResume());
        btnApprove.addActionListener(e -> updateStatus("Approved"));
        btnReject.addActionListener(e -> updateStatus("Rejected"));

        loadApplicationData();
    }

    private void loadApplicationData() {
        try {
            model.setRowCount(0);
            ApplicationDAO dao = new ApplicationDAO();
            ResultSet rs = dao.getAllApplications();

            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("application_id"),
                        rs.getString("name"),
                        rs.getString("company_name"),
                        rs.getString("status")
                });
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void updateStatus(String status) {
        String idStr = txtApplicationId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            int applicationId = Integer.parseInt(idStr);
            ApplicationDAO dao = new ApplicationDAO();
            boolean result = dao.updateApplicationStatus(applicationId, status);

            if (result) {
                JOptionPane.showMessageDialog(
                        this,
                        "Application status set to '" + status + "' successfully.",
                        "Status Updated",
                        JOptionPane.INFORMATION_MESSAGE
                );
                txtApplicationId.setText("");
                loadApplicationData(); // Reload table row items dynamically
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Failed to update application status.",
                        "Database Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void viewResume() {
        String idStr = txtApplicationId.getText().trim();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an application from the table first.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        try {
            int applicationId = Integer.parseInt(idStr);
            ApplicationDAO appDAO = new ApplicationDAO();
            int studentId = appDAO.getStudentIdByApplicationId(applicationId);

            StudentDAO studentDAO = new StudentDAO();
            String resumePath = studentDAO.getResumePathByStudentId(studentId);

            if (resumePath == null || resumePath.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "This student has not uploaded a resume yet.",
                        "No Resume File",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            java.awt.Desktop.getDesktop().open(new java.io.File(resumePath));

        } catch(Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to open resume file. Check if the file exists locally.",
                    "File Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}