package com.example.notemanagement;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Note {

    private String title;
    private String date;
    private String context;


    /**
     * Contructor for add object read from file
     */
    public Note() {

    }


    /**
     * Contructor create new obj with title, context from param and time form function get time now
     * @param title
     * @param context
     */
    public Note(String title, String context) {

        /*Get time by LocalDateTime and set format*/
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        /*Use parameters and function get time create new object*/
        this.title = title;
        this.date = dtf.format(now);
        this.context = context;
    }


    /**
     * get variable Title
     * @return
     */
    public String getTitle() {
        return title;
    }


    /**
     * set variable Title
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }


    /**
     * get variable Date
     * @return
     */
    public String getDate() {
        return date;
    }


    /**
     * set variable Date
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }


    /**
     * set variable Context
     * @return
     */
    public String getContext() {
        return context;
    }


    /**
     * set variable Context
     * @param context
     */
    public void setContext(String context) {
        this.context = context;
    }
}
