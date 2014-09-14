package com.kwiatkowski.androhht.androhht;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import com.kwiatkowski.androhht.androhht.util.DateConverter;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Calendar;

;import static com.kwiatkowski.androhht.androhht.util.DateConverter.DateWithoutTime;
import static com.kwiatkowski.androhht.androhht.util.DateConverter.toDateWithTime;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */



public class SpecialPrice_Adjuster extends Fragment implements Button.OnClickListener, DatePickerFragment.DatePickerFragmentListener {



    private static final String ITEM_UNIQUE_ID = "unique_id";
    private static final String ITEM_BARCODE = "ean13";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_PRICE = "price_original";
    private static final String ITEM_SPECIAL_PRICE = "price_special";
    private static final String ITEM_SPECIAL_FROM = "special_from";
    private static final String ITEM_SPECIAL_TO = "special_to";
    private static final int FROM_REQUESTER = 0;
    private static final int TO_REQUESTER = 1;

    public String payload_price;
    EditText edit_price_adjuster_x;


    private int unique_id;
    private String ean13;
    private String description;
    private String price;
    private String special_price;
    private String special_from;
    private String special_to;


    private PriceAdjusterListener mListener;


    public static SpecialPrice_Adjuster newInstance(int item_unique_id, String item_barcode, String item_description, String item_price, String item_special_price, String item_special_from, String item_special_to) {


        SpecialPrice_Adjuster fragment = new SpecialPrice_Adjuster();
        Bundle args_adjuster = new Bundle();
        args_adjuster.putInt(ITEM_UNIQUE_ID, item_unique_id);
        args_adjuster.putString(ITEM_BARCODE, item_barcode);
        args_adjuster.putString(ITEM_DESCRIPTION, item_description);
        args_adjuster.putString(ITEM_PRICE, item_price);
        args_adjuster.putString(ITEM_SPECIAL_PRICE, item_special_price);
        args_adjuster.putString(ITEM_SPECIAL_FROM, item_special_from);
        args_adjuster.putString(ITEM_SPECIAL_TO, item_special_to);
        fragment.setArguments(args_adjuster);
        Log.i("oncreteview", "price adjuste new instancer");
        return fragment;
    }
    public SpecialPrice_Adjuster() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            unique_id = getArguments().getInt(ITEM_UNIQUE_ID);
            ean13 = getArguments().getString(ITEM_BARCODE);
            description = getArguments().getString(ITEM_DESCRIPTION);
            price = getArguments().getString(ITEM_PRICE);
            special_price = getArguments().getString(ITEM_SPECIAL_PRICE);
            try {
                special_from = DateWithoutTime(getArguments().getString(ITEM_SPECIAL_FROM));
            } catch (ParseException e) {
                e.printStackTrace();
                special_from = "1900-01-01";
            }
            try {
                special_to = DateWithoutTime(getArguments().getString(ITEM_SPECIAL_TO));
            } catch (ParseException e) {
                e.printStackTrace();
                special_to = "1900-01-01";
            }

