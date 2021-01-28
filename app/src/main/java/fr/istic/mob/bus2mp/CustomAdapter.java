package fr.istic.mob.bus2mp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    ArrayList<String> titleAndColor;
    Context context;

    public CustomAdapter(Context context, ArrayList<String> titleAndColor){
        this.context=context;
        this.titleAndColor = titleAndColor;
    }

    @Override
    public int getCount(){
        return titleAndColor.size();
    }

    @Override
    public Object getItem(int arg0){
        return titleAndColor.get(arg0);
    }
    @Override
    public long getItemId(int arg0){
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view= inflater.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        TextView txv=(TextView) view.findViewById(android.R.id.text1);
        txv.setBackgroundColor(Color.parseColor("#"+titleAndColor.get(position).split(",")[1]));
        txv.setTextSize(20f);
        txv.setText(titleAndColor.get(position).split(",")[0]);
        return view;
    }


}
