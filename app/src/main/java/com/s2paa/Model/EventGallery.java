package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

/**
 * Created by admin on 9/6/2017.
 */

@Table(name = "event_gallery")
public class EventGallery extends Entity implements Serializable {

    @SerializedName("img_id")
    @TableField(name = "img_id",datatype = Entity.DATATYPE_INTEGER)
    public int img_id;

    @SerializedName("event_id")
    @TableField(name = "event_id",datatype = Entity.DATATYPE_INTEGER)
    public int event_id;

    @SerializedName("img_urls")
    @TableField(name = "img_urls",datatype = Entity.DATATYPE_STRING)
    public String img_urls;


}
