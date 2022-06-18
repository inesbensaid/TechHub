package com.example.techhub;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchPage extends AppCompatActivity {

    EditText txtname;
    Button search;
    RadioGroup radioGroup1, radioGroup2;
    RadioButton radioButton1, radioButton2;
    DatabaseReference ref;
    //Project project;
    //completedProject project;
    ArrayList<Project> projectsList, results, newProjects, currentProjects, completedProjects;
    ListView ProjectList;
    TabLayout tabLayout;
    String  category;
    TextView toolbar;
  //  LinearLayout buttons;
    TextView cat;
    ImageView backButton, backButton2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);

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
                        Intent intent1 = new Intent(SearchPage.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(SearchPage.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(SearchPage.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(SearchPage.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });



        txtname = (EditText) findViewById(R.id.etName);
        search = (Button) findViewById(R.id.searchButton);
        radioGroup1 = (RadioGroup) findViewById(R.id.radio);
       // radioGroup2 = (RadioGroup) findViewById(R.id.radio2);
        cat = (TextView) findViewById(R.id.Cat);
        backButton = (ImageView) findViewById(R.id.back);
        //backButton2 = (ImageView) findViewById(R.id.back);

        ref = FirebaseDatabase.getInstance().getReference().child("Projects");

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 String txt_name = txtname.getText().toString().trim().toUpperCase();

                int selectedId = radioGroup1.getCheckedRadioButtonId();
                radioButton1 = (RadioButton) findViewById(selectedId);

               // int selectedId2 = radioGroup2.getCheckedRadioButtonId();
                //radioButton2 = (RadioButton) findViewById(selectedId);

                if (TextUtils.isEmpty(txt_name) && radioGroup1.getCheckedRadioButtonId() == -1) {
                    showToastMessage("Please fill one field at least to view results!");
                    txtname.setError("Please fill one field at least to view results!");
                    cat.setError("Please fill one field at least to view results!");
                    //Toast.makeText(SearchPage.this, "Please fill one field at least to view results!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (radioGroup1.getCheckedRadioButtonId() != -1) {

                    category = radioButton1.getText().toString().trim();
                }
                //String status = radioButton2.getText().toString().trim();

           // for (Project project: projectsList) {
                results = new ArrayList<>();

                if (!TextUtils.isEmpty(txt_name)) {
                    if (radioGroup1.getCheckedRadioButtonId() != -1) {

                        ref.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //projectsList = new ArrayList<>();

                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    // if (snapshot.child("Projects/status").equals("New")) {
                                    Project project = new Project();
                                    project.setName(postSnapshot.child("name").getValue().toString().trim());
                                    project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                                    project.setCategory(postSnapshot.child("category").getValue(String.class));
                                    project.setProgress(postSnapshot.child("progress").getValue(String.class));
                                     project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString().trim()));
                                    project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString().trim()));
                                    project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                                     project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                                    project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                                    project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));
                                      //project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                                    //  project.setKey(postSnapshot.getKey().trim());

                                    // Retrieve by all search options
                                    if (project.getName().equals(txt_name) && project.getCategory().equals(category)) {
                                        results.add(project);
                                    }
                                }
                                setContentView(R.layout.project_list);
                                backButton2 = (ImageView) findViewById(R.id.back);
                                backButton2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                                toolbar = (TextView) findViewById(R.id.catName);
                                toolbar.setText("Results");
                                tabLayout = findViewById(R.id.tabs);
                                ProjectList = (ListView) findViewById(R.id.Project_list);

                                selectedNew(results);

                                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        int selected = tabLayout.getSelectedTabPosition();

                                        switch (selected) {

                                            case 0:
                                                selectedNew(results);
                                                break;

                                            case 1:
                                                selectedCurrent(results);
                                                break;

                                            case 2:
                                                selectedCompleted(results);
                                                break;
                                        }
                                    }

                                    @Override
                                    public void onTabUnselected(TabLayout.Tab tab) {
                                        selectedNew(results);
                                    }

                                    @Override
                                    public void onTabReselected(TabLayout.Tab tab) {
                                    }
                                });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SearchPage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        // Retrieve by name
                        ref.addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                projectsList = new ArrayList<>();

                                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                    // if (snapshot.child("Projects/status").equals("New")) {
                                    Project project = new Project();
                                    project.setName(postSnapshot.child("name").getValue().toString().trim());
                                    project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                                    project.setCategory(postSnapshot.child("category").getValue(String.class));
                                    project.setProgress(postSnapshot.child("progress").getValue(String.class));
                                    project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString().trim()));
                                    project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString().trim()));
                                    project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                                    project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                                    project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                                    project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                                    project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));
                                    //   project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                                    //  project.setKey(postSnapshot.getKey().trim());

                                    if (project.getName().equals(txt_name)) {
                                        results.add(project);
                                    }
                                }
                                setContentView(R.layout.project_list);
                                backButton2 = (ImageView) findViewById(R.id.back);
                                backButton2.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                });
                                toolbar = (TextView) findViewById(R.id.catName);
                                toolbar.setText("Results");
                                tabLayout = findViewById(R.id.tabs);
                                ProjectList = (ListView) findViewById(R.id.Project_list);

                                selectedNew(results);

                                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                    @Override
                                    public void onTabSelected(TabLayout.Tab tab) {
                                        int selected = tabLayout.getSelectedTabPosition();

                                        switch (selected) {

                                            case 0:
                                                selectedNew(results);
                                                break;

                                            case 1:
                                                selectedCurrent(results);
                                                break;

                                            case 2:
                                                selectedCompleted(results);
                                                break;
                                        }
                                    }

                                    @Override
                                    public void onTabUnselected(TabLayout.Tab tab) {
                                        selectedNew(results);
                                    }

                                    @Override
                                    public void onTabReselected(TabLayout.Tab tab) {
                                    }
                                });

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SearchPage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                } else if (radioGroup1.getCheckedRadioButtonId() != -1) {
                    ref.addValueEventListener(new ValueEventListener() {
                        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            projectsList = new ArrayList<>();

                            for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                                // if (snapshot.child("Projects/status").equals("New")) {
                                Project project = new Project();
                                project.setName(postSnapshot.child("name").getValue().toString().trim());
                                project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                                project.setCategory(postSnapshot.child("category").getValue(String.class));
                                project.setProgress(postSnapshot.child("progress").getValue(String.class));
                                project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString().trim()));
                                project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString().trim()));
                                project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                                project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                                project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                                project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                                project.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));
                                //   project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                                //  project.setKey(postSnapshot.getKey().trim());

                                if (project.getCategory().equals(category)) {
                                    results.add(project);
                                }

                            }

                            setContentView(R.layout.project_list);
                            backButton2 = (ImageView) findViewById(R.id.back);
                            backButton2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            toolbar = (TextView) findViewById(R.id.catName);
                            toolbar.setText("Results");
                            tabLayout = findViewById(R.id.tabs);
                            ProjectList = (ListView) findViewById(R.id.Project_list);

                            selectedNew(results);

                            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                                @Override
                                public void onTabSelected(TabLayout.Tab tab) {
                                    int selected = tabLayout.getSelectedTabPosition();

                                    switch (selected) {

                                        case 0:
                                            selectedNew(results);
                                            break;

                                        case 1:
                                            selectedCurrent(results);
                                            break;

                                        case 2:
                                            selectedCompleted(results);
                                            break;
                                    }
                                }

                                @Override
                                public void onTabUnselected(TabLayout.Tab tab) {
                                    selectedNew(results);
                                }

                                @Override
                                public void onTabReselected(TabLayout.Tab tab) {
                                }
                            });


                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(SearchPage.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
                        }
                    });

                   } else {
                    // All are empties == display error message
                    Toast.makeText(SearchPage.this, "Please fill one field at least to view results!", Toast.LENGTH_LONG).show();
                }
            }
        });


       /* project on click
        ProjectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                Intent intent = new Intent(SearchPage.this, ProjectDetails.class);
                //Project project = (Project) adapterView.getAdapter().getItem(i);

                Project project = (Project) adapterView.getItemAtPosition(i) ;
                String k = project.getKey();
                intent.putExtra("message", k);
                startActivity(intent);
            }
        }); */

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    public void selectedNew(ArrayList<Project> list) {
        newProjects = new ArrayList<>();

        for (Project project: list) {
            if (project.getStatus().equals("New")) {
                newProjects.add(project);
            }
        }
        //adapter
        ProjectsAdapter adapter = new ProjectsAdapter(SearchPage.this, newProjects);
        //attach
        ProjectList.setAdapter(adapter);
    }



    public void selectedCurrent(ArrayList<Project> list) {
        currentProjects = new ArrayList<>();

        for (Project project: list) {
            if (project.getStatus().equals("Current")) {
                currentProjects.add(project);
            }
        }
        //adapter
        ProjectsAdapter adapter = new ProjectsAdapter(SearchPage.this, currentProjects);
        //attach
        ProjectList.setAdapter(adapter);
    }

    public void selectedCompleted(ArrayList<Project> list) {
        completedProjects = new ArrayList<>();

        for (Project project: list) {
            if (project.getStatus().equals("Completed")) {
                completedProjects.add(project);
            }
        }
        //adapter
        ProjectsAdapter adapter = new ProjectsAdapter(SearchPage.this, completedProjects);
        //attach
        ProjectList.setAdapter(adapter);
    }

    public void showToastMessage(String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /*Toast toast = Toast.makeText(SearchPage.this, msg, Toast.LENGTH_LONG);
                toast.setGravity(Gravity.CENTER_HORIZONTAL,0,0);
                toast.show(); */
              Toast.makeText(SearchPage.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }


}
