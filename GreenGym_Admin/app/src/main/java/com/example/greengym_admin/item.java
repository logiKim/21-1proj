package com.example.greengym_admin;

public class item {

    String r_id;
    String p_name;
    String r_text;
    String r_phone;
    String r_name;
    String r_date;

    public item(String r_id, String p_name, String r_text, String r_phone, String r_name, String r_date){
        this.r_id = r_id;
        this.p_name = p_name;
        this.r_text = r_text;
        this.r_phone = r_phone;
        this.r_name = r_name;
        this.r_date = r_date;
    }

    public String getR_id() {
        return r_id;
    }

    public String getP_name() {
        return p_name;
    }

    public String getR_text() {
        return r_text;
    }

    public String getR_phone() {
        return r_phone;
    }

    public String getR_name() {
        return r_name;
    }

    public String getR_date() {
        return r_date;
    }
}