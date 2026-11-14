package util.components;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AnimatedCardPanel extends JPanel {
    private Color themeColor;
    private boolean isHovered = false;
    private boolean isSelected = false;
    private float animationProgress = 0f;
    private Timer animationTimer;

    public AnimatedCardPanel(Color themeColor) {
        this.themeColor = themeColor;
        setOpaque(false);
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(20, 20, 20, 20));

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

    public void setThemeColor(Color color) {
        this.themeColor = color;
        repaint();
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        repaint();
    }
    
    public boolean isSelected() {
        return this.isSelected;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        int lift = (int) (animationProgress * 5);
        
        int shadowOffset = 3 + lift;
        g2.setColor(new Color(0, 0, 0, 40 - (lift * 2))); 
        g2.fillRoundRect(8, 8 + shadowOffset, width - 16, height - 16, 20, 20);

        int cardY = 5 - lift;
        Color bgColor = themeColor;
        
        if (isSelected) {
            bgColor = new Color(
                Math.min(255, bgColor.getRed() + 30),
                Math.min(255, bgColor.getGreen() + 30),
                Math.min(255, bgColor.getBlue() + 30)
            );
        }

        g2.setColor(bgColor);
        g2.fillRoundRect(5, cardY, width - 10, height - 15, 15, 15);

        if (isSelected) {
            g2.setColor(new Color(255, 255, 255, 200));
            g2.setStroke(new BasicStroke(3f));
            g2.drawRoundRect(5, cardY, width - 10, height - 15, 15, 15);
        }

        g2.dispose();
    }
    
    @Override
    public Insets getInsets() {
        int lift = (int) (animationProgress * 5);
        return new Insets(20 - lift, 20, 20 + lift, 20);
    }
}
