package view;

import javax.swing.*;
import java.awt.*;

public class ResumeReviewForm extends JPanel {
    public ResumeReviewForm() {
        setLayout(new BorderLayout());
        JLabel titleLabel = new JLabel("Resume Review", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        JLabel infoLabel = new JLabel("This module allows Admins to view students' resumes and update their approval status.", SwingConstants.CENTER);
        add(infoLabel, BorderLayout.CENTER);
    }
}
