package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.ResultSet;

import dao.OfferDAO;
import model.Offer;
import util.components.AnimatedButton;

public class OfferManagementForm extends JPanel {
    private OfferDAO offerDAO;
    private JPanel pendingCol;
    private JPanel acceptedCol;
    private JPanel declinedCol;

    public OfferManagementForm() {
        this.offerDAO = new OfferDAO();
        setLayout(new BorderLayout(0, 15));
        setBackground(new Color(248, 250, 252));
        setBorder(new EmptyBorder(20, 20, 20, 20));

        // Header
        JLabel titleLabel = new JLabel("Offer Management Board");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(new Color(15, 23, 42));

        JLabel subtitle = new JLabel("Kanban overview of all student placement offers");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        subtitle.setForeground(new Color(100, 116, 139));

        JPanel titlePanel = new JPanel(new BorderLayout(0, 3));
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitle, BorderLayout.SOUTH);
        add(titlePanel, BorderLayout.NORTH);

        // Kanban Board
        JPanel kanbanBoard = new JPanel(new GridLayout(1, 3, 20, 0));
        kanbanBoard.setOpaque(false);

        pendingCol = createKanbanColumn("Pending Offers", new Color(245, 158, 11));
        acceptedCol = createKanbanColumn("Accepted Offers", new Color(16, 185, 129));
        declinedCol = createKanbanColumn("Declined Offers", new Color(239, 68, 68));

        kanbanBoard.add(new JScrollPane(pendingCol) {{
            setBorder(BorderFactory.createEmptyBorder());
            getViewport().setBackground(new Color(241, 245, 249));
            getVerticalScrollBar().setUnitIncrement(16);
        }});
        kanbanBoard.add(new JScrollPane(acceptedCol) {{
            setBorder(BorderFactory.createEmptyBorder());
            getViewport().setBackground(new Color(241, 245, 249));
            getVerticalScrollBar().setUnitIncrement(16);
        }});
        kanbanBoard.add(new JScrollPane(declinedCol) {{
            setBorder(BorderFactory.createEmptyBorder());
            getViewport().setBackground(new Color(241, 245, 249));
            getVerticalScrollBar().setUnitIncrement(16);
        }});

        add(kanbanBoard, BorderLayout.CENTER);

