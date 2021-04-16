package com.word.game.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.word.game.Fragments.GonderilenSoruFragment;
import com.word.game.R;
import com.word.game.Models.Soru;
import com.word.game.Activitys.SoruGonder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class GonderilenSoruAdapter extends RecyclerView.Adapter<GonderilenSoruAdapter.GonderilenViewHolder> {

    private ArrayList<Soru> sorus;
    private int[] avatars = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8};
    private int sayac = 0;
    public boolean isShimmer=true;
    int shimmerNumber;


    public GonderilenSoruAdapter(ArrayList<Soru> sorus) {
        this.sorus = sorus;
        shimmerNumber = sorus.size();

    }

    @NonNull
    @Override
    public GonderilenViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GonderilenViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.gonderilen_sorular_design,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GonderilenViewHolder holder, int position) {
        if (isShimmer)
            holder.shimmerGonderilenSorular.startShimmer();
        else {
            holder.shimmerGonderilenSorular.stopShimmer();
            holder.shimmerGonderilenSorular.setShimmer(null);
            holder.bindSkor(sorus.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return isShimmer ? shimmerNumber : sorus.size();
    }

    class GonderilenViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        ShimmerFrameLayout shimmerGonderilenSorular;
        FrameLayout timeLine;
        View timeDesign;
        Button onaylaButton, reddetButton;
        TextView tarihZaman, gelenUserName, gelenSoru, gelenCevap, gelenKategori, soruDurum, soruGecenZaman;

        public GonderilenViewHolder(@NonNull View itemView) {
            super(itemView);

            shimmerGonderilenSorular = itemView.findViewById(R.id.shimmerGonderilenSorular);
            avatar = itemView.findViewById(R.id.avatar);
            tarihZaman = itemView.findViewById(R.id.tarihZaman);
            onaylaButton = itemView.findViewById(R.id.onaylaButton);
            reddetButton = itemView.findViewById(R.id.reddetButton);
            gelenUserName = itemView.findViewById(R.id.gelenUserName);
            timeDesign = itemView.findViewById(R.id.timeView);
            timeLine = itemView.findViewById(R.id.timeLine);
            gelenSoru = itemView.findViewById(R.id.gelenSoru);
            gelenCevap = itemView.findViewById(R.id.gelenCevap);
            gelenKategori = itemView.findViewById(R.id.gelenKategori);
            soruDurum = itemView.findViewById(R.id.soruDurum);
            soruGecenZaman = itemView.findViewById(R.id.soruGecenZaman);

        }


        void bindSkor(Soru sorus) {

            try{
                String[] d = sorus.getZaman().split("/");
                String[] d2 = d[1].split("\n");
                sorus.setZaman(d2[1] + "\n" + d2[0] + "/" + d[0]);
            }catch (Exception e){e.printStackTrace();}


            soruGecenZaman.setText(gecenZamanHesapla(sorus.getZaman()));
            soruDurum.setText(sorus.getSoruDurum());
            gelenUserName.setText(sorus.getUserName());
            tarihZaman.setText(sorus.getZaman());
            gelenSoru.setText(sorus.getSoru());
            gelenCevap.setText(sorus.getCevap());
            gelenKategori.setText(sorus.getKategori());
            avatar.setImageResource(avatars[sayac]);
            if (sayac == 7) {
                sayac = 0;
            } else
                sayac++;

            if (sorus.getSoruDurum().matches("beklemede"))
                soruDurum.setTextColor(Color.parseColor("#FFDD00"));
            else if (sorus.getSoruDurum().matches("onaylandı"))
                soruDurum.setTextColor(Color.parseColor("#1DAF3D"));
            else
                soruDurum.setTextColor(Color.parseColor("#C81919"));


            reddetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soruBul(false, sorus.getSoruKey());
                }
            });

            onaylaButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    soruBul(true, sorus.getSoruKey());

                }
            });


            FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/").getReference().child("admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue().toString().matches(SoruGonder.sharedPreferences.getString("myId", ""))) {
                        if (sorus.getSoruDurum().matches("beklemede")) {
                            soruDurum.setVisibility(View.GONE);
                            onaylaButton.setVisibility(View.VISIBLE);
                            reddetButton.setVisibility(View.VISIBLE);
                        } else {
                            soruDurum.setVisibility(View.VISIBLE);
                            onaylaButton.setVisibility(View.GONE);
                            reddetButton.setVisibility(View.GONE);
                        }

                    } else {
                        soruDurum.setVisibility(View.VISIBLE);
                        onaylaButton.setVisibility(View.GONE);
                        reddetButton.setVisibility(View.GONE);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }


    private String gecenZamanHesapla(String zaman) {

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy.MM.dd");
        String TIME = myFormat.format(new Date());
        String zamanFark = "";
        String[] zaman2 = zaman.split("/");
        long fark2 = 0;

        try {
            Date tarih1 = myFormat.parse(zaman2[1]);
            Date tarih2 = myFormat.parse(TIME);

            long fark = tarih2.getTime() - tarih1.getTime();
            fark2 = TimeUnit.DAYS.convert(fark, TimeUnit.MILLISECONDS);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (fark2 > 0) {
            if (fark2 < 7) {
                zamanFark = fark2 + " gün önce";
            } else if (fark2 < 30) {
                zamanFark = fark2 / 7 + " hafta önce";
            } else if (fark2 < 365) {
                zamanFark = fark2 / 30 + " ay önce";
            } else {
                zamanFark = fark2 / 365 + " yıl önce";
            }

        } else {

            SimpleDateFormat saat = new SimpleDateFormat("HH:mm");
            String time = saat.format(new Date());
            String[] zaman3 = zaman2[0].split("\n");
            String[] zaman4 = zaman3[1].split(":");
            String[] zaman5 = time.split(":");

            int[] simdikiZaman = {Integer.parseInt(zaman5[0]), Integer.parseInt(zaman5[1])};
            int[] gelenZaman = {Integer.parseInt(zaman4[0]), Integer.parseInt(zaman4[1])};


            if (time.matches(zaman3[1])) {
                zamanFark = "şimdi";
            } else if (simdikiZaman[0] == gelenZaman[0]) {

                if (simdikiZaman[1] > gelenZaman[1])
                    zamanFark = simdikiZaman[1] - gelenZaman[1] + " dakika önce";
                else
                    zamanFark = 60 - gelenZaman[1] + simdikiZaman[1] + " dakika önce";

            } else if (simdikiZaman[0] > gelenZaman[0]) {
                if (simdikiZaman[0] - gelenZaman[0] == 1) {
                    if (gelenZaman[1] > simdikiZaman[1])
                        zamanFark = 60 - gelenZaman[1] + simdikiZaman[1] + " dakika önce";
                    else
                        zamanFark = simdikiZaman[0] - gelenZaman[0] + " saat önce";


                } else
                    zamanFark = simdikiZaman[0] - gelenZaman[0] + " saat önce";
            }

        }

        return zamanFark;

    }

    private void soruBul(boolean islem, String key) {


        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        try {
            firebaseDatabase = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("gelenSorular");

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot gelenSoruDataSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot gelenSnapshot : gelenSoruDataSnapshot.getChildren()) {
                            if (gelenSnapshot.child("soruKey").getValue().toString().matches(key)) {
                                if (islem) {
                                    gelenSnapshot.getRef().child("soruDurum").setValue("onaylandı");
                                    soruOnayla(gelenSnapshot);
                                } else {
                                    gelenSnapshot.getRef().child("soruDurum").setValue("reddedildi");
                                    // gelenSnapshot.getRef().removeValue();
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void soruOnayla(DataSnapshot dataSnapshot) {

        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;
        String soruKat = "";
        Soru yeniSoru = new Soru(dataSnapshot.child("soru").getValue().toString().trim(), dataSnapshot.child("cevap").getValue().toString().trim().toUpperCase(), Integer.parseInt(dataSnapshot.child("puan").getValue().toString().trim().toUpperCase()), dataSnapshot.child("kategori").getValue().toString().trim().toUpperCase(), dataSnapshot.child("userName").getValue().toString().trim(), dataSnapshot.child("zaman").getValue().toString().trim(), dataSnapshot.child("soruKey").getValue().toString().trim(), dataSnapshot.child("soruDurum").getValue().toString().trim(),dataSnapshot.child("notif").getValue().toString().trim());
        switch (Integer.parseInt(dataSnapshot.child("puan").getValue().toString().trim())) {

            case 400:
                soruKat = "soru4";
                break;
            case 500:
                soruKat = "soru5";
                break;
            case 600:
                soruKat = "soru6";
                break;
            case 700:
                soruKat = "soru7";
                break;
            case 800:
                soruKat = "soru8";
                break;
            case 900:
                soruKat = "soru9";
                break;
            case 1000:
                soruKat = "soru10";
                break;

        }


        try {

            firebaseDatabase = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("sorular/" + soruKat);

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int soruSize = 0;
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        soruSize++;
                    }

                    databaseReference.child(String.valueOf(soruSize)).setValue(yeniSoru);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
