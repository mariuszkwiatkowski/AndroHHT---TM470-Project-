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
import android.widget.Toast;

import com.kwiatkowski.androhht.androhht.util.CryptoUtil;
import com.kwiatkowski.androhht.androhht.util.DateConverter;

import java.sql.SQLException;
import java.text.ParseException;


public class PasswordDialog extends DialogFragment {

    DatabaseManager dbManager;




    public interface PasswordDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
        public void onPasswordCheckComplete(boolean passCheck, int profileID, int request_type, String password_input);
    }


    PasswordDialogListener mListener;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {

            mListener = (PasswordDialogListener) activity;
        } catch (ClassCastException e) {

            throw new ClassCastException(activity.toString()
                    + " must implement PasswordDialogListener");
        }

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final int profileID = getArguments().getInt("profileID");
        final int request_type = getArguments().getInt("request_type");
        final View passwordView;


        dbManager = new DatabaseManager(getActivity());
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        passwordView = inflater.inflate(R.layout.password_dialog, null);

        builder.setView(passwordView);


        AlertDialog.Builder builder1 = builder.setMessage(String.format(getResources().getString(R.string.confirm_credentials)))
                .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        EditText password_input_field = (EditText) passwordView.findViewById(R.id.edit_dialog_password);
                        String password_input = password_input_field.getText().toString();

                        if (password_input != null && password_input.length() > 0) {

                            //retrieves cursor corresponding to the 'passcode' field in the database
                            Cursor dbCursor = dbManager.getPassword(profileID);
                            String password_db = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_PASSCODE));
                            try {
                                password_db = CryptoUtil.cryptoDecode(password_db, password_input);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            //Toast.makeText(getActivity(), password_input + " // " + password_db, Toast.LENGTH_LONG).show();
                            //returns the result to the activity
                            if (password_input.equals(password_db)) {

                                try {
                                    Log.i("date time", DateConverter.getCurrentDateTime());
                                    dbManager.updaleLastLogin(profileID, DateConverter.getCurrentDateTime());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                mListener.onPasswordCheckComplete(true, profileID, request_type, password_db);
                            } else {
                                mListener.onPasswordCheckComplete(false, profileID, request_type, null);
                            }
                        } else {
                            Toast.makeText(getActivity(), String.format(getResources().getString(R.string.must_provide_password)), Toast.LENGTH_SHORT).show();
                        }


                        PasswordDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        PasswordDialog.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public static PasswordDialog newInstance(int profileID, int request_type) {
        PasswordDialog passwordDialog = new PasswordDialog();

        Bundle args = new Bundle();
        args.putInt("profileID", profileID);
        args.putInt("request_type", request_type);
        passwordDialog.setArguments(args);

        return passwordDialog;
    }


    }
