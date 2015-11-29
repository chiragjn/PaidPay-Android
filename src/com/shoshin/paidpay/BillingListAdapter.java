package com.shoshin.paidpay;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class BillingListAdapter extends ArrayAdapter<Item> {
	  private final Context context;
	  private ArrayList<Item> items;
	  
	  BillingListAdapter(Context context,ArrayList<Item> items) {
	    super(context, -1, items);
	    this.context = context;
	    this.items=items; 
	  }

	  @Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = inflater.inflate(R.layout.bill_list_item, parent, false);
	    TextView itemName = (TextView) rowView.findViewById(R.id.item_name);
	    TextView itemQuantity = (TextView) rowView.findViewById(R.id.item_quantity);
	    TextView itemPrice = (TextView) rowView.findViewById(R.id.item_price);
	    itemName.setText(items.get(position).itemName);
	    itemQuantity.setText(String.valueOf(items.get(position).quantity));
	    itemPrice.setText(String.valueOf(items.get(position).price));
	 
	    return rowView;
	  }
	}