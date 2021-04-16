package com.word.game.Activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.word.game.Services.HeartService;
import com.word.game.R;
import com.word.game.Models.Soru;
import com.word.game.Services.SoruNotifService;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {


    FirebaseDatabase database;
    DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private ImageView can1, can2, can3;
    private ImageView basla_button, soruGonder_button, skor_button, cikis_button, title;
    private Animation title_animation, heart_animaton;
    private MediaPlayer click_sound;
    Dialog infoUserGonderDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Uri uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.click_sound);
        click_sound = MediaPlayer.create(getApplicationContext(), uri);
        sharedPreferences = this.getSharedPreferences(this.getPackageName(), Context.MODE_PRIVATE);

        if (sharedPreferences.getBoolean("firstEnter", true))
            uniqueCreateID();



        can1 = findViewById(R.id.heart1);
        can2 = findViewById(R.id.heart2);
        can3 = findViewById(R.id.heart3);
        basla_button = findViewById(R.id.start_button);
        skor_button = findViewById(R.id.score_button);
        cikis_button = findViewById(R.id.exit_button);
        soruGonder_button = findViewById(R.id.soruGonder_button);
        title = findViewById(R.id.title);


        title_animation = AnimationUtils.loadAnimation(this, R.anim.title_loading_animation);
        heart_animaton = AnimationUtils.loadAnimation(this, R.anim.heart_animation);

        title.startAnimation(title_animation);


        can1.startAnimation(heart_animaton);
        can2.startAnimation(heart_animaton);
        can3.startAnimation(heart_animaton);

        database = FirebaseDatabase.getInstance("https://wordgame-4e677-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("sorular");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    soruGir();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        yenileCanKayit();
        canDoldurBaslat();
        butonİslemleri();

        if (sharedPreferences.getString("userName", "").matches("")) {
            gosterUserİnfoDialog();
        }

    }

    private void butonİslemleri() {

        basla_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.setAlpha(0.7f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {

                    v.setAlpha(1f);

                    if (sharedPreferences.getInt("kayitliCanSayisi", 3) > 0) {
                        startActivity(new Intent(getApplicationContext(), GameActivity.class));
                        overridePendingTransition(R.anim.slide_right, R.anim.slide_out_right);
                    } else
                        Toast.makeText(getApplicationContext(), "Tüm enerji tükendi!", Toast.LENGTH_SHORT).show();

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
                    v.setAlpha(0.7f);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.setAlpha(1f);
                    finish();

                }

                return true;
            }
        });

        soruGonder_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.setAlpha(0.7f);

                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.setAlpha(1f);
                    startActivity(new Intent(getApplicationContext(), SoruGonder.class));

                }

                return true;
            }
        });

        skor_button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    click_sound.start();
                    v.setAlpha(0.7f);


                } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {

                    v.setAlpha(1f);
                    startActivity(new Intent(getApplicationContext(), SkorActivity.class));

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
        ArrayList<Soru> sorular10 = new ArrayList();

        sorular4.add(new Soru("“Ulvi” sözünün Türkçe kökenli eş anlamlısı", "YÜCE", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Deride sinirler boyunca beliren, “Gece yanığı” olarak da bilinen hastalık", "ZONA", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Ait olunan fakat uzak kalıp özlenen yer", "sıla", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bakıma ve barınmaya muhtaç bir grup insanın oturduğu, yetiştirildiği veya bakıldığı kurum", "yurt", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));

        sorular4.add(new Soru("Olması başka durumların gerçekleşmesini gerektiren şey, koşul", "Şart", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bir şey içilirken alınan tat", "İçim", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Belli bir unvan kazanması beklenen müstakbel kişi, namzet", "Aday", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Dış uyaranlara karşı bilincin bütünüyle veya bir bölümünün yittiği, her tür etkinliğin büyük ölçüde azaldığı dinlenme durumu", "Uyku", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Işığın dalga boylarına, renklerine göre meydana getirdiği sıra", "Tayf", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Çabuk ve kolay kavrayan, zeyrek", "Zeki", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));

        sorular4.add(new Soru("Temel niteliği bir olan dil, hayvan veya bitki topluluğu", "Aile", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("“Anlamsız ve faydasız yere” anlamında bir söz", "Boşa", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bayağı kesirlerde pay ile payda arasına konulan yatay çizginin okunuşu", "Bölü", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Halk ağzında, “Alev, yalım”", "Alaz", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));

        sorular4.add(new Soru("“Filiz, sürgün” anlamına gelen bir kadın adı", "Ajda", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Adını bir dondurma türüne de vermiş olan Avrupa şehri", "Roma", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Ruhi yaşam ve bedenle uyumlu olmayı amaçlayan kültürfizik ve felsefe sistemi", "Yoga", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Argoda, “Vurgun, kazanç, kar”", "Voli", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Sigorta kuruluşlarına bağlı olanların ödemek zorunda oldukları ücret", "Prim", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("“Yukarı, üst” anlamlarında eski bir sözcük", "Fevk", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bir taşıttan yararlanmayan trafik öğesi", "Yaya", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Argoda, “Tehdit içeren saldırgan tavır, söz veya eylem”", "Atar", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("“Umar” sözünün daha yaygın kullanılan Farsça kökenli eş anlamlısı", "Çare", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Etrafı helis eğrisi biçiminde girintiler ve çıkıntılarla dolanmış çivi", "Vida", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Mecazen, “Gönül ve cesaret kırıcı, huysuz, sert”", "Ters", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Neden olduğu rahatsız edici kaşıntı nedeniyle “Sevimsiz insanlara” da yakıştırılan bir deri hastalığı\n", "Uyuz", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Yüksekliği 0-500 M. arasında değişen, doğal coğrafi oluşum", "Tepe", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("“Şuh” sözcüğünün Batı kökenli eş ve yakın anlamlı karşılığı", "Vamp", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Türkiye’nin Doğu Anadolu Bölgesi’nde doğup Hazar Denizi’ne dökülen Nehir", "Aras", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Modern miğfer", "Kask", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Etrafı iplikle örülen düğme yarığı", "İlik", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Nargilenin ucuna takılan, tütün konulan tuva", "Lüle", 400, "4 HARFLİ SORULAR", "", "", "", "","false"));


        sorular5.add(new Soru("Haber toplama, yayma ve üyelerine dağıtma işiyle uğraşan kuruluş", "Ajans", 500, "5 HARFLİ SORULAR", "", "", "", "","false"));
        sorular5.add(new Soru("Gelişen teknolojiyle birçok alanda insanın yerini alabileceği düşünülen elektromekanik araç", "Robot", 500, "5 HARFLİ SORULAR", "", "", "", "","false"));
        sorular5.add(new Soru("Akdeniz’e özgü, Deniz kıyısında bile yetişebilen bir ağaç", "Ilgın", 500, "5 HARFLİ SORULAR", "", "", "", "","false"));
        sorular5.add(new Soru("Bir desimetreküp hacmindeki ölçü birimi", "Litre", 500, "5 HARFLİ SORULAR", "", "", "", "","false"));


        sorular6.add(new Soru("Gönlü değişken, aşkı vefasız olan", "Hercai", 600, "6 HARFLİ SORULAR", "", "", "", "","false"));
        sorular6.add(new Soru("Şerit biçimde metal veya plastik levhalardan yapılmış bir tür perde", "Jaluzi", 600, "6 HARFLİ SORULAR", "", "", "", "","false"));
        sorular6.add(new Soru("Yakınma ve hafifseme yoluyla “Şimdiki devir” anlamında kullanılan sözcük", "Zamane", 600, "6 HARFLİ SORULAR", "", "", "", "","false"));
        sorular6.add(new Soru("Argoda, “Aldatarak parasını çekmek”", "Sağmak", 600, "6 HARFLİ SORULAR", "", "", "", "","false"));


        sorular7.add(new Soru("Tatlı ve hamur işi dükkanı", "Pastane", 700, "7 HARFLİ SORULAR", "", "", "", "","false"));
        sorular7.add(new Soru("Argoda, “Gönlü olup olmadığını anlamak için manalı tavırlarda bulunmak, kur yapmak”", "İşatmak", 700, "7 HARFLİ SORULAR", "", "", "", "","false"));
        sorular7.add(new Soru("“Amacında, yolunda” anlamında bir zarf", "Uğrunda", 700, "7 HARFLİ SORULAR", "", "", "", "","false"));
        sorular7.add(new Soru("Evliya mucizesi", "Keramet", 700, "7 HARFLİ SORULAR", "", "", "", "","false"));


        sorular8.add(new Soru("Bir baltaya sap olmadan, başıboş yaşama hali", "Haytalık", 800, "8 HARFLİ SORULAR", "", "", "", "","false"));
        sorular8.add(new Soru("“Olmak” fiili ile beraber kullanıldığında “Ölmek” anlamına gelen kelime", "Rahmetli", 800, "8 HARFLİ SORULAR", "", "", "", "","false"));
        sorular8.add(new Soru("Alacakların toplanması", "Tahsilat", 800, "8 HARFLİ SORULAR", "", "", "", "","false"));
        sorular8.add(new Soru("“Acınmak, yazıklanmak, teessüf etmek” anlamlarındaki söz", "Yerinmek", 800, "8 HARFLİ SORULAR", "", "", "", "","false"));


        sorular9.add(new Soru("Sıcak suya soğuk veya soğuğa sıcak su katma", "Ilıştırma", 900, "9 HARFLİ SORULAR", "", "", "", "","false"));
        sorular9.add(new Soru("Özellikle gözler için, “Yuvasından dışarıya doğru fırlamak” anlamında bir söz", "Pörtlemek", 900, "9 HARFLİ SORULAR", "", "", "", "","false"));
        sorular9.add(new Soru("“Bu işle ilgilenmem, buna karışmam” anlamında bir tabir", "Nemelazım", 900, "9 HARFLİ SORULAR", "", "", "", "","false"));
        sorular9.add(new Soru("Genellikle ev işlerinde çalışan, işverenlerin isteklerini yerine getiren emekçi", "Hizmetkar", 900, "9 HARFLİ SORULAR", "", "", "", "","false"));


        sorular10.add(new Soru("“Tüm hava koşullarında, yaz kış kullanılabilen” gereçlere yönelik bir tabir", "Dörtmevsim", 1000, "10 HARFLİ SORULAR", "", "", "", "","false"));
        sorular10.add(new Soru("“Ulaşmak, varmak” anlamındaki eski bir tabir", "Vasılolmak", 1000, "10 HARFLİ SORULAR", "", "", "", "","false"));
        sorular10.add(new Soru("Mecazi anlamda, “Birinin aklını başına toplamasını sağlamak”", "Uyandırmak", 1000, "10 HARFLİ SORULAR", "", "", "", "","false"));
        sorular10.add(new Soru("Kefeni yırtmayı adet haline getirmiş kişiler” için kullanılan bir söz", "Dokuzcanlı", 1000, "10 HARFLİ SORULAR", "", "", "", "","false"));


        databaseReference.child("soru4").setValue(sorular4);
        databaseReference.child("soru5").setValue(sorular5);
        databaseReference.child("soru6").setValue(sorular6);
        databaseReference.child("soru7").setValue(sorular7);
        databaseReference.child("soru8").setValue(sorular8);
        databaseReference.child("soru9").setValue(sorular9);
        databaseReference.child("soru10").setValue(sorular10);


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

    private void uniqueCreateID() {

        String ID = "";
        String[] harfDizi = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "R", "S", "T", "U", "V", "Y", "W", "X", "Z", "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "r", "s", "t", "u", "v", "y", "w", "x", "z"};
        for (int i = 0; i < 8; i++) {

            ID += harfDizi[new Random().nextInt(harfDizi.length)] + new Random().nextInt(10);

        }

        sharedPreferences.edit().putString("myId", ID).apply();
        sharedPreferences.edit().putBoolean("firstEnter", false).apply();


    }


    private void gosterUserİnfoDialog() {


        infoUserGonderDialog = new Dialog(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(infoUserGonderDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        infoUserGonderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        infoUserGonderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        infoUserGonderDialog.setCancelable(false);
        infoUserGonderDialog.setContentView(R.layout.username_enter_design);
        infoUserGonderDialog.show();

        EditText username = infoUserGonderDialog.findViewById(R.id.userEdit);
        TextView isimUyari = infoUserGonderDialog.findViewById(R.id.isimUyari);

        infoUserGonderDialog.findViewById(R.id.cancelButton).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setAlpha(0.8f);

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setAlpha(1f);
                    finish();
                }
                return true;
            }
        });

        infoUserGonderDialog.findViewById(R.id.enterButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!username.getText().toString().trim().matches("")) {
                    sharedPreferences.edit().putString("userName", username.getText().toString().trim()).apply();
                    infoUserGonderDialog.dismiss();
                } else {
                    isimUyari.setVisibility(View.VISIBLE);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isimUyari.setVisibility(View.GONE);
                        }
                    }, 2000);
                }
            }
        });


    }


}