package com.webblen.events.webblen.Objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by codelation on 3/26/18.
 */
@SuppressWarnings("serial")
public class WebblenEvent implements Serializable{

    private String address;
    private String author;
    private ArrayList<String> categories;
    private String date;
    private String description;
    private int distanceFromUser;
    private boolean event18;
    private boolean event21;
    private String eventKey;
    private Double lat;
    private Double lon;
    private boolean notificationOnly;
    private String pathToImage;
    private int radius;
    private String time;
    private String title;
    private int views;


    public WebblenEvent(){

    }

    public WebblenEvent(String address,
                        String author,
                        ArrayList<String> categories,
                        String date,
                        String description,
                        int distanceFromUser,
                        boolean event18,
                        boolean event21,
                        String eventKey,
                        Double lat,
                        Double lon,
                        boolean notificationOnly,
                        String pathToImage,
                        int radius,
                        String time,
                        String title,
                        int views){

        this.address = address;
        this.author = author;
        this.categories = categories;
        this.date = date;
        this.description = description;
        this.distanceFromUser = distanceFromUser;
        this.event18 = event18;
        this.event21 = event21;
        this.eventKey = eventKey;
        this.lat = lat;
        this.lon = lon;
        this.notificationOnly = notificationOnly;
        this.pathToImage = pathToImage;
        this.radius = radius;
        this.time = time;
        this.title = title;
        this.views = views;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArrayList<String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<String> categories) {
        this.categories = categories;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDistanceFromUser() {
        return distanceFromUser;
    }

    public void setDistanceFromUser(int distanceFromUser) {
        this.distanceFromUser = distanceFromUser;
    }

    public boolean isEvent18() {
        return event18;
    }

    public void setEvent18(boolean event18) {
        this.event18 = event18;
    }

    public boolean isEvent21() {
        return event21;
    }

    public void setEvent21(boolean event21) {
        this.event21 = event21;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public boolean isNotificationOnly() {
        return notificationOnly;
    }

    public void setNotificationOnly(boolean notificationOnly) {
        this.notificationOnly = notificationOnly;
    }

    public String getPathToImage() {
        return pathToImage;
    }

    public void setPathToImage(String pathToImage) {
        this.pathToImage = pathToImage;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public String toString(){
        return "Title: " + title + " Description: " + description + " Categories: " + categories + " Date: " + date +
                " Time: " + time + " Address: " + address + " Views: " + views ;
    }


}
