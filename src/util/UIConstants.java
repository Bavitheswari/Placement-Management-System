package util;

import java.awt.Color;
import java.awt.Font;

public class UIConstants {

    // Modern harmonious color tokens (Slate & Indigo theme)
    public static final Color SIDEBAR = new Color(15, 23, 42);       // Slate 900 (Deep dark sidebar)
    public static final Color HEADER = new Color(30, 41, 59);        // Slate 800 (Top header)
    public static final Color BUTTON = new Color(79, 70, 229);       // Indigo 600 (Primary buttons)
    public static final Color BUTTON_HOVER = new Color(67, 56, 202); // Indigo 700
    public static final Color BACKGROUND = new Color(248, 250, 252);  // Slate 50 (Very light background)
    public static final Color CARD_BG = Color.WHITE;                 // Card background
    public static final Color BORDER = new Color(226, 232, 240);      // Slate 200 (Light border)
    
    // Status colors
    public static final Color APPROVED = new Color(16, 185, 129);    // Emerald 500 (Success/Approved)
    public static final Color REJECTED = new Color(239, 68, 68);     // Rose 500 (Error/Rejected)
    public static final Color PENDING = new Color(245, 158, 11);     // Amber 500 (Warning/Pending)
    
    // Text colors
    public static final Color TEXT_DARK = new Color(15, 23, 42);     // Slate 900
    public static final Color TEXT_LIGHT = Color.WHITE;
    public static final Color TEXT_MUTED = new Color(100, 116, 139);  // Slate 500

    // Typography
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 28);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font NORMAL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
}