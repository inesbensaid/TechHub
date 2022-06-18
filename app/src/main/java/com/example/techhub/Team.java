package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Team extends AppCompatActivity {

    private DatabaseReference ref;
    ArrayList<User> users = new ArrayList();
    //Toolbar toolbar;
    ListView UsersList;
    User user;
    ImageView backbutton;
  //  ImageView profile, pay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message1");

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
                        Intent intent1 = new Intent(Team.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(Team.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(Team.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(Team.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });

        //pay = (ImageView) findViewById(R.id.pay);
      //  profile = (ImageView) findViewById(R.id.person);

        UsersList = (ListView) findViewById(R.id.Project_list);
        backbutton = (ImageView) findViewById(R.id.back);
        ref = FirebaseDatabase.getInstance().getReference().child("Projects");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (postSnapshot.getKey().trim().equals(message)) {
                        for ( DataSnapshot Snapshot : postSnapshot.child("users").getChildren()) {
                            User user = new User();
                            user.setName(Snapshot.child("name").getValue().toString());
                            user.setEmail(Snapshot.child("email").getValue().toString());
                            user.setuId(Snapshot.child("uId").getValue().toString());
                            user.setRole(Snapshot.child("role").getValue().toString());
                            users.add(user);
                            //System.out.println("Added Successfully>>>>>>>>>");
                        }

                        //adapter
                        UsersAdapter adapter = new UsersAdapter(Team.this, users);
                        //attach
                        UsersList.setAdapter(adapter);

                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*
        // user on click
        UsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                Intent intent = new Intent(Team.this, TeamDetails.class);

                User user = (User) adapterView.getItemAtPosition(i);
                String k = user.getuId();


                    intent.putExtra("message", k);
                    intent.putExtra("message1", message);
                    startActivity(intent);
            }
        });  */

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




    }
}