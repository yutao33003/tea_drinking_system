package tea_drinking_system.frame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import tea_drinking_system.employee.EmployeeInitialization;

import java.awt.Font;
import java.util.Enumeration;
import javax.swing.UIDefaults;

public class Main {
    public static void main(String[] args) {
    	
        // 設置系統屬性確保正確的中文編碼
        System.setProperty("file.encoding", "UTF-8");
        System.setProperty("sun.jnu.encoding", "UTF-8");
        
        SwingUtilities.invokeLater(() -> {
            try {
                // 設定系統外觀
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // 設置全局中文字體
                setGlobalFont();
                
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            // 啟動應用程式
            new DrinkFrame();
            EmployeeInitialization.startAllEmployees();
        });
    }
    
    private static void setGlobalFont() {

        Font chineseFont = new Font("微軟正黑體", Font.PLAIN, 12);
        Font chineseFontBold = new Font("微軟正黑體", Font.BOLD, 12);
        
        if (!chineseFont.getFamily().equals("微軟正黑體")) {

            String[] chineseFonts = {"SimHei", "SimSun", "Microsoft YaHei", "Arial Unicode MS", "Dialog"};
            for (String fontName : chineseFonts) {
                Font testFont = new Font(fontName, Font.PLAIN, 12);
                if (testFont.canDisplayUpTo("中文測試") == -1) {
                    chineseFont = testFont;
                    chineseFontBold = new Font(fontName, Font.BOLD, 12);
                    break;
                }
            }
        }
        
        // 設置所有UI組件的預設字體
        UIDefaults defaults = UIManager.getDefaults();
        Enumeration<Object> keys = defaults.keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = defaults.get(key);
            if (value instanceof Font) {
                Font font = (Font) value;
                if (font.isBold()) {
                    defaults.put(key, chineseFontBold.deriveFont(font.getSize2D()));
                } else {
                    defaults.put(key, chineseFont.deriveFont(font.getSize2D()));
                }
            }
        }
    }
}