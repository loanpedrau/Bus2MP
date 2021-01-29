package fr.istic.mob.bus2mp;

import android.app.Activity;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.istic.mob.bus2mp.model.BusRoute;

public class MainActivity extends FragmentActivity {

    private List<BusRoute> allBusRoute = new ArrayList<BusRoute>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadBusRouteData();

        if (savedInstanceState == null) {
            Fragment firstFragment = new FirstFragment(allBusRoute);
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

}