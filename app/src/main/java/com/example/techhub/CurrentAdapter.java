package com.example.techhub;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CurrentAdapter extends ArrayAdapter<Project> {

    private Context context;

    public CurrentAdapter(Context context, ArrayList<Project> projects) {
        super(context, 0, projects);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView team, progress, chat;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_current, parent, false);
        }

        // Get the data item for this position
        final Project project = getItem(position);

        team = (ImageView) convertView.findViewById(R.id.team);
        progress = (ImageView) convertView.findViewById(R.id.progress);
        chat = (ImageView) convertView.findViewById(R.id.chat);

        Project project1 = getItem(position);

        TextView name,owner , description ;


        name = (TextView) convertView.findViewById(R.id.Project_name);
        description =  (TextView) convertView.findViewById(R.id.Description);
       
        name.setText(project.getName()+"\n");
        description.setText(project.getDescription()+"\n");

        team.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Team.class);
                intent.putExtra("message1", project.getKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Progress.class);
                intent.putExtra("message1", project.getKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Chat.class);
                intent.putExtra("message1", project.getKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
