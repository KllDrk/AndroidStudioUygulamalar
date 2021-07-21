package com.example.anaproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class GirisActivity extends AppCompatActivity implements View.OnClickListener{

    private Button AnaMenu,Menu;
    EditText MailAdresi,Sifre;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giris);

        AnaMenu=findViewById(R.id.AnaMenu);
        AnaMenu.setOnClickListener(this);
        Menu=findViewById(R.id.Menu);
        Menu.setOnClickListener(this);

        MailAdresi=findViewById(R.id.MailAdresi);
        Sifre=findViewById(R.id.Sifre);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View v) {

        String eMail = MailAdresi.getText().toString().trim();
        String password = Sifre.getText().toString().trim();



        switch(v.getId())
        {


            case R.id.AnaMenu:
            {
                Intent intent=new Intent(GirisActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.Menu:
            {
                if(TextUtils.isEmpty(eMail)) {
                    MailAdresi.setError("Burası Boş Bırakılamaz.");
                    break;
                }
                if(TextUtils.isEmpty(password)) {
                    Sifre.setError("Burası Boş Bırakılamaz.");
                    break;
                }

                if(password.length()<8) {
                    Sifre.setError("Şifre en az 8 haneli olması gerekmektedir.");
                    break;
                }
                if(password.length()>16) {
                    Sifre.setError("Sifre en fazla 16 haneli olabilir.");
                    break;
                }


                firebaseAuth.signInWithEmailAndPassword(eMail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(GirisActivity.this, "Başarıyla Giriş Yapıldı.", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(GirisActivity.this,MenuActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(GirisActivity.this, "Hata", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            }
        }
    }
}