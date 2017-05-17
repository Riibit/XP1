package at.sw2017.q_up;

public class Place {
    private String placeId;
    private String placeName;
    private String latitude;
    private String longitude;
    private String ratingPos;
    private String ratingNeg;
    private String avgProcessingSecs;

    public Place() {

    }

    public Place(Place p) {
        this(p.placeId, p.placeName, p.latitude, p.longitude, p.ratingPos, p.ratingNeg, p.avgProcessingSecs);
    }

    public Place(String id, String name, String lat, String lon, String rating_pos, String rating_neg, String processing_time) {
        placeId = id;
        placeName = name;
        latitude = lat;
        longitude = lon;
        ratingPos = rating_pos;
        ratingNeg = rating_neg;
        avgProcessingSecs = processing_time;
    }
}
