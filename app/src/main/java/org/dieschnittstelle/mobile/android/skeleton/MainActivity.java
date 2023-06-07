package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import org.dieschnittstelle.mobile.android.skeleton.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText(R.string.welcome_message_alternative);

        welcomeText.setOnClickListener(view -> {
            showMessage("hello");
        });
    }

    protected void showMessage(String msg){
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }
}
