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
    
	private final int CELL_DEFAULT_HEIGHT = 150;
    private final int NUM_OF_CELLS = 5;

    private ExpandingListView mListView;
    
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
     	
     		View walletView = inflater.inflate(
                     R.layout.fragment_collection_object_wallet, container, false);
     		
     		ExpandableListItem[] values = new ExpandableListItem[] {
                    new ExpandableListItem("Citrus", R.drawable.cit,CELL_DEFAULT_HEIGHT,"$100"),
                    new ExpandableListItem("Paypal", R.drawable.cit,CELL_DEFAULT_HEIGHT,"$200"),
                    new ExpandableListItem("Paytm", R.drawable.cit,CELL_DEFAULT_HEIGHT,"$300"),
                    new ExpandableListItem("PayUMoney", R.drawable.cit,CELL_DEFAULT_HEIGHT,"$400"),
            };
     		
     		 List<ExpandableListItem> mData = new ArrayList<ExpandableListItem>();

             for (int i = 0; i < NUM_OF_CELLS; i++) {
                 ExpandableListItem obj = values[i % values.length];
                 mData.add(new ExpandableListItem(obj.getCardName(), obj.getCardLogo(),
                         obj.getCollapsedHeight(), obj.getBalance()));
             }

             CustomArrayAdapter adapter = new CustomArrayAdapter(this.getContext(), R.layout.list_full_card, mData);

             mListView = (ExpandingListView)walletView.findViewById(R.id.lvExp);
             mListView.setAdapter(adapter);
             Log.e("View Created","yes");
             return walletView;
     }
}