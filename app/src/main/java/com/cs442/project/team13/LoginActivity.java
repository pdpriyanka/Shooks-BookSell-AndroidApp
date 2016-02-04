package com.cs442.project.team13;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import static com.parse.Parse.initialize;


public class LoginActivity extends ActionBarActivity {

    private ImageView logo;
    private EditText username;
    private EditText password;
    private Button register;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ParseAnalytics.trackAppOpened(getIntent());
        username = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
    }

  /*@Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }*/

  /*
  public void signIn(final View v){
		v.setEnabled(false);
		ParseUser.logInInBackground(mUsernameField.getText().toString(), mPasswordField.getText().toString(), new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				if (user != null) {
					Intent intent = new Intent(LoginActivity.this, TodoActivity.class);
					startActivity(intent);
					finish();
				} else {
					// Signup failed. Look at the ParseException to see what happened.
					switch(e.getCode()){
					case ParseException.USERNAME_TAKEN:
						mErrorField.setText("Sorry, this username has already been taken.");
						break;
					case ParseException.USERNAME_MISSING:
						mErrorField.setText("Sorry, you must supply a username to register.");
						break;
					case ParseException.PASSWORD_MISSING:
						mErrorField.setText("Sorry, you must supply a password to register.");
						break;
					case ParseException.OBJECT_NOT_FOUND:
						mErrorField.setText("Sorry, those credentials were invalid.");
						break;
					default:
						mErrorField.setText(e.getLocalizedMessage());
						break;
					}
					v.setEnabled(true);
				}
			}
		});
	}
   */

    public void signIn(final View v){
        v.setEnabled(false);
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        if(!isConnected){
            Toast.makeText(this,"No internet connection ",Toast.LENGTH_LONG).show();
            login.setEnabled(true);

        }
        else {
            ParseUser.logInInBackground(username.getText().toString(), password.getText().toString(), new LogInCallback() {
                @Override
                public void done(ParseUser user, ParseException e) {
                    if (user != null) {
                        SharedPreferences receive = getSharedPreferences("myPreferences", Context.MODE_PRIVATE);
                        Boolean userIsLogInBefore = receive.contains(username.getText().toString());
                        Boolean isReceive = true;
                        if (userIsLogInBefore) {
                            isReceive = receive.getBoolean(username.getText().toString(), true);
                        } else {
                            SharedPreferences.Editor edit = receive.edit();
                            edit.putBoolean(username.getText().toString(), isReceive);
                            edit.commit();
                        }
                        // Boolean isReceive = receive.getBoolean(username.getText().toString(),true);
                        Installation installation = (Installation) ParseInstallation.getCurrentInstallation();
                        //installation.put("username", username.getText().toString());
                        installation.setUsername(username.getText().toString());
                        installation.setReceive(isReceive);
                        installation.saveInBackground();
                        Intent intent = new Intent(LoginActivity.this, MainPage.class);
                        intent.putExtra("username", username.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        // Signup failed. Look at the ParseException to see what happened.
                        switch (e.getCode()) {
                            case ParseException.USERNAME_TAKEN:
                                //mErrorField.setText("Sorry, this username has already been taken.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, this username has already been taken.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case ParseException.USERNAME_MISSING:
                                //mErrorField.setText("Sorry, you must supply a username to register.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, you must supply a username to login.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case ParseException.PASSWORD_MISSING:
                                //mErrorField.setText("Sorry, you must supply a password to register.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, you must supply a password to login.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            case ParseException.OBJECT_NOT_FOUND:
                                //mErrorField.setText("Sorry, those credentials were invalid.");
                                Toast.makeText(getApplicationContext(),
                                        "Sorry, those credentials were invalid.",
                                        Toast.LENGTH_LONG).show();
                                break;
                            default:
                                //mErrorField.setText(e.getLocalizedMessage());
                                Toast.makeText(getApplicationContext(),
                                        e.getLocalizedMessage(),
                                        Toast.LENGTH_LONG).show();
                                break;
                        }
                        v.setEnabled(true);
                    }
                }
            });
        }
    }
    public void showRegistration(View v) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }
}