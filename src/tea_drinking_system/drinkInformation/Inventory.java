package tea_drinking_system.drinkInformation;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private List<Ingredient> stockList;

    public Inventory() {
        stockList = new ArrayList<>();
        stockList.add(new Ingredient("紅茶", 20));
        stockList.add(new Ingredient("綠茶", 20));
        stockList.add(new Ingredient("牛奶", 10));
        stockList.add(new Ingredient("冬瓜茶", 10));
        stockList.add(new Ingredient("檸檬汁", 10));
        stockList.add(new Ingredient("珍珠", 10));
    }

    public void addInventory(String ingreName, int quantity) {
        for (Ingredient ing : stockList) {
            if (ing.getName().equals(ingreName)) {
                ing.setQuantity(ing.getQuantity() + quantity);
                return;
            }
        }
        stockList.add(new Ingredient(ingreName, quantity));
    }
    
    public List<Ingredient> getInventory(){
    	return stockList;
    }

}
