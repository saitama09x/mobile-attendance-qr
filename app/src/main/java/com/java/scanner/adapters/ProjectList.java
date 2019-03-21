package com.java.scanner.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import com.java.scanner.model.*;
import com.java.scanner.controller.*;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.app.Dialog;

public class ProjectList extends ArrayAdapter<String>{

private final Activity context;
private String[] projectname;
private String[] projectid;
private String[] status;
private Integer[] projsel;
private final Db_sqlite sqlite;
private Dialog dialog;

public ProjectList(Context context, String[] projectname, String[] projectid, String[] status, Integer[] projsel) {

	super(context, R.layout.project_list, projectname);
	this.context = (Activity) context;
	this.projectname = projectname;
	this.projectid = projectid;
	this.status = status;
	this.projsel = projsel;
	this.sqlite = new Db_sqlite(context);
	this.dialog = new Dialog(context);
}

@Override
public View getView(int position, View view, ViewGroup parent) {
	LayoutInflater inflater = this.context.getLayoutInflater();
	View rowView = inflater.inflate(R.layout.project_list, null, true);
	TextView name = (TextView) rowView.findViewById(R.id.projectname);
	TextView code = (TextView) rowView.findViewById(R.id.projectcode);	
	ImageView check = (ImageView) rowView.findViewById(R.id.checkmark);
	final LinearLayout wrapping = (LinearLayout) rowView.findViewById(R.id.wrapping);
	wrapping.setTag(projectid[position]);

	name.setText(projectname[position]);
	code.setText("[" + projectid[position] + "]");
	if(projsel[position] == 1){
		check.setImageResource(R.drawable.checkmark);
	}
	wrapping.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
             	View elem = (View) LayoutInflater.from(context).inflate(R.layout.passproject, null);
				dialog.setContentView(elem);
				dialog.show();             	
				final EditText password = (EditText) elem.findViewById(R.id.password);
				Button submit_pass = (Button) elem.findViewById(R.id.submit_pass);
				submit_pass.setOnClickListener(new View.OnClickListener() {
		             public void onClick(View v) {
		                 if(password.getText().toString().equals("abc")){
		                 		sqlite.activateProj(Integer.parseInt(wrapping.getTag().toString()));
				        		Intent goToNextActivity = new Intent(context.getApplicationContext(), ProjectsActivity.class);
								context.finish();
								context.startActivity(goToNextActivity);  
		                 }else{
		                 	Toast.makeText(context, "Wrong Password", Toast.LENGTH_SHORT);
		                 }
		             }
		         });        		          			
             }
         });

	return rowView;

}

}