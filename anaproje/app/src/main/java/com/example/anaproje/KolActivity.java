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

public class KolActivity extends AppCompatActivity implements View.OnClickListener{

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
        setContentView(R.layout.activity_kol);

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
                    Toast.makeText(KolActivity.this, "Bitti", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(KolActivity.this,TebriklerActivity.class);
                    startActivity(intent);
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==50000)
                {
                    Toast.makeText(KolActivity.this, "Dinlen", Toast.LENGTH_SHORT).show();
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==0 && (SystemClock.elapsedRealtime()-chronometer.getBase())!=0)
                {
                    Toast.makeText(KolActivity.this, "Başla", Toast.LENGTH_SHORT).show();
                    switch(tekrar%6)
                    {
                        case 0: {
                            isim1.setText("Vücut Ağırlığı ile Dips");
                            isim2.setText("Bir yatak, bir sandalye veya bir bench sehpasına sırtın dönük vaziyette ayakta durun ve her iki elinizi omuz genişliğinde "+
                                    "kullandığınız ekipmanın üzerine koyun. Bacaklarınızı öne doğru uzatın. Ön kolunuz 90 derecelik bir açı oluşturuncaya kadar, "+
                                    "dirseklere doğru esneterek vücudunuzu yavaşça indirin.");
                        }
                        case 1: {
                            isim1.setText("Yengeç Yürüyüşü ");
                            isim2.setText("Arkanıza yaslanmış vaziyette ve elleriniz yerde olacak şekilde yere oturun, sonrasında bacaklarınızı önünüzde olacak şekilde"+
                                    " bükün. Kalçalarınızı yukarı kaldırın, böylece sadece elleriniz ve ayaklarınız yerde olacaktır. Sonrasında ellerinizi ve"+
                                    " ayaklarınızı kullanarak yürümeye başlayın.");
                        }
                        case 2: {
                            isim1.setText("İmkansız Yukarı Doğru Pres");
                            isim2.setText("Yüz üstü karnınızın üzerine yatın ve avuç içlerinizle birlikte kollarınızı başınızın üstüne doğru kaldırın. Ayak parmaklarınız" +
                                    " ve parmak uçlarınızdan, vücudunuzu yavaşça yere dönmeden önce olabildiğince yükseğe kaldırın.");
                        }
                        case 3: {
                            isim1.setText("Vücut Yukarı");
                            isim2.setText("Omuz genişliği ile ön kolları birbirinden ayıran bir “plank” pozisyonunda başlayın. Avuç içlerinizi yere koyun ve vücudunuzu "+
                                    "yukarı doğru uzatın, gövdenin baştan başa düz kalmasını sağlayın.");
                        }
                        case 4:
                        {
                            isim1.setText("Amuda Kalkarak Duvar Yürüyüşü");
                            isim2.setText("Ayaklarınızı ters olarak duvara koyarak kendinizi amuda kalkma pozisyonuna getirin. Ellerinizi ileriye doğru hareket ettirin ve"+
                                    " dibe ulaşıncaya kadar duvardan aşağı doğru yürüyün.");
                        }
                        case 5:
                        {
                            isim1.setText("45 Derece Eğimli Şınav");
                            isim2.setText("Ayaklarınız yere koyulmuş vaziyette, ellerinizi bir yatak ya da bir sandalyeye omuz genişliğinden biraz daha geniş şekilde"+
                                    " yerleştirin. Kollarınızı bükün ve göğsünüz sehpaya dokunana kadar vücudunuzu indirin ve sonrasında vücudunuzu başlangıç pozisyonuna "+
                                    "doğru geri kaldırın.");
                        }
                    }
                    tekrar++;
                    Toast.makeText(KolActivity.this, "tekrar sayısı:"+tekrar, Toast.LENGTH_SHORT).show();
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