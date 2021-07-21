package com.example.anaproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class SureActivity extends AppCompatActivity implements View.OnClickListener{

    private Button SureButon,GeriDon;
    EditText Sure;
    int sure;
    int[] deger =new int[5];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sure);
        Sure=findViewById(R.id.Sure);
        SureButon=findViewById(R.id.SureButon);
        SureButon.setOnClickListener(this);
        GeriDon=findViewById(R.id.GeriDon);
        GeriDon.setOnClickListener(this);

    }



    @Override
    public void onClick(View v) {
        int sayi=0;
        Intent intent;
        Intent verial=getIntent();
        deger=verial.getIntArrayExtra("String");
        sure=Integer.parseInt(Sure.getText().toString());
        switch (v.getId())
        {
            case R.id.SureButon:
            {
                for(int i=0;i<deger.length;i++)
                {
                    if(deger[i]==0)
                    {
                        sayi++;
                    }
                    else
                    {
                        break;
                    }
                }
                switch (sayi)
                {
                    case 0:
                    {
                        intent=new Intent(SureActivity.this,KarinActivity.class);
                        intent.putExtra("Sure",sure);
                        startActivity(intent);
                        break;
                    }
                    case 1:
                    {
                        intent=new Intent(SureActivity.this,KolActivity.class);
                        intent.putExtra("Sure",sure);
                        startActivity(intent);
                        break;
                    }
                    case 2:
                    {
                        intent=new Intent(SureActivity.this,GogusActivity.class);
                        intent.putExtra("Sure",sure);
                        startActivity(intent);
                        break;
                    }
                    case 3:
                    {
                        intent=new Intent(SureActivity.this,SirtActivity.class);
                        intent.putExtra("Sure",sure);
                        startActivity(intent);
                        break;
                    }
                    case 4:
                    {
                        intent=new Intent(SureActivity.this,BacakActivity.class);
                        intent.putExtra("Sure",sure);
                        startActivity(intent);
                        break;
                    }
                }
                break;

            }
            case R.id.GeriDon:
            {
                intent=new Intent(SureActivity.this,MenuActivity.class);
                startActivity(intent);
                break;
            }
        }
    }
}