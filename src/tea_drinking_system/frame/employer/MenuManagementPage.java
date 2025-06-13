package tea_drinking_system.frame.employer;

import javax.swing.*;

import java.util.ArrayList;
import java.util.List;
import tea_drinking_system.drinkInformation.Ingredient;
import tea_drinking_system.drinkInformation.Drink;
import tea_drinking_system.drinkInformation.DrinkCenter;
import tea_drinking_system.employee.EmployeeInitialization;
import tea_drinking_system.frame.LogOverviewPanel;
import java.awt.*;

public class MenuManagementPage extends JPanel {
    private LogOverviewPanel logPanel;
    private CardLayout parentLayout;
    private JPanel parentContainer;
    private DefaultListModel<String> menuModel;
    private JList<String> menuList;
    
    public MenuManagementPage(LogOverviewPanel logPanel, CardLayout layout, JPanel container) {
        this.logPanel = logPanel;
        this.parentLayout = layout;
        this.parentContainer = container;    
        initializeComponents();
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        setOpaque(false);
        
        // 標題
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("菜單管理");
        title.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        titlePanel.add(title);
        
        // 主要內容區域
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        menuModel = new DefaultListModel<>();
        
        for (Drink drink : DrinkCenter.menu.getDrinkList()) {
            menuModel.addElement(drink.getName() + " - $" + drink.getPrice());
        }
        
        menuList = new JList<>(menuModel);
        menuList.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(menuList);
        
        // 操作按鈕區域
        JPanel operationPanel = new JPanel(new GridLayout(4, 1, 5, 5));
        operationPanel.setOpaque(false);
        
        JButton addBtn = new JButton("新增品項");
        JButton editBtn = new JButton("編輯品項");
        JButton deleteBtn = new JButton("刪除品項");
        JButton saveBtn = new JButton("儲存菜單");
        
        addBtn.addActionListener(e -> addMenuItem());
        editBtn.addActionListener(e -> editMenuItem());
        deleteBtn.addActionListener(e -> deleteMenuItem());
        saveBtn.addActionListener(e -> saveMenu());
        
        operationPanel.add(addBtn);
        operationPanel.add(editBtn);
        operationPanel.add(deleteBtn);
        operationPanel.add(saveBtn);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(operationPanel, BorderLayout.EAST);
        
        // 控制按鈕區域
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.setOpaque(false);
        
        JButton backBtn = new JButton("返回店長頁面");
        backBtn.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        backBtn.addActionListener(e -> {
            logPanel.log("Employer", "從菜單管理返回店長頁面");
            parentLayout.show(parentContainer, "employer");
        });
        
        JButton logBtn = new JButton("查看日誌");
        logBtn.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        logBtn.addActionListener(e -> {
            logPanel.setSourcePage("menuManagement"); // 記錄來源頁面
            logPanel.log("Other", "從菜單管理查看日誌");
            parentLayout.show(parentContainer, "log");
        });
        
        controlPanel.add(backBtn);
        controlPanel.add(logBtn);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private void addMenuItem() {
        String input = JOptionPane.showInputDialog(this, "請輸入新品項\n格式: 品名 - $價格 - 原料:數量,...\n例: 珍珠奶茶 - $50 - 珍珠:1,奶茶:2");

        if (input != null && !input.trim().isEmpty()) {
            try {
                String[] parts = input.split("-");
                if (parts.length != 3) throw new IllegalArgumentException();

                String name = parts[0].trim();
                String priceStr = parts[1].trim().replace("$", "");
                int price = Integer.parseInt(priceStr);

                String[] ingredientEntries = parts[2].trim().split(",");
                List<Ingredient> ingredients = new ArrayList<>();

                for (String entry : ingredientEntries) {
                    String[] ingParts = entry.trim().split(":");
                    if (ingParts.length != 2) throw new IllegalArgumentException("原料格式錯誤");

                    String ingName = ingParts[0].trim();
                    int quantity = Integer.parseInt(ingParts[1].trim());

                    ingredients.add(new Ingredient(ingName, quantity));
                }

                // 新增飲料
                EmployeeInitialization.employer.addDrinkToMenu(
                    DrinkCenter.menu,
                    DrinkCenter.formula,
                    name,
                    price,
                    ingredients
                );

                // 加入清單模型
                menuModel.addElement(name + " - $" + price);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "輸入格式錯誤，請使用：品名 - $價格 - 原料:數量,...\n例：珍珠奶茶 - $50 - 珍珠:1,奶茶:2");
            }
        }
    }

    private void editMenuItem() {
        int selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex != -1) {
            String currentItem = menuModel.getElementAt(selectedIndex);
            
            // 嘗試解析名稱與價格
            try {
                String[] parts = currentItem.split("-\\s*\\$");
                if (parts.length != 2) throw new IllegalArgumentException("格式錯誤");

                String name = parts[0].trim();
                String currentPrice = parts[1].trim();

                String newPriceStr = JOptionPane.showInputDialog(this, "修改「" + name + "」的價格:", currentPrice);
                if (newPriceStr != null && !newPriceStr.trim().isEmpty()) {
                    int newPrice = Integer.parseInt(newPriceStr.trim());
                    String updatedItem = name + " - $" + newPrice;
                    menuModel.setElementAt(updatedItem, selectedIndex);
                    EmployeeInitialization.employer.editDrinkPrice(DrinkCenter.menu, name, newPrice);
                }

            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "選取項目格式錯誤，無法編輯價格");
            }

        } else {
            JOptionPane.showMessageDialog(this, "請先選擇要編輯價格的品項");
        }
    }

    
    private void deleteMenuItem() {
        int selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex != -1) {
            String itemToDelete = menuModel.getElementAt(selectedIndex);
            int confirm = JOptionPane.showConfirmDialog(this, "確定要刪除 \"" + itemToDelete + "\" 嗎?");
            if (confirm == JOptionPane.YES_OPTION) {
            	
                menuModel.removeElementAt(selectedIndex);
                logPanel.log("Employer", "刪除菜單品項: " + itemToDelete);
            }
        } else {
            JOptionPane.showMessageDialog(this, "請先選擇要刪除的品項");
        }
    }
    
    private void saveMenu() {
        logPanel.log("Employer", "儲存菜單，共 " + menuModel.getSize() + " 個品項");
        JOptionPane.showMessageDialog(this, "菜單已儲存！");
    }
}
