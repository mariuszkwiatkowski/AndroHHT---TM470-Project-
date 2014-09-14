package com.kwiatkowski.androhht.androhht;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.kwiatkowski.androhht.androhht.util.CryptoUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */


public class MultipleItemsFoundDialog extends DialogFragment  {
    public List<basicProductInfo> products = new ArrayList<basicProductInfo>();





    public interface MultipleItemsFoundDialogListener {

        public void item_selected_from_multiple(int requester, int item_selected);
    }


    MultipleItemsFoundDialogListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {

            mListener = (MultipleItemsFoundDialogListener) activity;
        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement MultipleItemsFoundDialogListener");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] product_names = getArguments().getStringArray("product_names");
        final int requester = getArguments().getInt("requester");






        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        builder = builder.setTitle(String.format(getResources().getString(R.string.multiple_items_found)))
                .setItems(product_names, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        mListener.item_selected_from_multiple(requester, i);
                    }
                });



        return builder.create();
    }

    public static MultipleItemsFoundDialog newInstance(List<basicProductInfo> products, int requester) {
        MultipleItemsFoundDialog multipleItemsFoundDialogDialog = new MultipleItemsFoundDialog();


        String[] list_of_product_names = new String[products.size()];
        for(int i=0; i < products.size(); i++) list_of_product_names[i] = products.get(i).getName() + " (" + products.get(i).getEAN() + ")";



        Bundle args = new Bundle();
        args.putInt("requester", requester);
        args.putStringArray("product_names", list_of_product_names);

        multipleItemsFoundDialogDialog.setArguments(args);

        return multipleItemsFoundDialogDialog;
    }


}

