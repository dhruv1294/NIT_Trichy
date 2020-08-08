package com.example.nittrichy.Notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.nittrichy.MessageActivity;
import com.example.nittrichy.PostActivity;
import com.example.nittrichy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    NotificationManager notificationManager;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

       if(remoteMessage.getData().get("sented")!=null) {
           Log.i("reached ","here message");
            String sented = remoteMessage.getData().get("sented");
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null && sented.equals(firebaseUser.getUid())) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    sendOreoNotification(remoteMessage);
                } else {
                    sendNotification(remoteMessage);
                }
            }
        }else{
            Log.i("reached ","here notify");
            //if(remoteMessage.getData().get("message")!=null) {
              //  Log.i("msg", remoteMessage.getData().get("message"));
                Intent intent = new Intent(this, PostActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                notificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    setupChannels();
                }
                int notificationId = 999;
                //int j = Integer.parseInt(user.replaceAll("[\\D]",""));
                Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel")
                        .setSmallIcon(R.drawable.nittlogo)
                        .setContentTitle("NIT TRICHY")
                        .setContentText("New Post/Notice/News is up")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setSound(defaultSoundUri);


                notificationManager.notify(notificationId, notificationBuilder.build());
            }
          /* OreoNotification oreoNotification = new OreoNotification(this);
           Notification.Builder builder = oreoNotification.getOreoNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"),pendingIntent,defaultSoundUri,Integer.toString(R.drawable.nittlogo));

           oreoNotification.getmanager().notify(notificationId,builder.build());*/

        //}
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(){
        String name = "name";
        NotificationChannel channel = new NotificationChannel("channel",name,NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(false);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }
    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title,body,pendingIntent,defaultSound,icon);
        int i=0;
        if(j>0){
            i=j;
        }
        oreoNotification.getmanager().notify(i,builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MessageActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userid",user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        int i=0;
        if(j>0){
            i=j;
        }
        noti.notify(i,builder.build());
    }

}
