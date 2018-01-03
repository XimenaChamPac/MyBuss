package com.example.eloy1.mybuss;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.ahmadrosid.lib.drawroutemap.DrawMarker;
import com.ahmadrosid.lib.drawroutemap.DrawRouteMaps;
import com.ahmadrosid.lib.drawroutemap.RouteDrawerTask;
import com.example.eloy1.mybuss.Class.Coordenadas;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    Marker mCurrLocationMarker;
    LocationRequest mLocationRequest;

    LatLng PosAct;//varianle publica para acceder de cualquier funcion
    LatLng PosMarcadorMovil=new LatLng(0.0, 0.0);;
    String ConPoMov="";

    private BroadcastReceiver broadcastReceiver;
    private Switch switcha;
    private TextView textView;
    private android.location.LocationListener listener;
    private LocationManager locationManager;

    private TextView textViewColor;
    private Button ubicacionCarro;

    //////////////
    double latitudCambiante,longitudCambiante;

    ////////////

    private Marker marcadorParaderos;

    //////////////////////
    boolean bD19Aida=false;
    boolean bD19Avuelta=false;
    boolean bD03AAida=false;
    boolean bD03AAvuelta=false;
    boolean bP010ida=false;
    boolean bP010vuelta=false;
    boolean bP19Bida=false;
    boolean bP19Bvuelta=false;
    //////////////////






    Coordenadas userSelected=null;
    List<Coordenadas> coordenadas = new ArrayList<>();
    ListView lstView;





    boolean estadoRuta=false;
    private String direccionText="";
    String ubicacion[] = {"Paradero Mercado Alto San Martin ", "Paradero Av Peru con C. Canga", "Paradero Plaza de San Jose", "Paradero del Cruce", "Paradero Cercano a la Municipalidad", "Paradero Cercano a la Parada", "Paradero Entrada a Hunter y Tiabaya/Sachaca", "Paradero Salaverry BCP", "Paradero Ormeño Salaverry", "Paradero Nuestra Señora del Pilar", "Paradero Goyeneche con Paucarpata", "Paradero Paucarpata con Independecia /Agentes", "Paradero Feria del Altiplano, Malecon Zolezzi y Elias Aguirre"};






    @Override
    protected void onResume() {
        super.onResume();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    textView = (TextView) findViewById(R.id.textView2);
                    textView.append("\n" + intent.getExtras().get("coordinates"));
                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("location_update"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ////////////////////
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        ////////////////////////////





        ubicacionCarro= (Button) findViewById(R.id.btnactualizar);
        ubicacionCarro.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng originD19As = new LatLng(PosAct.latitude, PosAct.longitude);//posicion actual
                LatLng destinationD19As = new LatLng(PosMarcadorMovil.latitude, PosMarcadorMovil.longitude);//posicion de destino que vendria a ser la del paradero
                DrawRouteMaps.getInstance(getApplicationContext()).draw(originD19As, destinationD19As, mMap,1);//dibuja el recorrido usando la clase DrawRouteMaps
                Toast.makeText(getApplicationContext(), "obtiene ruta al marcador", Toast.LENGTH_LONG).show();
            }
        }));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Toast.makeText(getApplicationContext(), "Manda ubicaciones", Toast.LENGTH_LONG).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        switcha = (Switch) findViewById(R.id.switch1);


        if (!runtime_permissions()) {
            Toast.makeText(getApplicationContext(), "CARGANDO RUTAS", Toast.LENGTH_LONG).show();
        }
        //PARATE IMPORTANTE DEL FUNCIONAMIENTO DEL SWITCH
        switcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (switcha.isChecked() == true) {
                    listener = new android.location.LocationListener() {
                        @Override
                        public void onLocationChanged(Location location) {


                            Intent i = new Intent("location_update");
                            //Aqui optiene la posicion actual del gps
                            i.putExtra("coordinates", location.getLongitude() + " " + location.getLatitude());
                            sendBroadcast(i);
                            //convierto las posiciones que son double a String
                            String latitudString = Double.toString(location.getLatitude());
                            String longitudString = Double.toString(location.getLongitude());
                            String mandarCoordenadas=latitudString+","+longitudString;
                            //crear un nombre al archivo
                            String nomarchivo = "posicion.txt";
                            //para saber si realmente esta entrando a crear el documento
                            Toast.makeText(getApplicationContext(), "Copiando Ubicacion", Toast.LENGTH_LONG).show();
                            new PostData(mandarCoordenadas).execute(Common.getAddressApi());


                            //escritura de las posiciones dentro del fichero
                            try {
                                File tarjeta = Environment.getExternalStorageDirectory();
                                File file = new File(tarjeta.getAbsolutePath(), nomarchivo);
                                OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true));//el true te sierve para mantener la informacion anterior
                                //agrego las posicones al fichero
                                osw.append("\n");
                                osw.append(latitudString);
                                osw.append("\n");
                                osw.append(longitudString);
                                osw.flush();
                                osw.close();


                            } catch (IOException ioe) {
                            }


                        }

                        @Override
                        public void onStatusChanged(String s, int i, Bundle bundle) {

                        }

                        @Override
                        public void onProviderEnabled(String s) {

                        }

                        @Override
                        public void onProviderDisabled(String s) {
                            Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        }
                    };

                    locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

                    //noinspection MissingPermission
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, listener);
                }
                if (switcha.isChecked() == false) {//cuando no queremos mas ubicaciones se activa

                    if (locationManager != null) {
                        //noinspection MissingPermission
                        locationManager.removeUpdates(listener); //ya no esuchas laas posciones

                    }

                    Toast.makeText(getApplicationContext(), "Detiene ubicaciones", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
            return;
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (android.location.LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (android.location.LocationListener) Local);

        //mensaje1.setText("Localizacion agregada");
        //  mensaje2.setText("");
    }
    /* Aqui empieza la Clase Localizacion */
    public class Localizacion implements android.location.LocationListener {
        MainActivity mainActivity;

        public MainActivity getMainActivity() {
            return mainActivity;
        }

        public void setMainActivity(MainActivity mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();

            String Text = " Lat = "+ loc.getLatitude() + " Long = " + loc.getLongitude();
            latitudCambiante=loc.getLatitude();
            longitudCambiante=loc.getLongitude();
            //mensaje1.setText(Text);
            //Toast.makeText(getApplicationContext(), Text, Toast.LENGTH_LONG).show();
            ///////VISUALIZAR AUTOMATICAMENTE PARADEROS
            //visualizar_paraderos(0.50);
            ///////FIN VISUALIZAR AUTOMATICAMENTE PARADEROS
            LatLng latLng_ = new LatLng(loc.getLatitude(), loc.getLongitude());//aqui

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_));
            //mMap.animateCamera(CameraUpdateFactory.zoomTo(23));
            // mMap.animateCamera(CameraUpdateFactory.zoomIn());
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14 ));//Zoom

            this.mainActivity.setLocation(loc);
        }

        @Override
        public void onProviderDisabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es desactivado
            //mensaje1.setText("GPS Desactivado");
            //Toast.makeText(getApplicationContext(), "GPS Desactivado", Toast.LENGTH_LONG).show();


        }

        @Override
        public void onProviderEnabled(String provider) {
            // Este metodo se ejecuta cuando el GPS es activado
           // mensaje1.setText("GPS Activado");
           // Toast.makeText(getApplicationContext(), "GPS Activado", Toast.LENGTH_LONG).show();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }
    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
                if (!list.isEmpty()) {
                    Address DirCalle = list.get(0);
                  //  mensaje2.setText("Mi direccion es: \n"
                    //        + DirCalle.getAddressLine(0));
                   // Toast.makeText(getApplicationContext(), DirCalle.getAddressLine(0), Toast.LENGTH_LONG).show();

                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    class GetData extends AsyncTask<String,Void,String> {
        ProgressDialog pd= new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //pd.setTitle("Porfavor espere ..");
            //pd.show();
        }



        @Override
        protected String doInBackground(String... params) {
            String stream=null;
            String urlString=params[0];

            HTTPDataHandler http= new HTTPDataHandler();
            stream=http.GetHTTPData(urlString);
            //Toast.makeText(getApplicationContext(), stream, Toast.LENGTH_LONG).show();

            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Type listType = new TypeToken<List<Coordenadas>>(){}.getType();
            coordenadas=gson.fromJson(s,listType);



            CustomAdapter adapter=new CustomAdapter(getApplicationContext(),coordenadas);
            //lstView=(ListView)findViewById(R.id.lstView);


            //lstView.setAdapter(adapter);



/*
            TextView txtlat= (TextView) findViewById(R.id.txtlatitud);
            TextView txtlon= (TextView) findViewById(R.id.txtlongitud);
            String lats= (String) txtlat.getText();
            String lons=(String) txtlon.getText();
            double latd=Double.parseDouble(lats);
            double lond=Double.parseDouble(lons);
            LatLng coor = new LatLng(latd,lond);
            MarkerOptions markerOptions = new MarkerOptions(); //para crear el marcador
            markerOptions.position(coor);//y aqui obtiene la posicion para el marcador
            setPosicion(coor);//coloca el marcador
            markerOptions.title("linea 1");//la etiqueta para el marcador
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));//color del marcador
            mCurrLocationMarker = mMap.addMarker(markerOptions);//agregar el marcador al mapa
*/

            pd.dismiss();
        }
    }


    class PostData extends AsyncTask<String,String,String>{
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        String latitud;


        public PostData(String latitud) {
            this.latitud= latitud;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // pd.setTitle("porfavor espere ....");
            //pd.show();
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... params) {
            String urlString=params[0];

            HTTPDataHandler hh=new HTTPDataHandler();
            String json="{\"coordenadas\":\""+latitud+"\"}";
            //String json="{\"latitud\":\""+latitud+"\",\"longitud\":\"" + longitud+"\"}";
            hh.PostHTTPData(urlString,json);
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //Refresh DAta
          //  new GetData().execute(Common.getAddressApi());
            pd.dismiss();
        }


    }


    // Metodo para cargar los elementos de la mainactivity
    private void cargarelementos(){
        // Inflamos el mapa
        mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment)).getMap();

    }

    private void enable_buttons() {


        switcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (switcha.isChecked() == true) {
                    Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                    startService(i);
                    Toast.makeText(getApplicationContext(), "Manda ubicaciones", Toast.LENGTH_LONG).show();


                }
                if (switcha.isChecked() == false) {
                    Intent i = new Intent(getApplicationContext(), GPS_Service.class);
                    stopService(i);
                    Toast.makeText(getApplicationContext(), "Detiene ubicaciones", Toast.LENGTH_LONG).show();


                }
            }
        });


    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 23 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }


    @Override

    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_camera:

                return true;
            case R.id.nav_gallery:
                return true;
            case R.id.nav_slideshow:

                return true;
            case R.id.nav_manage:

                return true;
            case R.id.action_settings:
                //metodoSettings()
                visualizar_contactanos();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void visualizar_contactanos() {
        Toast.makeText(getApplicationContext(), "Se presionó Contactanos", Toast.LENGTH_LONG).show();


        Intent i = new Intent(this, programadores.class );
        startActivity(i);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {//PARADEROS
            // Handle the camera action
            Toast.makeText(this, "Se presionó Visualizar Paraderos", Toast.LENGTH_LONG).show();

            visualizar_paraderos(0.5);

        } else if (id == R.id.nav_gallery) {
            Toast.makeText(this, "Se presionó Visualizar Rutas", Toast.LENGTH_LONG).show();
            visualizar_rutas();


        } else if (id == R.id.nav_slideshow) {
           //// Toast.makeText(this, "Se presionó Informacion Buses", Toast.LENGTH_LONG).show();
            visualizar_informacion();


        } else if (id == R.id.nav_manage) {
            Toast.makeText(this, "Se presionó Salir", Toast.LENGTH_LONG).show();
            finish();


        } else if (id == R.id.nav_share) {
            Toast.makeText(this, "Se presionó Compartir", Toast.LENGTH_LONG).show();
            compartir();



        } else if (id == R.id.nav_send) {

            mandar_destino();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void mandar_destino() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if(ConPoMov!="") {

            //https://www.google.com/maps/place/seguipersa+e.i.r.l/@-16.4057649,-71.5029722,17z/data=!3m1!4b1!4m5!3m4!1s0x91424b74b1675399:0x82c364f2d1e97324!8m2!3d-16.4057701!4d-71.5007781
            intent.putExtra(Intent.EXTRA_TEXT, "Enviado desde MYBuss. Me estoy dirigiendo a: http://maps.google.com/?q=" + PosMarcadorMovil.latitude + "," + PosMarcadorMovil.longitude);
            startActivity(Intent.createChooser(intent, "Compartir con"));
        }else{
            Toast.makeText(this, "No se ha seleccionado ningun Destino", Toast.LENGTH_LONG).show();

        }
    }

    private void compartir() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "Usa nuestro aplicativo MyBuss para movilizarte por Arequipa https://drive.google.com/file/d/1OGnJfxuUaM0J6O28BkUMQ-idZZ_xQoyy/view?usp=sharing");
        startActivity(Intent.createChooser(intent, "Compartir con"));
    }

    private void visualizar_informacion() {
        Intent i = new Intent(this, informacion.class );
        startActivity(i);
    }

    private void visualizar_rutas() {
        //DESCOMENTAR PARA VER DIFERENTES RUTAS
        //COMOS E TRABAJA CON EL API DE GOOGLE MAPS A MAS CONSULTAS NOS QUEDAMOS SIN
        //ACCESO POR QUE ESTAMOS USANDO CUENTA GRATIOS
        if(bD19Aida==true) {
            Toast.makeText(this, "Ruta D 19A IDA", Toast.LENGTH_LONG).show();
            //Ruta D 19A : Alto San Martin-Cercado Y Viceversa BAJADA
            double laD19Ab[] = {-16.403938,-16.405605,-16.40508,-16.402515,-16.403969,-16.402437,-16.401203,-16.40982,-16.407604,-16.406862,-16.40608};
            double lnD19Ab[] = {-71.490654,-71.50588,-71.50747,-71.51063,-71.512985,-71.51497,-71.516754,-71.53205,-71.53397,-71.53301,-71.533775};
            LatLng destinationMarker = new LatLng(laD19Ab[0], lnD19Ab[0]);
            DrawMarker.getInstance(this).draw(mMap, destinationMarker, R.drawable.marker_a, "Ruta D 19A VUELTA"); //agregar marcadores

            for(int D19A=0; D19A < laD19Ab.length;D19A++) {
                if(D19A+1<laD19Ab.length) {
                    LatLng originD19A = new LatLng(laD19Ab[D19A], lnD19Ab[D19A]);//posicion actual
                    LatLng destinationD19A = new LatLng(laD19Ab[D19A + 1], lnD19Ab[D19A + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originD19A, destinationD19A, mMap,2);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    DrawMarker.getInstance(this).draw(mMap, destinationMarker, R.drawable.marker_b, "Ruta D 19A IDA");//marcador de destino

                    LatLngBounds boundsD19A = new LatLngBounds.Builder().include(originD19A).include(destinationD19A).build();
                    Point displaySizeD19A = new Point();
                }
            }

            SystemClock.sleep(4000);

        }
        if(bD19Avuelta==true) {
            Toast.makeText(this, "Ruta D 19A VUELTA", Toast.LENGTH_LONG).show();
            //Ruta D 19A : Alto San Martin-Cercado Y Viceversa SUBIDA

            double laD19As[] = {-16.406054,-16.402525,-16.404425,-16.398842,-16.399591,-16.401182,-16.40242,-16.404026,-16.400406,-16.405409,-16.403978};
            double lnD19As[] = {-71.533745,-71.52939,-71.5274,-71.52123,-71.51553,-71.51674,-71.514984,-71.51293,-71.50694,-71.503456,-71.490585};
            LatLng originMarker = new LatLng(laD19As[0], lnD19As[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarker, R.drawable.marker_a, "Ruta D 19A SUBIDA"); //agregar marcadores
            for(int D19A=0; D19A < laD19As.length;D19A++) {
                if(D19A+1<laD19As.length) {
                    LatLng originD19As = new LatLng(laD19As[D19A], lnD19As[D19A]);//posicion actual
                    LatLng destinationD19As = new LatLng(laD19As[D19A + 1], lnD19As[D19A + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originD19As, destinationD19As, mMap,1);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsD19As = new LatLngBounds.Builder().include(originD19As).include(destinationD19As).build();
                    Point displaySizeD19As = new Point();
                }
            }

            SystemClock.sleep(6000);
        }
        if(bD03AAida==true) {
            Toast.makeText(this, "Ruta D 03AA IDA", Toast.LENGTH_LONG).show();
            //Ruta D 03AA : Jerusalen-Mariano Melgar-Cercado Y Viceversa BAJADA
            double laD03AAb[] = {-16.39928,-16.398777,-16.398077,-16.398275,-16.397173,-16.39545,-16.395885,-16.395098,-16.392704,-16.392345,-16.391865,-16.3923,-16.391382,-16.394304,-16.400385,-16.400558,-16.401217,-16.401033,-16.403107,-16.403519,-16.40128,-16.401623,-16.40141,-16.402328,-16.400192,-16.40239,-16.401365,-16.404472,-16.400915,-16.404552,-16.40533,-16.409851};
            double lnD03AAb[] = {-71.48915,-71.48823,-71.48739,-71.48925,-71.48879,-71.48827,-71.4858,-71.48419,-71.482056,-71.48311,-71.48378,-71.48402,-71.485565,-71.48914,-71.49023,-71.48896,-71.48898,-71.49029,-71.49058,-71.494865,-71.494415,-71.495285,-71.500206,-71.502846,-71.5042,-71.50792,-71.50865,-71.51386,-71.5192,-71.522194,-71.52156,-71.53201};
            LatLng originMarker = new LatLng(laD03AAb[0], lnD03AAb[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarker, R.drawable.marker_a, "Ruta D03AA IDA"); //agregar marcadores

            for(int D03AA=0; D03AA < laD03AAb.length;D03AA++) {
                if(D03AA+1<laD03AAb.length) {
                    LatLng originD03AA = new LatLng(laD03AAb[D03AA], lnD03AAb[D03AA]);//posicion actual
                    LatLng destinationD03AA= new LatLng(laD03AAb[D03AA + 1], lnD03AAb[D03AA + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originD03AA, destinationD03AA, mMap,1);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsD19A = new LatLngBounds.Builder().include(originD03AA).include(destinationD03AA).build();
                    Point displaySizeD19A = new Point();
                }
            }
            bD03AAida=false;
            SystemClock.sleep(5000);

        }
        if(bD03AAvuelta==true) {
            Toast.makeText(this, "Ruta D 03AA VUELTA", Toast.LENGTH_LONG).show();

            //Ruta D 03AA : Jerusalen-Mariano Melgar-Cercado Y Viceversa SUBIDA
            double laD03AAs[] = {-16.409517,-16.40766,-16.406414,-16.402538,-16.404425,-16.39883,-16.39949,-16.401161,-16.402449,-16.404013,-16.400438,-16.403399,-16.40101,-16.401344,-16.401587,-16.402723,-16.403748,-16.403305,-16.403013,-16.400438,-16.400352,-16.399265};
            double lnD03AAs[] = {-71.53368,-71.53397,-71.53425,-71.52941,-71.52739,-71.52121,-71.51685,-71.51681,-71.51498,-71.51293,-71.506966,-71.50466,-71.50057,-71.49989,-71.495346,-71.494705,-71.49475,-71.49061,-71.48963,-71.48952,-71.49021,-71.48983};
            LatLng originMarker = new LatLng(laD03AAs[0], lnD03AAs[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarker, R.drawable.marker_a, "Ruta D03AA VUELTA"); //agregar marcadores

            for(int D03AA=0; D03AA < laD03AAs.length;D03AA++) {
                if(D03AA+1<laD03AAs.length) {
                    LatLng originD03AA = new LatLng(laD03AAs[D03AA], lnD03AAs[D03AA]);//posicion actual
                    LatLng destinationD03AA= new LatLng(laD03AAs[D03AA + 1], lnD03AAs[D03AA + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originD03AA, destinationD03AA, mMap,2);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsD19A = new LatLngBounds.Builder().include(originD03AA).include(destinationD03AA).build();
                    Point displaySizeD19A = new Point();
                }
            }
            bD03AAvuelta=false;
            SystemClock.sleep(4000);


        }
        if(bP010ida==true ){
            //Ruta P 010 : Patasagua -Tiabaya -Cercado Y Viceversa IDA
            double laP010ida[] = {-16.442415,-16.447351,-16.449879,-16.43151,-16.405754,-16.408966,-16.402529,-16.404291};
            double lnP010ida[] = {-71.61105,-71.592926,-71.58711,-71.566414,-71.5401,-71.537415,-71.5294,-71.52746};
            LatLng originMarker = new LatLng(laP010ida[0], laP010ida[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarker, R.drawable.marker_a, "Ruta P010 IDA"); //agregar marcadores

            for(int P010=0; P010 < laP010ida.length;P010++) {
                if(P010+1<laP010ida.length) {
                    LatLng originP010 = new LatLng(laP010ida[P010], lnP010ida[P010]);//posicion actual
                    LatLng destinationP010= new LatLng(laP010ida[P010 + 1], lnP010ida[P010 + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originP010, destinationP010, mMap,1);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsP010 = new LatLngBounds.Builder().include(originP010).include(destinationP010).build();
                    Point displaySizeP010 = new Point();
                }
            }

            SystemClock.sleep(4000);


        }

        if(bP010vuelta==true ){
            //Ruta P 010 : Patasagua -Tiabaya -Cercado Y Viceversa VUELTA
            double laP010vuelta[] = {-16.404213,-16.407225,-16.40905,-16.412685,-16.431477,-16.4499,-16.447344,-16.442383};
            double lnP010vuelta[] = {-71.52753,-71.53109,-71.529724,-71.54555,-71.56166,-71.58712,-71.592964,-71.61156};
            LatLng originMarker = new LatLng(laP010vuelta[0], laP010vuelta[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarker, R.drawable.marker_a, "Ruta P010 VUELTA"); //agregar marcadores

            for(int P010=0; P010 < laP010vuelta.length;P010++) {
                if(P010+1<laP010vuelta.length) {
                    LatLng originP010 = new LatLng(laP010vuelta[P010], lnP010vuelta[P010]);//posicion actual
                    LatLng destinationP010= new LatLng(laP010vuelta[P010 + 1], lnP010vuelta[P010 + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originP010, destinationP010, mMap,2);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsP010 = new LatLngBounds.Builder().include(originP010).include(destinationP010).build();
                    Point displaySizeP010 = new Point();
                }
            }

            SystemClock.sleep(4000);

        }
        if(bP19Bida==true ){
            //Ruta P 19B : Tiabaya -Cercado Y Viceversa ida
            double laP19Bida[] = {-16.434694,-16.437986,-16.43995,-16.448036,-16.449924,-16.431479,-16.431572,-16.405783,-16.408995,-16.402523,-16.404293};
            double lnP19Bida[] = {-71.59955,-71.59739,-71.59298,-71.59277,-71.58715,-71.566444,-71.56167,-71.540115,-71.53744,-71.52941,-71.52751};
            LatLng originMarkerP19Bida = new LatLng(laP19Bida[0], lnP19Bida[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarkerP19Bida, R.drawable.marker_a, "Ruta P19B IDA"); //agregar marcadores

            for(int P19B=0; P19B < laP19Bida.length;P19B++) {
                if(P19B+1<laP19Bida.length) {
                    LatLng originP19B = new LatLng(laP19Bida[P19B], lnP19Bida[P19B]);//posicion actual
                    LatLng destinationP19B= new LatLng(laP19Bida[P19B + 1], lnP19Bida[P19B + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originP19B, destinationP19B, mMap,1);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsP19B = new LatLngBounds.Builder().include(originP19B).include(destinationP19B).build();
                    Point displaySizeP19B = new Point();
                }
            }


            SystemClock.sleep(4000);

        }
        if(bP19Bvuelta==true){
            //Ruta P 19B : Tiabaya -Cercado Y Viceversa vuelta
            double laP19Bvuelta[] = {-16.40431,-16.406883,-16.414028,-16.422262,-16.406363,-16.419283,-16.424927,-16.43393,-16.434683};
            double lnP19Bvuelta[] = {-71.52748,-71.53069,-71.52797,-71.54418,-71.563354,-71.58505,-71.587494,-71.59976,-71.599556};
            LatLng originMarkerlaP19Bvuelta = new LatLng(laP19Bvuelta[0], lnP19Bvuelta[0]);
            DrawMarker.getInstance(this).draw(mMap, originMarkerlaP19Bvuelta, R.drawable.marker_b, "Ruta P19B VUELTA"); //agregar marcadores

            for(int P19B=0; P19B < laP19Bvuelta.length;P19B++) {
                if(P19B+1<laP19Bvuelta.length) {
                    LatLng originP19B = new LatLng(laP19Bvuelta[P19B], lnP19Bvuelta[P19B]);//posicion actual
                    LatLng destinationP19B= new LatLng(laP19Bvuelta[P19B + 1], lnP19Bvuelta[P19B + 1]);//posicion de destino que vendria a ser la del paradero
                    DrawRouteMaps.getInstance(this).draw(originP19B, destinationP19B, mMap,2);//dibuja el recorrido usando la clase DrawRouteMaps
                    //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
                    //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

                    LatLngBounds boundsP19B = new LatLngBounds.Builder().include(originP19B).include(destinationP19B).build();
                    Point displaySizeP19B = new Point();
                }
            }

            SystemClock.sleep(4000);
        }

        else{
            Toast.makeText(this, "NO HAY PARADEROS ", Toast.LENGTH_LONG).show();

        }


    }

    private void visualizar_paraderos(double distvisualizacion) {
        //Creamos el arraylist para guardar los marcadores
        ArrayList<Marker> listadoMar = new ArrayList<Marker>();

        //PARA CREAR LAS PARADAS EJEMPLO1
        double la[] = {-16.400964, -16.4010329, -16.437962, -16.439956, -16.449053, -16.431501, -16.431400, -16.405901, -16.407593, -16.408831, -16.402650, -16.404151, -16.402208};
        double ln[] = {-71.497669, -71.5001998, -71.597475, -71.592929, -71.589835, -71.566536, -71.561623, -71.539925, -71.538586, -71.537175, -71.529467, -71.527738, -71.514796};
        // Toast.makeText(getApplicationContext(), "antes de pasar datos", Toast.LENGTH_LONG).show();

        for(int x=0;x< la.length;x++) {
            // Usamos constructor de la clase pasamos latitud, longitud y contexto
            //llname = new LatLongName(la[x], ln[x]);
            // Toast.makeText(getApplicationContext(), "dato pasado "+x, Toast.LENGTH_LONG).show();
            // Elemento geocoder en el contexto
            Geocoder geocoder = new Geocoder(MainActivity.this);
            // Elemento list que contendra la direccion
            List<Address> direcciones = null;

            // Funcion para obtener coger el nombre desde el geocoder
            try {
                direcciones = geocoder.getFromLocation(la[x], ln[x],1);
            } catch (Exception e) {
                Log.d("Error", "Error en geocoder:"+e.toString());
            }

            // Funcion que determina si se obtuvo resultado o no
            if(direcciones != null && direcciones.size() > 0 ){

                // Creamos el objeto address
                Address direccion = direcciones.get(0);

                // Creamos el string a partir del elemento direccion
                direccionText = String.format("%s,%s, %s",
                        direccion.getMaxAddressLineIndex() > 0 ? direccion.getAddressLine(0) : "",
                        direccion.getThoroughfare(),
                        direccion.getFeatureName());
            }
            ubicacion[x]=direccionText;


            // Arrancamos la tarea en segundo plano
            // llname.execute();
        }
//        Toast.makeText(getApplicationContext(), "despues de pasar datos", Toast.LENGTH_LONG).show();




        //para obtener la ditancia de mi PosAct a cada paradero que este cercano a nuestra posicion
        for (int i = 0; i < la.length; i++) {
            double distancia = distanciaCoord(latitudCambiante, longitudCambiante, la[i], ln[i]);//ddistancia posAct con posicion de apradero
            String aString = String.format("%.2f", distancia);//convierte de double a string pero solo con dos decimales
            String dista = "distancia=" + aString + " Km "; //informacion de distancia que se agregara al array de ubicacines(nombre de paraderos)
            ubicacion[i] = dista + ubicacion[i]; //concateno la informacion de distancias con la informacion de ubicaciones (nombre de los paraderos)1
        }

        //Cuando vayas a eliminar si tenemos el id del marker a borrar recorremos la lista y eliminamos este marcador
        for(int i = 0; i<listadoMar.size(); i++)
        {
                     listadoMar.remove(listadoMar.get(i));

        }

        //Ahora que hemos eliminado el marcador, recargamos el mapa
        mMap.clear();


        for (int i = 0; i < la.length; i++) {

            if(distanciaCoord(latitudCambiante, longitudCambiante,la[i], ln[i])<=distvisualizacion) {
                LatLng paradero1 = new LatLng(la[i], ln[i]);//agregar la latiud y longitud de cada paradero
                marcadorParaderos=mMap.addMarker(new MarkerOptions().position(paradero1).title(ubicacion[i]));//agrego datoss al marcador
                //añadimos el marcador a la lista
                listadoMar.add(marcadorParaderos);
                if(i==0){
                    bD03AAida=true;
                }
            }

        }
    }


    public void onMapReady(GoogleMap googleMap) {


        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        } else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                PosMarcadorMovil=latLng;
                mMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.avion))
                        .anchor(0.0f, 1.0f)
                        .position(PosMarcadorMovil));
                ConPoMov=PosMarcadorMovil.toString();
                Toast.makeText(getApplicationContext(),PosMarcadorMovil.toString(), Toast.LENGTH_LONG).show();


            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(),"Has pulsado una marca", Toast.LENGTH_LONG).show();
                return false;
            }
        });



    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }



    @Override
        public void onLocationChanged(Location location) {
        mLastLocation = location;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }



        //PObtiene la posicion de tu usuario al iniciar el aplicativo
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());//aqui
        MarkerOptions markerOptions = new MarkerOptions(); //para crear el marcador
        markerOptions.position(latLng);//y aqui obtiene la posicion para el marcador
        PosAct = latLng;//copiar la posicion actual a otro objeto
        markerOptions.title("Posicion Actual");//la etiqueta para el marcador
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));//color del marcador
        mCurrLocationMarker = mMap.addMarker(markerOptions);//agregar el marcador al mapa




        //move map camera
       // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
       // mMap.animateCamera(CameraUpdateFactory.zoomTo(10));//Zoom

        //PARA DIBUJAR LA RUTA DE MI POSICION A ALGUN PARADERO DESEADO
       // LatLng origin = new LatLng(PosAct.latitude, PosAct.longitude);//posicion actual
        //LatLng destination = new LatLng(la[11], ln[11]);//posicion de destino que vendria a ser la del paradero
        //DrawRouteMaps.getInstance(this).draw(origin, destination, mMap);//dibuja el recorrido usando la clase DrawRouteMaps
        //DrawMarker.getInstance(this).draw(mMap, origin, R.drawable.marker_a, "Posicion Actual"); //agregar marcadores
        //DrawMarker.getInstance(this).draw(mMap, destination, R.drawable.marker_b, "Paradero Seleccionado");//marcador de destino

        //LatLngBounds bounds = new LatLngBounds.Builder().include(origin).include(destination).build();
        //Point displaySize = new Point();
        //getWindowManager().getDefaultDisplay().getSize(displaySize);
        //mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, displaySize.x, 250, 30));






        //stop location updates
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                enable_buttons();
            } else {
                runtime_permissions();
            }
        }


        // other 'case' lines to check for other permissions this app might request.
        // You can add here other case statements according to your requirement.

    }



    ///FUNCION PARA OBTENER LA DISTANCIA
    public static double distanciaCoord(double lat1, double lng1, double lat2, double lng2) {
        //double radioTierra = 3958.75;//en millas
        double radioTierra = 6371;//en kilómetros
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double va1 = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double va2 = 2 * Math.atan2(Math.sqrt(va1), Math.sqrt(1 - va1));
        double distancia = radioTierra * va2;

        return distancia;
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
