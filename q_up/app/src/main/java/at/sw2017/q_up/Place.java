package at.sw2017.q_up;

public class Place {
    public String placeId;
    public String placeName;
    public String latitude;
    public String longitude;
    public String ratingPositive;
    public String ratingNegative;
    public String avgProcessingSecs;

    public Place() {

    }

    public Place(Place p) {
        this(p.placeId, p.placeName, p.latitude, p.longitude, p.ratingPositive, p.ratingNegative, p.avgProcessingSecs);
    }

    public Place(String id, String name, String lat, String lon, String rating_pos, String rating_neg, String processing_time) {
        placeId = id;
        placeName = name;
        latitude = lat;
        longitude = lon;
        ratingPositive = rating_pos;
        ratingNegative = rating_neg;
        avgProcessingSecs = processing_time;
    }
}