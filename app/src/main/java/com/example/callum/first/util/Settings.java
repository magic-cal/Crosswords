package com.example.callum.first.util;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.ToggleButton;

import com.example.callum.first.R;

public class Settings extends AppCompatActivity implements View.OnClickListener{

 Switch hideKeyboard;
Globalclass g1 = new Globalclass();
    ToggleButton hide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        hideKeyboard = (Switch) findViewById(R.id.switch1);
        hide = (ToggleButton)findViewById(R.id.toggleButton);
        hide.setOnClickListener(this);
        hideKeyboard.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    @Override
    public void onClick(View view) {
        Log.d("Button","pressed"+view.getId());
        switch(view.getId()) {

            case (R.id.switch1):
                g1.setHideKeyboard(!g1.getHideKeyboard());
                break;
            case (R.id.toggleButton):
                g1.setHideKeyboard(!g1.getHideKeyboard());
                break;
        }
    }
}
