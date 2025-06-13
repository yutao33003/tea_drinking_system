// Delivery.java - 改進版外送員
package tea_drinking_system.employee;

import java.util.concurrent.atomic.AtomicInteger;
import tea_drinking_system.business.DeliveryOrder;
import tea_drinking_system.business.Order;
import tea_drinking_system.business.TaskCenter;
import tea_drinking_system.frame.LogOverviewPanel;

public class Delivery extends Employee {
    private static final AtomicInteger deliveryCount = new AtomicInteger(1);
    private volatile boolean isWorking = true;
    private Thread workerThread;

    public Delivery(String name, LogOverviewPanel logger) {
        super(name, "外送員", 30000, logger);
        this.staffID = "DE" + String.format("%03d", deliveryCount.getAndIncrement());
    }

    public synchronized void startWorking() {
        if (workerThread != null && workerThread.isAlive()) {
            return; // 避免重複啟動
        }
        
        isWorking = true;
        workerThread = new Thread(() -> {
            while (isWorking) {
                try {
                    DeliveryOrder order = TaskCenter.deliveryQueue.take(); // 阻塞式搶單
                    
                    // 同步日誌輸出
                    synchronized (logger) {
                        System.out.println(getName() + " 搶到外送單：" + order.getDeliveryID());
                        logger.log("Delivery", getName() + " 搶到外送單：" + order.getDeliveryID());
                    }
                    
                    deliver(order);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Delivery-" + getName());
        
        workerThread.start();
    }

    public synchronized void stopWorking() {
        isWorking = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    private void deliver(Order order) {
        if (order instanceof DeliveryOrder deliveryOrder) {
            synchronized (logger) {
                System.out.println(getName() + " 開始外送：" + deliveryOrder.getDeliveryID());
                logger.log("Delivery", getName() + " 開始外送：" + deliveryOrder.getDeliveryID());
            }
            
            try {
                Thread.sleep(3000); // 模擬外送時間
                
                // 原子性更新訂單狀態
                synchronized (deliveryOrder) {
                    deliveryOrder.updateStatus("已外送");
                }
                
                TaskCenter.completedQueue.add(deliveryOrder);
                
                synchronized (logger) {
                    System.out.println(getName() + " 完成外送：" + deliveryOrder.getDeliveryID());
                    logger.log("Delivery", getName() + " 完成外送：" + deliveryOrder.getDeliveryID());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                synchronized (logger) {
                    logger.log("Delivery", getName() + " 外送被中斷");
                }
            }
        } else {
            synchronized (logger) {
                System.out.println("錯誤：非外送訂單傳入 deliver 方法。");
                logger.log("Delivery", "錯誤：非外送訂單傳入 deliver 方法。");
            }
        }
    }
}