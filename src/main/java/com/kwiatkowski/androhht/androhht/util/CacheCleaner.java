package com.kwiatkowski.androhht.androhht.util;

import android.content.Context;
import android.text.format.DateUtils;
import android.util.Log;


import java.io.File;
import java.util.Date;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */
public class CacheCleaner {
    static String TAG = "Clean_Cache";


    static int clearCacheFolder(final File dir, final int numDays) {

        int deletedFiles = 0;
        if (dir!= null && dir.isDirectory()) {
            try {
                for (File child:dir.listFiles()) {


                    if (child.isDirectory()) {
                        deletedFiles += clearCacheFolder(child, numDays);
                    }

                    if (child.lastModified() < new Date().getTime() - numDays * DateUtils.DAY_IN_MILLIS) {
                        if (child.delete()) {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch(Exception e) {
                Log.e(TAG, String.format("Unable to clean the cache, %s", e.getMessage()));
            }
        }
        return deletedFiles;
    }

    /*
     * 0 = clear all files
     */
    public static void clearCache(final Context context, final int numDays) {

        int numDeletedFiles = clearCacheFolder(context.getCacheDir(), numDays);

    }

}
