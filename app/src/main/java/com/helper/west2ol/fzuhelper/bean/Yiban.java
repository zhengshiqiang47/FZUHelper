package com.helper.west2ol.fzuhelper.bean;

/**
 * Created by CoderQiang on 2017/9/20.
 */

public class Yiban {
    private String image;
    private String title;
    private String url;
    private boolean passby=false;

    public boolean isPassby() {
        return passby;
    }

    public void setPassby(boolean passby) {
        this.passby = passby;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
