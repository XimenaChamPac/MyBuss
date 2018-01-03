package com.ahmadrosid.lib.drawroutemap;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eloy1.mybuss.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private PolylineOptions lineOptions;
    private GoogleMap mMap;
    private int routeColor;
    private int tipoColor=2;



    public RouteDrawerTask(GoogleMap mMap,int C) {

        this.mMap = mMap;
        this.tipoColor=C;

    }


    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("RouteDrawerTask", jsonData[0]);
            DataRouteParser parser = new DataRouteParser();
            Log.d("RouteDrawerTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);
            Log.d("RouteDrawerTask", "Executing routes");
            Log.d("RouteDrawerTask", routes.toString());

        } catch (Exception e) {
            Log.d("RouteDrawerTask", e.toString());
            e.printStackTrace();
        }
        return routes;
    }

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        if (result != null)
            drawPolyLine(result);
    }

    private void drawPolyLine(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        lineOptions = null;

        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);
                //////////// aqui creo que pondre para que genere el archivo
                //escritura de las posiciones dentro del fichero

                String nomarchivo = "ruta1.txt";


                try {
                    File tarjeta = Environment.getExternalStorageDirectory();
                    File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
                    OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true));//el true te sierve para mantener la informacion anterior
                    //agrego las posicones al fichero
                    osw.append("\n");
                    osw.append(point.get("lat"));
                    osw.append("\n");
                    osw.append(point.get("lng"));
                    osw.flush();
                    osw.close();


                } catch (IOException ioe) {
                }

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(6);

           if(tipoColor==1) {
                routeColor = ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorRouteLine);
            }
            if(tipoColor==2) {


                routeColor = ContextCompat.getColor(DrawRouteMaps.getContext(), R.color.colorRouteLine2);

            }


            if (routeColor == 0)
                lineOptions.color(0xFF0A8F08);
            else
                lineOptions.color(routeColor);
        }

        // Drawing polyline in the Google Map for the i-th route
        if (lineOptions != null && mMap != null) {
            mMap.addPolyline(lineOptions);
        } else {
            Log.d("onPostExecute", "without Polylines draw");
        }
    }

}
