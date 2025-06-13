package tea_drinking_system.frame;

import javax.swing.*;
import java.awt.*;

public class PageFactory {
    private LogOverviewPanel logPanel;
    
    public PageFactory(LogOverviewPanel logPanel) {
        this.logPanel = logPanel;
    }
    
    public JPanel createStartPage(CardLayout layout, JPanel container) {
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        
        // 標題
        JLabel title = new JLabel("歡迎使用茶飲系統");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setFont(new Font("微軟正黑體", Font.BOLD, 32));
        title.setForeground(new Color(44, 62, 80));
        
        // 按鈕樣式設定
        Dimension buttonSize = new Dimension(200, 50);
        Font buttonFont = new Font("微軟正黑體", Font.BOLD, 18);
        
        JButton customerBtn = createStyledButton("顧客點餐", buttonSize, buttonFont, new Color(52, 152, 219));
        JButton employerBtn = createStyledButton("店長管理", buttonSize, buttonFont, new Color(231, 76, 60));
        JButton logBtn = createStyledButton("查看日誌", buttonSize, buttonFont, new Color(46, 204, 113));
        
        customerBtn.addActionListener(e -> {
            logPanel.log("Other", "切換到顧客頁面");
            layout.show(container, "customer");
        });
        
        employerBtn.addActionListener(e -> {
            logPanel.log("Other", "切換到店長頁面");
            layout.show(container, "employer");
        });
        
        logBtn.addActionListener(e -> {
            logPanel.setSourcePage("start"); // 記錄來源頁面
            logPanel.log("Other", "查看日誌頁面");
            layout.show(container, "log");
        });
        
        // 組裝頁面
        panel.add(Box.createVerticalGlue());
        panel.add(title);
        panel.add(Box.createVerticalStrut(50));
        panel.add(customerBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(employerBtn);
        panel.add(Box.createVerticalStrut(20));
        panel.add(logBtn);
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    public JPanel createCustomerPage(CardLayout layout, JPanel container) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // 標題面板
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("顧客點餐系統");
        title.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(title);
        
        // 主要內容面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JButton orderBtn = createNavigationButton("開始點餐", "Customer", "開始點餐流程", "order");
        JButton menuBtn = createNavigationButton("查看菜單", "Customer", "查看菜單", "menuPage" );
        
        mainPanel.add(orderBtn);
        mainPanel.add(menuBtn);
        
        // 返回按鈕面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton backBtn = createStyledButton("返回首頁", new Dimension(150, 40), 
            new Font("微軟正黑體", Font.PLAIN, 16), new Color(149, 165, 166));
        backBtn.addActionListener(e -> {
            logPanel.log("Other", "從顧客頁面返回首頁");
            layout.show(container, "start");
        });
        
        JButton logBtn = createStyledButton("查看日誌", new Dimension(150, 40),
        		new Font("微軟正黑體", Font.PLAIN, 16), new Color(149, 165, 166));
        logBtn.addActionListener(e -> {
            logPanel.log("Other", "前往日誌");
            layout.show(container, "log");
        });
        bottomPanel.add(backBtn);
        bottomPanel.add(logBtn);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    public JPanel createEmployerPage(CardLayout layout, JPanel container) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // 標題面板
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("店長管理系統");
        title.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(title);
        
        // 主要內容面板
        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 15, 15));
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        
        JButton menuMgmtBtn = createNavigationButton("菜單管理", "Employer", "進入菜單管理", "menuManagement");
        JButton staffMgmtBtn = createNavigationButton("員工管理", "Employer", "進入員工管理", "employeeManagement");
        JButton inventoryBtn = createActionButton("庫存管理", "Employer", "進入庫存管理");
        
        mainPanel.add(menuMgmtBtn);
        mainPanel.add(staffMgmtBtn);
        mainPanel.add(inventoryBtn);
        
        // 返回按鈕面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        JButton backBtn = createStyledButton("返回首頁", new Dimension(150, 40), 
            new Font("微軟正黑體", Font.PLAIN, 16), new Color(149, 165, 166));
        backBtn.addActionListener(e -> {
            logPanel.log("Other", "從店長頁面返回首頁");
            layout.show(container, "start");
        });
        
        JButton logBtn = createStyledButton("查看日誌", new Dimension(150, 40),
        		new Font("微軟正黑體", Font.PLAIN, 16), new Color(149, 165, 166));
        logBtn.addActionListener(e -> {
            logPanel.log("Other", "前往日誌");
            layout.show(container, "log");
        });
        bottomPanel.add(backBtn);
        bottomPanel.add(logBtn);
        
        panel.add(titlePanel, BorderLayout.NORTH);
        panel.add(mainPanel, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    
    private JButton createStyledButton(String text, Dimension size, Font font, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setMaximumSize(size);
        button.setMinimumSize(size);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 確保按鈕字體支持中文
        Font chineseFont = FontUtils.createChineseFont(font.getStyle(), font.getSize());
        button.setFont(chineseFont);
        
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(color.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JButton createNavigationButton(String text, String role, String logMessage, String targetPage) {
        JButton button = new JButton(text);
        button.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        button.addActionListener(e -> {
            logPanel.log(role, logMessage);
            // 跳轉到指定頁面
            if (targetPage != null) {
                CardLayout layout = (CardLayout) ((JPanel) button.getParent().getParent().getParent()).getLayout();
                JPanel container = (JPanel) button.getParent().getParent().getParent();
                layout.show(container, targetPage);
            }
        });
        return button;
    }
    
    private JButton createActionButton(String text, String role, String logMessage) {
        JButton button = new JButton(text);
        button.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        button.addActionListener(e -> {
            logPanel.log(role, logMessage);
            // 這裡可以添加其他動作，但不跳轉頁面
        });
        return button;
    }
}
