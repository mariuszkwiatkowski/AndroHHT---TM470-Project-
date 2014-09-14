package com.kwiatkowski.androhht.androhht;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kwiatkowski.androhht.androhht.util.MagentoApiService;
import com.kwiatkowski.androhht.androhht.util.MagentoXMLParser;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */

public class retrieveFullProduct extends AsyncTask<String, Void, Void> {
    public Context mContext;
    public ProgressDialog Dialog;
    public int requester;
    public String item_price = null;
    public basicProductInfo fullProduct = new basicProductInfo();
    public basicProductInfo single_product;

    List<basicProductInfo> retrieved_products_array = new ArrayList<basicProductInfo>();


    public retrieveFullProduct(Context context, retrieveFullProduct_interface resp_interface, int requester) {
        this.mContext = context;
        this.retrieveFullProduct_response = resp_interface;

    }



    public retrieveFullProduct_interface retrieveFullProduct_response = null;

    public interface  retrieveFullProduct_interface {
        void full_product_retrieved(int requester, basicProductInfo single_product);
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
        OAuthRequest request = new OAuthRequest(Verb.GET, api_url + "products/"+ Integer.toString(single_product.getEntityId()) );


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
            //item_price = magento_parser.parsePrice(stream);
            fullProduct = magento_parser.parseFullProduct(stream);


            single_product.setAttribute_set_id(fullProduct.getAttribute_set_id());
            single_product.setType_id(fullProduct.getType_id());
            single_product.setEnabled(fullProduct.isEnabled());
            single_product.setVisibility(fullProduct.getVisibility());
            single_product.setTax_class_id(fullProduct.getTax_class_id());
            single_product.setWeight(fullProduct.getWeight());
            single_product.setDesc(fullProduct.getDesc());
            single_product.setShort_desc(fullProduct.getShort_desc());
            single_product.setPrice(fullProduct.getPrice());

            //}



        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        Dialog.dismiss();

        retrieveFullProduct_response.full_product_retrieved(requester, single_product);
    }


}



