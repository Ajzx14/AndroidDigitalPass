package com.example.shiva.try1;

import android.graphics.Bitmap;

/**
 * Created by Lincoln on 15/01/16.
 */
public class Movie {
    private String title, genre, year;
    private String stand_profile,branch_profile,fname_profile,lname_profile,mname_profile,mobile_profile,p_mobile_profile,email_profile,paidtill_profile,route_profile,college_profile,bustype_profile;
    private Bitmap image;

    public Movie() {
    }

    public Movie(Bitmap image,String fname_profile,String lname_profile,String mname_profile,String mobile_profile,String email_profile,String paidtill_profile,String route_profile,String bustype_profile,String stand_profile,String college_profile) {
        this.image =image;
        this.fname_profile = fname_profile;
        this.lname_profile = lname_profile;
        this.mname_profile = mname_profile;
        this.mobile_profile = mobile_profile;
        this.email_profile = email_profile;
        this.paidtill_profile = paidtill_profile;
        this.route_profile = route_profile;
        this.bustype_profile = bustype_profile;
        this.stand_profile = stand_profile;
        this.college_profile =college_profile;
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
//
    public String getbustype() {
        return bustype_profile;
    }

    public void setbustype(String bustype_profile) {
        this.bustype_profile = bustype_profile;
    }

    public String getlemail() {
        return email_profile;
    }

    public void setemail(String email_profile) {
        this.email_profile = email_profile;
    }

    public String getpaidtill() {
        return paidtill_profile;
    }

    public void setpaidtill(String paidtill_profile) {
        this.paidtill_profile = paidtill_profile;
    }
    //
    public String getmobile() {
        return mobile_profile;
    }

    public void setmobile(String mobile_profile) {
        this.mobile_profile = mobile_profile;
    }

    public String getstand() {
        return stand_profile;
    }

    public void setstand(String stand_profile) {
        this.stand_profile = stand_profile;
    }

    public String getroute() {
        return route_profile;
    }

    public void setroute(String route_profile) {
        this.route_profile = route_profile;
    }
    public String getcollege() {
        return college_profile;
    }

    public void setcollege(String college_profile) {
        this.college_profile = college_profile;
    }

}
