package fr.istic.mob.bus2mp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.istic.mob.bus2mp.model.Stop;
import fr.istic.mob.bus2mp.model.StopTime;
import fr.istic.mob.bus2mp.model.Trip;

public class SecondFragment extends Fragment {

    private int route_id;
    private List<Stop> stops;
    private List<StopTime> stopTimes = new ArrayList<StopTime>();
    private List<Trip> trips;
    private Activity mainActivity;
    private ArrayList<String> stopNames = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private Thread loadStopTimeData;
    private String direction;
    private Time time;
    private Date date;

    public SecondFragment(Activity activity, int route_id, List<Stop> stops, List<Trip> trips, String direction, Time time, Date date){
        this.route_id = route_id;
        this.stops = stops;
        this.trips = trips;
        this.mainActivity = activity;
        this.direction = direction;
        this.time = time;
        this.date = date;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.second_fragment, container, false);
        long trip_id = getTripWithRouteId(route_id);
        loadStopTimeData(trip_id);
        loadStopTimeData.start();
        try {
            loadStopTimeData.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayList<Stop> stopFromStopsTimes = getStopFromStopsTimes(stopTimes);
        for(Stop stop : stopFromStopsTimes){
            stopNames.add(stop.getStop_name());
        }
        ListView listViewStops = view.findViewById(R.id.listViewStops);


        adapter =new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1,
                stopNames);

        listViewStops.setAdapter(adapter);

        listViewStops.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String stopNameClicked = stopNames.get(position);
                        ThirdFragment thirdFragment = new ThirdFragment(mainActivity,route_id, direction, stopNameClicked, time, date);

                    }
                }
        );
        return view;
    }

    private long getTripWithRouteId(int route_id){
        long trip_id = -1;
        for(Trip trip : trips){
            try {
                if (route_id == Integer.valueOf(trip.getRoute_id())) {
                    trip_id = Long.valueOf(trip.getTrip_id());
                    return trip_id;
                }
            }catch (NumberFormatException nfe){}
        }
        return trip_id;
    }

    private ArrayList<StopTime> getStopTimeofTrips(ArrayList<Trip> allTrips){
        ArrayList<StopTime> stopTimesCorrespondingOfTrips = new ArrayList<>();
        for(Trip trip : allTrips){
            for(StopTime stopTime : stopTimes){
                if(trip.getTrip_id().equals(stopTime.getTrip_id())){
                    stopTimesCorrespondingOfTrips.add(stopTime);
                }
            }
        }
        return stopTimesCorrespondingOfTrips;
    }

    private ArrayList<Stop> getStopFromStopsTimes(List<StopTime> allStopTimes){
        ArrayList<Stop> stopOfStopTimes = new ArrayList<>();
        for(StopTime stopTime : allStopTimes){
            for(Stop stop : stops){
                if(stop.getStop_id() == Integer.valueOf(stopTime.getStop_id())){
                    stopOfStopTimes.add(stop);
                }
            }
        }
        return stopOfStopTimes;
    }

    private void loadStopTimeData(final long trip_id){
        loadStopTimeData = new Thread(new Runnable() {
            public void run() {
                Uri stopTimeURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop_time");
                Cursor stopTimeCursor = mainActivity.getContentResolver().query(stopTimeURI, null, "trip_id="+trip_id, null, null);
                try {
                    while (stopTimeCursor.moveToNext()) {
                        String trip_id = stopTimeCursor.getString(0);
                        String arrival_time = stopTimeCursor.getString(1);
                        String departure_time = stopTimeCursor.getString(2);
                        String stop_id = stopTimeCursor.getString(3);
                        String stop_sequence = stopTimeCursor.getString(4);
                        String stop_headsign = stopTimeCursor.getString(5);
                        String pickup_type = stopTimeCursor.getString(6);
                        String drop_off_type = stopTimeCursor.getString(7);
                        String shape_dist_traveled = stopTimeCursor.getString(8);

                        StopTime stopTime = new StopTime(trip_id,arrival_time, departure_time, stop_id, stop_sequence,
                                stop_headsign, pickup_type, drop_off_type, shape_dist_traveled);
                        stopTimes.add(stopTime);

                    }
                } finally {
                    stopTimeCursor.close();
                }
            }
        });

    }
}
