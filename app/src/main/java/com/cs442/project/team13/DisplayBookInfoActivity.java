package com.cs442.project.team13;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DisplayBookInfoActivity extends AppCompatActivity {

    private TextView isbn;
    private TextView title;
    private TextView author;
    private TextView price;
    private TextView state;
    private TextView university;
    private TextView department;
    private TextView class1;
    private TextView professor;
    private RadioGroup condition;
    private ParseImageView photo;
    private String userId;
    private double latitude;
    private double longitude;
    boolean setLocation;
    private String email="";
    private String usrId;
    private String bookId;

    private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;

    // note that these credentials will differ between live & sandbox environments.
    private static final String CONFIG_CLIENT_ID = "AWx2GsDsBERFFJyXwvv3zhKgS3umLOv8C-3xN-DQyE-EulUojrgV28u_5SGC3K7ihPI2uquKgT6Z35h2";

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(CONFIG_ENVIRONMENT)
            .clientId(CONFIG_CLIENT_ID)
                    // The following are only used in PayPalFuturePaymentActivity.
            .merchantName("Example Merchant")
            .merchantPrivacyPolicyUri(Uri.parse("https://www.example.com/privacy"))
            .merchantUserAgreementUri(Uri.parse("https://www.example.com/legal"));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }

        setContentView(R.layout.activity_display_book_info);
        isbn = (TextView)findViewById(R.id.isbn);
        title = (TextView)findViewById(R.id.title);
        author = (TextView)findViewById(R.id.author);
        price = (TextView)findViewById(R.id.price);
        condition = (RadioGroup)findViewById(R.id.condition);
        photo = (ParseImageView)findViewById(R.id.book_image);
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v instanceof ImageView)
                    loadPhoto((ImageView)v);

            }
        });

        state = (TextView)findViewById(R.id.stateb);
        university = (TextView)findViewById(R.id.universityb);
        department = (TextView)findViewById(R.id.departmentb);
        class1 = (TextView)findViewById(R.id.class1b);
        professor = (TextView)findViewById(R.id.professorb);
        ((TextView)findViewById(R.id.categories)).setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);


        Intent intent = getIntent();
        boolean providerDetail = intent.getBooleanExtra("providerDetail", true);
        if(!providerDetail)
        {
                    ((Button) findViewById(R.id.provider_info)).setVisibility(View.GONE);
                    ((Button) findViewById(R.id.buyButton)).setVisibility(View.GONE);
        }
        String id= intent.getStringExtra("id");
        if(id!=null)
            bookId = id;
            displayBookInfo(id);

        Intent intt = new Intent(this, PayPalService.class);
        intt.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intt);

    }

    private void displayBookInfo(String id){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    Book book = (Book) parseObject;
                    ParseFile image = book.getPhoto();
                    photo.setParseFile(image);
                    photo.loadInBackground();

                    isbn.setText(book.getIsbn());
                    title.setText(book.getTitle());
                    author.setText(book.getAuthor());
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
                    price.setText(Double.toString(book.getPrice()));


                    state.setText(book.getState());
                    university.setText(book.getUniversity());
                    department.setText(book.getDepartment());
                    class1.setText(book.getClass1());
                    professor.setText(book.getProfessor());

                    userId = book.getUserId();
                    latitude = book.getLatitude();
                    longitude = book.getLongitude();

                    setLocation = book.getSetlocation();

                } else {
                    Toast.makeText(DisplayBookInfoActivity.this, "Book information not found", Toast.LENGTH_LONG).show();
                }

            }

        });

    }

        public void showProviderInfo(View view){

            if(userId!=null && !userId.isEmpty()) {
                Intent intent = new Intent(this, DisplayProviderInfoActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
            }
            else{
                Toast.makeText(DisplayBookInfoActivity.this, "Provider information not found.", Toast.LENGTH_LONG).show();
            }
        }

    public void showBookLocation(View view){
        if(setLocation)
        {
            Intent intent = new Intent(DisplayBookInfoActivity.this, DisplayBookLocationActivity.class);
            intent.putExtra("latitude", latitude);
            intent.putExtra("longitude", longitude);
            startActivity(intent);
        }else {
            Toast.makeText(DisplayBookInfoActivity.this, "Location is not set for this book.", Toast.LENGTH_LONG).show();
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
        imageDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }

        });

        Dialog d =imageDialog.create();
        //imageDialog.show();


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(d.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        d.show();
        d.getWindow().setAttributes(lp);

    }
    public void buyBook(View view){
        ParseQuery query = ParseQuery.getQuery("_User");
        query.whereEqualTo("objectId", userId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e == null) {
                    ArrayList<String> stateList = new ArrayList<>();
                    for (ParseObject o : list) {
                        int count = 0;
                        if (!stateList.isEmpty()) {
                            for (String a : stateList) {
                                String b = o.getString("email");
                                boolean stt = b.equals(a);
                                if (stt) {
                                    count++;
                                }
                            }
                            if (count == 0) {
                                stateList.add(o.getString("email"));
                                stateList.add(o.getString("username"));
                            }
                        } else {
                            stateList.add(o.getString("email"));
                            stateList.add(o.getString("username"));
                        }
                    }

                    email = stateList.get(0);
                    usrId = stateList.get(1);
                    makePayment(email, usrId, Double.parseDouble(price.getText().toString()));


                    Log.d("UserID", " Price" + price.getText().toString() + " Email " + email);
                } else {
                    Toast.makeText(DisplayBookInfoActivity.this, "Provider information not found ", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void makePayment(String recipient, String sellerName, double amount){
        PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(""+amount), "USD", ""+title.getText() ,PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(DisplayBookInfoActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
        startActivityForResult(intent, 1);
    }

    public void removeBook(String id){
        Log.d("Got to", "Start Removing");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Book");
        query.getInBackground(id, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, com.parse.ParseException e) {
                if (e == null) {
                    Book book = (Book) parseObject;
                    book.deleteInBackground();
                    Log.d("Removed", "Book");
                } else {
                    Toast.makeText(DisplayBookInfoActivity.this, "Book information not found", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("Got Result", "");
      if (resultCode == RESULT_OK) {
          PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
          Intent intent = new Intent(this,PurchaseReceipt.class);
          intent.putExtra("userId","Owner:"+ usrId);
          String str = "Title: "+title.getText().toString()+" \n Price: "+confirm.getPayment().toString().substring(21,confirm.getPayment().toString().length()-7)+" \n Author: "+author.getText().toString()+" \n ISBN: "+ isbn.getText().toString();
          intent.putExtra("paymentData",str);
          removeBook(bookId);
          Log.d("Removed Books","now to Receipt");
          startActivity(intent);
        if (confirm != null) {
            try {
                Log.d("paymentExample", confirm.toJSONObject().toString(4));
            } catch (JSONException e) {
                Log.e("paymentExample", "an extremely unlikely failure occurred: ", e);
            }
        }
      } else if (resultCode == RESULT_CANCELED) {
      } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
      }
    }




}
