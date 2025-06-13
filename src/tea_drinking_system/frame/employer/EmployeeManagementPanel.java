package tea_drinking_system.frame.employer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import tea_drinking_system.employee.Employee;
import tea_drinking_system.employee.EmployeeInitialization;
import tea_drinking_system.employee.Cashier;
import tea_drinking_system.frame.LogOverviewPanel;

public class EmployeeManagementPanel extends JPanel {
    private LogOverviewPanel logPanel;
    private CardLayout parentLayout;
    private JPanel parentContainer;
    private JTextArea employeeListArea;
    
    public EmployeeManagementPanel(LogOverviewPanel logPanel, CardLayout parentLayout, JPanel parentContainer) {
        this.logPanel = logPanel;
        this.parentLayout = parentLayout;
        this.parentContainer = parentContainer;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // 初始載入員工列表
        updateEmployeeList();
    }
    
    private void initializeComponents() {
        setOpaque(false);
        setLayout(new BorderLayout());
        
        // 員工列表顯示區域
        employeeListArea = new JTextArea();
        employeeListArea.setEditable(false);
        employeeListArea.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        employeeListArea.setBackground(new Color(248, 249, 250));
        employeeListArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }
    
    private void setupLayout() {
        // 標題面板
        JPanel titlePanel = createTitlePanel();
        
        // 主要內容面板
        JPanel mainPanel = createMainPanel();
        
        // 底部按鈕面板
        JPanel bottomPanel = createBottomPanel();
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        
        JLabel title = new JLabel("員工管理系統");
        title.setFont(new Font("微軟正黑體", Font.BOLD, 24));
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setForeground(new Color(44, 62, 80));
        
        titlePanel.add(title);
        return titlePanel;
    }
    
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 操作按鈕面板
        JPanel buttonPanel = createButtonPanel();
        
        // 員工列表顯示面板
        JPanel listPanel = createListPanel();
        
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(listPanel, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);
        
        JButton viewAllBtn = createStyledButton("查看所有員工", new Color(52, 152, 219));
        JButton hireBtn = createStyledButton("僱用新員工", new Color(46, 204, 113));
        JButton fireBtn = createStyledButton("解僱員工", new Color(231, 76, 60));
        JButton refreshBtn = createStyledButton("重新整理", new Color(155, 89, 182));
        
        buttonPanel.add(viewAllBtn);
        buttonPanel.add(hireBtn);
        buttonPanel.add(fireBtn);
        buttonPanel.add(refreshBtn);
        
