package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import dao.CompanyDAO;
import util.UIConstants;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ViewCompaniesForm extends JPanel {

    private JPanel cardsPanel;
    private int selectedCompanyId = -1;
    private String selectedCompanyName = "";
    private List<AnimatedCardPanel> allCards = new ArrayList<>();

    public ViewCompaniesForm() {
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(20, 20, 20, 20));
        setLayout(new BorderLayout(0, 15));

        JLabel titleLabel = new JLabel("Registered Companies List");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(UIConstants.TEXT_DARK);
        add(titleLabel, BorderLayout.NORTH);

        cardsPanel = new JPanel();
        cardsPanel.setBackground(Color.WHITE);
        cardsPanel.setLayout(new GridLayout(0, 2, 20, 20));
        cardsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnDelete = new JButton("Delete Selected Company");
        btnDelete.setBackground(UIConstants.REJECTED);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFont(UIConstants.BOLD_FONT);
        btnDelete.setFocusPainted(false);
        btnDelete.setPreferredSize(new Dimension(200, 40));
        btnDelete.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        bottomPanel.setOpaque(false);
        bottomPanel.add(btnDelete);
        add(bottomPanel, BorderLayout.SOUTH);

        btnDelete.addActionListener(e -> {
            if (selectedCompanyId == -1) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a company card first.",
                        "No Selection",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to permanently delete '" + selectedCompanyName + "'?\nThis cannot be undone.",
                    "Confirm Deletion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                CompanyDAO dao = new CompanyDAO();
                boolean result = dao.deleteCompany(selectedCompanyId);

                if (result) {
                    JOptionPane.showMessageDialog(
                            this,
                            "Company '" + selectedCompanyName + "' deleted successfully.",
                            "Success",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    selectedCompanyId = -1; 
                    selectedCompanyName = "";
                    loadCompaniesData(); 
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Cannot delete company. Please remove all active placement drives associated with it first.",
                            "Deletion Failed",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

        loadCompaniesData();
    }

    public void loadCompaniesData() {
        cardsPanel.removeAll();
        allCards.clear();
        selectedCompanyId = -1;
        selectedCompanyName = "";

        try {
            CompanyDAO dao = new CompanyDAO();
            ResultSet rs = dao.getAllCompanies();

            // Vibrant solid colors for cards
            Color[] themeColors = {
                new Color(41, 128, 185), // Deep Blue
                new Color(39, 174, 96),  // Deep Green
                new Color(142, 68, 173), // Deep Purple
                new Color(211, 84, 0),   // Burnt Orange
                new Color(192, 57, 43),  // Deep Red
                new Color(22, 160, 133)  // Teal
            };
            int colorIndex = 0;

            while (rs.next()) {
                int id = rs.getInt("company_id");
                String name = rs.getString("company_name");
                String location = rs.getString("location");
                double pkg = rs.getDouble("company_package");
                String role = rs.getString("role");

                AnimatedCardPanel card = new AnimatedCardPanel(id, name, location, pkg, role, themeColors[colorIndex % themeColors.length]);
                
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        for (AnimatedCardPanel c : allCards) {
                            c.setSelected(false);
                        }
                        card.setSelected(true);
                        selectedCompanyId = id;
                        selectedCompanyName = name;
                    }
                });

                cardsPanel.add(card);
                allCards.add(card);
                colorIndex++;
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // Custom panel class for 3D animation and solid colors
    class AnimatedCardPanel extends JPanel {
        private int id;
        private Color themeColor;
        private boolean isHovered = false;
        private boolean isSelected = false;
        private float animationProgress = 0f; // 0 to 1 for hover animation
        private Timer animationTimer;

        public AnimatedCardPanel(int id, String name, String location, double pkg, String role, Color themeColor) {
            this.id = id;
            this.themeColor = themeColor;
            setOpaque(false); // We will paint the background ourselves
            setLayout(new BorderLayout(10, 10));
            setBorder(new EmptyBorder(20, 20, 20, 20)); // padding inside card
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);

            // Text color is white to contrast with vibrant background
            Color textColor = Color.WHITE;
            Color subTextColor = new Color(240, 240, 240);

            JLabel nameLabel = new JLabel(name);
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
            nameLabel.setForeground(textColor);
            nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel roleLabel = new JLabel("Role: " + role);
            roleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            roleLabel.setForeground(subTextColor);
            roleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            JLabel locLabel = new JLabel("Location: " + location);
            locLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            locLabel.setForeground(subTextColor);
            locLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            
            // For package, maybe use a slightly bright yellowish color to make it pop
            JLabel pkgLabel = new JLabel(String.format("Package: %.2f LPA", pkg));
            pkgLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));
            pkgLabel.setForeground(new Color(255, 235, 132)); 
            pkgLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            contentPanel.add(nameLabel);
            contentPanel.add(Box.createVerticalStrut(10));
            contentPanel.add(roleLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(locLabel);
            contentPanel.add(Box.createVerticalStrut(5));
            contentPanel.add(pkgLabel);

            add(contentPanel, BorderLayout.CENTER);

            // Timer for smooth hover animation
            animationTimer = new Timer(15, e -> {
                boolean repainted = false;
                if (isHovered && animationProgress < 1f) {
                    animationProgress = Math.min(1f, animationProgress + 0.1f);
                    repainted = true;
                } else if (!isHovered && animationProgress > 0f) {
                    animationProgress = Math.max(0f, animationProgress - 0.1f);
                    repainted = true;
                }
                
                if (repainted) {
                    repaint();
                } else {
                    animationTimer.stop();
                }
            });

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    animationTimer.start();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    animationTimer.start();
                }
            });
        }

        public void setSelected(boolean selected) {
            this.isSelected = selected;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int width = getWidth();
            int height = getHeight();

            // Calculate animated lift (moves up by 5 pixels on hover)
            int lift = (int) (animationProgress * 5);
            
            // Draw shadow
            int shadowOffset = 3 + lift;
            int shadowSize = 10 + (lift * 2);
            g2.setColor(new Color(0, 0, 0, 40 - (lift * 2))); // shadow fades slightly when higher
            g2.fillRoundRect(8, 8 + shadowOffset, width - 16, height - 16, 20, 20);

            // Draw card background
            int cardY = 5 - lift;
            Color bgColor = themeColor;
            
            // Brighten color slightly if selected to indicate selection
            if (isSelected) {
                bgColor = new Color(
                    Math.min(255, bgColor.getRed() + 30),
                    Math.min(255, bgColor.getGreen() + 30),
                    Math.min(255, bgColor.getBlue() + 30)
                );
            }

            g2.setColor(bgColor);
            g2.fillRoundRect(5, cardY, width - 10, height - 15, 15, 15);

            // Draw selection border if selected
            if (isSelected) {
                g2.setColor(new Color(255, 255, 255, 200));
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(5, cardY, width - 10, height - 15, 15, 15);
            }

            g2.dispose();
        }
        
        // Override getInsets to push content up dynamically with the card
        @Override
        public Insets getInsets() {
            int lift = (int) (animationProgress * 5);
            return new Insets(20 - lift, 20, 20 + lift, 20);
        }
    }
}
