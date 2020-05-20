package com.example.cinesaragon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signIn,signUp;
    private EditText password,email,confirmpw;
    private Context context = this;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MenuPrincipalActivity.class));
            finish();
        }

        progressDialog = new ProgressDialog(this);

        signIn = findViewById(R.id.signInButtonReg);
        signUp =  findViewById(R.id.signUpButtonReg);
        password =  findViewById(R.id.passwordRegisterText);
        email = findViewById(R.id.emailRegisterText);
        confirmpw = findViewById(R.id.confirmPWRegisterText);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        if(view == signIn){
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        if(view == signUp){
            registerUser();
        }
    }

    private void registerUser(){
        String userEmail = email.getText().toString().trim();
        String userPass = password.getText().toString().trim();
        String confirmpass = confirmpw.getText().toString().trim();

        if(TextUtils.isEmpty(userEmail)){
            Toast.makeText(context,"Email can not be empty.", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(userPass)){
            Toast.makeText(context,"Password can not be empty.", Toast.LENGTH_LONG).show();
            return;
        }
        if(!userPass.equals(confirmpass)){
            Toast.makeText(context,"Passwords must be match.", Toast.LENGTH_LONG).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this,"User is registered successfully.",Toast.LENGTH_LONG).show();
                            progressDialog.hide();
                            firebaseAuth.signOut();
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            progressDialog.hide();
                            Toast.makeText(RegisterActivity.this,"User couldn't register, please try again.",Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }


}
