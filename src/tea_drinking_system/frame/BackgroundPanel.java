package tea_drinking_system.frame;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    private Image backgroundImage;
    
    public BackgroundPanel(String imagePath) {
        loadBackgroundImage(imagePath);
        setLayout(new CardLayout()); // 切換頁面
    }
    
    private void loadBackgroundImage(String imagePath) {
        try {
            java.net.URL imageURL = getClass().getResource(imagePath);
            if (imageURL == null) {
                System.err.println("圖片找不到！請確認路徑與位置是否正確: " + imagePath);
                // 創建一個預設的漸層背景
                createDefaultBackground();
            } else {
                backgroundImage = new ImageIcon(imageURL).getImage();
            }
        } catch (Exception e) {
            System.err.println("載入背景圖片時發生錯誤: " + e.getMessage());
            createDefaultBackground();
        }
    }
    
    private void createDefaultBackground() {
        // 如果沒有背景圖片，創建一個簡單的漸層背景
        backgroundImage = null;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            // 繪製背景圖片
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            // 繪製預設漸層背景
            Graphics2D g2d = (Graphics2D) g;
            GradientPaint gradient = new GradientPaint(
                0, 0, new Color(230, 240, 250),
                0, getHeight(), new Color(200, 220, 240)
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}