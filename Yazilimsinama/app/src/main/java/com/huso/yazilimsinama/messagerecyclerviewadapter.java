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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;

public class messagerecyclerviewadapter extends RecyclerView.Adapter<messagerecyclerviewadapter.Postmessage> {
    ArrayList<String> advesoyadarray;
    ArrayList<String> messagearray;
    ArrayList<String> sifrearray;
    ArrayList<String> messagegonderenkisiarray;
    FirebaseFirestore firebaseFirestore;
    FirebaseUser firebaseUser;

    public messagerecyclerviewadapter(ArrayList<String> advesoyadarray,ArrayList<String> messagearray, ArrayList<String> sifrearray, ArrayList<String> messagegonderenkisiarray, FirebaseFirestore firebaseFirestore, FirebaseUser firebaseUser) {
        this.advesoyadarray = advesoyadarray;
        this.messagearray = messagearray;
        this.sifrearray = sifrearray;
        this.messagegonderenkisiarray = messagegonderenkisiarray;
        this.firebaseFirestore = firebaseFirestore;
        this.firebaseUser = firebaseUser;
    }

    @NonNull
    @Override
    public Postmessage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.message_recyclerview,parent,false);
        return new messagerecyclerviewadapter.Postmessage(view);
    }

    @Override
    public int getItemCount() {
        return messagearray.size();
    }

    @Override
    public void onBindViewHolder(@NonNull Postmessage holder, int position) {
        Context context=holder.itemView.getContext();
        holder.messagebilgisi.setText(advesoyadarray.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText editText=new EditText(context);
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Mesajı Okumak İçin Şifrenizi Giriniz");
                builder.setMessage("Şifrenizi Giriniz");
                builder.setView(editText);
                builder.setNegativeButton("Doğrulamıyorum",null);
                builder.setPositiveButton("Doğruluyorum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String sifreedittext=editText.getText().toString();
                        if (sifreedittext.matches(sifrearray.get(position))){
                            Intent intent=new Intent(context,messagegoruntulemesayfasi.class);
                            intent.putExtra("goruntulenmessage",messagearray.get(position));
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

    class Postmessage extends RecyclerView.ViewHolder{
        TextView messagebilgisi;

        public Postmessage(@NonNull View itemView) {
            super(itemView);

            messagebilgisi=itemView.findViewById(R.id.messagebilgisi_textview);
        }
    }
}
