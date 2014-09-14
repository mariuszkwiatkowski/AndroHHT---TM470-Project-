package com.kwiatkowski.androhht.androhht;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;


/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */
public class Qty_adjuster extends Fragment implements Button.OnClickListener {

    private static final String ITEM_UNIQUE_ID = "unique_id";
    private static final String ITEM_BARCODE = "ean13";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_QUANTITY = "quantity";
    public String payload;
    EditText edit_qty_adjuster_x;


    private int unique_id;
    private String ean13;
    private String description;
    private String quantity;


    private QtyAdjusterListener mListener;


    public static Qty_adjuster newInstance(int ITEM_UNIQUE_ID_PARAM, String ITEM_BARCODE_PARAM, String ITEM_DESCRIPTION_PARAM, String ITEM_QUANTITY_PARAM) {


        Qty_adjuster fragment = new Qty_adjuster();
        Bundle args_adjuster = new Bundle();
        args_adjuster.putInt(ITEM_UNIQUE_ID, ITEM_UNIQUE_ID_PARAM);
        args_adjuster.putString(ITEM_BARCODE, ITEM_BARCODE_PARAM);
        args_adjuster.putString(ITEM_DESCRIPTION, ITEM_DESCRIPTION_PARAM);
        args_adjuster.putString(ITEM_QUANTITY, ITEM_QUANTITY_PARAM);
        fragment.setArguments(args_adjuster);
        return fragment;
    }
    public Qty_adjuster() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unique_id = getArguments().getInt(ITEM_UNIQUE_ID);
            ean13 = getArguments().getString(ITEM_BARCODE);
            description = getArguments().getString(ITEM_DESCRIPTION);
            quantity = getArguments().getString(ITEM_QUANTITY);


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View adjuster_view = null;
        adjuster_view = inflater.inflate(R.layout.fragment_qty_adjuster, container, false);

        Button plus_one = (Button) adjuster_view.findViewById(R.id.btn_plus_one);
        plus_one.setOnClickListener(this);

        Button minus_one = (Button) adjuster_view.findViewById(R.id.btn_minus_one);
        minus_one.setOnClickListener(this);

        Button plus_ten = (Button) adjuster_view.findViewById(R.id.btn_plus_ten);
        plus_ten.setOnClickListener(this);

        Button minus_ten = (Button) adjuster_view.findViewById(R.id.btn_minus_ten);
        minus_ten.setOnClickListener(this);

        Button no_stock = (Button) adjuster_view.findViewById(R.id.btn_no_stock);
        no_stock.setOnClickListener(this);

        Button btn_discard = (Button) adjuster_view.findViewById(R.id.btn_discard_adj_qty);
        btn_discard.setOnClickListener(this);

        Button btn_accept = (Button) adjuster_view.findViewById(R.id.btn_accept_adj_qty);
        btn_accept.setOnClickListener(this);

        try {
            TextView item_barcode = (TextView) adjuster_view.findViewById(R.id.txt_barcode_product_id);
            item_barcode.setText(ean13);
            TextView item_description = (TextView) adjuster_view.findViewById(R.id.txt_product_description);
            item_description.setText(description);
            EditText edit_quantity = (EditText) adjuster_view.findViewById(R.id.edit_qty_adjuster);
            edit_quantity.setText(quantity);
            edit_quantity.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

            adjuster_view.setFocusableInTouchMode(true);
            adjuster_view.requestFocus();
            adjuster_view.setOnKeyListener( new View.OnKeyListener() {
                @Override
                public boolean onKey(View view, int i, KeyEvent keyEvent) {
                    int action = keyEvent.getAction();

                    Long current_adjuster_value;
                    edit_qty_adjuster_x = (EditText) getActivity().findViewById(R.id.edit_qty_adjuster);
                    current_adjuster_value = Long.parseLong(edit_qty_adjuster_x.getText().toString());
                    if (!current_adjuster_value.equals(quantity)) {
                        Button apply_changes = (Button) getActivity().findViewById(R.id.btn_accept_adj_qty);
                        apply_changes.setEnabled(true);
                    }

                    if (i == KeyEvent.KEYCODE_VOLUME_UP && action == KeyEvent.ACTION_DOWN) {
                        Log.i("Volume button", "up");

                        current_adjuster_value = current_adjuster_value + 1;
                        edit_qty_adjuster_x.setText(current_adjuster_value.toString());

                        return true;

                    } else if (i == KeyEvent.KEYCODE_VOLUME_DOWN && action == KeyEvent.ACTION_DOWN) {
                        Log.i("Volume button", "down");

                        current_adjuster_value = current_adjuster_value - 1;
                        edit_qty_adjuster_x.setText(current_adjuster_value.toString());

                        return true;
                    }



                return false;
            }
        });

        return adjuster_view;



    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.StockAdjustmentRequested(unique_id,payload);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (QtyAdjusterListener) activity;
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




    public interface QtyAdjusterListener {

        public void StockAdjustmentRequested(int unique_id, String payload);
    }





    @Override
    public void onClick(View view) {
        Long current_adjuster_value;

        edit_qty_adjuster_x = (EditText) getActivity().findViewById(R.id.edit_qty_adjuster);
        current_adjuster_value = Long.parseLong(edit_qty_adjuster_x.getText().toString());

        if (!current_adjuster_value.equals(quantity)) {
            Button apply_changes = (Button) getActivity().findViewById(R.id.btn_accept_adj_qty);
            apply_changes.setEnabled(true);
        }


        switch (view.getId()) {
            case R.id.btn_plus_one:

                current_adjuster_value = current_adjuster_value + 1;
                edit_qty_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_minus_one:

                current_adjuster_value = current_adjuster_value - 1;
                edit_qty_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_plus_ten:

                current_adjuster_value = current_adjuster_value + 10;
                edit_qty_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_minus_ten:

                current_adjuster_value = current_adjuster_value - 10;
                edit_qty_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_no_stock:

                current_adjuster_value = 0L;
                edit_qty_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_discard_adj_qty:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();



                break;
            case R.id.btn_accept_adj_qty:
                payload = XMLQtyAdjust(current_adjuster_value);
                mListener.StockAdjustmentRequested(unique_id, payload);


                break;
        }
    }

    private String XMLQtyAdjust(Long new_quantity) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter str_writer = new StringWriter();
        try {
            serializer.setOutput(str_writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "magento_api");
            serializer.startTag("", "qty");
            serializer.text(Long.toString(new_quantity));
            serializer.endTag("", "qty");


            serializer.startTag("", "is_in_stock");
            serializer.text(new_quantity > 0 ? "1" : "0");
            serializer.endTag("", "is_in_stock");


            serializer.endTag("", "magento_api");
            serializer.endDocument();

            Log.i("XML_OUTPUT", str_writer.toString());
            //Toast.makeText(getActivity(), str_writer.toString(), Toast.LENGTH_SHORT).show();


            return str_writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }





}
