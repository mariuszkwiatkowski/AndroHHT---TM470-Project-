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


public class retrievePrice extends AsyncTask<String, Void, Void> {
    public Context mContext;
    public ProgressDialog Dialog;
    public int requester;
    public String item_price = null;
    public basicProductInfo fullPricing = new basicProductInfo();
    public basicProductInfo single_product;

    List<basicProductInfo> retrieved_products_array = new ArrayList<basicProductInfo>();


    public retrievePrice(Context context, retrievePrice_interface resp_interface, int requester) {
        this.mContext = context;
        this.retrievePrice_response = resp_interface;

    }



    public retrievePrice_interface retrievePrice_response = null;

    public interface  retrievePrice_interface {
        void item_price_retrieved(int requester, basicProductInfo single_product);
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
            fullPricing = magento_parser.parseFullPricing(stream);


            single_product.setPrice(fullPricing.getPrice());
            single_product.setSpecial_price(fullPricing.getSpecial_price());
            single_product.setSpecial_from(fullPricing.getSpecial_from());
            single_product.setSpecial_to(fullPricing.getSpecial_to());

            Log.i("TEST", Double.toString(single_product.getPrice()));

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        Dialog.dismiss();

        retrievePrice_response.item_price_retrieved(requester, single_product);
    }


}



