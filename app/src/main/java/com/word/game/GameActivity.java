package com.word.game;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import pl.droidsonroids.gif.GifTextView;

public class GameActivity extends AppCompatActivity {

    private TextView soruPuani, zaman, soru, soruKategori;
    private CountDownTimer countDownTimer;
    private long kalanZaman;
    private ImageView durdurButton, pasButton, harfAlButton;
    private boolean isMute = false;
    private LinearLayout soruKismi1, soruKismi2;
    private String soruCevap, öncekiSoru = "", simdikiSoru = "", öncekiKategori = "", hangiSoru = "soru4", pasGecilenSoru = "";

    private int soruSayaci = 0, anlikPuan = 0, kategoriSoruSayaci = 0, harfAlSayac = 0;
    private GifTextView harf1, harf2, harf3, harf4, harf5, harf6, harf7, harf8, harf9, anlikPuanText;
    private EditText cevapAlButton;
    private LottieAnimationView cevapGonderButton, tebriklerAnimation, loadingAnimation;
    private boolean isGeriSayim = false, isİlkmi = true;
    private RelativeLayout tahminKismi;
    private ArrayList<Integer> harfRandomSayiList;

    private ImageView can1, can2;
    SharedPreferences sharedPreferences;

    private MediaPlayer click_effect, gameMusic;

    private Animation scale_up, scale_down, heart_animation, harf_degis_anim;

