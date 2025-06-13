package tea_drinking_system.frame.customerPanel;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import tea_drinking_system.frame.LogOverviewPanel;
import tea_drinking_system.drinkInformation.Menu;
import tea_drinking_system.drinkInformation.Drink;

public class MenuPage extends JPanel {
    private CardLayout parentLayout;
    private JPanel parentContainer;
    private LogOverviewPanel logger;
    private Menu menu;

    public MenuPage(LogOverviewPanel logger, CardLayout layout, JPanel container) {
        this.logger = logger;
        this.parentLayout = layout;
        this.parentContainer = container;
        this.menu = new Menu();

        logger.log("Other","初始化MenuPage");
        initializeComponents();
        loadMenuItems();

        // 將此頁面加入到父容器的CardLayout中
        parentContainer.add(this, "MenuPage");
        logger.log("Other","MenuPage已加入到佈局中");
        logger.log("Other","MenuPage初始化完成");
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // 標題
        JLabel titleLabel = new JLabel("茶飲菜單", JLabel.CENTER);
        titleLabel.setFont(new Font("Microsoft JhengHei", Font.BOLD, 24));
        titleLabel.setBorder(new EmptyBorder(20, 20, 20, 20));

        add(titleLabel, BorderLayout.NORTH);
    }

    private void loadMenuItems() {
        logger.log("Other","開始載入菜單項目");

        // 菜單項目面板
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        menuPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        menuPanel.setBackground(Color.WHITE);

        // 添加每個飲品
        for (Drink drink : menu.getDrinkList()) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
            itemPanel.setBackground(Color.WHITE);
            itemPanel.setPreferredSize(new Dimension(0, 60));

            JLabel nameLabel = new JLabel("  " + drink.getName());
            nameLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 18));

            JLabel priceLabel = new JLabel("NT$ " + drink.getPrice() + "  ");
            priceLabel.setFont(new Font("Microsoft JhengHei", Font.PLAIN, 18));
            priceLabel.setHorizontalAlignment(JLabel.RIGHT);

            itemPanel.add(nameLabel, BorderLayout.WEST);
            itemPanel.add(priceLabel, BorderLayout.EAST);

            menuPanel.add(itemPanel);
            logger.log("Other","載入飲品: " + drink.getName() + " - NT$ " + drink.getPrice());
        }

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        add(scrollPane, BorderLayout.CENTER);
        
        // 底部按鈕面板
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        
        JButton backBtn = createStyledButton("返回顧客頁面", new Dimension(150, 40),
                new Font("微軟正黑體", Font.PLAIN, 16), new Color(149, 165, 166));
        backBtn.addActionListener(e -> {
            logger.log("Other", "返回顧客頁面");
            parentLayout.show(parentContainer, "customer");
        });

        JButton logBtn = createStyledButton("查看日誌", new Dimension(150, 40),
                new Font("微軟正黑體", Font.PLAIN, 16), new Color(149, 165, 166));
        logBtn.addActionListener(e -> {
            logger.log("Other", "前往日誌");
            parentLayout.show(parentContainer, "log");
        });
        
        bottomPanel.add(backBtn);
        bottomPanel.add(logBtn);
        add(bottomPanel, BorderLayout.SOUTH);
        
        logger.log("Other","菜單載入完成，共載入 " + menu.getDrinkList().size() + " 項飲品");
    }
    
    // 創建樣式化按鈕的輔助方法
    private JButton createStyledButton(String text, Dimension size, Font font, Color bgColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(size);
        button.setFont(font);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        return button;
    }
}