package com.jobayerjim.minecraftskins.models;

public class SkinsModel {
    private int id;
    private String image_name;
    private String res_name;
    private int viewed;
    private int liked;
    private int downloaded;
    private int favourite;

    public SkinsModel(int id, String image_name, String res_name, int viewed, int liked, int downloaded, int favourite) {
        this.id = id;
        this.image_name = image_name;
        this.res_name = res_name;
        this.viewed = viewed;
        this.liked = liked;
        this.downloaded = downloaded;
        this.favourite = favourite;
    }

    public int getId() {
        return id;
    }

    public String getImage_name() {
        return image_name;
    }

    public String getRes_name() {
        return res_name;
    }

    public int getViewed() {
        return viewed;
    }

    public int getLiked() {
        return liked;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public int getFavourite() {
        return favourite;
    }

    public void setFavourite(int favourite) {
        this.favourite = favourite;
    }
}
