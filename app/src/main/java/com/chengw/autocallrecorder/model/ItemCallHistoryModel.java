package com.chengw.autocallrecorder.model;

/**
 * Created by Cheng on 3/3/2015.
 */
public class ItemCallHistoryModel {
    String phone_num;
    String contact_name;
    boolean incoming_call;
    boolean incloud;

    public ItemCallHistoryModel(String phone_num, String contact_name, boolean incoming_call, boolean incloud) {
        this.phone_num = phone_num;
        this.contact_name = contact_name;
        this.incoming_call = incoming_call;
        this.incloud = incloud;
    }

    public String getPhone_num() {
        return phone_num;
    }

    public void setPhone_num(String phone_num) {
        this.phone_num = phone_num;
    }

    public String getContact_name() {
        return contact_name;
    }

    public void setContact_name(String contact_name) {
        this.contact_name = contact_name;
    }
}
