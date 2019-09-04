package com.example.shiva.try1;

import android.graphics.Bitmap;

/**
 * Created by Lincoln on 15/01/16.
 */
public class scanned_users {
    private String title, genre, year;
    private String stand_profile,branch_profile,amount,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
    private Bitmap image;

    public scanned_users() {
    }

    public scanned_users(Bitmap image,String fname_profile,String mname_profile,String lname_profile,String mobile_profile,String amount) {
        this.image =image;
        this.fname_profile = fname_profile;
        this.lname_profile = lname_profile;
        this.mname_profile = mname_profile;
        this.mobile_profile = mobile_profile;
        this.amount = amount;

    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getfname() {
        return fname_profile;
    }

    public void setfname(String fname_profile) {
        this.fname_profile = fname_profile;
    }

    public String getlname() {
        return lname_profile;
    }

    public void setlname(String lname_profile) {
        this.lname_profile = lname_profile;
    }

    public String getmname() {
        return mname_profile;
    }

    public void setmname(String mname_profile) {
        this.mname_profile = mname_profile;
    }

    public String getmobile() {
        return mobile_profile;
    }

    public void setmobile(String mobile_profile) {
        this.mobile_profile = mobile_profile;
    }

    public String getamount() {
        return amount;
    }

    public void setamount(String stand_profile) {
        this.amount = amount;
    }
}
