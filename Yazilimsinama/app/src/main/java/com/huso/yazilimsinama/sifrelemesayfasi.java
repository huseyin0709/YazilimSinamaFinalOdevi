package com.huso.yazilimsinama;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class sifrelemesayfasi extends AppCompatActivity {
    EditText yazilanmetinedittext,spnsifreedittext;
    TextView sonucmetintextview;
    CheckBox sha256checkbox,spncheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifrelemesayfasi);

        yazilanmetinedittext=findViewById(R.id.yazilanmetin_edittext);
        spnsifreedittext=findViewById(R.id.spn_sifre_edittext);
        sonucmetintextview=findViewById(R.id.sonuc_textView);
        sha256checkbox=findViewById(R.id.sha256_checkBox);
        spncheckbox=findViewById(R.id.spn_checkbox);


        spnsifreedittext.setVisibility(View.INVISIBLE);
        spncheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spnsifreedittext.setVisibility(View.VISIBLE);
                if(!spncheckbox.isChecked()){
                    spnsifreedittext.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void buton(View view){
        String yazilanmetin=yazilanmetinedittext.getText().toString();
        String sifre=spnsifreedittext.getText().toString();
        if (sha256checkbox.isChecked()){
            byte[] inputdata=yazilanmetin.getBytes();
            byte[] outputdata=new byte[0];

            try {
                outputdata=sha.encryptSHA(inputdata,"SHA-256");
            }catch (Exception e){
                e.printStackTrace();
            }
            BigInteger shaData=new BigInteger(1,outputdata);
            sonucmetintextview.setText(shaData.toString(16));

        }
        else if(spncheckbox.isChecked()){
            String metinresult = sifrevemetinibinaryecevirme(yazilanmetin.getBytes(StandardCharsets.UTF_8));
            String sifreresult=sifrevemetinibinaryecevirme(sifre.getBytes(StandardCharsets.UTF_8));
            System.out.println(binary(metinresult, 16, "\n")+"message");
            System.out.println(binary(sifreresult,16,"\n")+"sifre");

            String metinstring=binary(metinresult, 16, "\n");
            String sifrestring=binary(sifreresult, 16, "\n");
            StringBuffer sb=new StringBuffer();
            for (int i = 0; i < metinstring.length(); i++) {
                sb.append(metinstring.charAt(i)^sifrestring.charAt(i));
            }
            System.out.println(sb);

            char[] dizi = new char[16];
            char[] temp=new char[16];
            for (int a=0;a<16;a++){
                dizi[a]=sb.charAt(a);
                temp[a]=dizi[a];
             }


            dizi[5]=temp[0];
            dizi[9]=temp[1];
            dizi[0]=temp[2];
            dizi[12]=temp[3];
            dizi[7]=temp[4];
            dizi[3]=temp[5];
            dizi[11]=temp[6];
            dizi[14]=temp[7];
            dizi[1]=temp[8];
            dizi[4]=temp[9];
            dizi[13]=temp[10];
            dizi[8]=temp[11];
            dizi[2]=temp[12];
            dizi[15]=temp[13];
            dizi[6]=temp[14];
            dizi[10]=temp[15];

            StringBuffer st=new StringBuffer();
            for (int i=0;i<16;i++){
                st.append(dizi[i]);
            }
            System.out.println(st);


            String s = st.toString();
            String cikti = "";

            for (int i = 0; i < s.length()/8; i++) {

                int a = Integer.parseInt(s.substring(8*i,(i+1)*8),2);
                cikti += (char)(a);
            }

            System.out.println(cikti);

            sonucmetintextview.setText(cikti);

        }


    }

    public static String sifrevemetinibinaryecevirme(byte[] input) {

        StringBuilder result = new StringBuilder();
        for (byte b : input) {
            int val = b;
            for (int i = 0; i < 8; i++) {
                result.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
        }
        return result.toString();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String binary(String binary, int blockSize, String separator) {

        List<String> result = new ArrayList<>();
        int index = 0;
        while (index < binary.length()/2) {
            result.add(binary.substring(index, Math.min(index + blockSize, binary.length())));
            index += blockSize;
        }

        return result.stream().collect(Collectors.joining(separator));
    }

}