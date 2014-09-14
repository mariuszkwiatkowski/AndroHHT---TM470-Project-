package com.kwiatkowski.androhht.androhht;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


//NetHttpTransport is proffered on new devices
//import com.google.api.client.http.apache.ApacheHttpTransport;


import org.json.JSONArray;
import org.json.JSONObject;
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



    public class retrieveItemID extends AsyncTask<String, Void, Void> {
        public Context mContext;
        public ProgressDialog Dialog;
        public int searchType = 0; // 1 = by ean13, 2 = by name
        public List<basicProductInfo> products = null;
        //public String ean13 = null;
        public String search_name = null;
        public int requester;


        public retrieveItemID (Context context, retrieveItemID_interface resp_interface, int requester) {
            this.mContext = context;
            this.retrieveItemID_response = resp_interface;
            this.requester = requester;

        }


        public retrieveItemID_interface retrieveItemID_response = null;

        public interface  retrieveItemID_interface {
            void listOfItemsRetrieved(List<basicProductInfo> basic_product_info, int requester);
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


            OAuthRequest request = new OAuthRequest(Verb.GET, api_url + "products");

            if (searchType == 2) {

                request.addQuerystringParameter("filter[1][attribute]", "name");
                request.addQuerystringParameter("filter[1][like]", "%"+search_name+"%");

            }  else {

                request.addQuerystringParameter("filter[1][attribute]", "ean13");
                request.addQuerystringParameter("filter[1][like]", search_name);

            }

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
                products = magento_parser.parse(stream);
                Log.i("Size of products list", Integer.toString(products.size()));


                Log.i("TEST", "array completed");


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            Dialog.dismiss();

            retrieveItemID_response.listOfItemsRetrieved(products, requester);
        }

}



