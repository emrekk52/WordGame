package com.word.game.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.word.game.Activitys.SkorActivity;
import com.word.game.R;
import com.word.game.Models.Skor;

import java.util.ArrayList;

public class SkorAdapter extends RecyclerView.Adapter<SkorAdapter.SkorViewHolder> {

    private ArrayList<Skor> skors;
    private int[] avatars = {R.drawable.avatar1, R.drawable.avatar2, R.drawable.avatar3, R.drawable.avatar4, R.drawable.avatar5, R.drawable.avatar6, R.drawable.avatar7, R.drawable.avatar8};
    private int sayac = 0;
    public boolean isShimmer = true;
    int shimmerNumber;


    public SkorAdapter(ArrayList<Skor> skors) {
        this.skors = skors;
        shimmerNumber = skors.size();
    }

    @NonNull
    @Override
    public SkorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SkorViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.score_layout_design,
                        parent,
                        false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SkorViewHolder holder, int position) {
        if (isShimmer)
            holder.shimmerFrameLayout.startShimmer();
        else {
            holder.shimmerFrameLayout.stopShimmer();
            holder.shimmerFrameLayout.setShimmer(null);
            holder.bindSkor(skors.get(position), position);
        }

    }

    @Override
    public int getItemCount() {
        return isShimmer ? shimmerNumber : skors.size();
    }

    class SkorViewHolder extends RecyclerView.ViewHolder {

        ShimmerFrameLayout shimmerFrameLayout;
        TextView userName, userSkor, userZaman, userSira, sonSkor;
        ImageView userImg;

        SkorViewHolder(@NonNull View itemView) {
            super(itemView);

            shimmerFrameLayout = itemView.findViewById(R.id.shimmer);
            userName = itemView.findViewById(R.id.userName);
            userSkor = itemView.findViewById(R.id.userSkor);
            sonSkor = itemView.findViewById(R.id.sonSkor);
            userZaman = itemView.findViewById(R.id.userZaman);
            userSira = itemView.findViewById(R.id.siralamaSayisi);
            userImg = itemView.findViewById(R.id.userImg);

        }

        void bindSkor(Skor skor, int position) {

            if (skor.getUserID().trim().matches(SkorActivity.sharedPreferences.getString("myId", ""))) {
                shimmerFrameLayout.setBackgroundColor(Color.parseColor("#F6F6F6"));
            }

            userSkor.setText(skorDuzenle(skor.getSkor()));
            userZaman.setText(skor.getZaman());
            userSira.setText(String.valueOf(position + 4));
            userName.setText(skor.getUserName());
            userImg.setImageResource(avatars[sayac]);
            sonSkor.setText(skorDuzenle(skor.getSonSkor()));
            if (sayac == 7) {
                sayac = 0;
            } else
                sayac++;
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


}
