package com.example.anaproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener{
    private Button Kol,Karın,Gogus,Bacak,Sırt,GeriDon;
    int[] dizi =new int[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        dizi[0]=0;
        dizi[1]=0;
        dizi[2]=0;
        dizi[3]=0;
        dizi[4]=0;

        Karın =findViewById(R.id.Karın);
        Karın.setOnClickListener(this);
        Kol =findViewById(R.id.Kol);
        Kol.setOnClickListener(this);
        Gogus =findViewById(R.id.Gogus);
        Gogus.setOnClickListener(this);
        Bacak =findViewById(R.id.Bacak);
        Bacak.setOnClickListener(this);
        Sırt =findViewById(R.id.Sırt);
        Sırt.setOnClickListener(this);
        GeriDon=findViewById(R.id.GeriDon);
        GeriDon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {


        Intent intent;
        switch(v.getId())
        {
            case R.id.Karın:
            {
                intent=new Intent(MenuActivity.this,SureActivity.class);
                dizi[0]=1;
                intent.putExtra("String",dizi);
                Toast.makeText(this, "Karin", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            case R.id.Kol:
            {
                intent=new Intent(MenuActivity.this,SureActivity.class);
                dizi[1]=1;
                intent.putExtra("String",dizi);
                Toast.makeText(this, "Kol", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            case R.id.Gogus:
            {
                intent=new Intent(MenuActivity.this,SureActivity.class);
                dizi[2]=1;
                intent.putExtra("String",dizi);
                Toast.makeText(this, "Gogus", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            case R.id.Bacak:
            {
                intent=new Intent(MenuActivity.this,SureActivity.class);
                dizi[3]=1;
                intent.putExtra("String",dizi);
                Toast.makeText(this, "Bacak", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            case R.id.Sırt:
            {
                intent=new Intent(MenuActivity.this,SureActivity.class);
                dizi[4]=1;
                intent.putExtra("String",dizi);
                Toast.makeText(this, "Sirt", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                break;
            }
            case R.id.GeriDon:
            {
                intent=new Intent(MenuActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            }
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }
}