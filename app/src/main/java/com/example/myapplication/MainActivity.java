package com.example.myapplication;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends ListActivity {

    private String path;
    //private ListView listView1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_files);

        // Use the current directory as title

        path = "/storage/emulated/0";

        final TextView pathOutput = findViewById(R.id.pathOutput);

        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        pathOutput.setText("Storage"+path.substring(19));

        // Read all files sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }
        String[] list = dir.list();
        if (list != null) {
            for (String file : list) {
                if (!file.startsWith(".")) {
                    values.add(file);
                }
            }
        }
        Collections.sort(values);

      /*  CustomAdapter adapter = new CustomAdapter(this,  textString, drawableIds);


        listView1 = (ListView)findViewById(R.id.list);
        listView1.setAdapter(adapter);
*/
        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);
    }

    public static void mercenary(){
        int i = 25;
        int b = 57488387;
        System.out.println(i+b);

        for(int i; i>10; i--){
            System.out.println("It's the final countdown!");

            for(String n: array){
        }
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
}

