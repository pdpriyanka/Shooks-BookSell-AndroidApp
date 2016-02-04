package com.cs442.project.team13;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by Priyanka on 10/30/2015.
 */
@ParseClassName("Book")
public class Book extends ParseObject {
    public static final String ISBN= "isbn";
    public static final String TITLE= "title";
    public static final String CONDITION= "condition";
    public static final String AUTHOR= "author";
    public static final String PRICE= "price";
    public static final String LATITUDE= "latitude";
    public static final String LONGITUDE= "longitude";
    public static final String PHOTO= "photo";
    public static final String USERID= "userId";
	public static final String STATE= "state";
    public static final String UNIVERSITY= "university";
    public static final String DEPARTMENT= "department";
    public static final String CLASS= "class";
    public static final String PROFESSOR= "professor";
    public static final String SETLOCATION = "setLocation";
    public static final String LOCATION = "location";


    public Book(){

    }

    public String getIsbn(){
        return getString(ISBN);
    }

    public void setIsbn(String isbn){
        put(ISBN, isbn);
    }

    public String getTitle(){
        return getString(TITLE);
    }

    public void setTitle(String title){
        put(TITLE, title);
    }

    public String getCondition(){
        return getString(CONDITION);
    }

    public void setCondition(String condition){
        put(CONDITION, condition);
    }

    public String getAuthor(){
        return getString(AUTHOR);
    }

    public void setAuthor(String author){
        put(AUTHOR, author);
    }

    public double getPrice(){
        return getDouble(PRICE);
    }

    public void setPrice(double price){
        put(PRICE, price);
    }

    public double getLatitude (){
        return getDouble(LATITUDE);
    }

    public void setLatitude(double latitude){
        put(LATITUDE, latitude);
    }

    public double getLongitude(){
        return getDouble(LONGITUDE);
    }

    public void setLongitude(double longitude){
        put(LONGITUDE, longitude);
    }

    public ParseFile getPhoto(){
        return getParseFile(PHOTO);
    }

    public void setPhoto(ParseFile pf){
        put(PHOTO,pf);
    }

    public String getUserId(){
        return getString(USERID);
    }

    public void setUserId(String userId){
        put(USERID,userId);
    }
    public String getState(){
        return getString(STATE);
    }

    public void setState(String state){
        put(STATE,state);
    }


    public String getUniversity(){
        return getString(UNIVERSITY);
    }

    public void setUniversity(String university){
        put(UNIVERSITY,university);
    }


    public String getDepartment(){
        return getString(DEPARTMENT);
    }

    public void setDepartment(String department){
        put(DEPARTMENT,department);
    }

    public String getClass1(){
        return getString(DEPARTMENT);
    }

    public void setClass1(String class1){
        put(CLASS,class1);
    }

    public String getProfessor(){
        return getString(PROFESSOR);
    }

    public void setProfessor(String professor){
        put(PROFESSOR,professor);
    }

    public boolean getSetlocation(){
        return getBoolean(SETLOCATION);
    }

    public void setSetlocation(boolean setlocation){
        put(SETLOCATION,setlocation);
    }

    public ParseGeoPoint getlocation(){
        return getParseGeoPoint(LOCATION);
    }

    public void setlocation(ParseGeoPoint location){
        put(LOCATION,location);
    }

}

