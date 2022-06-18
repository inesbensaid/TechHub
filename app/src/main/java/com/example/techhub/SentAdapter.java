package com.example.techhub;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;



public class SentAdapter extends ArrayAdapter<Request> {
    public SentAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Request request = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.sent_item, parent, false);
        }

        Request request1 = getItem(position);

        TextView name, description, ago;

        name = (TextView) convertView.findViewById(R.id.Request_name);
        description =  (TextView) convertView.findViewById(R.id.Request_Description);
        ago =  (TextView) convertView.findViewById(R.id.ago);

        DatabaseReference pDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");
        String projectId =request.getProjectID();
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getKey().trim().equals(projectId)) {
                        String pName = postSnapshot.child("name").getValue().toString().trim();
                        name.setText("Applying to :"+ pName);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        String timeAgo = TimeAgo.getTimeAgo(request.getTime());
        ago.setText(timeAgo);
        description.setText("Status: "+request.getStatus()+"\n");

        return convertView;
    }


}