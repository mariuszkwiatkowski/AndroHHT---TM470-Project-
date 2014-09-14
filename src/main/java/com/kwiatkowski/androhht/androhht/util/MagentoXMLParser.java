package com.kwiatkowski.androhht.androhht.util;



import android.util.Log;

import com.kwiatkowski.androhht.androhht.basicProductInfo;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class MagentoXMLParser {
    List<basicProductInfo> basic_products;
    private basicProductInfo basic_Product;
    private String tag_content;


    public MagentoXMLParser() {
        basic_products = new ArrayList<basicProductInfo>();
    }
    public List<basicProductInfo> getBasic_products(){
        return basic_products;
    };

    public List<basicProductInfo> parse(InputStream xml_input){
        Log.i("XML_PARSER)", "PARSER STARTED");
        XmlPullParserFactory xml_factory = null;
        XmlPullParser xml_parser = null;
        try {
            xml_factory = XmlPullParserFactory.newInstance();
            xml_factory.setNamespaceAware(false);

            xml_parser = xml_factory.newPullParser();

            xml_parser.setInput(xml_input, null);

            int eventType = xml_parser.getEventType();



            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xml_parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("entity_id")) {

                            basic_Product = new basicProductInfo();
                        }
                        break;

                    case XmlPullParser.TEXT:
                        tag_content = xml_parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("entity_id")) {

                            basic_products.add(basic_Product);
                            basic_Product.setEntityId(Integer.parseInt(tag_content));
                        } else if (tagname.equalsIgnoreCase("sku")) {
                            basic_Product.setSku(tag_content);
                        } else if (tagname.equalsIgnoreCase("name")) {
                            basic_Product.setName(tag_content);
                        } else if (tagname.equalsIgnoreCase("ean13")) {
                            basic_Product.setEAN(tag_content);
                        }
                        break;

                    default:
                        break;
                }
                eventType = xml_parser.next();
            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return basic_products;
    }

    public String parseQuantity(InputStream xml_input){
        String item_quantity = null;
        Log.i("XML_PARSER)", "PARSER QTY STARTED");
        XmlPullParserFactory xml_factory = null;
        XmlPullParser xml_parser = null;
        try {
            xml_factory = XmlPullParserFactory.newInstance();
            xml_factory.setNamespaceAware(false);

            xml_parser = xml_factory.newPullParser();

            xml_parser.setInput(xml_input, null);

            int eventType = xml_parser.getEventType();



            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xml_parser.getName();
                switch (eventType) {

                    case XmlPullParser.TEXT:
                        tag_content = xml_parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("qty")) {

                            item_quantity = tag_content;
                        }
                        break;

                    default:
                        break;
                }
                eventType = xml_parser.next();
            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return item_quantity;
    }


    public String parsePrice(InputStream xml_input){
        String item_price = null;
        Log.i("XML_PARSER)", "PARSER QTY STARTED");
        XmlPullParserFactory xml_factory = null;
        XmlPullParser xml_parser = null;
        try {
            xml_factory = XmlPullParserFactory.newInstance();
            xml_factory.setNamespaceAware(false);

            xml_parser = xml_factory.newPullParser();

            xml_parser.setInput(xml_input, null);

            int eventType = xml_parser.getEventType();



            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xml_parser.getName();
                switch (eventType) {

                    case XmlPullParser.TEXT:
                        tag_content = xml_parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("price")) {

                            item_price = tag_content;
                        }
                        break;

                    default:
                        break;
                }
                eventType = xml_parser.next();
            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return item_price;
    }

    public basicProductInfo parseFullPricing(InputStream xml_input){
        String item_price = null;
        String special_price = null;
        String special_from = null;
        String special_to = null;
        basicProductInfo itemPricing = new basicProductInfo();

        Log.i("XML_PARSER)", "PARSER QTY STARTED");
        XmlPullParserFactory xml_factory = null;
        XmlPullParser xml_parser = null;
        try {
            xml_factory = XmlPullParserFactory.newInstance();
            xml_factory.setNamespaceAware(false);

            xml_parser = xml_factory.newPullParser();

            xml_parser.setInput(xml_input, null);

            int eventType = xml_parser.getEventType();



            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xml_parser.getName();
                switch (eventType) {


                    case XmlPullParser.TEXT:
                        tag_content = xml_parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("price")) {

                            item_price = tag_content;
                        }

                        if (tagname.equalsIgnoreCase("special_price")) {

                            special_price = tag_content;
                        }

                        if (tagname.equalsIgnoreCase("special_from_date")) {

                            special_from = tag_content;
                        }

                        if (tagname.equalsIgnoreCase("special_to_date")) {

                            special_to = tag_content;
                        }
                        break;

                    default:
                        break;
                }
                eventType = xml_parser.next();
            }



        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (item_price != null && !item_price.isEmpty()) {
            try {
                itemPricing.setPrice(Double.parseDouble(item_price));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                itemPricing.setPrice(Double.parseDouble("0.00"));
            }
        } else {
            itemPricing.setPrice(Double.parseDouble("0.00"));
        }

        if (special_price != null && !special_price.isEmpty()) {
            try {
                itemPricing.setSpecial_price(Double.parseDouble(special_price));
                itemPricing.setSpecial_from(special_from);
                itemPricing.setSpecial_to(special_to);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                itemPricing.setSpecial_price(Double.parseDouble("0.00"));
                itemPricing.setSpecial_from("1901-01-01 00:00:00");
                itemPricing.setSpecial_to("1901-01-01 00:00:00");
            }

        } else {
            itemPricing.setSpecial_price(Double.parseDouble("0.00"));
            itemPricing.setSpecial_from("1901-01-01 00:00:00");
            itemPricing.setSpecial_to("1901-01-01 00:00:00");
        }
        Log.i("Special price", String.valueOf(itemPricing.getSpecial_price()));
        return itemPricing;
    }

    public basicProductInfo parseFullProduct(InputStream xml_input){
        basicProductInfo fullProductInfo = new basicProductInfo();

        Log.i("XML_PARSER)", "PARSER Full product STARTED");
        XmlPullParserFactory xml_factory = null;
        XmlPullParser xml_parser = null;
        try {
            xml_factory = XmlPullParserFactory.newInstance();
            xml_factory.setNamespaceAware(false);

            xml_parser = xml_factory.newPullParser();

            xml_parser.setInput(xml_input, null);

            int eventType = xml_parser.getEventType();



            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = xml_parser.getName();
                switch (eventType) {


                    case XmlPullParser.TEXT:
                        tag_content = xml_parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("attribute_set_id")) {
                            fullProductInfo.setAttribute_set_id(Integer.parseInt(tag_content));
                        }
                        if (tagname.equalsIgnoreCase("type_id")) {
                            fullProductInfo.setType_id(tag_content);
                        }

                        if (tagname.equalsIgnoreCase("status")) {
                            Log.i("tag content status", tag_content);
                            fullProductInfo.setEnabled(tag_content.equals("1") ? true : false);
                        }

                        if (tagname.equalsIgnoreCase("visibility")) {
                            fullProductInfo.setVisibility(Integer.parseInt(tag_content));
                        }
                        if (tagname.equalsIgnoreCase("tax_class_id")) {
                            fullProductInfo.setTax_class_id(Integer.parseInt(tag_content));
                        }
                        if (tagname.equalsIgnoreCase("weight")) {
                            fullProductInfo.setWeight(Double.parseDouble(tag_content));
                        }

                        if (tagname.equalsIgnoreCase("description")) {
                            fullProductInfo.setDesc(tag_content);
                        }

                        if (tagname.equalsIgnoreCase("short_description")) {
                            fullProductInfo.setShort_desc(tag_content);
                        }

                        if (tagname.equalsIgnoreCase("price")) {
                            String item_price = tag_content;
                            if (item_price != null && !item_price.isEmpty()) {
                                try {
                                    fullProductInfo.setPrice(Double.parseDouble(item_price));
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                    fullProductInfo.setPrice(Double.parseDouble("0.00"));
                                }
                            } else {
                                fullProductInfo.setPrice(Double.parseDouble("0.00"));
                            }
                            Log.i("itemprice", String.valueOf(fullProductInfo.getPrice()));

                        }

                        break;

                    default:
                        break;
                }
                eventType = xml_parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return fullProductInfo;
    }


}
