package tea_drinking_system.drinkInformation;

public class Ingredient {
    private String name;
    private int quantity; // 單位為份數

    public Ingredient(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    
    public int getQuantity() { return quantity; }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

