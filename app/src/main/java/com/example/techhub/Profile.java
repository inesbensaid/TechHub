package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    TextView emailTxtView, nameTxtView, userName, web, bio , EditBtn;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();
    ImageView backButton;

    private static final String USERS = "Users";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameTxtView = findViewById(R.id.firstN);
        userName = findViewById(R.id.UserN);
        emailTxtView = findViewById(R.id.Mail);
        web = findViewById(R.id.website);
        bio = findViewById(R.id.bio);
        EditBtn = findViewById(R.id.EditBtn);
        backButton = findViewById(R.id.back);;
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child(USERS);


        userRef.addValueEventListener(new ValueEventListener() {
            String fname, mail, web1, bio1;
            @Override
            public void onDataChange(DataSnapshot snapshot){
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String userID = postSnapshot.child("uId").getValue().toString().trim();
                    //System.out.println(userID);
                    if (userID.equals(user_id)) {
                        fname = postSnapshot.child("name").getValue(String.class);
                        mail = postSnapshot.child("email").getValue(String.class);
                        web1 = postSnapshot.child("website").getValue(String.class);
                        bio1 = postSnapshot.child("bio").getValue(String.class);
                        break;
                    }
                }
                nameTxtView.setText(fname);
                emailTxtView.setText(currentUser.getEmail());
                userName.setText(fname);
                web.setText(web1);
                bio.setText(bio1);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        EditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, EditProfile.class));

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
                        Intent intent1 = new Intent(Profile.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(Profile.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(Profile.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(Profile.this, MorePage.class);
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
