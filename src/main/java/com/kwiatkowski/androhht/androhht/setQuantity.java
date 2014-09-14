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


public class setQuantity extends AsyncTask<String, Void, Void> {
    public Context mContext;
    public ProgressDialog Dialog;
    public int requester;
    public String item_quantity = null;
    public int unique_id;
    public String payload;
    public int code_response;

    public setQuantity (Context context, setQuantity_interface set_qty_interface, int unique_id, String payload) {
        this.mContext = context;
        this.unique_id = unique_id;
        this.payload = payload;
        this.setQuantity_response = set_qty_interface;

    }



    public setQuantity_interface setQuantity_response = null;

    public interface  setQuantity_interface {
        void item_qty_set(int result_code);
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

        Log.i("TEST", token + " ]] " + token_secret + " ]] " + consumer_secret + " ]] " + consumer_key);

        OAuthService service = new ServiceBuilder()
                .provider(MagentoApiService.class)
                .apiKey(consumer_key)
                .apiSecret(consumer_secret)
                .build();

        Log.i("TEST INTEGER", Integer.toString(unique_id));
        OAuthRequest request = new OAuthRequest(Verb.PUT, api_url + "stockitems/"+ Integer.toString(unique_id) );

        request.addHeader("Accept","text/xml");
        request.addHeader("Content-Type","text/xml");

        request.addPayload(payload);
        Log.i("payload", payload);

        Token accessToken;
        accessToken = new Token(token, token_secret);
        service.signRequest(accessToken , request);


        try {


            Response response = request.send();
            code_response = response.getCode();
            Log.i("TEST", String.valueOf(code_response));




        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        Dialog.dismiss();

        setQuantity_response.item_qty_set(code_response);
    }


}



