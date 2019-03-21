package com.java.scanner.controller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;

import android.content.ClipData;
import android.content.ClipboardManager;
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
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.app.ActionBar;
import android.view.MenuItem;

import com.java.scanner.model.*;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Set;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DtrActivity extends ActionBarActivity  {

	private Db_sqlite sqlite;
	private ListView dtrlist;
	private int emp_id = 200;
	private TextView emp_name_view, emp_id_view;
	private ImageView smallpic;
    public static int ids = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dtr_activity);
        dtrlist = (ListView) findViewById(R.id.dtr_list);
        emp_name_view = (TextView) findViewById(R.id.emp_name);
		emp_id_view = (TextView) findViewById(R.id.emp_id);

        dtrlist.setDivider(null);
		sqlite = new Db_sqlite(this);

		Cursor res = sqlite.getData(emp_id);
		int count_row = res.getCount();
		String[] date_created = new String[count_row];
		String[] time_created = new String[count_row];
		String[] check = new String[count_row];
        Integer[] ids = new Integer[count_row];
        Bitmap[] bitimg = new Bitmap[count_row];
		if(count_row >= 0){
			int count = 0;
			res.moveToFirst();
			emp_id_view.setText(Integer.toString(res.getInt(0)));
			emp_name_view.setText(res.getString(1));
			while(res.isAfterLast() == false){	
				date_created[count]	= res.getString(res.getColumnIndex("date_created"));
				time_created[count]	= res.getString(res.getColumnIndex("time_created"));
				ids[count] = res.getInt(res.getColumnIndex("id"));
                int emp_check	= res.getInt(res.getColumnIndex("check_type"));

                Bitmap bitmap_pic = null;
                if(res.getBlob(res.getColumnIndex("picture")) != null){
					byte[] picture_byte = res.getBlob(res.getColumnIndex("picture"));
					bitmap_pic = BitmapFactory.decodeByteArray(picture_byte, 0, picture_byte.length);
				}

				bitimg[count] = bitmap_pic;
				Cursor res_type = sqlite.getCheckType(emp_check);
		        res_type.moveToFirst(); 
		        String type_name = res_type.getString(0);
		        check[count] = type_name;
				count++;
				res.moveToNext();
			}
		}

        DtrListAdapter adapter = new DtrListAdapter(this, date_created, time_created, check, ids, bitimg);
		dtrlist.setAdapter(adapter);	

    }  

    public void goHome(View v){
        Intent goToNextActivity = new Intent(getApplicationContext(), MainScreen.class);
        finish();
        startActivity(goToNextActivity);
    }
}