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
import androidx.fragment.app.FragmentTransaction;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import fr.istic.mob.bus2mp.model.Stop;

public class ThirdFragment extends Fragment {

    private int route_id;
    private Activity mainActivity;
    private List<Stop> allStops = new ArrayList<Stop>();
    private ArrayAdapter<String> adapter;
    private ArrayList<String> hours = new ArrayList<>();
    private String stopName;
    private String direction;
    private Thread loadHours;
    private Time time;
    private Date date;
    private int stopIdForFragment4;

    public ThirdFragment(Activity activity, int route_id, String direction, String stop, Time time, Date date){
        super();
        this.mainActivity = activity;
        this.stopName = stop;
        this.direction = direction;
        this.route_id = route_id;
        this.time = time;
        this.date = date;
        this.stopIdForFragment4 =0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);
        if(hours.size()==0) {
            loadStopDataWithStopName(this.stopName);
            initThreadSerchHours(route_id, direction);
            loadHours.start();
            ListView listViewHours = view.findViewById(R.id.listViewHours);
            try {
                loadHours.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            hours = keepHoursOnlyAfterMySettings();
            adapter = new ArrayAdapter<String>(mainActivity,
                    android.R.layout.simple_list_item_1,
                    hours);

            listViewHours.setAdapter(adapter);
            listViewHours.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String h = hours.get(position);
                            FourFragment fourFragment = new FourFragment(mainActivity, stopIdForFragment4, h);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out  // popExit
                            );
                            ft.replace(R.id.fragmentToDisplay, fourFragment).addToBackStack(null).commit();
                        }
                    }
            );
        }else{
            ListView listViewHours = view.findViewById(R.id.listViewHours);
            adapter = new ArrayAdapter<String>(mainActivity,
                    android.R.layout.simple_list_item_1,
                    hours);

            listViewHours.setAdapter(adapter);
            listViewHours.setOnItemClickListener(
                    new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String h = hours.get(position);
                            FourFragment fourFragment = new FourFragment(mainActivity, stopIdForFragment4, h);
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out  // popExit
                            );
                            ft.replace(R.id.fragmentToDisplay, fourFragment).addToBackStack(null).commit();
                        }
                    }
            );
        }
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

    private void initThreadSerchHours(final int route_id, final String direction){
        loadHours = new Thread(new Runnable() {
            public void run() {
            String stopNameDirection = direction.substring(direction.indexOf("(") + 1, direction.indexOf(")")).trim();
            List<String> stopTimes = new ArrayList<>();
            boolean alreadyFill = false;
            for(Stop stop : allStops) {
                int stop_id = stop.getStop_id();
                Uri tripURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/trip");
                Cursor tripWithSameRouteIDCursor = mainActivity.getContentResolver().query(tripURI, null, "route_id=" + route_id+" AND trip_headsign LIKE \'%"+stopNameDirection+"%\'", null, null);
                tripWithSameRouteIDCursor.moveToNext();
                long service_id = tripWithSameRouteIDCursor.getLong(1);
                tripWithSameRouteIDCursor.close();
                Cursor tripWithSameRouteIDCursor2 = mainActivity.getContentResolver().query(tripURI,null, "route_id=" + route_id+" AND trip_headsign LIKE \'%"+stopNameDirection+"%\' AND service_id="+service_id, null, null);
                while(tripWithSameRouteIDCursor2.moveToNext() && !alreadyFill){
                    long trip_id = tripWithSameRouteIDCursor2.getLong(2);
                    Uri stopTimeURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop_time");
                    Cursor stopTimeCursor = mainActivity.getContentResolver().query(stopTimeURI, null, "trip_id=" + trip_id+" AND stop_id="+stop_id, null, null);
                    while(stopTimeCursor.moveToNext()) {
                        hours.add(stopTimeCursor.getString(2));
                    }
                    stopIdForFragment4 = stop_id;
                    stopTimeCursor.close();
                }
                if(hours.size()>0){
                    alreadyFill = true;
                }
                tripWithSameRouteIDCursor2.close();
            }
            }
        });
    }

    private ArrayList<String> keepHoursOnlyAfterMySettings(){
        ArrayList<Time> hoursToKeep = new ArrayList<>();
        for(String hour : hours){
            String[] data = hour.split(":");
            String theHour = data[0];
            String min = data[1];
            Time t = new Time(Integer.valueOf(theHour), Integer.valueOf(min), 0);
            if(t.after(time)){
                hoursToKeep.add(t);
            }
        }
        Collections.sort(hoursToKeep, new Comparator<Time>(){
            public int compare(Time time1, Time time2){
                return time1.after(time2) ? 1 : -1;
            }
        });
        ArrayList<String> hoursToKeepStr = new ArrayList<>();
        for(Time t : hoursToKeep){
            hoursToKeepStr.add(t.toString());
        }
        return hoursToKeepStr;
    }
}
