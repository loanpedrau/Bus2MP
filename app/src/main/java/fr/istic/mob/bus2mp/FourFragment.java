package fr.istic.mob.bus2mp;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class FourFragment extends Fragment {

    private Activity mainActivity;
    private int stop_id;
    private String hour;
    private ArrayAdapter<String> adapter;

    public FourFragment(Activity activity, int stop_id, String hour){
        super();
        this.stop_id = stop_id;
        this.hour = hour;
        this.mainActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.four_fragment, container, false);
        ListView listViewStopHours = view.findViewById(R.id.listViewStopHours);

        Uri stopTimeURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop_time");
        Cursor stopTimeCursor = mainActivity.getContentResolver().query(stopTimeURI, null, "departure_time=\""+this.hour+"\" AND stop_id="+stop_id, null, null);
        stopTimeCursor.moveToNext();
        long trip_id = stopTimeCursor.getLong(0);
        int stop_sequence = stopTimeCursor.getInt(4);
        stopTimeCursor.close();

        ArrayList<String> stopWithHour = new ArrayList<>();
        Cursor stopTimeForTripCursor = mainActivity.getContentResolver().query(stopTimeURI, null, "trip_id="+trip_id+" AND stop_sequence >="+stop_sequence, null, "stop_sequence");
        while (stopTimeForTripCursor.moveToNext()){
            String timeBus = stopTimeForTripCursor.getString(2);
            int stop_id = stopTimeForTripCursor.getInt(3);
            Uri stopURI = Uri.parse("content://fr.istic.mob.busmp.provider.StarProvider/stop");
            Cursor stopCursor = mainActivity.getContentResolver().query(stopURI, null, "stop_id="+stop_id, null, null);
            stopCursor.moveToNext();
            String stopName = stopCursor.getString(2);
            stopCursor.close();
            stopWithHour.add(stopName+" - "+timeBus);
        }
        stopTimeForTripCursor.close();

        adapter =new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_list_item_1,
                stopWithHour);
        listViewStopHours.setAdapter(adapter);
        return view;
    }
}

