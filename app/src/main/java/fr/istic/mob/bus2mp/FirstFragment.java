package fr.istic.mob.bus2mp;

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

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.bus2mp.model.BusRoute;

public class FirstFragment extends Fragment {

    private Spinner spinnerLigne;
    private Spinner spinnerDirection;
    private List<BusRoute> allBusRoute;
    private boolean dateSelected = false;
    private boolean timeSelected = false;
    private boolean directionSelected = false;

    public FirstFragment(List<BusRoute> allBusRoute){
        super();
        this.allBusRoute = allBusRoute;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        Button switchFragmentButton = view.findViewById(R.id.nextButton);
        switchFragmentButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dateSelected && timeSelected && directionSelected) {
                            Fragment secondFragment = new SecondFragment();
                            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(
                                    R.anim.slide_in,  // enter
                                    R.anim.fade_out,  // exit
                                    R.anim.fade_in,   // popEnter
                                    R.anim.slide_out  // popExit
                            );
                            ft.replace(R.id.fragmentToDisplay, secondFragment).addToBackStack(null).commit();
                        }
                    }
                }
        );

        spinnerLigne = (Spinner) view.findViewById(R.id.spinnerLine);
        spinnerDirection = (Spinner) view.findViewById(R.id.spinnerDirection);

        ArrayList<String> titleLines = new ArrayList<String>();
        for(BusRoute busRoute : allBusRoute){
            titleLines.add(busRoute.getRoute_short_name()+","+busRoute.getRoute_color());
        }
        CustomAdapter dataAdapter = new CustomAdapter(getContext(),titleLines);
        spinnerLigne.setAdapter(dataAdapter);

        spinnerLigne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    ArrayList<String> directions = new ArrayList<String>();
                    BusRoute busRoute = allBusRoute.get(position);
                    String[] data = busRoute.getRoute_long_name().split("<>");
                    directions.add(data[0]);
                    directions.add(data[data.length-1]);
                    ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                            android.R.layout.simple_spinner_item,directions);
                    spinnerDirection.setAdapter(dataAdapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { }
        });
        spinnerDirection.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        directionSelected = true;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        directionSelected = false;
                    }
                }
        );

        final TimePicker timePicker = view.findViewById(R.id.timePicker);
        timePicker.setOnTimeChangedListener(
                new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                        timeSelected = true;
                    }
                }
        );

        final DatePicker datePicker = view.findViewById(R.id.datePicker);
        datePicker.setOnDateChangedListener(
                new DatePicker.OnDateChangedListener() {
                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        dateSelected = true;
                    }
                }
        );

        return view;
    }

    private void switchToFragment2(){
        Fragment secondFragment = new SecondFragment();
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentToDisplay, secondFragment).addToBackStack(null).commit();
    }
}
