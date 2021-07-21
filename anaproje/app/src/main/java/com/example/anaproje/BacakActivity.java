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

public class BacakActivity extends AppCompatActivity implements View.OnClickListener{

    private Button Start,Stop,Restart;
    int sure;
    int tekrar=0;
    private Chronometer chronometer;
    private TextView isim1,isim2;
    private long pauseOffset;
    private boolean running;
    double toplamZaman,oncekiZaman,kalori,kilo,boy;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    String eMail,userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bacak);

        Start=findViewById(R.id.Start);
        Start.setOnClickListener(this);
        Restart=findViewById(R.id.Restart);
        Restart.setOnClickListener(this);
        Stop=findViewById(R.id.Stop);
        Stop.setOnClickListener(this);

        Intent verial=getIntent();
        sure=verial.getIntExtra("Sure",0);
        sure=60000*sure;
        Toast.makeText(this, "Toplam Sure:"+sure/60000, Toast.LENGTH_SHORT).show();



        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
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
                    Toast.makeText(BacakActivity.this, "Bitti", Toast.LENGTH_SHORT).show();

                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==50000)
                {
                    Toast.makeText(BacakActivity.this, "Dinlen", Toast.LENGTH_SHORT).show();
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==0 && (SystemClock.elapsedRealtime()-chronometer.getBase())!=0)
                {
                    Toast.makeText(BacakActivity.this, "Başla", Toast.LENGTH_SHORT).show();
                    switch(tekrar%6)
                    {
                        case 0: {
                            isim1.setText("Butt Lifter & Hamstring Toner");
                            isim2.setText("Dirsekleriniz ve dizleriniz üzerinde durun. Sırtınız düz olmalı. Alt bacağınızı kalçanıza doğru yaklaştırarak sağ dizinizin "+
                                    "arasına hafif bir dambıl koyun. Şimdi ayağınızı zemine göre 90 derece olacak şekilde kaldırın ve başlangıç pozisyonuna geri dönün.");
                        }
                        case 1: {
                            isim1.setText("Squat");
                            isim2.setText("Kalça genişliğindeki ayaklarınızla ayakta durun. (Hareketi ağırlık kullanarak da yapabilirsiniz ancak formunuzu bozmayan ama "+
                                    "sizi birazda zorlayacak bir ağırlık olmasına dikkat edin.) Ellerinizi göğüs hizasında tutun. Üst vücudunuz ise mümkün olduğunca dik "+
                                    "olmalı. Dizlerinizi bükün ve bir sandalyeye oturacakmış gibi çömelirken kalçanızı geriye doğru atın. Uyluklarınızın yere 90 derece "+
                                    "olduğu noktaya kadar gitmeye çalışın");
                        }
                        case 2: {
                            isim1.setText("Side Lunge");
                            isim2.setText("Ellerinizde ağırlıklar, kalça genişliğinde açık olan bacaklarınızla ayakta durun. Sağ tarafa doğru bir adım atın ve zemine"+
                                    " doğru alçalın. Hareket boyunca üst vücudunuzu olabildiğince dik tutun. Başlangıç pozisyonuna geri dönün. Sonra diğer ayağınızla "+
                                    "hareketi tekrarlayın.");
                        }
                        case 3: {
                            isim1.setText("Wall Sit");
                            isim2.setText("Bu hareketi ellerinizde ağırlıklar tutarak da yapabilirsiniz. Bir duvara yaslanarak squat pozisyonuna geçin. Üst bacaklarınız "+
                                    "zemine paralel gelene kadar alçalın. Bu pozisyonda durun. Zorlanıyorsanız pozisyondan çıkıp birkaç saniye bacaklarınızı sallayın");
                        }
                        case 4:
                        {
                            isim1.setText("Single-Leg Deadlift");
                            isim2.setText("Bu hareketi ağırlık kullanarak veya ağırlık kullanmadan da uygulayabilirsiniz. Kalça genişliğindeki bacaklarınızla ayakta durun. "+
                                    "Dizleriniz hafifçe bükülü olsun. Ellerinizi (ya da ağırlık kullanıyorsanız ağırlıkları) uyluklarınızın önünde tutun. Belinizden hafifçe "+
                                    "bükülün ve kalçanızı geride tutarken göğsünüz ileride olsun. Sırtınız düz olmalı. Bir ayağınızı kaldırarak 90 derecelik açıya inerken"+
                                    " başınızın ve sırtınızın düz bir çizgi oluşturduğundan emin olun.");
                        }
                        case 5:
                        {
                            isim1.setText("Bridge");
                            isim2.setText("Sırt üstü zemine uzanın. Ayaklarınız kalça genişliğine açık olmalı. Dizleriniz bükülü pozisyonda, topuklarınız ise kalçanıza "+
                                    "yakın durmalı. Harekete direnç eklemek istiyorsanız alt karın bölgenizde bir ağırlık tutabilirsiniz. Topuklarınızdan güç alarak"+
                                    " kalçanızı kaldırın. 3-4 saniye bu şekilde durun ve sonra başlangıç pozisyonuna dönün. ");
                        }
                    }
                    tekrar++;
                    Toast.makeText(BacakActivity.this, "tekrar sayısı:"+tekrar, Toast.LENGTH_SHORT).show();
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