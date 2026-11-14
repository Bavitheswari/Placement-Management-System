package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.sql.ResultSet;
import dao.ApplicationDAO;
import dao.StudentDAO;

public class ViewMyApplicationsForm extends JPanel {

    private JPanel listPanel;

    public ViewMyApplicationsForm(String email) {
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 25, 20, 25));
        setLayout(new BorderLayout(0, 15));

        JLabel titleLabel = new JLabel("Application History & Tracker");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Track the live status of your hiring applications");
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

        loadApplications(email);
    }

    private void loadApplications(String email) {
        listPanel.removeAll();
        try {
            StudentDAO studentDAO = new StudentDAO();
            int studentId = studentDAO.getStudentIdByEmail(email);

            ApplicationDAO dao = new ApplicationDAO();
            ResultSet rs = dao.getApplicationsByStudentId(studentId);

            boolean hasApps = false;
            while (rs.next()) {
                hasApps = true;
                String company = rs.getString("company_name");
                String status = rs.getString("status");
                listPanel.add(createTrackerCard(company, status));
                listPanel.add(Box.createVerticalStrut(15));
            }

            if (!hasApps) {
                JLabel emptyLbl = new JLabel("You haven't applied to any drives yet.");
                emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                emptyLbl.setForeground(new Color(148, 163, 184));
                listPanel.add(emptyLbl);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        listPanel.add(Box.createVerticalGlue());
    }

    private JPanel createTrackerCard(String company, String status) {
        JPanel card = new JPanel(new BorderLayout(20, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                // Soft static shadow
                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(true);
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(15, 20, 15, 20)
        ));
        card.setPreferredSize(new Dimension(800, 110));
        card.setMinimumSize(new Dimension(400, 110));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));

        JPanel leftPanel = new JPanel(new BorderLayout(0, 5));
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(200, 0));

        JLabel compLbl = new JLabel(company);
        compLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        compLbl.setForeground(new Color(15, 23, 42));

        JLabel statLbl = new JLabel("Current Status: " + status);
        statLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statLbl.setForeground(new Color(100, 116, 139));

        leftPanel.add(compLbl, BorderLayout.NORTH);
        leftPanel.add(statLbl, BorderLayout.CENTER);

        // Progress Tracker Component
        TrackerGraphics tracker = new TrackerGraphics(status);
        
        card.add(leftPanel, BorderLayout.WEST);
        card.add(tracker, BorderLayout.CENTER);

        return card;
    }

    // --- Custom Animated Tracker Graphics ---
    class TrackerGraphics extends JPanel {
        private String currentStatus;
        private int currentStep;
        
        public TrackerGraphics(String status) {
            this.currentStatus = status;
            setOpaque(false);
            setPreferredSize(new Dimension(300, 80));
            setMinimumSize(new Dimension(200, 80));
            
            if ("Pending".equalsIgnoreCase(status)) currentStep = 1;
            else if ("Under Review".equalsIgnoreCase(status)) currentStep = 2; // Hypothetical middle step
            else if ("Approved".equalsIgnoreCase(status) || "Accepted".equalsIgnoreCase(status)) currentStep = 3;
            else if ("Rejected".equalsIgnoreCase(status) || "Declined".equalsIgnoreCase(status)) currentStep = -1;
            else currentStep = 1; // Default
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int centerY = h / 2 - 10;
            
            int p1x = 40, p2x = w / 2, p3x = w - 40;

            // Base Track line
            g2.setColor(new Color(226, 232, 240));
            g2.setStroke(new BasicStroke(4f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g2.drawLine(p1x, centerY, p3x, centerY);

            // Active Track line
            if (currentStep > 1) {
                g2.setColor(new Color(59, 130, 246)); // Blue
                g2.drawLine(p1x, centerY, currentStep == 2 ? p2x : p3x, centerY);
            }
            if (currentStep == -1) {
                g2.setColor(new Color(239, 68, 68)); // Red for rejected
                g2.drawLine(p1x, centerY, p3x, centerY);
            }

            // Draw Nodes
            drawNode(g2, p1x, centerY, "Applied", currentStep >= 1 || currentStep == -1);
            drawNode(g2, p2x, centerY, "In Review", currentStep >= 2 || currentStep == -1);
            
            if (currentStep == -1) {
                drawNode(g2, p3x, centerY, "Rejected", true);
            } else {
                drawNode(g2, p3x, centerY, "Offered", currentStep == 3);
            }

            g2.dispose();
        }

        private void drawNode(Graphics2D g2, int x, int y, String label, boolean active) {
            int radius = 8;
            
            if (active) {
                // Glow effect
                Color glow = currentStep == -1 ? new Color(239, 68, 68, 80) : new Color(59, 130, 246, 80);
                g2.setColor(glow);
                g2.fillOval(x - radius - 4, y - radius - 4, (radius * 2) + 8, (radius * 2) + 8);
                
                g2.setColor(currentStep == -1 ? new Color(220, 38, 38) : new Color(37, 99, 235));
            } else {
                g2.setColor(new Color(203, 213, 225));
            }
            
            g2.fillOval(x - radius, y - radius, radius * 2, radius * 2);

            // Node label
            g2.setFont(new Font("Segoe UI", active ? Font.BOLD : Font.PLAIN, 11));
            g2.setColor(active ? new Color(15, 23, 42) : new Color(148, 163, 184));
            FontMetrics fm = g2.getFontMetrics();
            int strX = x - (fm.stringWidth(label) / 2);
            g2.drawString(label, strX, y + 24);
        }
    }
}