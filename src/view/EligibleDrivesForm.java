package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import dao.ApplicationDAO;
import dao.DriveDAO;
import dao.StudentDAO;
import model.Application;
import util.UserSession;
import util.components.AnimatedButton;

public class EligibleDrivesForm extends JPanel {

    private JPanel listPanel;

    public EligibleDrivesForm() {
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setLayout(new BorderLayout(0, 15));

        JLabel titleLabel = new JLabel("Your Eligible Placement Drives");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Companies you meet the criteria for — click Apply to submit your profile");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 3));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(new Color(248, 250, 252));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadEligibleDrives();
    }

    private void loadEligibleDrives() {
        listPanel.removeAll();
        try {
            StudentDAO studentDAO = new StudentDAO();
            int studentId = studentDAO.getStudentIdByEmail(UserSession.studentEmail);
            double cgpa = studentDAO.getStudentCgpa(studentId);

            DriveDAO driveDAO = new DriveDAO();
            ResultSet rs = driveDAO.getEligibleDrives(cgpa);

            Color[] accentColors = {
                new Color(99, 102, 241), new Color(16, 185, 129), 
                new Color(245, 158, 11), new Color(236, 72, 153)
            };
            int colorIndex = 0;
            boolean hasDrives = false;

            while (rs.next()) {
                hasDrives = true;
                int driveId = rs.getInt("drive_id");
                String company = rs.getString("company_name");
                String date = rs.getString("drive_date");
                double minCgpa = rs.getDouble("min_cgpa");
                Color accent = accentColors[colorIndex % accentColors.length];
                
                listPanel.add(createTimelineRow(driveId, company, date, minCgpa, accent));
                listPanel.add(Box.createVerticalStrut(12));
                colorIndex++;
            }
            
            if (!hasDrives) {
                JLabel emptyLbl = new JLabel("No eligible drives found at this moment.");
                emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                emptyLbl.setForeground(new Color(148, 163, 184));
                listPanel.add(emptyLbl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createTimelineRow(int driveId, String company, String date, double cgpa, Color accent) {
        JPanel row = new JPanel(new BorderLayout(15, 0)) {
            private float shadowAlpha = 0f;
            private Timer timer;
            private boolean hovering = false;
            {
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                    new EmptyBorder(16, 0, 16, 20)
                ));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));
                
                timer = new Timer(15, e -> {
                    if (hovering && shadowAlpha < 1f) { shadowAlpha = Math.min(1f, shadowAlpha + 0.15f); repaint(); }
                    else if (!hovering && shadowAlpha > 0f) { shadowAlpha = Math.max(0f, shadowAlpha - 0.15f); repaint(); }
                    else { timer.stop(); }
                });
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hovering = true; timer.start(); }
                    @Override public void mouseExited(MouseEvent e) { hovering = false; timer.start(); }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (shadowAlpha > 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), (int)(shadowAlpha * 20)));
                    g2.fillRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 8, 8);
                    g2.dispose();
                }
            }
        };

        // Left Accent Bar
        JPanel accentBar = new JPanel();
        accentBar.setBackground(accent);
        accentBar.setPreferredSize(new Dimension(5, 0));

        // Dot indicator
        JPanel dotPanel = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                int cx = getWidth() / 2, cy = getHeight() / 2;
                g2.setColor(new Color(accent.getRed(), accent.getGreen(), accent.getBlue(), 50));
                g2.fillOval(cx - 14, cy - 14, 28, 28);
                g2.setColor(accent);
                g2.fillOval(cx - 8, cy - 8, 16, 16);
                g2.dispose();
            }
        };
        dotPanel.setOpaque(false);
        dotPanel.setPreferredSize(new Dimension(55, 0));

        // Middle Content
        JPanel content = new JPanel(new GridLayout(2, 1, 0, 5));
        content.setOpaque(false);

        JLabel compLbl = new JLabel(company);
        compLbl.setFont(new Font("Segoe UI", Font.BOLD, 17));
        compLbl.setForeground(new Color(15, 23, 42));

        JPanel metaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        metaPanel.setOpaque(false);
        metaPanel.add(createBadge("📅 " + date, new Color(241, 245, 249), new Color(71, 85, 105)));
        metaPanel.add(Box.createHorizontalStrut(10));
        metaPanel.add(createBadge("Required CGPA: " + cgpa, new Color(220, 252, 231), new Color(22, 101, 52)));
        
        content.add(compLbl);
        content.add(metaPanel);

        // Right side: Apply Button
        AnimatedButton btnApply = new AnimatedButton("Apply Now", accent);
        btnApply.setPreferredSize(new Dimension(130, 40));
        btnApply.addActionListener(e -> applyDrive(driveId));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        rightPanel.add(btnApply, BorderLayout.CENTER);

        row.add(accentBar, BorderLayout.WEST);
        row.add(dotPanel, BorderLayout.LINE_START);
        row.add(content, BorderLayout.CENTER);
        row.add(rightPanel, BorderLayout.EAST);

        return row;
    }

    private JLabel createBadge(String text, Color bg, Color fg) {
        JLabel badge = new JLabel(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 11));
        badge.setForeground(fg);
        badge.setBackground(bg);
        badge.setOpaque(false);
        badge.setBorder(new EmptyBorder(4, 10, 4, 10));
        return badge;
    }

    private void applyDrive(int driveId) {
        try {
            StudentDAO studentDAO = new StudentDAO();
            int studentId = studentDAO.getStudentIdByEmail(UserSession.studentEmail);

            // Check resume
            String resumePath = studentDAO.getResumePathByStudentId(studentId);
            if (resumePath == null || resumePath.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                    "You must upload a resume before applying for drives.",
                    "Resume Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }

            ApplicationDAO dao = new ApplicationDAO();
            if (dao.alreadyApplied(studentId, driveId)) {
                JOptionPane.showMessageDialog(this,
                    "You have already submitted an application for this drive.",
                    "Already Applied", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            Application app = new Application();
            app.setStudentId(studentId);
            app.setDriveId(driveId);
            app.setStatus("Pending");

            if (dao.applyForDrive(app)) {
                JOptionPane.showMessageDialog(this,
                    "Application submitted successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Failed to submit application.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}