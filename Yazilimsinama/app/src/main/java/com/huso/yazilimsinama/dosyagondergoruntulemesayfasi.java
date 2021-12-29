package com.huso.yazilimsinama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class dosyagondergoruntulemesayfasi extends AppCompatActivity {
    ImageView dosyagonderilenimageview;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dosyagondergoruntulemesayfasi);

        dosyagonderilenimageview=findViewById(R.id.gonderilendosya_imageView);
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();

        /*Bitmap smallImage=makeSmallerImage(secilenfoto,300);

        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        smallImage.compress(Bitmap.CompressFormat.JPEG,50,outputStream);
        byte[] byteArray=outputStream.toByteArray();

         */


        Intent intent=getIntent();
        String gelenkisi=intent.getStringExtra("dosyagonderenenkisi");
        resmigoruntule(gelenkisi);


    }

    public void resmigoruntule(String gelenkisi){
        StorageReference newreferences=storageReference.child("images/"+gelenkisi+".jpeg");
        newreferences.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                String downloadurl=uri.toString();
                Picasso.get().load(downloadurl).into(dosyagonderilenimageview);

            }
        });
    }

    /*public Bitmap makeSmallerImage(Bitmap image, int maximumSize){
        int width=image.getWidth();
        int height=image.getHeight();

        float bitmapRatio=(float) width/(float) height;

        if(bitmapRatio>1){
            width=maximumSize;
            height=(int) (width/bitmapRatio);
        }else {
            height=maximumSize;
            width=(int) (height*bitmapRatio);
        }
        return image.createScaledBitmap(image,width,height,true);
    }

     */

}