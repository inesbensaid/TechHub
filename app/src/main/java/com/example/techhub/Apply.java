package com.example.techhub;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Apply extends AppCompatActivity {

    DatabaseReference ref;
    Button send;
    RadioGroup radioGroup;
    RadioButton radioButton, dev, tst, fin, mang, other;
    EditText des, name;
    TextView cat;
    String pName;
    Project project;
    Toolbar toolbar;
    ImageView BackButton;
    String ownerID;

    private APIService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setTitle("Apply");
        BackButton = (ImageView) findViewById(R.id.back);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        // Static user ID just to test easily without login page
        String user_id = currentUser.getUid();

        dev = (RadioButton) findViewById(R.id.Developer);
        tst = (RadioButton) findViewById(R.id.Tester);
        fin = (RadioButton) findViewById(R.id.Financier);
        mang = (RadioButton) findViewById(R.id.Manager);
        other = (RadioButton) findViewById(R.id.other);



        Request request = new Request();
        ref = FirebaseDatabase.getInstance().getReference().getRoot();
        send = (Button) findViewById(R.id.Send);
        radioGroup = (RadioGroup) findViewById(R.id.radio);
        cat = (TextView) findViewById(R.id.Cat);
        des = (EditText) findViewById(R.id.etName);
        name = (EditText) findViewById(R.id.projectName);



        Bundle bundle = getIntent().getExtras();
        String message = bundle.getString("message1");
        ref.child("Projects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postSnapshot: snapshot.getChildren()) {
                    if (postSnapshot.getKey().trim().equals(message)) {
                        project = new Project();
                        project.setName(postSnapshot.child("name").getValue().toString().trim());
                        pName = postSnapshot.child("name").getValue().toString().trim();
                        name.setText("Apply To "+pName);
                        project.setDescription(postSnapshot.child("description").getValue().toString().trim());
                        project.setFinanciers(Long.parseLong(postSnapshot.child("financiers").getValue().toString()));
                        project.setTesters(Long.parseLong(postSnapshot.child("testers").getValue().toString()));
                        project.setDevelopers(Long.parseLong(postSnapshot.child("developers").getValue().toString().trim()));
                        project.setManagers(Long.parseLong(postSnapshot.child("managers").getValue().toString().trim()));
                        project.setOthers(Long.parseLong(postSnapshot.child("others").getValue().toString().trim()));
                        project.setStatus(postSnapshot.child("status").getValue().toString().trim());
                        project.setUserID(postSnapshot.child("userID").getValue().toString().trim());
                        ownerID = postSnapshot.child("userID").getValue().toString().trim();
                        project.setKey(postSnapshot.getKey().trim());

                        if (project.getDevelopers()==0) {
                            dev.setVisibility(View.GONE);
                            //System.out.printf("Hi");
                        }
                        if (project.getTesters()==0) {
                            tst.setVisibility(View.GONE);
                        }
                        if (project.getFinanciers()==0) {
                            fin.setVisibility(View.GONE);
                        }
                        if (project.getManagers()==0) {
                            mang.setVisibility(View.GONE);
                        }
                        if (project.getOthers()==0) {
                            other.setVisibility(View.GONE);
                        }

                        break;
                    }


                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Apply.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        // Apply Button
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_des = des.getText().toString().trim();
                /////// Check if it is required
                if (TextUtils.isEmpty(txt_des)) {
                    des.setError("Request description is required!");
                    return;
                }

                if (radioGroup.getCheckedRadioButtonId() == -1)
                {
                    cat.setError("You have to choose a role!");
                    return;
                }

                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                String role = radioButton.getText().toString().trim();

                request.setFrom(user_id);
                request.setTo(project.getUserID());
                request.setDescription(txt_des);
                request.setRole(role);
                request.setProjectID(project.getKey());
                request.setStatus("Sent");
                request.setTime(System.currentTimeMillis());
                ref.child("Requests").push().setValue(request);
                Toast.makeText(Apply.this, "Your request has been sent successfully!", Toast.LENGTH_SHORT).show();
                System.out.println(ownerID);
                FirebaseDatabase.getInstance().getReference().child("Tokens").child(ownerID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String userToken = dataSnapshot.getValue(String.class);
                        sendNotifications(userToken, "New Request!", "You have a new coming request on your project "+pName+" !");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

             finish();

            }
        });

        BackButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
                        Toast.makeText(Apply.this, "Failed ", Toast.LENGTH_LONG);
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
