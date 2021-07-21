package com.example.anaproje;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class KarinActivity extends AppCompatActivity implements View.OnClickListener{

    private Button Start,Stop,Restart;
    int sure;
    int tekrar=0;
    private Chronometer chronometer;
    private TextView isim1,isim2;
    private long pauseOffset;
    private boolean running;
    double toplamZaman,oncekiZaman,kalori;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String eMail,userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_karin);

        Start=findViewById(R.id.Start);
        Start.setOnClickListener(this);
        Restart=findViewById(R.id.Restart);
        Restart.setOnClickListener(this);
        Stop=findViewById(R.id.Stop);
        Stop.setOnClickListener(this);

        Intent verial=getIntent();
        sure=verial.getIntExtra("Sure",0);
        sure=60000*sure;
        Toast.makeText(this, ""+sure, Toast.LENGTH_SHORT).show();

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Kullanıcı İsimleri").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                kalori = value.getDouble("Kalori");
                toplamZaman = value.getDouble("Toplam Süre");
                oncekiZaman = value.getDouble("Önceki Süre");
                kalori += (38.8*sure)/60000;
                toplamZaman +=sure;
            }
        });

        Map<String,Object> edited = new HashMap<>();
        edited.put("Kalori",kalori);
        edited.put("Toplam Süre",toplamZaman);
        edited.put("Önceki Süre",oncekiZaman);
        documentReference.update(edited);

        isim1=findViewById(R.id.isim1);
        isim2=findViewById(R.id.isim2);

        chronometer=findViewById(R.id.choronometer);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setFormat("Sure: %s");

        isim1.setText("Isınma Hareketi");
        isim2.setText("Ayaklarınızı omuz genişliğinde açmış ve kollarınız yanınızda olacak şekilde ayakta durun." +
                "Kollarınızı yavaşça öne doğru dairesel hareketlerle sallayın. Bunu yaparken, omuzlarını ısınıyor hissetmelisiniz." +
                "Sekiz tekrar boyunca dairesel harekete devam edin");

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if((SystemClock.elapsedRealtime()-chronometer.getBase())>=sure)
                {
                    chronometer.setBase(SystemClock.elapsedRealtime());
                    Toast.makeText(KarinActivity.this, "Bitti", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(KarinActivity.this,TebriklerActivity.class);
                    startActivity(intent);
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==50000)
                {
                    Toast.makeText(KarinActivity.this, "Dinlen", Toast.LENGTH_SHORT).show();
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==0 && (SystemClock.elapsedRealtime()-chronometer.getBase())!=0)
                {
                    Toast.makeText(KarinActivity.this, "Başla", Toast.LENGTH_SHORT).show();
                    switch(tekrar%6)
                    {
                        case 0: {
                            isim1.setText("Plank");
                            isim2.setText("Dirsekler 90 derece olacak şekilde yerde vücut yere paralel ve eller yumruk olacak şekilde en az 30 saniye boyunca bu pozisyonda"+
                                    " kalarak yapılır. Zamanla bu pozisyonda duruş süreniz arttıkça; evde keyifle yapabileceğiniz bir egzersiz haline gelecek.");
                        }
                        case 1: {
                            isim1.setText("Mekik");
                            isim2.setText("Sırt üstü yat, dizlerin kırılı ve ayak tabanların yerde olsun. Dizlerin kalça hizasında açık olmalı. Ellerini başının arkasına"+
                                    " yerleştir ama parmaklarını birbirine geçirme. Başparmakların kulaklarının arkasına gelsin. Dirseklerini dışa doğru açık tut, hafifçe "+
                                    "içe dönük olsunlar. Çeneni hafif yukarı kaldır, göğsün ve çenen arasında boşluk olsun. Karnını içeri çek. Başın, boynun ve kürek"+
                                    " kemiklerin aynı anda yerden kalksın. Tepe noktadayken 1–2 saniye bekle. Sonrasında aşağı yavaşça inerek hareketi sonlandırın.");
                        }
                        case 2: {
                            isim1.setText("Leg Raise");
                            isim2.setText("Halı ya da egzersiz matı üzerine sırt üstü yatın. Ellerinizle sabit bir objeye tutunun ve bacaklarınızı bitiştirerek aynı "+
                                    "anda yukarı doğru kaldırın, biraz daha yavaş bir şekilde aşağı indirin.");
                        }
                        case 3: {
                            isim1.setText("Flutter Kicks");
                            isim2.setText("Sırt üstü yatın ve her iki elinizi, dirseklerinize kadar kalçalarınızın altına alın. Düz duran bacaklarınızı sırayla yerden"+
                                    " bir karışlık mesafe boyunca tekme atar gibi kaldırın ve indirin.");
                        }
                        case 4:
                        {
                            isim1.setText("Swiss ball crunch");
                            isim2.setText("Egzersiz veya pilates topuyla gerçekleştirilen ve karın kaslarının büyük kısmını çalıştıran bu hareket için kalçanıza kadar "+
                                    "olan üst gövdenizle topun üzerine uzanın. Dizlerinizi sandalyede oturuyormuşsunuz gibi kırın ve yere sağlam şekilde basın. Ellerinizi,"+
                                    " omuzlarınızın üzerine çapraz olacak şekilde yerleştirin ve uzandığınız yerden kalkın.");
                        }
                        case 5:
                        {
                            isim1.setText("Mountain climbers");
                            isim2.setText("Üst gövdenizi ve karın kaslarınızı çalıştıran etkili bir egzersizdir. Bu pozisyon için şınav pozisyonuna geçin. Bir dağa "+
                                    "tırmanıyormuşçasına ayaklarınızı sırasıyla öne çekin ve arkaya itin.");
                        }
                    }
                    tekrar++;
                    Toast.makeText(KarinActivity.this, "tekrar sayısı:"+tekrar, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.Start:
            {
                if(!running)
                {
                    chronometer.setBase(SystemClock.elapsedRealtime()-pauseOffset);
                    chronometer.start();
                    Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();
                    running=true;
                }
                break;
            }
            case R.id.Stop:
            {
                if(running)
                {
                    chronometer.stop();
                    pauseOffset= SystemClock.elapsedRealtime()-chronometer.getBase();
                    Toast.makeText(this, "stop", Toast.LENGTH_SHORT).show();
                    running=false;
                }
                break;

            }
            case R.id.Restart:
            {
                chronometer.setBase(SystemClock.elapsedRealtime());
                Toast.makeText(this, "restart", Toast.LENGTH_SHORT).show();
                pauseOffset=0;
                break;
            }
        }
    }
}