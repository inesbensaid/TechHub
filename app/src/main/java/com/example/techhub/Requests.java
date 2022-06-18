package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

public class Requests extends AppCompatActivity {

    private DatabaseReference mDatabase;
    ArrayList<Request> requestsList;
    ArrayList<Request>ComingRequestsList;
    ListView RequestList;
    TabLayout tabLayout;
    Button Accept , Reject ;
    Toolbar toolbar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requestslist);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Requests");

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
                        Intent intent1 = new Intent(Requests.this, HomePage.class);
                        startActivity(intent1);
                        break;

                    case R.id.add:
                        Intent intent2 = new Intent(Requests.this, projects.class);
                        startActivity(intent2);
                        break;

                    case R.id.account:
                        Intent intent3 = new Intent(Requests.this, Requests.class);
                        startActivity(intent3);
                        break;
                    case R.id.setting:
                        Intent intent4 = new Intent(Requests.this, MorePage.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });

        tabLayout = findViewById(R.id.tabs);
        RequestList = (ListView) findViewById(R.id.Req_list);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Requests");
        selectedRequest(); //

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int selected = tabLayout.getSelectedTabPosition();

                switch (selected) {

                    case 0:
                        selectedRequest();
                        break;

                    case 1:
                        selectedSent();
                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                selectedRequest();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        //Request on click
        RequestList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, android.view.View view, int i, long l) {
                Intent intent = new Intent(Requests.this, SentDetails.class);
                //Project project = (Project) adapterView.getAdapter().getItem(i);
                //  Intent intent2 = new Intent(Requests.this, ComingDetails.class);

                Request request = (Request) adapterView.getItemAtPosition(i) ;
                String k = request.getKey();
                //     String C = request.getStatus();
                intent.putExtra("message", k);
                startActivity(intent);


            }
        });
        Accept = findViewById(R.id.Accept);
        Reject =  findViewById(R.id.Reject);


    }

    public void selectedSent() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestsList = new ArrayList<>();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = currentUser.getUid();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Request request = new Request();
                    request.setKey(postSnapshot.getKey());
                    request.setDescription(postSnapshot.child("description").getValue().toString().trim());
                    request.setFrom(postSnapshot.child("from").getValue().toString().trim());
                    request.setTo(postSnapshot.child("to").getValue().toString().trim());
                    request.setRole(postSnapshot.child("role").getValue().toString().trim());
                    request.setProjectID(postSnapshot.child("projectID").getValue().toString().trim());
                    request.setStatus(postSnapshot.child("status").getValue().toString().trim());
                    request.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));

                    /////////////////
                    if(request.getFrom().equals(currentUserId) ) {
                        requestsList.add(request);}
                }
                //adapter
                SentAdapter adapter = new SentAdapter(Requests.this, requestsList);
                //attach
                RequestList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void selectedRequest() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ComingRequestsList = new ArrayList<>();
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String currentUserId = currentUser.getUid();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Request request = new Request();
                    request.setKey(postSnapshot.getKey());
                    request.setDescription(postSnapshot.child("description").getValue().toString().trim());
                    request.setFrom(postSnapshot.child("from").getValue().toString().trim());
                    request.setTo(postSnapshot.child("to").getValue().toString().trim());
                    request.setRole(postSnapshot.child("role").getValue().toString().trim());
                    request.setProjectID(postSnapshot.child("projectID").getValue().toString().trim());
                    request.setStatus(postSnapshot.child("status").getValue().toString().trim());
                    request.setTime(Long.parseLong(postSnapshot.child("time").getValue().toString().trim()));
                    // System.out.println("req desc : "+ request.getDescription());
                  if(request.getTo().equals(currentUserId)) {
                      ComingRequestsList.add(request);
                  }
                  if(!(request.getStatus().equals("Sent"))){
                      ComingRequestsList.remove(request);
                  }

                }
                //adapter
                ComingRequestAdapter adapter = new ComingRequestAdapter(Requests.this, ComingRequestsList);
                //attach
                RequestList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

}