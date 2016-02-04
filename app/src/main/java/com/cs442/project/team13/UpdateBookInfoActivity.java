package com.cs442.project.team13;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;

public class UpdateBookInfoActivity extends AppCompatActivity {

    private Book book;
    private EditText isbn;
    private EditText title;
    private EditText price;
    private EditText author;
    private RadioGroup condition;
    private String conditionValue;
    private ParseImageView photo;
    private double latitude;
    private double longitude;
    private boolean setLocation = false;
    private boolean loadBookInfo = true;

    private static final int SELECT_PHOTO = 100;
    private static final int CAMERA_REQUEST = 200;
    private static final int MAP_REQUEST = 300;
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String SETLOCATION = "setLocation";
    public static String CONDITION = "condition";
    public static String LOADBOOKINFO = "loadBookInfo";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }
        setContentView(R.layout.activity_update_book_info);
        String bookId = getIntent().getStringExtra("bookId");
        isbn = (EditText) findViewById(R.id.isbn);
        title = (EditText) findViewById(R.id.title);
        author = (EditText) findViewById(R.id.author);
        condition = (RadioGroup) findViewById(R.id.condition);
        photo = (ParseImageView) findViewById(R.id.book_image);
        price = (EditText) findViewById(R.id.price);

        if(savedInstanceState!=null){
            if(savedInstanceState.containsKey(CONDITION)) {
                conditionValue = savedInstanceState.getString(CONDITION);
            }

            if(savedInstanceState.containsKey(SETLOCATION)) {
                setLocation = savedInstanceState.getBoolean(SETLOCATION);
            }

            if(savedInstanceState.containsKey(LOADBOOKINFO)) {
                loadBookInfo = savedInstanceState.getBoolean(LOADBOOKINFO);
            }

            if(savedInstanceState.containsKey(LATITUDE) &&savedInstanceState.containsKey(LONGITUDE) ) {
                latitude = savedInstanceState.getDouble(LATITUDE);
                longitude = savedInstanceState.getDouble(LONGITUDE);
            }

        }/*else{
            conditionValue="";
            setLocation = false;
            loadBookInfo = true;
        }*/

        if(loadBookInfo == true) {
            if (bookId != null && !bookId.trim().isEmpty()) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Book");
                query.getInBackground(bookId.trim(), new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, com.parse.ParseException e) {
                        if (e == null) {
                            book = (Book) parseObject;



                            photo.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    registerForContextMenu(v);
                                    openContextMenu(v);
                                }
                            });

                            ParseFile image = book.getPhoto();
                            photo.setParseFile(image);
                            photo.loadInBackground();


                            isbn.setText(book.getIsbn());
                            title.setText(book.getTitle());
                            author.setText(book.getAuthor());
                            price.setText(Double.toString(book.getPrice()));

                            int radioButtonId = -1;
                            if (Constants.NEW.equals(book.getCondition())) {
                                radioButtonId = R.id.new1;
                            } else if (Constants.LIKE_NEW.equals(book.getCondition())) {
                                radioButtonId = R.id.likenew;
                            } else if (Constants.USED.equals(book.getCondition())) {
                                radioButtonId = R.id.used;
                            } else if (Constants.DAMAGED.equals(book.getCondition())) {
                                radioButtonId = R.id.damaged;
                            }

                            if (radioButtonId != -1) {
                                RadioButton radioButton = (RadioButton) findViewById(radioButtonId);
                                radioButton.setChecked(true);
                            }

                            loadBookInfo = false;

                            latitude = book.getLatitude();
                            longitude = book.getLongitude();

                        }
                    }
                });
            }
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_book_image,menu);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        switch (item.getItemId()){
            case R.id.take_photo:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
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

    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();


        switch(view.getId()) {
            case R.id.new1:
                if (checked)
                    conditionValue= Constants.NEW;
                break;
            case R.id.likenew:
                if (checked)
                    conditionValue= Constants.LIKE_NEW;
                break;

            case R.id.used:
                if (checked)
                    conditionValue= Constants.USED;
                break;

            case R.id.damaged:
                if (checked)
                    conditionValue= Constants.DAMAGED;
                break;
        }
    }


    private  boolean validation(Context context){
        boolean resultOk = true;
        if(isbn!=null && isbn.getText().toString().isEmpty()){
            Toast.makeText(context, "Please enter the value for isbn. ", Toast.LENGTH_LONG).show();
            resultOk = false;
        }else if(title!=null && title.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter the value for title. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        }else if(author!=null && author.getText().toString().isEmpty()){
            Toast.makeText(context,"Please enter the value for author. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        } else if(conditionValue!=null && conditionValue.isEmpty()){
            Toast.makeText(context,"Please enter the value for condition. ",Toast.LENGTH_LONG).show();
            resultOk = false;
        }

        return resultOk;
    }

    public void update(final View view){

        boolean resultOk = validation(view.getContext());

        if(resultOk){

            resultOk = Validation.getInstance().validateIsbn(isbn.getText().toString());
            if(resultOk == false){
                Toast.makeText(this,"Please enter valid isbn for book.",Toast.LENGTH_LONG).show();
            }
            else
            {

                book.setTitle(title.getText().toString());
                book.setAuthor(author.getText().toString());
                if(conditionValue!=null)
                    book.setCondition(conditionValue);

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

                if(setLocation){
                    book.setSetlocation(setLocation);
                    setLocation = false;
                    book.setLatitude(latitude);
                    book.setLongitude(longitude);
                }


                book.saveEventually(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(view.getContext(), "Your book information is updated.", Toast.LENGTH_LONG).show();
                            setResult(RESULT_OK, getIntent());
                            UpdateBookInfoActivity.this.finish();
                        } else
                            Toast.makeText(view.getContext(), "Error occur during updating book.", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

    }


    public void cancel(View view){
        this.finish();

    }

    public void updateLocation(View v){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra(LATITUDE,latitude);
        intent.putExtra(LONGITUDE,longitude);
        startActivityForResult(intent, MAP_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //from the gallery
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
                }
                break;

            case MAP_REQUEST:

                if(resultCode == RESULT_OK){
                    setLocation = true;
                    latitude = (double)data.getDoubleExtra("latitude",0);
                    longitude = (double)data.getDoubleExtra("longitude",0);
                }
                break;
        }
    }

    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putDouble(LATITUDE, latitude);
        savedInstanceState.putDouble(LONGITUDE, longitude);
        savedInstanceState.putString(CONDITION,conditionValue);
        savedInstanceState.putBoolean(SETLOCATION, setLocation);
        savedInstanceState.putBoolean(LOADBOOKINFO, loadBookInfo);

    }

}
