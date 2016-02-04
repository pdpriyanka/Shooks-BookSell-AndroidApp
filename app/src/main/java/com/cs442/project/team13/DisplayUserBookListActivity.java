package com.cs442.project.team13;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class DisplayUserBookListActivity extends AppCompatActivity {


    private ListView booksList;
    private UserBookListAdapter userBookListAdapter;
    private static final int POST_BOOK = 111;
    private static final int UPDATE_BOOK = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setLogo(R.drawable.logo);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setTitle(TextUtils.concat("  " + getSupportActionBar().getTitle()));
        }
        setContentView(R.layout.activity_display_user_book_list);
        userBookListAdapter = new UserBookListAdapter(this, new ArrayList<Book>());
        userBookListAdapter.setNotifyOnChange(true);

        booksList = (ListView) findViewById(R.id.booksList);

        booksList.setAdapter(userBookListAdapter);

      //  booksList.setOnItemClickListener(this);

        loadData();
    }


    public void loadData(){
        ParseQuery<Book> query = ParseQuery.getQuery(Book.class);
        query.whereEqualTo("userId", ParseUser.getCurrentUser().getObjectId()).addAscendingOrder("createdAt");

        query.findInBackground(new FindCallback<Book>() {
            @Override
            public void done(List<Book> books, ParseException error) {
                if (books != null) {
                    userBookListAdapter.clear();
                    userBookListAdapter.addAll(books);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_user_book_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.post) {
            Intent intent = new Intent(this, BookCategoryActivity.class);
            startActivityForResult(intent, POST_BOOK);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {

            case POST_BOOK:

                if (resultCode == RESULT_OK && null != data && data.getStringExtra("bookId")!=null) {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Book").whereEqualTo("userId",ParseUser.getCurrentUser().getObjectId());
                    query.getInBackground(data.getStringExtra("bookId").trim(), new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, com.parse.ParseException e) {
                            if (e == null) {
                                userBookListAdapter.add((Book)parseObject);
                                userBookListAdapter.notifyDataSetChanged();
                            }
                        }
                    });

                }
                break;

            case UPDATE_BOOK:
                if (resultCode == RESULT_OK ) {
                    loadData();
                }
                break;
        }
    }


    public void update(View v,String bookId){
        Intent intent = new Intent(DisplayUserBookListActivity.this,UpdateBookInfoActivity.class);
        intent.putExtra("bookId", bookId);
        startActivityForResult(intent, UPDATE_BOOK);
    }
    public void delete(View v,Book book){

        showDeleteConfirmAlert(v,book);

    }

    private void showDeleteConfirmAlert(final View v,final Book book){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Delete");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want delete this book information?");



        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {

                dialog.dismiss();
                book.deleteInBackground();
                userBookListAdapter.remove(book);
                userBookListAdapter.notifyDataSetChanged();
                Toast.makeText(v.getContext(), "Your book information is deleted.", Toast.LENGTH_SHORT).show();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}

