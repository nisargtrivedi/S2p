package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 8/24/2017.
 */

@Table(name ="event")
public class EventObjects extends Entity {

    @SerializedName("event_id")
    @TableField(name = "event_id",datatype = Entity.DATATYPE_INTEGER)
    public int event_id;

    @SerializedName("user_id")
    @TableField(name = "user_id",datatype = Entity.DATATYPE_STRING)
    public String user_id;


    @SerializedName("title")
    @TableField(name = "title",datatype = Entity.DATATYPE_STRING)
    public String title;

    @SerializedName("description")
    @TableField(name = "description",datatype = Entity.DATATYPE_STRING)
    public String description;

    @SerializedName("event_date")
    @TableField(name = "event_date",datatype = Entity.DATATYPE_STRING)
    public String event_date;


    @SerializedName("main_img")
    @TableField(name = "main_img",datatype = Entity.DATATYPE_STRING)
    public String main_img;

    @SerializedName("eventTotalComments")
    @TableField(name = "eventTotalComments",datatype = Entity.DATATYPE_INTEGER)
    public int eventTotalComments;

    @SerializedName("eventTotalLikes")
    @TableField(name = "eventTotalLikes",datatype = Entity.DATATYPE_INTEGER)
    public int eventTotalLikes;

    @SerializedName("is_like")
    @TableField(name = "is_like",datatype = Entity.DATATYPE_INTEGER)
    public int is_like;

    @SerializedName("eventImages")
    List<EventGallery> list=new ArrayList<>();




//    public String message;
//    public Date date;
}
// "event_id": 1,
//         "title": "Janmashtami Celebration",
//         "description": "On Saturday 28th Aug. 2010, the school invited students of Std. I with their parents for Janmashtami celebration.  They participated in a performance on the life of Lord Krishna.   The performance was put up by the members of an Educational Trust that provides value-based education to children.  It was an impressive performance wherein the members narrated, in simple language,  events from the life of Lord Krishna and enacted some of them. It was a combination of story-telling and singing. They encouraged children and teachers also to participate in the performance.  Everyone in the audience enjoyed the performance very much.",
//         "user_id": "1",
//         "user_type": "admin",
//         "create_at": "2017-08-23 01:00:04",
//         "event_date": "08/15/2017",