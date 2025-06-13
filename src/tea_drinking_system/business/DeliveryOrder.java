package tea_drinking_system.business;

import java.util.concurrent.atomic.AtomicInteger;

import tea_drinking_system.Customer;

public class DeliveryOrder extends Order{
	private static final AtomicInteger counter = new AtomicInteger(1); // 執行序安全整數計算器
	private String deliveryID;
	private Order order;
	private String address;
	
	public DeliveryOrder(Customer customer, String address) {
        super(customer);
        this.address = address;
        this.deliveryID = "DO" + String.format("%03d", counter.getAndIncrement());
        markAsDelivery();
    }
	
	public String getDeliveryID() {
        return deliveryID;
    }
	
	public Order getOrder() {
        return order;
    }
	
	public String getAddress() {
        return address;
    }
	
	public void scheduleDelivery() {
        // 安排配送邏輯（例如安排時間、通知外送員等等）
        System.out.println("配送訂單 " + deliveryID + " 已排程至地址：" + address);
    }
}
