package tea_drinking_system.drinkInformation;

public class Drink {

	private String name;
	private int price;
	
	public Drink(String name, int price) {
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPrice() {
		return price;
	}
	
	protected void editPrice(int price) {
		this.price = price;
	}
}
