package com.roger.ifoodaula.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.roger.ifoodaula.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                abrirAutentificacao();
            }
        },  3000);
    }

    private void abrirAutentificacao(){
        Intent i = new Intent(SplashActivity.this, AutentificacaoActivity. class);
        startActivity(i);
        finish();
    }
}
