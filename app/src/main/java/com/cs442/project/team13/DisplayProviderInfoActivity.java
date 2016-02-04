package com.cs442.project.team13;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.ByteArrayOutputStream;

public class DisplayProviderInfoActivity extends AppCompatActivity {
    private TextView name;
    private TextView email;
    private TextView phoneNumber;
    private ParseImageView photo;
    private ImageView call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  "+getSupportActionBar().getTitle()));
        }

        setContentView(R.layout.activity_display_provider_info);
        name = (TextView)findViewById(R.id.name);
        email = (TextView)findViewById(R.id.email);
        phoneNumber = (TextView)findViewById(R.id.phoneNumber);
        photo = (ParseImageView)findViewById(R.id.provider_image);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof ImageView)
                    loadPhoto((ImageView)v);

            }
        });
        call = (ImageView)findViewById(R.id.call);
        Intent intent = getIntent();
        String id= intent.getStringExtra("userId");
        if(id!=null)
            displayProviderInfo(id);
    }

    private void displayProviderInfo(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
        query.getInBackground(id.trim(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    Userdata userdata = (Userdata) parseObject;
                    ParseFile image = userdata.getPhoto();
                    if(image == null){

                        Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.personhead);
                        ByteArrayOutputStream bos=new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                        byte[] img=bos.toByteArray();
                        image = new ParseFile(img);
                        try {
                            image.save();
                        } catch (ParseException e1) {
                            image = null;
                        }
                    }
                    /*ImageView imageView = (ImageView) findViewById(android.R.id.provider_image);
                    byte[] bitmapdata = new byte[0];
                    try {
                        bitmapdata = image.getData();
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata , 0, bitmapdata.length);
                        photo.setImageBitmap(bitmap);
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }
                        */
                    photo.setParseFile(image);
                    photo.loadInBackground();


                    name.setText(userdata.getUsername());
                    email.setText(userdata.getEmail());
                    //phoneNumber.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                    phoneNumber.setText(userdata.getPhone());
                    call.setImageResource(R.drawable.call);

                } else {
                    Toast.makeText(DisplayProviderInfoActivity.this, "Provider information not found ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void dialPhone(final View view){

        if(!phoneNumber.getText().toString().isEmpty())
        {



            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    view.getContext());

            // set title
            alertDialogBuilder.setTitle("Call");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Do you want to call provider?")
                    .setCancelable(false)
                    .setPositiveButton("Call",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +phoneNumber.getText().toString()));
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
        }
    }


    private void loadPhoto(ImageView imageView) {

        ImageView tempImageView = imageView;


        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        ImageView image = (ImageView) layout.findViewById(R.id.fullimage);
        image.setImageDrawable(tempImageView.getDrawable());
        imageDialog.setView(layout);
        imageDialog.setPositiveButton("Close", new DialogInterface.OnClickListener(){

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        imageDialog.create();
        imageDialog.show();
    }
}
