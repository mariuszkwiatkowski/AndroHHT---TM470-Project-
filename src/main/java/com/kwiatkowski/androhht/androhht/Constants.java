package com.kwiatkowski.androhht.androhht;


/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */


public class Constants {
    public static final String TAG = "oauth";

    public static final String API_KEY_TAG = "api_key_tag";
    public static final String API_SECRET_TAG = "api_secret_tag";
    public static final String API_URL_TAG = "api_url_tag";
    public static final String REQUEST_URL = "oauth/initiate";
    public static final String ACCESS_URL = "oauth/token";
    public static final String AUTHORIZE_URL = "admin/oauth_authorize";




    public static final String	OAUTH_CALLBACK_URL		= "http://localhost/";


    public final static String MESSAGE_PROFILE_ID = "com.kwiatkowski.androhht.androhht.profile_id";
    public final static String MESSAGE_PROFILE_NAME = "com.kwiatkowski.androhht.androhht.profile_name";
    public final static String MESSAGE_API_ADDRESS = "com.kwiatkowski.androhht.androhht.api_address";
    public final static String MESSAGE_CONS_KEY = "com.kwiatkowski.androhht.androhht.cons_key";
    public final static String MESSAGE_CONS_SECRET = "com.kwiatkowski.androhht.androhht.cons_secret";
    public final static String MESSAGE_OAUTH_TOKEN = "com.kwiatkowski.androhht.androhht.oauth_token";
    public final static String MESSAGE_OAUTH_SECRET = "com.kwiatkowski.androhht.androhht.oauth_secret";
    public final static String MESSAGE_CURRENCY_ID = "com.kwiatkowski.androhht.androhht.currency_id";
    public final static String MESSAGE_LANGUAGE_ID = "com.kwiatkowski.androhht.androhht.language_id";


    public final static String MAGENTO_EAN_FIELD_NAME = "EAN";



    //---------Retrieve SKU constants ------//

    public static final String RETRIEVE_SKU_RECEIVER_FIELD_NAME = "retrieve_sku_receiver_field_name";

    //----- end Retrieve SKU constants  ----- //


    //---------Qty adjuster constants ------//

    public static final String ADJUSTER_PRODUCT_LOOKUP_TYPE_NAME = "adjuster_product_lookup_type_name";

    //----- Qty adjuster constants  ----- //


    public static final int BARCODE_SCANNER_REQ_CODE = 2000;
    public static final int PROFILE_SCANNER_REQ_CODE = 2020;
    public static final int ADD_PROFILE_REQ_CODE = 3000;
    public static final int ADD_PROFILE_OAUTH_REQ_CODE = 5000;
    public static final int ADD_IMAGE_FROM_GALLERY_REQ_CODE = 6000;

    public static final String TOKEN_RETURN_NAME = "token_returned";
    public static final String TOKEN_SECRET_RETURN_NAME = "token_secret_returned";


}
