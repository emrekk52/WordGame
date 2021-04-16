package com.word.game.Activitys;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.word.game.Adapters.PagerViewAdapter;
import com.word.game.R;

public class SoruGonder extends AppCompatActivity {

    TextView soruGonder, gonderilenSoru;
    ViewPager fragmentViewpager;
    PagerViewAdapter pagerViewAdapter;
    private MediaPlayer click_sound;
    private ImageView backButton;
    private Animation scale_down, scale_up;
    public static SharedPreferences sharedPreferences;
    public static boolean st = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soru_gonder);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);


        soruGonder = findViewById(R.id.soruGonder);
        gonderilenSoru = findViewById(R.id.gonderilenSoru);
        fragmentViewpager = findViewById(R.id.fragmentViewpager);

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.click_sound);
        click_sound = MediaPlayer.create(getApplicationContext(), uri);
        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);


        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        fragmentViewpager.setAdapter(pagerViewAdapter);

        if (getIntent().getExtras() != null)
            st = getIntent().getExtras().getBoolean("st");
        if (getIntent().getExtras() != null && st == true)
            fragmentViewpager.setCurrentItem(1);

        fragmentViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onPageSelected(int position) {
                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        backButtonIslem();
        soruGonder.startAnimation(scale_up);
        gonderilenSoru.startAnimation(scale_down);

        soruGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewpager.setCurrentItem(0);
            }
        });
        gonderilenSoru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentViewpager.setCurrentItem(1);
            }
        });
    }

    public void onClick(View v) {
        super.onBackPressed(); // or super.finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onChangeTab(int position) {

        if (position == 0) {
            soruGonder.startAnimation(scale_up);
            gonderilenSoru.startAnimation(scale_down);
            soruGonder.setTextColor(getColor(R.color.lightColor));
            gonderilenSoru.setTextColor(getColor(R.color.darkColor));

        } else if (position == 1) {
            soruGonder.startAnimation(scale_down);
            gonderilenSoru.startAnimation(scale_up);
            gonderilenSoru.setTextColor(getColor(R.color.lightColor));
            soruGonder.setTextColor(getColor(R.color.darkColor));
        }


    }

    private void backButtonIslem() {

        backButton = findViewById(R.id.buttonBack);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.setAlpha(0.8f);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1f);
                    onClick(backButton);

                }

                return true;
            }
        });

    }
}