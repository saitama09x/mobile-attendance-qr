package com.java.scanner.controller;

import android.content.BroadcastReceiver;
import android.widget.Toast;
import android.content.Intent;
import android.content.Context;
import com.java.scanner.model.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class TimeChangedReceiver extends BroadcastReceiver {
	private Db_sqlite sqlite;
    @Override
    public void onReceive(Context context, Intent intent) {
    	sqlite = new Db_sqlite(context);
    	SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:ss");

        String string_date = date_format.format(new Date());
        String string_time = time_format.format(new Date());

        sqlite.time_changed(string_time, string_date);
    }

}