package com.word.game.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.word.game.Activitys.SoruGonder;
import com.word.game.Adapters.GonderilenSoruAdapter;
import com.word.game.R;
import com.word.game.Models.Soru;

import java.util.ArrayList;
import java.util.Comparator;

public class GonderilenSoruFragment extends Fragment {

    private RecyclerView recyclerViewGonderilen;
    private LottieAnimationView toggleButton;
    private ShimmerFrameLayout shimmerText;
    private TextView kayitSayisi;
    private LottieAnimationView notFoundAnim;
    private SwipeRefreshLayout swiperRefresh;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private GonderilenSoruAdapter gonderilenSoruAdapter;
    private ArrayList<Soru> sorus = new ArrayList<>();
    private boolean isClose = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_gonderilen_sorular, null);
        recyclerViewGonderilen = view.findViewById(R.id.recyclerViewGonderilen);
        kayitSayisi = view.findViewById(R.id.kayitSayisi);
        swiperRefresh = view.findViewById(R.id.swiperRefresh);

        notFoundAnim = view.findViewById(R.id.notFoundAnim);
        toggleButton = view.findViewById(R.id.toggleButton);
        shimmerText = view.findViewById(R.id.shimmerText);
        recyclerViewGonderilen.setHasFixedSize(true);
        recyclerViewGonderilen.setLayoutManager(new LinearLayoutManager(view.getContext()));


        if (SoruGonder.st) {
            toggleButton.setProgress(0f);
            isClose = true;
        } else {
            toggleButton.setProgress(0.5f);
        }

        try {
            firebaseDatabase = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("gelenSorular");
            getSorular();
        } catch (Exception e) {
            e.printStackTrace();
        }


        swiperRefresh.setColorSchemeColors(getResources().getColor(R.color.beklemede), getResources().getColor(R.color.onaylandi), getResources().getColor(R.color.reddedildi));
        swiperRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getSorular();
            }
        });


        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isClose) {
                    toggleButton.setMinAndMaxProgress(0.0f, 0.5f);
                    isClose = false;

                } else {
                    toggleButton.setMinAndMaxProgress(0.5f, 1f);
                    isClose = true;
                }
                toggleButton.playAnimation();
                getSorular();
            }
        });


        return view;
    }


    private void getSorular() {


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                shimmerText.startShimmer();
                shimmerText.setShimmer(new Shimmer.AlphaHighlightBuilder()
                        .setBaseAlpha(0.1f)
                        .setIntensity(0)
                        .build());


                sorus.clear();

                for (DataSnapshot gelenSoruDataSnapshot : dataSnapshot.getChildren()) {
                    if (isClose) {
                        if (gelenSoruDataSnapshot.getKey().matches(SoruGonder.sharedPreferences.getString("myId", ""))) {
                            for (DataSnapshot gelenSnapshot : gelenSoruDataSnapshot.getChildren()) {
                                Soru soru = new Soru(gelenSnapshot.child("soru").getValue().toString().trim(), gelenSnapshot.child("cevap").getValue().toString().trim().toUpperCase(), Integer.parseInt(gelenSnapshot.child("puan").getValue().toString().trim()), gelenSnapshot.child("kategori").getValue().toString().trim().toUpperCase(), gelenSnapshot.child("userName").getValue().toString().trim(), gelenSnapshot.child("zaman").getValue().toString().trim(), gelenSnapshot.child("soruKey").getValue().toString().trim(), gelenSnapshot.child("soruDurum").getValue().toString().trim(), gelenSnapshot.child("notif").getValue().toString().trim());
                                sorus.add(soru);
                            }
                        }
                    } else {
                        for (DataSnapshot gelenSnapshot : gelenSoruDataSnapshot.getChildren()) {
                            Soru soru = new Soru(gelenSnapshot.child("soru").getValue().toString().trim(), gelenSnapshot.child("cevap").getValue().toString().trim().toUpperCase(), Integer.parseInt(gelenSnapshot.child("puan").getValue().toString().trim()), gelenSnapshot.child("kategori").getValue().toString().trim().toUpperCase(), gelenSnapshot.child("userName").getValue().toString().trim(), gelenSnapshot.child("zaman").getValue().toString().trim(), gelenSnapshot.child("soruKey").getValue().toString().trim(), gelenSnapshot.child("soruDurum").getValue().toString().trim(),gelenSnapshot.child("notif").getValue().toString().trim());
                            sorus.add(soru);
                        }
                    }

                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    sorus.sort(Comparator.comparing(Soru::getZaman).reversed());
                }

                if (sorus.size() <= 0)
                    notFoundAnim.setVisibility(View.VISIBLE);
                else
                    notFoundAnim.setVisibility(View.GONE);


                gonderilenSoruAdapter = new GonderilenSoruAdapter(sorus);
                recyclerViewGonderilen.setAdapter(gonderilenSoruAdapter);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gonderilenSoruAdapter.isShimmer = false;
                        gonderilenSoruAdapter.notifyDataSetChanged();
                        swiperRefresh.setRefreshing(false);
                        shimmerText.setShimmer(new Shimmer.AlphaHighlightBuilder()
                                .setBaseAlpha(1)
                                .setIntensity(0)
                                .build());
                        kayitSayisi.setText("Toplamda " + sorus.size() + " adet\nsoru listelendi!");
                        shimmerText.stopShimmer();
                    }
                }, 1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}
