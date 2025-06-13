package tea_drinking_system.frame;

import java.awt.*;

public class FontUtils {
    // 預定義的中文字體
    public static final Font DEFAULT_CHINESE_FONT = createChineseFont(Font.PLAIN, 12);
    public static final Font BOLD_CHINESE_FONT = createChineseFont(Font.BOLD, 14);
    public static final Font LARGE_CHINESE_FONT = createChineseFont(Font.BOLD, 18);
    public static final Font TITLE_CHINESE_FONT = createChineseFont(Font.BOLD, 24);
    
    /**
     * 創建支持中文的字體
     */
    public static Font createChineseFont(int style, int size) {
        String[] chineseFonts = {
            "微軟正黑體",        // Windows
            "Microsoft YaHei",   // Windows 英文版
            "PingFang SC",       // macOS 簡體
            "PingFang TC",       // macOS 繁體
            "Noto Sans CJK SC",  // Linux
            "SimHei",            // 黑體
            "SimSun",            // 宋體
            "Dialog"             // 系統預設
        };
        
        for (String fontName : chineseFonts) {
            Font font = new Font(fontName, style, size);
            // 測試字體是否能顯示中文
            if (font.canDisplayUpTo("中文測試") == -1) {
                return font;
            }
        }
        
        // 如果都不行，返回系統預設字體
        return new Font(Font.DIALOG, style, size);
    }
    
    /**
     * 檢查字體是否支援中文
     */
    public static boolean supportsChinese(Font font) {
        return font.canDisplayUpTo("中文測試繁體简体") == -1;
    }
    
    /**
     * 獲取系統可用的中文字體列表
     */
    public static String[] getAvailableChineseFonts() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] allFonts = ge.getAvailableFontFamilyNames();
        
        java.util.List<String> chineseFonts = new java.util.ArrayList<>();
        for (String fontName : allFonts) {
            Font font = new Font(fontName, Font.PLAIN, 12);
            if (supportsChinese(font)) {
                chineseFonts.add(fontName);
            }
        }
        
        return chineseFonts.toArray(new String[0]);
    }
}

