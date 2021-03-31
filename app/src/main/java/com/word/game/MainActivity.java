package com.word.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference soru4, soru5, soru6, soru7, soru8, soru9;
    private SharedPreferences sharedPreferences;
    private ImageView can1, can2, can3;
    private ImageView basla_button, soru_gonder_button, skor_button, cikis_button, title;
    private Animation scale_up, scale_down, title_animation, heart_animaton;
    private MediaPlayer click_sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.click_sound);
        click_sound = MediaPlayer.create(getApplicationContext(), uri);
        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);


        can1 = findViewById(R.id.heart1);
        can2 = findViewById(R.id.heart2);
        can3 = findViewById(R.id.heart3);
        basla_button = findViewById(R.id.start_button);
        skor_button = findViewById(R.id.score_button);
        cikis_button = findViewById(R.id.exit_button);
        soru_gonder_button = findViewById(R.id.soru_gonder_button);
        title = findViewById(R.id.title);


        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        title_animation = AnimationUtils.loadAnimation(this, R.anim.title_loading_animation);
        heart_animaton = AnimationUtils.loadAnimation(this, R.anim.heart_animation);

        title.startAnimation(title_animation);


        can1.startAnimation(heart_animaton);
        can2.startAnimation(heart_animaton);
        can3.startAnimation(heart_animaton);

        database = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
        soruGir();


        yenileCanKayit();
        canDoldurBaslat();
        butonİslemleri();



    }

    private void butonİslemleri() {

        basla_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    v.startAnimation(scale_up);
                    v.setAlpha(1f);

                    if (sharedPreferences.getInt("kayitliCanSayisi", 3) > 0) {
                        sharedPreferences.edit().putInt("kayitliCanSayisi", sharedPreferences.getInt("kayitliCanSayisi", 3) - 1).apply();
                        startActivity(new Intent(getApplicationContext(), GameActivity.class));
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
                    } else
                        Toast.makeText(getApplicationContext(), "Can kalmadı!", Toast.LENGTH_SHORT).show();

                    canDoldurBaslat();
                }
                return true;
            }
        });


        cikis_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.startAnimation(scale_up);
                    v.setAlpha(1f);
                    finish();

                }

                return true;
            }
        });

        soru_gonder_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.startAnimation(scale_up);
                    v.setAlpha(1f);

                }

                return true;
            }
        });

        skor_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.startAnimation(scale_up);
                    v.setAlpha(1f);


                }

                return true;
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
    }


    private void soruGir() {


        ArrayList<Soru> sorular4 = new ArrayList();
        ArrayList<Soru> sorular5 = new ArrayList();
        ArrayList<Soru> sorular6 = new ArrayList();
        ArrayList<Soru> sorular7 = new ArrayList();
        ArrayList<Soru> sorular8 = new ArrayList();
        ArrayList<Soru> sorular9 = new ArrayList();

        sorular4.add(new Soru("“Ulvi” sözünün Türkçe kökenli eş anlamlısı", "YÜCE", 400, "4 HARFLİ SORULAR"));
        sorular4.add(new Soru("Deride sinirler boyunca beliren, “Gece yanığı” olarak da bilinen hastalık", "ZONA", 400, "4 HARFLİ SORULAR"));
        sorular4.add(new Soru("Ait olunan fakat uzak kalıp özlenen yer", "sıla", 400, "4 HARFLİ SORULAR"));
        sorular4.add(new Soru("Bakıma ve barınmaya muhtaç bir grup insanın oturduğu, yetiştirildiği veya bakıldığı kurum", "yurt", 400, "4 HARFLİ SORULAR"));


        sorular5.add(new Soru("Haber toplama, yayma ve üyelerine dağıtma işiyle uğraşan kuruluş", "Ajans", 500, "5 HARFLİ SORULAR"));
        sorular5.add(new Soru("Gelişen teknolojiyle birçok alanda insanın yerini alabileceği düşünülen elektromekanik araç", "Robot", 500, "5 HARFLİ SORULAR"));
        sorular5.add(new Soru("Akdeniz’e özgü, Deniz kıyısında bile yetişebilen bir ağaç", "Ilgın", 500, "5 HARFLİ SORULAR"));
        sorular5.add(new Soru("Bir desimetreküp hacmindeki ölçü birimi", "Litre", 500, "5 HARFLİ SORULAR"));


        sorular6.add(new Soru("Gönlü değişken, aşkı vefasız olan", "Hercai", 600, "6 HARFLİ SORULAR"));
        sorular6.add(new Soru("Şerit biçimde metal veya plastik levhalardan yapılmış bir tür perde", "Jaluzi", 600, "6 HARFLİ SORULAR"));
        sorular6.add(new Soru("Yakınma ve hafifseme yoluyla “Şimdiki devir” anlamında kullanılan sözcük", "Zamane", 600, "6 HARFLİ SORULAR"));
        sorular6.add(new Soru("Argoda, “Aldatarak parasını çekmek”", "Sağmak", 600, "6 HARFLİ SORULAR"));


        sorular7.add(new Soru("Tatlı ve hamur işi dükkanı", "Pastane", 700, "7 HARFLİ SORULAR"));
        sorular7.add(new Soru("Argoda, “Gönlü olup olmadığını anlamak için manalı tavırlarda bulunmak, kur yapmak”", "İşatmak", 700, "7 HARFLİ SORULAR"));
        sorular7.add(new Soru("“Amacında, yolunda” anlamında bir zarf", "Uğrunda", 700, "7 HARFLİ SORULAR"));
        sorular7.add(new Soru("Evliya mucizesi", "Keramet", 700, "7 HARFLİ SORULAR"));


        sorular8.add(new Soru("Bir baltaya sap olmadan, başıboş yaşama hali", "Haytalık", 800, "8 HARFLİ SORULAR"));
        sorular8.add(new Soru("“Olmak” fiili ile beraber kullanıldığında “Ölmek” anlamına gelen kelime", "Rahmetli", 800, "8 HARFLİ SORULAR"));
        sorular8.add(new Soru("Alacakların toplanması", "Tahsilat", 800, "8 HARFLİ SORULAR"));
        sorular8.add(new Soru("“Acınmak, yazıklanmak, teessüf etmek” anlamlarındaki söz", "Yerinmek", 800, "8 HARFLİ SORULAR"));


        sorular9.add(new Soru("Sıcak suya soğuk veya soğuğa sıcak su katma", "Ilıştırma", 900, "9 HARFLİ SORULAR"));
        sorular9.add(new Soru("Özellikle gözler için, “Yuvasından dışarıya doğru fırlamak” anlamında bir söz", "Pörtlemek", 900, "9 HARFLİ SORULAR"));
        sorular9.add(new Soru("“Bu işle ilgilenmem, buna karışmam” anlamında bir tabir", "Nemelazım", 900, "9 HARFLİ SORULAR"));
        sorular9.add(new Soru("Genellikle ev işlerinde çalışan, işverenlerin isteklerini yerine getiren emekçi", "Hizmetkar", 900, "9 HARFLİ SORULAR"));


        soru4 = database.getReference("soru4");
        soru5 = database.getReference("soru5");
        soru6 = database.getReference("soru6");
        soru7 = database.getReference("soru7");
        soru8 = database.getReference("soru8");
        soru9 = database.getReference("soru9");


        soru4.setValue(sorular4);
        soru5.setValue(sorular5);
        soru6.setValue(sorular6);
        soru7.setValue(sorular7);
        soru8.setValue(sorular8);
        soru9.setValue(sorular9);

    }


    private void canKayitAyarla() {

        if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 3) {
            can1.setImageDrawable(getDrawable(R.drawable.heart));
            can2.setImageDrawable(getDrawable(R.drawable.heart));
            can3.setImageDrawable(getDrawable(R.drawable.heart));
        } else if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 2) {
            can1.setImageDrawable(getDrawable(R.drawable.heart));
            can2.setImageDrawable(getDrawable(R.drawable.heart));
            can3.setImageDrawable(getDrawable(R.drawable.heart_bos));

        } else if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 1) {
            can1.setImageDrawable(getDrawable(R.drawable.heart));
            can2.setImageDrawable(getDrawable(R.drawable.heart_bos));
            can3.setImageDrawable(getDrawable(R.drawable.heart_bos));

        } else if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 0) {
            can1.setImageDrawable(getDrawable(R.drawable.heart_bos));
            can2.setImageDrawable(getDrawable(R.drawable.heart_bos));
            can3.setImageDrawable(getDrawable(R.drawable.heart_bos));

        }

    }

    private void canDoldurBaslat() {

        if (sharedPreferences.getInt("kayitliCanSayisi", 3) < 3) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                this.startForegroundService(new Intent(getApplicationContext(), HeartService.class));
            } else {
                this.startService(new Intent(getApplicationContext(), HeartService.class));
            }
        }
    }

    private void yenileCanKayit() {

        final Handler handler = new Handler();
        Timer timer;


        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        canKayitAyarla();
                    }
                });
            }
        };

        timer = new Timer();

        timer.schedule(timerTask, 1000, 1000);


    }


}