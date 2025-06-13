// DrinkFrame.java - Main application frame
package tea_drinking_system.frame;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.*;

import tea_drinking_system.frame.employer.EmployeeManagementPanel;
import tea_drinking_system.frame.employer.MenuManagementPage;
import tea_drinking_system.frame.customerPanel.FillInformation;
import tea_drinking_system.frame.customerPanel.MenuPage;

public class DrinkFrame extends JFrame {
    public static LogOverviewPanel logPanel;
    private CardLayout cardLayout;
    private JPanel container;
    
    public DrinkFrame() {
        initializeFrame();
        setupComponents();
        setVisible(true);
    }
    
    private void initializeFrame() {
        setTitle("茶飲系統");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // 視窗置中
        setLayout(new BorderLayout());
    }
    
    private void setupComponents() {
        // 設定背景panel
        BackgroundPanel bgPanel = new BackgroundPanel("/image/background.jpg");
        
        // 設定卡片布局容器
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);
        
        // 初始化日誌面板
        logPanel = new LogOverviewPanel();
        logPanel.setParentLayout(cardLayout, container); // 設定父容器引用
        
        // 創建頁面工廠實例以便傳遞日誌引用
        PageFactory pageFactory = new PageFactory(logPanel);
        
        // 添加各個頁面
        container.add(pageFactory.createStartPage(cardLayout, container), "start");
        container.add(pageFactory.createCustomerPage(cardLayout, container), "customer");
        container.add(pageFactory.createEmployerPage(cardLayout, container), "employer");
        container.add(logPanel, "log");
        
        // 添加子頁面
        container.add(new FillInformation(logPanel, cardLayout, container), "order");
        container.add(new MenuManagementPage(logPanel, cardLayout, container), "menuManagement");
        container.add(new EmployeeManagementPanel(logPanel, cardLayout, container),"employeeManagement");
        container.add(new MenuPage(logPanel, cardLayout, container),"menuPage");
        
        bgPanel.add(container);
        add(bgPanel);
    }
    
    public void showPage(String pageName) {
        cardLayout.show(container, pageName);
    }
    
    public LogOverviewPanel getLogPanel() {
        return logPanel;
    }
}