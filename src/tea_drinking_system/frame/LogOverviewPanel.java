package tea_drinking_system.frame;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.nio.charset.StandardCharsets;

public class LogOverviewPanel extends JPanel {
    private Map<String, JTextArea> logAreas;
    private Map<String, Color> roleColors;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    private CardLayout parentLayout;
    private JPanel parentContainer;
    private Stack<String> pageHistory;
    private JTextField commandInput;
    
    // 中文字體常量
    private static final Font CHINESE_FONT = new Font("微軟正黑體", Font.PLAIN, 12);
    private static final Font CHINESE_FONT_BOLD = new Font("微軟正黑體", Font.BOLD, 14);
    private static final Font CONSOLE_FONT = getSupportedConsoleFont();
    
    public LogOverviewPanel() {
        setupRoleColors();
        initializeComponents();
        pageHistory = new Stack<>();
    }
    
    /**
     * 獲取支持中文的等寬字體
     */
    private static Font getSupportedConsoleFont() {
        String[] consoleFonts = {
            "Consolas", "Microsoft YaHei Mono", "SimHei", 
            "Microsoft YaHei", "SimSun", "Monospaced"
        };
        
        for (String fontName : consoleFonts) {
            Font font = new Font(fontName, Font.PLAIN, 12);
            if (font.canDisplayUpTo("中文測試123") == -1) {
                return font;
            }
        }
        return new Font("Monospaced", Font.PLAIN, 12);
    }
    
    public void setParentLayout(CardLayout layout, JPanel container) {
        this.parentLayout = layout;
        this.parentContainer = container;
    }
    
    public void setSourcePage(String sourcePage) {
        if (!pageHistory.isEmpty() && pageHistory.peek().equals(sourcePage)) {
            return;
        }
        pageHistory.push(sourcePage);
    }
    
    private void goBack() {
        if (parentLayout != null && parentContainer != null && !pageHistory.isEmpty()) {
            String previousPage = pageHistory.pop();
            log("Other", "從日誌頁面返回到: " + previousPage);
            parentLayout.show(parentContainer, previousPage);
        }
    }
    
    private void initializeComponents() {
        setLayout(new BorderLayout());
        
        JPanel logDisplayPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        logAreas = new HashMap<>();
        
        String[] roles = {"Customer", "Maker", "Cashier", "Employer", "Delivery", "Other"};
        
        for (String role : roles) {
            JPanel panel = createLogPanel(role);
            logDisplayPanel.add(panel);
        }
        
        JPanel controlPanel = createControlPanel();
        
        add(logDisplayPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        controlPanel.setOpaque(false);
        
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        
        JLabel inputLabel = new JLabel("輸入指令: ");
        inputLabel.setFont(CHINESE_FONT_BOLD);
        
        commandInput = new JTextField();
        commandInput.setFont(CONSOLE_FONT);
        commandInput.addActionListener(e -> processCommand());
        
        JButton sendBtn = new JButton("執行");
        sendBtn.setFont(CHINESE_FONT);
        sendBtn.addActionListener(e -> processCommand());
        
        inputPanel.add(inputLabel, BorderLayout.WEST);
        inputPanel.add(commandInput, BorderLayout.CENTER);
        inputPanel.add(sendBtn, BorderLayout.EAST);
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setOpaque(false);
        
        JButton backBtn = new JButton("返回上一頁");
        backBtn.setFont(CHINESE_FONT_BOLD);
        backBtn.addActionListener(e -> goBack());
        
        JButton clearBtn = new JButton("清除所有日誌");
        clearBtn.setFont(CHINESE_FONT_BOLD);
        clearBtn.addActionListener(e -> clearAllLogs());
        
        buttonPanel.add(backBtn);
        buttonPanel.add(clearBtn);
        
        controlPanel.add(inputPanel, BorderLayout.CENTER);
        controlPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        return controlPanel;
    }
    
    private void processCommand() {
        String command = commandInput.getText().trim();
        if (!command.isEmpty()) {
            String role = "Other";
            String message = command;
            
            if (command.contains(":")) {
                String[] parts = command.split(":", 2);
                String inputRole = parts[0].trim();
                if (logAreas.containsKey(inputRole)) {
                    role = inputRole;
                    message = parts[1].trim();
                }
            }
            
            log(role, message);
            commandInput.setText("");
        }
    }
    
    private JPanel createLogPanel(String role) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(CONSOLE_FONT); // 使用支持中文的字體
        area.setBackground(new Color(250, 250, 250));
        
        // 確保JTextArea能正確處理UTF-8編碼
        area.putClientProperty("charset", "UTF-8");
        
        JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(getRoleColor(role), 2), 
            role, 
            0, 
            0, 
            CHINESE_FONT_BOLD, // 使用中文字體
            getRoleColor(role)
        ));
        
        panel.add(scroll, BorderLayout.CENTER);
        panel.setOpaque(false);
        
        logAreas.put(role, area);
        return panel;
    }
    
    private void setupRoleColors() {
        roleColors = new HashMap<>();
        roleColors.put("Customer", new Color(52, 152, 219));
        roleColors.put("Maker", new Color(46, 204, 113));
        roleColors.put("Cashier", new Color(241, 196, 15));
        roleColors.put("Employer", new Color(231, 76, 60));
        roleColors.put("Delivery", new Color(155, 89, 182));
        roleColors.put("Other", new Color(149, 165, 166));
    }
    
    private Color getRoleColor(String role) {
        return roleColors.getOrDefault(role, Color.BLACK);
    }
    
    public void log(String role, String message) {
        SwingUtilities.invokeLater(() -> {
            JTextArea area = logAreas.get(role);
            if (area != null) {
                String timestamp = LocalDateTime.now().format(TIME_FORMATTER);
                String logEntry = String.format("[%s] %s%n", timestamp, message);
                
                // 確保字符串以UTF-8編碼處理
                try {
                    // 驗證字符串是否包含正確的中文字符
                    byte[] bytes = logEntry.getBytes(StandardCharsets.UTF_8);
                    String validatedEntry = new String(bytes, StandardCharsets.UTF_8);
                    
                    area.append(validatedEntry);
                    area.setCaretPosition(area.getDocument().getLength());
                    
                    // 強制重繪以確保中文正確顯示
                    area.repaint();
                } catch (Exception e) {
                    // 如果編碼有問題，至少顯示基本信息
                    area.append(String.format("[%s] [編碼錯誤] %s%n", timestamp, 
                        message.replaceAll("[^\\x00-\\x7F]", "?")));
                }
            }
        });
    }
    
    public void clearLog(String role) {
        SwingUtilities.invokeLater(() -> {
            JTextArea area = logAreas.get(role);
            if (area != null) {
                area.setText("");
            }
        });
    }
    
    public void clearAllLogs() {
        SwingUtilities.invokeLater(() -> {
            logAreas.values().forEach(area -> area.setText(""));
            log("Other", "所有日誌已清除");
        });
    }
}
