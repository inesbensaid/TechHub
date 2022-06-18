package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProjectDetails extends AppCompatActivity {
    TextView pName , pDes , NoOfDevelopers , NoOfTesters ,NoOfFinanciers , NoOfManagers ,NoOfOthers;
    DatabaseReference reference, mDatabase ;
    Button apply , Start;
    Project project;
    CardView deleteCard;
    User u;
    ImageView delete, backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_details);

        //
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        reference = FirebaseDatabase.getInstance().getReference().child("Projects");
        pName = findViewById(R.id.pName);
        pDes = findViewById(R.id.pDes);
        NoOfDevelopers = findViewById(R.id.NoOfDevelopers);
        NoOfTesters = findViewById(R.id.NoOfTesters);
        NoOfFinanciers = findViewById(R.id.NoOfFinanciers);
        NoOfManagers = findViewById(R.id.NoOfManagers);
        NoOfOthers = findViewById(R.id.NoOfOthers);
        apply = findViewById(R.id.apply);
        Start = findViewById(R.id.Start);
        delete = findViewById(R.id.Delete);
       // deleteCard = findViewById(R.id.card2);
        backbutton = findViewById(R.id.back);
        //
        Start.setVisibility(View.GONE);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = currentUser.getUid();
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.child("uId").getValue().toString().trim().equals(user_id)){
                        u = new User();
                        u.setName(postSnapshot.child("name").getValue().toString().trim());
                        u.setEmail(postSnapshot.child("email").getValue().toString().trim());
                        u.setuId(postSnapshot.child("uId").getValue().toString().trim());
                        u.setRole("Owner");
                        //u.setKey(postSnapshot.getKey());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (postSnapshot.getKey().trim().equals(message)) {
                        project = new Project();
                        project.setName(postSnapshot.child("name").getValue().toString().trim());
                        project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                        project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString().trim()));
                        project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString().trim()));
                        project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                        project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                        project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                        project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                        project.setCategory(postSnapshot.child("category").getValue().toString().trim());
                        project.setKey(postSnapshot.getKey().trim());
                        String s = postSnapshot.child("userID").getValue().toString().trim();
                        pName.setText(project.getName());
                        pDes.setText(project.getDescription());
                        pDes.setMovementMethod(new ScrollingMovementMethod());
                        NoOfDevelopers.setText("Developers : "+project.getDevelopers());
                        NoOfManagers.setText("Managers : "+project.getManagers());
                        NoOfFinanciers.setText("Financiers : "+project.getFinanciers());
                        NoOfTesters.setText("Testers : "+project.getTesters());
                        NoOfOthers.setText("Others : "+project.getOthers());

                        if(project.getStatus().equals("Current") || project.getStatus().equals("Completed") || s.equals(user_id)){
                            apply.setVisibility(View.GONE);
                        }

                        if(s.equals(user_id) && project.getStatus().equals("New")) {
                            //System.out.println("Hi");
                            //apply.setVisibility(View.GONE);
                            Start.setVisibility(View.VISIBLE);
                        }

                        if(!s.equals(user_id)) {
                            delete.setVisibility(View.GONE);
                            //deleteCard.setVisibility(View.GONE);
                        }

                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProjectDetails.this, Apply.class);
                intent.putExtra ("message1", project.getKey());
                startActivity(intent);
            }
        });

        Start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add owner to Team
                reference.child(message).child("users").push().setValue(u);
                // Update project Status >>> Update Progress to 1st phase???
                reference.child(message).child("status").setValue("Current");
                reference.child(message).child("progress").setValue("Phase1");
                Toast.makeText(ProjectDetails.this, "Your project has been started!", Toast.LENGTH_LONG).show();
                Start.setVisibility(View.GONE);
                finish();

            }
        });



         delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Delete Project
                reference.child(message).removeValue();
                delete.setVisibility(View.GONE);
                Toast.makeText(ProjectDetails.this, "Your project has been deleted successfully!", Toast.LENGTH_LONG).show();
                finish();

            }
        });




    }
}
