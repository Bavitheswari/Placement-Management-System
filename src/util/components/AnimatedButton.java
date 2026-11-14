package util.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnimatedButton extends JButton {
    private Color themeColor;
    private boolean isHovered = false;
    private float animationProgress = 0f;
    private Timer animationTimer;

    public AnimatedButton(String text, Color themeColor) {
        super(text);
        this.themeColor = themeColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setOpaque(false);
        setForeground(Color.WHITE);
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(200, 45));

        animationTimer = new Timer(15, e -> {
            boolean repainted = false;
            if (isHovered && animationProgress < 1f) {
                animationProgress = Math.min(1f, animationProgress + 0.15f);
                repainted = true;
            } else if (!isHovered && animationProgress > 0f) {
                animationProgress = Math.max(0f, animationProgress - 0.15f);
                repainted = true;
            }
            
            if (repainted) {
                repaint();
                revalidate();
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

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int lift = (int) (animationProgress * 3); // subtle lift for buttons
        
        int shadowOffset = 2 + lift;
        g2.setColor(new Color(0, 0, 0, 40 - (lift * 4))); 
        g2.fillRoundRect(2, 2 + shadowOffset, width - 4, height - 6, 15, 15);

        int buttonY = 2 - lift;
        Color bgColor = themeColor;
        
        if (isHovered) {
            bgColor = new Color(
                Math.min(255, bgColor.getRed() + 20),
                Math.min(255, bgColor.getGreen() + 20),
                Math.min(255, bgColor.getBlue() + 20)
            );
        }

        g2.setColor(bgColor);
        g2.fillRoundRect(2, buttonY, width - 4, height - 6, 15, 15);

        // draw text manually to shift with lift
        FontMetrics fm = g2.getFontMetrics();
        int textX = (width - fm.stringWidth(getText())) / 2;
        int textY = buttonY + ((height - 6 - fm.getHeight()) / 2) + fm.getAscent();
        
        g2.setColor(getForeground());
        g2.drawString(getText(), textX, textY);

        g2.dispose();
    }
}
