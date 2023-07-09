package org.dieschnittstelle.mobile.android.skeleton;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import org.dieschnittstelle.mobile.android.skeleton.Model.User;
import org.dieschnittstelle.mobile.android.skeleton.generated.callback.OnClickListener;
import org.dieschnittstelle.mobile.android.skeleton.util.AsyncOperationRunner;
import org.dieschnittstelle.mobile.android.skeleton.util.Authenticator;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private TextInputEditText emailField;
    private TextInputEditText passwordField;
    private Authenticator authenticator;
    private ProgressBar progressBar;

    private AsyncOperationRunner operationRunner;
    private TextView errorMessage;
    private boolean showError = false;

    private boolean emailHasText = false;
    private boolean pwdHasText = false;
    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.+[a-z]+";

    private boolean emailLastFocus = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authenticator = new Authenticator();

        loginButton = findViewById(R.id.loginButton);
        emailField = findViewById(R.id.login_email);
        passwordField = findViewById(R.id.login_password);
        errorMessage = findViewById(R.id.loginErrorMessage);
        progressBar = findViewById(R.id.login_progressBar);

        operationRunner = new AsyncOperationRunner(this, progressBar);

        loginButton.setOnClickListener(view ->{
            checkLogin();
        });

        emailField.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                if(emailField.getText().toString().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailField.getText().toString()).matches()){
                    emailField.setError("Invalid email!");
                }
            }
        });

        emailField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(showError){
                    errorMessage.setVisibility(View.GONE);
                    showError = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                emailHasText = s.length() > 0;
                if(emailHasText && pwdHasText){
                    loginButton.setVisibility(View.VISIBLE);
                }else{
                    loginButton.setVisibility(View.GONE);
                }
            }
        });

        passwordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(showError){
                    errorMessage.setVisibility(View.GONE);
                    showError = false;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                pwdHasText = s.length() > 0;
                if(emailHasText && pwdHasText){
                    loginButton.setVisibility(View.VISIBLE);
                }else{
                    loginButton.setVisibility(View.GONE);
                }
            }
        });
    }

    private void checkLogin() {
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        User user = new User(email, password);

        operationRunner.run(
                () -> {
                    try{
                        Thread.sleep(2000);
                        return authenticator.validate(user);
                    }catch (Exception e){
                        return false;
                    }
                },
                result ->{
                    Log.i(null, "Result: " + result);
                    if(result){
                        startActivity(new Intent(LoginActivity.this, OverviewActivity.class));
                        finish();
                    }else{
                        errorMessage.setVisibility(View.VISIBLE);
                        showError = true;
                    }
                }
        );
    }
}