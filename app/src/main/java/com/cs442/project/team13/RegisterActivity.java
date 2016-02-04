package com.cs442.project.team13;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bachelor_2009 on 10/30/2015.
 */
public class RegisterActivity extends Activity {


    EditText name;
    EditText password;
    EditText phone;
    EditText passwordconf;
    ImageView photo;
    EditText email;
    int name_count;
    int email_count;
    //Userdata u;
    Button save;
    Button login;
    ParseFile photofile;
    SharedPreferences s ;
    HashMap<String, Boolean> userList;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        s= getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        userList  = new HashMap<String, Boolean>();
        Button takephoto1 = (Button) findViewById(R.id.takephoto);
        photo = (ImageView) findViewById(R.id.photo);
        name = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        phone = (EditText) findViewById(R.id.phone);
        passwordconf = (EditText) findViewById(R.id.confPass);
        email = (EditText) findViewById(R.id.email);
        save = (Button) findViewById(R.id.save);
        login = (Button) findViewById(R.id.login);
        //u = new Userdata();
        takephoto1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1337);
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(data!=null && data.hasExtra("data")) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            photo.setImageBitmap(bp);
            /*ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] pic = stream.toByteArray();

            if (pic != null) {
                String input = name.getText().toString();

                photofile = new ParseFile(input + ".jpg", pic);

            }*/
        } else {
            Toast.makeText(this,
                    "No Picture taken",
                    Toast.LENGTH_LONG).show();
        }

    }

    public void register(final View v){

        Validation validation = Validation.getInstance();
        final String input_name = name.getText().toString();
        String input_email = email.getText().toString();
        String input_phone = phone.getText().toString();
        String input_pass = password.getText().toString();
        String input_condpass = passwordconf.getText().toString();
        if(input_email.isEmpty()||input_name.isEmpty()||input_pass.isEmpty()) {
            Toast.makeText(getApplicationContext(),
                    "Name, Password,Email fields cannot be empty",
                    Toast.LENGTH_LONG).show();
        } else if(!input_condpass.equals(input_pass)) {
            Toast.makeText(this,
                    "Two passwords do not match",
                    Toast.LENGTH_LONG).show();
        }  else if(!validation.validateEmail(input_email)){
            Toast.makeText(this,
                    "Invalid Email Address",
                    Toast.LENGTH_LONG).show();
        } else if(!validation.validatePhoneNumber(input_phone)){
            Toast.makeText(this,
                    "Invalid Phone Number",
                    Toast.LENGTH_LONG).show();
        }else {
            v.setEnabled(false);
            Userdata user = new Userdata();
            user.setPassword(input_pass);
            user.setEmail(input_email);
            user.setUsername(input_name);
            Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] imageInByte = stream.toByteArray();
            ParseFile parseFile = new ParseFile(imageInByte);
            try {
                parseFile.save();
                user.setPhoto(parseFile);
            } catch (ParseException e) {
                Toast.makeText(v.getContext(), "Error in saving user image.", Toast.LENGTH_LONG).show();
            }
            /*if (photofile != null) {
                try {

                    photofile.save();
                    user.setPhoto(photofile);
                } catch (ParseException e) {
                    Toast.makeText(this,
                            "Error with saving picture",
                            Toast.LENGTH_LONG).show();
                }
            }*/
            //user.setPhone(input_phone);
            if(!input_phone.isEmpty()) {
                user.setPhone(input_phone);

            }
            ParseUser currentUser = ParseUser.getCurrentUser();
            currentUser.logOut();

            user.signUpInBackground(new SignUpCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Installation installation = (Installation)ParseInstallation.getCurrentInstallation();
                        //installation.put("username", input_name);
                        installation.setUsername(input_name);
                        installation.setReceive(true);
                        installation.saveInBackground();
                        //add new user into userList , which is a hashmap, and then add hashmap into sharedpreferences
                        userList.put(input_name,true);
                        SharedPreferences.Editor edit = s.edit();
                        /*
                        SharedPreferences.Editor editor = getSharedPreferences(PREFS_NAME, 0).edit();
                        for( Entry entry : backUpCurency_values.entrySet() )
                        editor.putString( entry.getKey(), entry.getValue() );
                        editor.commit();
                         */
                        for(Map.Entry entry: userList.entrySet()) {
                            edit.putBoolean((String)entry.getKey(), (Boolean)entry.getValue());
                            edit.commit();
                        }
                        Intent i = new Intent(RegisterActivity.this, MainPage.class);
                        i.putExtra("username", input_name);
                        startActivity(i);
                        finish();
                       // startActivity(i);
                        //finish();
                    } else {
                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN:
                                //mErrorField.setText("Sorry, this username has already been taken.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, this username has already been taken.",
                                        Toast.LENGTH_LONG).show();

                                break;
                            case ParseException.USERNAME_MISSING:
                                //mErrorField.setText("Sorry, you must supply a username to register.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, you must supply a username to register.",
                                        Toast.LENGTH_LONG).show();

                                break;
                            case ParseException.PASSWORD_MISSING:
                                //mErrorField.setText("Sorry, you must supply a password to register.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, you must supply a password to register.",
                                        Toast.LENGTH_LONG).show();

                                break;
                            case ParseException.EMAIL_MISSING:
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, you must supply a email to register.",
                                        Toast.LENGTH_LONG).show();

                                break;
                            case ParseException.EMAIL_TAKEN:
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, this email has already been taken.",
                                        Toast.LENGTH_LONG).show();

                                break;

                            default:
                                //mErrorField.setText(e.getLocalizedMessage());
                                Toast.makeText(getApplicationContext(),
                                        e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();

                        }
                        v.setEnabled(true);
                    }
                }
            });
        }
    }

    public void showLogin(View v) {
        //s= getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        //SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //stringList = s.getStringSet("store", stringList);
        //Boolean receive = s.getBoolean("isReceive", true);
        SharedPreferences receive = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
        Boolean isReceive = receive.getBoolean(name.getText().toString(),true);
        Installation installation = (Installation)ParseInstallation.getCurrentInstallation();
        //installation.put("username", input_name);
        installation.setUsername(name.getText().toString());
        installation.setReceive(isReceive);
        installation.saveInBackground();
        Intent intent = new Intent(this, LoginActivity.class);
        //Bundle bundle = new Bundle();
        //bundle.putString("username",name.getText().toString());
        intent.putExtra("username",name.getText().toString());
        startActivity(intent);
        finish();
    }

    public void reflesh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
