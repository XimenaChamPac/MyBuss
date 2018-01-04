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
    private List<Coordenadas> lstUsers;




    public CustomAdapter(Context mContext, List<Coordenadas> lstUsers) {
        this.mContext = mContext;
        this.lstUsers = lstUsers;


    }

    @Override
    public int getCount() {
        return lstUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return lstUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public String ultiDato(int position){
        return lstUsers.get(position).getCoordendas();
    }
    public String ultiDatoReportar(int position){
        return lstUsers.get(position).getReportar();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.row,null);

    TextView txtUser = (TextView) view.findViewById(R.id.txtUser);
    txtUser.setText(lstUsers.get(position).getCoordendas());





        return view;


    }




}
