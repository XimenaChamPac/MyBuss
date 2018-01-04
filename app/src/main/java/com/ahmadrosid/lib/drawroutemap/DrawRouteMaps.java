package com.ahmadrosid.lib.drawroutemap;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;



public class DrawRouteMaps {

    private static DrawRouteMaps instance;
    private Context context;


    public static DrawRouteMaps getInstance(Context context) {
        instance = new DrawRouteMaps();
        instance.context = context;
        return instance;
    }

    public DrawRouteMaps draw(LatLng origin, LatLng destination, GoogleMap googleMap,int C,int Ru){
        String url_route = FetchUrl.getUrl(origin, destination);
        DrawRoute drawRoute = new DrawRoute(googleMap,C,Ru);
        drawRoute.execute(url_route);

        return instance;
    }

    public static Context getContext() {
        return instance.context;
    }
}
