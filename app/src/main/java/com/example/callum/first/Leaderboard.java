package com.example.callum.first;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
//import android.view.It

import com.example.callum.first.util.DataBaseHelper;

import java.util.ArrayList;

public class Leaderboard extends AppCompatActivity {

    DataBaseHelper myDbHelper = new DataBaseHelper(this);
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDetails(Integer.parseInt(myDbHelper.getLevel()), "");
        gridView.setOnItemClickListener(null);
            }

//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        gridView = (GridView) findViewById(R.id.gridView1);
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myDbHelper.getHighScores(Integer.parseInt(myDbHelper.getLevel()),""));
//        gridView.setAdapter(adapter);
////        addDetails(Integer.parseInt(myDbHelper.getLevel()), "");
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                String section = ((TextView) v).getText().toString();
//                Log.d("section", section);
//                switch (section) {
//                    case "Time":
//                        addDetails(Integer.parseInt(myDbHelper.getLevel()), "ORDER BY time ASC");
//                        break;
//                    case "Guesses":
//                        addDetails(Integer.parseInt(myDbHelper.getLevel()), "ORDER BY guesses ASC");
//                        break;
//                }
//
//            }
//        });}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leaderboard, menu);
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
    }
    public void addDetails(int Level,String sortBy){
        setContentView(R.layout.activity_leaderboard);
        gridView = (GridView) findViewById(R.id.gridView1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, myDbHelper.getHighScores(Level,""+sortBy));
        gridView.setAdapter(adapter);

    }

//    @Override
//    public void onClick(View v) {
//
//    }
}
