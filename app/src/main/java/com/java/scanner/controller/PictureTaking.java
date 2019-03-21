package com.java.scanner.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;

import android.graphics.Bitmap;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.java.scanner.model.*;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap.CompressFormat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.ActionBar;
import android.view.MenuItem;
import java.security.MessageDigest;

import java.util.ArrayList;
import java.util.*;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import android.support.v4.content.ContextCompat;
import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.provider.*;
import android.hardware.Camera;
import android.widget.FrameLayout;
import android.hardware.Camera.PictureCallback;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.content.ContentResolver;
import android.util.Log;
import android.content.CursorLoader;
import java.io.File;
import android.os.Environment;

public class PictureTaking extends ActionBarActivity  {

    private Button button;
    private Button b1,b2, add_remarks_btn;
    private ImageView picture_img;

    TextView emp_name_view, emp_id_view, emp_time_view, emp_date_view, emp_date_out, 
    emp_time_out, check_type_view, hashcode, remarks;

    public static String emp_name = "";
    public static int emp_id = 0;
    public static int check_type = 1;
    public static int id = 0;
    private Db_sqlite sqlite;
    private boolean is_pictured = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_taking);
        sqlite = new Db_sqlite(this);
          
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extra.quickCapture", true);        
        startActivityForResult(intent, 1);      

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setNavigationIcon(R.drawable.new_back_btn);
        myToolbar.setTitle("Patrila DTR");      
        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
                 finish();
                 startActivity(goToNextActivity);
            }
        });


        b1 = (Button)findViewById(R.id.button);
        picture_img = (ImageView)findViewById(R.id.imageView);

        emp_name_view = (TextView) this.findViewById(R.id.emp_name);
        emp_id_view = (TextView) this.findViewById(R.id.emp_id);
        emp_time_view = (TextView) this.findViewById(R.id.emp_time);
        emp_date_view = (TextView) this.findViewById(R.id.emp_date);
        check_type_view = (TextView) this.findViewById(R.id.check_type);
        hashcode = (TextView) this.findViewById(R.id.hashcode);
        remarks = (TextView) this.findViewById(R.id.remarks);
        add_remarks_btn = (Button) this.findViewById(R.id.add_remarks_btn);
        
        b1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
            startActivityForResult(intent, 1);
         }
      });

      add_remarks_btn.setVisibility(View.GONE);  

    }
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      // TODO Auto-generated method stub
      super.onActivityResult(requestCode, resultCode, data);                  

      if (RESULT_OK == resultCode) {
        
        Date date = new Date();
        SimpleDateFormat format_time = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format_date = new SimpleDateFormat("yyyy-MM-dd");
        String string_time = format_time.format(date);
        String string_date = format_date.format(date); 
        
        emp_name_view.setText(PictureTaking.emp_name);
        emp_id_view.setText(Integer.toString(PictureTaking.emp_id));

        emp_time_view.setText(string_time);
        emp_date_view.setText(string_date);

        Bitmap picture_bmp = (Bitmap) data.getExtras().get("data");
        picture_img.setImageBitmap(picture_bmp);                                 

        String hash = this.sha1(Integer.toString(PictureTaking.emp_id) + "" + string_time + " " + string_date);

        String first_4 = hash.substring(0, 3);
        String last_4 = hash.substring(Math.max(0, hash.length() - 3));
        String hash_code = first_4 + "" + last_4;

        hashcode.setText(hash_code);
        
        Cursor res_type = sqlite.getCheckType(PictureTaking.check_type);
        res_type.moveToFirst(); 
        String type_name = res_type.getString(0);
        check_type_view.setText(type_name);
        is_pictured = true;
        add_remarks_btn.setVisibility(View.VISIBLE);  

      }
   }

   public void add_remarks(View view){
      final Dialog dialog = new Dialog(this);  
      dialog.setContentView(R.layout.remarks_dialog);  
      final EditText remarks_text = (EditText) dialog.findViewById(R.id.remarks);
      remarks_text.setText(remarks.getText().toString());
      Button rem_btn = (Button) dialog.findViewById(R.id.button);
      rem_btn.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
            remarks.setText(remarks_text.getText().toString());
            dialog.dismiss();
         }
      });
      dialog.show();
   }


   @Override
   protected void onDestroy() {
      super.onDestroy();
   }

   public String sha1(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();
    
            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++)
                hexString.append(String.format("%02X", 0xFF & messageDigest[i]));
            return hexString.toString();
    
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void done(View view){
        
        if(!is_pictured){
            Toast.makeText(this, "Please take your picture", Toast.LENGTH_LONG).show();
            return;
        }

        Bitmap bitmap_img = ((BitmapDrawable) picture_img.getDrawable()).getBitmap();          
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap_img.compress(CompressFormat.PNG, 0, stream);
        byte[] picture_byte =  stream.toByteArray();
        String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);

        Cursor project_id = sqlite.getProjectId();
        if(project_id.getCount() > 0){
          project_id.moveToFirst();  
          int proj_id = project_id.getInt(project_id.getColumnIndex("id"));
          sqlite.check(PictureTaking.emp_name, PictureTaking.emp_id, emp_time_view.getText().toString(), 
          emp_date_view.getText().toString(), PictureTaking.check_type, picture_byte, 
          hashcode.getText().toString(), remarks.getText().toString(), androidId, proj_id);
        }else{

        }

        //delete the last image taken


        Intent goToNextActivity = new Intent(getApplicationContext(), LogsActivity.class);
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

    @Override
    protected void onPause() {
        super.onPause();
    }

   
}
