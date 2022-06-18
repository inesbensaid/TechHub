package com.example.techhub;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.baoyachi.stepview.VerticalStepView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Progress extends AppCompatActivity {
    VerticalStepView verticalStepView;
    List<String> list = new ArrayList<>();
    Button nextPhase ;
    DatabaseReference pDatabase ;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();
    Project project ;
    ImageView backbutton;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        verticalStepView = (VerticalStepView) findViewById(R.id.verticalStepView);
        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message1");
        nextPhase = findViewById(R.id.nextPhase);
        backbutton = findViewById(R.id.back);

        pDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                project = new Project();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getKey().equals(message)) {
                        String progress = postSnapshot.child("progress").getValue().toString().trim();
                        checkProgress(progress);
                        project.setKey(postSnapshot.getKey());
                        project.setProgress(progress);
                        String userID = postSnapshot.child("userID").getValue().toString().trim();
                        if (!(userID.equals(user_id))){
                            nextPhase.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        nextPhase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder finish = new AlertDialog.Builder(view.getContext());
                finish.setTitle("End Project");
                finish.setMessage("End This Project and Move it To Completed ?");
                AlertDialog.Builder sure = new AlertDialog.Builder(view.getContext());
                sure.setTitle("Proceed to Next Phase ? ");
                sure.setMessage("no going back allowed ");
                String progress = project.getProgress();

                if(progress.equals("Phase1")){ // now 1  move to 2
                    sure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            setStepper(5);
                            startActivity(getIntent());
                            pDatabase.child(project.getKey()).child("progress").setValue("Phase2");
                        }
                    });
                    sure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    sure.create().show();
                }
                else if(progress.equals("Phase2")){ // move to 3
                    sure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            setStepper(4);
                            startActivity(getIntent());
                            pDatabase.child(project.getKey()).child("progress").setValue("Phase3");
                        }
                    });
                    sure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    sure.create().show();
                }
                else if(progress.equals("Phase3")){ // move to 4
                    sure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            setStepper(3);
                            startActivity(getIntent());
                            pDatabase.child(project.getKey()).child("progress").setValue("Phase4");
                        }
                    }); sure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }
                    });
                    sure.create().show();
                }
                else if(progress.equals("Phase4")){ // move to 5
                    sure.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            setStepper(2);
                            startActivity(getIntent());
                            pDatabase.child(project.getKey()).child("progress").setValue("Phase5");
                        }
                    }); sure.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    sure.create().show();
                }
                else if(progress.equals("Phase5")){ // move to completed

                    finish.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                            setStepper(1);
                            startActivity(getIntent());
                            pDatabase.child(project.getKey()).child("progress").setValue("Done");
                            pDatabase.child(project.getKey()).child("status").setValue("Completed");
                            pDatabase.child(project.getKey()).child("recommendations").setValue("Nothing");
                            pDatabase.child(project.getKey()).child("resources").setValue("Nothing");
                            pDatabase.child(project.getKey()).child("url").setValue("https://www.Nothing.com");
                            nextPhase.setVisibility(View.GONE);
                        }
                    });
                    finish.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) { }});
                    finish.create().show();
                }
            }
        });



//  Navigation Bar
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
                        Intent intent1 = new Intent(Progress.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(Progress.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(Progress.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(Progress.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }// end onCreate
    private void checkProgress(String progress) {

        if(progress.equals("Phase1")){
            setStepper(6);
        }else if(progress.equals("Phase2")){
            setStepper(5);
        }else if(progress.equals("Phase3")){
            setStepper(4);
        }else if(progress.equals("Phase4")){
            setStepper(3);
        }else if(progress.equals("Phase5")){
            setStepper(2);
        }

    }

    private void setStepper(int i) {
        list.add("New Project");
        list.add("Phase 1 : Analysis");
        list.add("Phase 2 : Design");
        list.add("Phase 3 : Coding");
        list.add("Phase 4 : Testing");
        list.add("Phase 5 : Maintenance");
        list.add("Completed Project");

        verticalStepView.setStepsViewIndicatorComplectingPosition(list.size()-i)
                .reverseDraw(false)
                .setStepViewTexts(list)
                .setLinePaddingProportion(1.2f)//0.85f
                .setStepsViewIndicatorCompletedLineColor(Color.parseColor("#ff6b00")) // completed
                .setStepsViewIndicatorUnCompletedLineColor(Color.parseColor("#000000"))
                .setStepViewUnComplectedTextColor(Color.parseColor("#000000"))// not completed
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(this,R.mipmap.done))
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(this,R.mipmap.attention2))
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(this,R.drawable.default_icon))
                .setTextSize(18)
                .setStepViewComplectedTextColor(Color.parseColor("#ff6b00"));

    }
}
