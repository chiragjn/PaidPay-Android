package com.shoshin.paidpay;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class P2pFragment extends Fragment {
	
	Bitmap decodedByte;
	
	
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
     	
     		View p2pView = inflater.inflate(
                     R.layout.fragment_p2p, container, false);
     		
     		final Button generate = (Button)p2pView.findViewById(R.id.generate);
     		final ImageView qrcode = (ImageView)p2pView.findViewById(R.id.qrcode);
     		final EditText amount = (EditText)p2pView.findViewById(R.id.amount);
     		final EditText desc = (EditText)p2pView.findViewById(R.id.desc);
     		final SharedPreferences sp = ((AppController)getActivity().getApplication()).getInstance().prefs;
     		generate.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(generate.getText().toString().equalsIgnoreCase("generate")){
						
						generate.setClickable(false);
						
						
						StringRequest post = new StringRequest(Request.Method.POST, "https://morning-reaches-5621.herokuapp.com/generate_transfer/",
            					new Response.Listener<String>() {

            						@Override
            						public void onResponse(String resp) {
            							
            							generate.setClickable(true);
            							generate.setText("Share");
            							JSONObject json = null;
            							try {
											json = new JSONObject(resp);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
            							
            							byte[] decodedString = null;
										try {
											decodedString = Base64.decode(json.getString("data"), Base64.DEFAULT);
										} catch (JSONException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
            							decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            							qrcode.setImageBitmap(decodedByte);
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
            						map.put("amount",amount.getText().toString());
            						map.put("description",desc.getText().toString());
            						map.put("currency","1"); 
            					
            					return map;
            				}
            			};
            			AppController.getInstance().getRequestQueue().add(post);
						
					}
					else{
						//share mode
						Intent share = new Intent(Intent.ACTION_SEND);
						share.setType("image/jpeg");
						ByteArrayOutputStream bytes = new ByteArrayOutputStream();
						decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
						File f = new File(Environment.getExternalStorageDirectory() + File.separator + "temporary_file.jpg");
						try {
						    f.createNewFile();
						    FileOutputStream fo = new FileOutputStream(f);
						    fo.write(bytes.toByteArray());
						} catch (IOException e) {                       
						        e.printStackTrace();
						}
						share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
						startActivity(Intent.createChooser(share, "Share Image"));
					}
					
				}
			});
             
             return p2pView;
     }

}
