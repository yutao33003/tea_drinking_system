package tea_drinking_system.employee;

import java.util.ArrayList;
import java.util.List;
import tea_drinking_system.frame.DrinkFrame;

public class EmployeeInitialization {
	public static List<Cashier> allCashiers = new ArrayList<>();
	public static Employer employer = new Employer("阿國",DrinkFrame.logPanel);

	public static void startAllEmployees(){
		Cashier ca1 = new Cashier("小白", DrinkFrame.logPanel, Cashier.CashierRole.READY); 
		Cashier ca2 = new Cashier("小聰", DrinkFrame.logPanel, Cashier.CashierRole.INVENTORY);
		Maker ma1 = new Maker("大林", DrinkFrame.logPanel);
		Maker ma2 = new Maker("大橘", DrinkFrame.logPanel);
		Delivery de1 = new Delivery("樁樁", DrinkFrame.logPanel);
		Delivery de2 = new Delivery("青青", DrinkFrame.logPanel);
		
        registerAndStartCashier(ca1);
        registerAndStartCashier(ca2);
        
        new Thread(ma1).start();
        new Thread(ma2).start();

        de1.startWorking();
        de2.startWorking();
    }
	
	    // 新增收銀員並讓他開始工作
	public static void registerAndStartCashier(Cashier cashier) {
		allCashiers.add(cashier);
	    new Thread(() -> cashier.startWorking()).start();
	}	
}
