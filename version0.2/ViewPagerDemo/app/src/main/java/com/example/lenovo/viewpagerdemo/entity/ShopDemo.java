package com.example.lenovo.viewpagerdemo.entity;

/**
 * Created by lenovo on 2018/5/25.
 */

public class ShopDemo {
    private int shopdId;
    private String shopdName;
    private String shopimg;
    private double lat;
    private double lng;

    public int getShopdId() {
        return shopdId;
    }

    public void setShopdId(int shopdId) {
        this.shopdId = shopdId;
    }

    public String getShopdName() {
        return shopdName;
    }

    public void setShopdName(String shopdName) {
        this.shopdName = shopdName;
    }

    public String getShopimg() {
        return shopimg;
    }

    public void setShopimg(String shopimg) {
        this.shopimg = shopimg;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
