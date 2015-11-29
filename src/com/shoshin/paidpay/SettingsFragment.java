package com.shoshin.paidpay;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SettingsFragment extends Fragment {
	
	
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
     	
     		View settingsView = inflater.inflate(
                     R.layout.fragment_settings, container, false);
             Button save = (Button)settingsView.findViewById(R.id.save);
             final EditText name = (EditText)settingsView.findViewById(R.id.name);
             final EditText email = (EditText)settingsView.findViewById(R.id.email);
             final EditText phone = (EditText)settingsView.findViewById(R.id.phonenumber);
             final EditText billing = (EditText)settingsView.findViewById(R.id.billing);
             final EditText shipping = (EditText)settingsView.findViewById(R.id.shipping);
             
             
             EditText ogpass = (EditText)settingsView.findViewById(R.id.orignalpass);
             EditText newpass = (EditText)settingsView.findViewById(R.id.newpass);
             EditText confirmpass = (EditText)settingsView.findViewById(R.id.new_confirm_pass);
             
             final SharedPreferences sp = ((AppController)getActivity().getApplication()).getInstance().prefs;
             name.setText(sp.getString("name", ""));
             email.setText(sp.getString("email", ""));
             phone.setText(sp.getString("phone", ""));
             billing.setText(sp.getString("billing", ""));
             shipping.setText(sp.getString("shipping", ""));
             
             save.setOnClickListener(new OnClickListener() {
				
				@SuppressLint("NewApi")
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Editor edit = sp.edit();
					edit.putString("name",name.getText().toString());
					edit.putString("email",email.getText().toString());
					edit.putString("phone",phone.getText().toString());
					edit.putString("billing",billing.getText().toString());
					edit.putString("shipping",shipping.getText().toString());
					edit.commit();
					edit.apply();
					
				}
			});
             return settingsView;
     }

}
