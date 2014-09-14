package com.kwiatkowski.androhht.androhht;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.api.client.auth.oauth.AbstractOAuthGetToken;
import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethods;
//NetHttpTransport is proffered on new devices
//import com.google.api.client.http.apache.ApacheHttpTransport;

import com.google.api.client.http.javanet.NetHttpTransport;

import com.kwiatkowski.androhht.androhht.util.CacheCleaner;
import com.kwiatkowski.androhht.androhht.util.ConnectivityCheck;
import com.kwiatkowski.androhht.androhht.util.QueryStringParser;
import com.kwiatkowski.androhht.androhht.util.ReflectionUtil;

import java.io.IOException;
import java.lang.reflect.Field;


/**
 *
 * Original code by Davy De Waele (2011)
 *
 * De Waele, D. (2011) Improved Twitter OAuth for Android [Online]. Available at http://blog.doityourselfandroid.com/2011/08/08/improved-twitter-oauth-android/ (Accessed 12 June 2014)
 * De Waele, D. (2011) AndroidTwitterGoogleApiJavaClient [Source code]. Available at https://github.com/ddewaele/AndroidTwitterGoogleApiJavaClient (Accessed 10 June 2014)
 *
 */
@SuppressLint("SetJavaScriptEnabled")
public class OAuthAccessTokenActivity extends Activity {
    public String intent_api_url;
    public String intent_api_key;
    public String intent_api_secret;


    private boolean userAuthorised = false;
    private String oauth_received_token = null;
    private String oauth_received_secret = null;



    final String TAG = "oauth";

    private boolean handled;



