package tea_drinking_system.business;

import tea_drinking_system.drinkInformation.Drink;

public class OrderItem {
    private Drink drink;
    private int quantity;

    public OrderItem(Drink drink, int quantity) {
        this.drink = drink;
        this.quantity = quantity;
    }

    public Drink getDrink() { return drink; }
    public void setDrink(Drink drink) { this.drink = drink; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getSubtotal() {
        return (int) (drink.getPrice() * quantity);
    }

    @Override
    public String toString() {
        return String.format("%s x%d = $%d", 
            drink.getName(), quantity, getSubtotal());
    }
}