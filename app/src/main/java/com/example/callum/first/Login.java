package com.example.callum.first;
//Importing all of the external sources needed
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.callum.first.util.DataBaseHelper;
import com.example.callum.first.util.Globalclass;

import java.util.Locale;

public class Login extends AppCompatActivity  implements View.OnClickListener, TextToSpeech.OnInitListener {
//  declaring variables
DataBaseHelper myDbHelper = new DataBaseHelper(this);
    Globalclass g1 = new Globalclass();
    EditText password;
    EditText username;
    Button signIn;
    Button signUp;
    TextView progress;
//    DatabaseHandler db = new DatabaseHandler(this);
    @Override
//    this runs when the intent is sent to start the login function
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Setting the GUI to show
        setContentView(R.layout.activity_login);
//      setting all of the vairable values needed
        password=(EditText)findViewById(R.id.Password_Box);
        username=(EditText)findViewById(R.id.Username_Box);
        signIn = (Button) findViewById(R.id.Sign_In_Button);
        signIn.setOnClickListener(this);
        signUp = (Button) findViewById(R.id.Sign_Up_Button);
        signUp.setOnClickListener(this);
        progress = (TextView)findViewById(R.id.progress);
    }

    @Override
//    this runs when the options menu is created
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
//    when an item on the options menu is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent("android.intent.action.util.Settings"));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void notValid(){
        username.setText("");
        password.setText("");
    }

    public void inClick() {
        //if their username is not there run sign up
        if (!myDbHelper.CheckIsDataAlreadyInDBorNot("Users","username","'"+username.getText().toString()+"'")) {
            progress.setText("That is not a username, please press sign Up to add it.");
        } else {
            if (myDbHelper.CheckIsDataAlreadyInDBorNot(password.getText().toString(),username.getText().toString())) {
                try {
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);

                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }catch (Exception e ){
                    Log.d("Error","hiding keyboard");
                }
                Log.d("Logged", "IN");
                myDbHelper.logOut();
                myDbHelper.logIn(username.getText().toString());
                progress.setText(username.getText().toString()+" you have logged in.");
//                progress.setText(myDbHelper.read("Users","loggedIn","username = 'user'"));
//TextToSpeech tts = new TextToSpeech(this,this);
//                tts.setLanguage(Locale.US);
//                tts.speak("Welcome"+(username.getText().toString())+"",TextToSpeech.QUEUE_ADD,null);
            }else{
                progress.setText("incorrect username or password");
            }

//        boolean valid= false;
//        username.setText("");
////        valid = g1.signIn(username.getText().toString(), password.getText().toString());
//        valid= g1.signIn("cal","pi");

//        if(!valid){
//            notValid();
//        }
//        g1.signIn();
//        username.setText("bpo");
        }
    }
    public void upClick() {
        if(!username.getText().equals(null)) {
            if (!myDbHelper.CheckIsDataAlreadyInDBorNot("Users", "username", "'" + username.getText().toString() + "'")) {
                myDbHelper.adduser(username.getText().toString(), password.getText().toString());
                Log.d("username", "created");
                progress.setText(username.getText().toString() + " you have just created an account.");
                myDbHelper.logOut();
                myDbHelper.logIn(username.getText().toString());
            } else {
                Log.d("username", "exists sorry");
                progress.setText("Username: " + username.getText().toString() + " already exists.");
            }

//        db.init();
//        db.addUser(username.getText().toString(), password.getText().toString());
//        signUp.setText(""+g1.getLevel());
//        String heyy[][]=db.getAllUsers();
//                for (int i = 0;i<10;i++){
//            for (int p =0;i<=2;i++){
//                Log.d("Array",heyy[i][p]);
//            }

//        }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.Sign_In_Button:
                inClick();
                break;
            case R.id.Sign_Up_Button:
                upClick();
                break;
        }
    }


    @Override
    public void onInit(int i) {

    }
}
