package com.cs442.project.team13;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseUser;

import org.w3c.dom.Text;

public class MainPage extends AppCompatActivity {
    Userdata user;
    private ImageView logo;
    private TextView welcome;


    private int[] ids={
            R.id.category,R.id.sell,R.id.wishlist, R.id.profile,R.id.setting,R.id.logout
    };

    private String[] names={
            "Categories","Sell","Wishlist","Profile","Settings","Logout"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);
        GridView gridView = (GridView) findViewById(R.id.gridview);

        // Set custom adapter (GridAdapter) to gridview

        gridView.setAdapter(new CustomGridAdapter(this, names ) );

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {


                buttonAction(position);

            }
        });

        /*GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageButtonAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                buttonAction(position);
            }
        });
*/
        //gridview.setAdapter(new ButtonAdapter(this));




        /*DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        float dp = 120f;
        float fpixels = metrics.density * dp;
        int pixels = (int) (fpixels + 0.5f);

        for(int position =0; position<6;position++)
        {
            final int position1 = position;
            Button button = new Button(this);
            button.setLayoutParams(new GridView.LayoutParams(pixels, pixels));
            button.setPadding(8, 8, 8, 8);
            button.setBackgroundResource(R.drawable.blue_button);
            button.setText(names[position]);
            button.setAllCaps(false);
            button.setTextSize(18);
            button.setTextColor(Color.WHITE);
            button.setId(ids[position]);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonAction(position1);
                }
            });
        }*/

        /*
        Button button1 = (Button)findViewById(R.id.category);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cs = new Intent(MainPage.this, CategorySearch.class);
                startActivity(cs);
            }
        });

        Button button2 = (Button)findViewById(R.id.sell);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainPage.this, DisplayUserBookListActivity.class);
                startActivity(intent2);
            }
        });

        Button button3 = (Button)findViewById(R.id.wishlist);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(MainPage.this, WishListResult.class);
                Intent ii = getIntent();
                Bundle b = ii.getExtras();
                //final String userId = b.getString("USERID");
                intent3.putExtra("username",b.getString("username"));
                startActivity(intent3);
            }
        });
        Button button4 = (Button)findViewById(R.id.profile);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainPage.this, UpdateProfile.class);
                startActivity(i);
            }
        });
        Button button5 = (Button)findViewById(R.id.setting);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainPage.this, UserSettingActivity.class));
            }
        });
        Button button6 = (Button)findViewById(R.id.logout);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        }); */
    }

    public void buttonAction(int position){
        switch(position) {

            case 0:
                //return true;
                Intent cs = new Intent(this, CategorySearch.class);
                startActivity(cs);
                break;
            case 1:
                Intent intent2 = new Intent(this, DisplayUserBookListActivity.class);
                startActivity(intent2);
                //return true;
                break;
            case 2:
                Intent intent3 = new Intent(this, WishListResult.class);
                Intent ii = getIntent();
                Bundle b = ii.getExtras();
                //final String userId = b.getString("USERID");
                intent3.putExtra("username",b.getString("username"));
                startActivity(intent3);
                //return true;
                break;
            case 3:
                Intent i = new Intent(MainPage.this, UpdateProfile.class);
                startActivity(i);
                //return true;
                break;
            case 4:
                startActivity(new Intent(this, UserSettingActivity.class));
                break;

            case 5:
                logout();
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.activity_main_actions, menu);
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.menu_search:
                return true;
                //break;
            case R.id.action_category:
                //return true;
                Intent cs = new Intent(this, CategorySearch.class);
                startActivity(cs);
                break;
            case R.id.action_sell:
                Intent intent2 = new Intent(this, DisplayUserBookListActivity.class);
                startActivity(intent2);
                //return true;
                break;
            case R.id.action_wishlist:
                Intent intent3 = new Intent(this, WishListResult.class);
                Intent ii = getIntent();
                Bundle b = ii.getExtras();
                //final String userId = b.getString("USERID");
                intent3.putExtra("username",b.getString("username"));
                startActivity(intent3);
                //return true;
                break;
            case R.id.action_profile:
                Intent i = new Intent(MainPage.this, UpdateProfile.class);
                startActivity(i);
                //return true;
                break;
            case R.id.setting:
                startActivity(new Intent(this, UserSettingActivity.class));
                break;

            case R.id.action_logout:
                logout();
                break;
        }
        return true;
    }
    public void logout() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ParseUser.logOut();
                Intent i = new Intent(MainPage.this, LoginActivity.class);
                startActivity(i);
                MainPage.this.finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Are sure do you want to Exit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }
}
