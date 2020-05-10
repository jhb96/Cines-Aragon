package com.example.cinesaragon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signInButton, signUpButton;
    private EditText email, password;
    private Context context = this;
    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressDialog = new ProgressDialog(this);


        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() !=null){
            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            finish();
        }

        signInButton = findViewById(R.id.signInButton);
        signUpButton =  findViewById(R.id.signUpButton);
        email= findViewById(R.id.emailText);
        password = findViewById(R.id.passwordText);

        signUpButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        if(view == signInButton){
            userLogin();
        }
        if(view == signUpButton){
            Intent intent = new Intent(context, RegisterActivity.class);
            startActivity(intent);
            finish();
        }

    }


    private void userLogin(){
        String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(this,"Email can not be empty.", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userPass)){
            Toast.makeText(context,"Password can not be empty.", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Loginning User.");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            progressDialog.hide();
                            finish();
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        }
                        else{
                            Toast.makeText(context,"Wrong username or password.",Toast.LENGTH_SHORT);
                            progressDialog.hide();
                        }
                    }
                });
    }

}
