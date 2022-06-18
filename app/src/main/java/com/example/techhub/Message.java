package com.example.techhub;

public class Message {
    private String text;
    private String id;
    private String uname;
    String time;



    public Message(String text, String id, String project , String uname, String time) {
        this.text = text;
        this.id = id;
        this.project = project;
        this.uname = uname ;
        this.time = time;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    private  String project ;

    public Message() {
    }
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
    public Message(String text, String id) {
        this.text = text;
        this.id = id;
    }
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
