package com.example.techhub;

import android.content.Context;
import android.content.DialogInterface;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComingRequestAdapter extends ArrayAdapter<Request> {
    Context context;
    APIService apiService  = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
    String pName;

    public ComingRequestAdapter(Context context, ArrayList<Request> requests) {
        super(context, 0, requests);
        this.context = context;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final Request request = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.coming_request_item, parent, false);
        }

        TextView name, description , role ,projectName, ago;
        Button Accept , Reject ;
        String userId = request.getFrom().trim();
        String projectId =request.getProjectID().trim();
        String userName ;
        User u = new User();
        Project project = new Project();

        // connect to layout
        name = (TextView) convertView.findViewById(R.id.Request_name);
        role =  (TextView) convertView.findViewById(R.id.role);
        projectName =  (TextView) convertView.findViewById(R.id.projectName);
        description =  (TextView) convertView.findViewById(R.id.Request_Description);
        Accept =  (Button) convertView.findViewById(R.id.Accept);
        Reject =  (Button) convertView.findViewById(R.id.Reject);
        ago =  (TextView) convertView.findViewById(R.id.ago);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        DatabaseReference pDatabase = FirebaseDatabase.getInstance().getReference().child("Projects");

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.child("uId").getValue().toString().trim().equals(userId)){
                        u.setName(postSnapshot.child("name").getValue().toString().trim());
                        u.setEmail(postSnapshot.child("email").getValue().toString().trim());
                        u.setuId(postSnapshot.child("uId").getValue().toString().trim());
                        u.setRole(request.getRole());
                        u.setKey(postSnapshot.getKey());
                        System.out.println(u.getKey());
                        name.setText(u.getName()+"\n");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
        pDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if(postSnapshot.getKey().trim().equals(projectId)) {
                        pName = postSnapshot.child("name").getValue().toString().trim();
                        projectName.setText("Applying to :"+ pName);
                        project.setKey(postSnapshot.getKey());
                        project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString().trim()));
                        project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString().trim()));
                        project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                        project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                        project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                        //GenericTypeIndicator<ArrayList<User>> t = new GenericTypeIndicator<ArrayList<User>>() {};

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        // display
        String timeAgo = TimeAgo.getTimeAgo(request.getTime());
        description.setText("About : "+request.getDescription());
        role.setText("Role : "+request.getRole());
        ago.setText(timeAgo);
        description.setMovementMethod(new ScrollingMovementMethod());

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        // buttons on click
        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder acceptDialog = new AlertDialog.Builder(view.getContext());
                AlertDialog.Builder done = new AlertDialog.Builder(view.getContext());
                AlertDialog.Builder fail = new AlertDialog.Builder(view.getContext());
                acceptDialog.setTitle("Accept " + u.getName() + " ?");
                done.setMessage("Accepted!");
                done.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        System.out.println(request.getFrom());
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(request.getFrom()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userToken = dataSnapshot.getValue(String.class);
                                sendNotifications(userToken, "Accepted!", "Your joining request to "+pName+" project has been accepted!");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });
                fail.setMessage("You reached the maximum of " + request.getRole());
                fail.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                acceptDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(request.getRole().equals("Developer")) {
                            long numOfDevelopers = project.getDevelopers() ;
                            if(numOfDevelopers > 0) {
                                numOfDevelopers--;
                                reference.child("Projects").child(request.getProjectID())
                                        .child("developers").setValue(numOfDevelopers);
                                reference.child("Requests").child(request.getKey())
                                        .child("status").setValue("Accepted");
                                // Update user role
                                mDatabase.child(u.getKey())
                                        .child("role").setValue("Developer");
                                u.setRole("Developer");
                                // Add user to projects array of users
                                reference.child("Projects").child(project.getKey())
                                        .child("users").push().setValue(u);
                                done.create().show();
                            }else
                                fail.create().show();
                        }
                        if(request.getRole().equals("Tester")) {
                            long numOfTesters = project.getTesters();
                            if(numOfTesters > 0) {
                                numOfTesters--;
                                reference.child("Projects").child(request.getProjectID())
                                        .child("testers").setValue(numOfTesters);
                                reference.child("Requests").child(request.getKey())
                                        .child("status").setValue("Accepted");
                                // Update user role
                                mDatabase.child(u.getKey())
                                        .child("role").setValue("Tester");
                                u.setRole("Tester");
                                // Add user to projects array of users
                                reference.child("Projects").child(project.getKey())
                                        .child("users").push().setValue(u);
                                done.create().show();
                            }else
                                fail.create().show();
                        }
                        if(request.getRole().equals("Financier")) {
                            long c2 = project.getFinanciers();
                            if(c2 > 0) {
                                c2--;
                                reference.child("Projects").child(request.getProjectID())
                                        .child("financiers").setValue(c2);
                                reference.child("Requests").child(request.getKey())
                                        .child("status").setValue("Accepted");
                                // Update user role
                                mDatabase.child(u.getKey())
                                        .child("role").setValue("Financier");
                                u.setRole("Financier");
                                // Add user to projects array of users
                                reference.child("Projects").child(project.getKey())
                                        .child("users").push().setValue(u);
                                done.create().show();
                            }else
                                fail.create().show();
                        }
                        if(request.getRole().equals("Manager")) {
                            long numOfManagers = project.getManagers() ; //managers
                            if(numOfManagers > 0) {
                                numOfManagers--;
                                reference.child("Projects").child(request.getProjectID())
                                        .child("managers").setValue(numOfManagers);
                                reference.child("Requests").child(request.getKey())
                                        .child("status").setValue("Accepted");
                                // Update user role
                                mDatabase.child(u.getKey())
                                        .child("role").setValue("Manager");
                                u.setRole("Manager");
                                // Add user to projects array of users
                                reference.child("Projects").child(project.getKey())
                                        .child("users").push().setValue(u);
                                done.create().show();
                            }else
                                fail.create().show();
                        }
                        if(request.getRole().equals("Other role")) {
                            long numOfOtherRoles = project.getOthers() ;// others
                            if(numOfOtherRoles > 0) {
                                numOfOtherRoles--;
                                reference.child("Projects").child(request.getProjectID())
                                        .child("others").setValue(numOfOtherRoles);
                                reference.child("Requests").child(request.getKey())
                                        .child("status").setValue("Accepted");
                                // Update user role
                                mDatabase.child(u.getKey())
                                        .child("role").setValue("Other role");
                                u.setRole("Other role");
                                // Add user to projects array of users
                                reference.child("Projects").child(project.getKey())
                                        .child("users").push().setValue(u);
                                done.create().show();
                            }else
                                fail.create().show();
                        }

                    }
                });
                acceptDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                acceptDialog.create().show();
            }
        });

        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder rejectDialog = new AlertDialog.Builder(view.getContext());
                AlertDialog.Builder done = new AlertDialog.Builder(view.getContext());
                rejectDialog.setTitle("Reject " + u.getName() + " ?");
                done.setMessage("Rejected!");
                done.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Reject Notification Here
                        System.out.println(request.getFrom());
                        FirebaseDatabase.getInstance().getReference().child("Tokens").child(request.getFrom()).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String userToken = dataSnapshot.getValue(String.class);
                                sendNotifications(userToken, "Rejected!", "Your joining request to "+pName+" project has been rejected!");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
                rejectDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        reference.child("Requests").child(request.getKey())
                                .child("status").setValue("Rejected");
                        done.create().show();

                    }
                });
                rejectDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close the dialog
                    }
                });

                rejectDialog.create().show();

            }
        });

        return convertView;
    }

    public void sendNotifications(String userToken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, userToken);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    System.out.println("Nothing happen");
                    if (response.body().success != 1) {
                        System.out.println("Failed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                        Toast.makeText(context, "Failed ", Toast.LENGTH_LONG);
                    } else
                        System.out.println("Success!!!!");
                } else
                    System.out.println("Failed2222222222!!!!!!!!!!!!!");
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }



}
