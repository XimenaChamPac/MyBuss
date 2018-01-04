package com.example.eloy1.mybuss;




import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.eloy1.mybuss.Class.Coordenadas;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class informacion extends Activity {
    List<Coordenadas> users = new ArrayList<>();
    ListView lstViewm;
    String auxVista;
    ImageView MostrarImg;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.informacion);
        lstViewm=(ListView)findViewById(R.id.lstView2);
        MostrarImg=(ImageView)findViewById(R.id.capturedImage) ;


        new GetData3().execute(Common.getAddressApi());

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] imageBytes = baos.toByteArray();
        imageBytes = Base64.decode(auxVista, Base64.DEFAULT);
        Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        MostrarImg.setImageBitmap(decodedImage);




    }
    class GetData3 extends AsyncTask<String,Void,String> {
        ProgressDialog pd= new ProgressDialog(informacion.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setTitle("Porfavor espere ..");
            pd.show();
        }



        @Override
        protected String doInBackground(String... params) {
            String stream=null;
            String urlString=params[0];

            HTTPDataHandler http= new HTTPDataHandler();
            stream=http.GetHTTPData(urlString);
            return stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            CustomAdapter adapter;

            Gson gson = new Gson();
            Type listType = new TypeToken<List<Coordenadas>>(){}.getType();
            users=gson.fromJson(s,listType);
            adapter=new CustomAdapter(getApplicationContext(),users);
            lstViewm.setAdapter(adapter);
            //ultPosi.setText(userrow.getText());
            // ultPosi.setText(adapter.getItem(adapter.getCount()).toString());

            auxVista= adapter.ultiDatoReportar(lstViewm.getCount()-1);

            Toast toast1 = Toast.makeText(getApplication(), adapter.ultiDatoReportar(lstViewm.getCount()-1), Toast.LENGTH_SHORT);
            toast1.show();
            //ultPosi.setText( adapter.ultiDato(lstViewm.getCount()-1) );
            lstViewm.clearTextFilter();
            pd.dismiss();
        }
    }
}