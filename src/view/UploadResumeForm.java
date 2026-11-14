package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import dao.StudentDAO;
import util.UIConstants;
import util.UserSession;
import util.components.AnimatedButton;

public class UploadResumeForm extends JPanel {

    private final JLabel lblStatus;
    private File selectedFile;

    public UploadResumeForm() {
        setBackground(new Color(248, 250, 252));
        setLayout(new GridBagLayout());
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel card = new JPanel();
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(35, 45, 35, 45)
        ));
        card.setPreferredSize(new Dimension(500, 400));

        JLabel title = new JLabel("Upload Resume");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(new Color(15, 23, 42));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Submit your PDF/Word document for recruiters to review");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Interactive Drop-Zone Box
        DropZonePanel dropZone = new DropZonePanel();
        dropZone.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        dropZone.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel uploadIcon = new JLabel("📄");
        uploadIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
        uploadIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        lblStatus = new JLabel("Click here to browse for a file");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblStatus.setForeground(new Color(99, 102, 241));
        lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel formatHint = new JLabel("Supported: .pdf, .doc, .docx");
        formatHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        formatHint.setForeground(new Color(148, 163, 184));
        formatHint.setAlignmentX(Component.CENTER_ALIGNMENT);

        dropZone.add(Box.createVerticalStrut(25));
        dropZone.add(uploadIcon);
        dropZone.add(Box.createVerticalStrut(10));
        dropZone.add(lblStatus);
        dropZone.add(Box.createVerticalStrut(5));
        dropZone.add(formatHint);

        // Click action for DropZone
        dropZone.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { chooseFile(); }
        });

        AnimatedButton btnSubmit = new AnimatedButton("Upload Document", new Color(16, 185, 129));
        btnSubmit.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        btnSubmit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnSubmit.addActionListener(e -> processUpload());

        card.add(title);
        card.add(Box.createVerticalStrut(8));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(30));
        card.add(dropZone);
        card.add(Box.createVerticalStrut(30));
        card.add(btnSubmit);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        add(card, gbc);

        checkExistingResume();
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Resume Documents (PDF, DOCX)", "pdf", "docx", "doc");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            lblStatus.setText(selectedFile.getName());
            lblStatus.setForeground(new Color(21, 128, 61)); // Dark green success
        }
    }

    private void processUpload() {
        if (selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please select a file first.", "No File", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            StudentDAO dao = new StudentDAO();
            int studentId = dao.getStudentByEmail(UserSession.studentEmail).getStudentId();
            
            // AppData dir
            String appDataStr = System.getenv("APPDATA");
            if (appDataStr == null) {
                appDataStr = System.getProperty("user.home");
            }
            
            File uploadDir = new File(appDataStr, "PlacementSystem_Resumes");
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            
            String fileExt = selectedFile.getName().substring(selectedFile.getName().lastIndexOf('.'));
            String newFileName = "Resume_" + studentId + fileExt;
            File destinationFile = new File(uploadDir, newFileName);
            
            Files.copy(selectedFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            
            if (dao.updateResumePath(studentId, destinationFile.getAbsolutePath())) {
                JOptionPane.showMessageDialog(this, "Resume uploaded successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Database error updating resume.", "Error", JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error uploading file: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void checkExistingResume() {
        try {
            StudentDAO dao = new StudentDAO();
            int studentId = dao.getStudentByEmail(UserSession.studentEmail).getStudentId();
            String existingPath = dao.getResumePathByStudentId(studentId);
            
            if (existingPath != null && !existingPath.isEmpty()) {
                File file = new File(existingPath);
                if (file.exists()) {
                    lblStatus.setText("Current: " + file.getName());
                    lblStatus.setForeground(new Color(15, 118, 110)); // Teal
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Custom Animated DropZone Panel ---
    class DropZonePanel extends JPanel {
        private float pulsePhase = 0f;
        private boolean hover = false;
        private Timer animationTimer;

        public DropZonePanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setOpaque(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            animationTimer = new Timer(30, e -> {
                if (!hover) {
                    pulsePhase += 0.05f;
                    if (pulsePhase > Math.PI * 2) pulsePhase = 0f;
                    repaint();
                }
            });
            animationTimer.start();

            addMouseListener(new MouseAdapter() {
                @Override public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                @Override public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (hover) {
                g2.setColor(new Color(238, 242, 255)); // Indigo 50
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 16, 16);
                
                g2.setStroke(new BasicStroke(2.0f));
                g2.setColor(new Color(99, 102, 241)); // Indigo 500
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
            } else {
                g2.setColor(new Color(248, 250, 252)); // Slate 50
                g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 16, 16);
                
                // Pulsating border
                float alpha = (float)(Math.sin(pulsePhase) + 1) / 2f; // 0.0 to 1.0
                int borderAlpha = 100 + (int)(alpha * 155); // 100 to 255
                
                Stroke dashed = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 0, new float[]{8}, 0);
                g2.setStroke(dashed);
                g2.setColor(new Color(165, 180, 252, borderAlpha)); // Indigo 300
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 16, 16);
            }
            g2.dispose();
        }
    }
}