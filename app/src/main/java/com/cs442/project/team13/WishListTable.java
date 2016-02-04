package com.cs442.project.team13;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by Evan on 2015/11/4.
 */
@ParseClassName("WishListTable")
public class WishListTable extends ParseObject {
    public static final String TITLE= "TITLE";
    public static final String ISBN= "ISBN";
    public static final String USERID= "USERID";

    public WishListTable(){

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



    public String getUserId(){
        return getString(USERID);
    }

    public void setUserId(String userId){
        put(USERID,userId);
    }
}
