package com.example.asjad.notificationalert;

/**
 * Created by Asjad on 5/23/2018.
 */

public class MemberModel {
    String id;
    String name;
    String number;

    public MemberModel(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
