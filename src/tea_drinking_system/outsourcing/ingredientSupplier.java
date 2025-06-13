package tea_drinking_system.outsourcing;

import tea_drinking_system.drinkInformation.Ingredient;

public class ingredientSupplier {
	public Ingredient supply(String name, int quantity) {
        System.out.println("供應商提供原料：" + name + " 數量：" + quantity);
        return new Ingredient(name, quantity);
    }
}
