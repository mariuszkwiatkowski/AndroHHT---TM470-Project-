package com.kwiatkowski.androhht.androhht;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.zxing.Result;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */

public class Retrieve_SKU extends Fragment implements Button.OnClickListener {

    Button scan_Button;
    EditText scan_Barcode;

    private int retriever_code;


    public interface RetrieveSKUListener {
         public void SKU_Retrieved(int search_type, String query, int retriever_code);
    }


    RetrieveSKUListener mListener;


    public static Retrieve_SKU newInstance(int retriever_code) {
        Retrieve_SKU fragment = new Retrieve_SKU();
        Bundle args = new Bundle();
        args.putInt(Constants.RETRIEVE_SKU_RECEIVER_FIELD_NAME, retriever_code);

        fragment.setArguments(args);
        return fragment;
    }
    public Retrieve_SKU() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            retriever_code = getArguments().getInt(Constants.RETRIEVE_SKU_RECEIVER_FIELD_NAME);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = null;
        view = inflater.inflate(R.layout.fragment_retrieve_sku, container, false);
        scan_Button = (Button) view.findViewById(R.id.btn_Scan);
        scan_Button.setOnClickListener(this);

        Button search_by_id = (Button) view.findViewById(R.id.btn_search_by_id);
        search_by_id.setOnClickListener(this);

        Button retrieve_product = (Button) view.findViewById(R.id.btn_scan_retrieve);
        retrieve_product.setOnClickListener(this);

        return view;



    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (RetrieveSKUListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_Scan:
                Intent start_scanner = new Intent(getActivity(), ScannerActivity.class);
                Retrieve_SKU.this.startActivityForResult(start_scanner, Constants.BARCODE_SCANNER_REQ_CODE);
                break;
            case R.id.btn_scan_retrieve:
                String barcode_text;

                EditText barcode_edit_text = (EditText) getActivity().findViewById(R.id.edit_barcode_scan);
                barcode_text = barcode_edit_text.getText().toString();

                if ((barcode_edit_text.getText().length() > 0)) {

                    Toast.makeText(getActivity(), "Barcode OK", Toast.LENGTH_SHORT).show();
                    mListener.SKU_Retrieved(1, barcode_text, retriever_code);

                }

                break;

            case R.id.btn_search_by_id:
                String search_text;
                EditText search_edit_text = (EditText) getActivity().findViewById(R.id.edit_product_id);
                search_text = search_edit_text.getText().toString();
                if (search_text != null) {
                    mListener.SKU_Retrieved(2,search_text, retriever_code);
                }
                break;
        }
    }


    public interface OnFragmentInteractionListener {

        public void onFragmentInteraction(Uri uri);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == Constants.BARCODE_SCANNER_REQ_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                Log.d("TAG", data.getStringExtra("scan_text"));
                Toast.makeText(getActivity(), data.getStringExtra("scan_text"), Toast.LENGTH_SHORT).show();
                scan_Barcode = (EditText) getActivity().findViewById(R.id.edit_barcode_scan);
                scan_Barcode.setText(data.getStringExtra("scan_text"));
            }
        }


    }


}

