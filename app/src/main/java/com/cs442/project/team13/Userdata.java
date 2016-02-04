package com.cs442.project.team13;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseUser;

/**
 * Created by bachelor_2009 on 10/31/2015.
 */
@ParseClassName("_User")
public class Userdata extends ParseUser {

    public Userdata() {

    }
    public ParseFile getPhoto() {
        return getParseFile("photo");
    }

    public void setPhoto(ParseFile p) {
        put("photo", p);
    }

    public String getPhone() {
        return getString("phoneNo");
    }

    public void setPhone(String p) {
        put("phoneNo",p);
    }
}

