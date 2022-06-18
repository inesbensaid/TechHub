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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
public class Other extends AppCompatActivity {
    private DatabaseReference mDatabase;
    ArrayList<Project> projectsList;
    Toolbar toolbar;
    ListView ProjectList;
    TabLayout tabLayout;
    ImageView backButton;
    TextView CatName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backButton = findViewById(R.id.back);;
        CatName = findViewById(R.id.catName);;
        CatName.setText("Other");

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
                        Intent intent1 = new Intent(Other.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(Other.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(Other.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(Other.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });


        
        
        tabLayout = findViewById(R.id.tabs);
        ProjectList = (ListView) findViewById(R.id.Project_list);     
        ProjectList.setEmptyView(findViewById(R.id.emptyElement));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");

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
                Intent intent = new Intent(Other.this, ProjectDetails.class);
                //Project project = (Project) adapterView.getAdapter().getItem(i);
                Intent intent2 = new Intent(Other.this, Completed_details.class);

                Project project = (Project) adapterView.getItemAtPosition(i) ;
                String k = project.getKey();
                String C = project.getStatus();
                if(C.equals("Completed")){
                    intent2.putExtra("message", k);
                    startActivity(intent2);
                }
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
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectsList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Project project = new Project();
                    project.setKey(postSnapshot.getKey());
                    project.setName(postSnapshot.child("name").getValue().toString().trim());
                    project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                    project.setCategory(postSnapshot.child("category").getValue().toString().trim());
                    project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));

                    if (project.getStatus().equals("New") && project.getCategory().equals("Other")) {
                        projectsList.add(project);
                    }
                }
                //adapter
                ProjectsAdapter adapter = new ProjectsAdapter(Other.this, projectsList);
                //attach
                ProjectList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void selectedCurrent() {

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectsList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Project project = new Project();
                    project.setKey(postSnapshot.getKey());
                    project.setName(postSnapshot.child("name").getValue().toString().trim());
                    project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                    project.setCategory(postSnapshot.child("category").getValue().toString().trim());
                    project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));

                    if (project.getStatus().equals("Current") && project.getCategory().equals("Other")) {
                        projectsList.add(project);
                    }
                }
                //adapter
                ProjectsAdapter adapter = new ProjectsAdapter(Other.this, projectsList);
                //attach
                ProjectList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void selectedCompleted() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                projectsList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Project project = new Project();
                    project.setKey(postSnapshot.getKey());
                    project.setName(postSnapshot.child("name").getValue().toString().trim());
                    project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                    project.setCategory(postSnapshot.child("category").getValue().toString().trim());
                    project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));

                    if (project.getStatus().equals("Completed") && project.getCategory().equals("Other")) {
                        projectsList.add(project);
                    }
                }
                //adapter
                ProjectsAdapter adapter = new ProjectsAdapter(Other.this, projectsList);
                //attach
                ProjectList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }
}
