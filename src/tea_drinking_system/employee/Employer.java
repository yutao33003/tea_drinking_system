package tea_drinking_system.employee;

import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import tea_drinking_system.drinkInformation.Drink;
import tea_drinking_system.drinkInformation.DrinkCenter;
import tea_drinking_system.drinkInformation.Formula;
import tea_drinking_system.drinkInformation.Menu;
import tea_drinking_system.employee.Cashier.CashierRole;
import tea_drinking_system.frame.LogOverviewPanel;
import tea_drinking_system.outsourcing.ingredientSupplier;
import tea_drinking_system.drinkInformation.Ingredient;

public class Employer extends Employee {
    
    // 員工管理相關的資料結構
    private final ConcurrentHashMap<String, Employee> employeeMap;
    private final List<Employee> employeeList;
    private final AtomicInteger totalEmployeeCount;
    
    public Employer(String name, LogOverviewPanel logger) {
        super(name, "店長", 45000, logger);
        this.staffID = "MANE001";
        this.employeeMap = new ConcurrentHashMap<>();
        this.employeeList = new ArrayList<>();
        this.totalEmployeeCount = new AtomicInteger(0);
    }
    
    public Employee hireEmployee(String name, String position, CashierRole role) {
        Employee newEmployee = null;
        
        if (position.equals("收銀員")) {
            newEmployee = new Cashier(name, logger, role);
            logger.log("Employer", getName() + " 已錄取 " + position + " " + name + " (角色: " + role + ")");
        } 
        else if (position.equals("製作員")) {
            newEmployee = new Maker(name, logger);
            logger.log("Employer", getName() + " 已錄取 " + position + " " + name);
        } 
        else if (position.equals("外送員")) {
            newEmployee = new Delivery(name, logger);
            logger.log("Employer", getName() + " 已錄取 " + position + " " + name);
        } 
        else {
            System.out.println("未知職位: " + position);
            logger.log("Employer", "未知職位: " + position + "，無法錄取 " + name);
            return null;
        }
        
        // 如果成功創建員工，加入管理清單
        if (newEmployee != null) {
            synchronized (employeeList) {
                employeeList.add(newEmployee);
                employeeMap.put(newEmployee.getStaffID(), newEmployee);
                totalEmployeeCount.incrementAndGet();
            }
            logger.log("Employer", "員工 " + name + " (ID: " + newEmployee.getStaffID() + ") 已加入員工名冊");
        }
        
        return newEmployee;
    }
    
    // 重載方法，支援不需要 CashierRole 的職位
    public Employee hireEmployee(String name, String position) {
        if (position.equals("收銀員")) {
            // 預設為庫存管理角色
            return hireEmployee(name, position, CashierRole.INVENTORY);
        } else {
            return hireEmployee(name, position, null);
        }
    }
    
    // 顯示所有員工資訊
    public void showAllEmployees() {
        synchronized (employeeList) {
            if (employeeList.isEmpty()) {
                logger.log("Employer", "=== 員工名冊 ===");
                logger.log("Employer", "目前沒有任何員工");
                logger.log("Employer", "==================");
                return;
            }
            
            logger.log("Employer", "=== 員工名冊 ===");
            logger.log("Employer", "總員工數: " + totalEmployeeCount.get() + " 人");
            logger.log("Employer", "-------------------");
            
            // 統計各職位員工數量
            int cashierCount = 0, makerCount = 0, deliveryCount = 0;
            
            for (Employee employee : employeeList) {
                String employeeInfo = String.format("ID: %s | 姓名: %s | 職位: %s | 薪資: %d", 
                    employee.getStaffID(), 
                    employee.getName(), 
                    employee.getPosition(), 
                    employee.getSalary());
                
                // 如果是收銀員，顯示角色資訊
                if (employee instanceof Cashier) {
                    Cashier cashier = (Cashier) employee;
                    // 注意：需要在 Cashier 類別中添加 getRole() 方法
                    employeeInfo += " | 角色: " + getCashierRoleString(cashier);
                    cashierCount++;
                } else if (employee instanceof Maker) {
                    makerCount++;
                } else if (employee instanceof Delivery) {
                    deliveryCount++;
                }
                
                logger.log("Employer", employeeInfo);
            }
            
            logger.log("Employer", "-------------------");
            logger.log("Employer", String.format("職位統計 - 收銀員: %d人 | 製作員: %d人 | 外送員: %d人", 
                cashierCount, makerCount, deliveryCount));
            logger.log("Employer", "==================");
        }
    }
    
    // 根據員工ID查找員工
    public Employee findEmployeeById(String staffID) {
        return employeeMap.get(staffID);
    }
    
    // 根據姓名查找員工
    public Employee findEmployeeByName(String name) {
        synchronized (employeeList) {
            for (Employee employee : employeeList) {
                if (employee.getName().equals(name)) {
                    return employee;
                }
            }
        }
        return null;
    }
    
    // 解僱員工
    public boolean fireEmployee(String staffID) {
        Employee employee = employeeMap.get(staffID);
        if (employee != null) {
            synchronized (employeeList) {
                employeeList.remove(employee);
                employeeMap.remove(staffID);
                totalEmployeeCount.decrementAndGet();
            }
            logger.log("Employer", "員工 " + employee.getName() + " (ID: " + staffID + ") 已被解僱");
            return true;
        } else {
            logger.log("Employer", "找不到員工ID: " + staffID);
            return false;
        }
    }
    
    // 獲取員工總數
    public int getTotalEmployeeCount() {
        return totalEmployeeCount.get();
    }
    
    // 獲取所有員工列表的副本（防止外部修改）
    public List<Employee> getAllEmployees() {
        synchronized (employeeList) {
            return new ArrayList<>(employeeList);
        }
    }
    
    // 輔助方法：獲取收銀員角色字串
    private String getCashierRoleString(Cashier cashier) {
        // 注意：這需要在 Cashier 類別中添加 getRole() 方法
        // 暫時返回預設值，實際需要修改 Cashier 類別
        return "未知角色"; // 這裡需要根據實際的 Cashier 類別實作來修改
    }
    
    // 原有的其他方法保持不變
    public void addDrinkToMenu(Menu menu, Formula formula, String Drinkname, int price, List<Ingredient> ingredients) {
        Drink drink = new Drink(Drinkname, price);
        menu.addDrink(drink, this);  // 傳入自己作為驗證身份
        formula.addFormula(drink.getName(), ingredients);
        logger.log("Employer", name + " 店長新增 " + Drinkname + " 入菜單, " + price + " 元");
    }
    
    public void orderIngredient(String ingreName) {
        ingredientSupplier supplier = new ingredientSupplier();
        Ingredient newStock = supplier.supply(ingreName, 100);
        DrinkCenter.inventory.addInventory(newStock.getName(), newStock.getQuantity());
        logger.log("Employer", name + " 店長已下訂 " + ingreName + " 100份");
    }
    
    public void deleteDrinkToMenu(Menu menu, Formula formula, String drinkName) {
        menu.deleteDrink(drinkName);
        formula.deleteDrinkFormula(drinkName);
        logger.log("Employer", name + " 店長已從菜單與食譜中刪除 " + drinkName);
    }
    
    public void editDrinkPrice(Menu menu, String drinkName, int price) {
        menu.editPrice(drinkName, price);
        logger.log("Employer", name + " 店長已將 " + drinkName + " 修改為 " + price + " 元");
    }
}

// 同時需要修改 Cashier 類別，添加 getRole() 方法
// 在 Cashier 類別中添加以下方法：
/*
public CashierRole getRole() {
    return this.role;
}
*/