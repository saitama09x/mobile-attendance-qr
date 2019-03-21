package com.java.scanner.model;

import com.java.scanner.controller.*;

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
import  java.io.ByteArrayOutputStream;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

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

public class MySqlProjectDl extends AsyncTask<String, Integer, String>{

private int count_row = 0;
private AlertDialog.Builder alertDialogBuilder;
private AlertDialog.Builder successDialogBuilder;
private AlertDialog alertDialog;
private ProgressDialog progressDialog;
private Handler updateBarHandler;
private Db_sqlite sqlite;
private Activity context;

public MySqlProjectDl(Context context) {
    this.context = (Activity) context; 
}

public BufferedReader urlLink(String link, String data) throws Exception{
    URL url = new URL(link);
    URLConnection conn = url.openConnection(); 

    conn.setDoOutput(true); 
    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream()); 

    wr.write( data ); 
    wr.flush(); 

    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    return reader;
}

protected void onPreExecute()  { 
    sqlite = new Db_sqlite(context); 
    sqlite.deleteAllProjects(); 
    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	
    alertDialogBuilder.setTitle("Updating");
    alertDialogBuilder.setMessage("Please Wait");
    alertDialog = alertDialogBuilder.create();
	alertDialog.show();
}

protected String doInBackground(String... args)  { 
try{    
    String link="http://mooplink.com/patrila_enterprise/prod/index.php/sync/getAllProjects";
    StringBuilder sb = new StringBuilder();
    String data  = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(0), "UTF-8");
    String line = null;
    BufferedReader reader = urlLink(link, data);
	while((line = reader.readLine()) != null)
    {
        sb.append(line);
        break;
    }
    return sb.toString();
}catch(Exception ex){
	return new String("Exception: " + ex.getMessage());
}

}

@Override
protected void onPostExecute(String data){ 
if(data != ""){	
try{
JSONObject jsonObj = new JSONObject(data);
JSONArray res = jsonObj.getJSONArray("result");
for (int i = 0; i < res.length(); i++) {
	JSONObject obj = res.getJSONObject(i);
	int proj_id = obj.getInt("id");
	String proj_name = obj.getString("name");
	String proj_loc = obj.getString("location");
	String status = obj.getString("status");

	sqlite.insertProject(proj_id, proj_name, proj_loc, status);
}
	alertDialog.dismiss();
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	    
	alertDialogBuilder.setTitle("Success");
	alertDialogBuilder.setMessage("Successfully Updated Projects");
	alertDialog = alertDialogBuilder.create();
	alertDialog.show();
	alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
		@Override
		public void onDismiss(final DialogInterface arg0) {				
				Intent refresh = new Intent(context, ProjectsActivity.class);
				context.finish(); 
				context.startActivity(refresh);
			}
		});
}catch(Exception ex){
	alertDialog.dismiss();
	AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);	    
	alertDialogBuilder.setTitle("Error");
	alertDialogBuilder.setMessage(ex.getMessage());
	alertDialog = alertDialogBuilder.create();
	alertDialog.show();
}

}

}

}   