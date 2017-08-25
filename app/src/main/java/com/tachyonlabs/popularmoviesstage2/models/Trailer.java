package com.tachyonlabs.popularmoviesstage2.models;

public class Trailer {
    String name;
    String site;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {

        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Trailer() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
