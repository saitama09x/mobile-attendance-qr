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

public class UploadDataActivity extends AppCompatActivity {
	
private Spinner  sel_type, sel_connect;	
	private ListView projectlist;
	private Db_sqlite sqlite;
	private TextView projtitle;
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.projects_view);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Patrila DTR"); 

        projectlist = (ListView) findViewById(R.id.projectlist);
        projtitle = (TextView) findViewById(R.id.projtitle);
	}	
}