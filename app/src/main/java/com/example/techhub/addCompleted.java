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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

public class addCompleted extends AppCompatActivity {
    EditText txtdes, txtname, txtdev, txttst, txtpm, txtfin, txtother, txturl, txtres, txtrec;
    Button nextbtn, cancelbtn, postbtn;
    ImageView BackButton;
    RadioGroup radioGroup;
    RadioButton radioButton, lastRadioBtn;
    TextView cat;
    DatabaseReference ref;
    completedProject project;
    // Category category;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_completed);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Comment because I try function without logging in>>>>>>>
        String user_id = currentUser.getUid();
       // String user_id = "AAAAAAAA";


        txtname = (EditText) findViewById(R.id.etName);
        txtdes = (EditText) findViewById(R.id.etDescription);
        txturl = (EditText) findViewById(R.id.etURL);
        txtres = (EditText) findViewById(R.id.etRec);
        txtrec = (EditText) findViewById(R.id.etRecommendation);
        txtdev = (EditText) findViewById(R.id.NoOfDevelopers);
        txttst = (EditText) findViewById(R.id.NoOfTesters);
        txtpm = (EditText) findViewById(R.id.NoOfManagers);
        txtfin = (EditText) findViewById(R.id.NoOfFinanciers);
        txtother = (EditText) findViewById(R.id.NoOfOthers);
        postbtn = (Button) findViewById(R.id.Post);
        //nextbtn = (Button) findViewById(R.id.Next);
        cancelbtn = (Button) findViewById(R.id.Cancel);
        BackButton = (ImageView) findViewById(R.id.back);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        //lastRadioBtn = (RadioButton) findViewById(R.id.Other);
        cat = (TextView) findViewById(R.id.Cat);

        project = new completedProject();
        //category = new Category("Games");
        ref = FirebaseDatabase.getInstance().getReference().child("Projects");
        postbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_name = txtname.getText().toString().trim().toUpperCase();
                String txt_des = txtdes.getText().toString().trim();
                String txt_url = txturl.getText().toString().trim();
                String txt_dev = txtdev.getText().toString().trim();
                String txt_fin = txtfin.getText().toString().trim();
                String txt_tst = txttst.getText().toString().trim();
                String txt_pm = txtpm.getText().toString().trim();
                String txt_other = txtother.getText().toString().trim();
                String txt_rec = txtrec.getText().toString().trim();
                String txt_res = txtres.getText().toString().trim();

                if (TextUtils.isEmpty(txt_name)) {
                    txtname.setError("Project name is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_des)) {
                    txtdes.setError("Project description is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_rec)) {
                    txtrec.setError("Project recommendations are required!");
                    return;
                }
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    cat.setError("Project Category is required!");
                    return;
                }
                if (TextUtils.isEmpty(txt_url)) {
                    txturl.setError("Project URL is required!");
                    return;
                }
                if (!isValidURL(txt_url)) {
                    txturl.setError("URL format is incorrect!");
                    return;
                }

                if (TextUtils.isEmpty(txt_res)) {
                    txtres.setError("Project helpful resources are required!");
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

                /*if (TextUtils.isEmpty(txt_dev) || txt_dev == "0") {
                    // Negative????
                    txtdes.setError("Number of team members is required!");
                    return;
                } */


                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);

                project.setName(txt_name);
                project.setDescription(txt_des);
                project.setStatus("Completed");
                project.setCategory(radioButton.getText().toString().trim());
                project.setDevelopers(Long.parseLong(txt_dev));
                project.setFinanciers(Long.parseLong(txtfin.getText().toString().trim()));
                project.setManagers(Long.parseLong(txtpm.getText().toString().trim()));
                project.setTesters(Long.parseLong(txttst.getText().toString().trim()));
                project.setOthers(Long.parseLong(txtother.getText().toString().trim()));
                project.setRecommendations(txt_rec);
                project.setResources(txt_res);
                project.setUserID(user_id);
                project.setUrl(txt_url);
                project.setProgress("Done");
                project.setUsers(null);
                project.setTime(System.currentTimeMillis());

                ref.push().setValue(project);
                Toast.makeText(addCompleted.this, "Your project has been posted successfully!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(), HomePage.class));


            }
        });

        cancelbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), projects.class));
            }
        });


        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), projects.class));
            }
        });

    }

    public boolean isValidURL(String url) {

        try {
            new URL(url).toURI();
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }

        return true;
    }



}
