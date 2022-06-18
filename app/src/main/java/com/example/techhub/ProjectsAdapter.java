package com.example.techhub;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProjectsAdapter extends ArrayAdapter<Project> {

    private Context context;


    public ProjectsAdapter(Context context, ArrayList<Project> projects) {
        super(context, 0, projects);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.view_projects, parent, false);
        }

        // Get the data item for this position
        final Project project = getItem(position);


        Project project1 = getItem(position);

        TextView name, ago, description ;

        name = (TextView) convertView.findViewById(R.id.Project_name);
        description =  (TextView) convertView.findViewById(R.id.Description);
        ago =  (TextView) convertView.findViewById(R.id.ago);


        String timeAgo = TimeAgo.getTimeAgo(project.getTime());

        name.setText(project.getName()+"\n");
        description.setText(project.getDescription()+"\n");
        ago.setText(timeAgo);


        return convertView;
    }
}