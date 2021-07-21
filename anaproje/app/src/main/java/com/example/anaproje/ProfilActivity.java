package com.example.anaproje;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfilActivity extends AppCompatActivity implements View.OnClickListener{

    Button AnaMenu,Cikis;
    FirebaseAuth firebaseAuth;
    TextView Isim,Yas,Boy,Kilo,VKI,SaglikDurumu,YakilanKalori;
    FirebaseFirestore firebaseFirestore;
    String isim,saglikDurumu,userID;
    double yas,boy,kilo,vki,kalori;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        Isim =          findViewById(R.id.Isim);
        Yas =           findViewById(R.id.Yas);
        Boy =           findViewById(R.id.Boy);
        Kilo =          findViewById(R.id.Kilo);
        VKI =           findViewById(R.id.VKI);
        SaglikDurumu =  findViewById(R.id.SaglikDurumu);
        AnaMenu =       findViewById(R.id.AnaMenu);
        Cikis =         findViewById(R.id.Cikis);
        YakilanKalori = findViewById(R.id.YakilanKalori);

        AnaMenu.setOnClickListener(this);
        Cikis.setOnClickListener(this);

        firebaseAuth =          FirebaseAuth.getInstance();
        firebaseFirestore =     FirebaseFirestore.getInstance();
        userID =                firebaseAuth.getUid();

        DocumentReference documentReference = firebaseFirestore.collection("Kullanıcı İsimleri").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
              isim = value.getString("İsim");
              yas = value.getDouble("Yaş");
              boy = value.getDouble("Boy");
              kilo = value.getDouble("kilo");
              vki = value.getDouble("VKI");
              saglikDurumu = value.getString("Sağlık Durumu");
              kalori = value.getDouble("Kalori");

              Isim.setText("İsim:"+isim);
              Yas.setText("Yaş:"+yas);
              Boy.setText("Boy:"+boy);
              Kilo.setText("Kilo:"+kilo);
              VKI.setText("VKİ:"+vki);
              SaglikDurumu.setText("Sağlık Durumu:"+saglikDurumu);
              YakilanKalori.setText("Kalori:"+kalori);
            }
        });
    }

    @Override
    public void onClick(View v) {

        Intent intent;

        switch(v.getId()){
            case R.id.AnaMenu:{
                intent= new Intent(ProfilActivity.this,MenuActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.Cikis:{
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setMessage("Hesabınızdan çıkmak istediğinizden emin misiniz?").setCancelable(false)
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                Intent intent = new Intent(ProfilActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


                break;
            }
        }
    }
}