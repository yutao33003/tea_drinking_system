package tea_drinking_system.drinkInformation;

import java.util.*;

public class Formula {
    private Map<String, List<Ingredient>> drinkFormulas = new HashMap<>();
    
    public Formula() {
    	List<Ingredient> blackTea = new ArrayList<>();
    	blackTea.add(new Ingredient("紅茶", 1));
    	drinkFormulas.put("紅茶", blackTea);
    	
    	List<Ingredient> greenTea = new ArrayList<>();
    	greenTea.add(new Ingredient("綠茶",1));
    	drinkFormulas.put("綠茶", greenTea);
    	
    	List<Ingredient> milkTea = new ArrayList<>();
    	milkTea.add(new Ingredient("紅茶",1));
    	milkTea.add(new Ingredient("牛奶",1));
    	milkTea.add(new Ingredient("珍珠",1));
    	drinkFormulas.put("珍珠奶茶", milkTea);
    	
    	List<Ingredient> lemonTea = new ArrayList<>();
    	lemonTea.add(new Ingredient("冬瓜茶", 1));
    	lemonTea.add(new Ingredient("檸檬汁", 1));
    	drinkFormulas.put("冬瓜檸檬", lemonTea);
    }

    public void addFormula(String drinkName, List<Ingredient> ingredients) {
        drinkFormulas.put(drinkName, ingredients);
    }

    // 取得配方
    public List<Ingredient> getFormula(String drinkName) {
        return drinkFormulas.get(drinkName);
    }

    // 可選：列出配方
    public void printFormula(String drinkName) {
        List<Ingredient> ingredients = drinkFormulas.get(drinkName);
        if (ingredients != null) {
            System.out.println("配方 - " + drinkName + ":");
            for (Ingredient i : ingredients) {
                System.out.println("- " + i.getName() + ": " + i.getQuantity());
            }
        } else {
            System.out.println("找不到配方");
        }
    }
    
    public void deleteDrinkFormula(String name) {
    	drinkFormulas.remove(name);
    }
}