    FirebaseDatabase database;
    DatabaseReference myRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);


        //tanımlamalar
        soruPuani = findViewById(R.id.soruPuani);
        soruKategori = findViewById(R.id.soruKategori);
        soru = findViewById(R.id.soru);
        soruKismi1 = findViewById(R.id.soruKismi1);
        soruKismi2 = findViewById(R.id.soruKismi2);
        zaman = findViewById(R.id.zaman);
        anlikPuanText = findViewById(R.id.anlikPuanText);
        tahminKismi = findViewById(R.id.tahminKismi);

        can1 = findViewById(R.id.can1);
        can2 = findViewById(R.id.can2);


        //tahmin butonlarının tanımlanması
        cevapAlButton = findViewById(R.id.cevapAlButton);
        cevapGonderButton = findViewById(R.id.cevapGonderButton);

        tebriklerAnimation = findViewById(R.id.tebriklerAnimation);
        loadingAnimation = findViewById(R.id.loading);
        loadingAnimation.setVisibility(View.VISIBLE);

        //tahminlerin girilmesi sonucu geribildirimin yapılması
        tahminAlİslemYap();

        harfRandomSayiList = new ArrayList<>();

        //harf tanımlamalarının yapılması
        harf1 = findViewById(R.id.harf1);
        harf2 = findViewById(R.id.harf2);
        harf3 = findViewById(R.id.harf3);
        harf4 = findViewById(R.id.harf4);
        harf5 = findViewById(R.id.harf5);
        harf6 = findViewById(R.id.harf6);
        harf7 = findViewById(R.id.harf7);
        harf8 = findViewById(R.id.harf8);
        harf9 = findViewById(R.id.harf9);

        try {
            database = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            myRef = database.getReference();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.click_sound);
        click_effect = MediaPlayer.create(getApplicationContext(), uri);

        oyunMuzikEfekti();

        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);
        heart_animation = AnimationUtils.loadAnimation(this, R.anim.title_loading_animation);
        harf_degis_anim = AnimationUtils.loadAnimation(this, R.anim.harf_degis_anim);

        can1.startAnimation(heart_animation);
        can2.startAnimation(heart_animation);

        durdurButtonİslem();
        pasButtonİslem();
        harfAlButtonİslem();

        arkaPlanEfekti();
        soruGetir();
        yenileCanKayit();

        extraButtonAnim();

    }

    private void durdurButtonİslem() {

        durdurButton = findViewById(R.id.durdurButton);

        durdurButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    click_effect.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.startAnimation(scale_up);
                    v.setAlpha(1f);

                    countDownTimer.cancel();
                    durdurGeriSayim();
                    tahminKismi.setVisibility(View.VISIBLE);
                    durdurButton.setAlpha(0.7f);
                    durdurButton.setEnabled(false);
                    harfAlButton.setEnabled(true);
                    harfAlButton.setAlpha(1f);
                    pasButton.setEnabled(false);
                    pasButton.setAlpha(0.7f);

                }

                return true;
            }
        });

    }

    private void oyunMuzikEfekti() {

        //oyun müziğinin başlangıçta açık ayarlanması

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.game_music);
        gameMusic = MediaPlayer.create(getApplicationContext(), uri);

        gameMusic.start();
        gameMusic.setLooping(true);


        LottieAnimationView soundAnim = findViewById(R.id.soundAnim);

        //başlangıçta oyun sesinin aktif olarak ayarlanmasına karşın animasyonların hazırlanması
        soundAnim.setMinAndMaxProgress(0.0f, 0.4f);
        soundAnim.playAnimation();
        isMute = true;


        //oyun arka planı ses aktif/pasif işlemleri
        soundAnim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMute) {
                    gameMusic.pause();
                    soundAnim.setMinAndMaxProgress(0.5f, 0.9f);
                    soundAnim.playAnimation();
                    isMute = false;

                } else {
                    if (!gameMusic.isPlaying())
                        gameMusic.start();
                    soundAnim.setMinAndMaxProgress(0.0f, 0.4f);
                    soundAnim.playAnimation();
                    isMute = true;
                }

            }
        });
    }

    //oyun arka planı zamanla renk geçiş efekti
    private void arkaPlanEfekti() {

        RelativeLayout activiyLinearLayout = findViewById(R.id.activityRelativeLayout);
        AnimationDrawable animationDrawable = (AnimationDrawable) activiyLinearLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

    }

    //oyun geri sayımın başlatılması
    private void geriSayim(boolean isFirst) {
        long saniye;
        if (!isFirst) {
            saniye = kalanZaman;
        } else
            saniye = 240000;

        countDownTimer = new CountDownTimer(saniye, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (((millisUntilFinished % 60000) / 1000) < 10)
                    zaman.setText(millisUntilFinished / 60000 + ":" + "0" + (millisUntilFinished % 60000) / 1000);
                else
                    zaman.setText(millisUntilFinished / 60000 + ":" + (millisUntilFinished % 60000) / 1000);

                kalanZaman = millisUntilFinished - 1000;
            }

            @Override
            public void onFinish() {
                Toast.makeText(getApplicationContext(), "ZAMAN SONA ERDİ!", Toast.LENGTH_LONG).show();
            }
        }.start();


    }

    //durdurulma esnasında 15 saniyelik cevap verme süresinin başlatılması
    private void durdurGeriSayim() {

        findViewById(R.id.countAnim).setVisibility(View.VISIBLE);
        final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.countdown_sound);

        countDownTimer = new CountDownTimer(15000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (millisUntilFinished / 1000 == 3)
                    mp.start();
            }

            @Override
            public void onFinish() {
                //verilen süre içerisinde doğru cevap verilememiş ve bir sonraki soruya geçilmiştir(puan alınmadı)
                geriSayimBitir();
                harfAlanSifirla();
                soruGetir();
            }
        }.start();


    }

    //15 saniyelik geri sayım bittiğinde yapılan işlemler
    private void geriSayimBitir() {
        countDownTimer.cancel();
        Toast.makeText(getApplicationContext(), "ZAMAN BİTTİ!", Toast.LENGTH_LONG).show();
        LottieAnimationView countAnim = findViewById(R.id.countAnim);
        countAnim.setVisibility(View.GONE);
        countAnim.setProgress(0);
        geriSayim(false);
        durdurButton.setAlpha(1f);
        durdurButton.setEnabled(true);
        harfAlButton.setEnabled(false);
        harfAlButton.setAlpha(0.7f);
        pasButton.setEnabled(true);
        pasButton.setAlpha(1f);
        tahminKismi.setVisibility(View.GONE);
    }

    //cevabın uzunluğuna göre yeni alanların açılması
    private String soruKarakterBelirleme() {


        TranslateAnimation harf1Anim = new TranslateAnimation(harf1.getWidth() - 300, 0, 0, 0);
        harf1Anim.setDuration(1000);
        TranslateAnimation harf2Anim = new TranslateAnimation(harf2.getWidth() - 400, 0, 0, 0);
        harf2Anim.setDuration(1100);
        TranslateAnimation harf3Anim = new TranslateAnimation(harf3.getWidth() - 500, 0, 0, 0);
        harf3Anim.setDuration(1200);
        TranslateAnimation harf4Anim = new TranslateAnimation(harf4.getWidth() - 600, 0, 0, 0);
        harf4Anim.setDuration(1300);
        TranslateAnimation harf5Anim = new TranslateAnimation(harf5.getWidth() - 700, 0, 0, 0);
        harf5Anim.setDuration(1400);
        TranslateAnimation harf6Anim = new TranslateAnimation(harf6.getWidth() - 800, 0, 0, 0);
        harf6Anim.setDuration(1500);
        TranslateAnimation harf7Anim = new TranslateAnimation(harf7.getWidth() - 900, 0, 0, 0);
        harf7Anim.setDuration(1600);
        TranslateAnimation harf8Anim = new TranslateAnimation(harf8.getWidth() - 1000, 0, 0, 0);
        harf8Anim.setDuration(1700);
        TranslateAnimation harf9Anim = new TranslateAnimation(harf9.getWidth() - 1100, 0, 0, 0);
        harf9Anim.setDuration(1800);


        harf1.startAnimation(harf1Anim);
        harf2.startAnimation(harf2Anim);
        harf3.startAnimation(harf3Anim);
        harf4.startAnimation(harf4Anim);

        System.out.println("soru sayacı " + soruSayaci);

        if (soruSayaci < 2) {
            hangiSoru = "soru4";
            soruKismi1.setVisibility(View.VISIBLE);

        } else if (soruSayaci < 4) {
            hangiSoru = "soru5";
            harf5.setVisibility(View.VISIBLE);
            harf5.startAnimation(harf5Anim);
        } else if (soruSayaci < 6) {
            hangiSoru = "soru6";
            soruKismi2.setVisibility(View.VISIBLE);
            harf6.setVisibility(View.VISIBLE);
            harf6.startAnimation(harf6Anim);
        } else if (soruSayaci < 8) {
            hangiSoru = "soru7";
            harf7.setVisibility(View.VISIBLE);
            harf7.startAnimation(harf7Anim);
        } else if (soruSayaci < 10) {
            hangiSoru = "soru8";
            harf8.setVisibility(View.VISIBLE);
            harf8.startAnimation(harf8Anim);
        } else if (soruSayaci < 12) {
            hangiSoru = "soru9";
            harf9.setVisibility(View.VISIBLE);
            harf9.startAnimation(harf9Anim);
        }


        return hangiSoru;
    }

    //yeni soru getirme
    private void soruGetir() {


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                kategoriSoruSayaci = 0;
                for (DataSnapshot dataSnapshot1 : dataSnapshot.child(hangiSoru).getChildren()) {
                    kategoriSoruSayaci++;
                }


                AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                alphaAnimation.setDuration(800);


                soru.startAnimation(alphaAnimation);
                soruKategori.startAnimation(alphaAnimation);
                soruPuani.startAnimation(alphaAnimation);


                //her kategoride bir pas hakkı vardır kontrolü
                int randomSoruSayisi = new Random().nextInt(kategoriSoruSayaci);


                //sorulan soruya göre cevap uzunluğunu belirleme işlemi
                simdikiSoru = dataSnapshot.child(soruKarakterBelirleme()).child(String.valueOf(randomSoruSayisi)).child("soru").getValue().toString();


                if (!öncekiSoru.matches(simdikiSoru) && !pasGecilenSoru.matches(simdikiSoru)) {
                    öncekiSoru = simdikiSoru;

                    soru.setText(simdikiSoru);
                    soruKategori.setText(dataSnapshot.child(soruKarakterBelirleme()).child(String.valueOf(randomSoruSayisi)).child("kategori").getValue().toString());
                    soruPuani.setText(dataSnapshot.child(soruKarakterBelirleme()).child(String.valueOf(randomSoruSayisi)).child("puan").getValue().toString());
                    soruCevap = dataSnapshot.child(soruKarakterBelirleme()).child(String.valueOf(randomSoruSayisi)).child("cevap").getValue().toString().trim().toUpperCase();


                    //sorular veritabanından geldikten sonra bir defaya mahsus geri sayım başlar!!
                    if (!isGeriSayim) {
                        geriSayim(true);
                        isGeriSayim = true;
                    }


                    soruSayaci++;

                    loadingAnimation.setVisibility(View.GONE);

                } else {
                    soruGetir();
                }

                if (!öncekiKategori.matches(soruKategori.getText().toString().trim())) {
                    öncekiKategori = soruKategori.getText().toString().trim();
                    pasButton.setEnabled(true);
                    pasButton.setAlpha(1f);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //verilen cevaba göre yapılan işlemler!
    private void tahminAlİslemYap() {


        cevapGonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String alinanCevap = cevapAlButton.getText().toString().trim().toUpperCase();
                cevapAlButton.setText("");

                cevapKontrol(alinanCevap);

            }
        });

    }

    //verilen cevabın kontrolü!
    private void cevapKontrol(String alinanCevap) {

        if (soruCevap.matches(alinanCevap)) {

            //doğru bildiği takdirde puan animasyonu başlar
            ValueAnimator anlikPuanAnimator = ValueAnimator.ofFloat(28, 19);
            anlikPuanAnimator.setDuration(500);
            anlikPuanAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float animatedValue = (float) valueAnimator.getAnimatedValue();
                    anlikPuanText.setTextSize(animatedValue);
                }
            });
            anlikPuanAnimator.start();

            final MediaPlayer win_Sound = MediaPlayer.create(getApplicationContext(), R.raw.win_sound_effect);
            win_Sound.start();
            tebriklerAnimation.setVisibility(View.VISIBLE);
            anlikPuan += Integer.parseInt(soruPuani.getText().toString().trim());
            anlikPuanText.setText(String.valueOf(anlikPuan));


            for (int i = 0; i < soruCevap.length(); i++) {
                char character = soruCevap.charAt(i);
                switch (i) {

                    case 0:
                        harf1.setText(String.valueOf(character));
                        break;
                    case 1:
                        harf2.setText(String.valueOf(character));
                        break;
                    case 2:
                        harf3.setText(String.valueOf(character));
                        break;
                    case 3:
                        harf4.setText(String.valueOf(character));
                        break;
                    case 4:
                        harf5.setText(String.valueOf(character));
                        break;
                    case 5:
                        harf6.setText(String.valueOf(character));
                        break;
                    case 6:
                        harf7.setText(String.valueOf(character));
                        break;
                    case 7:
                        harf8.setText(String.valueOf(character));
                        break;
                    case 8:
                        harf9.setText(String.valueOf(character));
                        break;

                }

            }


            islemDurdur();
            geriSayimBitir();

            //oyun bitti yeni oyuna başlamak için dokunun!! :))
            if (soruSayaci < 12) {
                soruGetir();
            } else {
                Toast.makeText(getApplicationContext(), "OYUN BİTTİ YENİ OYUNA BAŞLAMAK İÇİN TIKLAYIN", Toast.LENGTH_LONG).show();
            }

        } else {
            //yanlışsa titret
            final MediaPlayer incorrect_sound = MediaPlayer.create(getApplicationContext(), R.raw.incorrect_sound);
            incorrect_sound.start();
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(500);

        }
    }

    //doğru bilinen cevap sonrası kısa süreliğine patlama animasyonu ortaya çıkar ve süre durdurularak normal süreden kısılmaz!
    private void islemDurdur() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tebriklerAnimation.setVisibility(View.GONE);
                tebriklerAnimation.setProgress(0);
                harfAlanSifirla();
            }
        }, 2500);

    }

    //yeni soruya geçilir ve harfler sıfırlanır..
    private void harfAlanSifirla() {

        harf1.setText("");
        harf2.setText("");
        harf3.setText("");
        harf4.setText("");
        harf5.setText("");
        harf6.setText("");
        harf7.setText("");
        harf8.setText("");
        harf9.setText("");

    }

    //soru geçme butonu
    private void pasButtonİslem() {

        pasButton = findViewById(R.id.pasButton);
        pasButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    click_effect.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.startAnimation(scale_up);
                    v.setAlpha(1f);

                    pasButton.setEnabled(false);
                    pasButton.setAlpha(0.7f);
                    pasGecilenSoru = soru.getText().toString();
                    soruDegistir();

                }

                return true;
            }
        });

    }

    //olan soruyu değiştirme işlemi
    private void soruDegistir() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int randomSoruSayisi = 0;
                do {
                    randomSoruSayisi = new Random().nextInt(kategoriSoruSayaci);
                    simdikiSoru = dataSnapshot.child(hangiSoru).child(String.valueOf(randomSoruSayisi)).child("soru").getValue().toString();
                } while (öncekiSoru.matches(simdikiSoru) && pasGecilenSoru.matches(simdikiSoru));

                öncekiSoru = simdikiSoru;


                TranslateAnimation yeniSoruAnim = new TranslateAnimation(soru.getWidth() - 300, 0, 0, 0);
                yeniSoruAnim.setDuration(400);

                soru.startAnimation(yeniSoruAnim);
                soruKategori.startAnimation(yeniSoruAnim);
                soruPuani.startAnimation(yeniSoruAnim);

                soru.setText(simdikiSoru);
                soruKategori.setText(dataSnapshot.child(hangiSoru).child(String.valueOf(randomSoruSayisi)).child("kategori").getValue().toString());
                soruPuani.setText(dataSnapshot.child(hangiSoru).child(String.valueOf(randomSoruSayisi)).child("puan").getValue().toString());
                soruCevap = dataSnapshot.child(hangiSoru).child(String.valueOf(randomSoruSayisi)).child("cevap").getValue().toString().trim().toUpperCase();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //harf alma işlemlerinde her alınan harfte düşülen puan ve diğer işlemler
    private void harfAlButtonİslem() {

        harfAlButton = findViewById(R.id.harfAlButton);
        harfAlButton.setAlpha(0.7f);
        harfAlButton.setEnabled(false);


        harfAlButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    click_effect.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.startAnimation(scale_up);
                    v.setAlpha(1f);

                    char character = '*';

                    if (isİlkmi) {
                        harfRandomSayiList.clear();
                        isİlkmi = false;
                        for (int i = 0; i < soruCevap.length(); i++) {
                            harfRandomSayiList.add(i);
                        }
                        Collections.shuffle(harfRandomSayiList);
                    }
                    int üretilenSayi = harfRandomSayiList.get(harfAlSayac);

                    harfAlSayac++;

                    for (int i = 0; i < soruCevap.length(); i++) {
                        if (i == üretilenSayi)
                            character = soruCevap.charAt(i);
                    }


                    soruPuani.setText(String.valueOf(Integer.parseInt(soruPuani.getText().toString().trim()) - 100));

                    switch (üretilenSayi) {

                        case 0:
                            harf1.setText(String.valueOf(character));
                            harf1.startAnimation(harf_degis_anim);
                            break;
                        case 1:
                            harf2.setText(String.valueOf(character));
                            harf2.startAnimation(harf_degis_anim);
                            break;
                        case 2:
                            harf3.setText(String.valueOf(character));
                            harf3.startAnimation(harf_degis_anim);
                            break;
                        case 3:
                            harf4.setText(String.valueOf(character));
                            harf4.startAnimation(harf_degis_anim);
                            break;
                        case 4:
                            harf5.setText(String.valueOf(character));
                            harf5.startAnimation(harf_degis_anim);
                            break;
                        case 5:
                            harf6.setText(String.valueOf(character));
                            harf6.startAnimation(harf_degis_anim);
                            break;
                        case 6:
                            harf7.setText(String.valueOf(character));
                            harf7.startAnimation(harf_degis_anim);
                            break;
                        case 7:
                            harf8.setText(String.valueOf(character));
                            harf8.startAnimation(harf_degis_anim);
                            break;
                        case 8:
                            harf9.setText(String.valueOf(character));
                            harf9.startAnimation(harf_degis_anim);
                            break;

                    }

                    if (soruPuani.getText().toString().trim().matches("0")) {
                        isİlkmi = true;
                        harfAlSayac = 0;
                        cevapKontrol(soruCevap);
                    }

                }


                return true;
            }
        });


    }

    private void canKayitAyarla() {

        if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 2) {
            can1.setImageDrawable(getDrawable(R.drawable.heart));
            can2.setImageDrawable(getDrawable(R.drawable.heart));

        } else if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 1) {
            can1.setImageDrawable(getDrawable(R.drawable.heart));
            can2.setImageDrawable(getDrawable(R.drawable.heart_bos));

        } else if (sharedPreferences.getInt("kayitliCanSayisi", 3) == 0) {
            can1.setImageDrawable(getDrawable(R.drawable.heart_bos));
            can2.setImageDrawable(getDrawable(R.drawable.heart_bos));

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

    private void extraButtonAnim() {

        anlikPuanText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(0.6f);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1f);
                }

                return true;
            }
        });

        zaman.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(0.6f);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1f);
                }

                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameMusic.stop();
    }
}