package com.kwiatkowski.androhht.androhht;

import android.app.Activity;
import com.kwiatkowski.androhht.androhht.basicProductInfo;
import com.kwiatkowski.androhht.androhht.util.ConnectivityCheck;


import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.util.Xml;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlSerializer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;


public class MainMenu extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, retrieveQuantity.retrieveQuantity_interface, Retrieve_SKU.RetrieveSKUListener, Qty_adjuster.QtyAdjusterListener, setQuantity.setQuantity_interface, setSpecialPrice.setSpecialPrice_interface, retrieveItemID.retrieveItemID_interface, MultipleItemsFoundDialog.MultipleItemsFoundDialogListener, retrievePrice.retrievePrice_interface, SpecialPrice_Adjuster.PriceAdjusterListener, Product_creator.ProductCreatorListener, retrieveFullProduct.retrieveFullProduct_interface, setFullProduct.setFullProduct_interface, Image_Add.Image_AddListener, setImage.setImage_interface, Category_Add.Category_AddListener, setCategory.setCategory_interface {


    public int message_profile_id = 0;
    public String message_profile_name = null;
    public String message_api_address = null;
    public String message_cons_key = null;
    public String message_cons_secret = null;
    public String message_oauth_token = null;
    public String message_oauth_secret = null;
    public String message_currency_id = null;
    public String message_language_id = null;
    public List<basicProductInfo> product_retrieved = new ArrayList<basicProductInfo>();
    public Bitmap selectedImage;





    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;


    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));




        //retrieving data passed via intent
        Intent fromWelcome = getIntent();
        Bundle res = new Bundle();
        res = fromWelcome.getExtras();
        message_profile_id = res.getInt(Constants.MESSAGE_PROFILE_ID);
        message_profile_name = res.getString(Constants.MESSAGE_PROFILE_NAME);
        message_api_address = res.getString(Constants.MESSAGE_API_ADDRESS);
        message_cons_key = res.getString(Constants.MESSAGE_CONS_KEY);
        message_cons_secret = res.getString(Constants.MESSAGE_CONS_SECRET);
        message_oauth_token = res.getString(Constants.MESSAGE_OAUTH_TOKEN);
        message_oauth_secret = res.getString(Constants.MESSAGE_OAUTH_SECRET);
        message_currency_id = res.getString(Constants.MESSAGE_CURRENCY_ID);
        message_language_id = res.getString(Constants.MESSAGE_LANGUAGE_ID);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(message_profile_name))
                .commit();


    }



    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();


        switch (position) {

            case 0:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(message_profile_name))
                        .commit();

                break;

            case 1:
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Retrieve_SKU.newInstance(1)) //get stock
                    .addToBackStack(null)
                    .commit();

             break;


            case 2:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Retrieve_SKU.newInstance(3)) //get product info
                        .addToBackStack(null)
                        .commit();


                break;


            case 3:
                //price check / markdown
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Retrieve_SKU.newInstance(2)) //get pricing
                        .addToBackStack(null)
                        .commit();

                break;

            case 4:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Product_creator.newInstance(0, null))
                        .addToBackStack(null)
                        .commit();


                break;

            case 7:
                finish();
                break;
            case 8:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Retrieve_SKU.newInstance(4)) //get product info
                        .addToBackStack(null)
                        .commit();


                break;

            case 9:
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Retrieve_SKU.newInstance(5)) //get product info
                        .addToBackStack(null)
                        .commit();


                break;

        }

        this.onSectionAttached(position + 1);
        restoreActionBar();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.nav_section0);
                break;
            case 2:
                mTitle = getString(R.string.nav_section1);
                break;
            case 3:
                mTitle = getString(R.string.nav_section2);
                break;
            case 4:
                mTitle = getString(R.string.nav_section3);
                break;
            case 5:
                mTitle = getString(R.string.nav_section4);
                break;
            case 6:
                mTitle = getString(R.string.nav_section5);
                break;
            case 7:
                mTitle = getString(R.string.nav_section6);
                break;
            case 9:
                mTitle = getString(R.string.nav_section8);
                break;
            case 10:
                mTitle = getString(R.string.nav_section9);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {

            getMenuInflater().inflate(R.menu.main_menu, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void listOfItemsRetrieved(List<basicProductInfo> basic_product_info, int requester) {
        Toast.makeText(this, "hurra", Toast.LENGTH_SHORT).show();

        String array_size = Integer.toString(basic_product_info.size());
        Log.i("retrieved array size: ", array_size);
        if (basic_product_info != null)  {
            Log.i("Multiple items", "Array passed if null condition");


            if (basic_product_info.size() > 1 ){
                product_retrieved = basic_product_info;
                try {

                    DialogFragment multipleItems_dialog = MultipleItemsFoundDialog.newInstance(basic_product_info, requester);
                    Log.i("Multiple item", "Dialog starting");
                    multipleItems_dialog.show(getFragmentManager(), "multiple_items");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (basic_product_info.size() == 1) {
                Toast.makeText(this, "One product", Toast.LENGTH_SHORT).show();
                retrieverHandler(requester, basic_product_info.get(0));
            } else if (basic_product_info.size() == 0){
                Toast.makeText(this, "No products found!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void item_selected_from_multiple(int requester, int item_selected) {
        Toast.makeText(this, Integer.toString(item_selected), Toast.LENGTH_SHORT).show();
        retrieverHandler(requester, product_retrieved.get(item_selected));
    }

    @Override
    public void SKU_Retrieved(int search_type, String query, int retriever_code) {


        try {

            if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            } else {

                String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address, message_api_address};
                retrieveItemID retr = new retrieveItemID(MainMenu.this, this, retriever_code);

                retr.Dialog = new ProgressDialog(MainMenu.this);
                retr.searchType = search_type;
                retr.search_name = query;


                retr.execute(reqParams);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void retrieverHandler(int requester, basicProductInfo single_product){
        Toast.makeText(this, Integer.toString(requester) + " ... " + single_product.getName().toString(), Toast.LENGTH_SHORT).show();

        switch (requester) {
            case 1:
                //adjust qty


                try {

                    if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                        Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

                    } else {

                        String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                        retrieveQuantity retr = new retrieveQuantity(MainMenu.this, this, 1);
                        retr.single_product = single_product;
                        retr.requester = requester;
                        retr.Dialog = new ProgressDialog(MainMenu.this);


                        retr.execute(reqParams);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }




            break;


            case 2:
                //price adjust
                try {

                    if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                        Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

                    } else {

                        String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                        retrievePrice retr = new retrievePrice(MainMenu.this, this, 1);
                        retr.single_product = single_product;
                        retr.requester = requester;
                        retr.Dialog = new ProgressDialog(MainMenu.this);

                        retr.execute(reqParams);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;


            case 3:
                // view/edit product
                try {

                    if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                        Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

                    } else {

                        String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                        retrieveFullProduct retr = new retrieveFullProduct(MainMenu.this, this, 1);
                        retr.single_product = single_product;
                        retr.requester = requester;
                        retr.Dialog = new ProgressDialog(MainMenu.this);


                        retr.execute(reqParams);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case 4:
                //add picture

                FragmentManager fragmentManager_img = getFragmentManager();
                Log.i("Starting fragment manager","add picture");
                fragmentManager_img.beginTransaction()
                        .replace(R.id.container, Image_Add.newInstance(single_product.getEntityId(), single_product.getEAN(), single_product.getName(), this))
                        .addToBackStack(null)
                        .commit();

                //

                break;

            case 5:
                //add category

                FragmentManager fragmentManager_cat = getFragmentManager();
                Log.i("Starting fragment manager","add category");
                fragmentManager_cat.beginTransaction()
                        .replace(R.id.container, Category_Add.newInstance(single_product.getEntityId(), single_product.getEAN(), single_product.getName(), this))
                        .addToBackStack(null)
                        .commit();

                //

                break;
        }

    }



    @Override
    public void item_qty_retrieved(int requester, basicProductInfo single_product) {
        switch (requester) {
            case 1:
            FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.container, Qty_adjuster.newInstance(single_product.getEntityId(), single_product.getEAN(), single_product.getName(), Long.toString(single_product.getStock_qty())))
                    .addToBackStack(null)
                    .commit();

            break;
        }
    }


    @Override
    public void item_price_retrieved(int requester, basicProductInfo single_product) {
        switch (requester) {
            case 2:



                    FragmentManager fragmentManager = getFragmentManager();
                    Log.i("Starting fragment manager","special price");
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, SpecialPrice_Adjuster.newInstance(single_product.getEntityId(), single_product.getEAN(), single_product.getName(), Double.toString(single_product.getPrice()), Double.toString(single_product.getSpecial_price()), single_product.getSpecial_from(), single_product.getSpecial_to()))
                            .addToBackStack(null)
                            .commit();

                break;
        }
    }

    @Override
    public void StockAdjustmentRequested(int unique_id, String payload) {
        try {

            if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            } else {

                String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                setQuantity set_qty = new setQuantity(MainMenu.this, this, unique_id, payload);
                set_qty.Dialog = new ProgressDialog(MainMenu.this);


                set_qty.execute(reqParams);


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void item_qty_set(int result_code) {
        Log.i("MainMenu", "back to the main activity" + String.valueOf(result_code));
        switch (result_code) {
            case 200:
                Toast.makeText(this, "Stock quantity successfully updated!", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                break;

            default:
                Toast.makeText(this, "Stock update failed! Please re-try.", Toast.LENGTH_SHORT).show();
                break;
        }

    }

    @Override
    public void PriceAdjustmentRequested(int unique_id, String payload) {
        try {


            if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            } else {
                String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                setSpecialPrice set_price = new setSpecialPrice(MainMenu.this, this, unique_id, payload);
                set_price.Dialog = new ProgressDialog(MainMenu.this);



                set_price.execute(reqParams);
            }



        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void item_special_price_set(int result_code) {
        Log.i("MainMenu", "back to the main activity" + String.valueOf(result_code));
        switch (result_code) {
            case 200:
                Toast.makeText(this, "Price successfully updated!", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                break;

            default:
                Toast.makeText(this, "Price update failed! Please re-try.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void ProductCreationOrUpdateRequested(int action_type, String payload, int entity_id) {
        Log.i("product creation data", String.valueOf(action_type) + " , " + payload + " , " + String.valueOf(entity_id));
        try {
            if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            } else {


                String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                setFullProduct set_product = new setFullProduct(MainMenu.this, this, entity_id, payload, action_type);
                set_product.Dialog = new ProgressDialog(MainMenu.this);

                set_product.execute(reqParams);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }




    }

    @Override
    public void full_product_retrieved(int requester, basicProductInfo single_product) {
        switch (requester) {
            case 3:


                FragmentManager fragmentManager = getFragmentManager();
                Log.i("Starting fragment manager", "view product");
                fragmentManager.beginTransaction()
                        .replace(R.id.container, Product_creator.newInstance(1, single_product))
                        .addToBackStack(null)
                        .commit();

                break;

        }

    }

    @Override
    public void product_created_or_amended(int result_code, int action_type, int product_id) {
        Log.i("product_created", String.valueOf(result_code));
        FragmentManager fragmentManager = getFragmentManager();
        switch (result_code) {
            case 200:
                Toast.makeText(this, "Product successfully created/amended!", Toast.LENGTH_SHORT).show();

                fragmentManager.popBackStack();
                break;

            case 404:
                Toast.makeText(this, "Product successfully created/amended!", Toast.LENGTH_SHORT).show();

                fragmentManager.popBackStack();
                break;

            default:
                Toast.makeText(this, "Product update failed! Please re-try.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void ImageAdditionRequested(int unique_id, String payload) {
        Log.i("image creation data", " , " + payload + " , " + String.valueOf(unique_id));
        try {

            if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            } else {

                String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                setImage set_image = new setImage(MainMenu.this, this, unique_id, payload, 0);
                set_image.Dialog = new ProgressDialog(MainMenu.this);

                set_image.execute(reqParams);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void image_added(int result_code) {
        Log.i("image added", String.valueOf(result_code));

        switch (result_code) {
            case 200:
                Toast.makeText(this, "Image successfully added!", Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.popBackStack();
                break;

            default:
                Toast.makeText(this, "Image upload failed! Please re-try.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void CategoryAdditionRequested(int unique_id, String payload) {
        Log.i("category creation data", " , " + payload + " , " + String.valueOf(unique_id));
        try {

            if (ConnectivityCheck.isConnected(this.getApplicationContext()) == false){
                Toast.makeText(this,getString(R.string.no_network), Toast.LENGTH_SHORT).show();

            } else {

                String[] reqParams = {message_oauth_token, message_oauth_secret, message_cons_key, message_cons_secret, message_api_address};
                setCategory set_cat = new setCategory(MainMenu.this, this, unique_id, payload, 0);
                set_cat.Dialog = new ProgressDialog(MainMenu.this);

                set_cat.execute(reqParams);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void category_added(int result_code) {
        FragmentManager fragmentManager = getFragmentManager();
        switch (result_code) {
            case 200:
                Toast.makeText(this, "Category successfully assigned!", Toast.LENGTH_SHORT).show();
                fragmentManager.popBackStack();
                break;


            case 404:
                Toast.makeText(this, "Category successfully assigned!", Toast.LENGTH_SHORT).show();

                fragmentManager.popBackStack();
                break;

            default:
                Toast.makeText(this, "Category assignment failed! Please re-try.", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    //






    //---------------------------------------------------------------------------------
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_PROFILE_NAME = "profile_name";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(String profile_name) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PROFILE_NAME, profile_name);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_menu, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.txt_Welcome_Hi);
            textView.setText("Hi "+ getArguments().getString(ARG_PROFILE_NAME)+",");
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainMenu) activity).onSectionAttached(1);
        }
    }
//-----------------------------------------------------------------------------------------------

    private String XMLAssignToDefaultCategory(int product_id) {
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter str_writer = new StringWriter();
        try {
            serializer.setOutput(str_writer);
            serializer.startDocument("UTF-8", true);
            serializer.startTag("", "magento_api");
            serializer.startTag("", "category_id");
            serializer.text("2");
            serializer.endTag("", "category_id");

            serializer.endTag("", "magento_api");
            serializer.endDocument();

            Log.i("XML_OUTPUT", str_writer.toString());


            return str_writer.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


}

