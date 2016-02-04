package com.cs442.project.team13;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class PostBookActivity extends AppCompatActivity {
    private EditText isbn;
    private EditText title;
    private EditText author;
    private EditText price;
    private String condition;
    private ImageView photo;
    private double latitude;
    private double longitude;
    private boolean setLocation = false;
    private boolean imageUploaded = false;

    private static final int SELECT_PHOTO = 100;
    private static final int CAMERA_REQUEST = 200;
    private static final int MAP_REQUEST = 300;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1000;
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String SETLOCATION = "setLocation";
    public static String CONDITION = "condition";
    public static String IMAGEUPLOADED = "imageUploaded";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }

        setContentView(R.layout.activity_post_book);
        isbn = (EditText)findViewById(R.id.isbn);
        title = (EditText)findViewById(R.id.title);
        author = (EditText)findViewById(R.id.author);
        price = (EditText)findViewById(R.id.price);
        photo = (ImageView)findViewById(R.id.book_image);

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerForContextMenu(v);
                openContextMenu(v);
            }
        });


        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(CONDITION)) {
                condition = savedInstanceState.getString(CONDITION);
            }

            if(savedInstanceState.containsKey(SETLOCATION)) {
                setLocation = savedInstanceState.getBoolean(SETLOCATION);
            }
            if(savedInstanceState.containsKey(LATITUDE) &&savedInstanceState.containsKey(LONGITUDE) ) {
                    latitude = savedInstanceState.getDouble(LATITUDE);
                longitude = savedInstanceState.getDouble(LONGITUDE);
            }

            if(savedInstanceState.containsKey(IMAGEUPLOADED)) {
                imageUploaded = savedInstanceState.getBoolean(IMAGEUPLOADED);
            }
        }

    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch(view.getId()) {
            case R.id.new1:
                if (checked)
                    condition= Constants.NEW;
                    break;
            case R.id.likenew:
                if (checked)
                    condition= Constants.LIKE_NEW;
                    break;

            case R.id.used:
                if (checked)
                    condition= Constants.USED;
                    break;

            case R.id.damaged:
                if (checked)
                    condition= Constants.DAMAGED;
                    break;
        }
    }


    private  boolean validation(Context context){
        boolean resultOk = true;
        if(isbn!=null && isbn.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter the value for isbn. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        }else if(title!=null && title.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter the value for title. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        }else if(author!=null && author.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter the value for author. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        } else if(condition.isEmpty()){
            Toast.makeText(context,"Please enter the value for condition. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        }

        return resultOk;
    }

    public void save(final View view){

        boolean resultOk = validation(view.getContext());

        if(resultOk){

            resultOk = Validation.getInstance().validateIsbn(isbn.getText().toString());
            if(!resultOk){
                Toast.makeText(this,"Please enter valid isbn for book.",Toast.LENGTH_LONG).show();
            }
            else
            {
                final Book book = new Book();
                book.setIsbn(isbn.getText().toString());
                book.setTitle(title.getText().toString());
                book.setAuthor(author.getText().toString());
                book.setCondition(condition);
                book.setUserId(ParseUser.getCurrentUser().getObjectId());
                if (!imageUploaded)
                {
                    photo.setImageResource(R.drawable.no_book_image);
                }

                Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageInByte = stream.toByteArray();
                ParseFile parseFile = new ParseFile(imageInByte);
                try {
                    parseFile.save();
                    book.setPhoto(parseFile);
                } catch (ParseException e) {
                    Toast.makeText(view.getContext(), "Error in saving book image.", Toast.LENGTH_LONG).show();
                }

                if (!price.getText().toString().isEmpty()) {
                    book.setPrice(Double.parseDouble(price.getText().toString()));
                }
                String tempString = getIntent().getStringExtra("state");
                if(tempString!=null){
                    book.setState(tempString);
                }

                tempString = getIntent().getStringExtra("university");
                if(tempString!=null){
                    book.setUniversity(tempString);
                }

                tempString = getIntent().getStringExtra("department");
                if(tempString!=null){
                    book.setDepartment(tempString);
                }

                tempString = getIntent().getStringExtra("class");
                if(tempString!=null){
                    book.setClass1(tempString);
                }

                tempString = getIntent().getStringExtra("professor");
                if(tempString!=null){
                    book.setProfessor(tempString);
                }

                if(setLocation){
                    book.setSetlocation(setLocation);
                    setLocation = false;
                    book.setLatitude(latitude);
                    book.setLongitude(longitude);
                }

                book.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                        {   //check this book is match other user's wishlist book
                            ParseQuery<ParseObject> check_available = ParseQuery.getQuery("WishListTable");
                            check_available.whereEqualTo("ISBN",isbn.getText().toString());
                            check_available.findInBackground(new FindCallback<ParseObject>() {
                                @Override
                                public void done(List<ParseObject> objects, ParseException e) {
                                    if(e==null) {
                                        for(ParseObject o: objects) {
                                            String username = o.getString("USERID");
                                            ParsePush parsePush = new ParsePush();
                                            ParseQuery pQuery = ParseInstallation.getQuery();
                                            pQuery.whereEqualTo("username", username).whereEqualTo("isReceive",true);
                                            parsePush.sendMessageInBackground("This item " + title.getText().toString() + " in your wish list is available", pQuery);
                                        }
                                    } else {
                                        Toast.makeText(view.getContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            Toast.makeText(view.getContext(), "Your book is posted.", Toast.LENGTH_LONG).show();
                            getIntent().putExtra("bookId", book.getObjectId());
                            setResult(RESULT_OK, getIntent());
                            PostBookActivity.this.finish();
                        }
                        else
                        Toast.makeText(view.getContext(), "Error occur during posting book.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }

    public void cancel(View view){
        this.finish();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case SELECT_PHOTO:
                if (resultCode == RESULT_OK && null != data) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();

                    photo.setImageBitmap(BitmapFactory.decodeFile(picturePath));

                }
                break;

            case CAMERA_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap photo1 = (Bitmap) data.getExtras().get("data");
                    photo.setImageBitmap(photo1);
                    imageUploaded = true;
                }



                break;

            case MAP_REQUEST:

                if(resultCode == RESULT_OK){
                    setLocation = true;
                    latitude = (double)data.getDoubleExtra("latitude",1000);
                    longitude = (double)data.getDoubleExtra("longitude",1000);
                    imageUploaded = true;
                }
                break;
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_book_image, menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case R.id.take_photo:
                PackageManager pm = getPackageManager();
                if(pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                   startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
                else {
                    Toast.makeText(this,"Your device does not have camera feature",Toast.LENGTH_LONG).show();
                }
                return true;

            case R.id.choose_photo:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                return true;

            case R.id.cancel:
                return false;
            default:
                return false;
        }
    }


    public void setLocation(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, MAP_REQUEST);
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble(LATITUDE, latitude);
        savedInstanceState.putDouble(LONGITUDE, longitude);
        savedInstanceState.putString(CONDITION,condition);
        savedInstanceState.putBoolean(SETLOCATION,setLocation);
        savedInstanceState.putBoolean(IMAGEUPLOADED, imageUploaded);

    }
}

