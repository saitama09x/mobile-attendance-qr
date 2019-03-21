package com.java.scanner.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import android.widget.ListView;
import android.database.Cursor;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.java.scanner.model.*;
import com.java.scanner.objects.*;
import com.java.scanner.adapters.*;

import java.util.*;
import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import android.os.AsyncTask;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import android.app.AlertDialog;
import android.provider.*;
import android.app.ProgressDialog;
import android.os.Handler;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.SystemClock;
import android.provider.Settings.*;
import java.lang.System;
import android.location.Location;
import  android.location.LocationManager;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.app.AlertDialog;

public class MainScreen extends AppCompatActivity {

private Context con;
private Button scan_btn, sync_btn;
private TextView hashcode, projtitle;
private ListView dtrlist;
private TextView total_sync, total_unsync;
private final Activity activity = this;
private Db_sqlite sqlite;
private Db_obj db_obj;


@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.main_screen);        

db_obj = new Db_obj(this);

Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
myToolbar.setTitle("Patrila DTR");    
setSupportActionBar(myToolbar);
myToolbar.setNavigationIcon(R.drawable.patrila_logo);

scan_btn = (Button) this.findViewById(R.id.scan);   
dtrlist = (ListView) findViewById(R.id.dtr_list);
projtitle = (TextView) findViewById(R.id.projtitle);

dtrlist.setDivider(null);
sqlite = new Db_sqlite(this);

Cursor projres = sqlite.getProjectName();
if(projres.getCount() > 0){
    projres.moveToFirst();
    projtitle.setText(projres.getString(projres.getColumnIndex("project_name")));
}else{
    projtitle.setText("No Project Selected");
}

Cursor res = sqlite.findLastFour();
SingleDtrAdapter adapter = db_obj.searchOperator(res, sqlite);
dtrlist.setAdapter(adapter);    

}

public void scan(View view){  
Cursor projres = sqlite.getProjectName();
if(projres.getCount() == 0){
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
    alertDialogBuilder.setTitle("No Project:");
    alertDialogBuilder.setMessage("Please Select Project");    
    AlertDialog alertDialog = alertDialogBuilder.create();
    alertDialog.show();
}else{
    Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
    finish();
    startActivity(goToNextActivity);
}   
}

public void show_all(View view){
Time_attendance.view_type = "operators";
Intent goToNextActivity = new Intent(getApplicationContext(), Time_attendance.class);
finish();
startActivity(goToNextActivity);
}

@Override
public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_screen_menu, menu);

    return super.onCreateOptionsMenu(menu);
}

@Override
public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
        case R.id.time_attendance:
        	Time_attendance.view_type = "operators";
			finish();
			startActivity(new Intent(getApplicationContext(), Time_attendance.class));
            return true;
        case R.id.sync:
            new MySqlUpload(this, new ProgressDialog(MainScreen.this), total_sync, total_sync).execute("");
            return true;
        case R.id.project:            
            finish();
            startActivity(new Intent(getApplicationContext(), ProjectsActivity.class));
            return true; 
        case R.id.settings:            
            finish();
            startActivity(new Intent(getApplicationContext(), AppSettings.class));
            return true;           
        default:
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            return super.onOptionsItemSelected(item);

   }
}

}
