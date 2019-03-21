package com.java.scanner.controller;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class TimeAttendanceAdapter extends ArrayAdapter<String>{

private final Activity context;
private final String[] emp_name;
private final String[] emp_id;

public TimeAttendanceAdapter(Context context, String[] emp_name, String[] emp_id) {

super(context, R.layout.time_attendance_adapter, emp_name);
this.context = (Activity) context;
this.emp_name = emp_name;
this.emp_id = emp_id;
}

@Override
public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.time_attendance_adapter, null, true);
	
	TextView emp_name_ = (TextView) rowView.findViewById(R.id.emp_name);
	TextView emp_id_ = (TextView) rowView.findViewById(R.id.emp_id);
	final Button view_btn = (Button) rowView.findViewById(R.id.view_btn);	
	view_btn.setTag(emp_id[position]);
	view_btn.setOnClickListener(new View.OnClickListener() {
		public void onClick(View view){				
			Time_attendance.view_type = "attendance";
			Time_attendance.operator_id = Integer.parseInt(view_btn.getTag().toString());			
			Intent goToNextActivity = new Intent(context.getApplicationContext(), Time_attendance.class);
			context.finish();
			context.startActivity(goToNextActivity);
		}
	});

	emp_name_.setText(emp_name[position]);
	emp_id_.setText("( " + emp_id[position] + " )");

	return rowView;

}

}