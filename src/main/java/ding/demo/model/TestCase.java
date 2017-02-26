package ding.demo.model;

import java.util.HashMap;

public class TestCase {
	private int id;
	private int colorCount = 0;
	private HashMap<Integer, Customer> customerMap = new HashMap<Integer, Customer>();
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getColorCount() {
		return colorCount;
	}
	public void setColorCount(int colorCount) {
		this.colorCount = colorCount;
	}
	public HashMap<Integer, Customer> getCustomerMap() {
		return customerMap;
	}
	public void setCustomerMap(HashMap<Integer, Customer> customerMap) {
		this.customerMap = customerMap;
	}
	
	public void insetNewCustomer(Customer c) {
		this.customerMap.put(c.getId(), c);
	}
}
