package com.example.sokobann;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void objetivo(View v){
        Intent intent = new Intent(MainActivity.this, Objetivo.class);
        startActivity(intent);
        finish();
    }

    public void play(View v){
        Intent intent = new Intent(MainActivity.this, Jogo.class);
        startActivity(intent);
        finish();
    }
}