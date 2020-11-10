package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

/**
 * Created by admin on 9/6/2017.
 */


public class Multiple_Gallary  implements Serializable {

    @SerializedName("img_id")
    public int img_id;

    @SerializedName("gallery_id")
    public int event_id;

    @SerializedName("img_urls")
    public String img_urls;


    @SerializedName("is_video")
    public int is_video;

    @SerializedName("img_thumb")
    public String img_thumb;



}
