package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

public class AddProject extends AppCompatActivity {
    EditText txtdes, txtname, txtdev, txttst, txtpm, txtfin, txtother;
    Button postbtn, cancelbtn;
    ImageView BackButton;
    RadioGroup radioGroup;
    RadioButton radioButton, lastRadioBtn;
    TextView cat;
    DatabaseReference ref, ref2;
    Project project;
    User user;
    ArrayList<User> users;
    Toolbar toolbar;
    Date date;
    // Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Add New Project Idea");

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        //Comment because I try function without logging in>>>>>>>
        String user_id = currentUser.getUid();

        users = new ArrayList<>();
        txtname = (EditText) findViewById(R.id.etName);
        txtdes = (EditText) findViewById(R.id.etDescription);
        txtdev = (EditText) findViewById(R.id.NoOfDevelopers);
        txttst = (EditText) findViewById(R.id.NoOfTesters);
        txtpm = (EditText) findViewById(R.id.NoOfManagers);
        txtfin = (EditText) findViewById(R.id.NoOfFinanciers);
        txtother = (EditText) findViewById(R.id.NoOfOthers);
        postbtn = (Button) findViewById(R.id.Post);
        cancelbtn = (Button) findViewById(R.id.Cancel);
        BackButton = (ImageView) findViewById(R.id.back);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        //lastRadioBtn = (RadioButton) findViewById(R.id.Other);
        cat = (TextView) findViewById(R.id.Cat);

        date = new Date();

        project = new Project();
        //category = new Category("Games");
        ref = FirebaseDatabase.getInstance().getReference().child("Projects");
        ref2 = FirebaseDatabase.getInstance().getReference().child("Users");

        postbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = txtname.getText().toString().toUpperCase().trim().toUpperCase();
                String txt_des = txtdes.getText().toString().trim();
                String txt_dev = txtdev.getText().toString().trim();
                String txt_fin = txtfin.getText().toString().trim();
                String txt_tst = txttst.getText().toString().trim();
                String txt_pm = txtpm.getText().toString().trim();
                String txt_other = txtother.getText().toString().trim();

                if (TextUtils.isEmpty(txt_name)) {
                    txtname.setError("Project name is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_des)) {
                    txtdes.setError("Project description is required!");
                    return;
                }
                if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    cat.setError("Project Category is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_dev)) {
                    txtdev.setError("Number of developers is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_tst)) {
                    txttst.setError("Number of testers is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_fin)) {
                    txtfin.setError("Number of financiers is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_pm)) {
                    txtpm.setError("Number of project managers is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_other)) {
                    txtother.setError("Number of other roles is required!");
                    return;
                }


                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                project.setName(txt_name);
                project.setDescription(txtdes.getText().toString().trim());
                project.setStatus("New");
                project.setCategory(radioButton.getText().toString().trim());
                project.setDevelopers(Long.parseLong(txtdev.getText().toString().trim()));
                project.setFinanciers(Long.parseLong(txtfin.getText().toString().trim()));
                project.setManagers(Long.parseLong(txtpm.getText().toString().trim()));
                project.setTesters(Long.parseLong(txttst.getText().toString().trim()));
                project.setOthers(Long.parseLong(txtother.getText().toString().trim()));
                project.setProgress("Null");
                project.setUserID(user_id);
                project.setTime(System.currentTimeMillis());

                /*ref2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            System.out.println(postSnapshot.child("uId").toString().trim());
                            System.out.println(user_id);
                            user = new User();
                            user.setName(postSnapshot.child("name").getValue().toString());
                            user.setEmail(postSnapshot.child("email").getValue().toString());
                            user.setuId(postSnapshot.child("uId").getValue().toString());
                            if (user.getuId().equals(user_id)) {
                                System.out.println("Entered if add project OWNER");
                                user.setRole("Owner");
                                users.add(user);

                            break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                project.setUsers(users);
                System.out.println(project); */
                ref.push().setValue(project);

                Toast.makeText(AddProject.this, "Your project has been posted successfully!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomePage.class));

            }
        });

        cancelbtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), projects.class));
            }
        });

        BackButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), projects.class));
            }
        });

    }

}
