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

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.java.scanner.model.*;

import java.util.Date;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.database.Cursor;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends AppCompatActivity {

    private Button button, check_in_btn, check_out_btn, rescan;
    final Activity activity = this;
    TextView emp_name, emp_id, last_log;
    private Db_sqlite sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) this.findViewById(R.id.button);
        check_in_btn = (Button) this.findViewById(R.id.check_in_btn);
        check_out_btn = (Button) this.findViewById(R.id.check_out_btn);
        rescan = (Button) this.findViewById(R.id.rescan);
        last_log = (TextView) this.findViewById(R.id.last_log);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setTitle("Patrila DTR");        

        emp_name = (TextView) this.findViewById(R.id.emp_name);
        emp_id = (TextView) this.findViewById(R.id.emp_id);

        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Scan");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();

        check_in_btn.setVisibility(View.GONE);
        check_out_btn.setVisibility(View.GONE);
        rescan.setVisibility(View.GONE);

    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        try{
            sqlite = new Db_sqlite(this);
            if(result != null) {            
                if(result.getContents() == null) {
                    Log.d("MainActivity", "Cancelled scan");
                    rescan.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                        JSONObject jsonObj = new JSONObject(result.getContents());                            
                        PictureTaking.emp_name = jsonObj.getString("name");
                        PictureTaking.emp_id = Integer.parseInt(jsonObj.getString("eid"));                                                
                        emp_name.setText(PictureTaking.emp_name);
                        emp_id.setText(Integer.toString(PictureTaking.emp_id));                    
                        Cursor cur = sqlite.getData(PictureTaking.emp_id);
                        int count_row = cur.getCount();
                        if(count_row > 0){
                            String logs = "";
                            cur.moveToFirst();
                            if(cur.getInt(cur.getColumnIndex("check_type")) == 1){
                                logs = "IN";
                                check_out_btn.setVisibility(View.VISIBLE);
                            }else if(cur.getInt(cur.getColumnIndex("check_type")) == 2){
                                logs = "OUT";                                
                                check_in_btn.setVisibility(View.VISIBLE);
                            }
                            String logs_time = cur.getString(cur.getColumnIndex("time_created")) + " " + 
                            cur.getString(cur.getColumnIndex("date_created"));
                            last_log.setText(logs_time + " " + logs);
                        }else{
                            check_in_btn.setVisibility(View.VISIBLE);
                            last_log.setText("No Record");
                        }
                    }
                } else {
                    // This is important, otherwise the result will not be passed to the fragment
                    super.onActivityResult(requestCode, resultCode, data);
                }
        }catch(Exception ex){
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        
    }

    public void check_in(View view){
        PictureTaking.check_type = 1;        
        Intent goToNextActivity = new Intent(getApplicationContext(), PictureTaking.class);
        finish();
        startActivity(goToNextActivity);
    }

    public void check_out(View view){
        PictureTaking.check_type = 2;        
        Intent goToNextActivity = new Intent(getApplicationContext(), PictureTaking.class);
        finish();
        startActivity(goToNextActivity);
    }

    public void re_scan(View view){      
        Intent goToNextActivity = new Intent(getApplicationContext(), MainActivity.class);
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
