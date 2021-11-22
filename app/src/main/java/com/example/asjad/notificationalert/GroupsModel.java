package com.example.asjad.notificationalert;

/**
 * Created by Asjad on 5/4/2018.
 */

public class GroupsModel {

    String id;
    String name;
    String members;

    public GroupsModel(String id, String name, String members) {
        this.id = id;
        this.name = name;
        this.members = members;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
