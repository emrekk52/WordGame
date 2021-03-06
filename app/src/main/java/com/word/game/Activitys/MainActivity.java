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
        buton??slemleri();

        if (sharedPreferences.getString("userName", "").matches("")) {
            gosterUser??nfoDialog();
        }

    }

    private void buton??slemleri() {

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
                        Toast.makeText(getApplicationContext(), "T??m enerji t??kendi!", Toast.LENGTH_SHORT).show();

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

        sorular4.add(new Soru("???Ulvi??? s??z??n??n T??rk??e k??kenli e?? anlaml??s??", "Y??CE", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Deride sinirler boyunca beliren, ???Gece yan????????? olarak da bilinen hastal??k", "ZONA", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Ait olunan fakat uzak kal??p ??zlenen yer", "s??la", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bak??ma ve bar??nmaya muhta?? bir grup insan??n oturdu??u, yeti??tirildi??i veya bak??ld?????? kurum", "yurt", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));

        sorular4.add(new Soru("Olmas?? ba??ka durumlar??n ger??ekle??mesini gerektiren ??ey, ko??ul", "??art", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bir ??ey i??ilirken al??nan tat", "????im", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Belli bir unvan kazanmas?? beklenen m??stakbel ki??i, namzet", "Aday", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("D???? uyaranlara kar???? bilincin b??t??n??yle veya bir b??l??m??n??n yitti??i, her t??r etkinli??in b??y??k ??l????de azald?????? dinlenme durumu", "Uyku", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("I????????n dalga boylar??na, renklerine g??re meydana getirdi??i s??ra", "Tayf", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("??abuk ve kolay kavrayan, zeyrek", "Zeki", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));

        sorular4.add(new Soru("Temel niteli??i bir olan dil, hayvan veya bitki toplulu??u", "Aile", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("???Anlams??z ve faydas??z yere??? anlam??nda bir s??z", "Bo??a", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Baya???? kesirlerde pay ile payda aras??na konulan yatay ??izginin okunu??u", "B??l??", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Halk a??z??nda, ???Alev, yal??m???", "Alaz", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));

        sorular4.add(new Soru("???Filiz, s??rg??n??? anlam??na gelen bir kad??n ad??", "Ajda", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Ad??n?? bir dondurma t??r??ne de vermi?? olan Avrupa ??ehri", "Roma", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Ruhi ya??am ve bedenle uyumlu olmay?? ama??layan k??lt??rfizik ve felsefe sistemi", "Yoga", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Argoda, ???Vurgun, kazan??, kar???", "Voli", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Sigorta kurulu??lar??na ba??l?? olanlar??n ??demek zorunda olduklar?? ??cret", "Prim", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("???Yukar??, ??st??? anlamlar??nda eski bir s??zc??k", "Fevk", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Bir ta????ttan yararlanmayan trafik ????esi", "Yaya", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Argoda, ???Tehdit i??eren sald??rgan tav??r, s??z veya eylem???", "Atar", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("???Umar??? s??z??n??n daha yayg??n kullan??lan Fars??a k??kenli e?? anlaml??s??", "??are", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Etraf?? helis e??risi bi??iminde girintiler ve ????k??nt??larla dolanm???? ??ivi", "Vida", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Mecazen, ???G??n??l ve cesaret k??r??c??, huysuz, sert???", "Ters", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Neden oldu??u rahats??z edici ka????nt?? nedeniyle ???Sevimsiz insanlara??? da yak????t??r??lan bir deri hastal??????\n", "Uyuz", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Y??ksekli??i 0-500 M. aras??nda de??i??en, do??al co??rafi olu??um", "Tepe", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("?????uh??? s??zc??????n??n Bat?? k??kenli e?? ve yak??n anlaml?? kar????l??????", "Vamp", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("T??rkiye???nin Do??u Anadolu B??lgesi???nde do??up Hazar Denizi???ne d??k??len Nehir", "Aras", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Modern mi??fer", "Kask", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Etraf?? iplikle ??r??len d????me yar??????", "??lik", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));
        sorular4.add(new Soru("Nargilenin ucuna tak??lan, t??t??n konulan tuva", "L??le", 400, "4 HARFL?? SORULAR", "", "", "", "","false"));


        sorular5.add(new Soru("Haber toplama, yayma ve ??yelerine da????tma i??iyle u??ra??an kurulu??", "Ajans", 500, "5 HARFL?? SORULAR", "", "", "", "","false"));
        sorular5.add(new Soru("Geli??en teknolojiyle bir??ok alanda insan??n yerini alabilece??i d??????n??len elektromekanik ara??", "Robot", 500, "5 HARFL?? SORULAR", "", "", "", "","false"));
        sorular5.add(new Soru("Akdeniz???e ??zg??, Deniz k??y??s??nda bile yeti??ebilen bir a??a??", "Ilg??n", 500, "5 HARFL?? SORULAR", "", "", "", "","false"));
        sorular5.add(new Soru("Bir desimetrek??p hacmindeki ??l???? birimi", "Litre", 500, "5 HARFL?? SORULAR", "", "", "", "","false"));


        sorular6.add(new Soru("G??nl?? de??i??ken, a??k?? vefas??z olan", "Hercai", 600, "6 HARFL?? SORULAR", "", "", "", "","false"));
        sorular6.add(new Soru("??erit bi??imde metal veya plastik levhalardan yap??lm???? bir t??r perde", "Jaluzi", 600, "6 HARFL?? SORULAR", "", "", "", "","false"));
        sorular6.add(new Soru("Yak??nma ve hafifseme yoluyla ?????imdiki devir??? anlam??nda kullan??lan s??zc??k", "Zamane", 600, "6 HARFL?? SORULAR", "", "", "", "","false"));
        sorular6.add(new Soru("Argoda, ???Aldatarak paras??n?? ??ekmek???", "Sa??mak", 600, "6 HARFL?? SORULAR", "", "", "", "","false"));


        sorular7.add(new Soru("Tatl?? ve hamur i??i d??kkan??", "Pastane", 700, "7 HARFL?? SORULAR", "", "", "", "","false"));
        sorular7.add(new Soru("Argoda, ???G??nl?? olup olmad??????n?? anlamak i??in manal?? tav??rlarda bulunmak, kur yapmak???", "????atmak", 700, "7 HARFL?? SORULAR", "", "", "", "","false"));
        sorular7.add(new Soru("???Amac??nda, yolunda??? anlam??nda bir zarf", "U??runda", 700, "7 HARFL?? SORULAR", "", "", "", "","false"));
        sorular7.add(new Soru("Evliya mucizesi", "Keramet", 700, "7 HARFL?? SORULAR", "", "", "", "","false"));


        sorular8.add(new Soru("Bir baltaya sap olmadan, ba????bo?? ya??ama hali", "Haytal??k", 800, "8 HARFL?? SORULAR", "", "", "", "","false"));
        sorular8.add(new Soru("???Olmak??? fiili ile beraber kullan??ld??????nda ?????lmek??? anlam??na gelen kelime", "Rahmetli", 800, "8 HARFL?? SORULAR", "", "", "", "","false"));
        sorular8.add(new Soru("Alacaklar??n toplanmas??", "Tahsilat", 800, "8 HARFL?? SORULAR", "", "", "", "","false"));
        sorular8.add(new Soru("???Ac??nmak, yaz??klanmak, teess??f etmek??? anlamlar??ndaki s??z", "Yerinmek", 800, "8 HARFL?? SORULAR", "", "", "", "","false"));


        sorular9.add(new Soru("S??cak suya so??uk veya so??u??a s??cak su katma", "Il????t??rma", 900, "9 HARFL?? SORULAR", "", "", "", "","false"));
        sorular9.add(new Soru("??zellikle g??zler i??in, ???Yuvas??ndan d????ar??ya do??ru f??rlamak??? anlam??nda bir s??z", "P??rtlemek", 900, "9 HARFL?? SORULAR", "", "", "", "","false"));
        sorular9.add(new Soru("???Bu i??le ilgilenmem, buna kar????mam??? anlam??nda bir tabir", "Nemelaz??m", 900, "9 HARFL?? SORULAR", "", "", "", "","false"));
        sorular9.add(new Soru("Genellikle ev i??lerinde ??al????an, i??verenlerin isteklerini yerine getiren emek??i", "Hizmetkar", 900, "9 HARFL?? SORULAR", "", "", "", "","false"));


        sorular10.add(new Soru("???T??m hava ko??ullar??nda, yaz k???? kullan??labilen??? gere??lere y??nelik bir tabir", "D??rtmevsim", 1000, "10 HARFL?? SORULAR", "", "", "", "","false"));
        sorular10.add(new Soru("???Ula??mak, varmak??? anlam??ndaki eski bir tabir", "Vas??lolmak", 1000, "10 HARFL?? SORULAR", "", "", "", "","false"));
        sorular10.add(new Soru("Mecazi anlamda, ???Birinin akl??n?? ba????na toplamas??n?? sa??lamak???", "Uyand??rmak", 1000, "10 HARFL?? SORULAR", "", "", "", "","false"));
        sorular10.add(new Soru("Kefeni y??rtmay?? adet haline getirmi?? ki??iler??? i??in kullan??lan bir s??z", "Dokuzcanl??", 1000, "10 HARFL?? SORULAR", "", "", "", "","false"));


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


    private void gosterUser??nfoDialog() {


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