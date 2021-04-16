package com.word.game.Activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.Lottie;
import com.airbnb.lottie.LottieAnimationView;
import com.word.game.R;


public class SplashScreen extends AppCompatActivity {

    private Button button;
    private TextView message, title;
    private ImageView triangle;
    private Animation scale_up, triangle_anim;
    private LottieAnimationView no_internet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        scale_up = AnimationUtils.loadAnimation(this, R.anim.heart_animation);
        triangle_anim = AnimationUtils.loadAnimation(this, R.anim.triangle_anim);

        no_internet = findViewById(R.id.no_internet);
        triangle = findViewById(R.id.triangle);
        title = findViewById(R.id.title);


        button = findViewById(R.id.yenidenDene);
        message = findViewById(R.id.messageText);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yenidenDene(false);
            }
        });


        title.startAnimation(scale_up);
        triangle.startAnimation(triangle_anim);
        findViewById(R.id.hexa1).startAnimation(scale_up);
        findViewById(R.id.hexa2).startAnimation(scale_up);
        findViewById(R.id.hexa3).startAnimation(scale_up);
        findViewById(R.id.hexa4).startAnimation(scale_up);
        findViewById(R.id.hexa5).startAnimation(scale_up);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                yenidenDene(true);
            }
        }, 2000);

    }


    private void yenidenDene(boolean state) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();

        } else {
            if (state) {
                triangle.setVisibility(View.GONE);
                title.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                message.setVisibility(View.VISIBLE);
                no_internet.setVisibility(View.VISIBLE);
                button.startAnimation(scale_up);
                message.startAnimation(scale_up);
                no_internet.startAnimation(scale_up);
            }

        }

    }

}