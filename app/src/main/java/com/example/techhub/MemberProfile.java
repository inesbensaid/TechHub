package com.example.techhub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MemberProfile extends AppCompatActivity {
    TextView emailTxtView, nameTxtView, userName, web, bio, editTextView;
    ImageView backButton;
    User u;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nameTxtView = findViewById(R.id.firstN);
        userName = findViewById(R.id.UserN);
        emailTxtView = findViewById(R.id.Mail);
        web = findViewById(R.id.website);
        bio = findViewById(R.id.bio);
        editTextView = findViewById(R.id.EditBtn);
        backButton = findViewById(R.id.back);;
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        editTextView.setVisibility(View.GONE);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        userRef.addValueEventListener(new ValueEventListener() {
            String fname, mail, web1, bio1;

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (postSnapshot.child("uId").getValue().toString().trim().equals(message)) {
                        fname = postSnapshot.child("name").getValue(String.class);
                       mail = postSnapshot.child("email").getValue(String.class);
                       bio1 = postSnapshot.child("bio").getValue(String.class);
                        web1 = postSnapshot.child("website").getValue(String.class);
                        nameTxtView.setText(fname);
                        emailTxtView.setText(mail);
                        userName.setText(fname);
                        web.setText(web1);
                        bio.setText(bio1);

                        break;
                    }}
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(MemberProfile.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(MemberProfile.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(MemberProfile.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(MemberProfile.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}