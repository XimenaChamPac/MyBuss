package com.example.eloy1.mybuss.Class;
import com.google.gson.annotations.SerializedName;

/**
 * Created by eloy1 on 27/12/2017.
 */

public class Id {
    @SerializedName("$oid")
    private  String oid;
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }


}
