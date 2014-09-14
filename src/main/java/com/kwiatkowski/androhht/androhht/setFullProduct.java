package com.kwiatkowski.androhht.androhht;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.kwiatkowski.androhht.androhht.util.MagentoApiService;

import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;


public class setFullProduct extends AsyncTask<String, Void, Void> {
    public Context mContext;
    public ProgressDialog Dialog;
    public int requester;
    public int unique_id;
    public String payload;
    public int code_response;
    public int product_action_type;

    public setFullProduct(Context context, setFullProduct_interface set_price_interface, int unique_id, String payload, int action_type) {
        this.mContext = context;
        this.unique_id = unique_id;
        this.payload = payload;
        this.setFullProduct_response = set_price_interface;
        this.product_action_type = action_type;

    }



    public setFullProduct_interface setFullProduct_response = null;

    public interface  setFullProduct_interface {
        void product_created_or_amended(int result_code, int action_type, int product_id);
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



        OAuthRequest request = new OAuthRequest(product_action_type == 0 ? Verb.POST : Verb.PUT, api_url + "products/" + (product_action_type == 0 ? "" : Integer.toString(unique_id)));

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

        setFullProduct_response.product_created_or_amended(code_response, product_action_type, unique_id);
    }


}



