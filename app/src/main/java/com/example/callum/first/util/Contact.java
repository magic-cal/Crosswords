package com.example.callum.first.util;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.callum.first.R;

public class Contact extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Button text = (Button)findViewById(R.id.Text_button);
        Button call = (Button)findViewById(R.id.Call_button);
        Button email = (Button)findViewById(R.id.Email_button);
        text.setOnClickListener(this);
        call.setOnClickListener(this);
        email.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
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
switch (view.getId()){
    case R.id.Call_button:
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:0123456789"));
        startActivity(intent);
        break;
    case R.id.Email_button:
        Intent intent1 = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
        intent1.setType("text/plain");
        intent1.putExtra(Intent.EXTRA_SUBJECT, "Debugging");
        intent1.putExtra(Intent.EXTRA_TEXT, "Body of email");
        intent1.setData(Uri.parse("mailto:info@magic-cal.co.uk")); // or just "mailto:" for blank
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        startActivity(intent1);
        break;
    case R.id.Text_button:
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("sms:"
                + "012345678")));
        break;


        }
    }
}
