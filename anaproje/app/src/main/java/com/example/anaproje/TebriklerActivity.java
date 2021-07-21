package com.example.anaproje;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class TebriklerActivity extends AppCompatActivity implements View.OnClickListener{

    Button Profil,AnaMenu;
    TextView Sure,YakilanKalori,Isim,VKI;
    double VKIdegeri;
    double toplamZaman,oncekiZaman;
    String userID;
    String isim;
    String saglikDuurmu;
    String Aciklama;
    double sure;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tebrikler);

        Sure =              findViewById(R.id.Sure);
        YakilanKalori =     findViewById(R.id.YakilanKalori);
        Isim =              findViewById(R.id.Isim);
        VKI =               findViewById(R.id.VKI);
        Profil =            findViewById(R.id.Profil);
        AnaMenu =           findViewById(R.id.AnaMenu);

        Profil.setOnClickListener(this);
        AnaMenu.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        user = firebaseAuth.getCurrentUser();
        userID = firebaseAuth.getUid();

        DocumentReference documentReference= firebaseFirestore.collection("Kullanıcı İsimleri").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                isim = value.getString("İsim");
                toplamZaman = value.getDouble("Toplam Süre");
                oncekiZaman = value.getDouble("Önceki Süre");
                VKIdegeri = value.getDouble("VKI");
                saglikDuurmu = value.getString("Sağlık Durumu");
                Aciklama = value.getString("Açıklama");
                sure = toplamZaman-oncekiZaman;
                oncekiZaman = toplamZaman;
                Sure.setText("Sure:"+sure);
                YakilanKalori.setText("Yakilan Kalori:"+(sure*38.8));
                Isim.setText("İsim:"+isim);
                VKI.setText("VKI Değeri:"+VKIdegeri+"\n"+Aciklama);
            }
        });

        Map<String,Object> update = new HashMap<>();
        update.put("Öncek Süre",oncekiZaman);
        documentReference.update(update);


    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.Profil:
            {
                Intent intent=new Intent(TebriklerActivity.this,ProfilActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.AnaMenu:
            {
                Intent intent=new Intent(TebriklerActivity.this,MenuActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}