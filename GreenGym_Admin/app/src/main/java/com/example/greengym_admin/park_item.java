package com.example.greengym_admin;

public class park_item {
    String park_name;
    String park_id;

    public park_item(String park_name, String park_id){
        this.park_name = park_name;
        this.park_id = park_id;
    }

    public String getPark_name() {
        return park_name;
    }

    public String getPark_id() {
        return park_id;
    }
}
