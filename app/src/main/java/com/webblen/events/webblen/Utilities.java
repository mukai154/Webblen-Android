package com.webblen.events.webblen;

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
}
