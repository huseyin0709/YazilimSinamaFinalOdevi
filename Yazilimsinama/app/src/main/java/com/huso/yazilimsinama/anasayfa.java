package com.huso.yazilimsinama;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class anasayfa extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();

    }
    public void profilimiduzenlebutonu(View view){
        Intent intent=new Intent(anasayfa.this,profilimiduzenlesayfasi.class);
        startActivity(intent);
    }
    public void sifrelemebutonu(View view){
        Intent intent=new Intent(anasayfa.this,sifrelemesayfasi.class);
        startActivity(intent);
    }
    public void messagegonderbutonu(View view){
        Intent intent=new Intent(anasayfa.this,messagegondersayfasi.class);
        startActivity(intent);
    }
    public void dosyagonderbutonu(View view){
        Intent intent=new Intent(anasayfa.this,dosyagondersayfasi.class);
        startActivity(intent);
    }
    public void cikisyapbutonu(View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(anasayfa.this);
        builder.setTitle("ÇIKIŞ YAP");
        builder.setMessage("Çıkış Yapmak İstermisiniz ?");
        builder.setNegativeButton("HAYIR",null);
        builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                firebaseAuth.signOut();
                Intent intenttosignup=new Intent(anasayfa.this,KullaniciGirisiSayfasi.class);
                startActivity(intenttosignup);
                finish();
            }
        });
        builder.show();
    }
}