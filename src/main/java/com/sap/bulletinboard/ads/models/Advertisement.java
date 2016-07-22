package com.sap.bulletinboard.ads.models;

import java.sql.Timestamp;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
//import javax.validation.constraints.NotNull;

@Entity
@Table(name = "Advertisements")
public class Advertisement {
    @Id  //for table
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "_title")
    private String title;
    
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Advertisement() {
    }

    public Advertisement(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }
    
    public Timestamp getCreatedAt(){
        return createdAt;
    }
    
    public Timestamp getUpdatedAt(){
        return updatedAt;
    }
    
    protected Timestamp now() {                     // use java.sql.Timestamp
        return new Timestamp((new Date()).getTime()); // use java.util.Date
    } 
}
