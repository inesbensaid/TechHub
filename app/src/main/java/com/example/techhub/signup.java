package com.example.techhub;

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
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class signup extends AppCompatActivity {
    //start
    private EditText Temail, Tpassword,TCpassword, FName;
    private Button Bregister;
    TextView signinButton;
    private FirebaseAuth auth;
    DatabaseReference ref;
    ProgressBar progressBar;
    User user;

    //end
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        //start
        FName = findViewById(R.id.FName);
        Temail = findViewById(R.id.Temail);
        Tpassword = findViewById(R.id.Tpassword);
        TCpassword = findViewById(R.id.TCpassword);
        Bregister = findViewById(R.id.Bregister);
        signinButton = findViewById(R.id.signinButton); 
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        user = new User();

        Bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_name = FName.getText().toString().trim();
                String txt_email = Temail.getText().toString().trim();
                String txt_pass = Tpassword.getText().toString().trim();
                String txt_cpass = TCpassword.getText().toString().trim();

                if (TextUtils.isEmpty(txt_email)) {
                    Temail.setError("Please Enter Your Email");
                    return;
                }
                if(TextUtils.isEmpty(txt_pass)) {
                    Tpassword.setError("Please Enter Your Password");
                    return;
                }
                if (TextUtils.isEmpty(txt_cpass)) {
                    TCpassword.setError("Please Confirm Your Password");
                    return;
                }
                if(TextUtils.isEmpty(txt_name)) {
                    FName.setError("Please Enter Your Name");
                    return;
                }

                if (txt_pass.length() < 8) {
                    Tpassword.setError("Please Enter at Least 8 Characters!");
                    return;
                }
               /* if (txt_pass.length() < ) {
                    Tpassword.setError("Please Enter at Least 6 Digits!");
                    return;
                } */

                if(!isValidPassword(txt_pass)) {
                    Tpassword.setError("Password must contain at least 1 Character, 1 Number and 1 Special Character!");
                    return;
                }
                if (!txt_cpass.equals(txt_pass)) {
                    TCpassword.setError(" password does not match confirm password!");
                    return;
                }  progressBar.setVisibility(View.VISIBLE);


                auth.createUserWithEmailAndPassword(txt_email, txt_pass).addOnCompleteListener(signup.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user.setName(FName.getText().toString().trim());
                            //user.setUsername(UserName.getText().toString().trim());
                            user.setEmail(Temail.getText().toString().trim());
                            user.setRole("");
                            //user.setPassword(Tpassword.getText().toString());
                            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                            String currentUserId = currentUser.getUid().trim();
                            user.setuId(currentUserId);
                            ref.push().setValue(user);
                            Toast.makeText(signup.this, "Registered!", Toast.LENGTH_LONG).show();
                            UpdateToken();
                            startActivity(new Intent(getApplicationContext(), HomePage.class));
                        } else {
                            Toast.makeText(signup.this, "Register is Failed! " + task.getException() //"Authentication failed."
                                    .getMessage(), Toast.LENGTH_LONG).show();
                                     progressBar.setVisibility(View.GONE);
                        }
                    }
                });



            }

        });


        //private void registerUser (String txt_email, String txt_pass){

        // }

        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
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

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    }
