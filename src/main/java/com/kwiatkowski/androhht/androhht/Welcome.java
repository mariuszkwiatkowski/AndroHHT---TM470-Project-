package com.kwiatkowski.androhht.androhht;

import com.kwiatkowski.androhht.androhht.util.CryptoUtil;
import com.kwiatkowski.androhht.androhht.util.SystemUiHider;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * Template generated automatically by Android Studio
 *
 * **
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 *
 *
 *
 */

public class Welcome extends Activity implements PasswordDialog.PasswordDialogListener {




    //variables for database manager and adapter
    private DatabaseManager dbManager;
    private SimpleCursorAdapter dataAdapter;





    private static final boolean AUTO_HIDE = true;


    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;


    private static final boolean TOGGLE_ON_CLICK = true;


    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;


    private SystemUiHider mSystemUiHider;


    @Override
    protected void onResume() {
        super.onResume();
        updateListOfProfiles();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbManager = new DatabaseManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }




        //check number of rows in the profiles database
        int profilesCount = dbManager.getProfilesCount();


        setContentView(R.layout.activity_welcome);

        final View controlsView = findViewById(R.id.fullscreen_content_controls);
        final View contentView = findViewById(R.id.fullscreen_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {

                    int mControlsHeight;
                    int mShortAnimTime;

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {

                            if (mControlsHeight == 0) {
                                mControlsHeight = controlsView.getHeight();
                            }
                            if (mShortAnimTime == 0) {
                                mShortAnimTime = getResources().getInteger(
                                        android.R.integer.config_shortAnimTime);
                            }
                            controlsView.animate()
                                    .translationY(visible ? 0 : mControlsHeight)
                                    .setDuration(mShortAnimTime);
                        } else {

                            controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
                        }

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });


        contentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TOGGLE_ON_CLICK) {
                    mSystemUiHider.toggle();
                } else {
                    mSystemUiHider.show();
                }
            }
        });



        Toast.makeText(this, Integer.toString(profilesCount), Toast.LENGTH_SHORT).show();

        TextView txt_no_profiles = (TextView) findViewById(R.id.txt_no_profiles_exist);

        if (profilesCount == 0) {

            txt_no_profiles.setText(String.format(getResources().getString(R.string.txt_no_profiles)));
            txt_no_profiles.setEnabled(true);
            txt_no_profiles.setVisibility(View.VISIBLE);
        } else {
            txt_no_profiles.setVisibility(View.INVISIBLE);
            displayProfilesList();


        }

        //set listener for add a new profile button
        createAddProfileButton();

    }


        private void displayProfilesList(){
            Cursor cursor = dbManager.getAllProfilesX();

            //columns to be bound
            String[] columns = new String[] {
                    DatabaseManager.PRF_ID,
                    DatabaseManager.PRF_NAME,
                    DatabaseManager.PRF_LAST_LOGIN
            };

            //field data will be bound to
            int[] to = new int[] {
                    R.id.list_ID,
                    R.id.list_Name,
                    R.id.list_LastLogin,
            };

            // create the adapter
            dataAdapter = new SimpleCursorAdapter(
                    this, R.layout.profiles_list_layout,
                    cursor,
                    columns,
                    to,
                    0);

            ListView listView_profiles = (ListView) findViewById(R.id.listView_Profiles);
            listView_profiles.setAdapter(dataAdapter);
            registerForContextMenu(listView_profiles);

        }

        private void createAddProfileButton() {
         Button add_new_profile = (Button) findViewById(R.id.btn_login_add_new_profile);
             add_new_profile.setOnClickListener(new View.OnClickListener() {
                 public void onClick(View v) {

                     Intent toAddProfile = new Intent(Welcome.this, AddEdit_Profile.class);
                     startActivityForResult(toAddProfile, Constants.ADD_PROFILE_REQ_CODE);
                 }
         });
        }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);


        delayedHide(100);
    }


    View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (AUTO_HIDE) {
                delayedHide(AUTO_HIDE_DELAY_MILLIS);
            }
            return false;
        }
    };

    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };


    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,  ContextMenu.ContextMenuInfo menuInfo) {
        if (v.getId()==R.id.listView_Profiles) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle("Profile...");
            String[] menuItems = getResources().getStringArray(R.array.welcome_context);
            for (int i = 0; i<menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }

            //long itemClicked = info.id;
            //Toast.makeText(this, Long.toString(itemClicked), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int menuItemIndex = item.getItemId();
        int itemClicked = info.position;


        ListView listView_profiles = (ListView) findViewById(R.id.listView_Profiles);
        Cursor cursor = (Cursor) listView_profiles.getItemAtPosition(itemClicked);


        int profile_id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseManager.PRF_ID));


        Toast.makeText(this, Integer.toString(menuItemIndex) + " // " + profile_id, Toast.LENGTH_SHORT).show();



        switch (menuItemIndex) {
            case 0:
                confirmPassword(profile_id, menuItemIndex);
            case 1:
                break;
            case 2:
                confirmPassword(profile_id, menuItemIndex);
                break;


        }




        String[] menuItems = getResources().getStringArray(R.array.welcome_context);
        String menuItemName = menuItems[menuItemIndex];

        return true;
    }


    public void confirmPassword(int profile_id, int request_type) {
        DialogFragment password_dialog = PasswordDialog.newInstance(profile_id, request_type);
        password_dialog.show(getFragmentManager(), "password");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    @Override
    public void onPasswordCheckComplete(boolean passCheck, int profileID, int request_type, String password_input) {
        if (passCheck == true) {
            Toast.makeText(this, Boolean.toString(passCheck) + " " + Integer.toString(profileID) + " " + Integer.toString(request_type), Toast.LENGTH_SHORT).show();

            switch (request_type) {
                case 0:

                    try {
                        dbManager = new DatabaseManager(this);
                        try {
                            dbManager.open();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }

                        Cursor dbCursor = dbManager.getSingleProfile(profileID);
                        String db_name = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_NAME));
                        String db_api = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_API_URL));
                        String db_cons_key = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_CONS_KEY));
                        String db_cons_secret = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_CONS_SECRET));
                        String db_token = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_ACC_OAUTH_TOKEN));
                        Log.i("DB", db_token);
                        db_token = CryptoUtil.cryptoDecode(db_token, password_input);
                        Log.i("DB", db_token);
                        String db_token_secret = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_ACC_OAUTH_TOKEN_SECRET));
                        Log.i("DB", db_token_secret);
                        db_token_secret = CryptoUtil.cryptoDecode(db_token_secret, password_input);
                        Log.i("DB", db_token_secret);
                        String db_currency = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_CURRENCY_ID));
                        String db_lang = dbCursor.getString(dbCursor.getColumnIndexOrThrow(DatabaseManager.PRF_LANG_ID));
                        dbManager.close();
                        openMainMenu(profileID, db_name, db_api, db_cons_key, db_cons_secret, db_token,db_token_secret, db_currency, db_lang);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case 1:
                    updateListOfProfiles();
                    break;
                case 2:
                    dbManager.deleteSingleProfile(profileID);
                    updateListOfProfiles();
                    break;
            }


        } else {
            Toast.makeText(this, String.format(getResources().getString(R.string.password_incorrect)), Toast.LENGTH_SHORT).show();
        }
    }

    public void openMainMenu(int profileID, String profile_name, String api_address, String cons_key, String cons_secret, String oauth_token, String oauth_secret, String currency_id, String language_id){
            Intent toMainMenu = new Intent(this, MainMenu.class);
            Bundle args = new Bundle();

            args.putInt(Constants.MESSAGE_PROFILE_ID, profileID);
            args.putString(Constants.MESSAGE_PROFILE_NAME, profile_name);
            args.putString(Constants.MESSAGE_API_ADDRESS, api_address);
            args.putString(Constants.MESSAGE_CONS_KEY, cons_key);
            args.putString(Constants.MESSAGE_CONS_SECRET, cons_secret);
            args.putString(Constants.MESSAGE_OAUTH_TOKEN, oauth_token);
            args.putString(Constants.MESSAGE_OAUTH_SECRET, oauth_secret);
            args.putString(Constants.MESSAGE_CURRENCY_ID, currency_id);
            args.putString(Constants.MESSAGE_LANGUAGE_ID, language_id);
            toMainMenu.putExtras(args);
            startActivity(toMainMenu);
    }


    private void updateListOfProfiles(){
        dbManager = new DatabaseManager(this);
        try {
            dbManager.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //check number of rows in the profiles database
        int profilesCount = dbManager.getProfilesCount();
        TextView txt_no_profiles = (TextView) findViewById(R.id.txt_no_profiles_exist);
        if (profilesCount == 0) {

            txt_no_profiles.setText(String.format(getResources().getString(R.string.txt_no_profiles)));
            txt_no_profiles.setEnabled(true);
            txt_no_profiles.setVisibility(View.VISIBLE);

        } else {
            txt_no_profiles.setEnabled(false);
            txt_no_profiles.setVisibility(View.INVISIBLE);
        }
        displayProfilesList();
        dbManager.close();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        switch (requestCode) {
            case Constants.ADD_PROFILE_REQ_CODE:
                if (resultCode == RESULT_OK) {
                    updateListOfProfiles();
                }
                if (resultCode == RESULT_CANCELED) {

                }

        }
    }
}