        AnimatedButton addOfferBtn = new AnimatedButton("+ Issue New Offer", new Color(99, 102, 241));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 5));
        bottomPanel.setOpaque(false);
        bottomPanel.add(addOfferBtn);
        add(bottomPanel, BorderLayout.SOUTH);

        addOfferBtn.addActionListener(e -> showAddOfferDialog());
        loadOffers();
    }

    private JPanel createKanbanColumn(String title, Color accent) {
        JPanel column = new JPanel();
        column.setLayout(new BoxLayout(column, BoxLayout.Y_AXIS));
        column.setBackground(new Color(241, 245, 249));
        column.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(226, 232, 240), 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Column Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        header.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLbl = new JLabel(title);
        titleLbl.setFont(new Font("Segoe UI", Font.BOLD, 15));
        titleLbl.setForeground(new Color(30, 41, 59));
        
        JPanel strip = new JPanel();
        strip.setBackground(accent);
        strip.setPreferredSize(new Dimension(0, 4));

        header.add(titleLbl, BorderLayout.CENTER);
        header.add(strip, BorderLayout.NORTH);
        
        column.add(header);
        return column;
    }

    private void loadOffers() {
        // Clear old items (keep header at index 0)
        while (pendingCol.getComponentCount() > 1) pendingCol.remove(1);
        while (acceptedCol.getComponentCount() > 1) acceptedCol.remove(1);
        while (declinedCol.getComponentCount() > 1) declinedCol.remove(1);

        try {
            ResultSet rs = offerDAO.getAllOffers();
            if (rs != null) {
                while (rs.next()) {
                    int offerId = rs.getInt("offer_id");
                    String studentName = rs.getString("name");
                    String company = rs.getString("company_name");
                    String role = rs.getString("role");
                    double ctc = rs.getDouble("ctc");
                    String status = rs.getString("status");

                    JPanel card = createOfferCard(offerId, studentName, company, role, ctc, status);
                    
                    if ("Accepted".equalsIgnoreCase(status)) {
                        acceptedCol.add(card);
                        acceptedCol.add(Box.createVerticalStrut(10));
                    } else if ("Declined".equalsIgnoreCase(status)) {
                        declinedCol.add(card);
                        declinedCol.add(Box.createVerticalStrut(10));
                    } else {
                        pendingCol.add(card);
                        pendingCol.add(Box.createVerticalStrut(10));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        pendingCol.revalidate(); pendingCol.repaint();
        acceptedCol.revalidate(); acceptedCol.repaint();
        declinedCol.revalidate(); declinedCol.repaint();
    }

    private JPanel createOfferCard(int offerId, String studentName, String company, String role, double ctc, String currentStatus) {
        JPanel card = new JPanel(new BorderLayout(0, 8)) {
            private float shadow = 0f;
            private Timer timer;
            private boolean hover = false;
            {
                setOpaque(true);
                setBackground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(203, 213, 225), 1, true),
                    new EmptyBorder(12, 14, 12, 14)
                ));
                setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
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
                        String[] options = {"Pending", "Accepted", "Declined", "Cancel"};
                        int choice = JOptionPane.showOptionDialog(
                            OfferManagementForm.this,
                            "Update status for " + studentName + "'s offer at " + company + ":",
                            "Update Offer Status",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            options,
                            currentStatus
                        );
                        if (choice >= 0 && choice < 3) {
                            offerDAO.updateOfferStatus(offerId, options[choice]);
                            loadOffers();
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
                    g2.setColor(new Color(0, 0, 0, (int)(shadow * 15)));
                    g2.fillRoundRect(2, 4, getWidth() - 4, getHeight() - 6, 8, 8);
                    g2.dispose();
                }
            }
        };

        JLabel nameLbl = new JLabel(studentName);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        nameLbl.setForeground(new Color(15, 23, 42));

        JLabel compLbl = new JLabel("\uD83C\uDFE2 " + company);
        compLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        compLbl.setForeground(new Color(71, 85, 105));

        JLabel roleLbl = new JLabel("\uD83D\uDCBC " + role);
        roleLbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        roleLbl.setForeground(new Color(71, 85, 105));

        JPanel meta = new JPanel(new GridLayout(2, 1, 0, 4));
        meta.setOpaque(false);
        meta.add(compLbl);
        meta.add(roleLbl);

        JLabel ctcLbl = new JLabel(String.format("%.2f LPA", ctc));
        ctcLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        ctcLbl.setForeground(new Color(16, 185, 129));
        ctcLbl.setHorizontalAlignment(SwingConstants.RIGHT);

        card.add(nameLbl, BorderLayout.NORTH);
        card.add(meta, BorderLayout.CENTER);
        card.add(ctcLbl, BorderLayout.EAST);

        return card;
    }

    private void showAddOfferDialog() {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Add Offer", true);
        dialog.setLayout(new GridLayout(7, 2, 5, 5));
        dialog.getRootPane().setBorder(new EmptyBorder(15, 15, 15, 15));

        dialog.add(new JLabel("Student ID:"));
        JTextField studentIdField = new JTextField();
        dialog.add(studentIdField);

        dialog.add(new JLabel("Company ID:"));
        JTextField companyIdField = new JTextField();
        dialog.add(companyIdField);

        dialog.add(new JLabel("Role:"));
        JTextField roleField = new JTextField();
        dialog.add(roleField);

        dialog.add(new JLabel("CTC (LPA):"));
        JTextField ctcField = new JTextField();
        dialog.add(ctcField);

        dialog.add(new JLabel("Status:"));
        JComboBox<String> statusCombo = new JComboBox<>(new String[]{"Pending", "Accepted", "Declined"});
        dialog.add(statusCombo);

        JButton submitBtn = new JButton("Save Offer");
        dialog.add(new JLabel());
        dialog.add(submitBtn);

        submitBtn.addActionListener(e -> {
            try {
                Offer offer = new Offer();
                offer.setStudentId(Integer.parseInt(studentIdField.getText()));
                offer.setCompanyId(Integer.parseInt(companyIdField.getText()));
                offer.setRole(roleField.getText());
                offer.setCtc(Double.parseDouble(ctcField.getText()));
                offer.setOfferDate(new Date(System.currentTimeMillis()));
                offer.setStatus(statusCombo.getSelectedItem().toString());

                if (offerDAO.addOffer(offer)) {
                    JOptionPane.showMessageDialog(dialog, "Offer added successfully!");
                    loadOffers();
                    dialog.dispose();
                } else {
                    JOptionPane.showMessageDialog(dialog, "Failed to add offer. Please check IDs.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Invalid input. Please enter numbers for IDs and CTC.");
            }
        });

        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}
