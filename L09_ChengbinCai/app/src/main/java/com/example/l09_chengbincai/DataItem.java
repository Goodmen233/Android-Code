package com.example.l09_chengbincai;

public class DataItem {

    public DataItem(String name, boolean favorite) {
        this.name = name;
        this.favorite = favorite;
    }

    private String name;
    private boolean favorite;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
}
