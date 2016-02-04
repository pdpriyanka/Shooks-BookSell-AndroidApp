package com.cs442.project.team13;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class SearchBookResultsActivity extends AppCompatActivity {
    private TextView txtQuery;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }

        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();


        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String input = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search


            final ListView listView2=(ListView)findViewById(R.id.searchResult);

            ParseQuery myQuery1 = new ParseQuery("Book");
            myQuery1.whereContains("isbn", input);
            ParseQuery myQuery2 = new ParseQuery("Book");
            myQuery2.whereContains("title", input);
            ParseQuery myQuery3 = new ParseQuery("Book");
            myQuery3.whereContains("author", input);

            List<ParseQuery<Book>> queries = new ArrayList<ParseQuery<Book>>();
            queries.add(myQuery1);
            queries.add(myQuery2);
            queries.add(myQuery3);

            ParseQuery<Book> mainQuery = ParseQuery.or(queries);
            mainQuery.findInBackground(new FindCallback<Book>() {
                public void done(List<Book> objects, ParseException e) {
                    if (e == null) {
                        SearchBookListAdapter searchBookListAdapter = new SearchBookListAdapter(SearchBookResultsActivity.this,objects);
                        searchBookListAdapter.setNotifyOnChange(true);
                        listView2.setAdapter(searchBookListAdapter);
                        if(objects.size()==0){
                            LinearLayout search = (LinearLayout)findViewById(R.id.searchMessage);
                            TextView textView = new TextView(SearchBookResultsActivity.this);
                            textView.setText(" No search result found.");
                            textView.setTextSize(18);
                            textView.setTextColor(Color.WHITE);
                            search.addView(textView);
                        }

                    } else {
                        e.getLocalizedMessage();
                    }
                }
            });

            listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View arg1, int pos, long id) {
                    Book abook = (Book) parent.getItemAtPosition(pos);
                    Intent intent = new Intent();
                    intent.setClass(SearchBookResultsActivity.this, DisplayBookInfoActivity.class);
                    intent.putExtra("id", abook.getObjectId());
                    startActivity(intent);
                }
            });


        }
    }
}

