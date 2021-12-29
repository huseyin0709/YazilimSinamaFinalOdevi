package com.huso.yazilimsinama;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class profilimiduzenlesayfasi extends AppCompatActivity {
    EditText adedittext,soyadedittext;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilimiduzenlesayfasi);

        adedittext=findViewById(R.id.ad_edittext);
        soyadedittext=findViewById(R.id.soyad_edittext);

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
    }

    public void kaydetbutonu(View view){
        String ad=adedittext.getText().toString();
        String soyad=soyadedittext.getText().toString();
        String sayfadakikisi=firebaseUser.getUid();
        HashMap<String,Object> profildata=new HashMap<>();
        profildata.put("ad",ad);
        profildata.put("soyad",soyad);
        profildata.put("sayfadakikisi",sayfadakikisi);
        firebaseFirestore.collection("Profil").document(sayfadakikisi).set(profildata).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                adedittext.setText("");
                soyadedittext.setText("");
                Toast.makeText(profilimiduzenlesayfasi.this,"Profiliniz basariyla kaydedildi.",Toast.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profilimiduzenlesayfasi.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }
}