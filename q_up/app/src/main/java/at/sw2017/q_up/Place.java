package at.sw2017.q_up;

import android.support.annotation.NonNull;

public class Place implements Comparable<Place>{
    public String placeId;
    public String placeName;
    public String latitude;
    public String longitude;
    public String ratingPos;
    public String ratingNeg;
    public String avgProcessingSecs;
    public String link;
    public String opening_hours;
    public String address;

    public Place() {

    }

    public Place(Place p) {
        this(p.placeId, p.placeName, p.latitude, p.longitude, p.ratingPos, p.ratingNeg, p.avgProcessingSecs, p.link, p.opening_hours, p.address);
    }

    public Place(String id, String name, String lat, String lon, String rating_pos, String rating_neg, String processing_time, String link_place,
                 String op_hours, String ad) {
        placeId = id;
        placeName = name;
        latitude = lat;
        longitude = lon;
        ratingPos = rating_pos;
        ratingNeg = rating_neg;
        avgProcessingSecs = processing_time;
        link = link_place;
        opening_hours = op_hours;
        address = ad;

    }

    @Override
    public int compareTo(Place other) {
        return placeName.compareTo(other.placeName);
    }

}