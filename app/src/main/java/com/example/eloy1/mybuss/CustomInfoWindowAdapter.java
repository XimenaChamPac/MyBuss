package com.example.eloy1.mybuss;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by eloy1 on 4/01/2018.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
    private String dato1,dato2,dato3;

    public CustomInfoWindowAdapter(LayoutInflater inflater,String dat1,String dat2, String dat3){
        this.inflater = inflater;
        this.dato1=dat1;
        this.dato2=dat2;
        this.dato3=dat3;
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.infowindow_layout, null);
        String[] info = m.getTitle().split("&");
        String url = m.getSnippet();
        ((TextView)v.findViewById(R.id.info_window_nombre)).setText(dato1);
        ((TextView)v.findViewById(R.id.info_window_placas)).setText(dato2);
        ((TextView)v.findViewById(R.id.info_window_estado)).setText(dato3);
        return v;
    }


    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
}