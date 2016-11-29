package com.wearable.fitnessapp;


import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class PortraitFragment extends Fragment{
    private boolean startworkout=false;
    private TextView txt;
    private  Gmap gmap;
    TextView secondstv;
    TextView stepstv;
    private double totalsteps=0;

    public interface ButtonClicked{
        public void sendText(boolean clicked);
    }
    public void receive(boolean clicked){
        startworkout=clicked;
        gmap.receive(startworkout);

    }
    public void senddata(double a ){
        totalsteps=a;
        stepstv.setText(String.format("%1$.4f",totalsteps/2000));
    }
    public void setTime(int minutes, int seconds){
        secondstv.setText(String.format("%d:%02d", minutes, seconds) );
    }
    public PortraitFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_potrait, container,
                false);

        FragmentManager a = getChildFragmentManager();
        gmap = (Gmap)a.findFragmentById(R.id.map);
        secondstv= (TextView)view.findViewById(R.id.textView4);
        stepstv= (TextView)view.findViewById(R.id.textView5);
        TextView txt= (TextView)view.findViewById(R.id.textView2);
        secondstv.setText("0");
        stepstv.setText(""+(int)totalsteps);


        return view;
    }




}
