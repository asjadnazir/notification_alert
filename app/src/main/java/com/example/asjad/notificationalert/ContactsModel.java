package com.example.asjad.notificationalert;

/**
 * Created by Asjad on 4/21/2018.
 */

public class ContactsModel {
    boolean isSelected;
    String id;
    String name;
    String number;

    public ContactsModel(boolean isSelected, String id, String name, String number) {
        this.isSelected = isSelected;
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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
