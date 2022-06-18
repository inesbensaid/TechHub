package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyProjects extends AppCompatActivity {

    DatabaseReference ref;
    Project project;
    completedProject completedProject;
    ArrayList<Project> projectsList;
    ListView ProjectList;
    TabLayout tabLayout;
    Toolbar toolbar;
    ImageView backButton;
    TextView CatName;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backButton = findViewById(R.id.back);;
        CatName = findViewById(R.id.catName);;
        CatName.setText("My Projects");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Intent intent1 = new Intent(MyProjects.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(MyProjects.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(MyProjects.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(MyProjects.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });


        tabLayout = findViewById(R.id.tabs);
        ProjectList = (ListView) findViewById(R.id.Project_list);
        ProjectList.setEmptyView(findViewById(R.id.emptyElement));
        ref = FirebaseDatabase.getInstance().getReference().child("Projects");

        selectedNew();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selected = tabLayout.getSelectedTabPosition();

                switch (selected) {

                    case 0:
                        selectedNew();
                        break;

                    case 1:
                        selectedCurrent();
                        break;

                    case 2:
                        selectedCompleted();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                selectedNew();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        // project on click
        ProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                Intent intent = new Intent(MyProjects.this, ProjectDetails.class);
                //Project project = (Project) adapterView.getAdapter().getItem(i);
                Intent intent2 = new Intent(MyProjects.this, Completed_details.class);
                //Intent intent3 = new Intent(MyProjects.this, currentDetails.class);

                Project project = (Project) adapterView.getItemAtPosition(i);
                String k = project.getKey();
                String C = project.getStatus();
                if (C.equals("Completed")) {
                    intent2.putExtra("message", k);
                    startActivity(intent2);
                } /*else if (C.equals("Current")) {
                    intent3.putExtra("message", k);
                    startActivity(intent3);
                }*/
                else {
                    intent.putExtra("message", k);
                    startActivity(intent);
                }

            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
        public void selectedNew() {
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    projectsList = new ArrayList<>();

                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        // Retrieve projects where the user is the owner
                       // if (snapshot.child("userID").equals(user_id) && snapshot.child("status").equals("New")) {
                                project = new Project();
                                project.setName(postSnapshot.child("name").getValue().toString().trim());
                                project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                                project.setCategory(postSnapshot.child("category").getValue(String.class));
                                project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                                project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                                project.setKey(postSnapshot.getKey().trim());
                        project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));

                                for (DataSnapshot Snapshot : postSnapshot.child("users").getChildren()) {
                                    String userID = Snapshot.child("uId").getValue().toString().trim();
                                    //System.out.println(userID);
                                    if (userID.equals(user_id) && project.getStatus().equals("New")) {
                                        projectsList.add(project);
                                        break;
                                    }
                                }

                        if (project.getStatus().equals("New") && (project.getUserID().equals(user_id))) {
                            projectsList.add(project);
                        }


                        }
                    //adapter
                    ProjectsAdapter adapter = new ProjectsAdapter(MyProjects.this, projectsList);
                    //attach
                    ProjectList.setAdapter(adapter);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    public void selectedCurrent() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                projectsList = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // Retrieve projects where the user is the owner
                    //if (snapshot.child("userID").equals(user_id) && snapshot.child("status").equals("Current")) {
                        project = new Project();
                        project.setName(postSnapshot.child("name").getValue().toString().trim());
                        project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                        project.setCategory(postSnapshot.child("category").getValue(String.class));
                        project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                        project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                        project.setKey(postSnapshot.getKey().trim());
                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));
                    for (DataSnapshot Snapshot : postSnapshot.child("users").getChildren()) {
                        String userID = Snapshot.child("uId").getValue().toString().trim();
                        //System.out.println(userID);
                        if (userID.equals(user_id) && project.getStatus().equals("Current")) {
                            projectsList.add(project);
                            break;
                        }
                    }

                }
                //adapter
                CurrentAdapter adapter = new CurrentAdapter(MyProjects.this, projectsList);
                //attach
                ProjectList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void selectedCompleted() {
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                projectsList = new ArrayList<>();

                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    // Retrieve projects where the user is the owner
                    //if (snapshot.child("userID").equals(user_id) && snapshot.child("status").equals("Completed")) {
                        project = new Project();
                        project.setName(postSnapshot.child("name").getValue().toString().trim());
                        project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                        project.setCategory(postSnapshot.child("category").getValue(String.class));
                        project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                        project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                        project.setKey(postSnapshot.getKey().trim());
                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));
                    for (DataSnapshot Snapshot : postSnapshot.child("users").getChildren()) {
                        String userID = Snapshot.child("uId").getValue().toString().trim();
                        //System.out.println(userID);
                        if (userID.equals(user_id) && project.getStatus().equals("Completed")) {
                            projectsList.add(project);
                            break;
                        }
                    }

                        if (project.getStatus().equals("Completed") && project.getUserID().equals(user_id)) {
                            projectsList.add(project);
                        }


                }
                //adapter
                ProjectsAdapter adapter = new ProjectsAdapter(MyProjects.this, projectsList);
                //attach
                ProjectList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}

