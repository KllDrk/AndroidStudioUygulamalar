package com.example.anaproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class KayitOlActivity extends AppCompatActivity implements View.OnClickListener{

    
    private Button AnaMenu,Menu;
    EditText MailAdresi,Sifre,Isim,Yas,Boy,Kilo;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fireBaseFireStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kayit_ol);

        AnaMenu=findViewById(R.id.AnaMenu);
        AnaMenu.setOnClickListener(this);
        Menu=findViewById(R.id.Menu);
        Menu.setOnClickListener(this);

        MailAdresi=findViewById(R.id.MailAdresi);
        Sifre=findViewById(R.id.Sifre);
        Isim=findViewById(R.id.Isim);
        Yas=findViewById(R.id.Yas);
        Boy=findViewById(R.id.Boy);
        Kilo=findViewById(R.id.Kilo);

        firebaseAuth= FirebaseAuth.getInstance();
        fireBaseFireStore= FirebaseFirestore.getInstance();
        if(firebaseAuth.getCurrentUser() !=null)
        {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.AnaMenu:
            {
                Intent intent=new Intent(KayitOlActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.Menu:
            {
                String mailAdresi =     MailAdresi.getText().toString().trim();
                String sifre =          Sifre.getText().toString().trim();
                String isim =           Isim.getText().toString().trim();
                double yas =            Integer.valueOf(Yas.getText().toString());
                double boy =            Integer.valueOf(Boy.getText().toString());
                double kilo =           Integer.valueOf(Kilo.getText().toString());
                double kalori=0,toplamSure=0,oncekiSure=0;
                String saglikDurumu = null;
                String  Aciklama = null;
                double VKI =kilo/(boy*boy);
                VKI*=10000;


                if(TextUtils.isEmpty(mailAdresi)) {
                    MailAdresi.setError("Buras?? Bo?? B??rak??lamaz.");
                    break;
                }
                if(TextUtils.isEmpty(sifre)) {
                    Sifre.setError("Buras?? Bo?? B??rak??lamaz.");
                    break;
                }
                if(TextUtils.isEmpty(isim)) {
                    Isim.setError("Buras?? Bo?? B??rak??lamaz.");
                    break;
                }
                if(TextUtils.isEmpty(Yas.toString())) {
                    Yas.setError("Buras?? Bo?? B??rak??lamaz.");
                    break;
                }
                if(TextUtils.isEmpty(Boy.toString())) {
                    Boy.setError("Buras?? Bo?? B??rak??lamaz.");
                    break;
                }
                if(TextUtils.isEmpty(Kilo.toString())) {
                    Kilo.setError("Buras?? Bo?? B??rak??lamaz.");
                    break;
                }
                if(sifre.length()<8) {
                    Sifre.setError("??ifre en az 8 haneli olmas?? gerekmektedir.");
                    break;
                }
                if(sifre.length()>16) {
                    Sifre.setError("Sifre en fazla 16 haneli olabilir.");
                    break;
                }
                if(yas>200) {
                    Yas.setError("Ya????n??z 200'den b??y??k olmas?? imkans??zd??r!!!");
                    break;

                }
                if(yas<13) {
                    Yas.setError("Bu uygulamay?? kullanabilmek i??in 13 ya????ndan b??y??k veya e??it olmak gerekmektedir!!!");
                    break;
                }
                if(boy>300) {
                    Boy.setError("Boyunuz en fazla 300 santimetre olabilir!!!");
                    break;
                }
                if(boy<50) {
                    Boy.setError("Boyunuz en az 50 santimetre olabilir!!!");
                    break;
                }
                if(kilo>500) {
                    Kilo.setError("Kilonuz en fazla 500 kilogram olabilir!!!");
                    break;
                }
                if(kilo< 30) {
                    Kilo.setError("Kilonuz en az 30 kilogram olabilir!!!");
                    break;
                }

                if(VKI<9 && VKI>=0)
                {
                    saglikDurumu ="As??r?? Zay??f";
                    Aciklama="V??cut Kitle Endeks de??erinize bak??lacak olursa acilen profesyonel bir destek alman??z gerekmektedir. Size tavsiyem s??rekli kilo"+
                            "almaya ??al??????m fakat bunu diyetler arac??l?????? ile yapmal??s??n??z aksi taktirde v??cudunuzda ??e??itli sorunlar ortaya ????kacakt??r.";
                }
                else if(VKI<15 && VKI>=9)
                {
                    saglikDurumu="Sportif";
                    Aciklama="V??cut Kitle Endeksi de??eriniz sporcularla hemen hemen benzerlik g??stermektedir. B??yle devam edin ??ok iyi gidiyorsunuz!!";
                }
                else if(VKI<25 && VKI>=15)
                {
                    saglikDurumu="Normal";
                    Aciklama="V??cut Kitle Endeksi de??eriniz stabil durumdad??r. Spor yaparak daha iyi olabilirsiniz.";
                }
                else if(VKI<30 && VKI>=25)
                {
                    saglikDurumu="Hafif ??i??man";
                    Aciklama ="V??cut Kitle Endeksi de??eriniz fazla kilonuzun oldu??unu g??steriyor. Bunu yapaca????n??z diyetlerle ??abucak v??cudunuzu "+
                            "toparlayabilirsiniz.";
                }
                else if(VKI<35 && VKI>=30)
                {
                    saglikDurumu ="??i??man";
                    Aciklama ="V??cut Kitle Endeksi De??eriniz sizin i??in hi?? iyi ??eyler g??stermiyor. L??tfen ??imdiden diyet yapmaya ve profesyonel tedavi"+
                            "almaya ba??lay??n.";
                }
                else if(VKI>=35)
                {
                    saglikDurumu="A????r?? ??i??man";
                    Aciklama ="Durumunuz ??ok k??t??. ??uan ki durumunuzu d??zeltmek i??in uzun s??reli diyetler uygulaman??z olacakt??r. Acilen profesyonel destek"+
                            "alman??z gerekmektedir.";
                }

                double finalVKI = VKI;
                String finalSaglikDurumu = saglikDurumu;
                String finalAciklama = Aciklama;
                firebaseAuth.createUserWithEmailAndPassword(mailAdresi,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(KayitOlActivity.this, "Ba??ar??l?? ??ekilde Kay??t Oldunuz.", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getUid();
                            DocumentReference documentReference = fireBaseFireStore.collection("Kullan??c?? ??simleri").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("??sim",isim);
                            user.put("Ya??",yas);
                            user.put("Mail Adresi",mailAdresi);
                            user.put("Boy",boy);
                            user.put("Kilo",kilo);
                            user.put("Kalori",kalori);
                            user.put("Toplam S??re",toplamSure);
                            user.put("??nceki S??re",oncekiSure);
                            user.put("VKI", finalVKI);
                            user.put("Sa??l??k Durumu", finalSaglikDurumu);
                            user.put("A????klama", finalAciklama);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","Kullan??c?? Profili Olu??tu \n Kullan??c?? ID:"+userID);
                                }
                            });
                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","ba??ar??s??z:"+e.toString());

                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        }
                        else
                        {
                            Toast.makeText(KayitOlActivity.this, "Bir hata olu??tu."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                Intent intent=new Intent(KayitOlActivity.this,MenuActivity.class);
                startActivity(intent);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}