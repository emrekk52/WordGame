package com.word.game;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.os.BuildCompat;

public class HeartService extends Service {

    SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        if (sharedPreferences.getInt("kayitliCanSayisi", 3) < 3)
            canDoldur();
        servisBildirimAc();
    }

    private void canDoldur() {

        new CountDownTimer(900000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onFinish() {
                sharedPreferences.edit().putInt("kayitliCanSayisi", sharedPreferences.getInt("kayitliCanSayisi", 3) + 1).apply();
                bildirimGonder();
                if (sharedPreferences.getInt("kayitliCanSayisi", 3) < 3) {
                    canDoldur();
                } else {
                    stopService(new Intent(getApplicationContext(), HeartService.class));
                }

            }
        }.start();

    }

    private void servisBildirimAc() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_01";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bildirimGonder() {

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "52";
        Notification notification = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "can_doldu",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.enableVibration(true);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("Hadi canlar doldu!")
                    .setContentText("Rakiplerini geçmek için bekleme!")
                    .setAutoCancel(true)
                    .setContentIntent(PendingIntent.getActivity(this, 52, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                    .setSound(uri)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setNumber(1)
                    .setVibrate(new long[]{700})
                    .build();
        } else {

            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Hadi canlar doldu!")
                    .setContentIntent(PendingIntent.getActivity(this, 52, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentText("Rakiplerini geçmek için bekleme!")
                    .setAutoCancel(true)
                    .setSound(uri)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setNumber(1)
                    .setVibrate(new long[]{700})
                    .build();
        }

        manager.notify(52, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}