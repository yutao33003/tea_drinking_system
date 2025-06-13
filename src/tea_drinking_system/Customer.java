package tea_drinking_system;

import tea_drinking_system.business.Order;

public class Customer {
	private String name;
	private String phoneNum;
	private String paymentMethod;
	
	public Customer(String name, String phone, String paymentMethod) {
	       this.name = name;
	       this.phoneNum = phone;
	       this.paymentMethod = paymentMethod;
	   }
	
	public String getName() {
		return name;
	}

	public String getPhoneNum() {
		return phoneNum;
	}
	
	public String getPaymentMethod() {
		return paymentMethod;
	}
	
	public Order placeOrder() {
		return new Order(this);
	}	
	
    public void orderReceived(boolean received) {
    	if (received) {
    		System.out.println("已收到飲料");
    	}
    	else {
    		System.out.println("未收到飲料");
    	}
    }
}
