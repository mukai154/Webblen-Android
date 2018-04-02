package com.webblen.events.webblen;

import java.util.Date;

/**
 * Created by codelation on 3/28/18.
 */

public class Utilities {

    //Get Drawable from Name
    public static int categoryToDrawable(String category){

        switch (category){
            case "ART":
                return R.drawable.art;
            case "MUSIC":
                return R.drawable.music;
            case "FOODDRINK":
                return R.drawable.fooddrink;
            case "SPORTS":
                return R.drawable.sports;
            case "COMMUNITY":
                return R.drawable.community;
            case "HEALTHFITNESS":
                return R.drawable.healthfitness;
            case "FAMILY":
                return R.drawable.family;
            case "ENTERTAINMENT":
                return R.drawable.entertainment;
            case "TECHNOLOGY":
                return R.drawable.technology;
            case "OUTDOORS":
                return R.drawable.outdoors;
            case "GAMING":
                return R.drawable.gaming;
            case "COMPETITION":
                return R.drawable.competition;
            case "NETWORKING":
                return R.drawable.networking;
            case "THEATRE":
                return R.drawable.theatre;
            case "CULTURE":
                return R.drawable.culture;
            case "SHOPPING":
                return R.drawable.shopping;
            case "AMUSEMENT":
                return R.drawable.amusement;
            case "EDUCATION":
                return R.drawable.education;
            case "COLLEGELIFE":
                return R.drawable.collegelife;
            case "WINEBREW":
                return R.drawable.winebrew;
            case "PARTYDANCE":
                return R.drawable.partydance;
            default:
                return R.drawable.amusement;
        }

    }

    //Date Difference
    public static int getDifferenceDays(Date d1, Date d2) {
        int daysdiff = 0;
        long diff = d2.getTime() - d1.getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        daysdiff = (int) diffDays;
        return daysdiff;
    }

    //Return Month String
    public static String getMonth(String datePart) {
        switch (datePart){
            case "01":
                return "Jan";
            case "02":
                return "Feb";
            case "03":
                return "Mar";
            case "04":
                return "Apr";
            case "05":
                return "May";
            case "06":
                return "Jun";
            case "07":
                return "Jul";
            case "08":
                return "Aug";
            case "09":
                return "Sep";
            case "10":
                return "Oct";
            case "11":
                return "Nov";
            case "12":
                return "Dec";
            default:
                return "Mar";
        }
    }

    //Return Price
    public static String getPrice(int eventRadius) {
        if (eventRadius == 250){
            return "2.99";
        } else if (eventRadius > 250 && eventRadius < 400){
            return "4.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "9.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "14.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "19.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "24.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "26.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "29.99";
        } else if (eventRadius > 375 && eventRadius < 600){
            return "34.99";
        } else {
            return "0.00";
        }
    }
}
