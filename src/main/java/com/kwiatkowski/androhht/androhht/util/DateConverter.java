package com.kwiatkowski.androhht.androhht.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */

public class DateConverter {

    public static String DateWithoutTime(String date_inc_time) throws ParseException {
        SimpleDateFormat  input_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat  output_format = new SimpleDateFormat("yyyy-MM-dd");
        String result = output_format.format(input_format.parse(date_inc_time));

        return result;
    }

    public static String toDateWithTime(String date_wo_time) throws ParseException {
        SimpleDateFormat  output_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat  input_format = new SimpleDateFormat("yyyy-MM-dd");
        String result = output_format.format(input_format.parse(date_wo_time));

        return result;
    }

    public static String getCurrentDateTime() throws  ParseException {

        SimpleDateFormat  output_format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String output = output_format.format(new Date());

        return output;

    }

}
