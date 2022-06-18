package com.example.techhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class Login extends AppCompatActivity {
    EditText loginEmail, loginPassword;
    Button loginButton;
    ProgressBar LoginProgressBar;
    TextView loginForgotPass,  signupButton , invalid ;
    FirebaseAuth mAuth;
    //  fakeuser1@somemail.com
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        LoginProgressBar = findViewById(R.id.loginProgressBar);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), HomePage.class));
            UpdateToken();
            finish();
        }
        loginEmail = findViewById(R.id.loginEmailAddress);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupButton =findViewById(R.id.signupButton);
        loginForgotPass = findViewById(R.id.loginForgotPass);
        invalid =findViewById(R.id.invalid);



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString().trim();
                String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Please Enter Your Email");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Please Enter Your Password");
                    return;
                }
                // No need!
                /*if (password.length()<8) {
                    loginPassword.setError("Please Enter at Least 8 Characters!");
                    return;
                } */
                LoginProgressBar.setVisibility(View.VISIBLE);

                // authenticate the user   >.<
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Login.this, "Logged in!",
                                    Toast.LENGTH_SHORT).show();
                            UpdateToken();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        } else {
                            invalid.setVisibility(View.VISIBLE);
                            LoginProgressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });
        // not finished yet
        loginForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText resetPassMail = new EditText(v.getContext());
                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter your email and we'll send you a link to get back into your account.");
                passwordResetDialog.setView(resetPassMail);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String mail = resetPassMail.getText().toString().trim();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "We sent an email to you with a link to get back into your account .", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Error! we couldn't find your account with that information " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                passwordResetDialog.create().show();

            }
        });
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), signup.class));
            }
        });
    }

    private void UpdateToken(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = currentUser.getUid();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(user_id).setValue(token);
    }
}
