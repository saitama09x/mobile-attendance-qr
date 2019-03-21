package com.java.scanner.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.ActionBar;
import android.view.MenuItem;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.java.scanner.model.*;

import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class RemarksActivity extends AppCompatActivity {
	private Db_sqlite sqlite;
	private int id = PictureTaking.id;
	private EditText remarks_view;
	@Override
    protected void onCreate(Bundle savedInstanceState) {    	 
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.remarks_activity);
        sqlite = new Db_sqlite(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.new_back_btn);
        myToolbar.setTitle("Patrila DTR");  
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent goToNextActivity = new Intent(getApplicationContext(), PictureTaking.class);
                 finish();
                 startActivity(goToNextActivity);
            }
        });
        remarks_view = (EditText) this.findViewById(R.id.remarks);
    }

    public void insertRemarks(View view){
    	boolean rem = sqlite.insertRemarks(id, remarks_view.getText().toString());
        if(rem){
            Intent goToNextActivity = new Intent(getApplicationContext(), LogsActivity.class);
            finish();
            startActivity(goToNextActivity);
        }
    	
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);
 
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_settings:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}