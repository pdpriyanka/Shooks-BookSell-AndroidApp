package com.cs442.project.team13;

import com.parse.ParseClassName;
import com.parse.ParseInstallation;

/**
 * Created by chen on 2015/11/13.
 */
@ParseClassName("_Installation")
public class Installation extends ParseInstallation {

    public Installation() {

    }

    public void setUsername(String name) {
        put("username",name);
    }

    public String getUsername() {
        return getString("username");
    }

   public void setReceive(boolean value) {
       put("isReceive",value);
   }

    public boolean getReceive() {
        return getBoolean("isReceive");
    }

}
