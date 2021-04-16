package com.word.game.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.word.game.Activitys.SoruGonder;
import com.word.game.R;
import com.word.game.Models.Soru;
import com.word.game.Services.SoruNotifService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class SoruGonderFragment extends Fragment {

    Spinner spinner;
    String[] kategoriHarf;
    String ID = "";
    EditText cevapAlEdit, soruAlEdit;
    Button gonderButton;
    TextView soruUyari, cevapUyari, cevapKarakterUyari;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    int soruPuan;
    String soruKategori;
    Dialog infoSoruGonderDialog;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_soru_gonder, null);
        spinner = view.findViewById(R.id.spinner);
        gonderButton = view.findViewById(R.id.gonderButton);
        cevapAlEdit = view.findViewById(R.id.cevapAlEdit);
        soruAlEdit = view.findViewById(R.id.soruAlEdit);


        try {
            firebaseDatabase = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
            databaseReference = firebaseDatabase.getReference("gelenSorular");
        } catch (Exception e) {
            e.printStackTrace();
        }

        soruUyari = view.findViewById(R.id.soruUyari);
        cevapUyari = view.findViewById(R.id.cevapUyari);
        cevapKarakterUyari = view.findViewById(R.id.cevapKarakterUyari);


        kategoriHarf = new String[]{"4 HARFLİ CEVAPLAR", "5 HARFLİ CEVAPLAR", "6 HARFLİ CEVAPLAR", "7 HARFLİ CEVAPLAR", "8 HARFLİ CEVAPLAR", "9 HARFLİ CEVAPLAR", "10 HARFLİ CEVAPLAR"};
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, kategoriHarf);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cevapAlEdit.setText("");

                switch (position) {
                    case 0:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
                        soruPuan = 400;
                        soruKategori = "4 HARFLİ SORULAR";
                        break;
                    case 1:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
                        soruPuan = 500;
                        soruKategori = "5 HARFLİ SORULAR";
                        break;
                    case 2:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
                        soruPuan = 600;
                        soruKategori = "6 HARFLİ SORULAR";
                        break;
                    case 3:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(7)});
                        soruPuan = 700;
                        soruKategori = "7 HARFLİ SORULAR";
                        break;
                    case 4:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(8)});
                        soruPuan = 800;
                        soruKategori = "8 HARFLİ SORULAR";
                        break;
                    case 5:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(9)});
                        soruPuan = 900;
                        soruKategori = "9 HARFLİ SORULAR";
                        break;
                    case 6:
                        cevapAlEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        soruPuan = 1000;
                        soruKategori = "10 HARFLİ SORULAR";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        gonderButtonIslem();

        return view;

    }

    private void gonderButtonIslem() {

        gonderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cevapAlEdit.getText().toString().trim().matches("")) {
                    cevapUyari.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cevapUyari.setVisibility(View.GONE);
                        }
                    }, 1800);

                } else if (cevapAlEdit.getText().length() * 100 != soruPuan) {
                    cevapKarakterUyari.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            cevapKarakterUyari.setVisibility(View.GONE);
                        }
                    }, 1800);
                }

                if (soruAlEdit.getText().toString().trim().matches("")) {
                    soruUyari.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            soruUyari.setVisibility(View.GONE);
                        }
                    }, 1800);

                }


                if (!cevapAlEdit.getText().toString().trim().matches("") && !soruAlEdit.getText().toString().trim().matches("") && cevapAlEdit.getText().length() * 100 == soruPuan) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd/HH:mm\nEEE");
                    String TIME = sdf.format(new Date());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        getContext().startForegroundService(new Intent(getContext(), SoruNotifService.class));
                    } else {
                        getContext().startService(new Intent(getContext(), SoruNotifService.class));

                    }
                    Soru soru = new Soru(soruAlEdit.getText().toString().trim(), cevapAlEdit.getText().toString().trim().toUpperCase(), soruPuan, soruKategori.toUpperCase(), SoruGonder.sharedPreferences.getString("userName", ""), TIME, soruCreateID(), "beklemede", "false");
                    databaseReference.child(SoruGonder.sharedPreferences.getString("myId", "") + "/" + ID).setValue(soru);
                    cevapAlEdit.setText("");
                    soruAlEdit.setText("");
                    gosterSoruİnfoDialog();
                }

            }
        });

    }


    private String soruCreateID() {

        ID = "";
        String[] harfDizi = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "V", "Y", "W", "X", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "v", "y", "w", "x", "z"};
        for (int i = 0; i < 16; i++) {

            ID += harfDizi[new Random().nextInt(harfDizi.length)] + new Random().nextInt(10);

        }
        return ID;

    }

    private void gosterSoruİnfoDialog() {
        infoSoruGonderDialog = new Dialog(getContext());
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(infoSoruGonderDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoSoruGonderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoSoruGonderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        infoSoruGonderDialog.setCancelable(false);
        infoSoruGonderDialog.setContentView(R.layout.soru_gonderildi_design);
        infoSoruGonderDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                infoSoruGonderDialog.dismiss();
            }
        }, 1000);

    }

}
