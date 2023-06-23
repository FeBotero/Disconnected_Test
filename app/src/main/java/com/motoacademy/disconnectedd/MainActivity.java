package com.motoacademy.disconnectedd;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    Button btnChange ,btnReg;
    EditText txtEmail, txtPassword;

    ProgressBar progressBar;
    WifiManager mWifiManager;

    FirebaseAuth mAuth;


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

        setContentView(R.layout.activity_main);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        btnChange = (Button) findViewById(R.id.btnChangeWifi);
        txtEmail = findViewById(R.id.etName);
        txtPassword= findViewById(R.id.etPassword);
        btnReg = findViewById(R.id.btRegister);
        progressBar = findViewById(R.id.progressbar);


        if(mWifiManager.isWifiEnabled()){
            btnChange.setText("WIFI TURN OFF");
        }else
        if(!mWifiManager.isWifiEnabled()){
            btnChange.setText("WIFI TURN ON");
        }

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email,password;
                progressBar.setVisibility(View.VISIBLE);
                btnReg.setVisibility(View.GONE);

                email = txtEmail.getText().toString();
                password = txtPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(MainActivity.this, "Email field cannot is empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(MainActivity.this, "Password field cannot is empty", Toast.LENGTH_SHORT).show();

                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Success", "createUserWithEmail:success");
                                    Toast.makeText(MainActivity.this, "Authentication sucess!",
                                            Toast.LENGTH_SHORT).show();
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    progressBar.setVisibility(View.GONE);
                                    btnChange.setText("WIFI TURN ON");
                                    mWifiManager.setWifiEnabled(false);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Failed", "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(MainActivity.this, "Authentication failed!",
                                            Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    btnReg.setVisibility(View.VISIBLE);

                                }
                            }
                        });
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mWifiManager.isWifiEnabled()){
                    btnChange.setText("WIFI TURN ON");
                    mWifiManager.setWifiEnabled(false);
                }else
                if(!mWifiManager.isWifiEnabled()){
                    btnChange.setText("WIFI TURN OFF");
                    mWifiManager.setWifiEnabled(true);
                }
            }
        });

    }
}