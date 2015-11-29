package com.shoshin.paidpay;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.citrus.sdk.Callback;
import com.citrus.sdk.CitrusClient;
import com.citrus.sdk.TransactionResponse;
import com.citrus.sdk.classes.Amount;
import com.citrus.sdk.classes.CitrusException;
import com.citrus.sdk.payment.PaymentType;
import com.citrus.sdk.response.CitrusError;

public class FragmentPayVia extends Fragment {

	private final int CELL_DEFAULT_HEIGHT = 130;

	private ExpandingListViewPayVia mListView;
	JSONObject json;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		
		final SharedPreferences sp = AppController.getInstance().prefs;
		try {
			json = new JSONObject(getArguments().getString("qr"));
		} catch (JSONException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		View walletView = inflater.inflate(R.layout.fragment_pay_via,
				container, false);

		ExpandableCardsWithOffers[] values = new ExpandableCardsWithOffers[] {
				new ExpandableCardsWithOffers("PaidPay Balance",R.drawable.ic_face_black_24dp,
						CELL_DEFAULT_HEIGHT, "$40", "OWN", "No Offers"),
				new ExpandableCardsWithOffers("Citrus", R.drawable.citrus,
						CELL_DEFAULT_HEIGHT, "$" + getArguments().getString("citrusbalance"), "CITRUS", "5% Off"),
				new ExpandableCardsWithOffers("Paypal", R.drawable.paypal,
						CELL_DEFAULT_HEIGHT, "$150", "PAYPAL", "3% Cashback"),
				new ExpandableCardsWithOffers("Paytm", R.drawable.paytm,
						CELL_DEFAULT_HEIGHT, "$30", "PAYTM", "No Offers"),
				new ExpandableCardsWithOffers("PayUMoney", R.drawable.payu,
						CELL_DEFAULT_HEIGHT, "$4.20", "PAYU", "Promo Code: XYZ"),
				new ExpandableCardsWithOffers("Visa", R.drawable.visa,
						CELL_DEFAULT_HEIGHT, "$80.0", "VISA",
						"5% Discount on Amazon"),
				new ExpandableCardsWithOffers("MasterCard",
						R.drawable.mastercard, CELL_DEFAULT_HEIGHT, "$200",
						"MASTERCARD", "No More Offers"), };

		List<ExpandableCardsWithOffers> mData = new ArrayList<ExpandableCardsWithOffers>();

		for (int i = 0; i < values.length; i++) {
			ExpandableCardsWithOffers obj = values[i];
			mData.add(new ExpandableCardsWithOffers(obj.getCardName(), obj
					.getCardLogo(), obj.getCollapsedHeight(), obj.getBalance(),
					obj.getType(), obj.getOffer()));
		}
		mData.add((new ExpandableCardsWithOffers("", 0, 0, "", "", "")));

		PayViaAdapter adapter = new PayViaAdapter(this.getContext(),
				R.layout.list_full_cards_with_offers, mData);
		Button btn = (Button)walletView.findViewById(R.id.pay_confirm);
		btn.setClickable(false);
		mListView = (ExpandingListViewPayVia) walletView.findViewById(R.id.lvExp);;
		mListView.pay_btn = btn;
		mListView.setAdapter(adapter);
		final CitrusClient citrusClient = CitrusClient.getInstance(getActivity());
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ExpandableCardsWithOffers e = (ExpandableCardsWithOffers)mListView.getAdapter().getItem(mListView.expandedPosition);
				
				final int mode = e.cardType.equalsIgnoreCase("CITRUS")?1:2;
				final String BILL_URL = "https://salty-plateau-1529.herokuapp.com/billGenerator.sandbox.php";
				if(mode==1)
				try {
	                citrusClient.payUsingCitrusCash(new PaymentType.CitrusCash(new Amount("1"), BILL_URL), new Callback<TransactionResponse>() {
	                    @Override
	                    public void success(TransactionResponse transactionResponse) {
	                    	//Send to server
	                    	
	                    	StringRequest post = new StringRequest(Request.Method.POST, "https://morning-reaches-5621.herokuapp.com/complete/",
                                    new Response.Listener<String>() {

                                      @Override
                                      public void onResponse(String resp) {
                                        
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
                                      try {
										map.put("key",json.getString("key"));
									
                                      map.put("type",json.getString("type"));
                                      } catch (JSONException e) {
  										// TODO Auto-generated catch block
  										e.printStackTrace();
  									}
                                      map.put("mode","1"); //1 or 2, citros or self
                                    
                                    return map;
                                  }
                                };
                                AppController.getInstance().getRequestQueue().add(post);
	                    }

	                    @Override
	                    public void error(CitrusError error) {
	                        
	                    }
	                });
	            } catch (CitrusException e1) {
	                e1.printStackTrace();

	                
	            }
				
				if(mode==2){
					StringRequest post = new StringRequest(Request.Method.POST, "https://morning-reaches-5621.herokuapp.com/complete/",
                            new Response.Listener<String>() {

                              @Override
                              public void onResponse(String resp) {
                                
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
                              try {
								map.put("key",json.getString("key"));
							
                              map.put("type",json.getString("type"));
                              } catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                              map.put("mode","2"); //1 or 2, citros or self
                            
                            return map;
                          }
                        };
                        AppController.getInstance().getRequestQueue().add(post);
				}
				
				
				
			}
		});
		Log.e("View Created", "yes");
		return walletView;
	}
}