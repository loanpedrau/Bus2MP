package fr.istic.mob.bus2mp;


import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.util.ArrayList;
import java.util.List;
import fr.istic.mob.bus2mp.model.BusRoute;
import fr.istic.mob.bus2mp.model.Calendar;
import fr.istic.mob.bus2mp.model.Stop;
import fr.istic.mob.bus2mp.model.Trip;

public class MainActivity extends FragmentActivity {

    private List<BusRoute> allBusRoute = new ArrayList<BusRoute>();
    private List<Stop> allStops = new ArrayList<Stop>();
    private List<Trip> allTrips = new ArrayList<>();
    private List<Calendar> allCalendar = new ArrayList<>();

    private String[] STOPS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadBusRouteData();
        loadStopData();
        loadTripData();

        STOPS = getAllStopsName();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, STOPS);
        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);

        if (savedInstanceState == null) {
            Fragment firstFragment = new FirstFragment(this,allBusRoute,allStops,allTrips, allCalendar );
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragmentToDisplay, firstFragment, "firstFragment").addToBackStack(null).commit();

        }
    }

    private void loadBusRouteData(){
        Uri busRouteURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/bus_route");
        Cursor busRouteCursor = getContentResolver().query(busRouteURI, null, null, null, null);
        try {
            while (busRouteCursor.moveToNext()) {
                int route_id = busRouteCursor.getInt(0);
                int agency_id = busRouteCursor.getInt(1);
                String route_short_name = busRouteCursor.getString(2);
                String route_long_name = busRouteCursor.getString(3);
                String route_desc = busRouteCursor.getString(4);
                int route_type = busRouteCursor.getInt(5);
                String route_url = busRouteCursor.getString(6);
                String route_color = busRouteCursor.getString(7);
                String route_text_color = busRouteCursor.getString(8);
                int route_sort_order = busRouteCursor.getInt(9);
                BusRoute busRoute = new BusRoute(route_id,agency_id,route_short_name,route_long_name,route_desc,
                        route_type,route_url,route_color,route_text_color,route_sort_order);
                allBusRoute.add(busRoute);

            }
        } finally {
            busRouteCursor.close();
        }
    }

    private void loadStopData(){
        Uri stopURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop");
        Cursor stopCursor = getContentResolver().query(stopURI, null, null, null, null);
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

    private void loadTripData(){
        Uri stopURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/trip");
        Cursor tripCursor = getContentResolver().query(stopURI, null, null, null, null);
        for(String name : tripCursor.getColumnNames()){
            System.out.println(name);
        }
        try {
            while (tripCursor.moveToNext()) {
                String route_id = tripCursor.getString(0);
                String service_id = tripCursor.getString(1);
                String trip_id = tripCursor.getString(2);
                String trip_headsign = tripCursor.getString(3);
                String trip_short_name = tripCursor.getString(4);
                String direction_id = tripCursor.getString(5);
                String block_id = tripCursor.getString(6);
                String shape_id = tripCursor.getString(7);
                String wheelchair_accessible = tripCursor.getString(8);
                String bikes_allowed = tripCursor.getString(9);

                Trip trip = new Trip(route_id,service_id, trip_id, trip_headsign,trip_short_name,
                        direction_id, block_id, shape_id, wheelchair_accessible, bikes_allowed);
                allTrips.add(trip);

            }
        } finally {
            tripCursor.close();
        }
    }

    private String[] getAllStopsName(){
        ArrayList<String> stops = new ArrayList<>();
        for(Stop stop : allStops){
            if(!stops.contains(stop.getStop_name())){
                stops.add(stop.getStop_name());
            }
        }
        String[] stopNames = new String[stops.size()];
        int i=0;
        for(String name : stops){
            stopNames[i] = name;
            i++;
        }
        return stopNames;
    }

}