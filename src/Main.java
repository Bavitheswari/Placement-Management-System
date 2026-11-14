import view.HomePage;
import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIManager;

public class Main {

    public static void main(String[] args) {
        try {
            // Apply FlatLaf look and feel
            FlatLightLaf.setup();
            
            // Premium layout and UI tweaks
            UIManager.put("Button.arc", 12);                // Beautiful rounded buttons
            UIManager.put("Component.arc", 10);             // Rounded text fields, comboboxes, spinner
            UIManager.put("TextComponent.arc", 10);
            UIManager.put("ScrollBar.thumbArc", 999);       // Pill-shaped scrollbars
            UIManager.put("ScrollBar.width", 10);           // Thinner scrollbars
            UIManager.put("Table.rowHeight", 32);           // Spacious table rows
            UIManager.put("Table.showHorizontalLines", true);
            UIManager.put("Table.showVerticalLines", false); // Modern borderless vertical dividers
            UIManager.put("Table.gridColor", new java.awt.Color(241, 245, 249));
            UIManager.put("TableHeader.height", 38);        // Tall headers
            UIManager.put("TableHeader.font", new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
            UIManager.put("TableHeader.background", new java.awt.Color(241, 245, 249));
            UIManager.put("TableHeader.separatorColor", new java.awt.Color(241, 245, 249));
            
        } catch (Exception e) {
            System.err.println("FlatLaf theme failed to load. Falling back to default.");
        }
        
        new HomePage();
    }
}