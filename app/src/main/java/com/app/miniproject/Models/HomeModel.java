package com.app.miniproject.Models;

import java.util.List;

public class HomeModel {
    //1- POST, 2- EXPERTISE, 3-TEAM, 4-USER
    private int type;

    private PostModel post;
    private List<TeamModel> teams;
    private List<ExpertiseModel> expertise;
    private List<UserModel> users;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PostModel getPost() {
        return post;
    }

    public void setPost(PostModel post) {
        this.post = post;
    }

    public List<TeamModel> getTeams() {
        return teams;
    }

    public void setTeams(List<TeamModel> teams) {
        this.teams = teams;
    }

    public List<ExpertiseModel> getExpertise() {
        return expertise;
    }

    public void setExpertise(List<ExpertiseModel> expertise) {
        this.expertise = expertise;
    }

    public List<UserModel> getUsers() {
        return users;
    }

    public void setUsers(List<UserModel> users) {
        this.users = users;
    }
}
