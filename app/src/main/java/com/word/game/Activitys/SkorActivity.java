package com.word.game.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
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
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.word.game.R;
import com.word.game.Models.Skor;
import com.word.game.Adapters.SkorAdapter;

import java.util.ArrayList;
import java.util.Comparator;

public class SkorActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView mySkor, myZaman, userText,mySonSkor;
    private MediaPlayer click_sound;
    private Animation scale_down, scale_up;
    private RecyclerView recyclerView;
    private ArrayList<Skor> skors = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private SkorAdapter skorAdapter;
    public static SharedPreferences sharedPreferences;
    private ShimmerFrameLayout challengePlace;
    private TextView firstSkor, firstUsername, secondSkor, secondUsername, thirdSkor, thirdUsername;
    private SwipeRefreshLayout swipeRefr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_skor);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.click_sound);
        click_sound = MediaPlayer.create(getApplicationContext(), uri);
        scale_down = AnimationUtils.loadAnimation(this, R.anim.scale_down);
        scale_up = AnimationUtils.loadAnimation(this, R.anim.scale_up);

        recyclerView = findViewById(R.id.recyclerView);
        mySkor = findViewById(R.id.mySkor);
        mySonSkor = findViewById(R.id.mySonSkor);
        myZaman = findViewById(R.id.myZaman);
        userText = findViewById(R.id.userText);
        swipeRefr = findViewById(R.id.swipeRefr);

        firstSkor = findViewById(R.id.firstSkor);
        firstUsername = findViewById(R.id.firstUsername);
        secondSkor = findViewById(R.id.secondSkor);
        secondUsername = findViewById(R.id.secondUsername);
        thirdSkor = findViewById(R.id.thirdSkor);
        thirdUsername = findViewById(R.id.thirdUsername);

        challengePlace = findViewById(R.id.challangePlace);
        challengePlace.startShimmer();

        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);


        if (!sharedPreferences.getString("userName", "kullanici").matches("kullanici")) {
            userText.setText("Merhaba, " + sharedPreferences.getString("userName", "kullanici"));
        } else {
            userText.setText("Merhaba, " + sharedPreferences.getString("myId", ""));
        }

        swipeRefr.setColorSchemeColors(getResources().getColor(R.color.beklemede), getResources().getColor(R.color.onaylandi), getResources().getColor(R.color.reddedildi));
        swipeRefr.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSkor();
            }
        });


        try {
            firebaseDatabase = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("skor");
            getSkor();
        } catch (Exception e) {
            e.printStackTrace();
        }


        buttonIslem();


    }


    public void onClick(View v) {
        super.onBackPressed();
    }

    private void buttonIslem() {

        backButton = findViewById(R.id.backButton);
        backButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.startAnimation(scale_down);
                    v.setAlpha(0.8f);


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.startAnimation(scale_up);
                    v.setAlpha(1f);
                    onClick(backButton);

                }

                return true;
            }
        });

    }

    private void getSkor() {
        try {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    skors.clear();

                    for (DataSnapshot skorSnapshot : dataSnapshot.getChildren()) {
                        Skor skor = new Skor(Integer.parseInt(skorSnapshot.child("skor").getValue().toString()), Integer.parseInt(skorSnapshot.child("sonSkor").getValue().toString()), skorSnapshot.child("zaman").getValue().toString(), skorSnapshot.child("userID").getValue().toString(), skorSnapshot.child("userName").getValue().toString());
                        skors.add(skor);
                        if (skorSnapshot.getKey().matches(sharedPreferences.getString("myId", ""))) {
                            mySkor.setText(skorDuzenle(Integer.parseInt(skorSnapshot.child("skor").getValue().toString())));
                            mySonSkor.setText(skorDuzenle(Integer.parseInt(skorSnapshot.child("sonSkor").getValue().toString())));
                            myZaman.setText("Zaman " + skorSnapshot.child("zaman").getValue().toString());
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        skors.sort(Comparator.comparing(Skor::getSkor).reversed());
                    }

                    for (Skor skorList : skors) {
                        if (skorList.getUserID().matches(sharedPreferences.getString("myId", ""))) {
                            findViewById(R.id.card).setVisibility(View.VISIBLE);
                            findViewById(R.id.sizText).setVisibility(View.VISIBLE);
                            break;
                        }
                    }


                    if (skors.size() > 3) {

                        ArrayList<Skor> newSkorList = new ArrayList<>();

                        for (int i = 3; i < skors.size(); i++) {
                            newSkorList.add(skors.get(i));
                        }


                        skorAdapter = new SkorAdapter(newSkorList);
                        recyclerView.setAdapter(skorAdapter);


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                skorAdapter.isShimmer = false;
                                skorAdapter.notifyDataSetChanged();

                            }
                        }, 1000);
                    }

                    for (int i = 0; i < skors.size(); i++) {

                        switch (i) {
                            case 0:
                                findViewById(R.id.firstLayout).setVisibility(View.VISIBLE);
                                firstSkor.setText(skorDuzenle(skors.get(i).getSkor()));
                                firstUsername.setText(skors.get(i).getUserName());
                                break;

                            case 1:
                                findViewById(R.id.secondLayout).setVisibility(View.VISIBLE);
                                secondSkor.setText(skorDuzenle(skors.get(i).getSkor()));
                                secondUsername.setText(skors.get(i).getUserName());
                                break;

                            case 2:
                                findViewById(R.id.thirdLayout).setVisibility(View.VISIBLE);
                                thirdSkor.setText(skorDuzenle(skors.get(i).getSkor()));
                                thirdUsername.setText(skors.get(i).getUserName());
                                break;

                        }

                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            challengePlace.stopShimmer();
                            challengePlace.setShimmer(null);
                            swipeRefr.setRefreshing(false);

                        }
                    }, 1000);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String skorDuzenle(int skor) {

        String duzenlenmisSkor = String.valueOf(skor);

        if (skor >= 1000 && skor < 10000) {

            duzenlenmisSkor = skor / 1000 + "," + (skor % 1000) / 100 + "" + (skor % 100) / 10 + "" + skor % 10;
        } else if (skor >= 10000 && skor < 100000) {
            duzenlenmisSkor = skor / 10000 + "" + (skor % 10000) / 1000 + "," + (skor % 1000) / 100 + "" + (skor % 100) / 10 + "" + skor % 10;
        }


        return duzenlenmisSkor;
    }


}