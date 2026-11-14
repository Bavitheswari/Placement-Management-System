package view;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import dao.InterviewRoundDAO;
import model.InterviewRound;

public class InterviewManagementForm extends JPanel {
    public InterviewManagementForm() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Interview Round Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        formPanel.add(new JLabel("Drive ID:"));
        JTextField driveIdField = new JTextField();
        formPanel.add(driveIdField);

        formPanel.add(new JLabel("Round Name (e.g. Aptitude):"));
        JTextField nameField = new JTextField();
        formPanel.add(nameField);

        JButton addBtn = new JButton("Add Round");
        formPanel.add(new JLabel());
        formPanel.add(addBtn);

        addBtn.addActionListener(e -> {
            try {
                InterviewRound round = new InterviewRound();
                round.setDriveId(Integer.parseInt(driveIdField.getText()));
                round.setRoundName(nameField.getText());
                round.setRoundDate(new Date(System.currentTimeMillis()));

                InterviewRoundDAO dao = new InterviewRoundDAO();
                if (dao.addRound(round)) {
                    JOptionPane.showMessageDialog(this, "Round added!");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add round.");
                }
            } catch(Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid Drive ID.");
            }
        });

        add(formPanel, BorderLayout.CENTER);
    }
}
