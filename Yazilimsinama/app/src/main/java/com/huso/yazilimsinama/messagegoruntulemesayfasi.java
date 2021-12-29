package com.huso.yazilimsinama;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class messagegoruntulemesayfasi extends AppCompatActivity {
    TextView messaglarigoruntuletextview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messagegoruntulemesayfasi);

        messaglarigoruntuletextview=findViewById(R.id.messaglarigoruntulu_textView);

        Intent intent=getIntent();
        String gelenmesaj=intent.getStringExtra("goruntulenmessage");
        messaglarigoruntuletextview.setText(gelenmesaj);
    }
}