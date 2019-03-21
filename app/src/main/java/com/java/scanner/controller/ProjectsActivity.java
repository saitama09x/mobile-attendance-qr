package com.java.scanner.controller;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
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
import java.util.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.java.scanner.model.*;

import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.widget.ArrayAdapter;

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
import com.java.scanner.adapters.*;
import android.app.Dialog;
import android.view.LayoutInflater;

public class ProjectsActivity extends AppCompatActivity {
	
	private Spinner  sel_type, sel_connect;	
	private ListView projectlist;
	private Db_sqlite sqlite;
	private TextView projtitle;
    private EditText password;
    private Button submit_pass;
    private Dialog dialog;
    private View elem;
    private final Context con = this;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_view);        

        projectlist = (ListView) findViewById(R.id.projectlist);
        projtitle = (TextView) findViewById(R.id.projtitle);

        sqlite = new Db_sqlite(this);
        dialog = new Dialog(this);

        elem = (View) LayoutInflater.from(this).inflate(R.layout.passproject, null);
        dialog.setContentView(elem);                
        submit_pass = (Button) elem.findViewById(R.id.submit_pass);

        Cursor projres = sqlite.getProjectName();
        if(projres.getCount() != 0){
            projres.moveToFirst();
            projtitle.setText(projres.getString(projres.getColumnIndex("project_name")));
        }else{
            projtitle.setText("No Project Selected");
        }		
        	
		List<String> projlist = new ArrayList<String>();
		List<String> projid = new ArrayList<String>();
		List<String> projstatus = new ArrayList<String>();
        List<Integer> projsel = new ArrayList<Integer>();
        Cursor cur = sqlite.getAllProjects();
        if(cur.getCount() > 0){
        	cur.moveToFirst();
        	while(cur.isAfterLast() == false){
        		projid.add(Integer.toString(cur.getInt(cur.getColumnIndex("id"))));
        		projlist.add(cur.getString(cur.getColumnIndex("project_name")));
        		projstatus.add(cur.getString(cur.getColumnIndex("status")));
                projsel.add(cur.getInt(cur.getColumnIndex("selected")));
        		cur.moveToNext();
        	}

            String[] projlist_ = projlist.toArray(new String[projlist.size()]);
            String[] projid_ = projid.toArray(new String[projid.size()]);
            String[] projstatus_ = projstatus.toArray(new String[projstatus.size()]);
            Integer[] projsel_ = projsel.toArray(new Integer[projsel.size()]);

            ProjectList projadapter = new ProjectList(this, projlist_, projid_, projstatus_, projsel_);
            projectlist.setAdapter(projadapter);   
        }
    }	

    public void updateProject(View v){
        dialog.show(); 
        final EditText password = (EditText) elem.findViewById(R.id.password);
        submit_pass.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(password.getText().toString().equals("abc")){
                    new MySqlProjectDl(con).execute();
                }else{
                    Toast.makeText(con, "Wrong Password", Toast.LENGTH_SHORT);
                }    	
            }
        });
    }

    public void goHome(View v){
        Intent goToNextActivity = new Intent(this, MainScreen.class);
        finish();
        startActivity(goToNextActivity);
    }

}