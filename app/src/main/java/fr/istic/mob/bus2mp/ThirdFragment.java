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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.istic.mob.bus2mp.model.BusRoute;
import fr.istic.mob.bus2mp.model.Calendar;
import fr.istic.mob.bus2mp.model.Stop;
import fr.istic.mob.bus2mp.model.StopTime;
import fr.istic.mob.bus2mp.model.Trip;

public class ThirdFragment extends Fragment {

    private int route_id;
    private Activity mainActivity;
    private List<Stop> allStops = new ArrayList<Stop>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> hours = new ArrayList<>();
    private String stopName;
    private String direction;

    public ThirdFragment(Activity activity, int route_id, String direction, String stop, Time time, Date date){
        super();
        this.mainActivity = activity;
        this.stopName = stop;
        this.direction = direction;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        loadStopDataWithStopName(this.stopName);
        ListView listViewHours = view.findViewById(R.id.listViewHours);

        for(Stop stop : allStops){
            hours.add(stop.getStop_name());
        }

        adapter =new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1,
                hours);

        listViewHours.setAdapter(adapter);
        Stop stopFound = searchStopCorrespondingToTheBus(route_id,direction);
        System.out.println("STOP FOUND : "+stopFound.getStop_id()+" - "+stopFound.getStop_code()+" - "+stopFound.getStop_name());
        return view;
    }

    private void loadStopDataWithStopName(String stopName){
        Uri stopURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop");
        Cursor stopCursor = mainActivity.getContentResolver().query(stopURI, null, "stop_name=\""+stopName+"\"", null, null);
        try {
            while (stopCursor.moveToNext()) {
                int stop_id = stopCursor.getInt(0);
                int stop_code = stopCursor.getInt(1);
                String stop_name = stopCursor.getString(2);
                String stop_desc = stopCursor.getString(3);
                float stop_lat = stopCursor.getFloat(4);
                float stop_long = stopCursor.getFloat(5);
                String zone_id = stopCursor.getString(6);
                String stop_url = stopCursor.getString(7);
                String location_type = stopCursor.getString(8);
                String parent_station = stopCursor.getString(9);
                String stop_timezone = stopCursor.getString(10);
                int wheelchair_boarding = stopCursor.getInt(11);

                Stop stop = new Stop(stop_id, stop_code, stop_name, stop_desc, stop_lat, stop_long, zone_id,
                        stop_url, location_type, parent_station, stop_timezone, wheelchair_boarding);
                allStops.add(stop);
            }
        } finally {
            stopCursor.close();
        }
    }

    private Stop searchStopCorrespondingToTheBus(long route_id, String direction){
        boolean stopCorrespondingFind = false;
        Stop stopToFind = null;
        for(Stop stop : allStops){
            int stop_id = stop.getStop_id();
            Uri stopTimeURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop_time");
            Cursor stopTimeCursor = mainActivity.getContentResolver().query(stopTimeURI, null, "stop_id="+stop_id+"", null, null);
            try {
                stopTimeCursor.moveToNext();
                String trip_id = stopTimeCursor.getString(0);
                Uri tripURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/trip");
                Cursor tripCursor = mainActivity.getContentResolver().query(stopTimeURI, null, "trip_id="+trip_id+"", null, null);
                tripCursor.moveToNext();
                String routeID_recup = tripCursor.getString(0);
                String trip_headsign = tripCursor.getString(3);
                if((Long.valueOf(routeID_recup) == route_id) && trip_headsign.contains(direction.substring(direction.indexOf("(")+1,direction.indexOf(")")))){
                    stopToFind = stop;
                }
                tripCursor.close();
            } finally {
                stopTimeCursor.close();
            }
        }
        return stopToFind;
    }
}
