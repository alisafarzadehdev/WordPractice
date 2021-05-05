package com.alisafarzadeh.roomdb;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;


import com.alisafarzadeh.roomdb.Utill.G;

import java.util.Locale;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MyServiceVocab extends Service implements TextToSpeech.OnInitListener {

    public static final String Channelid="com.alisafarzadeh.vocabbuilder.Service.Channel";
    int time;
    String[] per;
    String[] eng;
    Bundle bundle ;
    Boolean random;

    TextToSpeech textToSpeech;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    AlarmManager alarmManager;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            textToSpeech = new TextToSpeech(this,this);
            bundle = intent.getExtras();
            time = bundle.getInt("time");
            per = (String[]) bundle.get("per");
            eng = (String[]) bundle.get("eng");
            random =(Boolean) bundle.get("ran");

            Log.d("ddd",per.length+""+time);
            for (int i = 0; i < per.length ; i++) {
                Log.d("ServiceData","per:"+per[i]+" // eng:"+eng[i]+"  // Time : "+time);
            }
        }catch (Exception e)
        {

        }


        switch (intent.getAction())
        {
            case "close":
                stopForeground(true);
                stopSelf();
                textToSpeech.shutdown();
                break;
            case "play":
                startserviice();
                break;
        }


        /*
        alarmManager = (AlarmManager) G.context.getSystemService(ALARM_SERVICE);
        Intent myintent = new Intent(this,MyServiceVocab.class);
        PendingIntent p = PendingIntent.getService(this,0,myintent,0);
        // myintent.putExtra("countint",i);
        // myintent.setAction("play");
        i++;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,3000,3000,p);
         */

        //startserviice();



        /*
        for (int i = 0; i < eng.length; i++) {
            try {
                showNotife(per[i],eng[i]);
                Log.d("Vocab","{"+per[i]+"\n"+eng[i]+"}");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
             Log.d("dd",e.getMessage());
            }
        }

         */

        return START_STICKY;
    }

    public void notificationshow(String eng , String per)
    {
        Intent i = new Intent(this, MyServiceVocab.class);
        i.setAction("close");
        PendingIntent closepen = PendingIntent.getService(this,0,i,0);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pending =  PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder buildernotife = new NotificationCompat.Builder(this);
        RemoteViews remoteViews = new RemoteViews(this.getPackageName(),R.layout.notifelayout);
        remoteViews.setTextViewText(R.id.englayout,eng);
        remoteViews.setTextViewText(R.id.perlayout,per);
        remoteViews.setOnClickPendingIntent(R.id.imgclose,closepen);
        buildernotife.setCustomBigContentView(remoteViews).setOngoing(true)
                .setContentIntent(pending).setSmallIcon(R.drawable.logo).setSubText("notife").build();

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            //mChannel.enableLights(true);
            //mChannel.setLightColor(Color.RED);
            //mChannel.enableVibration(true);
            //mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);
            notificationManager.createNotificationChannel(mChannel);
        }

        startForeground(101,buildernotife.build());

    }
    public void showNotife(String content , String title)
    {
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT>=26)
        {
            NotificationChannel notificationChannel = new NotificationChannel(Channelid,"Vocab",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(),Channelid)
                .setContentText(content)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.ic_baseline_playcircle);
        startForeground(1,notificationCompatBuilder.build());

    }

    int i =0;
    public void startserviice()
    {
        Timer t = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                notificationshow(per[i],eng[i]);
                try {
                    textToSpeech.speak(eng[i], TextToSpeech.QUEUE_FLUSH, null);
                }
                catch (Exception e)
                {
                    Toast.makeText(G.context, e.getMessage()+"", Toast.LENGTH_SHORT).show();
                }

                if (random)
                {
                    Random random = new Random();
                    i=random.nextInt(per.length);
                }else
                {
                    i = i+1;
                    if (i == per.length)
                    {
                        i=0;
                    }
                }

            }
        };

        t.schedule(timerTask,2000, time*1000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status==TextToSpeech.SUCCESS){
            textToSpeech.setLanguage(Locale.UK);
            textToSpeech.setPitch(0.4f);
            textToSpeech.setSpeechRate(0.7f);
        }
    }
}
