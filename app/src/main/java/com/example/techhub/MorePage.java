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

public class MorePage extends AppCompatActivity {

    ImageView person, projectsIcon ;
    TextView profile, projects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_page);

//
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
                        Intent intent1 = new Intent(MorePage.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(MorePage.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(MorePage.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(MorePage.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });

        // Profile Page

        profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                startActivity(new Intent(MorePage.this, Profile.class));
                }
        });

        person = findViewById(R.id.person);
        person.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                startActivity(new Intent(MorePage.this, Profile.class));
            }
        });


        projectsIcon = findViewById(R.id.projectsIcon);
        projectsIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                startActivity(new Intent(MorePage.this, MyProjects.class));
               }
        });

        projects = findViewById(R.id.projects);
        projects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                startActivity(new Intent(MorePage.this, MyProjects.class));

            }
        });
    }

    // logout
    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish(); }

}