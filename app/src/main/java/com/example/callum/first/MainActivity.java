package com.example.callum.first;
//Declaring all of the imports needed
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.callum.first.util.DataBaseHelper;
import com.example.callum.first.util.Globalclass;

import java.io.IOException;
import java.sql.SQLException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
//defining the variables that are needed
DataBaseHelper myDbHelper = new DataBaseHelper(this);
     Globalclass g1 = new Globalclass();
    String image;
    Button play;
    TextView level;
    Button login;
//    Boolean loggedIn;
    ProgressBar progress;
    boolean loggedIn;


    @Override
//    this runs every time the main section is opened
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        g1.init();
//        g1.level=5;
        play = (Button) findViewById(R.id.Play_button);
        play.setOnClickListener(this);
        progress = (ProgressBar) findViewById(R.id.progressBar);
        progress.setOnClickListener(this);
        level = (TextView) findViewById(R.id.Level_Text);
//        level.setText("Level: "+g1.getLevel());
        login = (Button) findViewById(R.id.Login_Button);
        login.setOnClickListener(this);
//        this shows the XML GUI


//        loggedIn = true;
        Log.w("great", "message");
        if (g1.loggedIn()) {
            login.setHighlightColor(getResources().getColor(R.color.Green));
        }
        initDatabase();
//        myDbHelper.read();
        Log.d("Here", "OnCreate");
//        g1.read();
//        g1.setAnswers();
//        initDatabase();
//        Log.d("Here", "is it ");

//        myDbHelper.insert();

//        myDbHelper.read();
//        myDbHelper.insert();
    }

    public void initDatabase() {
        Log.d("database","run");
        try {
            myDbHelper.createDataBase();
            Log.d("try1", "run");
        } catch (Exception ioe) {
            Log.d("Database","failed");
        }
        Log.d("mid","run");
        try {
            Log.d("try2","run");
            myDbHelper.openDataBase();
        }catch(Exception sqle){
            Log.d("Database","failed");
        }
        Log.d("fin", "run");
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.settings:
//                newGame();
//                return true;
//            case R.id.help:
//                showHelp();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
//    }

    @Override
//    when the GUI is no longer showing but not closed
    public void onPause() {
        super.onPause();
//
    }

//      public void reload() {}


    @Override
//    when it returns to the GUI
    public void onResume() {
        super.onResume();
//        reset the Play button text to normal
        play.setTextColor(getResources().getColor(R.color.Black));
        play.setText("Play");
        String i = myDbHelper.getLevel();
        level.setText("level: " +i);
        progress.setProgress(Integer.parseInt(i));
    }

    @Override
//    when the options menu is created
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
//    When one of the options menu have been selected
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Contact) {
            startActivity(new Intent("android.intent.action.util.Contact"));
            return true;
        }
        if (id == R.id.action_Developer) {
            startActivity(new Intent("android.intent.action.Leaderboard"));
            return true;
        }
        if (id == R.id.action_Settings) {
            startActivity(new Intent("android.intent.action.util.Settings"));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
//if the play button is selected
    private void PlayClick() {
        loggedIn = myDbHelper.CheckIsDataAlreadyInDBorNot("Users","loggedIn","'1' AND plevel > '0'");
        if (loggedIn) {
//            AND plevel > '0'
//        if they are loogged in start the play activity
            startActivity(new Intent("android.intent.action.PLAY"));
//            Intent intent = new Intent(getBaseContext(), Play.class);
//            intent.putExtra("GlobalClass", Globalclass.class);
//            startActivity(intent);
        } else {
//            otherwhise show that they need to sign in
            play.setTextColor(getResources().getColor(R.color.Red));
            play.setText("Please login first");
        }
    }
//  When the login button is pressed open login
    private void LoginClick() {
        startActivity(new Intent("android.intent.action.LOGIN"));
    }
    @Override
//    if something was clicked
    public void onClick(View v) {
        switch (v.getId()) {
//            if the play button was clicked
            case R.id.Play_button:
                PlayClick();
                break;
//            if the login button was clicked
            case R.id.Login_Button:
                LoginClick();
                break;
        }
    }

    public String getBoard(){
        myDbHelper.read("Level","board");
        return myDbHelper.read("Level","board","levelNo = '1'");
    }



//    public void newimage() {
//        image = "hello";
//    }

}