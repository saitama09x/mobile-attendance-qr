package com.java.scanner.objects;

import com.java.scanner.model.*;
import com.java.scanner.adapters.*;
import com.java.scanner.controller.*;
import android.database.Cursor;
import java.util.*;
import android.content.Context;

public class Db_obj{

	private Context con;

	public Db_obj(Context con){
		this.con = con;
	}

	public List<Operator_class> getOperatorList(Cursor cur){

		List<Operator_class> ops_class = new ArrayList<Operator_class>();
		while(cur.isAfterLast() == false){
			ops_class.add(new Operator_class(
			cur.getInt(cur.getColumnIndex("emp_id")),
			cur.getString(cur.getColumnIndex("emp_name"))));
			cur.moveToNext();
		}

		return ops_class;
	}	

	public SingleDtrAdapter searchOperator(Cursor res, Db_sqlite sqlite){

		List<String> list_emp_name = new ArrayList<String>();
		List<String> list_emp_id = new ArrayList<String>();
		List<String> list_date_created = new ArrayList<String>();
		List<String> list_time_created = new ArrayList<String>();
		List<String> list_check = new ArrayList<String>();
		List<Integer> list_ids = new ArrayList<Integer>();
		List<String> list_project = new ArrayList<String>();
		List<Integer> list_sync_status = new ArrayList<Integer>();
		if(res.getCount() > 0){
		res.moveToFirst();
		while(res.isAfterLast() == false){
		list_emp_name.add(res.getString(res.getColumnIndex("emp_name")));
		list_emp_id.add(Integer.toString(res.getInt(res.getColumnIndex("emp_id"))));
		list_date_created.add(res.getString(res.getColumnIndex("time_created")));
		list_time_created.add(res.getString(res.getColumnIndex("date_created")));
		list_sync_status.add(res.getInt(res.getColumnIndex("sync_status")));
		list_ids.add(res.getInt(res.getColumnIndex("id")));

		Cursor res_type = sqlite.getCheckType(res.getInt(res.getColumnIndex("check_type")));
		res_type.moveToFirst();                
		String type_name = res_type.getString(0);
		list_check.add(type_name);
		Cursor project_id = sqlite.getProject(res.getInt(res.getColumnIndex("project_id")));
		if(project_id.getCount() != 0){
			project_id.moveToFirst();
			String project_name = project_id.getString(project_id.getColumnIndex("project_name"));
			list_project.add(project_name);
		}else{
			list_project.add("No Project");
		}
		res.moveToNext(); 	
		}
		}
		String[] emp_name = list_emp_name.toArray(new String[list_emp_name.size()]);
		String[] emp_id = list_emp_id.toArray(new String[list_emp_id.size()]);
		String[] date_created = list_date_created.toArray(new String[list_date_created.size()]);
		String[] time_created = list_time_created.toArray(new String[list_time_created.size()]);
		Integer[] sync_status = list_sync_status.toArray(new Integer[list_sync_status.size()]);
		String[] check = list_check.toArray(new String[list_check.size()]);
		Integer[] ids = list_ids.toArray(new Integer[list_ids.size()]);
		String[] projects = list_project.toArray(new String[list_project.size()]);

		SingleDtrAdapter adapter = new SingleDtrAdapter(con, emp_name, emp_id, 
		date_created, time_created, check, ids, sync_status, projects);
		return adapter;
	}

	public TimeAttendanceAdapter list_operators(Cursor res){
		int count_row = res.getCount();
		List<String> list_emp_name = new ArrayList<String>();
		List<String> list_emp_id = new ArrayList<String>();
		if(count_row > 0){
		res.moveToFirst();
		while(res.isAfterLast() == false){
		list_emp_name.add(res.getString(res.getColumnIndex("emp_name")));
		list_emp_id.add(Integer.toString(res.getInt(res.getColumnIndex("emp_id"))));
		res.moveToNext();		
		}
		}
		String[] emp_name = list_emp_name.toArray(new String[list_emp_name.size()]);
		String[] emp_id = list_emp_id.toArray(new String[list_emp_id.size()]);

		TimeAttendanceAdapter adapter = new TimeAttendanceAdapter(con, emp_name, emp_id);
		return adapter;
	}
}