    public OAuthAccessTokenActivity() {};


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
            Toast.makeText(this,"No Internet connectivity!", Toast.LENGTH_SHORT).show();
            finish();
        };


        Intent fromProfile = getIntent();
        Bundle res = new Bundle();
        res = fromProfile.getExtras();
        intent_api_url = res.getString(Constants.API_URL_TAG);
        intent_api_url = intent_api_url.replace("api/rest/", "");
        intent_api_key = res.getString(Constants.API_KEY_TAG);;
        intent_api_secret = res.getString(Constants.API_SECRET_TAG);


        CacheCleaner.clearCache(this,0);

        Log.i(TAG, "Starting task to retrieve request token.");

    }

    private WebView webview;

    @Override
    protected void onResume() {
        super.onResume();

        CookieSyncManager.createInstance(this);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();


        webview = new WebView(this);
        WebSettings ws = webview.getSettings();
        ws.setAppCacheEnabled(false);
        ws.setJavaScriptEnabled(true);
        ws.setSaveFormData(false);
        webview.clearCache(true);
        webview.clearFormData();
        webview.clearHistory();


        webview.setVisibility(View.VISIBLE);
        setContentView(webview);

        handled=false;

                new PreProcessToken().execute();

            }

            private class PreProcessToken extends AsyncTask<Uri, Void, Void> {


                final OAuthHmacSigner signer = new OAuthHmacSigner();
                private String authorizationUrl;

                @Override
                protected Void doInBackground(Uri...params) {

                    try {

                        signer.clientSharedSecret = intent_api_secret;

                        OAuthGetTemporaryToken temporaryToken = new OAuthGetTemporaryToken(intent_api_url + Constants.REQUEST_URL);


                        Field field = AbstractOAuthGetToken.class.getDeclaredField("usePost");
                        field.setAccessible(true);
                        field.set(temporaryToken, true);

                        temporaryToken.transport = new NetHttpTransport();


                        temporaryToken.consumerKey = intent_api_key;
                        temporaryToken.callback = Constants.OAUTH_CALLBACK_URL;
                        temporaryToken.signer = signer;




                OAuthCredentialsResponse tempCredentials = temporaryToken.execute();

                field.set(temporaryToken, false);

                signer.tokenSharedSecret = tempCredentials.tokenSecret;

                OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(intent_api_url + Constants.AUTHORIZE_URL);
                authorizeUrl.temporaryToken = tempCredentials.token;
                authorizationUrl = authorizeUrl.build();

                Log.i(Constants.TAG, "Using authorizationUrl = " + authorizationUrl);

                handled=false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(Void result) {

            Log.i(TAG, "Retrieving request token from the server");

            webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url,Bitmap bitmap)  {
                    Log.i(Constants.TAG, "onPageStarted : " + url + " handled = " + handled);
                }
                @Override
                public void onPageFinished(final WebView view, final String url)  {
                    Log.i(Constants.TAG, "onPageFinished : " + url + " handled = " + handled);

                    if (url.startsWith(Constants.OAUTH_CALLBACK_URL)) {
                        if (url.indexOf("oauth_token=")!=-1) {
                            webview.setVisibility(View.INVISIBLE);

                            if (!handled) {
                                new ProcessToken(url,signer).execute();
                            }
                        } else {
                            webview.setVisibility(View.VISIBLE);
                        }
                    }
                }

            });

            webview.loadUrl(authorizationUrl);

        }

    }

    private class ProcessToken extends AsyncTask<Uri, Void, Void> {

        String url;
        private OAuthHmacSigner signer;


        public ProcessToken(String url,OAuthHmacSigner signer) {
            this.url=url;
            this.signer = signer;

        }

        @Override
        protected Void doInBackground(Uri...params) {


            Log.i(Constants.TAG, "doInbackground called with url " + url);

            if (url.startsWith(Constants.OAUTH_CALLBACK_URL) && !handled) {
                try {

                    if (url.indexOf("oauth_token=")!=-1) {
                        handled=true;
                        String requestToken  = extractParamFromUrl(url,"oauth_token");
                        String verifier= extractParamFromUrl(url,"oauth_verifier");
                        Log.i(Constants.TAG, "request token + verifier: " + requestToken + " " + verifier);
                        signer.clientSharedSecret = intent_api_secret;

                        OAuthGetAccessToken accessToken = new OAuthGetAccessToken(intent_api_url + Constants.ACCESS_URL);

                        Field field = null;
                        try {
                            field = ReflectionUtil.getField(accessToken.getClass(), "usePost");
                        } catch (NoSuchFieldException e) {
                            e.printStackTrace();
                        }
                        ReflectionUtil.makeAccessible(field);
                        try {
                            field.setBoolean(accessToken, true);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        accessToken.transport = new NetHttpTransport();
                        accessToken.temporaryToken = requestToken;
                        accessToken.signer = signer;
                        accessToken.consumerKey = intent_api_key;
                        accessToken.verifier = verifier;
                        OAuthCredentialsResponse credentials = accessToken.execute();
                        signer.tokenSharedSecret = credentials.tokenSecret;


                        Log.i(Constants.TAG, "Token = " + credentials.token + "; Token secret = " + credentials.tokenSecret);

                        if (credentials.token != null && credentials.tokenSecret != null) {
                            oauth_received_token = credentials.token.toString();
                            oauth_received_secret = credentials.tokenSecret.toString();
                            userAuthorised = true;

                        } else {
                            // un-used
                            userAuthorised = false;
                        }


                    } else if (url.indexOf("error=")!=-1) {
                        //unused
                    }




                } catch (IOException e) {
                    e.printStackTrace();
                }



            }
            return null;
        }

        private String extractParamFromUrl(String url,String paramName) {
            String queryString = url.substring(url.indexOf("?", 0)+1,url.length());
            QueryStringParser queryStringParser = new QueryStringParser(queryString);
            return queryStringParser.getQueryParamValue(paramName);
        }

        @Override
        protected void onPreExecute() {

        }


        @Override
        protected void onPostExecute(Void result) {
            Log.i(Constants.TAG," ++++++++++++ Starting mainscreen again");

            webview.clearCache(true);
            webview.clearFormData();
            webview.clearHistory();

            isAuthorised();
        }

    }

    public void isAuthorised (){
        Intent back_to_profile = this.getIntent();

        if (userAuthorised == true) {
            Bundle args = new Bundle();
            args.putString(Constants.TOKEN_RETURN_NAME, oauth_received_token);
            args.putString(Constants.TOKEN_SECRET_RETURN_NAME, oauth_received_secret);
            back_to_profile.putExtras(args);
            setResult(RESULT_OK, back_to_profile);

        } else {
            setResult(RESULT_CANCELED, back_to_profile);
        }
        finish();
    }

}
