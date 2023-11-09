package com.elm.boycothelper.model;

public class HomeModel {
    private String nameCate;
    private int photoCate;

    public HomeModel(String nameCate, int photoCate) {
        this.nameCate = nameCate;
        this.photoCate = photoCate;
    }

    public String getNameCate() {
        return nameCate;
    }

    public void setNameCate(String nameCate) {
        this.nameCate = nameCate;
    }

    public int getPhotoCate() {
        return photoCate;
    }

    public void setPhotoCate(int photoCate) {
        this.photoCate = photoCate;
    }
}
