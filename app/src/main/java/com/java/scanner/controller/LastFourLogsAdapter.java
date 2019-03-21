package com.java.scanner.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class LastFourLogsAdapter extends ArrayAdapter<String>{

private final Activity context;
private final String[] emp_name;
private final String[] emp_id;
private final String[] date_created;
private final String[] time_created;
private final String[] check;
private final Integer[] ids;
private final Integer[] sync_status;

public LastFourLogsAdapter(Context context, String[] emp_name, String[] emp_id,
	String[] date_created, String[] time_created, 
	String[] check, Integer[] ids, Integer[] sync_status) {

super(context, R.layout.dtr_list_adapter, date_created);
	this.context = (Activity) context;
	this.emp_name = emp_name;
	this.emp_id = emp_id;
	this.date_created = date_created;
	this.time_created = time_created;
	this.check = check;
	this.ids = ids;
	this.sync_status = sync_status;
}

@Override
public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.last_four_adapter, null, true);
	
	TextView emp_name_ = (TextView) rowView.findViewById(R.id.emp_name);
	TextView emp_id_ = (TextView) rowView.findViewById(R.id.emp_id);
	TextView date_created_ = (TextView) rowView.findViewById(R.id.date_created);
	TextView time_created_ = (TextView) rowView.findViewById(R.id.time_created);
	TextView check_ = (TextView) rowView.findViewById(R.id.check);	
	TextView sync_status_ = (TextView) rowView.findViewById(R.id.sync_status);	
	//final Button view_btn = (Button) rowView.findViewById(R.id.view_btn);	
	final LinearLayout click_line = (LinearLayout) rowView.findViewById(R.id.click_line);
	if(sync_status[position] == 1)	{
		sync_status_.setText("SYNCED");
	}else{
		sync_status_.setText("UNSYNCED");
	}

	click_line.setTag(ids[position]);
	click_line.setOnClickListener(new View.OnClickListener() {
		public void onClick(View view){				
			Time_attendance.attendance_id = Integer.parseInt(click_line.getTag().toString());
			Time_attendance.view_type = "view_check_last_four";			
			Intent goToNextActivity = new Intent(context.getApplicationContext(), Time_attendance.class);
			context.finish();
			context.startActivity(goToNextActivity);
		}
	});
/*
	view_btn.setTag(ids[position]);
	view_btn.setOnClickListener(new View.OnClickListener() {
		public void onClick(View view){				
			DtrActivity.ids = Integer.parseInt(view_btn.getTag().toString());
			Intent goToNextActivity = new Intent(context.getApplicationContext(), ViewCheck.class);
			context.finish();
			context.startActivity(goToNextActivity);
		}
	});
*/
	emp_name_.setText(emp_name[position]);
	emp_id_.setText("( " + emp_id[position] + " )");
	date_created_.setText(date_created[position]);
	time_created_.setText(time_created[position]);
	check_.setText(check[position]);

	return rowView;

}

}