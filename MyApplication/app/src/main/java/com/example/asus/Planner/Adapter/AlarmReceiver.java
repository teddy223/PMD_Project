package com.example.asus.Planner.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.example.asus.Planner.R;



public class AlarmReceiver extends BroadcastReceiver {
    MediaPlayer player;
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm aktif!", Toast.LENGTH_LONG).show();
        player = MediaPlayer.create(context, R.raw.ringtone);
        player.start();
    }

}

