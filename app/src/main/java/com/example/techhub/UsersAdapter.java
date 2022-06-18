package com.example.techhub;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersAdapter extends ArrayAdapter<User> {

    private Context context;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String user_id = currentUser.getUid();

     DatabaseReference ref, ref2;



    public UsersAdapter(Context context, ArrayList<User> users) {
        super(context, 0, users);
        this.context = context;

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView profile, pay;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_users, parent, false);
        }

        // Get the data item for this position
        final User u = getItem(position);


        pay = (ImageView) convertView.findViewById(R.id.pay);
        profile = (ImageView) convertView.findViewById(R.id.person);

        Bundle bundle = ((Activity) context).getIntent().getExtras();
        String message = u.getuId();
        String message1 = bundle.getString("message1");
        System.out.println("THis is message "+message);
        System.out.println("This is message 1"+message1);


        ref = FirebaseDatabase.getInstance().getReference().child("Users");
        ref2 = FirebaseDatabase.getInstance().getReference().child("Projects");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {

                    User user = new User();

                    user.setName(postSnapshot.child("name").getValue().toString());
                    user.setEmail(postSnapshot.child("email").getValue().toString());
                    user.setuId(postSnapshot.child("uId").getValue().toString());
                    if (user.getuId().equals(message)) {
                        if (user.getuId().equals(user_id)) {
                            pay.setVisibility(View.GONE);
                        }
                        break;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        ref2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (postSnapshot.getKey().trim().equals(message1)) {
                        if (!postSnapshot.child("userID").getValue().toString().equals(user_id)) {
                            for ( DataSnapshot Snapshot : postSnapshot.child("users").getChildren()) {
                                if(Snapshot.child("uId").getValue().toString().equals(user_id)) {
                                    if (!Snapshot.child("role").getValue().toString().equals("Financier")) {
                                        pay.setVisibility(View.GONE);
                                        //System.out.println("Visibility");
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });



        User user1 = getItem(position);

        TextView name, description ;


        name = (TextView) convertView.findViewById(R.id.Project_name);
        description =  (TextView) convertView.findViewById(R.id.Description);
        ///
        // Add Role>>>> in class User to display here
        name.setText(u.getName()+"\n");
        if (u.getuId().equals(user_id)) {
            name.setText(u.getName()+" "+"(ME)"+"\n");
        }
        description.setText(u.getRole()+"\n");

        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Payment.class);
                intent.putExtra("message", u.getuId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


        // Profile class displays current user profile just
              profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MemberProfile.class);
                intent.putExtra("message", u.getuId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });



        return convertView;
    }


}
