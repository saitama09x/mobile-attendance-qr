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
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class DtrListAdapter extends ArrayAdapter<String>{

private final Activity context;
private final String[] date_created;
private final String[] time_created;
private final String[] check;
private final Integer[] ids;
private final Bitmap[] smallpic;
public DtrListAdapter(Context context, String[] date_created, String[] time_created, 
	String[] check, Integer[] ids, Bitmap[] smallpic) {

super(context, R.layout.dtr_list_adapter, date_created);
this.context = (Activity) context;
this.date_created = date_created;
this.time_created = time_created;
this.check = check;
this.ids = ids;
this.smallpic = smallpic;
}

@Override
public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = context.getLayoutInflater();
	View rowView= inflater.inflate(R.layout.dtr_list_adapter, null, true);

	TextView date_created_ = (TextView) rowView.findViewById(R.id.date_created);
	TextView time_created_ = (TextView) rowView.findViewById(R.id.time_created);
	TextView check_ = (TextView) rowView.findViewById(R.id.check);	
	ImageView smallpic_ = (ImageView) rowView.findViewById(R.id.smallpic);	

	final Button view_btn = (Button) rowView.findViewById(R.id.view_btn);	
	view_btn.setTag(Integer.toString(ids[position]));
	view_btn.setOnClickListener(new View.OnClickListener() {
		public void onClick(View v){				
				DtrActivity.ids = Integer.parseInt(view_btn.getTag().toString());
				Intent goToNextActivity = new Intent(context.getApplicationContext(), AfterTaking.class);
				context.finish();
				context.startActivity(goToNextActivity);
		}
	});

	date_created_.setText(date_created[position]);
	time_created_.setText(time_created[position]);
	check_.setText(check[position]);
	if(smallpic[position] != null){
		smallpic_.setImageBitmap(smallpic[position]);
	}	
	return rowView;

}

}