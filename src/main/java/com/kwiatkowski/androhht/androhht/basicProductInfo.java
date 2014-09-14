package com.kwiatkowski.androhht.androhht;


/**
 * This application uses 3rd party libraries and code.
 * Refer to LICENCE.txt for licensing details
 */


public class basicProductInfo {
    private int entity_id;
    private String sku;
    private String name;
    private String ean;
    private long stock_qty;
    private double price;
    private double special_price;
    private String special_from;
    private String special_to;
    private double weight;
    private boolean enabled;
    private int visibility;
    private int tax_class_id;
    private int attribute_set_id;
    private String type_id;
    private String desc;
    private String short_desc;

    public basicProductInfo() {}

    public int getEntityId() {return this.entity_id;}
    public String getSku() {return this.sku;}
    public String getName() {return this.name;}
    public String getEAN() {return this.ean;}
    public long getStock_qty() {return this.stock_qty;}
    public double getPrice() {return this.price;}
    public double getSpecial_price() {return this.special_price;}
    public String getSpecial_from() {return this.special_from;}
    public String getSpecial_to() {return this.special_to;}
    public double getWeight() {return this.weight;}
    public boolean isEnabled() {return this.enabled;}
    public int getVisibility() {return this.visibility;}
    public int getTax_class_id() {return this.tax_class_id;}
    public int getAttribute_set_id() {return this.attribute_set_id;}
    public String getType_id() {return this.type_id;}
    public String getDesc() {return this.desc;}
    public String getShort_desc() {return this.short_desc;}

    public void setEntityId(int entity_id) {this.entity_id = entity_id; }
    public void setSku(String sku) {this.sku = sku;}
    public void setName(String name){ this.name = name; }
    public void setEAN(String ean){ this.ean = ean; }
    public void setStock_qty(long stock_qty) {this.stock_qty = stock_qty;}
    public void setPrice(double price) {this.price = price;}
    public void setSpecial_price(double special_price) {this.special_price = special_price;}
    public void setSpecial_from(String special_from) {this.special_from = special_from;}
    public void setSpecial_to(String special_to) {this.special_to = special_to;}
    public void setWeight(double weight) {this.weight = weight;}
    public void setEnabled(boolean enabled) {this.enabled = enabled;}
    public void setVisibility(int visibility) {this.visibility = visibility;}
    public void setTax_class_id(int tax_class_id) {this.tax_class_id = tax_class_id;}
    public void setAttribute_set_id(int attribute_set_id) {this.attribute_set_id = attribute_set_id;}
    public void setType_id(String type_id) {this.type_id = type_id;}
    public void setDesc(String desc) {this.desc = desc;}
    public void setShort_desc(String short_desc) {this.short_desc = short_desc;}

}


