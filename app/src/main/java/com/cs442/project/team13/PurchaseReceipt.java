package com.cs442.project.team13;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class PurchaseReceipt extends AppCompatActivity {

    private TextView purchaseInfo;
    private TextView purchaseVendor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Start Receipt","ok");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_receipt);

        Intent intent = getIntent();
        String usrId = intent.getStringExtra("userId").toString();
        String data = intent.getStringExtra("paymentData").toString();
        purchaseVendor = (TextView) findViewById(R.id.purchaseVendor);
        purchaseVendor.setText(usrId+"");
        purchaseInfo = (TextView) findViewById(R.id.purchaseInfo);
        purchaseInfo.setText(data);

        Log.d("Sent Receipt","");

    }
    public void continueShopping(View v){
        Intent intent = new Intent(this, MainPage.class);
        startActivity(intent);

    }
}
