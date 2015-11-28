package com.shoshin.paidpay;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HistoryFragment extends Fragment {
	
	

        @Override
        public View onCreateView(LayoutInflater inflater,
                ViewGroup container, Bundle savedInstanceState){
        	
        		View historyView = inflater.inflate(
                        R.layout.fragment_collection_object_history, container, false);
                
                return historyView;
        }
    

}
