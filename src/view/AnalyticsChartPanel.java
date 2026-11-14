package view;

import javax.swing.*;
import java.awt.*;
import dao.StatisticsDAO;
import util.UIConstants;

public class AnalyticsChartPanel extends JPanel {

    private final StatisticsDAO statsDAO;

    public AnalyticsChartPanel() {
        this.statsDAO = new StatisticsDAO();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int approved = statsDAO.approvedCount();
        int rejected = statsDAO.rejectedCount();
        int pending = statsDAO.pendingCount();
        int total = approved + rejected + pending;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw title
        g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2.setColor(UIConstants.TEXT_DARK);
        g2.drawString("Application Status Analysis", 20, 30);

        if (total == 0) {
            g2.setFont(UIConstants.NORMAL_FONT);
            g2.setColor(UIConstants.TEXT_MUTED);
            g2.drawString("No applications data available to display chart.", 20, 80);
            g2.dispose();
            return;
        }

        // Draw dynamic statistics details
        int chartY = 70;
        int barHeight = 25;
        int maxBarWidth = Math.max(100, width - 260);

        // Calculate ratios
        double approvedRatio = (double) approved / total;
        double rejectedRatio = (double) rejected / total;
        double pendingRatio = (double) pending / total;

        // Draw Bars
        int currentY = chartY;

        // Approved Bar
        drawStatBar(g2, "Approved", approved, approvedRatio, UIConstants.APPROVED, 20, currentY, maxBarWidth, barHeight);
        currentY += 50;

        // Pending Bar
        drawStatBar(g2, "Pending", pending, pendingRatio, UIConstants.PENDING, 20, currentY, maxBarWidth, barHeight);
        currentY += 50;

        // Rejected Bar
        drawStatBar(g2, "Rejected", rejected, rejectedRatio, UIConstants.REJECTED, 20, currentY, maxBarWidth, barHeight);
        
        // Draw a neat Pie Chart next to it if width permits
        if (width > 420) {
            int pieSize = Math.min(150, height - 120);
            int pieX = width - pieSize - 40;
            int pieY = 60;

            int startAngle = 0;
            int approvedAngle = (int) Math.round(approvedRatio * 360);
            int pendingAngle = (int) Math.round(pendingRatio * 360);
            int rejectedAngle = 360 - approvedAngle - pendingAngle;

            // Paint approved arc
            g2.setColor(UIConstants.APPROVED);
            g2.fillArc(pieX, pieY, pieSize, pieSize, startAngle, approvedAngle);
            startAngle += approvedAngle;

            // Paint pending arc
            g2.setColor(UIConstants.PENDING);
            g2.fillArc(pieX, pieY, pieSize, pieSize, startAngle, pendingAngle);
            startAngle += pendingAngle;

            // Paint rejected arc
            g2.setColor(UIConstants.REJECTED);
            g2.fillArc(pieX, pieY, pieSize, pieSize, startAngle, rejectedAngle);

            // Inner circle for donut chart styling
            int innerSize = (int) (pieSize * 0.5);
            int innerX = pieX + (pieSize - innerSize) / 2;
            int innerY = pieY + (pieSize - innerSize) / 2;
            g2.setColor(Color.WHITE);
            g2.fillOval(innerX, innerY, innerSize, innerSize);

            // Draw center text
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.setColor(UIConstants.TEXT_MUTED);
            String centerStr = "Total: " + total;
            FontMetrics fm = g2.getFontMetrics();
            int strW = fm.stringWidth(centerStr);
            g2.drawString(centerStr, innerX + (innerSize - strW) / 2, innerY + (innerSize / 2) + 4);
        }

        g2.dispose();
    }

    private void drawStatBar(Graphics2D g2, String label, int count, double ratio, Color barColor, 
                             int x, int y, int maxWidth, int height) {
        // Draw label & count
        g2.setFont(UIConstants.BOLD_FONT);
        g2.setColor(UIConstants.TEXT_DARK);
        g2.drawString(label + " (" + count + ")", x, y + 15);

        // Draw track background
        g2.setColor(new Color(241, 245, 249)); // Slate 100
        g2.fillRoundRect(x + 130, y, maxWidth, height, 8, 8);

        // Draw bar progress
        int progressWidth = (int) (ratio * maxWidth);
        if (progressWidth > 0) {
            g2.setColor(barColor);
            g2.fillRoundRect(x + 130, y, progressWidth, height, 8, 8);
        }

        // Draw percentage text inside or next to the bar
        g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
        g2.setColor(UIConstants.TEXT_MUTED);
        String pctStr = String.format("%.1f%%", ratio * 100);
        g2.drawString(pctStr, x + 130 + maxWidth + 10, y + 16);
    }
}
