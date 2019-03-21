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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.ActionBar;
import android.view.MenuItem;

import com.java.scanner.model.*;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.support.v4.content.ContextCompat;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class ViewCheck extends AppCompatActivity {
private Db_sqlite sqlite;
private Button button;
private Button b1,b2;
private int ids = DtrActivity.ids;
protected void onCreate(Bundle savedInstanceState) {    	 
super.onCreate(savedInstanceState);
setContentView(R.layout.view_check);

sqlite = new Db_sqlite(this);

TextView emp_name_view = (TextView) this.findViewById(R.id.emp_name);
TextView emp_id_view = (TextView) this.findViewById(R.id.emp_id);
TextView emp_time_view = (TextView) this.findViewById(R.id.emp_time);
TextView emp_date_view = (TextView) this.findViewById(R.id.emp_date);
TextView check_type_view = (TextView) this.findViewById(R.id.check_type);
TextView emp_remarks_view = (TextView) this.findViewById(R.id.remarks);
ImageView picture_img = (ImageView) this.findViewById(R.id.picture);
TextView hashcode = (TextView) this.findViewById(R.id.hashcode);

Cursor res = sqlite.findById(ids); 
int count_row = res.getCount();
if(count_row >= 1){
res.moveToLast();    
int emp_id_ = res.getInt(0);
String emp_name = res.getString(1);
String emp_time = res.getString(2);
String emp_date = res.getString(3);    
int emp_check = res.getInt(4);            
String emp_remarks = res.getString(6);
String hash = res.getString(8);

if(res.getBlob(7) != null){
byte[] picture_byte = res.getBlob(7);
Bitmap bitmap_pic = BitmapFactory.decodeByteArray(picture_byte, 0, picture_byte.length);
picture_img.setImageBitmap(bitmap_pic);
}else{
picture_img.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.blank_face));
}           

emp_name_view.setText(emp_name);
emp_id_view.setText(Integer.toString(emp_id_));
emp_time_view.setText(emp_time);
emp_date_view.setText(emp_date + " ");
emp_remarks_view.setText(emp_remarks);

hashcode.setText(hash);
Cursor res_type = sqlite.getCheckType(emp_check);
res_type.moveToFirst(); 

String type_name = res_type.getString(0);
check_type_view.setText(type_name);

}

}

public void goHome(View v){
Intent goToNextActivity = new Intent(getApplicationContext(), MainScreen.class);
finish();
startActivity(goToNextActivity);
}

}
