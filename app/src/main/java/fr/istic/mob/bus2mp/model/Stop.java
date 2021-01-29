package fr.istic.mob.bus2mp.model;

public class Stop {

    private int stop_id;
    private int stop_code;
    private String stop_name;
    private String stop_desc;
    private float stop_lat;
    private float stop_lon;
    private String zone_id;
    private String stop_url;
    private String location_type;
    private String parent_station;
    private String stop_timezone;
    private int wheelchair_boarding;


    public Stop(int stop_id, int stop_code, String stop_name, String stop_desc, float stop_lat,
                float stop_lon, String zone_id, String stop_url, String location_type,
                String parent_station, String stop_timezone, int wheelchair_boarding) {
        this.stop_id = stop_id;
        this.stop_code = stop_code;
        this.stop_name = stop_name;
        this.stop_desc = stop_desc;
        this.stop_lat = stop_lat;
        this.stop_lon = stop_lon;
        this.zone_id = zone_id;
        this.stop_url = stop_url;
        this.location_type = location_type;
        this.parent_station = parent_station;
        this.stop_timezone = stop_timezone;
        this.wheelchair_boarding = wheelchair_boarding;
    }

    public int getStop_id() {
        return stop_id;
    }

    public void setStop_id(int stop_id) {
        this.stop_id = stop_id;
    }

    public int getStop_code() {
        return stop_code;
    }

    public void setStop_code(int stop_code) {
        this.stop_code = stop_code;
    }

    public String getStop_name() {
        return stop_name;
    }

    public void setStop_name(String stop_name) {
        this.stop_name = stop_name;
    }

    public String getStop_desc() {
        return stop_desc;
    }

    public void setStop_desc(String stop_desc) {
        this.stop_desc = stop_desc;
    }

    public float getStop_lat() {
        return stop_lat;
    }

    public void setStop_lat(float stop_lat) {
        this.stop_lat = stop_lat;
    }

    public float getStop_lon() {
        return stop_lon;
    }

    public void setStop_lon(float stop_lon) {
        this.stop_lon = stop_lon;
    }

    public String getZone_id() {
        return zone_id;
    }

    public void setZone_id(String zone_id) {
        this.zone_id = zone_id;
    }

    public String getStop_url() {
        return stop_url;
    }

    public void setStop_url(String stop_url) {
        this.stop_url = stop_url;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getParent_station() {
        return parent_station;
    }

    public void setParent_station(String parent_station) {
        this.parent_station = parent_station;
    }

    public String getStop_timezone() {
        return stop_timezone;
    }

    public void setStop_timezone(String stop_timezone) {
        this.stop_timezone = stop_timezone;
    }

    public int getWheelchair_boarding() {
        return wheelchair_boarding;
    }

    public void setWheelchair_boarding(int wheelchair_boarding) {
        this.wheelchair_boarding = wheelchair_boarding;
    }


}
