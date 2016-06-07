package com.nodrex.connectedworld.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nodrex.android.tools.Util;
import com.nodrex.connectedworld.MainActivity;
import com.nodrex.connectedworld.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LightBalbFragment extends Fragment implements View.OnClickListener{

    MainActivity activity;

    public LightBalbFragment() {
        // Required empty public constructor
    }

    public void setActivity(MainActivity activity){
        this.activity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_light_balb, container, false);

        v.findViewById(R.id.deviceName).setOnClickListener(this);
        v.findViewById(R.id.wifiName).setOnClickListener(this);
        v.findViewById(R.id.wifiPassword).setOnClickListener(this);

        Util.log("onCreateView");

        return v;
    }

    @Override
    public void onClick(View v) {
        Util.log("Edittext is setted");
    }
}
