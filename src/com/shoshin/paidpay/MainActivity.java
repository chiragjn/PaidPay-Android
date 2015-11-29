package com.shoshin.paidpay;


import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

public class MainActivity extends AppCompatActivity {
	
	

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    CollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    NfcAdapter mAdapter;
    PendingIntent mPendingIntent;

    @SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
    	
    	
    	
    	
        super.onCreate(savedInstanceState);
        
        
        
        
        mAdapter = NfcAdapter.getDefaultAdapter(this);   
        if (mAdapter == null) {
            //nfc not support your device.
            return;
        }
        mPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
                getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        
        
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Log.e("GCM","Done");
                } else {
                	Log.e("GCM","Not Done");
                }
            }
        };
        

        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(MainActivity.this, RegistrationIntentService.class);
            startService(intent);
        }
        
        final SharedPreferences sp = ((AppController)getApplication()).getInstance().prefs;
        Editor edit = sp.edit();
		edit.putString("name","John Cena");
		edit.putString("email","john@cena.com");
		edit.putString("phone","9930112199");
		edit.putString("billing","355 Avenue, 34th Floor, Colaba, Mumbai - 400054");
		edit.putString("shipping","355 Avenue, 34th Floor, Colaba, Mumbai - 400054");
		edit.putString("id","1");
		edit.commit();
		edit.apply();
        
        setContentView(R.layout.splash_layout);
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
            	setContentView(R.layout.activity_main);
                
                //Real Main
                mDemoCollectionPagerAdapter =
                        new CollectionPagerAdapter(
                                getSupportFragmentManager());
                mViewPager = (ViewPager) findViewById(R.id.pager);
                mViewPager.setAdapter(mDemoCollectionPagerAdapter);
                mViewPager.setCurrentItem(1);//Setting camera as default page
            }
        }, 700);
        
        
        
        
        
    }
    
    
    @Override
    protected void onNewIntent(Intent intent){    
        getTagInfo(intent);
        }
    
    
    private void getTagInfo(Intent intent) {
    Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
    
    AsyncTask<Tag, Void, String> reader = new AsyncTask<Tag, Void, String>() {

		
			 
		    @Override
		    protected String doInBackground(Tag... params) {
		        Tag tag = params[0];
		         
		        Ndef ndef = Ndef.get(tag);
		        if (ndef == null) {
		            // NDEF is not supported by this Tag. 
		            return null;
		        }
		 
		        NdefMessage ndefMessage = ndef.getCachedNdefMessage();
		 
		        NdefRecord[] records = ndefMessage.getRecords();
		        for (NdefRecord ndefRecord : records) {
		            if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
		                try {
		                    return readText(ndefRecord);
		                } catch (UnsupportedEncodingException e) {
		                    Log.e("nfc", "Unsupported Encoding", e);
		                }
		            }
		        }
		 
		        return null;
		    }
		     
		    private String readText(NdefRecord record) throws UnsupportedEncodingException {
		        /*
		         * See NFC forum specification for "Text Record Type Definition" at 3.2.1 
		         * 
		         * http://www.nfc-forum.org/specs/
		         * 
		         * bit_7 defines encoding
		         * bit_6 reserved for future use, must be 0
		         * bit_5..0 length of IANA language code
		         */
		 
		        byte[] payload = record.getPayload();
		 
		        // Get the Text Encoding
		        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
		 
		        // Get the Language Code
		        int languageCodeLength = payload[0] & 0063;
		         
		        // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
		        // e.g. "en"
		         
		        // Get the Text
		        return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
		    }
		    
		    
		 protected void onPostExecute(String result) {
			 
			 Log.e("NFCCCC","Result is : "+result);
			 Fragment newFragment = new TransactionFragment();
             Bundle forward = new Bundle();
             forward.putString("qrkey", result.split("~")[0]);
             forward.putString("qrtype", result.split("~")[1]);
             newFragment.setArguments(forward);
             
             FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
             transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
             
             transaction.replace(R.id.pagerparent, newFragment);
             
             transaction.addToBackStack(null);	                       
             
             transaction.commit();
			 
		 };
    	
	}.execute(tag);
    
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
        mAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
        if (mAdapter != null) {
            mAdapter.disableForegroundDispatch(this);
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    
    
    
    
    public class CollectionPagerAdapter extends FragmentStatePagerAdapter {
        public CollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	
        	Fragment fragment = null;
        	if(i==0)	fragment = new HistoryFragment();
        	if(i==1)	fragment = new CameraFragment();
        	if(i==2)	fragment = new WalletFragment();
        	
            
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }


    }
    
    
    
    
    
}