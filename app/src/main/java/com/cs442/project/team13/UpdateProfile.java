package com.cs442.project.team13;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.ParseImageView;


import android.widget.Toast;

import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

/**
 * Created by bachelor_2009 on 11/2/2015.
 */
public class UpdateProfile extends Activity {

    EditText update_name;
    EditText update_pass;
    EditText update_passconf;
    EditText update_phone;
    ParseImageView imageView;
    //ImageView photo;
    Button take;
    ParseFile photofile;
    Userdata user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        imageView = (ParseImageView) findViewById(R.id.profile_photo);
        update_name = (EditText) findViewById(R.id.update_name);
        update_pass = (EditText) findViewById(R.id.update_password);
        update_passconf = (EditText) findViewById(R.id.update_confpass);
        update_phone = (EditText) findViewById(R.id.update_phone);
        take = (Button) findViewById(R.id.update_photo);
        user = (Userdata)ParseUser.getCurrentUser();
        photofile = user.getPhoto();
        imageView.setParseFile(photofile);
        imageView.loadInBackground();

        update_name.setText(user.getUsername());
        update_phone.setText(user.getPhone());


        take.setOnClickListener(new View.OnClickListener() {
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

        if(data.hasExtra("data")) {
            Bitmap bp = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(bp);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] pic = stream.toByteArray();

            if (pic != null) {
                String input = update_name.getText().toString();

                photofile = new ParseFile(input + ".jpg", pic);

            }
        } else {
            Toast.makeText(this,
                    "No Picture taken",
                    Toast.LENGTH_LONG).show();
        }

    }
    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);


        Bitmap bp = (Bitmap) data.getExtras().get("data");
        imageView.setImageBitmap(bp);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] pic = stream.toByteArray();

        if(pic != null) {
            String input = update_name.getText().toString();

            photofile = new ParseFile(input+".jpg",pic);

        }

    }*/


    public void updateUser(View v) {
        Validation validation = Validation.getInstance();
        String name = update_name.getText().toString();
        String pass = update_pass.getText().toString();
        String passconf = update_passconf.getText().toString();
        String phone = update_phone.getText().toString();
        if(!pass.equals(passconf)) {
            Toast.makeText(this,
                    "Two passwords do not match, password does not change.",
                    Toast.LENGTH_LONG).show();
        } else if(pass.equals(passconf)&&!pass.isEmpty()){
            user.setPassword(pass);
        }
        if (!name.isEmpty()) {
            user.setUsername(name);
        }
        if (!phone.isEmpty()) {
            //user.setPhone(phone);
            if(validation.validatePhoneNumber(phone)) {
                user.setPhone(phone);
            } else {
                Toast.makeText(this,
                        "Invalid phone number, phone number does not change. ",
                        Toast.LENGTH_LONG).show();
            }
        }
        if (photofile != null) {
            try {
                photofile.save();
                user.setPhoto(photofile);
            } catch (ParseException e) {
                Toast.makeText(this,
                        "Error with saving picture",
                        Toast.LENGTH_LONG).show();
            }
        }

        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getApplicationContext(),
                            "Update successfully",
                            Toast.LENGTH_LONG).show();
                } else {
                    switch (e.getCode()){
                        default:
                            Toast.makeText(getApplicationContext(),
                                    e.getLocalizedMessage(),
                                    Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);
        finish();
    }


}

