package com.wenhua.wenhua;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/23 0023.
 */
public class WhMapMarkerInfo implements Serializable
{

    private static final long serialVersionUID = -758459502806858414L;
    private double latitude;
    private double longitude;
    private int imgId;
    private String name;
    private String distance;
    private int zan;
    private String camId;

    public static List<WhMapMarkerInfo> infos = new ArrayList<WhMapMarkerInfo>();


    public WhMapMarkerInfo()
    {
    }

    public WhMapMarkerInfo(double latitude, double longitude, int imgId, String name,
                           String distance, int zan)
    {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
    }

    public WhMapMarkerInfo(double latitude, double longitude,String strcamId)
    {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.camId=strcamId;
    }

    public String getName()
    {
        //return name;
        return "whGPRSCamera";
    }
    public String getCamId()
    {
        return camId;
    }

    public void setName(String name)
    {
        this.name = name;
    }


}