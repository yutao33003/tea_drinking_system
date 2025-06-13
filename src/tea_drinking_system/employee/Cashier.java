package tea_drinking_system.employee;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import tea_drinking_system.business.Order;
import tea_drinking_system.business.OrderItem;
import tea_drinking_system.business.TaskCenter;
import tea_drinking_system.drinkInformation.DrinkCenter;
import tea_drinking_system.drinkInformation.Ingredient;
import tea_drinking_system.frame.LogOverviewPanel;

public class Cashier extends Employee {
    private static final AtomicInteger cashierCount = new AtomicInteger(1);
    private CashierRole role;
    private volatile boolean isWorking = true;
    private Thread workerThread;
    
    // 庫存檢查鎖，確保庫存檢查和扣除的原子性
    private static final ReentrantLock inventoryLock = new ReentrantLock();

    public Cashier(String name, LogOverviewPanel logger, CashierRole role) {
        super(name, "收銀員", 32000, logger);
        this.staffID = "CA" + String.format("%03d", cashierCount.getAndIncrement());
        this.role = role;
    }

    public static enum CashierRole {
        INVENTORY, READY
    }
    
    public CashierRole getRole() {
        return this.role;
    }

    public synchronized void startWorking() {
        if (workerThread != null && workerThread.isAlive()) {
            return;
        }
        
        isWorking = true;
        workerThread = new Thread(() -> {
            while (isWorking) {
                try {
                    if (role == CashierRole.INVENTORY) {
                        Order order = TaskCenter.inventoryQueue.take();
                        confirmInventory(order);
                    } else if (role == CashierRole.READY) {
                        Order order = TaskCenter.completedQueue.take();
                        notifyOrderReady(order);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Cashier-" + getName() + "-" + role);
        
        workerThread.start();
    }

    public synchronized void stopWorking() {
        isWorking = false;
        if (workerThread != null) {
            workerThread.interrupt();
        }
    }

    // 線程安全的庫存確認
    public boolean confirmInventory(Order order) {
        inventoryLock.lock();
        try {
            List<OrderItem> items = order.getOrderItems();

            // 先檢查所有原料是否足夠
            for (OrderItem item : items) {
                if (!checkItemInventory(item)) {
                    return false;
                }
            }

            // 所有原料都足夠，扣除庫存並提交訂單
            for (OrderItem item : items) {
                deductInventory(item);
            }

            logSafely("庫存確認完成，提交訂單: " + order.getOrderID());
            submitOrder(order);
            return true;

        } finally {
            inventoryLock.unlock();
        }
    }

    private boolean checkItemInventory(OrderItem item) {
        String drinkName = item.getDrink().getName();
        int quantity = item.getQuantity();

        List<Ingredient> requiredIngredients = DrinkCenter.formula.getFormula(drinkName);
        if (requiredIngredients == null) {
            logSafely("找不到「" + drinkName + "」的配方，無法確認庫存。");
            return false;
        }

        for (Ingredient required : requiredIngredients) {
            boolean found = false;
            for (Ingredient stock : DrinkCenter.inventory.getInventory()) {
                if (stock.getName().equals(required.getName())) {
                    found = true;
                    int requiredTotal = required.getQuantity() * quantity;

                    if (stock.getQuantity() < requiredTotal) {
                        logSafely("原料不足: " + required.getName() +
                                "（需要: " + requiredTotal + ", 庫存: " + stock.getQuantity() + "）");
                        return false;
                    }
                    break;
                }
            }

            if (!found) {
                logSafely("庫存中找不到原料: " + required.getName() + " 請改點他項");
                return false;
            }
        }
        return true;
    }

    private void deductInventory(OrderItem item) {
        String drinkName = item.getDrink().getName();
        int quantity = item.getQuantity();

        List<Ingredient> requiredIngredients = DrinkCenter.formula.getFormula(drinkName);
        if (requiredIngredients != null) {
            for (Ingredient required : requiredIngredients) {
                for (Ingredient stock : DrinkCenter.inventory.getInventory()) {
                    if (stock.getName().equals(required.getName())) {
                        int requiredTotal = required.getQuantity() * quantity;
                        stock.setQuantity(stock.getQuantity() - requiredTotal);
                        break;
                    }
                }
            }
        }
    }

    public void submitOrder(Order order) {
        try {
            TaskCenter.taskQueue.put(order); // 加入待製作佇列
            TaskCenter.orderCashierMap.put(order, this); // 登記誰送出的
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logSafely("提交訂單被中斷: " + order.getOrderID());
        }
    }

    public void notifyOrderReady(Order order) {
        String message = "收銀員 [" + getName() + "] 的訂單 [" + order.getOrderID() + "] 已完成！";
        String pickupMessage = "編號:" + order.getOrderID() + "請取餐";
        
        synchronized (logger) {
            System.out.println(message);
            logger.log("Cashier", message);
            logger.log("Cashier", pickupMessage);
        }
    }

    private void logSafely(String message) {
        synchronized (logger) {
            logger.log("Cashier", message);
        }
    }
}
