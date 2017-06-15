package com.example.asus.Planner.Activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.asus.Planner.R.layout.activity_main);


        TabHost tabhost = getTabHost();
        TabHost.TabSpec spec;
        Intent intent;


        intent = new Intent().setClass(this, MessageActivity.class);
        spec = tabhost.newTabSpec("Message").setIndicator("Message",null).setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, ReminderActivity.class);
        spec = tabhost.newTabSpec("Reminder").setIndicator("Reminder",null).setContent(intent);
        tabhost.addTab(spec);

        intent = new Intent().setClass(this, NoteActivity.class);
        spec = tabhost.newTabSpec("Notes").setIndicator("Notes",null).setContent(intent);
        tabhost.addTab(spec);

    }


}




