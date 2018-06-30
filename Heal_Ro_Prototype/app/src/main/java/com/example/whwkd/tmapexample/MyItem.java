package com.example.whwkd.tmapexample;

import android.graphics.drawable.Drawable;

public class MyItem {

    private Drawable icon;
    private String name;
    private String contents;

    private Drawable icon2;
    private String KM;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon2() {
        return icon2;
    }

    public void setIcon2(Drawable icon2) {
        this.icon2 = icon2;
    }

    public String getKM() {
        return KM;
    }

    public void setKM(String KM) {
        this.KM = KM;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

}

