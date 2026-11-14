package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;

import dao.OfferDAO;

public class StudentOfferForm extends JPanel {
    private OfferDAO offerDAO;
    private JPanel listPanel;
    private int studentId;

    public StudentOfferForm(int studentId) {
        this.offerDAO = new OfferDAO();
        this.studentId = studentId;
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 25, 20, 25));

        // Header
        JLabel titleLabel = new JLabel("My Job Offers");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Your official placement offers and their status");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 4));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        listPanel = new JPanel();
        listPanel.setBackground(new Color(248, 250, 252));
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(248, 250, 252));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        loadOffers(studentId);
    }

    public void refresh() {
        loadOffers(studentId);
    }

    public void loadOffers(int studentId) {
        listPanel.removeAll();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));

        try {
            ResultSet rs = offerDAO.getOffersByStudent(studentId);
            boolean hasOffers = false;
            
            if (rs != null) {
                while (rs.next()) {
                    hasOffers = true;
                    int offerId = rs.getInt("offer_id");
                    String company = rs.getString("company_name");
                    String role = rs.getString("role");
                    double ctc = rs.getDouble("ctc");
                    String offerDate = rs.getDate("offer_date").toString();
                    String status = rs.getString("status");

                    listPanel.add(createBannerTile(offerId, company, role, ctc, offerDate, status));
                    listPanel.add(Box.createVerticalStrut(15));
                }
            }

            if (!hasOffers) {
                JLabel emptyLbl = new JLabel("No job offers yet. Keep applying!");
                emptyLbl.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                emptyLbl.setForeground(new Color(148, 163, 184));
                emptyLbl.setAlignmentX(Component.CENTER_ALIGNMENT);
                
                listPanel.setLayout(new GridBagLayout()); // center it
                listPanel.add(emptyLbl);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        listPanel.add(Box.createVerticalGlue());
        listPanel.revalidate();
        listPanel.repaint();
    }

    private JPanel createBannerTile(int offerId, String company, String role, double ctc, String date, String status) {
        Color statusBg, statusFg;
        if ("Accepted".equalsIgnoreCase(status)) {
            statusBg = new Color(220, 252, 231);
            statusFg = new Color(22, 101, 52);
        } else if ("Declined".equalsIgnoreCase(status)) {
            statusBg = new Color(254, 226, 226);
            statusFg = new Color(153, 27, 27);
        } else {
            statusBg = new Color(254, 243, 199);
            statusFg = new Color(180, 83, 9);
        }

        JPanel tile = new JPanel(new BorderLayout(15, 0)) {
            private float shadow = 0f;
            private Timer timer;
            private boolean hover = false;
            {
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
                    new EmptyBorder(20, 25, 20, 25)
                ));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                
                timer = new Timer(15, e -> {
                    if (hover && shadow < 1f) { shadow = Math.min(1f, shadow + 0.15f); repaint(); }
                    else if (!hover && shadow > 0f) { shadow = Math.max(0f, shadow - 0.15f); repaint(); }
                    else timer.stop();
                });
                
                addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { hover = true; timer.start(); }
                    @Override public void mouseExited(MouseEvent e) { hover = false; timer.start(); }
                    @Override public void mouseClicked(MouseEvent e) {
                        if ("Pending".equalsIgnoreCase(status)) {
                            String[] options = {"Accept Offer", "Decline Offer", "Later"};
                            int choice = JOptionPane.showOptionDialog(
                                StudentOfferForm.this,
                                "Offer from " + company + " (" + String.format("%.2f LPA", ctc) + ") for role: " + role + "\nWould you like to accept or decline this offer?",
                                "Respond to Job Offer",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                options,
                                options[0]
                            );
                            if (choice == 0) {
                                offerDAO.updateOfferStatus(offerId, "Accepted");
                                loadOffers(studentId);
                            } else if (choice == 1) {
                                offerDAO.updateOfferStatus(offerId, "Declined");
                                loadOffers(studentId);
                            }
                        }
                    }
                });
            }
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (shadow > 0) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(new Color(0, 0, 0, (int)(shadow * 12)));
                    g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 12, 12);
                    g2.dispose();
                }
            }
        };

        // Left side: Company & Role
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setOpaque(false);

        JLabel compLbl = new JLabel(company);
        compLbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        compLbl.setForeground(new Color(15, 23, 42));

        JLabel roleLbl = new JLabel(role);
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        roleLbl.setForeground(new Color(71, 85, 105));

        JLabel dateLbl = new JLabel("Offered on " + date);
        dateLbl.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateLbl.setForeground(new Color(148, 163, 184));

        leftPanel.add(compLbl);
        leftPanel.add(Box.createVerticalStrut(4));
        leftPanel.add(roleLbl);
        leftPanel.add(Box.createVerticalStrut(8));
        leftPanel.add(dateLbl);

        // Right side: CTC & Status Badge
        JPanel rightPanel = new JPanel(new BorderLayout(0, 8));
        rightPanel.setOpaque(false);

        JLabel ctcLbl = new JLabel(String.format("₹ %.2f LPA", ctc));
        ctcLbl.setFont(new Font("Segoe UI", Font.BOLD, 20));
        ctcLbl.setForeground(new Color(16, 185, 129));
        ctcLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        JLabel badge = new JLabel(status, SwingConstants.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(statusBg);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        badge.setFont(new Font("Segoe UI", Font.BOLD, 13));
        badge.setForeground(statusFg);
        badge.setBorder(new EmptyBorder(6, 16, 6, 16));
        badge.setOpaque(false);

        JPanel badgeWrap = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        badgeWrap.setOpaque(false);
        badgeWrap.add(badge);

        rightPanel.add(ctcLbl, BorderLayout.NORTH);
        rightPanel.add(badgeWrap, BorderLayout.SOUTH);

        tile.add(leftPanel, BorderLayout.CENTER);
        tile.add(rightPanel, BorderLayout.EAST);

        return tile;
    }
}
