package com.kwiatkowski.androhht.androhht;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */



public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    public int requester;
    private Fragment mFragment;

    public DatePickerFragment(int req) {
        requester = req;
    };



    public interface DatePickerFragmentListener {
        public void DatePickerRetrieved(int requester, String date_selected);
    }

    DatePickerFragmentListener mListener;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
// Use the current date as the default date in the picker
        final Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);


// Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        String month2char = String.valueOf(month + 1);
        if (month2char.length() == 1) {
            month2char = "0" + month2char;
        }

        String date_selected = String.valueOf(year) + "-" + month2char + "-" + String.valueOf(day);
        Log.i("date set", date_selected);

        switch (requester) {
            case 0:
                try {
                    EditText edt_offer_from = (EditText) getActivity().findViewById(R.id.edit_offer_from);
                    edt_offer_from.setText(date_selected);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;

            case 1:

                try {
                    EditText edt_offer_to = (EditText) getActivity().findViewById(R.id.edit_offer_to);
                    edt_offer_to.setText(date_selected);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                break;

        }


    }



}