package com.huso.yazilimsinama;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class dosyagondersayfasi extends AppCompatActivity {
    ImageView profilimage;
    EditText resimsifreedittext;
    RecyclerView resimdosyasirecyclerview;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private ActivityResultLauncher<String> permisionLauncher;
    private Bitmap secilenfoto;
    private Uri imagedata;
    ArrayList<String> dosyasifrearray;
    ArrayList<String> dosyagonderenkisiarray;
    ArrayList<String> advesoyadarray;
    dosyagonderrecyclerviewadapter dosyagonderrecyclerviewadapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosyagondersayfasi);

        profilimage=findViewById(R.id.resimdosya_imageView);
        resimsifreedittext=findViewById(R.id.resimdosyasisifreleme_edittext);
        resimdosyasirecyclerview=findViewById(R.id.resimdosyasi_recyclerView);

        dosyasifrearray=new ArrayList<>();
        dosyagonderenkisiarray=new ArrayList<>();
        advesoyadarray=new ArrayList<>();

        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();


        resmicvdeayarla();
        registerLauncher();
        dosyagonderenler();
    }

    public void dosyagonderenler(){
        firebaseFirestore.collection("Resimler").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String resimsifre=(String) snapshot.getString("resimsifre");
                        String dosyagonderenkisi=(String) snapshot.getString("dosyagonderenkisi");

                        firebaseFirestore.collection("Profil").document(dosyagonderenkisi).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if (documentSnapshot!=null) {
                                    String ad = documentSnapshot.getString("ad");
                                    String soyad = documentSnapshot.getString("soyad");
                                    String advesoyad = ad + "  " + soyad;

                                    advesoyadarray.add(advesoyad);
                                    dosyasifrearray.add(resimsifre);
                                    dosyagonderenkisiarray.add(dosyagonderenkisi);

                                    resimdosyasirecyclerview.setLayoutManager(new LinearLayoutManager(dosyagondersayfasi.this));//recyclerview ın kaydırılması
                                    dosyagonderrecyclerviewadapter = new dosyagonderrecyclerviewadapter(advesoyadarray,dosyasifrearray,dosyagonderenkisiarray,firebaseFirestore,firebaseUser);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                                    resimdosyasirecyclerview.setAdapter(dosyagonderrecyclerviewadapter);//burada recyclerview ile adaptera bağlıyoruz
                                    dosyagonderrecyclerviewadapter.notifyDataSetChanged();
                                }
                            }
                        });


                    }
                }
            }
        });
    }

    public void resimgondermebutonu(View view){

        String resimsifre=resimsifreedittext.getText().toString();
        String dosyagonderenkisi=firebaseUser.getUid();
        HashMap<String,Object> resimdata=new HashMap<>();
        resimdata.put("resimsifre",resimsifre);
        resimdata.put("dosyagonderenkisi",dosyagonderenkisi);
        firebaseFirestore.collection("Resimler").add(resimdata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(dosyagondersayfasi.this,"Dosya Başarılı Bir Şekilde Gönderilmiştir.",Toast.LENGTH_LONG).show();

            }
        });
    }

    public void fotorafimageview(View view){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
                Snackbar.make(view,"Galerinize gidip fotoraf secmek icin izin veriyormusunuz?",Snackbar.LENGTH_INDEFINITE).setAction("İzin veriyorum", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        permisionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);

                    }
                }).show();
            }else{
                permisionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        }else {
            Intent intenttogallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            activityResultLauncher.launch(intenttogallery);

        }

    }

    private void registerLauncher(){
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    Intent intentFromResult=result.getData();
                    if (intentFromResult !=null){
                        imagedata=intentFromResult.getData();

                        try {
                            if (Build.VERSION.SDK_INT >=28) {
                                ImageDecoder.Source source = ImageDecoder.createSource(dosyagondersayfasi.this.getContentResolver(), imagedata);
                                secilenfoto = ImageDecoder.decodeBitmap(source);
                                profilimage.setImageBitmap(secilenfoto);
                                resmistrogeaktarma();

                            }else {
                                secilenfoto= MediaStore.Images.Media.getBitmap(dosyagondersayfasi.this.getContentResolver(),imagedata);
                                profilimage.setImageBitmap(secilenfoto);
                                resmistrogeaktarma();

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        permisionLauncher=registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    Intent intenttogallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    activityResultLauncher.launch(intenttogallery);
                }else {
                    Toast.makeText(dosyagondersayfasi.this,"İzine İhtiyac Var!",Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    public void resmistrogeaktarma(){
        String sayfadakikisi=firebaseUser.getUid();

        storageReference.child("images/" + sayfadakikisi + ".jpeg").putFile(imagedata).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(dosyagondersayfasi.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });



    }
    public void resmicvdeayarla(){
        String sayfadakikisi=firebaseUser.getUid();
        StorageReference newreferences=storageReference.child("images/"+sayfadakikisi+".jpeg");
        newreferences.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadurl=uri.toString();
                Picasso.get().load(downloadurl).into(profilimage);
            }
        });
    }
}