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
import android.view.KeyEvent;
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


public class Image_Add extends Fragment implements Button.OnClickListener {

    private static final String ITEM_UNIQUE_ID = "unique_id";
    private static final String ITEM_BARCODE = "ean13";
    private static final String ITEM_DESCRIPTION = "description";

    public Context mCtx;
    public Bitmap selectedImage;
    public String payload;


    private int unique_id;
    private String ean13;
    private String description;


    private Image_AddListener mListener;


    public static Image_Add newInstance(int ITEM_UNIQUE_ID_PARAM, String ITEM_BARCODE_PARAM, String ITEM_DESCRIPTION_PARAM, Context context) {


        Image_Add fragment = new Image_Add();
        fragment.mCtx = context;
        Bundle args_adjuster = new Bundle();
        args_adjuster.putInt(ITEM_UNIQUE_ID, ITEM_UNIQUE_ID_PARAM);
        args_adjuster.putString(ITEM_BARCODE, ITEM_BARCODE_PARAM);
        args_adjuster.putString(ITEM_DESCRIPTION, ITEM_DESCRIPTION_PARAM);
        fragment.setArguments(args_adjuster);
        return fragment;
    }
    public Image_Add() {
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

        View image_add_view = null;
        image_add_view = inflater.inflate(R.layout.fragment_picture_add, container, false);

        Button btn_discard = (Button) image_add_view.findViewById(R.id.btn_add_picture_discard);
        btn_discard.setOnClickListener(this);

        Button btn_accept = (Button) image_add_view.findViewById(R.id.btn_add_picture_accept);
        btn_accept.setOnClickListener(this);

        ImageView imageView = (ImageView) image_add_view.findViewById(R.id.imageView_add_picture);
        imageView.setOnClickListener(this);

        try {
            TextView item_barcode = (TextView) image_add_view.findViewById(R.id.txt_barcode_product_id);
            item_barcode.setText(ean13);
            TextView item_description = (TextView) image_add_view.findViewById(R.id.txt_product_description);
            item_description.setText(description);

        } catch (Exception e) {
            e.printStackTrace();
        }



        return image_add_view;



    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.ImageAdditionRequested(unique_id, payload);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (Image_AddListener) activity;
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




    public interface Image_AddListener {
         public void ImageAdditionRequested(int unique_id, String payload);
    }


    @Override
    public void onClick(View view) {



        switch (view.getId()) {
            case R.id.imageView_add_picture:
                selectImageFromGallery(Constants.ADD_IMAGE_FROM_GALLERY_REQ_CODE);

                break;

            case R.id.btn_add_picture_discard:
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();

                break;
            case R.id.btn_add_picture_accept:
                ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
                selectedImage.compress(Bitmap.CompressFormat.JPEG,80,byteStream);
                byte[] byteArrayImage = byteStream.toByteArray();
                String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

                payload = XMLNewImage(encodedImage);
                Log.i("encoded image", payload);

                mListener.ImageAdditionRequested(unique_id, payload);


                break;
        }
    }


    public void selectImageFromGallery(int request_code) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, request_code);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.ADD_IMAGE_FROM_GALLERY_REQ_CODE && resultCode == Activity.RESULT_OK)

            try {
                ImageView imageView = (ImageView) getActivity().findViewById(R.id.imageView_add_picture);

                if (selectedImage != null) {
                    selectedImage.recycle();
                }



                Uri selectedImageUri = data.getData();
                String path = selectedImageUri.getPath();
                Log.i("path",path);
                InputStream stream = getActivity().getContentResolver().openInputStream(data.getData());
                selectedImage = BitmapFactory.decodeStream(stream);
                stream.close();
                imageView.setImageBitmap(selectedImage);



            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String XMLNewImage(String base64Image) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter str_writer = new StringWriter();
        try {
            serializer.setOutput(str_writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "magento_api");
            serializer.startTag("", "file_mime_type");
            serializer.text("image/jpeg");
            serializer.endTag("", "file_mime_type");

            serializer.startTag("", "file_content");
            serializer.text(base64Image);
            serializer.endTag("", "file_content");

            serializer.endTag("", "magento_api");
            serializer.endDocument();

            Log.i("XML_OUTPUT", str_writer.toString());


            return str_writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}
