package tea_drinking_system.drinkInformation;

import java.util.ArrayList;
import java.util.List;

import tea_drinking_system.employee.Employee;
import tea_drinking_system.employee.Employer;

public class Menu {
	private List<Drink>drinks;
	
	public Menu() {
		drinks = new ArrayList<>();
        // 預設飲料
        drinks.add(new Drink("紅茶", 30));
        drinks.add(new Drink("綠茶", 35));
        drinks.add(new Drink("珍珠奶茶", 55));
        drinks.add(new Drink("冬瓜檸檬", 45));
	}
	
	public Drink getDrink(String name) {
		for (Drink d : drinks) {
            if (d.getName().equals(name)) {
                return d;
            }
        }
        return null; // 沒找到
	}
	
	public void addDrink(Drink drink, Employee operator) {
        if (operator instanceof Employer) {
            drinks.add(drink);
            System.out.println("飲料 " + drink.getName() + " 已由老闆新增！");
        } else {
            System.out.println("illegal modification");
        }
    }
	
	public List<Drink> getDrinkList() {
	    return drinks;
	}
	
	public void printMenu() {
        System.out.println("=== 菜單 ===");
        for (int i = 0; i < drinks.size(); i++) {
            Drink d = drinks.get(i);
            System.out.println((i + 1) + ". " + d.getName() + " - $" + d.getPrice());
        }
    }
	
	public void deleteDrink(String name) {
		for(Drink d: drinks) {
			if (d.getName() == name) {
				drinks.remove(d);
			}
		}
	}
	
	public void editPrice(String name, int price) {
		for(Drink d: drinks) {
			if (d.getName() == name) {
				d.editPrice(price);
			}
		}
	}

}
