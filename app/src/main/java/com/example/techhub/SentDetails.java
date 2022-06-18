package com.example.techhub;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SentDetails extends AppCompatActivity {
    TextView pName , RDes , RStates;
    DatabaseReference reference ;
    Button cancel ;
    Request request;
    ImageView backbutton ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sent_details);
        //
        reference = FirebaseDatabase.getInstance().getReference().child("Requests");
        RDes = findViewById(R.id.rDes);
        RStates = findViewById(R.id.rStates);
        cancel = findViewById(R.id.Cancel);
        backbutton = findViewById(R.id.back);


        //

        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (postSnapshot.getKey().trim().equals(message)) {
                        request = new Request();
                        request.setProjectID(postSnapshot.child("projectID").getValue().toString().trim());
                        request.setDescription(postSnapshot.child("description").getValue().toString().trim());
                        request.setStatus(postSnapshot.child("status").getValue(String.class));
                        request.setKey(postSnapshot.getKey().trim());
                        RDes.setText(request.getDescription());
                        RDes.setMovementMethod(new ScrollingMovementMethod());
                        RStates.setText(request.getStatus());

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
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Requests.class));
            }
        });
    }
}
