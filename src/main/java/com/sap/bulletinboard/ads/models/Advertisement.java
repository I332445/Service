package com.sap.bulletinboard.ads.models;

public class Advertisement {
    private String title;
    
    public Advertisement(){}
    
    //constructor
    public Advertisement(String title){
        this.title = title;
    }
    
    //setter
    public void setTitle(String title){
        this.title = title;
    }
    
    //getter
    public String getTitle(){
        return title;
    }
}
