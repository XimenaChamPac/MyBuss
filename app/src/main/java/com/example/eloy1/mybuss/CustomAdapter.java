package com.example.eloy1.mybuss;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.eloy1.mybuss.Class.Coordenadas;

import java.util.List;

/**
 * Created by eloy1 on 27/12/2017.
 */

public class CustomAdapter extends BaseAdapter {
    private Context mContext;
    private List<Coordenadas> lstCoordenadas;

    public CustomAdapter(Context mContext, List<Coordenadas> lstCoordenadas) {
        this.mContext = mContext;
        this.lstCoordenadas = lstCoordenadas;
    }

    @Override
    public int getCount() {
        return lstCoordenadas.size();
    }

    @Override
    public Object getItem(int position) {
        return lstCoordenadas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {



        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row,null);

        TextView txtlat= (TextView) view.findViewById(R.id.txtlatitud);;
        txtlat.setText(lstCoordenadas.get(position).getLatitud());
        Toast.makeText(mContext, lstCoordenadas.get(position).getLatitud(), Toast.LENGTH_LONG).show();



        return null;


    }
}
