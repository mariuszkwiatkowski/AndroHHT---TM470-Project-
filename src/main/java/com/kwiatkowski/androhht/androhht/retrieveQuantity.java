package com.kwiatkowski.androhht.androhht;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;


import com.kwiatkowski.androhht.androhht.util.MagentoApiService;
import com.kwiatkowski.androhht.androhht.util.MagentoXMLParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */

public class retrieveQuantity extends AsyncTask<String, Void, Void> {
    public Context mContext;
    public ProgressDialog Dialog;
    public int requester;
    public String item_quantity = null;
    public basicProductInfo single_product;

    List<basicProductInfo> retrieved_products_array = new ArrayList<basicProductInfo>();


    public retrieveQuantity (Context context, retrieveQuantity_interface resp_interface, int requester) {
        this.mContext = context;
        this.retrieveQuantity_response = resp_interface;

    }



    public retrieveQuantity_interface retrieveQuantity_response = null;

    public interface  retrieveQuantity_interface {
        void item_qty_retrieved(int requester, basicProductInfo single_product);
    }

    @Override
    protected void onPreExecute() {

        Dialog.setMessage("Please wait..");
        Dialog.show();

    }

    @Override
    protected Void doInBackground(String...reqParams) {


        String consumer_key, consumer_secret, token, token_secret, api_url;
        token = reqParams[0];
        token_secret = reqParams[1];
        consumer_key = reqParams[2];
        consumer_secret = reqParams[3];
        api_url = reqParams[4];



        Log.i("TEST", token + " ]] "+ token_secret + " ]] "+ consumer_secret + " ]] " + consumer_key);

        OAuthService service = new ServiceBuilder()
                .provider(MagentoApiService.class)
                .apiKey(consumer_key)
                .apiSecret(consumer_secret)
                .build();

        Log.i("TEST INTEGER", Integer.toString(single_product.getEntityId()));
        OAuthRequest request = new OAuthRequest(Verb.GET, api_url + "stockitems/"+ Integer.toString(single_product.getEntityId()) );


        request.addHeader("Accept","text/xml");

        Token accessToken;
        accessToken = new Token(token, token_secret);
        service.signRequest(accessToken , request);


        try {


            Response response = request.send();
            String xml_response = String.valueOf(response.getBody());
            Log.i("TEST", xml_response);



            MagentoXMLParser magento_parser = new MagentoXMLParser();
            InputStream stream = new ByteArrayInputStream(xml_response.getBytes("UTF_8"));
            item_quantity = magento_parser.parseQuantity(stream);
            Log.i("TEST", item_quantity);
            single_product.setStock_qty((long)Double.parseDouble(item_quantity));

            Log.i("TEST", item_quantity);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        Dialog.dismiss();

        retrieveQuantity_response.item_qty_retrieved(requester, single_product);
    }


}



