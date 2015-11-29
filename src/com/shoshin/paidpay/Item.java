package com.shoshin.paidpay;

public class Item {
	int quantity;
	double price;
	String itemName;
	
	Item(String itemName,double price,int quantity)
	{
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}
}
