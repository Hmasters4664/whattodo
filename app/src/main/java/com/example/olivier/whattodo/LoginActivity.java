package com.example.olivier.whattodo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mlogin;
    private Button mSignUp;
    private FirebaseAuth mAuth;
    private static final String TAG="ERROR";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mEmail= (EditText)findViewById(R.id.email);
        mPassword= (EditText)findViewById(R.id.password);
        mlogin = (Button)findViewById(R.id.login);
        mSignUp = (Button)findViewById(R.id.sign_up);
        mAuth = FirebaseAuth.getInstance();

        mSignUp.setOnClickListener(new View.OnClickListener(){

            @Override public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), SignIn.class );
                startActivity(i);
            }

        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=mEmail.getText().toString().trim();
                String password= mPassword.getText().toString().trim();
                if (!email.isEmpty() && (!password.isEmpty())) {
                    login(email, password);
                } else {
                    Toast.makeText(LoginActivity.this, "You must provide a " +
                            "username and password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void login(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           // updateUI(user);
                            Intent i = new Intent(getBaseContext(), MainActivity.class );
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication " +
                                    "failed loading the registration page.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(), SignIn.class );
                            startActivity(i);
                        }

                        // ...
                    }
                });


    }
}
