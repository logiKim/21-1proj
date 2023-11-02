package com.example.greengym_admin;

public class equip_item {
    String e_name;
    String e_id;

    public equip_item(String e_name, String e_id){
        this.e_name = e_name;
        this.e_id = e_id;
    }

    public String getE_name() {
        return e_name;
    }

    public String getE_id() {
        return e_id;
    }
}
