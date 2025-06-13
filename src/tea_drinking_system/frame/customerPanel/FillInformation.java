package tea_drinking_system.frame.customerPanel;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import tea_drinking_system.Customer;
import tea_drinking_system.frame.LogOverviewPanel;
import tea_drinking_system.frame.FontUtils;

public class FillInformation extends JPanel {
    private JTextField nameField;
    private JTextField phoneField;
    private JTextField addressField;
    private JCheckBox deliveryCheck;
    private JComboBox<String> paymentCombo;
    private CardLayout parentLayout;
    private JPanel parentContainer;
    private LogOverviewPanel logger;

    // 字體
    private static final Font CHINESE_FONT = FontUtils.createChineseFont(Font.PLAIN, 14);
    private static final Font CHINESE_FONT_BOLD = FontUtils.createChineseFont(Font.BOLD, 16);

    public FillInformation(LogOverviewPanel logger, CardLayout layout, JPanel container) {
        this.logger = logger;
        this.parentLayout = layout;
        this.parentContainer = container;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("填寫顧客資訊", SwingConstants.CENTER);
        titleLabel.setFont(CHINESE_FONT_BOLD);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
        formPanel.setOpaque(false);

        // 姓名
        JLabel nameLabel = new JLabel("姓名：");
        nameLabel.setFont(CHINESE_FONT);
        formPanel.add(nameLabel);
        nameField = new JTextField();
        formPanel.add(nameField);

        // 電話
        JLabel phoneLabel = new JLabel("電話：");
        phoneLabel.setFont(CHINESE_FONT);
        formPanel.add(phoneLabel);
        phoneField = new JTextField();
        formPanel.add(phoneField);

        // 付款方式
        JLabel paymentLabel = new JLabel("付款方式：");
        paymentLabel.setFont(CHINESE_FONT);
        formPanel.add(paymentLabel);
        paymentCombo = new JComboBox<>(new String[]{"現金", "信用卡", "行動支付"});
        formPanel.add(paymentCombo);

        // 是否外送
        JLabel deliveryLabel = new JLabel("是否外送：");
        deliveryLabel.setFont(CHINESE_FONT);
        formPanel.add(deliveryLabel);
        deliveryCheck = new JCheckBox();
        deliveryCheck.setOpaque(false);
        formPanel.add(deliveryCheck);

        // 地址欄位
        JLabel addressLabel = new JLabel("地址（若為外送需填）：");
        addressLabel.setFont(CHINESE_FONT);
        formPanel.add(addressLabel);
        addressField = new JTextField();
        formPanel.add(addressField);

        add(formPanel, BorderLayout.CENTER);

        // 勾選外送時控制地址欄位是否可用
        deliveryCheck.addActionListener(e -> addressField.setEnabled(deliveryCheck.isSelected()));

        // 預設為不可編輯，除非勾選外送
        addressField.setEnabled(false);

        // 下一步按鈕
        JButton nextButton = new JButton("前往點餐");
        nextButton.setFont(CHINESE_FONT);
        nextButton.addActionListener(e -> proceedToOrder());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(nextButton);
        buttonPanel.setOpaque(false);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void proceedToOrder() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String payment = (String) paymentCombo.getSelectedItem();
        boolean isDelivery = deliveryCheck.isSelected();
        String address = addressField.getText().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請完整填寫姓名與電話", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (isDelivery && address.isEmpty()) {
            JOptionPane.showMessageDialog(this, "請填寫地址以進行外送", "錯誤", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer customer = new Customer(name, phone, payment);
        logger.log("Customer", "填寫資訊完成：姓名=" + name + "，電話=" + phone + "，付款方式=" + payment +
                "，外送=" + (isDelivery ? "是" : "否") + (isDelivery ? "，地址=" + address : ""));

        OrderPanel orderPanel = new OrderPanel(logger, parentLayout, parentContainer, customer, isDelivery, address);
        parentContainer.add(orderPanel, "OrderPanel");
        parentLayout.show(parentContainer, "OrderPanel");
    }
}

  

