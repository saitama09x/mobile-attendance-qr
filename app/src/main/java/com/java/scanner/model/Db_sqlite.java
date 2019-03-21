package com.java.scanner.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;
import com.java.scanner.controller.R;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Db_sqlite extends SQLiteOpenHelper {
   
   public String tbl_dtr = "dtr4";

	public Db_sqlite(Context context)
   	{
      super(context, "myDBName_4.db", null, 1);
   	}

   	@Override
   	public void onCreate(SQLiteDatabase db) {
      // TODO Auto-generated method stub
      db.execSQL(
      "create table if not exists " + tbl_dtr +
      " (id integer primary key, emp_name varchar, emp_id int, " + 
      "time_created time, date_created date, check_type int, "+
      "remarks text, picture blob, hashcode text, sync_status int, "+
      "androidId text, project_id int)");	 	 

      db.execSQL(
      "create table if not exists check_type " +
      "(id integer primary key, type_name varchar)"); 

      db.execSQL(
      "create table if not exists  projects(id integer primary key, " +
      "project_name varchar, location varchar, status varchar, selected int )"
      );
      
      db.execSQL(
      "create table if not exists time_change(id integer primary key, time_created time, date_created date)"
      );

      ContentValues contentValues = new ContentValues();
      contentValues.put("id", 1); 
      contentValues.put("type_name", "in"); 
      db.insert("check_type", null, contentValues);

      contentValues = new ContentValues();
      contentValues.put("id", 2); 
      contentValues.put("type_name", "out"); 
      db.insert("check_type", null, contentValues);      

      for(int x = 0; x < 20; x++){
         contentValues = new ContentValues();
         contentValues.put("emp_name", "admin");
         contentValues.put("emp_id", 200);
         contentValues.put("time_created", "10:00:00");   
         contentValues.put("date_created", "2017-01-01");
         contentValues.put("check_type", 1);
         contentValues.put("hashcode", "testing");
         contentValues.put("sync_status", 0);
         contentValues.put("androidId", "testing");
         contentValues.put("project_id", 1);
         db.insert(tbl_dtr, null, contentValues);  
      }

       for(int x = 0; x < 20; x++){
         contentValues = new ContentValues();
         contentValues.put("emp_name", "guest");
         contentValues.put("emp_id", 300);
         contentValues.put("time_created", "10:00:00");   
         contentValues.put("date_created", "2017-01-01");
         contentValues.put("check_type", 1);
         contentValues.put("hashcode", "testing");
         contentValues.put("sync_status", 0);
         contentValues.put("androidId", "testing");
         contentValues.put("project_id", 2);
         db.insert(tbl_dtr, null, contentValues);  
      }


	}
   
   @Override
   public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("ALTER TABLE "+tbl_dtr+" ADD COLUMN project_id INTEGER");   
      
   }

   public Cursor getAllProjects(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from projects", null );
      return res;
   }

   public Cursor getProjectName(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from projects where selected = 1", null );
      return res;
   }

   public void insertProject(int id, String name, String location, String status){
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put("id", id);
       contentValues.put("project_name", name);
       contentValues.put("location", location);
       contentValues.put("status", status);
       contentValues.put("selected", 0);
       db.insert("projects", null, contentValues);
   }

   public void activateProj(int code){
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put("selected", 0);
       db.update("projects", contentValues, "selected=1", null);

       contentValues = new ContentValues();
       contentValues.put("selected", 1);
       db.update("projects", contentValues, "id='" + code + "'", null);

   }

   public boolean check(String emp_name, int emp_id, 
   	String time_created, String date_created, int type, 
      byte[] picture, String hashcode, String remarks, String androidId, int project_id)

   {
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("emp_name", emp_name);
      contentValues.put("emp_id", emp_id);
      contentValues.put("time_created", time_created);	
      contentValues.put("date_created", date_created);
      contentValues.put("check_type", type);
      contentValues.put("picture", picture);
      contentValues.put("hashcode", hashcode);
      contentValues.put("sync_status", 0);
      contentValues.put("remarks", remarks);
      contentValues.put("androidId", androidId);
      contentValues.put("project_id", project_id);
      db.insert(tbl_dtr, null, contentValues);
      return true;
   }

    public void time_changed(String time, String date)
      {
         SQLiteDatabase db = this.getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put("time_created", time);
         contentValues.put("date_created", date);
         db.insert("time_change", null, contentValues);
      }

    public Cursor getTimeChanged(){
         SQLiteDatabase db = this.getReadableDatabase();
         Cursor res =  db.rawQuery( "select * from time_change", null );
         return res;
      }

   public Cursor getData(int emp_id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select emp_id, emp_name, time_created, " + 
         "date_created, check_type, id, remarks, picture, hashcode from "+tbl_dtr + 
         " where emp_id = " + emp_id + " order by id desc", null );
      return res;
   }

   public Cursor getCheckType(int type){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery("select type_name from check_type where id = " + type, null);
      return res;
   }
   
   public Cursor getProject(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery("select * from projects where id = " + id, null);
      return res;
   }   

   public Cursor getProjectId(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery("select * from projects where selected = 1", null);
      return res;
   }

   public boolean insertRemarks(int id, String remarks){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("remarks", remarks);
      db.update(tbl_dtr, contentValues, "id=" + id, null);
      return true;
   }
   
   public Cursor findById(int id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select emp_id, emp_name, time_created, " + 
         "date_created, check_type, id, remarks, picture, hashcode, sync_status from "+tbl_dtr+" where id = " + id, null );
      return res;
   }

   public Cursor findAll(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr, null );
      return res;

   }

   public Cursor findLastFour(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr+" order by id desc limit 4", null );
      return res;         
   }

   public Cursor findGroupOperators(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr + " group by emp_id", null );
      return res;
   }

   public Cursor findAllUnsync(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr+" where sync_status = 0", null );
      return res;
   }

   public Cursor findAllSync(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr+" where sync_status = 1", null );
      return res;
   }

   public void deleteAllProjects(){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete("projects", null, null);
   }

   public void delete_sync_status(int id){
      SQLiteDatabase db = this.getWritableDatabase();
      db.delete(tbl_dtr, "id=" + id, null);  
   }

   public void update_sync_status(int id){
      SQLiteDatabase db = this.getWritableDatabase();
      ContentValues contentValues = new ContentValues();
      contentValues.put("sync_status", 1);
      db.update(tbl_dtr, contentValues, "id=" + id, null);
   }

   public Cursor findAllOperators(){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr+" group by emp_id", null );
      return res;
   }

   public Cursor findSingleDTR(int emp_id){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr+
         " where emp_id = " + emp_id, null );
      return res;
   }

   public Cursor searchOperator(int emp_id, String date_from, String date_to){
      SQLiteDatabase db = this.getReadableDatabase();
      Cursor res =  db.rawQuery( "select * from "+tbl_dtr+" where emp_id = " + emp_id + 
         " and (date_created between '"+date_from+"' and '"+date_to+"')", null );
      return res;
   }


}
