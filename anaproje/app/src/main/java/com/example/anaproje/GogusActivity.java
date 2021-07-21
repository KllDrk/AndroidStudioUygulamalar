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

public class GogusActivity extends AppCompatActivity implements View.OnClickListener{

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
        setContentView(R.layout.activity_gogus);

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
                    Toast.makeText(GogusActivity.this, "Bitti", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(GogusActivity.this,TebriklerActivity.class);
                    startActivity(intent);
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==50000)
                {
                    Toast.makeText(GogusActivity.this, "Dinlen", Toast.LENGTH_SHORT).show();
                }
                else if((SystemClock.elapsedRealtime()-chronometer.getBase())%60000==0 && (SystemClock.elapsedRealtime()-chronometer.getBase())!=0)
                {
                    Toast.makeText(GogusActivity.this, "Başla", Toast.LENGTH_SHORT).show();
                    switch(tekrar%6)
                    {
                        case 0: {
                            isim1.setText("Üst Göğüs Pres");
                            isim2.setText("Özel olarak yapılmış Incline Chest Press Makine denilen veya Smith Makine denilen cihazda oturarak ve sırt dayalı olarak "+
                                    "uygulanır.  Aynı düz sehpada halterle göğüs pres uygulaması");
                        }
                        case 1: {
                            isim1.setText("Omuz Makinesinde Üst Göğüs Pres");
                            isim2.setText(" bacaklar her iki yanda kalacak şekilde yatılır. Bar , Sehpanın her iki yanında bulunan askıların üzerindedir." +
                                    "Barı, omuz genişliğinden biraz daha geniş tutuş açıklığı ile her iki el ile kavranır. Nefes alınarak köprücük kemiğinin üzerine gelecek şekilde"+
                                    " indirilir. Yine kontrollü ve yavaşça, kollar tam düzelinceye kadar, göğüs kaslarında tam gerginlik hissederek yukarıya kaldırılır." +
                                    "Ellerin tutuş açıklığı omuz genişliğinden daha dar ise, göğüs kaslarının iç kısımları, daha geniş ise, dış ve yan kısımları daha etkili çalışır.");
                        }
                        case 2: {
                            isim1.setText("Düz Sehpada Açış");
                            isim2.setText("Simple Bench denilen düz bir sehpa üzerine,  her iki ele birer dumbbell almak suretiyle uzanılır.  Dumbbell omuz yanlarında ve 1+" +
                                    "1avuç içleri bacak tarafına doğrudur. Bu konumda, nefes verilerek kollar tamamen düzelinceye ve göz seviyesine gelinceye kadar, 1+" +
                                    "1kontrollü ve yavaşça kaldırılır, ve nefes alınarak tekrar başlangıç durumuna dönülür.");
                        }
                        case 3: {
                            isim1.setText("Üst Göğüs Makinede Press");
                            isim2.setText("Oturma Platformuna oturup sırt yerleştirilir , makine açısı kendinden olduğu için ayara gerek yoktur. Nefes vererek yukarı doğru1+" +
                                    "1 dirsekler düzelinceye kadar itilir ve nefes alarak yavaşça başlangıç noktasına kadar bırakılır.");
                        }
                        case 4:
                        {
                            isim1.setText("Bench Press Machine");
                            isim2.setText("Özel olarak yapılmış Chest Press Makinası denilen bir cihazda oturarak ve sırt dayalı olarak uygulanır.  Aynı bench press uygulaması gibi çalışılır." +
                                    "Buradaki fark, hareketin makinanın müsaade ettiği sınırlar içinde kalması nedeniyle hareket riski olmayarak, sabit bir düzlem içinde,"+
                                    " egzersizi tam yapabilmeyi sağlamaktır.  Tutma barlarının aşağıda tutulması ile alt göğüs,  yukarıda tutulması ile üst göğüs ve "+
                                    "yanlardan tutulması ile de yan göğüs kasları çalıştırılabilir.");
                        }
                        case 5:
                        {
                            isim1.setText("Cable Cross Over");
                            isim2.setText("Her iki yanda bulunan kulplar, vücut hafifçe öne eğik bir konumda, ortada birleştirilmek üzere ve göğüs kasları sıkıştırılarak,"+
                                    "  nefes vermek suretiyle çekilir.  Ortada birleştirme anında birkaç saniye beklenir ve sonra, her iki kulp, nefes alınarak yavaş ve"+
                                    " kontrollü bir şekilde yine başlangıç durumuna, yani baş seviyesine kadar yanlara götürülür. ");
                        }

                    }
                    tekrar++;
                    Toast.makeText(GogusActivity.this, "tekrar sayısı:"+tekrar, Toast.LENGTH_SHORT).show();
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