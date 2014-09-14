package com.kwiatkowski.androhht.androhht;


public class Profile {
    private int id;
    private String name;
    private String passcode;
    private String api_url;
    private String lang_id;
    private String last_login;
    private String currency_id;
    private String cons_key;
    private String cons_secret;
    private String acc_oauth_token;
    private String acc_oauth_token_secret;

    //Empty constructor
    public Profile(){

    }



    //get id
    public int getID(){
        return this.id;
    }
    //get name
    public String getName(){
        return this.name;
    }
    //get passcode
    public String getPasscode(){
        return this.passcode;
    }
    //get api url
    public String getApiURL(){
        return this.api_url;
    }
    //get lang id
    public String getLangID(){
        return this.lang_id;
    }
    //get last login
    public String getLastLogin(){
        return this.last_login;
    }
    //get currency id
    public String getCurrencyID(){
        return this.currency_id;
    }
    //get cons key
    public String getConsKey(){
        return this.cons_key;
    }
    //get cons key
    public String getConsSecret(){
        return this.cons_secret;
    }
    //get req oauth token
    public String getAccAuthToken(){
        return this.acc_oauth_token;
    }
    //get req oauth token secret
    public String getAccAuthTokenSecret(){
        return this.acc_oauth_token_secret;
    }



    //set id
    public void setID(int id){
        this.id = id;
    }
    //set name
    public void setName(String name){
        this.name = name;
    }
    //set passcode
    public void setPasscode(String passcode){
        this.passcode = passcode;
    }

    //set api url
    public void setApiURL(String api_url){
        this.api_url = api_url;
    }
    //set lang id
    public void setLangID(String lang_id){
        this.lang_id = lang_id;
    }
    //set last login
    public void setLastLogin(String last_login){this.last_login = last_login; }
    //set currency id
    public void setCurrencyID(String currency_id){
        this.currency_id = currency_id;
    }
    //set cons key
    public void setConsKey(String cons_key){
        this.cons_key = cons_key;
    }
    //set cons key
    public void setConsSecret(String cons_secret){
        this.cons_secret = cons_secret;
    }
    //set req oauth token
    public void setAccAuthToken(String acc_oauth_token){
        this.acc_oauth_token = acc_oauth_token;
    }
    //set req oauth token secret
    public void setAccAuthTokenSecret(String acc_oauth_token_secret){
        this.acc_oauth_token_secret = acc_oauth_token_secret;
    }



}
