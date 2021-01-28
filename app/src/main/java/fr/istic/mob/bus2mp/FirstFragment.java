package fr.istic.mob.bus2mp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class FirstFragment extends Fragment {

    private Spinner spinnerLigne;
    private Spinner spinnerDirection;
    private ArrayList<String> dataLines;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
        Button switchFragmentButton = view.findViewById(R.id.button);
        switchFragmentButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment secondFragment = new SecondFragment();
                        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.fragmentToDisplay, secondFragment).commit();
                    }
                }
        );

        dataLines = new ArrayList<String>();
        spinnerLigne = (Spinner) view.findViewById(R.id.spinnerLine);
        spinnerDirection = (Spinner) view.findViewById(R.id.spinnerDirection);

        //remplir le dataline ici

        ArrayList<String> titleLines = new ArrayList<String>();
        for(String line : dataLines){
            String[] data = line.split(",");
            titleLines.add(data[0]+","+data[1]);
        }
        CustomAdapter dataAdapter = new CustomAdapter(getContext(),titleLines);
        spinnerLigne.setAdapter(dataAdapter);

        spinnerLigne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> directions = new ArrayList<String>();
                String line = dataLines.get(position);
                String[] data = line.split(",");
                directions.add(data[2]);
                directions.add(data[3]);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item,directions);
                spinnerDirection.setAdapter(dataAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
    }
}
