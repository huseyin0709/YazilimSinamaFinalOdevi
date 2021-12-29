package com.huso.yazilimsinama;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class dosyagonderrecyclerviewadapter extends RecyclerView.Adapter<dosyagonderrecyclerviewadapter.Postdosyagonder> {

    ArrayList<String> advesoyadarray;
    ArrayList<String> dosyasifrearray;
    ArrayList<String> dosyagonderenkisiarray;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public dosyagonderrecyclerviewadapter(ArrayList<String> advesoyadarray,ArrayList<String> dosyasifrearray, ArrayList<String> dosyagonderenkisiarray, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser) {
        this.advesoyadarray = advesoyadarray;
        this.dosyasifrearray = dosyasifrearray;
        this.dosyagonderenkisiarray = dosyagonderenkisiarray;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public Postdosyagonder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.dosyagonder_recyclerview,parent,false);
        return new dosyagonderrecyclerviewadapter.Postdosyagonder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Postdosyagonder holder, int position) {
        holder.dosyagonderenkisitextview.setText(advesoyadarray.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context=holder.itemView.getContext();
                final EditText editText=new EditText(context);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Dosyayı Görmek İçin Şifrenizi Giriniz");
                builder.setMessage("Şifrenizi Giriniz");
                builder.setView(editText);
                builder.setNegativeButton("Doğrulamıyorum",null);
                builder.setPositiveButton("Doğruluyorum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sifreedittext=editText.getText().toString();
                        if (sifreedittext.matches(dosyasifrearray.get(position))){
                            Intent intent=new Intent(context,dosyagondergoruntulemesayfasi.class);
                            intent.putExtra("dosyagonderenenkisi",dosyagonderenkisiarray.get(position));
                            context.startActivity(intent);
                        }else {
                            Toast.makeText(context,"Yanlis Sifre Girdiniz",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return dosyasifrearray.size();
    }

    public class Postdosyagonder extends RecyclerView.ViewHolder{
        TextView dosyagonderenkisitextview;

        public Postdosyagonder(@NonNull View itemView) {
            super(itemView);
            dosyagonderenkisitextview=itemView.findViewById(R.id.dosyagonderenkisi_textview);
        }
    }
}
