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
                    MailAdresi.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(TextUtils.isEmpty(sifre)) {
                    Sifre.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(TextUtils.isEmpty(isim)) {
                    Isim.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(TextUtils.isEmpty(Yas.toString())) {
                    Yas.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(TextUtils.isEmpty(Boy.toString())) {
                    Boy.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(TextUtils.isEmpty(Kilo.toString())) {
                    Kilo.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(sifre.length()<8) {
                    Sifre.setError("Şifre en az 8 haneli olması gerekmektedir.");
                    break;
                }
                if(sifre.length()>16) {
                    Sifre.setError("Sifre en fazla 16 haneli olabilir.");
                    break;
                }
                if(yas>200) {
                    Yas.setError("Yaşınız 200'den büyük olması imkansızdır!!!");
                    break;

                }
                if(yas<13) {
                    Yas.setError("Bu uygulamayı kullanabilmek için 13 yaşından büyük veya eşit olmak gerekmektedir!!!");
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
                    saglikDurumu ="Asırı Zayıf";
                    Aciklama="Vücut Kitle Endeks değerinize bakılacak olursa acilen profesyonel bir destek almanız gerekmektedir. Size tavsiyem sürekli kilo"+
                            "almaya çalışım fakat bunu diyetler aracılığı ile yapmalısınız aksi taktirde vücudunuzda çeşitli sorunlar ortaya çıkacaktır.";
                }
                else if(VKI<15 && VKI>=9)
                {
                    saglikDurumu="Sportif";
                    Aciklama="Vücut Kitle Endeksi değeriniz sporcularla hemen hemen benzerlik göstermektedir. Böyle devam edin çok iyi gidiyorsunuz!!";
                }
                else if(VKI<25 && VKI>=15)
                {
                    saglikDurumu="Normal";
                    Aciklama="Vücut Kitle Endeksi değeriniz stabil durumdadır. Spor yaparak daha iyi olabilirsiniz.";
                }
                else if(VKI<30 && VKI>=25)
                {
                    saglikDurumu="Hafif Şişman";
                    Aciklama ="Vücut Kitle Endeksi değeriniz fazla kilonuzun olduğunu gösteriyor. Bunu yapacağınız diyetlerle çabucak vücudunuzu "+
                            "toparlayabilirsiniz.";
                }
                else if(VKI<35 && VKI>=30)
                {
                    saglikDurumu ="Şişman";
                    Aciklama ="Vücut Kitle Endeksi Değeriniz sizin için hiç iyi şeyler göstermiyor. Lütfen şimdiden diyet yapmaya ve profesyonel tedavi"+
                            "almaya başlayın.";
                }
                else if(VKI>=35)
                {
                    saglikDurumu="Aşırı Şişman";
                    Aciklama ="Durumunuz çok kötü. Şuan ki durumunuzu düzeltmek için uzun süreli diyetler uygulamanız olacaktır. Acilen profesyonel destek"+
                            "almanız gerekmektedir.";
                }

                double finalVKI = VKI;
                String finalSaglikDurumu = saglikDurumu;
                String finalAciklama = Aciklama;
                firebaseAuth.createUserWithEmailAndPassword(mailAdresi,sifre).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(KayitOlActivity.this, "Başarılı Şekilde Kayıt Oldunuz.", Toast.LENGTH_SHORT).show();
                            userID = firebaseAuth.getUid();
                            DocumentReference documentReference = fireBaseFireStore.collection("Kullanıcı İsimleri").document(userID);
                            Map<String,Object> user= new HashMap<>();
                            user.put("İsim",isim);
                            user.put("Yaş",yas);
                            user.put("Mail Adresi",mailAdresi);
                            user.put("Boy",boy);
                            user.put("Kilo",kilo);
                            user.put("Kalori",kalori);
                            user.put("Toplam Süre",toplamSure);
                            user.put("Önceki Süre",oncekiSure);
                            user.put("VKI", finalVKI);
                            user.put("Sağlık Durumu", finalSaglikDurumu);
                            user.put("Açıklama", finalAciklama);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","Kullanıcı Profili Oluştu \n Kullanıcı ID:"+userID);
                                }
                            });
                            documentReference.set(user).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","başarısız:"+e.toString());

                                }
                            });
                            startActivity(new Intent(getApplicationContext(),MenuActivity.class));
                        }
                        else
                        {
                            Toast.makeText(KayitOlActivity.this, "Bir hata oluştu."+task.getException().getMessage(), Toast.LENGTH_SHORT).show();

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