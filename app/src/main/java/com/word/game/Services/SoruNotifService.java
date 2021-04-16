package com.word.game.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.word.game.Activitys.MainActivity;
import com.word.game.Activitys.SoruGonder;
import com.word.game.R;

public class SoruNotifService extends Service {

    SharedPreferences sharedPreferences;

    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public void onCreate() {
        super.onCreate();

        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);
        servisBildirimAc();

        try {
            database = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            myRef = database.getReference("gelenSorular/" + sharedPreferences.getString("myId", ""));
            kontrolOnaylandimi();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void bildirimGonder(String mesaj) {

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification_sound);
        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String CHANNEL_ID = "5";
        Notification notification = null;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "soru_durumu",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.enableVibration(true);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            notification = new Notification.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("Gönderdiğiniz sorunun durumu değişti!")
                    .setContentText(mesaj)
                    .setAutoCancel(true)
                    .setShowWhen(true)
                    .setWhen(System.currentTimeMillis())
                    .setContentIntent(PendingIntent.getActivity(this, 5, new Intent(this, SoruGonder.class).putExtra("st", true), PendingIntent.FLAG_UPDATE_CURRENT))
                    .setSound(uri)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setNumber(1)
                    .setVibrate(new long[]{700})
                    .build();
        } else {

            notification = new Notification.Builder(getApplicationContext())
                    .setContentTitle("Gönderdiğiniz sorunun durumu değişti!")
                    .setContentIntent(PendingIntent.getActivity(this, 5, new Intent(this, SoruGonder.class).putExtra("st", true), PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentText(mesaj)
                    .setShowWhen(true)
                    .setAutoCancel(true)
                    .setWhen(System.currentTimeMillis())
                    .setSound(uri)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.mipmap.ic_launcher))
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setNumber(1)
                    .setVibrate(new long[]{700})
                    .build();
        }

        manager.notify(5, notification);

    }

    private void servisBildirimAc() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String CHANNEL_ID = "my_channel_02";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    "sorudurum",
                    NotificationManager.IMPORTANCE_DEFAULT);

            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setContentTitle("")
                    .setContentText("").build();

            startForeground(1, notification);
        }
    }

    private void kontrolOnaylandimi() {

        myRef.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (dataSnapshot1.child("notif").getValue().toString().trim().matches("false")) {
                        if (dataSnapshot1.child("soruDurum").getValue().toString().trim().toLowerCase().matches("onaylandı")) {
                            bildirimGonder("Sorunuz onaylandı!");
                            dataSnapshot1.getRef().child("notif").setValue("true");
                        } else if (dataSnapshot1.child("soruDurum").getValue().toString().trim().toLowerCase().matches("reddedildi")) {
                            bildirimGonder("Maalesef, sorunuz uygun görülmemiştir!");
                            dataSnapshot1.getRef().child("notif").setValue("true");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
