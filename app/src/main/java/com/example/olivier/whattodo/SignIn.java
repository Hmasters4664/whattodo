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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mSignUp;
    private FirebaseAuth mAuth;
    private static final String TAG="ERROR";
    private EditText mName;
    private FirebaseFirestore mFire;
    String email;
    String password;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mEmail= (EditText)findViewById(R.id.semail);
        mPassword= (EditText)findViewById(R.id.spassword);
        mName=(EditText)findViewById(R.id.sname);
        mSignUp = (Button)findViewById(R.id.sign_ups);
        mAuth = FirebaseAuth.getInstance();
        mFire= FirebaseFirestore.getInstance();
        mSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email=mEmail.getText().toString().trim();
                password= mPassword.getText().toString().trim();
                name= mName.getText().toString().trim();

                if (!email.isEmpty() && (!password.isEmpty())) {
                    signUP();
                } else {
                    Toast.makeText(SignIn.this, "You must provide a username and " +
                            "password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void signUP()
    {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            final FirebaseUser newUser = task.getResult().getUser();
                            //success creating user, now set display name as name
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build();

                            newUser.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                           // mDialog.dismiss();
                                            if (task.isSuccessful()) {
                                                Log.d(SignIn.class.getName(), "User profile updated.");
                                                /***CREATE USER IN FIREBASE DB AND REDIRECT ON SUCCESS**/
                                                createUserInDb(newUser.getUid(), newUser.getDisplayName(), newUser.getEmail());

                                            }else{
                                                //error
                                                Toast.makeText(SignIn.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            Intent i = new Intent(getBaseContext(), MainActivity.class );
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed please " +
                                    "try again after a few seconds.", Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }


                    }
                });
    }

    private void createUserInDb(String userId, String displayName, String email){
        //mUsersDBref = FirebaseDatabase.getInstance().getReference().child("Users");
        User user = new User(userId, displayName, email);
        Map<String, Object> userz = new HashMap<>();
        userz.put("UserID",userId );
        userz.put("DisplayName", name);
        userz.put("email", email);


        mFire.collection("Users").document(name)
                .set(userz)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }
}
