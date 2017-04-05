package at.sw2017.q_up;

/**
 * Created by tinag on 29.3.2017..
 */

public class Place {
    public String placeId;
    public String placeName;
    public String latitude;
    public String longitude;
    public String avgRating;
    public String avgProcessingSecs;

    public Place() {

    }

    public Place(String id, String name, String lat, String lon, String avgr, String proctime) {
        placeId = id;
        placeName = name;
        latitude = lat;
        longitude = lon;
        avgRating = avgr;
        avgProcessingSecs = proctime;
    }
}
