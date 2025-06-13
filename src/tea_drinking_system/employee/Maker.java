package tea_drinking_system.employee;

import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;
import tea_drinking_system.business.DeliveryOrder;
import tea_drinking_system.business.Order;
import tea_drinking_system.business.TaskCenter;
import tea_drinking_system.frame.LogOverviewPanel;

public class Maker extends Employee implements Runnable {
    private static final AtomicInteger makerCount = new AtomicInteger(1);
    private volatile boolean isWorking = true;

    public Maker(String name, LogOverviewPanel logger) {
        super(name, "製作員", 34000, logger);
        this.staffID = "MA" + String.format("%03d", makerCount.getAndIncrement());
    }

    public synchronized void stopWorking() {
        isWorking = false;
        Thread.currentThread().interrupt();
    }

    @Override
    public void run() {
        while (isWorking && !Thread.currentThread().isInterrupted()) {
            try {
                Order order = TaskCenter.taskQueue.take(); // 自動「搶單」
                
                // 同步日誌記錄
                logSafely("製作員 " + name + " 開始製作訂單 " + order.getOrderID());
                
                Thread.sleep(2000); // 模擬製作時間
                
                logSafely("製作員 " + name + " 完成訂單 " + order.getOrderID());

                // 原子性處理訂單分發
                processCompletedOrder(order);

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logSafely(name + " 製作中斷！");
                break;
            }
        }
    }

    private void processCompletedOrder(Order order) {
        try {
            if (!order.isDelivery()) {
                TaskCenter.completedQueue.put(order);
            } else {
                DeliveryOrder deliveryOrder = (DeliveryOrder) order;
                TaskCenter.deliveryQueue.put(deliveryOrder);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logSafely(name + " 訂單分發被中斷");
        }
    }

    private void logSafely(String message) {
        System.out.println(message);
        SwingUtilities.invokeLater(() -> {
            synchronized (logger) {
                logger.log("Maker", message);
            }
        });
    }
}

