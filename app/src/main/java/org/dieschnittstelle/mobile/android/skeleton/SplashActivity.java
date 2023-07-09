package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            boolean isOnline = ((TodoApplication)getApplication()).getIsOnline();
            if(isOnline){
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));

            }else{
                startActivity(new Intent(SplashActivity.this, OverviewActivity.class));
            }
            finish();
        }, 2000);
    }
}