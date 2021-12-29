package com.huso.yazilimsinama;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class messagegondersayfasi extends AppCompatActivity {
    EditText messageedittext,messagespnsifreedittext;
    CheckBox messagespncheckbox,messagesha256checkbox;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    RecyclerView messagerecyclerview;
    messagerecyclerviewadapter messagerecyclerviewadapter;
    ArrayList<String> messagearray;
    ArrayList<String> messagegonderenkisiarray;
    ArrayList<String> sifrearray;
    ArrayList<String> advesoyadarray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagegondersayfasi);

        messageedittext=findViewById(R.id.message_edittext);
        messagespnsifreedittext=findViewById(R.id.messagespn_sifre_edittext);
        messagespncheckbox=findViewById(R.id.messagespn_checkbox);
        messagesha256checkbox=findViewById(R.id.messagesha256_checkBox);
        firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        firebaseUser=firebaseAuth.getCurrentUser();
        messagerecyclerview=findViewById(R.id.message_recyclerview);

        messagearray=new ArrayList<>();
        messagegonderenkisiarray=new ArrayList<>();
        sifrearray=new ArrayList<>();
        advesoyadarray=new ArrayList<>();


        Thread myThread=new Thread(new MyServer());
        myThread.start();

        messagegonderelenler();
    }

    class MyServer implements Runnable
    {
        ServerSocket ss;
        Socket mysocket;
        DataInputStream dis;
        String message;
        Handler handler=new Handler();

        public void run(){
            try {
                ss=new ServerSocket(9700);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"waiting for client",Toast.LENGTH_SHORT).show();

                    }
                });
                while (true){
                    mysocket=ss.accept();
                    dis=new DataInputStream(mysocket.getInputStream());
                    message=dis.readUTF();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),"Message şifreli şekilde gönderildi:"+message,Toast.LENGTH_LONG).show();
                            messagegonder(message);
                        }
                    });
                }

            }catch (IOException e)  {
                e.printStackTrace();

            }
        }
    }

    public void messagegondermebutonu(View view){
        BackroundTask b=new BackroundTask();
        b.execute(messageedittext.getText().toString());


    }

    class BackroundTask extends AsyncTask<String,Void,String>
    {
        Socket s;
        DataOutputStream dos;
        String ip,message;


        @Override
        protected String doInBackground(String... params) {
            ip="10.0.2.16";
            message=params[0];

            try {
                s=new Socket(ip,9700);
                dos=new DataOutputStream(s.getOutputStream());
                dos.writeUTF(message);

                dos.close();

                s.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

    }

    public void messagegonder(String message){
        String messagegonderenkisi=firebaseUser.getUid();
        String sifre=messagespnsifreedittext.getText().toString();
        HashMap<String,Object> messagedata=new HashMap<>();
        messagedata.put("message",message);
        messagedata.put("messagegonderenkisi",messagegonderenkisi);
        messagedata.put("sifre",sifre);
        firebaseFirestore.collection("Messagler").add(messagedata).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void messagegonderelenler(){
        firebaseFirestore.collection("Messagler").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot querySnapshot, @Nullable FirebaseFirestoreException error) {
                if (querySnapshot!=null) {
                    for (DocumentSnapshot snapshot : querySnapshot.getDocuments()) {
                        String message=(String) snapshot.getString("message");
                        String messagegonderenkisi=(String) snapshot.getString("messagegonderenkisi");
                        String sifre=(String) snapshot.getString("sifre");

                        firebaseFirestore.collection("Profil").document(messagegonderenkisi).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                                if (documentSnapshot!=null){
                                    String ad=documentSnapshot.getString("ad");
                                    String soyad=documentSnapshot.getString("soyad");
                                    String advesoyad=ad+"  "+soyad;

                                    advesoyadarray.add(advesoyad);
                                    messagearray.add(message);
                                    messagegonderenkisiarray.add(messagegonderenkisi);
                                    sifrearray.add(sifre);

                                    messagerecyclerview.setLayoutManager(new LinearLayoutManager(messagegondersayfasi.this));//recyclerview ın kaydırılması
                                    messagerecyclerviewadapter = new messagerecyclerviewadapter(advesoyadarray,messagearray,sifrearray,messagegonderenkisiarray,firebaseFirestore,firebaseUser);//messag gonderen kisi ve mesagın adını adaptera gonderiyoruz
                                    messagerecyclerview.setAdapter(messagerecyclerviewadapter);//burada recyclerview ile adaptera bağlıyoruz
                                    messagerecyclerviewadapter.notifyDataSetChanged();
                                }
                            }
                        });

                    }
                }
            }
        });
    }
}