package com.motoacademy.disconnectedd;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnLog;
    EditText txtEmail, txtPassword;

    TextView gotoRegister;
    ProgressBar progressBar;
    WifiManager mWifiManager;

    HandleProps handleProps = new HandleProps();



    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),Api.class);
            startActivity(intent);
            finish();
        }
    }
    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE) ;

        txtEmail = findViewById(R.id.etName);
        txtPassword= findViewById(R.id.etPassword);
        btnLog = findViewById(R.id.btLogin);
        gotoRegister = findViewById(R.id.textGotoLogin);
        progressBar = findViewById(R.id.progressbar);



        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Register.class);
                startActivity(intent);
                finish();
            }
        });



        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                progressBar.setVisibility(View.VISIBLE);
                btnLog.setVisibility(View.GONE);


                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Email field cannot is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Password field cannot is empty", Toast.LENGTH_SHORT).show();

                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility((View.GONE));
                                    Log.d("Success", "signInWithEmail:success");

                                    handleProps.read("persist.control.wifi.service");
                                    handleProps.write("persist.control.wifi.service",Boolean.toString(true));
                                    handleProps.read("persist.control.wifi.service");


                                    Intent intent = new Intent(getApplicationContext(),Api.class);
                                    startActivity(intent);
                                    finish();

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("fail", "signInWithEmail:failure", task.getException());
                                    handleProps.read("persist.control.wifi.service");
                                    handleProps.write("persist.control.wifi.service",Boolean.toString(false));
                                    handleProps.read("persist.control.wifi.service");
                                    Toast.makeText(MainActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });




    }
}