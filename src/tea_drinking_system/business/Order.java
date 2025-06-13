package tea_drinking_system.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import tea_drinking_system.Customer;
import tea_drinking_system.drinkInformation.Drink;

public class Order {
    private static final AtomicInteger counter = new AtomicInteger(1);
    private String orderID;
    private Customer customer;
    private int totalPrice;
    private String orderStatus = "新訂單";
    private boolean isDelivery = false;
    private Date createdAt = new Date();
    private List<OrderItem> items = new ArrayList<>();

    public Order(Customer customer) {
        this.customer = customer;
        this.orderID = "OR" + String.format("%03d", counter.getAndIncrement());
    }

    public void updateStatus(String status) {
        orderStatus = status;
    }

    public String getStatus() {
        return orderStatus;
    }

    public void addDrink(Drink drink, int quantity) {
        items.add(new OrderItem(drink, quantity));
    }
    
    public void totalPrice(int totalPrice) {
    	this.totalPrice = totalPrice;
    }

    public int getTotalPrice() {
        int total = 0;
        for (OrderItem item : items) {
            total += item.getSubtotal();
        }
        return total;
    }

    public String getOrderID() {
        return orderID;
    }

    public void markAsDelivery() {
        this.isDelivery = true;
    }

    public boolean isDelivery() {
        return isDelivery;
    }
    
    public void setItem(List<OrderItem> items) {
    	this.items = items;
    }

    public List<OrderItem> getOrderItems() {
        return items;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : items) {
            sb.append(item.toString()).append("\n");
        }
        sb.append(String.format("總計: $%d", getTotalPrice()));
        return sb.toString();
    }

    public void printOrder() {
        System.out.println("訂單編號: " + orderID);
        System.out.println("顧客名稱: " + customer.getName());
        System.out.println("建立時間: " + createdAt);
        System.out.println("是否為外送: " + isDelivery);
        System.out.println("訂單內容:");
        for (OrderItem item : items) {
            System.out.println("- " + item);
        }
        System.out.println("總金額: $" + getTotalPrice());
    }
}
