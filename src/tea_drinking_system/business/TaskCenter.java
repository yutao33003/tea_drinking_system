package tea_drinking_system.business;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import tea_drinking_system.employee.Cashier;

public class TaskCenter {
	public static final BlockingQueue<Order> inventoryQueue= new LinkedBlockingQueue<>();
    public static final BlockingQueue<Order> taskQueue = new LinkedBlockingQueue<>();
    public static final BlockingQueue<Order> completedQueue = new LinkedBlockingQueue<>();
    public static final BlockingQueue<DeliveryOrder> deliveryQueue = new LinkedBlockingQueue<>();
    
    // 記錄訂單與負責收銀員的對應關係
    public static final Map<Order, Cashier> orderCashierMap = new ConcurrentHashMap<>();
}
