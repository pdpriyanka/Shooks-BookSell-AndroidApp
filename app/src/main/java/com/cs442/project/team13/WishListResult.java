package com.cs442.project.team13;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;
import java.util.regex.Pattern;

import java.util.regex.Matcher;

public class WishListResult extends AppCompatActivity {

    private LinearLayout container;
    private EditText textIn;
    private EditText isbn;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wishlistresult);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }

        Intent i = getIntent();
        Bundle b = i.getExtras();
        userId = b.getString("username");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("WishListTable");
        query.whereContains("USERID",userId);

        container = (LinearLayout)findViewById(R.id.container);

        textIn = (EditText)findViewById(R.id.title);
        isbn = (EditText)findViewById(R.id.isbn);

        Button buttonAdd = (Button)findViewById(R.id.add);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    for (ParseObject o : list) {
                        LayoutInflater layoutInflater =
                                (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View addView = layoutInflater.inflate(R.layout.row, null);
                        TextView textOut = (TextView) addView.findViewById(R.id.textout);
                        String title = o.getString("TITLE");
                        String isbn = o.getString("ISBN");
                        final String objectid = o.getObjectId();
                        textOut.setText("ISBN - " + isbn+"\nTitle - "+title);
                        ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);
                        buttonRemove.setOnClickListener(new OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                ((LinearLayout) addView.getParent()).removeView(addView);
                                ParseQuery<ParseObject> q = ParseQuery.getQuery("WishListTable");
                                q.whereEqualTo("objectId", objectid);
                                q.getFirstInBackground(new GetCallback<ParseObject>() {
                                    @Override
                                    public void done(ParseObject parseObject, ParseException e) {
                                        if (e == null) {
                                            parseObject.deleteInBackground();
                                        } else {
                                            Toast.makeText(getApplication(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        });

                        container.addView(addView);

                    }
                    ParseQuery<ParseObject> check_available = ParseQuery.getQuery("WishListTable");
                    check_available.whereContains("USERID", userId);
                    check_available.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (e == null) {
                                for (ParseObject o : objects) {
                                    final String wishTitle = o.getString("TITLE");
                                    final String wishisbn = o.getString("ISBN");
                                    ParseQuery<ParseObject> findBook = ParseQuery.getQuery("Book");
                                    findBook.whereEqualTo("isbn", wishisbn);
                                    findBook.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> objects, ParseException e) {
                                            if (e == null) {
                                                int size = objects.size();
                                                if (size > 0) {
                                                    ParsePush parsePush = new ParsePush();
                                                    ParseQuery pQuery = ParseInstallation.getQuery();
                                                    pQuery.whereEqualTo("username", userId).whereEqualTo("isReceive",true);
                                                    parsePush.sendMessageInBackground("This item " + wishTitle + " in your wish list is available", pQuery);
                                                    //Toast.makeText(getApplication(), wishTitle+" is found", Toast.LENGTH_LONG).show();
                                                }
                                            } else {
                                                Toast.makeText(getApplication(), e.getLocalizedMessage() + "!!!!!", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(getApplication(), e.getLocalizedMessage() + "~~~~~", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplication(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });


        buttonAdd.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                save();

            }
        });

    }

    public void save(){
        final String title = textIn.getText().toString();
        final String isbnin = isbn.getText().toString();
        if (!title.isEmpty() && !isbnin.isEmpty()) {
            if (new Validation().validateIsbn(isbnin)) {
                ParseQuery<ParseObject> checkIsbnQuery = ParseQuery.getQuery("WishListTable");
                //checkIsbnQuery.whereContains("USERID",userId);
                checkIsbnQuery.whereContains("USERID", userId).whereEqualTo("ISBN", isbnin);
                checkIsbnQuery.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            //object exists
                            Toast.makeText(getApplication(), "It has already existed in you wishlist", Toast.LENGTH_LONG).show();
                        } else {

                            //object doesn't exist
                            LayoutInflater layoutInflater =
                                    (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View addView = layoutInflater.inflate(R.layout.row, null);
                            TextView textOut = (TextView) addView.findViewById(R.id.textout);
                            //*final String title = titltIn.getText().toString();
                            //final String isbnin = isbn.getText().toString();//*
                            textOut.setText("ISBN - " + isbnin + "\nTitle - " + title);
                            WishListTable wishlist = new WishListTable();
                            wishlist.setIsbn(isbnin);
                            wishlist.setTitle(title);
                            wishlist.setUserId(userId);
                            wishlist.saveInBackground();

                            ImageButton buttonRemove = (ImageButton) addView.findViewById(R.id.remove);
                            //buttonRemove.setBackground(getResources().getDrawable(R.drawable.button_gradient));
                            //buttonRemove.setOnClickListener(new OnClickListener() {
                            buttonRemove.setOnClickListener(new OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    ((LinearLayout) addView.getParent()).removeView(addView);
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("WishListTable");
                                    query.whereEqualTo("TITLE", title);
                                    query.whereEqualTo("ISBN", isbnin);
                                    query.whereEqualTo("USERID", userId);
                                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                                        @Override
                                        public void done(ParseObject parseObject, ParseException e) {
                                            if (e == null) {
                                                parseObject.deleteInBackground();
                                            } else {
                                                Toast.makeText(getApplication(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }
                            });
                            container.addView(addView);
                            textIn.setText("");
                            isbn.setText("");
                        }
                    }
                });

            } else {
                Toast.makeText(getApplication(), "Invalid ISBN!", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(getApplication(), "Title and/or ISBN fields cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

}
