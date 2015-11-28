package com.shoshin.paidpay;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

public class CameraFragment extends Fragment {
	
	private boolean barcodeScanned = false;
	private boolean previewing = true;
	private Camera mCamera;
    private CameraPreview mPreview;
    private Handler autoFocusHandler;

    TextView scanText;
    Button scanButton;

    ImageScanner scanner;
    
    static {
        System.loadLibrary("iconv");
    } 
	
	 @Override
     public View onCreateView(LayoutInflater inflater,
             ViewGroup container, Bundle savedInstanceState){
     	
     		View cameraView = inflater.inflate(
                     R.layout.fragment_collection_object_camera, container, false);
     		
     		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
     		
     		
     		autoFocusHandler = new Handler();
            mCamera = getCameraInstance();

            /* Instance barcode scanner */
            scanner = new ImageScanner();
            scanner.setConfig(0, Config.X_DENSITY, 3);
            scanner.setConfig(0, Config.Y_DENSITY, 3);

            mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
            FrameLayout preview = (FrameLayout)cameraView.findViewById(R.id.cameraPreview);
            preview.addView(mPreview);

            scanText = (TextView)cameraView.findViewById(R.id.scanText);

            scanButton = (Button)cameraView.findViewById(R.id.ScanButton);

            scanButton.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (barcodeScanned) {
                            barcodeScanned = false;
                            scanText.setText("Scanning...");
                            mCamera.setPreviewCallback(previewCb);
                            mCamera.startPreview();
                            previewing = true;
                            mCamera.autoFocus(autoFocusCB);
                        }
                    }
                });
     		
             
             return cameraView;
     }
	 
	 
	 @Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		releaseCamera();
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
	                    mCamera.stopPreview();
	                    
	                    SymbolSet syms = scanner.getResults();
	                    for (Symbol sym : syms) {
	                        scanText.setText("barcode result " + sym.getData());
	                        barcodeScanned = true;
	                        Log.e("1","1");
	                        Fragment newFragment = new WalletFragment();
	                        Log.e("1","1");
	                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
	                        Log.e("1","1");
	                        transaction.replace(R.id.parr, newFragment);
	                        Log.e("1","1");
	                        transaction.addToBackStack(null);
	                        Log.e("1","1");
	                        transaction.commit();
	                        Log.e("1","1");
	                        
	                    }
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
