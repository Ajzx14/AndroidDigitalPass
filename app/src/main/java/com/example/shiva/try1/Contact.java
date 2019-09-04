package com.example.shiva.try1;

import android.graphics.Bitmap;

/**
 * Created by ravi on 16/11/17.
 */

public class Contact {
    String name;
    Bitmap image;
    String phone;
    String college;
    public Contact() {
    }

    public Contact(String name, Bitmap image, String phone,String college) {
        this.name = name;
        this.image = image;
        this.phone = phone;
        this.college = college;
    }

    public String getName() {
        return name;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Bitmap getImage() {
        return image;
    }

    public String getPhone() {
        return phone;
    }
}
