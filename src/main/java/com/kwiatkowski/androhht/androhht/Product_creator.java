package com.kwiatkowski.androhht.androhht;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.SwitchPreference;
import android.util.Log;
import android.util.Xml;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.xmlpull.v1.XmlSerializer;

import java.io.StringWriter;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */

public class Product_creator extends Fragment implements Button.OnClickListener {
    public int product_action_type = 0; //0 - create, 1 - amend
    public basicProductInfo product = new basicProductInfo();
    String payload;

    private ProductCreatorListener mListener;


    public static Product_creator newInstance(int action_type, basicProductInfo product_to_amend) {


        Product_creator fragment = new Product_creator(action_type, product_to_amend);


        return fragment;
    }
    public Product_creator(int action_type, basicProductInfo product_to_amend) {
        this.product_action_type = action_type;
        this.product = product_to_amend;
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View product_view = null;
        product_view = inflater.inflate(R.layout.fragment_product_edit, container, false);

        Button btn_discard = (Button) product_view.findViewById(R.id.btn_product_discard);
        btn_discard.setOnClickListener(this);

        Button btn_accept = (Button) product_view.findViewById(R.id.btn_product_accept);
        btn_accept.setOnClickListener(this);


        if (product_action_type == 1 && product != null) {
            EditText product_name = (EditText) product_view.findViewById(R.id.edit_product_name);
            EditText product_sku = (EditText) product_view.findViewById(R.id.edit_product_sku);
            EditText product_ean13 = (EditText) product_view.findViewById(R.id.edit_product_ean13);
            EditText product_price = (EditText) product_view.findViewById(R.id.edit_product_price);
            EditText product_weight = (EditText) product_view.findViewById(R.id.edit_product_weight);
            Switch product_status = (Switch) product_view.findViewById(R.id.switch_product_status);
            EditText product_visibility = (EditText) product_view.findViewById(R.id.edit_product_visibility);
            EditText product_tax_class_id = (EditText) product_view.findViewById(R.id.edit_product_tax_class);
            EditText product_attribute_set = (EditText) product_view.findViewById(R.id.edit_product_attribute_set);
            EditText product_desc = (EditText) product_view.findViewById(R.id.edit_product_description);
            EditText product_short_desc = (EditText) product_view.findViewById(R.id.edit_product_short_description);


            product_name.setText(product.getName());
            product_sku.setText(product.getSku());
            product_ean13.setText(product.getEAN());
            product_price.setText(String.valueOf(product.getPrice()));
            product_weight.setText(String.valueOf(product.getWeight()));
            product_status.setChecked(product.isEnabled());
            product_visibility.setText(String.valueOf(product.getVisibility()));
            product_tax_class_id.setText(String.valueOf(product.getTax_class_id()));
            product_attribute_set.setText(String.valueOf(product.getAttribute_set_id()));
            product_desc.setText(product.getDesc());
            product_short_desc.setText(product.getShort_desc());

        }



        return product_view;

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.ProductCreationOrUpdateRequested(product_action_type, payload, 0);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (ProductCreatorListener) activity;
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


    public interface ProductCreatorListener {

        public void ProductCreationOrUpdateRequested(int action_type, String payload, int entity_id);
    }


    @Override
    public void onClick(View view) {


        switch (view.getId()) {

            case R.id.btn_product_discard:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();

                break;
            case R.id.btn_product_accept:
                EditText product_name = (EditText) getActivity().findViewById(R.id.edit_product_name);
                EditText product_sku = (EditText) getActivity().findViewById(R.id.edit_product_sku);
                EditText product_ean13 = (EditText) getActivity().findViewById(R.id.edit_product_ean13);
                EditText product_price = (EditText) getActivity().findViewById(R.id.edit_product_price);
                EditText product_weight = (EditText) getActivity().findViewById(R.id.edit_product_weight);
                Switch product_status = (Switch) getActivity().findViewById(R.id.switch_product_status);
                EditText product_visibility = (EditText) getActivity().findViewById(R.id.edit_product_visibility);
                EditText product_tax_class_id = (EditText) getActivity().findViewById(R.id.edit_product_tax_class);
                EditText product_attribute_set = (EditText) getActivity().findViewById(R.id.edit_product_attribute_set);
                EditText product_desc = (EditText) getActivity().findViewById(R.id.edit_product_description);
                EditText product_short_desc = (EditText) getActivity().findViewById(R.id.edit_product_short_description);


                if (product == null) {
                    product = new basicProductInfo();
                    product.setType_id("simple");
                }





                product.setAttribute_set_id(Integer.parseInt(product_attribute_set.getText().toString()));

                product.setSku(product_sku.getText().toString());
                    product.setName(product_name.getText().toString());
                    product.setEAN(product_ean13.getText().toString());
                    product.setPrice(Double.parseDouble(product_price.getText().toString()));
                    product.setWeight(Double.parseDouble(product_weight.getText().toString()));
                    product.setEnabled(product_status.isChecked());
                    product.setVisibility(Integer.parseInt(product_visibility.getText().toString()));
                    product.setTax_class_id(Integer.parseInt(product_tax_class_id.getText().toString()));
                    product.setDesc(product_desc.getText().toString());
                    product.setShort_desc(product_short_desc.getText().toString());

                Log.i("desc",product.getDesc() );

                payload = XMLProductCreateOrUpdate(product_action_type, product);
                Log.i("new product payload", payload);
                mListener.ProductCreationOrUpdateRequested(product_action_type, payload, product_action_type == 0 ? 0 : product.getEntityId());


                break;
        }
    }

    private String XMLProductCreateOrUpdate(int action_type, basicProductInfo product) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter str_writer = new StringWriter();
        try {
            serializer.setOutput(str_writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "magento_api");

            serializer.startTag("", "attribute_set_id");
            serializer.text(String.valueOf(product.getAttribute_set_id()));
            serializer.endTag("", "attribute_set_id");

            if (action_type == 0) {

                serializer.startTag("", "type_id");
                serializer.text(product.getType_id());
                serializer.endTag("", "type_id");
            }

            serializer.startTag("", "sku");
            serializer.text(product.getSku());
            serializer.endTag("", "sku");

            serializer.startTag("", "name");
            serializer.text(product.getName());
            serializer.endTag("", "name");

            serializer.startTag("", "ean13");
            serializer.text(product.getEAN());
            serializer.endTag("", "ean13");

            serializer.startTag("", "price");
            serializer.text(String.valueOf(product.getPrice()));
            serializer.endTag("", "price");

            serializer.startTag("", "weight");
            serializer.text(String.valueOf(product.getWeight()));
            serializer.endTag("", "weight");

            serializer.startTag("", "status");
            serializer.text(product.isEnabled() == true ? "1":"0");
            serializer.endTag("", "status");

            serializer.startTag("", "visibility");
            serializer.text(String.valueOf(product.getVisibility()));
            serializer.endTag("", "visibility");

            serializer.startTag("", "tax_class_id");
            serializer.text(String.valueOf(product.getTax_class_id()));
            serializer.endTag("", "tax_class_id");

            serializer.startTag("", "description");
            serializer.text(product.getDesc());
            serializer.endTag("", "description");

            serializer.startTag("", "short_description");
            serializer.text(product.getShort_desc());
            serializer.endTag("", "short_description");

            serializer.endTag("", "magento_api");
            serializer.endDocument();

            Log.i("XML_OUTPUT", str_writer.toString());



            return str_writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
