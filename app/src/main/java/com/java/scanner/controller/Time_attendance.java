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
import android.app.DatePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import com.java.scanner.model.*;
import com.java.scanner.adapters.*;
import com.java.scanner.objects.*;

import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import java.util.*;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.support.v4.content.ContextCompat;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;

public class Time_attendance extends AppCompatActivity {

private Context con;
private ListView dtrlist;
private Db_sqlite sqlite;
public static String view_type = "operators";
public static int operator_id = 0;
public static int attendance_id = 0;
private String date_type = "from";
private int year, month, day;
private ImageView picture_img;
private Dialog dialog;
private DatePicker dpResult;
private TextView from_date, to_date;
private RelativeLayout dtr_content;
private int search_emp_id;
private Db_obj db_obj;

@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

sqlite = new Db_sqlite(this);
db_obj = new Db_obj(this);

if(Time_attendance.view_type == "operators"){
setContentView(R.layout.time_attendance); 
initDialogList();
this.list_operators();
}else if(Time_attendance.view_type == "attendance"){
setContentView(R.layout.time_attendance); 
initDialogList();
this.getAttendance(Time_attendance.operator_id);
}else if(Time_attendance.view_type == "view_check"){
setContentView(R.layout.view_check);
this.show_attendance_details(Time_attendance.attendance_id);
}else if(Time_attendance.view_type == "view_check_last_four"){
setContentView(R.layout.view_check);
this.show_attendance_details(Time_attendance.attendance_id);
}
}

@Override
protected Dialog onCreateDialog(int id) {

final Calendar c = Calendar.getInstance();
year = c.get(Calendar.YEAR);
month = c.get(Calendar.MONTH);
day = c.get(Calendar.DAY_OF_MONTH);

if (id == 999) {
return new DatePickerDialog(this, 
myDateListener, year, month, day);
}
return null;
}

private void initDialogList(){

dtrlist = (ListView) findViewById(R.id.dtr_list);	
dialog = new Dialog(this);	
dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
dialog.getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
View elem = (View) LayoutInflater.from(this).inflate(R.layout.searchdialog, null);
dialog.setContentView(elem);	

Spinner operator_sel = (Spinner) elem.findViewById(R.id.operators);
from_date = (TextView) elem.findViewById(R.id.from_date);
to_date = (TextView) elem.findViewById(R.id.to_date);
dtr_content = (RelativeLayout) findViewById(R.id.dtr_content);

Cursor ops = sqlite.findGroupOperators();
int row_count = ops.getCount();
if(row_count > 0){
ops.moveToFirst();
List<Operator_class> ops_class = db_obj.getOperatorList(ops);
OperatorAdapter ops_adp = new OperatorAdapter(this, R.layout.date_spinner, ops_class);
operator_sel.setAdapter(ops_adp);
}

operator_sel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
@Override
public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

Operator_class ops = (Operator_class) parent.getSelectedItem();
setSearchEmpId(ops.getId());

}

@Override
public void onNothingSelected(AdapterView<?> parent) {    
}
});
dtrlist.setDivider(null);
}


private DatePickerDialog.OnDateSetListener myDateListener = new 
DatePickerDialog.OnDateSetListener() {
@Override
public void onDateSet(DatePicker arg0, 
int arg1, int arg2, int arg3) {
String yr = Integer.toString(arg1);
String month = "00";
String day = "00";	
if(( arg2 + 1 ) < 10){
month = "0" + Integer.toString(arg2 + 1);
}else if(( arg2 + 1 ) > 9){
month = Integer.toString(arg2 + 1);
}

if(arg3 < 10){
day = "0" + Integer.toString(arg3);
}else if(arg3 > 9){
day = Integer.toString(arg3);
}
setDate(yr, month, day);
}
};

public void setDate(String yr, String month, String day) {
if(this.date_type == "from"){
from_date.setText(yr + "-" + month + "-" + day);
}else if(this.date_type == "to"){
to_date.setText(yr + "-" + month + "-" + day);
}
}

public void setFrom_date(View view){
showDialog(999);
this.date_type = "from";
}

public void setTo_date(View view){
showDialog(999);
this.date_type = "to";
}

public void searchOperator(View view){
Cursor res = sqlite.searchOperator(getSearchEmpId(), from_date.getText().toString(), to_date.getText().toString());
SingleDtrAdapter adapter = db_obj.searchOperator(res, sqlite);
dtrlist.setAdapter(adapter);
dialog.dismiss();
}

public void list_operators(){
Cursor res = sqlite.findAllOperators();
TimeAttendanceAdapter adapter = db_obj.list_operators(res);
dtrlist.setAdapter(adapter);
}


public void go_attendance(View v){
Time_attendance.view_type = "operators";
finish();
startActivity(new Intent(getApplicationContext(), Time_attendance.class)); 
}


public void getAttendance(int id){
Cursor res = sqlite.findSingleDTR(id);
SingleDtrAdapter adapter = db_obj.searchOperator(res, sqlite);
dtrlist.setAdapter(adapter);	
}

public void setSearchEmpId(int emp_id){
this.search_emp_id = emp_id;
}

public int getSearchEmpId(){
return this.search_emp_id;
}

public void show_attendance_details(int id){

TextView emp_name_view = (TextView) this.findViewById(R.id.emp_name);
TextView emp_id_view = (TextView) this.findViewById(R.id.emp_id);
TextView emp_time_view = (TextView) this.findViewById(R.id.emp_time);
TextView emp_date_view = (TextView) this.findViewById(R.id.emp_date);
TextView check_type_view = (TextView) this.findViewById(R.id.check_type);
TextView emp_remarks_view = (TextView) this.findViewById(R.id.remarks);
ImageView picture_img = (ImageView) this.findViewById(R.id.picture);
TextView hashcode = (TextView) this.findViewById(R.id.hashcode);

Cursor res = sqlite.findById(id); 
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

public void modalSearch(View view){	
dialog.show();
}

public void goHome(View view){
Intent goToNextActivity = new Intent(getApplicationContext(), MainScreen.class);
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
case R.id.action_search:

return true;      
case R.id.action_settings:

return true;              
default:
// If we got here, the user's action was not recognized.
// Invoke the superclass to handle it.
return super.onOptionsItemSelected(item);

}
}

}