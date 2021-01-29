package fr.istic.mob.bus2mp.model;

public class Calendar {

    private int service_id;
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private String start_date;
    private String end_date;


    public Calendar(int service_id,
                    int monday,
                    int tuesday,
                    int wednesday,
                    int thursday,
                    int friday,
                    int saturday,
                    int sunday,
                    String start_date,
                    String end_date){
        this.service_id = service_id;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getFriday() {
        return friday;
    }

    public int getMonday() {
        return monday;
    }

    public int getSaturday() {
        return saturday;
    }

    public int getService_id() {
        return service_id;
    }

    public int getSunday() {
        return sunday;
    }

    public int getThursday() {
        return thursday;
    }

    public int getTuesday() {
        return tuesday;
    }

    public int getWednesday() {
        return wednesday;
    }

    public String getEnd_date() {
        return end_date;
    }

    public String getStart_date() {
        return start_date;
    }
}
