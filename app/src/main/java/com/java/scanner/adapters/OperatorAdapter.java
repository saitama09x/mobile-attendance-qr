package com.java.scanner.adapters;

import java.util.*;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.java.scanner.controller.*;
import com.java.scanner.model.*;

public class OperatorAdapter extends ArrayAdapter<String>{

	private Activity activity;
    private List date;
    LayoutInflater inflater;
    private Operator_class month;

    public OperatorAdapter(Context con, int textViewResourceId, List date) 
     {
        super(con, textViewResourceId, date);
         
        /********** Take passed values **********/
        this.activity = (Activity) con;
        this.date     = date;

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
         
      }
 
    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
    	 View row = inflater.inflate(R.layout.date_spinner, parent, false);
    	 TextView text = (TextView) row.findViewById(R.id.sample);
         month = (Operator_class) date.get(position);
         text.setText(month.getName());
    	 return row;
    }

}