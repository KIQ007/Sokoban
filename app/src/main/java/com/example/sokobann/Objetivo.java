package com.example.sokobann;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Objetivo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivo);

        TextView tvObjetivo = findViewById(R.id.tvObjetivo);

        tvObjetivo.setText("O objetivo do jogo é mover todas as caveiras para as posições onde há chamas.");
    }

    public void voltar(View v){
        Intent intent = new Intent(Objetivo.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}