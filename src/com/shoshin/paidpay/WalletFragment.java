package com.shoshin.paidpay;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class WalletFragment extends Fragment {
    
	private final int CELL_DEFAULT_HEIGHT = 130;

    private ExpandingListView mListView;
    
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
     	
     		View walletView = inflater.inflate(
                     R.layout.fragment_collection_object_wallet, container, false);
     		
     		ExpandableListItem[] values = new ExpandableListItem[] {
                    new ExpandableListItem("Citrus", R.drawable.citrus,CELL_DEFAULT_HEIGHT,"$100","CITRUS"),
                    new ExpandableListItem("Paypal", R.drawable.paypal,CELL_DEFAULT_HEIGHT,"$200","PAYPAL"),
                    new ExpandableListItem("Paytm", R.drawable.paytm,CELL_DEFAULT_HEIGHT,"$300","PAYTM"),
                    new ExpandableListItem("PayUMoney", R.drawable.payu,CELL_DEFAULT_HEIGHT,"$400","PAYU"),
                    new ExpandableListItem("Visa", R.drawable.visa,CELL_DEFAULT_HEIGHT,"$400","VISA"),
                    new ExpandableListItem("MasterCard", R.drawable.mastercard,CELL_DEFAULT_HEIGHT,"$400","MASTERCARD"),
            };
     		
     		 List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();

             for (int i = 0; i < values.length; i++) {
                 ExpandableListItem obj = values[i];
                 mData.add(new ExpandableListItem(obj.getCardName(), obj.getCardLogo(),
                         obj.getCollapsedHeight(), obj.getBalance(),obj.getType()));
             }
             mData.add((new ExpandableListItem("",0,0,"","")));

             CustomArrayAdapter adapter = new CustomArrayAdapter(this.getContext(), R.layout.list_full_card, mData);

             mListView = (ExpandingListView)walletView.findViewById(R.id.lvExp);
             mListView.setAdapter(adapter);
             Log.e("View Created","yes");
             return walletView;
     }
}