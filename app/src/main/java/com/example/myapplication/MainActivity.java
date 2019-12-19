package com.example.myapplication;

import android.app.AlarmManager;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ListActivity {

    private String path;
    int[] icons = {R.drawable.folder,R.drawable.file,R.drawable.help2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        ListView listView = findViewById(android.R.id.list);
        registerForContextMenu(listView);

        // Use the current directory as title

        path = "/storage/emulated/0";

        final TextView pathOutput = findViewById(R.id.pathOutput);

        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        pathOutput.setText("Storage"+path.substring(19));

        final ImageButton b1 = findViewById(R.id.imageButton);

        b1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getBaseContext(),"Welcome, this app automatically deletes files from chosen folders at the end of the month. Long press folders to activate, long press files to exempt them from deletion.", Toast.LENGTH_LONG).show();
            }
        });

        // Read all files sorted into the values-array
        List<String> values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            pathOutput.setText("(inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
            if (values.isEmpty()){
                Toast.makeText(this,"This directory is empty.", Toast.LENGTH_LONG).show();
            }
        }
        Collections.sort(values);


        // Put the data into the list
       ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);

        CustomAdapter myAdapter = new CustomAdapter(MainActivity.this, values, icons);
        listView.setAdapter(myAdapter);



        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //if (!prefs.getBoolean("firstTime", false)) {

            Intent alarmIntent = new Intent(this, AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

            AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.DAY_OF_MONTH, 19);
            //calendar.set(Calendar.HOUR_OF_DAY, 7);
            //calendar.set(Calendar.MINUTE, 0);
            //calendar.set(Calendar.SECOND, 1);

            manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY*30, pendingIntent);

            //SharedPreferences.Editor editor = prefs.edit();
            //editor.putBoolean("firstTime", true);
            //editor.apply();
        //}
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("path", filename);
            startActivity(intent);
        } else {
            Toast.makeText(this, filename + " is not a directory", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        int index = info.position;

        super.onCreateContextMenu(menu, v, menuInfo);

        String filename = (String) getListAdapter().getItem(index);

        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }



        if (new File(filename).isDirectory()) {
            menu.setHeaderTitle("Choose option for this folder:");
            menu.add(0, v.getId(), 0, "Set for deletion.");
        } else {
            menu.setHeaderTitle("Choose option for this file:");
            menu.add(0, v.getId(), 0, "Exempt file.");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;

        String filename = (String) getListAdapter().getItem(index);
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }

        if (new File(filename).isDirectory()) {
            Toast.makeText(this, "The folder at " + filename + " will be cleared at the end of each month.", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "The file at " + filename + " is exempt from deletion.", Toast.LENGTH_LONG).show();
        }

            return true;
    }
}