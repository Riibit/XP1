package at.sw2017.q_up;

/**
 * Created by tinag on 29.3.2017..
 */

public class Place {
    private Integer placeId;
    private String placeName;
    private Float latitude;
    private Float longitude;
    private Float avgRating;
    private Integer avgProcessingSecs;

    public Place(Integer id, String name, Float lat, Float lon, Float avgR, Integer proctime) {
        placeId = id;
        placeName = name;
        latitude = lat;
        longitude = lon;
        avgRating = avgR;
        avgProcessingSecs = proctime;
    }
}
