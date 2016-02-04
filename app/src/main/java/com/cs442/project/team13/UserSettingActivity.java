package com.cs442.project.team13;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.parse.ParseInstallation;
import com.parse.ParseUser;

/**
 * Created by chen on 2015/11/20.
 */
public class UserSettingActivity extends Activity {

    //TextView indicator;
    Switch isReceiveOn;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

       // indicator = (TextView) findViewById(R.id.switchStatus);
        isReceiveOn = (Switch) findViewById(R.id.mySwitch);
        Userdata user = (Userdata) ParseUser.getCurrentUser();
        final String username = user.getUsername();
        final SharedPreferences receive = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = receive.edit();
        isReceiveOn.setChecked(receive.getBoolean(username, true));
        isReceiveOn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean(username, isChecked);
                edit.commit();
                Installation installation = (Installation) ParseInstallation.getCurrentInstallation();
                installation.setReceive(isChecked);
                installation.saveInBackground();

            }
        });
        /*addPreferencesFromResource(R.xml.settings);
        final SharedPreferences receive = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        PreferenceManager.setDefaultValues(this,"myPreferences", Context.MODE_PRIVATE, R.xml.settings, false);

        Userdata user = (Userdata) ParseUser.getCurrentUser();
        final String username = user.getUsername();

        final SwitchPreference InternetStatus = (SwitchPreference) findPreference("receive_notification");
        if (InternetStatus != null){
            InternetStatus.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object isNotificationEnable) {
                    //Boolean isReceive = receive.getBoolean(username, true);
                    Boolean isReceiveOn = ((Boolean) isNotificationEnable).booleanValue();
                    *//*if(isReceive == true) {
                        InternetStatus.setChecked(true);
                    } else {
                        InternetStatus.setChecked(false);
                    }
                    Boolean isReceiveOn = ((Boolean) isNotificationEnable).booleanValue();

                    if((isReceive==true && isReceiveOn==true) || (isReceive == false && isReceiveOn == false)) {
                        Installation installation = (Installation) ParseInstallation.getCurrentInstallation();
                        installation.setReceive(isReceiveOn);
                        installation.saveInBackground();
                    } else if((isReceive == true && isReceiveOn == false) || (isReceive == false && isReceiveOn == true)) {
                        SharedPreferences.Editor edit = receive.edit();
                        edit.putBoolean(username,isReceiveOn);
                        edit.commit();
                        Installation installation = (Installation) ParseInstallation.getCurrentInstallation();
                        installation.setReceive(isReceiveOn);
                        installation.saveInBackground();
                    }*//*
                    SharedPreferences.Editor edit = receive.edit();
                    edit.putBoolean(username,isReceiveOn);
                    edit.commit();
                    Installation installation = (Installation) ParseInstallation.getCurrentInstallation();
                    installation.setReceive(isReceiveOn);
                    installation.saveInBackground();
                    return true;
                }
            });
        }*/

    }
}
