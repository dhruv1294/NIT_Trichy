package com.example.nittrichy.Notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.nittrichy.PostActivity;
import com.example.nittrichy.R;

import java.util.Calendar;


import static com.example.nittrichy.PostActivity.CHANNEL_ID;

public class AlarmService extends Service {
    AlarmManager alarmManager;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //return super.onStartCommand(intent, flags, startId);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Log.i("reached","service");
        Intent notificationIntent = new Intent(this, PostActivity.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this,
                0, notificationIntent, 0);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("NIT TRICHY")
                .setContentText("SERVICE")
                .setSmallIcon(R.drawable.ic_bookmark_black_24dp)
                .setContentIntent(pendingIntent2)
                .build();
        startForeground(1, notification);
        String[] date1 = intent.getStringArrayExtra("date");
        String[] time1 = intent.getStringArrayExtra("time");
        Long millis = intent.getLongExtra("millis",0);
        Log.i("millis",Long.toString(millis));
        Log.i("Current",Long.toString(System.currentTimeMillis()));
        String title = intent.getStringExtra("title");
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(millis);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE,Integer.parseInt(date1[0])-1);
        calendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(time1[0]));
        calendar.set(Calendar.MINUTE,Integer.parseInt(time1[1]));
        calendar.set(Calendar.SECOND,0);
        Log.i("manual millis",Long.toString(calendar.getTimeInMillis()));
        Log.i("date",date1[0]+" "+ date1[1]+" "+date1[2]);
        Log.i("time",time1[0]+" " + time1[1]);


        //c.set(2020, 8, 22, 22, 46,0);
        Intent intentAlarm = new Intent(this, AlertReciever.class);
        intentAlarm.putExtra("title",title);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);


        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent intentAlarm = new Intent(this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intentAlarm, 0);
        alarmManager.cancel(pendingIntent);
    }
}
