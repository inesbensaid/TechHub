package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class projects extends AppCompatActivity {
    private CardView card2;
    private CardView card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        card2 = findViewById(R.id.card2);
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                startActivity(new Intent(projects.this, addCompleted.class));
                finish();
            }
        });


        card = findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View V) {
                startActivity(new Intent(projects.this, AddProject.class));
                finish();
            }
        });

///

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
                        Intent intent1 = new Intent(projects.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(projects.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(projects.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(projects.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });





    }
}