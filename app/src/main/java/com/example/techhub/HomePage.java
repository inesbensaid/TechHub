package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;


public class HomePage extends AppCompatActivity {

    ImageView search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

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
                        Intent intent1 = new Intent(HomePage.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(HomePage.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(HomePage.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(HomePage.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });

        search = (ImageView) findViewById(R.id.Search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SearchPage.class));
            } });


        CardView edu, travel, social, games, health, other;
        edu = (CardView) findViewById(R.id.cardView);
        games = (CardView) findViewById(R.id.cardView3);
        travel = (CardView) findViewById(R.id.cardView4);
        social = (CardView) findViewById(R.id.cardView5);
        health = (CardView) findViewById(R.id.cardView2);
        other = (CardView) findViewById(R.id.cardView6);

        edu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Education.class));
            }
        });

        games.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Games.class));
            }
        });

        travel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Travel.class));
            }
        });

        social.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Social.class));
            }
        });

        health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Health.class));
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Other.class));
            }
        });


    }


    // logout
    public void Logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }

    // Add New Project
    public void AddProject(View view) {
        startActivity(new Intent(getApplicationContext(), AddProject.class));
    }


}
