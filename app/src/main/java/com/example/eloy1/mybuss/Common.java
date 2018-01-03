package com.example.eloy1.mybuss;

import com.example.eloy1.mybuss.Class.Coordenadas;

/**
 * Created by eloy1 on 27/12/2017.
 */

public class Common {
    private static String DB_NAME="mybuss";
    private static String COLLECTION_NAME="coordenadas";
    public static String API_KEY="_v19Wc7M6Ds5eqFdrjI7DZPF702azsFj";

    public static String getAddressSingle(Coordenadas coord){
        String baseUrl = String.format("https://api.mlab.com/api/1/databases/%s/collections/%s/",DB_NAME,COLLECTION_NAME);
        StringBuilder stringBuilder= new StringBuilder(baseUrl);
        stringBuilder.append("/"+coord.get_id().getOid()+"?apiKey="+API_KEY);
        return stringBuilder.toString();
    }

    public static String getAddressApi(){
        String baseUrl = String.format("https://api.mlab.com/api/1/databases/%s/collections/%s/",DB_NAME,COLLECTION_NAME);
        StringBuilder stringBuilder= new StringBuilder(baseUrl);
        stringBuilder.append("?apiKey="+API_KEY);
        return stringBuilder.toString();

    }
}

