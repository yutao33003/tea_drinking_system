package tea_drinking_system.employee;

import tea_drinking_system.frame.LogOverviewPanel;

public class Employee {
	protected String name;
	protected String position;
	protected String staffID;
	protected int salary;
	protected LogOverviewPanel logger;
	
	public Employee(String name, String position, int salary, LogOverviewPanel logger) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.logger = logger;
	}
	
	public String getName() {
		return name;
	}
	
	public void sayHello(String pos) {
		System.out.printf("你好我是" + position + name);
		logger.log(pos, "你好我是" + position + " " + name +"工號:" + staffID);
	}
	
	public String getStaffID() {
        return staffID;
    }
	
	public String getPosition() {
		return position;
	}
	
	public int getSalary() {
		return salary;
	}
}
