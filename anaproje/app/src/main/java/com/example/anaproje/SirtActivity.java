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

public class SirtActivity extends AppCompatActivity implements View.OnClickListener{

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
        setContentView(R.layout.activity_sirt);

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
                    Toast.makeText(SirtActivity.this, "Bitti", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(SirtActivity.this,TebriklerActivity.class);
                    startActivity(intent);
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==50000)
                {
                    Toast.makeText(SirtActivity.this, "Dinlen", Toast.LENGTH_SHORT).show();
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==0 && (SystemClock.elapsedRealtime()-chronometer.getBase())!=0)
                {
                    Toast.makeText(SirtActivity.this, "Başla", Toast.LENGTH_SHORT).show();
                    switch(tekrar%6)
                    {
                        case 0: {
                            isim1.setText("Wide Grip Pull Up");
                            isim2.setText("Sırt kaslarını bu hareket ile geliştirmek için öncelikle asılacak olduğunuz barın yüksekliği en az bir kol mesafesinde olması"+
                                    " gerekmektedir. Barı tutuğunuzda elleriniz bir omuz genişliğinde ya da daha açık olmalıdır. Sonrasında ise kollarınızla ve sırtınız "+
                                    "ile çekmelisiniz. Burada nefes kontrolü çok önemlidir. Zamanla daha rahat yapmaya başlayacağınız bu egzersiz ile sırtınızın orta"+
                                    " kısmında bulunan kas önce kuvvetlenir sonra da gelişmeye başlar.");
                        }
                        case 1: {
                            isim1.setText("Reverse Cable Croosover");
                            isim2.setText("Adından anlaşılacağı gibi kabloların ters, yani çapraz şekilde kullanılmasıyla yapılan bir harekettir. Sağdan gelen kablo sol "+
                                    "ele, soldan gelen kablo sağ ele alınır. Başlama pozisyonunda vücut dik, eller yukarıda önde, yumruklar birleşme noktasında olmalıdır."+
                                    " Kolların iki yana paralel olarak açılması ile yapılır. Ayakta ve oturarak yapılabilen bu hareket sırt kaslarını çalıştırır. Yapılması"+
                                    " zor olsa da başta kürek, arka omuz ve trapez kaslarını iyi çalıştıran hareketlerdendir.");
                        }
                        case 2: {
                            isim1.setText("Barfiks");
                            isim2.setText("Hareketin yapılışında eller omuz genişliğinde açılmalı ve kartal tutuşu adı verilen avuç içleri ileriye bakacak şekilde " +
                                    "kavranmalıdır. Hareket bu şekilde yapıldığı zaman sırt kaslarını etkili çalıştırır. Enseye doğru yapılan şekli daha zordur ve sırt" +
                                    " kaslarını daha çok zorlar. Barfiks, kişinin kendi vücut ağırlığı ile yapılıyor olsa da bir çubuk ya da alet gerektirdiği için aletli" +
                                    " hareketler arasında değerlendirmeye alındığını hatırlatalım.");
                        }
                        case 3: {
                            isim1.setText("Dambıl Row");
                            isim2.setText("Hareketin yapılışı için sağ kol ile başlandığını düşünürsek, düz şekilde ayarladığınız sehpaya sol dizinizi ve elinizi yaslayıp" +
                                    " sağ elinizde dambıl ile sağ ayağınız yerde olacak şekilde harekete başlıyorsunuz. Dambıl karın boşluğuna kadar çekilerek dirsek " +
                                    "tamamen kapatılır ve sırt kasları sıkıştırılır. Avuç içi vücuda bakacak şekilde olmasına ve ağırlığın dikey hareketi bozmadan " +
                                    "yapılmasına dikkat edilmelidir.");
                        }
                        case 4:
                        {
                            isim1.setText("Yerde Yüzme");
                            isim2.setText(" Bu hareketi yaparken hiçbir şekilde kemik ya da bağlarınızı zorlayarak yapmayın. Sağ kol ve sol bacak destek olarak yerde "+
                                    "kalırken sol kol ve sağ bacak yukarı kaldırılır. Kaldırılan kol ve bacak indirildikten sonra diğerleri kaldırılır.");
                        }
                        case 5:
                        {
                            isim1.setText("Bird Dog");
                            isim2.setText(" Hareketin yapılışına yere diz ve eller üzerinde konum alınarak başlanır. Sağ ayak ile sol el, sol ayak ile sağ el çaprazlama "+
                                    "hareket ettirilir. Sol kol dirsekten kırılmadan karşıya doğru uzatılırken, sağ bacak düz olacak şekilde arkaya doğru uzatılır. Bu"+
                                    " pozisyonda denge sağlamak için ayrıca karın kasları ve diğer ufak kaslar da çalışmaktadır. ");
                        }
                    }
                    tekrar++;
                    Toast.makeText(SirtActivity.this, "tekrar sayısı:"+tekrar, Toast.LENGTH_SHORT).show();
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