        return buttonPanel;
    }
    
    private JPanel createListPanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(employeeListArea);
        scrollPane.setPreferredSize(new Dimension(700, 350));
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(52, 152, 219), 2), 
            "員工列表",
            0, 0, 
            new Font("微軟正黑體", Font.BOLD, 16),
            new Color(52, 152, 219)
        ));
        
        listPanel.add(scrollPane, BorderLayout.CENTER);
        
        return listPanel;
    }
    
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout());
        bottomPanel.setOpaque(false);
        
        JButton backBtn = createStyledButton("返回店長頁面", new Color(149, 165, 166));
        JButton logBtn = createStyledButton("查看日誌", new Color(149, 165, 166));
        
        bottomPanel.add(backBtn);
        bottomPanel.add(logBtn);
        
        return bottomPanel;
    }
    
    private void setupEventHandlers() {
        // 查看所有員工按鈕
        Component[] components = ((JPanel)((JPanel)getComponent(1)).getComponent(0)).getComponents();
        JButton viewAllBtn = (JButton) components[0];
        JButton hireBtn = (JButton) components[1];
        JButton fireBtn = (JButton) components[2];
        JButton refreshBtn = (JButton) components[3];
        
        viewAllBtn.addActionListener(e -> {
            logPanel.log("Employer", "查看所有員工");
            updateEmployeeList();
        });
        
        hireBtn.addActionListener(e -> {
            logPanel.log("Employer", "開始僱用新員工");
            showHireEmployeeDialog();
        });
        
        fireBtn.addActionListener(e -> {
            logPanel.log("Employer", "開始解僱員工");
            showFireEmployeeDialog();
        });
        
        refreshBtn.addActionListener(e -> {
            logPanel.log("Employer", "重新整理員工列表");
            updateEmployeeList();
        });
        
        // 底部按鈕
        Component[] bottomComponents = ((JPanel)getComponent(2)).getComponents();
        JButton backBtn = (JButton) bottomComponents[0];
        JButton logBtn = (JButton) bottomComponents[1];
        
        backBtn.addActionListener(e -> {
            logPanel.log("Other", "從員工管理頁面返回店長頁面");
            parentLayout.show(parentContainer, "employer");
        });
        
        logBtn.addActionListener(e -> {
            logPanel.setSourcePage("employeeManagement");
            logPanel.log("Other", "前往日誌");
            parentLayout.show(parentContainer, "log");
        });
    }
    
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("微軟正黑體", Font.PLAIN, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        
        // 添加懸停效果
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
    
    // 更新員工列表顯示
    public void updateEmployeeList() {
        List<Employee> employees = EmployeeInitialization.employer.getAllEmployees();
        
        StringBuilder sb = new StringBuilder();
        sb.append("=== 員工管理系統 ===\n\n");
        
        if (employees.isEmpty()) {
            sb.append("📋 目前沒有任何員工\n");
            sb.append("💡 請點擊「僱用新員工」來添加員工\n");
        } else {
            sb.append(String.format("👥 共有 %d 名員工：\n\n", employees.size()));
            
            int index = 1;
            for (Employee emp : employees) {
                sb.append(String.format("【員工 %d】\n", index++));
                sb.append(String.format("🆔 員工ID: %s\n", emp.getStaffID()));
                sb.append(String.format("👤 姓名: %s\n", emp.getName()));
                sb.append(String.format("💼 職位: %s\n", emp.getPosition()));
                sb.append(String.format("💰 薪資: %,d 元\n", emp.getSalary()));
                
                if (emp instanceof Cashier) {
                    Cashier cashier = (Cashier) emp;
                    String roleStr = cashier.getRole() == Cashier.CashierRole.INVENTORY ? "庫存管理" : "取餐通知";
                    sb.append(String.format("🏷️ 角色: %s\n", roleStr));
                }
                
                sb.append("────────────────────────\n\n");
            }
            
            // 統計資訊
            sb.append("=== 統計資訊 ===\n");
            long cashierCount = employees.stream().filter(emp -> "收銀員".equals(emp.getPosition())).count();
            long makerCount = employees.stream().filter(emp -> "製作員".equals(emp.getPosition())).count();
            long deliveryCount = employees.stream().filter(emp -> "外送員".equals(emp.getPosition())).count();
            
            sb.append(String.format("收銀員: %d 名 | 製作員: %d 名 | 外送員: %d 名\n", cashierCount, makerCount, deliveryCount));
            
            int totalSalary = employees.stream().mapToInt(Employee::getSalary).sum();
            sb.append(String.format("總薪資支出: %,d 元/月\n", totalSalary));
        }
        
        employeeListArea.setText(sb.toString());
        employeeListArea.setCaretPosition(0); // 滾動到頂部
    }
    
    // 僱用員工對話框
    private void showHireEmployeeDialog() {
        logPanel.log("Employer", "開啟僱用員工對話框");

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JTextField nameField = new JTextField(15);
        JComboBox<String> positionCombo = new JComboBox<>(new String[] { "收銀員", "製作員", "外送員" });
        JComboBox<String> roleCombo = new JComboBox<>(new String[] { "INVENTORY", "READY" });

        // 設定角色選擇框的顯示文字
        roleCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if ("INVENTORY".equals(value)) {
                    setText("庫存管理");
                } else if ("READY".equals(value)) {
                    setText("取餐通知");
                }
                return this;
            }
        });

        // 排列組件
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("員工姓名:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("職位:"), gbc);
        gbc.gridx = 1;
        panel.add(positionCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("收銀員角色:"), gbc);
        gbc.gridx = 1;
        panel.add(roleCombo, gbc);

        // 根據職位選擇決定是否啟用角色選擇
        positionCombo.addActionListener(e -> {
            boolean isCashier = "收銀員".equals(positionCombo.getSelectedItem());
            roleCombo.setEnabled(isCashier);
        });

        // 初始設定
        roleCombo.setEnabled("收銀員".equals(positionCombo.getSelectedItem()));

        int result = JOptionPane.showConfirmDialog(this, panel, "僱用新員工", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            String position = (String) positionCombo.getSelectedItem();

            if (name.isEmpty()) {
                logPanel.log("Employer", "僱用失敗: 員工姓名不能為空");
                JOptionPane.showMessageDialog(this, "請輸入員工姓名！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 檢查是否已存在同名員工
            if (EmployeeInitialization.employer.findEmployeeByName(name) != null) {
                logPanel.log("Employer", "僱用失敗: 員工 " + name + " 已存在");
                JOptionPane.showMessageDialog(this, "員工 " + name + " 已存在！", "錯誤", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Employee newEmployee;
            if ("收銀員".equals(position)) {
                String roleStr = (String) roleCombo.getSelectedItem();
                Cashier.CashierRole role = "INVENTORY".equals(roleStr) ? Cashier.CashierRole.INVENTORY
                        : Cashier.CashierRole.READY;
                newEmployee = EmployeeInitialization.employer.hireEmployee(name, position, role);
            } else {
                newEmployee = EmployeeInitialization.employer.hireEmployee(name, position);
            }

            if (newEmployee != null) {
                String successMessage = "成功僱用員工：" + name + "\nID：" + newEmployee.getStaffID();
                logPanel.log("Employer", "僱用成功 - " + name + " (ID: " + newEmployee.getStaffID() + ")");
                JOptionPane.showMessageDialog(this, successMessage, "僱用成功", JOptionPane.INFORMATION_MESSAGE);
                updateEmployeeList(); // 更新顯示
            } else {
                logPanel.log("Employer", "僱用失敗 - 無法創建員工: " + name);
                JOptionPane.showMessageDialog(this, "僱用失敗！", "錯誤", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // 解僱員工對話框
    private void showFireEmployeeDialog() {
        logPanel.log("Employer", "開啟解僱員工對話框");

        List<Employee> employees = EmployeeInitialization.employer.getAllEmployees();

        if (employees.isEmpty()) {
            logPanel.log("Employer", "解僱操作失敗: 目前沒有任何員工");
            JOptionPane.showMessageDialog(this, "目前沒有任何員工可以解僱！", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // 創建員工選擇列表
        String[] employeeOptions = employees.stream().map(emp -> {
            String roleInfo = "";
            if (emp instanceof Cashier) {
                Cashier cashier = (Cashier) emp;
                String roleStr = cashier.getRole() == Cashier.CashierRole.INVENTORY ? "庫存管理" : "取餐通知";
                roleInfo = " - " + roleStr;
            }
            return emp.getStaffID() + " - " + emp.getName() + " (" + emp.getPosition() + roleInfo + ")";
        }).toArray(String[]::new);

        String selected = (String) JOptionPane.showInputDialog(this, "請選擇要解僱的員工：", "解僱員工",
                JOptionPane.QUESTION_MESSAGE, null, employeeOptions, employeeOptions[0]);

        if (selected != null) {
            String staffID = selected.split(" - ")[0];
            Employee selectedEmployee = EmployeeInitialization.employer.findEmployeeById(staffID);

            if (selectedEmployee != null) {
                String confirmMessage = String.format(
                    "確定要解僱以下員工嗎？\n\n" +
                    "員工資訊：\n" +
                    "姓名：%s\n" +
                    "ID：%s\n" +
                    "職位：%s\n" +
                    "薪資：%,d 元\n\n" +
                    "⚠️ 此操作無法復原！",
                    selectedEmployee.getName(),
                    selectedEmployee.getStaffID(),
                    selectedEmployee.getPosition(),
                    selectedEmployee.getSalary()
                );

                int confirm = JOptionPane.showConfirmDialog(this, confirmMessage, "確認解僱", 
                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = EmployeeInitialization.employer.fireEmployee(staffID);
                    if (success) {
                        logPanel.log("Employer", "解僱成功: " + selectedEmployee.getName() + " 已被解僱");
                        JOptionPane.showMessageDialog(this, "員工已成功解僱！", "解僱成功", JOptionPane.INFORMATION_MESSAGE);
                        updateEmployeeList(); // 更新顯示
                    } else {
                        logPanel.log("Employer", "解僱失敗: 無法解僱員工 " + selectedEmployee.getName());
                        JOptionPane.showMessageDialog(this, "解僱失敗！", "錯誤", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }
    
    // 提供公開方法供外部調用
    public void refreshEmployeeList() {
        updateEmployeeList();
    }
    
    // 取得當前員工數量
    public int getEmployeeCount() {
        return EmployeeInitialization.employer.getAllEmployees().size();
    }
}
