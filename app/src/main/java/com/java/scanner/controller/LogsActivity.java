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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;


public class LogsActivity extends AppCompatActivity {
	private Db_sqlite sqlite;
	private Button button;
  private Button b1,b2;
  private ImageView picture_img;
  TextView emp_name_view, emp_id_view, emp_time_view, emp_date_view, 
  check_type_view, emp_remarks_view, hashcode;

  private int emp_id = PictureTaking.emp_id;
	protected void onCreate(Bundle savedInstanceState) {    	 
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.logs_activity);

        sqlite = new Db_sqlite(this);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);        
        myToolbar.setTitle("Patrila DTR"); 

        emp_name_view = (TextView) this.findViewById(R.id.emp_name);
        emp_id_view = (TextView) this.findViewById(R.id.emp_id);
        emp_time_view = (TextView) this.findViewById(R.id.emp_time);
        emp_date_view = (TextView) this.findViewById(R.id.emp_date);
        check_type_view = (TextView) this.findViewById(R.id.check_type);
        emp_remarks_view = (TextView) this.findViewById(R.id.remarks);
        picture_img = (ImageView) this.findViewById(R.id.picture);
        hashcode = (TextView) this.findViewById(R.id.hashcode);

        Cursor res = sqlite.getData(emp_id); 
        SimpleDateFormat format_time = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format_date_view = new SimpleDateFormat("MMM, d yyyy");
        SimpleDateFormat format_time_view = new SimpleDateFormat("hh:mm a");
        Date emp_time__ = new Date();
        Date emp_date__ = new Date();
        int count_row = res.getCount();
	    if(count_row >= 1){
    	    	res.moveToFirst();    
    	    	int emp_id_ = res.getInt(0);
    	    	String emp_name = res.getString(1);
            try{
              emp_time__ = format_time.parse(res.getString(2));
              emp_date__ = format_time.parse(res.getString(3));
            }catch(Exception ex){

            }            
            
          	String emp_time = format_time_view.format(emp_time__);
          	String emp_date = format_date_view.format(emp_date__) + " ";  

          	int emp_check = res.getInt(4);          	
          	int id = res.getInt(5); 
          	String emp_remarks = res.getString(6);
          	byte[] picture_byte = res.getBlob(7);
            String hash = res.getString(8);

          	Bitmap bitmap_pic = BitmapFactory.decodeByteArray(picture_byte, 0, picture_byte.length);

          	emp_name_view.setText(emp_name);
          	emp_id_view.setText(Integer.toString(emp_id_));
          	emp_time_view.setText(emp_time);
          	emp_date_view.setText(emp_date + " ");
          	emp_remarks_view.setText(emp_remarks);
          	picture_img.setImageBitmap(bitmap_pic);
            hashcode.setText(hash);
          	Cursor res_type = sqlite.getCheckType(emp_check);
          	res_type.moveToFirst(); 

         	  String type_name = res_type.getString(0);
          	check_type_view.setText(type_name);
	    }

    }


    public void dtrlist(View view){
        Intent goToNextActivity = new Intent(getApplicationContext(), DtrActivity.class);
        finish();
        startActivity(goToNextActivity);      
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
                Intent goToNextActivity = new Intent(getApplicationContext(), MainScreen.class);
                finish();
                startActivity(goToNextActivity);
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
