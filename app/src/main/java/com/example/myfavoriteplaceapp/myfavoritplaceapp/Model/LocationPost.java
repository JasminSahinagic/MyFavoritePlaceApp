package com.example.myfavoriteplaceapp.myfavoritplaceapp.Model;

import java.io.Serializable;

public class LocationPost implements Serializable{

    private String address;
    private String attributes;
    private String id;
    private String latLng;
    private String locale;
    private String name;
    private String phoneNumber;
    private String placeType;
    private String priceLevel;
    private String rating;
    private String image;
    private String userID;
    private String postDate;
    private String userInput;


    public LocationPost() {
    }

    public LocationPost(String address, String attributes, String id, String latLng, String locale, String name, String phoneNumber, String placeType, String priceLevel, String rating, String image, String userID, String postDate, String userInput) {
        this.address = address;
        this.attributes = attributes;
        this.userInput = userInput;
        this.id = id;
        this.latLng = latLng;
        this.locale=locale;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.placeType = placeType;
        this.priceLevel = priceLevel;
        this.rating = rating;
        this.image = image;
        this.userID = userID;
        this.postDate = postDate;
    }

    public String getUserInput() {
        return userInput;
    }

    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatLng() {
        return latLng;
    }

    public void setLatLng(String latLng) {
        this.latLng = latLng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }

    public String getPriceLevel() {
        return priceLevel;
    }

    public void setPriceLevel(String priceLevel) {
        this.priceLevel = priceLevel;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }


}
