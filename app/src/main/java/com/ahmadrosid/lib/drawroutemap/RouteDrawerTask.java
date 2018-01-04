package com.ahmadrosid.lib.drawroutemap;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eloy1.mybuss.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RouteDrawerTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    private PolylineOptions lineOptions;
    private GoogleMap mMap;
    private int routeColor;
    private int tipoColor=2;
    private int rut=0;
    ArrayList<LatLng> points= new ArrayList<>();
    ArrayList<Double> latfic = new ArrayList<Double>();

    ArrayList<Double> lonfic = new ArrayList<Double>();


    public RouteDrawerTask(){

    }





    public RouteDrawerTask(GoogleMap mMap,int C,int Ru) {

        this.mMap = mMap;
        this.tipoColor=C;
        this.rut=Ru;

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
    public void setPuntos(double latP,double lonP){
        LatLng position = new LatLng(latP, lonP);
        points.add(position);



    }

    private boolean rellenarEditTexts() {
        try
        {
            File ruta_sd = Environment.getExternalStorageDirectory();

            File f = new File(ruta_sd.getAbsolutePath(), "p010ida.txt");

            BufferedReader fin =
                    new BufferedReader(
                            new InputStreamReader(
                                    new FileInputStream(f)));

            String texto = fin.readLine();
            String linea;


            while((linea=fin.readLine())!=null) {


               // double partlat=Double.parseDouble(part1);
                //double partlong=Double.parseDouble(part2);

                if((linea != null) && (!linea.equals(""))) {
                    String str = linea;

                    int intNumber = Integer.parseInt(str.substring(0, str.indexOf('.')));
                    float decNumbert = Float.parseFloat(str.substring(str.indexOf('.')));
                    int decNumberInt = Integer.parseInt(str.substring(str.indexOf('.') + 1));

                    System.out.println(intNumber);
                    System.out.println(decNumbert);
                    System.out.println(decNumberInt);


                    if(intNumber==-16) {
                        double dlat=Double.parseDouble(linea);
                        latfic.add(dlat);
                        Log.e("latitud", linea);




                    }

                    if(intNumber==-71) {
                        double dlon=Double.parseDouble(linea);
                        lonfic.add(dlon);
                        Log.e("longitud", linea);


                    }
                }


            }



            fin.close();
        }
        catch (Exception ex)
        {
            Log.e("Ficheros", "Error al leer fichero desde tarjeta SD");
        }
return false;

}


    private void drawPolyLine(List<List<HashMap<String, String>>> result) {
        lineOptions = null;

        // for (int i = 0; i < result.size(); i++) {
        //LatLng position = new LatLng(-16.39914, -71.497345);
        //LatLng position2 = new LatLng(-16.400248, -71.49648);
     //setPuntos(-16.39914, -71.497345);
       // setPuntos(-16.400248, -71.49648);
        //
        if(rut==3) {
            rellenarEditTexts();
            SystemClock.sleep(5);


            double laD19Ab[] = {-16.39928, -16.398777, -16.398077, -16.398275, -16.397173, -16.39545, -16.395885, -16.395098, -16.392704, -16.392345, -16.391865, -16.3923, -16.391382, -16.394304, -16.400385, -16.400558, -16.401217, -16.401033, -16.403107, -16.403519, -16.40128, -16.401623, -16.40141, -16.402328, -16.400192, -16.40239, -16.401365, -16.404472, -16.400915, -16.404552, -16.40533, -16.409851};
            double lnD19Ab[] = {-71.48915, -71.48823, -71.48739, -71.48925, -71.48879, -71.48827, -71.4858, -71.48419, -71.482056, -71.48311, -71.48378, -71.48402, -71.485565, -71.48914, -71.49023, -71.48896, -71.48898, -71.49029, -71.49058, -71.494865, -71.494415, -71.495285, -71.500206, -71.502846, -71.5042, -71.50792, -71.50865, -71.51386, -71.5192, -71.522194, -71.52156, -71.53201};


            for (int D19A = 0; D19A < latfic.size(); D19A++) {
                if (D19A + 1 < latfic.size()) {
                    setPuntos(latfic.get(D19A), lonfic.get(D19A));
                    setPuntos(latfic.get(D19A+1), lonfic.get(D19A+1));

                }
            }
        }

        for (int i = 0; i < result.size(); i++) {
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            /*
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
            }*/



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
