package com.example.anaproje;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button Cikis,Ana_Menu,GirisYap;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Cikis=findViewById(R.id.Cikis);
        Cikis.setOnClickListener(this);
        Ana_Menu=findViewById(R.id.Ana_Menu);
        Ana_Menu.setOnClickListener(this);
        GirisYap=findViewById(R.id.GirisYap);
        GirisYap.setOnClickListener(this);

        firebaseAuth= FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            Intent  intent=new Intent(MainActivity.this,MenuActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {

        Intent intent;
        switch (v.getId())
        {
            case R.id.Cikis:
            {
                AlertDialog.Builder builder= new AlertDialog.Builder(this);
                builder.setMessage("Uygulamadan çıkmak istediğinizden emin misiniz?").setCancelable(false)
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
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

            case R.id.Ana_Menu:
            {
                intent=new Intent(MainActivity.this,KayitOlActivity.class);
                startActivity(intent);
                break;
            }

            case R.id.GirisYap:
            {
                intent=new Intent(MainActivity.this,GirisActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}