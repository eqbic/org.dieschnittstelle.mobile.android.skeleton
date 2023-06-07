package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

public class OverviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
    }

    protected void showMessage(String msg){
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }
}
