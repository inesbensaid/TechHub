package com.example.techhub;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

    public class Completed_details extends AppCompatActivity {
        TextView etName , etDescription , etRecommendation , etURL ,etRec , NoOfDevelopers2 ,NoOfFinanciers ,NoOfManagers, NoOfTesters, NoOfOthers;
        DatabaseReference reference ;
        Project project;
        ImageView backbutton, delete;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.completed_project);
            //
            reference = FirebaseDatabase.getInstance().getReference().child("Projects");
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String user_id = currentUser.getUid();
            etName = findViewById(R.id.etName);
            etDescription = findViewById(R.id.etDescription);
            etRecommendation = findViewById(R.id.etRecommendation);
            etURL = findViewById(R.id.etURL);
            etRec = findViewById(R.id.etRec);
            NoOfDevelopers2 = findViewById(R.id.NoOfDevelopers2);
            NoOfFinanciers = findViewById(R.id.NoOfFinanciers);
            NoOfManagers = findViewById(R.id.NoOfManagers);
            NoOfTesters = findViewById(R.id.NoOfTesters);
            NoOfOthers = findViewById(R.id.NoOfOthers);
            backbutton = findViewById(R.id.back);
            delete = findViewById(R.id.Delete);


            //

            Bundle bundle = getIntent().getExtras();
            String message = bundle.getString("message");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                        if (postSnapshot.getKey().trim().equals(message)) {
                            project = new Project();
                            project.setName(postSnapshot.child("name").getValue().toString().trim());
                            project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                            project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                            project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                            project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString().trim()));
                            project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                            project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString()));//(String.class).trim());
                            project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                            project.setRecommendations(postSnapshot.child("recommendations").getValue(String.class).trim());
                            project.setResources(postSnapshot.child("resources").getValue(String.class).trim());
                            project.setCategory(postSnapshot.child("category").getValue().toString().trim());
                            project.setUrl(postSnapshot.child("url").getValue(String.class).trim());
                            project.setKey(postSnapshot.getKey().trim());
                            String s = postSnapshot.child("userID").getValue().toString().trim();
                            etName.setText(project.getName());
                            etDescription.setText(project.getDescription());
                            etDescription.setMovementMethod(new ScrollingMovementMethod());
                            NoOfDevelopers2.setText("Developers:"+project.getDevelopers());
                            NoOfManagers.setText("Managers:"+project.getManagers());
                            NoOfFinanciers.setText("Financiers:"+project.getFinanciers());
                            NoOfTesters.setText("Testers:"+project.getTesters());
                            NoOfOthers.setText("Others:"+project.getOthers());
                            etRecommendation.setText(project.getRecommendations());
                            etURL.setText(project.getResources());
                            etRec.setText(project.getUrl());

                            if(!s.equals(user_id)) {
                                delete.setVisibility(View.GONE);
                            }

                            break;
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Delete Project
                    reference.child(message).removeValue();
                    delete.setVisibility(View.GONE);
                    Toast.makeText(Completed_details.this, "Your project has been deleted successfully!", Toast.LENGTH_LONG).show();
                    finish();
                }
            });

            backbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });





        }
    }


