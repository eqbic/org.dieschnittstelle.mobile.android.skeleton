package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

public class OverviewActivity extends AppCompatActivity {

    private ViewGroup listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        this.listView = findViewById(R.id.listView);
        Arrays.asList("das", "ist", "ein", "test").forEach(listItem -> {
            TextView itemView = (TextView) getLayoutInflater().inflate(R.layout.activity_overview_listitem, null);
            itemView.setText(listItem);
            this.listView.addView(itemView);
            itemView.setOnClickListener(view ->{
                showMessage("Selected: " + ((TextView)view).getText());
            });
        });
    }

    protected void showMessage(String msg){
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        Snackbar.make(findViewById(R.id.rootView), msg, Snackbar.LENGTH_SHORT).show();
    }
}
