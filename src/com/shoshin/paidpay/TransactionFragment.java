package com.shoshin.paidpay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.toolbox.StringRequest;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.response.CitrusError;
import com.citrus.sdk.response.CitrusResponse;

public class TransactionFragment extends Fragment {//Only for the merchant account
	String balance;
	 Button btnPay;
	 String paidpaybalance;
	 double total;
	
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
		 
final SharedPreferences sp = ((AppController)getActivity().getApplication()).getInstance().prefs;
		 
		 
		 StringRequest post = new StringRequest(Request.Method.POST, "https://morning-reaches-5621.herokuapp.com/balance/",
                 new Response.Listener<String>() {

                   @Override
                   public void onResponse(String resp) {
                     JSONObject json;
					try {
						json = new JSONObject(resp);
					
                     paidpaybalance = json.getString("data");
                     
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
		 
		 
		 
		 
		 final String qrtype = getArguments().getString("qrtype");
		 final String qrkey = getArguments().getString("qrkey");
		 final String qr = getArguments().getString("qr");
		 
		 View transactionView = inflater.inflate(
                 R.layout.fragment_transaction, container, false);
		 
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
		 
		 
		 
		 
		 
		 
		 ListView billingList = (ListView)transactionView.findViewById(R.id.merchant_billing_list);
  		
  		TextView merchant_name = (TextView)transactionView.findViewById(R.id.merchant_name);
  		TextView totale = (TextView)transactionView.findViewById(R.id.total_price);
  		ArrayList<Item> items = new ArrayList<Item>();
  		total = 0.0;
  		final String rawData = getArguments().getString("qr");
  		try {
				JSONObject json = new JSONObject(rawData);
				merchant_name.setText(json.getString("receiver"));
				JSONArray products = json.getJSONArray("products"); 
				
				for(int i=0 ; i<products.length();i++)
				{
					JSONObject j = (JSONObject)products.get(i);
					items.add(new Item(j.getString("name"),(j.getDouble("price")*j.getInt("quantity")),j.getInt("quantity")));
					total+=(j.getDouble("price")*j.getInt("quantity"));
				}
			} catch (JSONException e) {
				Log.e(getTag(), e.toString());
				e.printStackTrace();
			}
  		totale.setText(String.valueOf(total));
  		BillingListAdapter billing_list_adapter =  new BillingListAdapter(getContext(),items);
  		billingList.setAdapter(billing_list_adapter);
     		
            
             
             
             
             
                    
                    
                    
                    
                    
					
  		 btnPay.setOnClickListener(new OnClickListener() {
 			
 			

 			@Override
 			public void onClick(View v) {
 				// TODO Auto-generated method stub
 				
 				//Fragment transaction to chirags frag with balance string in the bundle
 				Bundle bundle = new Bundle();
 				bundle.putString("citrusbalance",balance);
 				bundle.putString("paidpaybalance",paidpaybalance);
 				bundle.putString("qr",getArguments().getString("qr"));
 				bundle.putString("totalcost", total+"");
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
