package com.kwiatkowski.androhht.androhht;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */



public class DatabaseManager {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "AndroHHT_DB";

    // Contacts table name
    private static final String TABLE_PRF_NAME = "AndroHHT_profiles";

    // Contacts Table Columns names
    public static final String PRF_ID = "_id";
    public static final String PRF_NAME = "name";
    public static final String PRF_PASSCODE = "passcode";
    public static final String PRF_API_URL = "api_url";
    public static final String PRF_LANG_ID = "lang_id";
    public static final String PRF_LAST_LOGIN = "last_login";
    public static final String PRF_CURRENCY_ID = "currency_id";
    public static final String PRF_CONS_KEY = "cons_key";
    public static final String PRF_CONS_SECRET = "cons_secret";
    public static final String PRF_ACC_OAUTH_TOKEN = "acc_oauth_token";
    public static final String PRF_ACC_OAUTH_TOKEN_SECRET = "acc_oauth_token_secret";


    // SQL statement to create profiles table
    private static final String CREATE_PROFILES_TABLE = "CREATE TABLE " + TABLE_PRF_NAME +
            "( " + PRF_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + PRF_NAME + " TEXT, " +
            PRF_PASSCODE + " TEXT, " + PRF_API_URL + " TEXT, " + PRF_LANG_ID + " TEXT, " +
            PRF_LAST_LOGIN + " TEXT, " + PRF_CURRENCY_ID + " TEXT, " + PRF_CONS_KEY + " TEXT, " +
            PRF_CONS_SECRET + " TEXT, " + " TEXT, " +
            PRF_ACC_OAUTH_TOKEN + " TEXT, " + PRF_ACC_OAUTH_TOKEN_SECRET + " TEXT" + ")";


    // SQL statement for deleting profiles table
    private static final String DROP_PROFILES_TABLE = "DROP TABLE IF EXISTS " + TABLE_PRF_NAME;

    //tag for debugger
    private static final String TAG = "DatabaseManager";


    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private final Context dbCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, CREATE_PROFILES_TABLE);

            // create profiles table
            db.execSQL(CREATE_PROFILES_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            //deletes the table
            db.execSQL(DROP_PROFILES_TABLE);

            //calls onCreate to create the table
            onCreate(db);

        }

    }

    public DatabaseManager(Context ctx){
        this.dbCtx = ctx;
    }

    public DatabaseManager open() throws SQLException{
        dbHelper = new DatabaseHelper(dbCtx);
        db = dbHelper.getWritableDatabase();

        return this;
    }

    public void close(){
           if (dbHelper != null){
               dbHelper.close();
           }
    }


    public long addProfile (Profile profile){

        //creates record to insert
        ContentValues record = new ContentValues();
        record.put(PRF_NAME, profile.getName());
        record.put(PRF_PASSCODE, profile.getPasscode());
        record.put(PRF_API_URL, profile.getApiURL());
        record.put(PRF_LANG_ID, profile.getLangID());
        record.put(PRF_LAST_LOGIN, profile.getLastLogin());
        record.put(PRF_CURRENCY_ID, profile.getCurrencyID());
        record.put(PRF_CONS_KEY, profile.getConsKey());
        record.put(PRF_CONS_SECRET, profile.getConsSecret());
        record.put(PRF_ACC_OAUTH_TOKEN, profile.getAccAuthToken());
        record.put(PRF_ACC_OAUTH_TOKEN_SECRET, profile.getAccAuthTokenSecret());

        //insert a record and returns the cursor
        return db.insert(TABLE_PRF_NAME, null, record);

    }



    public boolean deleteAllProfiles() {
        int doneDelete = 0;
                doneDelete = db.delete(TABLE_PRF_NAME, null, null);
                Log.w(TAG, Integer.toString(doneDelete));
                return doneDelete > 0;
    }


    public boolean deleteSingleProfile(int profileID){
        int deleteSingle = 0;
        deleteSingle = db.delete(TABLE_PRF_NAME, PRF_ID + " = ?", new String[] {String.valueOf(profileID)});
        return deleteSingle > 0;

    }


    public Cursor getAllProfilesX() {
        Cursor dbCursor = db.query(TABLE_PRF_NAME, new String[] {PRF_ID, PRF_NAME, PRF_LAST_LOGIN},null,null,null,null,null);

        if (dbCursor != null) {
            dbCursor.moveToFirst();
        }
        return dbCursor;
    }

    public Cursor getPassword(int profileID) {
        Cursor dbCursor = db.query(TABLE_PRF_NAME, new String[] {PRF_PASSCODE}, PRF_ID + " = ?", new String[] {String.valueOf(profileID)}, null, null, null);

        if (dbCursor != null) {
            dbCursor.moveToFirst();
        }
        return dbCursor;


    }

    public Cursor getSingleProfile(int profileID) {
        Cursor dbCursor = db.query(TABLE_PRF_NAME, new String[] {PRF_ID, PRF_NAME, PRF_API_URL, PRF_CONS_KEY, PRF_CONS_SECRET, PRF_ACC_OAUTH_TOKEN, PRF_ACC_OAUTH_TOKEN_SECRET, PRF_CURRENCY_ID, PRF_LANG_ID}, PRF_ID + " = ?", new String[] {String.valueOf(profileID)}, null, null, null);

        if (dbCursor != null) {
            dbCursor.moveToFirst();
        }
        return dbCursor;


    }



    public int getProfilesCount() {
        String sqlProfilesCount = "SELECT * FROM " + TABLE_PRF_NAME;
        Cursor cursor = db.rawQuery(sqlProfilesCount, null);


        try {

            if (!cursor.moveToFirst()) {
                return 0;
            } else {
                return cursor.getCount();

            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }




    public List<Profile> getAllProfiles() {
        List<Profile> profiles = new LinkedList<Profile>();

        String sqlGetAllProfiles = "SELECT _id, name, last_login FROM " + TABLE_PRF_NAME;


        Cursor cursor = db.rawQuery(sqlGetAllProfiles, null);

        Profile profile = null;
        if (cursor.moveToFirst()) {
            do {
                profile = new Profile();
                profile.setID(Integer.parseInt(cursor.getString(0)));
                profile.setName(cursor.getString(1));
                profile.setLastLogin(cursor.getString(2));

                profiles.add(profile);
            } while (cursor.moveToNext());
        }

        return profiles;

    }


    public void updaleLastLogin (int profile_id, String last_login){

        //creates set of arguments to update
        ContentValues record = new ContentValues();

        record.put(PRF_LAST_LOGIN, last_login);

        //update a record
        db.update(TABLE_PRF_NAME, record, PRF_ID + "=" + String.valueOf(profile_id), null);

    }


}
