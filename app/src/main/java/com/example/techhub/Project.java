package com.example.techhub;

import java.util.ArrayList;
import java.util.Date;

public class Project{

//
    private String name;
    private String description;
    private String category;
    private String status;
    private String progress;
    private long testers;
    private long developers;
    private long managers;
    private long financiers;
    private long others;
    private String userID;
    private String owner;
    private String key;
    private String recommendations;
    private String resources;
    private String url;
    private ArrayList<User> users = new ArrayList<>();
    Date date;
    long time;


    public String getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(String recommendations) {
        this.recommendations = recommendations;
    }

    public String getResources() {
        return resources;
    }

    public void setResources(String resources) {
        this.resources = resources;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Project() {}

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTesters() {
        return testers;
    }

    public void setTesters(long testers) {
        this.testers = testers;
    }

    public long getDevelopers() {
        return developers;
    }

    public void setDevelopers(long developers) {
        this.developers = developers;
    }

    public long getManagers() {
        return managers;
    }

    public void setManagers(long managers) {
        this.managers = managers;
    }

    public long getOthers() {
        return others;
    }

    public void setOthers(long others) {
        this.others = others;
    }

    public long getFinanciers() {
        return financiers;
    }

    public void setFinanciers(long financiers) {
        this.financiers = financiers;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

