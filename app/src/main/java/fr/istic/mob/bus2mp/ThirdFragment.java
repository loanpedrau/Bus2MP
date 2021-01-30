package fr.istic.mob.bus2mp;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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

    public ThirdFragment(Activity activity, int route_id, String direction, String stop, Time time, Date date){
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.third_fragment, container, false);


        return view;
    }

}
