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
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.widget.*;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.ActionBar;
import android.view.MenuItem;
import android.widget.ListView;
import android.database.Cursor;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import java.util.*;
import java.lang.Thread;
import java.text.SimpleDateFormat;
import android.app.AlarmManager;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.java.scanner.model.*;
import com.java.scanner.objects.*;
import com.java.scanner.adapters.*;
import android.widget.ProgressBar;

public class AppSettings extends AppCompatActivity {

private Dialog date_dialog;
private TextView mcurrdate, mcurrtime;
private GPSTracker gps;
private LinearLayout gpsdatetime;
private ListView projectlist;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
gps = new GPSTracker(this);
setContentView(R.layout.app_settings);
}

public void dateDialog(View v){
date_dialog.show();
if(gps.canGetLocation()){
double latitude = gps.getLatitude();
double longitude = gps.getLongitude();
long time = gps.getTime();
SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");
Date datetime = new Date(time);
String time_ = time_format.format(datetime);
String date_ = date_format.format(datetime);
}
}

public void goSetProject(View v){
Intent goToNextActivity = new Intent(getApplicationContext(), ProjectsActivity.class);
finish();
startActivity(goToNextActivity);	
}

public void goHome(View view){
Intent goToNextActivity = new Intent(getApplicationContext(), MainScreen.class);
finish();
startActivity(goToNextActivity);	
}



}