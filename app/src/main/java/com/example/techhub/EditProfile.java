package com.example.techhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

public class EditProfile extends AppCompatActivity {
    private static final String TAG = "TAG";
    Toolbar toolbar;
    EditText username , website , mail , bio;
    TextView userpName ;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();
    Button save ;
    private Timer timer ;
    private final long DELAY = 8000; // in ms
    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference userRef = rootRef.child("Users");
    String uKey ,fname, web1,bio1 ,eml;
    boolean success = false , s ;
    String  uPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        toolbar.setTitle("Profile");
        username = findViewById(R.id.username);
        userpName = findViewById(R.id.UserN);
        mail = findViewById(R.id.Mail);
        website = findViewById(R.id.website);
        bio = findViewById(R.id.bio);
        save = findViewById(R.id.saveBtn);
        String email = currentUser.getEmail();
        mail.setText(email);
        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        }


        userRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot){
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String userID = postSnapshot.child("uId").getValue().toString().trim();
                    //System.out.println(userID);
                    if (userID.equals(user_id)) {
                        uKey= postSnapshot.getKey().trim();
                        fname = postSnapshot.child("name").getValue(String.class);
                        web1 = postSnapshot.child("website").getValue(String.class);
                        bio1 = postSnapshot.child("bio").getValue(String.class);
                        eml = postSnapshot.child("email").getValue(String.class);
                        break;
                    }
                }
                username.setText(fname);
                userpName.setText(fname);
                website.setText(web1);
                bio.setText(bio1);
             //   mail.setText(eml);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

        mail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    AlertDialog.Builder b = new AlertDialog.Builder( v.getContext(), R.style.Widget_AppCompat_ButtonBar_AlertDialog);//EditProfile.this
                    final View v2 = getLayoutInflater().inflate(R.layout.password, null);
                    b.setView(v2);
                    b.setTitle("Enter Your Password");
                    b.setMessage("please re-enter your password to change your Email");
                    b.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            EditText editText = v2.findViewById(R.id.editTextTextPassword);
                            editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                                @Override
                                public void onFocusChange(View view, boolean b1) {
                                    if (!b1) {
                                        String psswrd ;
                                        psswrd = editText.getText().toString().trim();
                                        System.out.println("TRYING TO EXIST !!! with pass1 : " + psswrd);
                                        if(!(psswrd.equals(null))){
                                            uPass = psswrd;
                                            System.out.println("TRYING TO EXIST !!! with pass2 : " + uPass);
                                            timer.cancel();
                                        }
                                    }
                                }
                            });
                        }
                    });
                    b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }});
                    b.create().show();
                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            uPass = ""; // write your password here
                          s =  confirmEmail(mail.getText().toString().trim(), uPass);

                        }

                    }, DELAY);
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder ok =  new AlertDialog.Builder(view.getContext());
                ok.setMessage("You Profile is Updated");
                ok.setTitle("success");
                ok.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { finish();}
                });
                String name = username.getText().toString().trim();
                String site = website.getText().toString().trim();
                String umail = mail.getText().toString().trim();
                String bio2 = bio.getText().toString().trim();
                if(!(name.equals(fname))) {
                    userRef.child(uKey).child("name").setValue(name);
                }
                if(!(site.equals(web1))) {
                    userRef.child(uKey).child("website").setValue(site);
                }
                if(!(bio2.equals(bio1))) {
                    userRef.child(uKey).child("bio").setValue(bio2);
                }

                ok.create().show();
            }
        });

        
    } // end onCreate
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean confirmEmail(String em , String pass){

        AuthCredential credential = EmailAuthProvider
                .getCredential(em, pass); // Current Login Credentials \\
        // Prompt the user to re-provide their sign-in credentials
        currentUser.reauthenticate(credential)
                .addOnCompleteListener((OnCompleteListener<Void>) task -> {
                    System.out.println("User re-authenticated.");
                    //----------------Code for Changing Email Address----------\\
                    currentUser.updateEmail(em)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        System.out.println("User email address updated.");
                                        success = true ;

                                            AlertDialog.Builder okemail =  new AlertDialog.Builder(EditProfile.this);
                                            okemail.setTitle("Email updated");
                                            okemail.setMessage("please login again");
                                            okemail.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    FirebaseAuth.getInstance().signOut();
                                                    startActivity(new Intent(getApplicationContext(), Login.class));

                                                }
                                            });
                                            okemail.create().show();

                                    }
                                }
                            });
                });
        return success;
    }
}
