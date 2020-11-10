package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

public class GallaryObjects implements Serializable {

    @SerializedName("main_img")
    public String main_img;

    @SerializedName("gallery_id")
    public String gallery_id;

    @SerializedName("title")
    @TableField(name = "title",datatype = Entity.DATATYPE_STRING)
    public String title;

    @SerializedName("gallery_date")
    @TableField(name = "gallery_date",datatype = Entity.DATATYPE_STRING)
    public String gallery_date;


}
