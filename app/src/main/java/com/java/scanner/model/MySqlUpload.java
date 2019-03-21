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

public class MySqlUpload extends AsyncTask<String, Integer, String>{   

private Context context;
private String fname, lname;        
private Db_sqlite sqlite;
private AlertDialog.Builder alertDialogBuilder;
private AlertDialog.Builder successDialogBuilder;
private AlertDialog alertDialog;
private ProgressDialog progressDialog;
private Handler updateBarHandler;
int count = 0;
Cursor res, sync, unsync;
int count_row = 0;
TextView total_sync, total_unsync;

public MySqlUpload(Context context, ProgressDialog progressDialog, 
    TextView total_sync, TextView total_unsync) {
    this.context = context; 
    this.progressDialog = progressDialog;
    this.total_sync = total_sync;
    this.total_unsync = total_unsync;
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
    res = sqlite.findAllUnsync();
    count_row = res.getCount();  
    progressDialog.setMessage("Please Wait....");
    progressDialog.setTitle("Uploading Data");
    progressDialog.setMax(count_row);
    progressDialog.setProgress(0);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.show();
}

protected String doInBackground(String... args)  {        
try{    
    String link="http://mooplink.com/patrila_enterprise/prod/index.php/sync/time_attendance";
    StringBuilder sb = new StringBuilder();
    if(count_row > 0){
        res.moveToFirst();
        while(res.isAfterLast() == false){
            int id = res.getInt(res.getColumnIndex("id"));
            int emp_id = res.getInt(res.getColumnIndex("emp_id"));
            String time_created =  res.getString(res.getColumnIndex("time_created"));
            String date_created = res.getString(res.getColumnIndex("date_created"));
            String androidId = res.getString(res.getColumnIndex("androidId"));
            String remarks = "";
            int check_type = res.getInt(res.getColumnIndex("check_type"));

            if(res.getString(res.getColumnIndex("remarks")) != null){
                remarks = res.getString(res.getColumnIndex("remarks"));
            }

            String pic_str = "";
            if(res.getBlob(res.getColumnIndex("picture")) != null){
                byte[] picture = res.getBlob(res.getColumnIndex("picture"));
                Bitmap bitmap_pic = BitmapFactory.decodeByteArray(picture, 0, picture.length);
                ByteArrayOutputStream baos = new  ByteArrayOutputStream();
                bitmap_pic.compress(Bitmap.CompressFormat.PNG, 50, baos);
                byte [] b = baos.toByteArray();
                pic_str = Base64.encodeToString(b, Base64.DEFAULT);                 
            }
                         
            String hashcode = "";
            if(res.getString(res.getColumnIndex("hashcode")) != null){
                hashcode = res.getString(res.getColumnIndex("hashcode"));
            }

            String data  = URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(emp_id), "UTF-8");
            data += "&" + URLEncoder.encode("time_created", "UTF-8") + "=" + URLEncoder.encode(time_created, "UTF-8");
            data += "&" + URLEncoder.encode("date_created", "UTF-8") + "=" + URLEncoder.encode(date_created, "UTF-8");
            data += "&" + URLEncoder.encode("check_type", "UTF-8") + "=" + URLEncoder.encode(Integer.toString(check_type), "UTF-8");
            data += "&" + URLEncoder.encode("remarks", "UTF-8") + "=" + URLEncoder.encode(remarks, "UTF-8");
            data += "&" + URLEncoder.encode("picture", "UTF-8") + "=" + URLEncoder.encode(pic_str, "UTF-8");
            data += "&" + URLEncoder.encode("hashcode", "UTF-8") + "=" + URLEncoder.encode(hashcode, "UTF-8");
            data += "&" + URLEncoder.encode("androidId", "UTF-8") + "=" + URLEncoder.encode(androidId, "UTF-8");
            String line = null;     
            BufferedReader reader = urlLink(link, data);
            while((line = reader.readLine()) != null)
            {
                sb.append(line);
                break;
            }
            sqlite.delete_sync_status(id);
            publishProgress(count);
            count++;
            res.moveToNext();
        }
    }    
}catch(Exception ex){
    return new String("Exception: " + ex.getMessage());
}   

return "Uploaded SuccessFully";
}

@Override
protected void onPostExecute(String data){
    if (progressDialog.isShowing()) {
        progressDialog.dismiss();
        Activity activity = (Activity) context;
        Intent goToNextActivity = new Intent(activity.getApplicationContext(), MainScreen.class);
        activity.finish();
        activity.startActivity(goToNextActivity);
    }    
}

@Override
protected void onProgressUpdate(Integer... text) {
    if (!progressDialog.isShowing()) {
        progressDialog.show(); 
    }
    progressDialog.setProgress(text[0]); 
}

}