            Log.i("params", price + " . " + special_price + " . " + special_from + special_to);

        }

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("oncreteview", "price adjuster2");
        View adjuster_view = null;
        adjuster_view = inflater.inflate(R.layout.fragment_special_price_adjuster, container, false);

        Button plus_one_percent = (Button) adjuster_view.findViewById(R.id.btn_plus_one_percent);
        plus_one_percent.setOnClickListener(this);

        Button minus_one_percent = (Button) adjuster_view.findViewById(R.id.btn_minus_one_percent);
        minus_one_percent.setOnClickListener(this);

        Button plus_five_percent = (Button) adjuster_view.findViewById(R.id.btn_plus_five_percent);
        plus_five_percent.setOnClickListener(this);

        Button minus_five_percent = (Button) adjuster_view.findViewById(R.id.btn_minus_five_percent);
        minus_five_percent.setOnClickListener(this);

        Button round_99 = (Button) adjuster_view.findViewById(R.id.btn_round_to_99);
        round_99.setOnClickListener(this);

        Button round_00 = (Button) adjuster_view.findViewById(R.id.btn_round_to_00);
        round_00.setOnClickListener(this);

        Button btn_discard = (Button) adjuster_view.findViewById(R.id.btn_discard_adj_price);
        btn_discard.setOnClickListener(this);

        Button btn_accept = (Button) adjuster_view.findViewById(R.id.btn_accept_adj_price);
        btn_accept.setOnClickListener(this);

        try {
            TextView item_barcode = (TextView) adjuster_view.findViewById(R.id.txt_barcode_product_id);
            item_barcode.setText(ean13);
            TextView item_description = (TextView) adjuster_view.findViewById(R.id.txt_product_description);
            item_description.setText(description);
            EditText edit_special_price = (EditText) adjuster_view.findViewById(R.id.edit_special_price);
            edit_special_price.setText(special_price);
            edit_special_price.setOnClickListener(this);
            EditText edt_offer_from = (EditText) adjuster_view.findViewById(R.id.edit_offer_from);
            edt_offer_from.setText(special_from);
            edt_offer_from.setOnClickListener(this);
            edt_offer_from.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {

                        DialogFragment date_picker = new DatePickerFragment(FROM_REQUESTER);
                        date_picker.show(getFragmentManager(), "datePicker");
                    }
                }
            });

            edt_offer_from.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment date_picker = new DatePickerFragment(FROM_REQUESTER);
                    date_picker.show(getFragmentManager(), "datePicker");
                }
            });
            EditText edt_offer_to = (EditText) adjuster_view.findViewById(R.id.edit_offer_to);
            edt_offer_to.setText(special_to);
            edt_offer_to.setOnClickListener(this);
            edt_offer_to.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean b) {
                    if (b) {

                        DialogFragment date_picker = new DatePickerFragment(TO_REQUESTER);
                        date_picker.show(getFragmentManager(), "datePicker");
                    }
                }
            });
            edt_offer_to.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogFragment date_picker = new DatePickerFragment(TO_REQUESTER);
                    date_picker.show(getFragmentManager(), "datePicker");
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


        Switch price_mode = (Switch) adjuster_view.findViewById(R.id.switch_price_mode);
        price_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                edit_price_adjuster_x = (EditText) getActivity().findViewById(R.id.edit_special_price);
                TextView starting_on = (TextView) getActivity().findViewById(R.id.txt_offer_starting_on);
                TextView finishing_on = (TextView) getActivity().findViewById(R.id.txt_offer_finishing_on);
                EditText edt_offer_from = (EditText) getActivity().findViewById(R.id.edit_offer_from);
                EditText edt_offer_to = (EditText) getActivity().findViewById(R.id.edit_offer_to);

                if (b) {
                    starting_on.setVisibility(View.VISIBLE);
                    finishing_on.setVisibility(View.VISIBLE);
                    edt_offer_from.setVisibility(View.VISIBLE);
                    edt_offer_to.setVisibility(View.VISIBLE);
                    edit_price_adjuster_x.setText(special_price);
                } else {
                    starting_on.setVisibility(View.GONE);
                    finishing_on.setVisibility(View.GONE);
                    edt_offer_from.setVisibility(View.GONE);
                    edt_offer_to.setVisibility(View.GONE);
                    edit_price_adjuster_x.setText(price);
                }

            }
        });



        adjuster_view.setFocusableInTouchMode(true);
        adjuster_view.requestFocus();
        adjuster_view.setOnKeyListener( new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int action = keyEvent.getAction();

                Double current_adjuster_value;
                Double new_adjuster_value;
                edit_price_adjuster_x = (EditText) getActivity().findViewById(R.id.edit_special_price);
                current_adjuster_value = Double.parseDouble(edit_price_adjuster_x.getText().toString());
                if (!current_adjuster_value.equals(Double.parseDouble(price))) {
                    Button apply_changes = (Button) getActivity().findViewById(R.id.btn_accept_adj_price);
                    apply_changes.setEnabled(true);
                }

                if (i == KeyEvent.KEYCODE_VOLUME_UP && action == KeyEvent.ACTION_DOWN) {
                    Log.i("Volume button", "up");

                    new_adjuster_value = roundTwoDecimals(current_adjuster_value * 1.01);
                    if (new_adjuster_value.equals(current_adjuster_value)) {
                        new_adjuster_value = new_adjuster_value + 0.01;
                    }
                    edit_price_adjuster_x.setText(new_adjuster_value.toString());
                    Button apply_changes = (Button) getActivity().findViewById(R.id.btn_accept_adj_price);
                    apply_changes.setEnabled(true);

                    return true;

                } else if (i == KeyEvent.KEYCODE_VOLUME_DOWN && action == KeyEvent.ACTION_DOWN) {
                    Log.i("Volume button", "down");

                    new_adjuster_value = roundTwoDecimals(current_adjuster_value * 0.99);
                    if (new_adjuster_value.equals(current_adjuster_value)) {
                        new_adjuster_value = new_adjuster_value - 0.01;
                    }
                    edit_price_adjuster_x.setText(new_adjuster_value.toString());

                    Button apply_changes = (Button) getActivity().findViewById(R.id.btn_accept_adj_price);
                    apply_changes.setEnabled(true);

                    return true;
                }



                return false;
            }
        });

        return adjuster_view;


    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.PriceAdjustmentRequested(unique_id, payload_price);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (PriceAdjusterListener) activity;
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
    public void DatePickerRetrieved(int requester, String date_selected) {
        switch (requester) {
            case FROM_REQUESTER:
                EditText edt_offer_from = (EditText) getActivity().findViewById(R.id.edit_offer_from);
                edt_offer_from.setText(date_selected);
                break;
            case TO_REQUESTER:
                EditText edt_offer_to = (EditText) getActivity().findViewById(R.id.edit_offer_from);
                edt_offer_to.setText(date_selected);
                break;
        }
    }



    public interface PriceAdjusterListener {

        public void PriceAdjustmentRequested(int unique_id, String payload);
    }


    public double roundNoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return Double.valueOf(twoDForm.format(d));
    }

    public double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return (Double.valueOf(twoDForm.format(d)));
    }


    public double round99(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#");
        return (Double.valueOf(twoDForm.format(d))-0.01);
    }



    @Override
    public void onClick(View view) {
        Double current_adjuster_value;


        edit_price_adjuster_x = (EditText) getActivity().findViewById(R.id.edit_special_price);
        current_adjuster_value = Double.parseDouble(edit_price_adjuster_x.getText().toString());

        if (!current_adjuster_value.equals(price)) {
            Button apply_changes = (Button) getActivity().findViewById(R.id.btn_accept_adj_price);
            apply_changes.setEnabled(true);
        }


        switch (view.getId()) {
            case R.id.btn_plus_one_percent:

                current_adjuster_value = roundTwoDecimals(current_adjuster_value * 1.01);
                edit_price_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_minus_one_percent:

                current_adjuster_value = roundTwoDecimals(current_adjuster_value * 0.99);
                edit_price_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_plus_five_percent:

                current_adjuster_value = roundTwoDecimals(current_adjuster_value * 1.05);
                edit_price_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_minus_five_percent:

                current_adjuster_value = roundTwoDecimals(current_adjuster_value * 0.95);
                edit_price_adjuster_x.setText(current_adjuster_value.toString());
                break;
            case R.id.btn_round_to_00:

                current_adjuster_value = roundNoDecimals(current_adjuster_value);
                edit_price_adjuster_x.setText(current_adjuster_value.toString());
                break;

            case R.id.btn_round_to_99:

                current_adjuster_value = round99(current_adjuster_value);
                edit_price_adjuster_x.setText(current_adjuster_value.toString());
                break;



            case R.id.btn_discard_adj_price:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();



                break;
            case R.id.btn_accept_adj_price:


                int price_change_type = 0;
                EditText from_date = (EditText) getActivity().findViewById(R.id.edit_offer_from);
                EditText to_date = (EditText) getActivity().findViewById(R.id.edit_offer_to);
                Switch price_mode = (Switch) getActivity().findViewById(R.id.switch_price_mode);

                String from_date_text = from_date.getText().toString();
                try {
                    from_date_text = toDateWithTime(from_date_text);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String to_date_text = to_date.getText().toString();
                try {
                    to_date_text = toDateWithTime(to_date_text);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Log.i("out dates", from_date_text + to_date_text);

                if (!price_mode.isChecked()) {
                    price_change_type = 1;
                }
                payload_price = XMLPriceAdjust(price_change_type, current_adjuster_value, from_date_text, to_date_text);
                mListener.PriceAdjustmentRequested(unique_id, payload_price);


                break;
        }
    }

    private String XMLPriceAdjust(int type, Double new_price, String from_date, String to_date) {
        //type 0 = special, 1 = normal
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter str_writer = new StringWriter();
        try {
            serializer.setOutput(str_writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "magento_api");
            if (type == 0) {
                serializer.startTag("", "special_price");
                serializer.text(Double.toString(new_price));
                serializer.endTag("", "special_price");
                serializer.startTag("", "special_from_date");
                serializer.text(from_date);
                serializer.endTag("", "special_from_date");
                serializer.startTag("", "special_to_date");
                serializer.text(to_date);
                serializer.endTag("", "special_to_date");
            } else {
                serializer.startTag("", "price");
                serializer.text(Double.toString(new_price));
                serializer.endTag("", "price");
            }

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
