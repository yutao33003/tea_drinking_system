package tea_drinking_system.frame.customerPanel;

import tea_drinking_system.drinkInformation.Menu;
import tea_drinking_system.employee.EmployeeInitialization;
import tea_drinking_system.frame.FontUtils;
import tea_drinking_system.frame.LogOverviewPanel;
import tea_drinking_system.Customer;
import tea_drinking_system.business.DeliveryOrder;
import tea_drinking_system.business.Order;
import tea_drinking_system.business.OrderItem;
import tea_drinking_system.business.TaskCenter;
import tea_drinking_system.drinkInformation.Drink;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPanel extends JPanel {
    private Menu menu;
    private Order order;
    private JPanel menuPanel;
    private JPanel orderSummaryPanel;
    private JLabel totalLabel;
    private CardLayout parentLayout;
    private JPanel parentContainer;
    private LogOverviewPanel logger;
    private Map<String, JComboBox<Integer>> drinkComboBoxes;
    
    // 中文字體
    private static final Font CHINESE_FONT = FontUtils.createChineseFont(Font.PLAIN, 12);
    private static final Font CHINESE_FONT_BOLD = FontUtils.createChineseFont(Font.BOLD, 14);
    private static final Font TITLE_FONT = FontUtils.createChineseFont(Font.BOLD, 18);
    
    public OrderPanel(LogOverviewPanel logger, CardLayout layout, JPanel container, Customer cus, boolean delivery, String address) {
        this.logger = logger;
        this.parentLayout = layout;
        this.parentContainer = container;
        this.menu = new Menu(); // 使用你的Menu類別
        if (delivery) {
        	if(address != null) {
        		this.order = new DeliveryOrder(cus, address);
        	}
        	else {
        		logger.log("Cashier", "沒有地址無法");
        	}
        }
        else {
        	this.order = new Order(cus);
        }
        
        this.drinkComboBoxes = new HashMap<>();
        initializeComponents();
        updateOrderSummary();
    }
    
    public void setParentLayout(CardLayout layout, JPanel container) {
        this.parentLayout = layout;
        this.parentContainer = container;
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        
        // 標題
        JLabel titleLabel = new JLabel("飲品點餐系統", SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(new Color(51, 51, 51));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // 主要內容區域
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        mainPanel.setOpaque(false);
        
        // 左側：選單區域
        JPanel leftPanel = createMenuPanel();
        
        // 右側：訂單摘要區域
        JPanel rightPanel = createOrderSummaryPanel();
        
        mainPanel.add(leftPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // 底部按鈕
        JPanel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(500, 400));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
            "飲品選單", 0, 0, CHINESE_FONT_BOLD, new Color(52, 152, 219)
        ));
        
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        loadMenuItems();
        
        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }
    
    private void loadMenuItems() {
        menuPanel.removeAll();
        
        // 從Menu物件獲取所有飲品
        List<Drink> drinks = menu.getDrinkList();
        
        if (drinks.isEmpty()) {
            JLabel emptyLabel = new JLabel("目前沒有可用的飲品", SwingConstants.CENTER);
            emptyLabel.setFont(CHINESE_FONT);
            emptyLabel.setForeground(Color.GRAY);
            menuPanel.add(emptyLabel);
        } else {
            for (Drink drink : drinks) {
                JPanel drinkPanel = createDrinkItemPanel(drink);
                menuPanel.add(drinkPanel);
                menuPanel.add(Box.createVerticalStrut(8));
            }
        }
        
        menuPanel.revalidate();
        menuPanel.repaint();
    }
    
    private JPanel createDrinkItemPanel(Drink drink) {
        JPanel panel = new JPanel(new BorderLayout(15, 0));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // 左側：飲品資訊
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(drink.getName());
        nameLabel.setFont(CHINESE_FONT_BOLD);
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel priceLabel = new JLabel("$" + drink.getPrice());
        priceLabel.setFont(CHINESE_FONT_BOLD);
        priceLabel.setForeground(new Color(231, 76, 60));
        
        infoPanel.add(nameLabel, BorderLayout.NORTH);
        infoPanel.add(Box.createVerticalStrut(5), BorderLayout.CENTER);
        infoPanel.add(priceLabel, BorderLayout.SOUTH);
        
        // 右側：數量選擇
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        controlPanel.setOpaque(false);
        
        JLabel qtyLabel = new JLabel("數量:");
        qtyLabel.setFont(CHINESE_FONT);
        
        Integer[] quantities = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        JComboBox<Integer> qtyCombo = new JComboBox<>(quantities);
        qtyCombo.setFont(CHINESE_FONT);
        qtyCombo.setPreferredSize(new Dimension(65, 30));
        
        // 存儲ComboBox引用，用於重置
        drinkComboBoxes.put(drink.getName(), qtyCombo);
        
        qtyCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedQty = (Integer) qtyCombo.getSelectedItem();
                updateOrderItem(drink, selectedQty);
            }
        });
        
        controlPanel.add(qtyLabel);
        controlPanel.add(qtyCombo);
        
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(controlPanel, BorderLayout.EAST);
        
        // 添加懸停效果
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(248, 249, 250));
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(52, 152, 219), 2),
                    BorderFactory.createEmptyBorder(14, 14, 14, 14)
                ));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(Color.WHITE);
                panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
                    BorderFactory.createEmptyBorder(15, 15, 15, 15)
                ));
            }
        });
        
        return panel;
    }
    
    private JPanel createOrderSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(300, 400));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(155, 89, 182), 2),
            "訂單摘要", 0, 0, CHINESE_FONT_BOLD, new Color(155, 89, 182)
        ));
        
        orderSummaryPanel = new JPanel();
        orderSummaryPanel.setLayout(new BoxLayout(orderSummaryPanel, BoxLayout.Y_AXIS));
        orderSummaryPanel.setBackground(Color.WHITE);
        orderSummaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JScrollPane scrollPane = new JScrollPane(orderSummaryPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // 總計標籤
        totalLabel = new JLabel("總計: $0", SwingConstants.CENTER);
        totalLabel.setFont(TITLE_FONT);
        totalLabel.setForeground(new Color(231, 76, 60));
        totalLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(224, 224, 224)),
            BorderFactory.createEmptyBorder(15, 0, 10, 0)
        ));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(totalLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));
        
        JButton clearBtn = createStyledButton("清空訂單", new Color(149, 165, 166));
        clearBtn.addActionListener(e -> clearOrder());
        
        JButton confirmBtn = createStyledButton("確認訂單", new Color(46, 204, 113));
        confirmBtn.addActionListener(e -> confirmOrder());
        
        JButton backBtn = createStyledButton("返回主選單", new Color(52, 152, 219));
        backBtn.addActionListener(e -> {
            logger.log("Other", "從店長頁面返回首頁");
            parentLayout.show(parentContainer, "start");
        });
        
        panel.add(clearBtn);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(confirmBtn);
        panel.add(Box.createHorizontalStrut(15));
        panel.add(backBtn);
        
        return panel;
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(CHINESE_FONT_BOLD);
        button.setPreferredSize(new Dimension(130, 40));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(color.darker());
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private void updateOrderItem(Drink drink, int quantity) {
        // 移除現有的訂單項目（如果存在）
    	order.getOrderItems().removeIf(item -> item.getDrink().getName().equals(drink.getName()));
        
        // 如果數量大於0，添加新的訂單項目
        if (quantity > 0) {
        	order.addDrink(drink, quantity);
            if (logger != null) {
            	logger.log("Customer", String.format("選擇 %s x%d (單價$%d)", 
            		    drink.getName(), quantity, drink.getPrice()));
            }
        } else {
            if (logger != null) {
                logger.log("Customer", String.format("移除 %s", drink.getName()));
            }
        }
        
        updateOrderSummary();
    }
    
    private void updateOrderSummary() {
        orderSummaryPanel.removeAll();
        
        if (order.getOrderItems().isEmpty()) {
            JLabel emptyLabel = new JLabel("<html><div style='text-align: center;'>尚未選擇任何飲品<br><br>請在左側選單選擇您要的飲品</div></html>", SwingConstants.CENTER);
            emptyLabel.setFont(CHINESE_FONT);
            emptyLabel.setForeground(Color.GRAY);
            emptyLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
            orderSummaryPanel.add(emptyLabel);
        } else {
            for (OrderItem item : order.getOrderItems()) {
                JPanel itemPanel = createOrderItemPanel(item);
                orderSummaryPanel.add(itemPanel);
                orderSummaryPanel.add(Box.createVerticalStrut(8));
            }
        }
        
        // 更新總計 - 修復：將 double 轉換為 int
        int total = (int) order.getOrderItems().stream().mapToDouble(OrderItem::getSubtotal).sum();
        totalLabel.setText(String.format("總計: $%d", total));
        
        orderSummaryPanel.revalidate();
        orderSummaryPanel.repaint();
    }
    
    private JPanel createOrderItemPanel(OrderItem item) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(224, 224, 224), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(item.getDrink().getName());
        nameLabel.setFont(CHINESE_FONT_BOLD);
        nameLabel.setForeground(new Color(51, 51, 51));
        
        JLabel detailLabel = new JLabel(String.format("$%d × %d", 
            item.getDrink().getPrice(), item.getQuantity()));
        detailLabel.setFont(CHINESE_FONT);
        detailLabel.setForeground(Color.GRAY);
        
        leftPanel.add(nameLabel, BorderLayout.NORTH);
        leftPanel.add(detailLabel, BorderLayout.SOUTH);
        
        // 修復：將 double 轉換為 int
        JLabel priceLabel = new JLabel("$" + (int)item.getSubtotal());
        priceLabel.setFont(CHINESE_FONT_BOLD);
        priceLabel.setForeground(new Color(231, 76, 60));
        priceLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(priceLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void clearOrder() {
    	order.getOrderItems().clear();
        updateOrderSummary();
        
        // 重置所有下拉選單
        for (JComboBox<Integer> combo : drinkComboBoxes.values()) {
            combo.setSelectedIndex(0);
        }
        
        if (logger != null) {
            logger.log("Customer", "清空所有訂單");
        }
    }
    
    private void confirmOrder() {
        if (order.getOrderItems().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "請選擇至少一項飲品！", "提示", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int total = (int) order.getOrderItems().stream().mapToDouble(OrderItem::getSubtotal).sum();
        
        StringBuilder orderDetails = new StringBuilder();
        orderDetails.append("=== 訂單確認 ===\n\n");
        for (OrderItem item : order.getOrderItems()) {
            orderDetails.append(String.format("%-10s × %-2d = $%-3d\n", 
                item.getDrink().getName(), 
                item.getQuantity(), 
                (int)item.getSubtotal()));
        }
        orderDetails.append("\n").append("=".repeat(25));
        orderDetails.append(String.format("\n總計金額: $%d", total));
        
        int result = JOptionPane.showConfirmDialog(this, 
            orderDetails.toString(), 
            "確認訂單", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (result == JOptionPane.YES_OPTION) {
            try {
                TaskCenter.inventoryQueue.put(order); // ✅ 將訂單交給任務中心處理
                if (logger != null) {
                    logger.log("Customer", String.format("送出訂單待確認，總金額: $%d", total));
                }

                JOptionPane.showMessageDialog(this,
                    "訂單已送出確認庫存！\n請稍等...", 
                    "送出成功", 
                    JOptionPane.INFORMATION_MESSAGE);
                clearOrder();
                
            } catch (InterruptedException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "系統錯誤，請稍後再試。", 
                    "錯誤", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // 提供方法讓外部可以更新選單（當Menu有變更時）
    public void refreshMenu() {
        loadMenuItems();
        clearOrder();
    }
}