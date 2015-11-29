package com.shoshin.paidpay;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

import org.json.JSONException;
import org.json.JSONObject;




import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class CameraFragment extends Fragment{
	
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    View cameraView;
    JSONObject qrcode;
    ImageScanner scanner;
    
    static {
        System.loadLibrary("iconv");
    } 
	
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
		 
		 
		 
		 
		 

     	
     		cameraView = inflater.inflate(
                     R.layout.fragment_collection_object_camera, container, false);
     		
     		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     		
     		
     		scanner = new ImageScanner();
            
            
            
            ImageView settings = (ImageView)cameraView.findViewById(R.id.settings);
            ImageView wallets = (ImageView)cameraView.findViewById(R.id.wallets);
            ImageView history = (ImageView)cameraView.findViewById(R.id.history);
            ImageView request = (ImageView)cameraView.findViewById(R.id.request);
            
            settings.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Fragment newFragment = new SettingsFragment();
                    
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.replace(R.id.pagerparent, newFragment);
                    
                    transaction.addToBackStack(null);
                    
                    transaction.commit();
					
				}
			});
            
            
            request.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					Fragment newFragment = new P2pFragment();
                    
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                    transaction.replace(R.id.pagerparent, newFragment);
                    
                    transaction.addToBackStack(null);
                    
                    transaction.commit();
					
				}
			});
            
            wallets.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((MainActivity)getActivity()).mViewPager.setCurrentItem(2);
					
				}
			});
            
            history.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((MainActivity)getActivity()).mViewPager.setCurrentItem(0);
					
				}
			});

           

//            scanButton.setOnClickListener(new OnClickListener() {
//                    public void onClick(View v) {
//                        if (barcodeScanned) {
//                            barcodeScanned = false;
//                            scanText.setText("Scanning...");
//                            mCamera.setPreviewCallback(previewCb);
//                            mCamera.startPreview();
//                            previewing = true;
//                            mCamera.autoFocus(autoFocusCB);
//                        }
//                    }
//                });
            
            
            
            
            
            
            
            
     		
             
             return cameraView;
     }
	 
	 
	 @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		releaseCamera();
	}
	 
	 @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		

        /* Instance barcode scanner */
		mCamera = getCameraInstance();
		autoFocusHandler = new Handler();
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
        FrameLayout preview = (FrameLayout)cameraView.findViewById(R.id.cameraPreview);
        preview.addView(mPreview);
		Log.e("called","Yesss");
		
		barcodeScanned = false;
        mCamera.startPreview();
        previewing = true;
        mCamera.autoFocus(autoFocusCB);
        mCamera.setPreviewCallback(previewCb);
	}
	 
	 
	 public static Camera getCameraInstance(){
	        Camera c = null;
	        try {
	            c = Camera.open();
	        } catch (Exception e){
	        }
	        return c;
	    }
	 
	 
	 private void releaseCamera() {
	        if (mCamera != null) {
	            previewing = false;
	            mCamera.setPreviewCallback(null);
	            mPreview.getHolder().removeCallback(mPreview);
	            mCamera.release();
	            mCamera = null;
	        }
	    }

	    private Runnable doAutoFocus = new Runnable() {
	            public void run() {
	                if (previewing)
	                    mCamera.autoFocus(autoFocusCB);
	            }
	        };
	        
	        
	        
	        public void nfc(String val){
	        	
                barcodeScanned = true;
                Bundle forward = new Bundle();
                
                
                forward.putString("qrkey", val.split("~")[0]);
                forward.putString("qrtype", val.split("~")[1]);
                Fragment newFragment = null;
                if(forward.getString("qrtype").equalsIgnoreCase("1")){
                	newFragment = new TransactionFragment();
                }
                else{
                	newFragment = new PeerTransactionFragment();
                }
                
                
                newFragment.setArguments(forward);
                
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
                
                transaction.replace(R.id.pagerparent, newFragment);
                
                transaction.addToBackStack(null);	                       
                
                transaction.commit();
                
                
                new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        /* Create an Intent that will start the Menu-Activity. */
                    	if (barcodeScanned) {
                            barcodeScanned = false;
                            //mCamera.startPreview();
                            previewing = true;
                            mCamera.autoFocus(autoFocusCB);
                            mCamera.setPreviewCallback(previewCb);
                        }
                    }
                }, 1000);
                
	        }

	    PreviewCallback previewCb = new PreviewCallback() {
	            public void onPreviewFrame(byte[] data, Camera camera) {
	                Camera.Parameters parameters = camera.getParameters();
	                Size size = parameters.getPreviewSize();

	                Image barcode = new Image(size.width, size.height, "Y800");
	                barcode.setData(data);

	                int result = scanner.scanImage(barcode);
	                
	                if (result != 0) {
	                    previewing = false;
	                    mCamera.setPreviewCallback(null);
	                    mPreview.getHolder().removeCallback(mPreview);
	                    mCamera.stopPreview();
	                    
	                    SymbolSet syms = scanner.getResults();
	                    for (Symbol sym : syms) {
	                    	String val = sym.getData();
	                        //scanText.setText("barcode result " + sym.getData());
	                        barcodeScanned = true;
	                        Bundle forward = new Bundle();
	                        Log.e("Value",val);
	                        try{
	                        qrcode = new JSONObject(val);
	                        forward.putString("qr", qrcode.toString());
	                        forward.putString("qrkey", qrcode.getString("key"));
	                        forward.putString("qrtype", qrcode.getString("type"));
	                        }
	                        catch(Exception e){
	                        	Log.e("Damn","Damn");
	                        }
	                        
	                        
	                        Fragment newFragment = null;
	                        try {
								if(qrcode.getString("type").equalsIgnoreCase("1")){
									newFragment = new TransactionFragment();
								}
								else{
									newFragment = new PeerTransactionFragment();
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	                        //= new TransactionFragment();
	                        
	                        newFragment.setArguments(forward);
	                        
	                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
	                        transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
	                        
	                        transaction.replace(R.id.pagerparent, newFragment);
	                        
	                        transaction.addToBackStack(null);	                       
	                        
	                        transaction.commit();
	                        
	                        
	                        
	                        
	                    }
	                    new Handler().postDelayed(new Runnable(){
	                        @Override
	                        public void run() {
	                            /* Create an Intent that will start the Menu-Activity. */
	                        	try{
	                        	if (barcodeScanned) {
	                                barcodeScanned = false;
	                                mCamera.startPreview();
	                                previewing = true;
	                                mCamera.autoFocus(autoFocusCB);
	                                mCamera.setPreviewCallback(previewCb);
	                            }
	                        	}
	                        	catch(Exception e){
	                        		
	                        	}
	                        }
	                    }, 1000);
	                    
	                }
	            }
	        };

	    // Mimic continuous auto-focusing
	    AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
	            public void onAutoFocus(boolean success, Camera camera) {
	                autoFocusHandler.postDelayed(doAutoFocus, 1000);
	            }
	        };



		

}
