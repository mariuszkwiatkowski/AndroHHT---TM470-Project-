package com.kwiatkowski.androhht.androhht;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */





public class Category_Add extends Fragment implements Button.OnClickListener {

    private static final String ITEM_UNIQUE_ID = "unique_id";
    private static final String ITEM_BARCODE = "ean13";
    private static final String ITEM_DESCRIPTION = "description";

    public Context mCtx;
    public String payload;



    private int unique_id;
    private String ean13;
    private String description;



    private Category_AddListener mListener;

    public static Category_Add newInstance(int ITEM_UNIQUE_ID_PARAM, String ITEM_BARCODE_PARAM, String ITEM_DESCRIPTION_PARAM, Context context) {


        Category_Add fragment = new Category_Add();
        fragment.mCtx = context;
        Bundle args_adjuster = new Bundle();
        args_adjuster.putInt(ITEM_UNIQUE_ID, ITEM_UNIQUE_ID_PARAM);
        args_adjuster.putString(ITEM_BARCODE, ITEM_BARCODE_PARAM);
        args_adjuster.putString(ITEM_DESCRIPTION, ITEM_DESCRIPTION_PARAM);
        fragment.setArguments(args_adjuster);
        return fragment;
    }
    public Category_Add() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unique_id = getArguments().getInt(ITEM_UNIQUE_ID);
            ean13 = getArguments().getString(ITEM_BARCODE);
            description = getArguments().getString(ITEM_DESCRIPTION);

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View category_add_view = null;
        category_add_view = inflater.inflate(R.layout.fragment_category_add, container, false);

        Button btn_discard = (Button) category_add_view.findViewById(R.id.btn_add_category_discard);
        btn_discard.setOnClickListener(this);

        Button btn_accept = (Button) category_add_view.findViewById(R.id.btn_add_category_accept);
        btn_accept.setOnClickListener(this);


        try {
            TextView item_barcode = (TextView) category_add_view.findViewById(R.id.txt_barcode_product_id);
            item_barcode.setText(ean13);
            TextView item_description = (TextView) category_add_view.findViewById(R.id.txt_product_description);
            item_description.setText(description);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return category_add_view;

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.CategoryAdditionRequested(unique_id, payload);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Category_AddListener) activity;
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


    public interface Category_AddListener {

        public void CategoryAdditionRequested(int unique_id, String payload);
    }





    @Override
    public void onClick(View view) {



        switch (view.getId()) {


            case R.id.btn_add_category_discard:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();

                break;
            case R.id.btn_add_category_accept:

                EditText edt_category = (EditText) getActivity().findViewById(R.id.edt_category_id);
                String category_text = edt_category.getText().toString();
                if (!category_text.equals("")) {
                    payload = XMLAssignToCategory(category_text);
                    Log.i("encoded image", payload);

                    mListener.CategoryAdditionRequested(unique_id, payload);
                }




                break;
        }
    }




    private String XMLAssignToCategory(String category) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter str_writer = new StringWriter();
        try {
            serializer.setOutput(str_writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "magento_api");
            serializer.startTag("", "category_id");
            serializer.text(category);
            serializer.endTag("", "category_id");


            serializer.endTag("", "magento_api");
            serializer.endDocument();

            Log.i("XML_OUTPUT", str_writer.toString());

            return str_writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
