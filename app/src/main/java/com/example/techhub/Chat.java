package com.example.techhub;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class Chat extends AppCompatActivity {

    private static final String TAG = Chat.class.getName();

    private EditText metText;
    private Button mbtSent;
    private DatabaseReference mFirebaseRef , uFirebaseRef;
    DatabaseReference pDatabase ;
    Toolbar toolbar;
    private List<Message> mChats;
    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();
    private String mId;
    String uName ;
    String time;
    Project project ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_page);

        Bundle bundle = getIntent().getExtras();
        String k = bundle.getString("message1");
        toolbar = (Toolbar) findViewById(R.id.toolbar3);
        metText = (EditText) findViewById(R.id.etText);
        mbtSent = (Button) findViewById(R.id.btSent);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvChat);
        mChats = new ArrayList<>();
       // mId =currentUser.getUid();
        mId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatAdapter(mChats, mId);
        mRecyclerView.setAdapter(mAdapter);

        setSupportActionBar(toolbar);
        // add back arrow to toolbar
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("message");
        pDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                project = new Project();
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getKey().equals(k)) {
                        project.setKey(postSnapshot.getKey().trim());
                        project.setName(postSnapshot.child("name").getValue().toString().trim());
                            toolbar.setTitle(project.getName() + " Chat");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        mbtSent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = metText.getText().toString().trim();
              //  str =project.getKey();
                if (!message.isEmpty()) {
                    DateFormat df = new SimpleDateFormat("HH:mm");
                    Calendar calobj = Calendar.getInstance();
                    time = df.format(calobj.getTime());
                    mFirebaseRef.push().setValue(new Message(message, mId,k ,uName, time));
                }
                metText.setText("");
            }
        });
        uFirebaseRef  = database.getReference("Users");
        uFirebaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (user_id.equals(postSnapshot.child("uId").getValue().toString().trim())){
                        uName =  postSnapshot.child("name").getValue().toString().trim();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        /**
         * Firebase - Receives message
         */
        mFirebaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null && dataSnapshot.getValue() != null) {
                    try {
                       // Message model = dataSnapshot.getValue(Message.class);
                        Message message = new Message();
                        message.setId(dataSnapshot.child("id").getValue().toString().trim());
                        message.setProject(dataSnapshot.child("project").getValue().toString().trim());
                        message.setText(dataSnapshot.child("text").getValue().toString().trim());
                        message.setUname(dataSnapshot.child("uname").getValue().toString().trim());
                        message.setTime(dataSnapshot.child("time").getValue().toString().trim());
                        if(message.getProject().equals(k)) {
                            mChats.add(message);
                        }
                        mRecyclerView.scrollToPosition(mChats.size() - 1);
                        mAdapter.notifyItemInserted(mChats.size() - 1);
                    } catch (Exception ex) {
                        Log.e(TAG, ex.getMessage());
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) { }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, databaseError.getMessage());
            }
        });

    }//end onCreate
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);
    }

}
