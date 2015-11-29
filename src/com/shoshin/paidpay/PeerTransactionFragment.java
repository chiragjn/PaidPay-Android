package com.shoshin.paidpay;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;

public class PeerTransactionFragment extends Fragment {//Only for the merchant account
	
	String balance;
	Button btnPay;
	TextView qt,amt,to;
	JSONObject qrcode;
	String paidpaybalance;
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
		 
		 
		 final SharedPreferences sp = ((AppController)getActivity().getApplication()).getInstance().prefs;
		 
		 
		 StringRequest post = new StringRequest(Request.Method.POST, "https://morning-reaches-5621.herokuapp.com/balance/",
                 new Response.Listener<String>() {

                   @Override
                   public void onResponse(String resp) {
                	   try{
                     JSONObject json = new JSONObject(resp);
                     paidpaybalance = json.getString("data");
                	   }
                	   catch(Exception e){
                		   
                	   }
                   }
                 }, new ErrorListener() {

                   @Override
                   public void onErrorResponse(VolleyError arg0) {
                     
                   }
                 }) {
               @Override
               protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> map = new HashMap<String,String>();
                 
                 
                   map.put("user_id",sp.getString("id", "1"));
                 
                 return map;
               }
             };
             //AppController.getInstance().getRequestQueue().add(post); Uncomment once monik implements webservice
		 
		 
		 final CitrusClient citrusClient = CitrusClient.getInstance(getActivity());
         citrusClient.enableLog(true);
//         String SIGNUP_ID = "test-signup";
//       String SIGNUP_SECRET = "c78ec84e389814a05d3ae46546d16d2e";
//       String SIGNIN_ID = "test-signin";
//       String SIGNIN_SECRET = "52f7e15efd4208cf5345dd554443fd99";
//       String VANITY = "testing";
         String signup_key="test-signup";
         String signup_secret="c78ec84e389814a05d3ae46546d16d2e";
         String signin_key="test-signin";
         String signin_secret="52f7e15efd4208cf5345dd554443fd99";
         String vanity="testing";
         citrusClient.init(signup_key,signup_secret,signin_key,signin_secret,vanity,com.citrus.sdk.Environment.SANDBOX);
		 
		 
		 
		 
		 
		 final String qr = getArguments().getString("qr");
		 try {
			qrcode = new JSONObject(qr);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 View transactionView = inflater.inflate(
                 R.layout.peer_fragment_transaction, container, false);
		 
		 
		 btnPay = (Button)transactionView.findViewById(R.id.pay);
		 btnPay.setClickable(false);
		 citrusClient.isUserSignedIn(new Callback<Boolean>() {
	            @Override
	            public void success(Boolean isUserLoggedIn) {
	                if (isUserLoggedIn) {
	                	
	                	citrusClient.getBalance(new com.citrus.sdk.Callback<Amount>() {
	                	     @Override
	                	     public void success(Amount amount) {
	                	    	 balance = amount.getValue();
	                	    	 btnPay.setClickable(true);
	                	     }

	                	     @Override
	                	     public void error(CitrusError error) {
	                	    	 
	                	     }
	                	  });
	                    
	                } else {
	                	citrusClient.signIn("jain.chirag925@gmail.com", "Engineer$@987", new Callback<CitrusResponse>() {
	                        @Override
	                        public void success(CitrusResponse citrusResponse) {
	                    //        Utils.showToast(getContext(), citrusResponse.getMessage());
	                        	citrusClient.getBalance(new com.citrus.sdk.Callback<Amount>() {
	                        	     @Override
	                        	     public void success(Amount amount) {
	                        	    	 balance=amount.getValue();
	                        	    	 btnPay.setClickable(true);
	                        	     }

	                        	     @Override
	                        	     public void error(CitrusError error) {}
	                        	  });
	                        		
	                        }

							@Override
							public void error(CitrusError error) {
								// TODO Auto-generated method stub
								
							}
	                	});
	                }
	            }

	            @Override
	            public void error(CitrusError error) {
	                
	            }
	        });
		 
		 qt = (TextView)transactionView.findViewById(R.id.qt);
		 amt = (TextView)transactionView.findViewById(R.id.amt);
		 to = (TextView)transactionView.findViewById(R.id.nm);
		 try{
		 amt.setText(amt.getText().toString()+qrcode.getString("amount"));
		 qt.setText("\""+qrcode.getString("description")+"\""+" - "+qrcode.getString("receiver").split(" ")[0]+".");
		 to.setText(to.getText().toString()+qrcode.getString("receiver"));
		 }
		 catch(Exception e){
			 
		 }
		 
		 
		 
		 btnPay.setOnClickListener(new OnClickListener() {
	 			
	 			

	 			@Override
	 			public void onClick(View v) {
	 				// TODO Auto-generated method stub
	 				
	 				//Fragment transaction to chirags frag with balance string in the bundle
	 				Bundle bundle = new Bundle();
	 				bundle.putString("citrusbalance",balance);
	 				bundle.putString("paidpaybalance",paidpaybalance);
	 				bundle.putString("qr",getArguments().getString("qr"));
	 				Fragment fm = new FragmentPayVia();
	 				fm.setArguments(bundle);
	                
	                FragmentTransaction transaction = getFragmentManager().beginTransaction();
	                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
	                
	                transaction.replace(R.id.pagerparent, fm);
	                
	                transaction.addToBackStack(null);	                       
	                
	                transaction.commit();
	 			}
	 		});
     	
     		
            
             
             
             
             
                    
                    
                    
                    
                    
					
					
					
                            
                            
                    

                    
                 
     	     
             
             return transactionView;
     }

}
