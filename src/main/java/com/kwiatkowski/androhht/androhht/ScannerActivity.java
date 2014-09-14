package com.kwiatkowski.androhht.androhht;

/**
 *
 * Original code by Dushyanth Maguluru (2014)
 *
 * Maguluru, D. (2014) Barcode Scanner Libraries for Android (2014). Available at https://github.com/dm77/barcodescanner (Accessed 13 April 2014)
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    public String scan_text_result;
    public String scan_format;


    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";

    private boolean mFlash;
    private boolean mAutoFocus;



    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);


        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
        } else {
            mFlash = false;
            mAutoFocus = true;

        }


        mScannerView = new ZXingScannerView(this);   // Programmatically initialize the scanner view
        setContentView(mScannerView);                // Set the scanner view as the content view

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);

    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }


    public void handleResult(Result rawResult) {
        //


        Intent intent = new Intent();
        if (rawResult != null) {
            scan_text_result = rawResult.getText();
            scan_format = String.valueOf(rawResult.getBarcodeFormat());
            intent.putExtra("scan_text", scan_text_result);
            intent.putExtra("scan_format", scan_format);
            setResult(RESULT_OK, intent);
            Log.i("TAG", scan_text_result);
            Log.i("TAG", scan_format);

        } else {
            setResult(RESULT_CANCELED, intent);
        }


        finish();


    }

}
