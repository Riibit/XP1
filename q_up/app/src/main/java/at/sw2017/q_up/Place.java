package at.sw2017.q_up;

/**
 * Created by tinag on 29.3.2017..
 */

public class Place {
    public String placeId;
    public String placeName;
    public String latitude;
    public String longitude;
    public String ratingPos;
    public String ratingNeg;
    public String avgProcessingSecs;

    public Place() {

    }

    public Place(Place p) {
        this(p.placeId, p.placeName, p.latitude, p.longitude, p.ratingPos, p.ratingNeg, p.avgProcessingSecs);
    }

    public Place(String id, String name, String lat, String lon, String rating_pos, String rating_neg, String proctime) {
        placeId = id;
        placeName = name;
        latitude = lat;
        longitude = lon;
        ratingPos = rating_pos;
        ratingNeg = rating_neg;
        avgProcessingSecs = proctime;
    }
}
