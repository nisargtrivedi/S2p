package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

/**
 * Created by admin on 9/26/2017.
 */

@Table(name = "comment")
public class Comment extends Entity implements Serializable {


    @SerializedName("e_comment_id")
    @TableField(name = "e_comment_id",datatype = Entity.DATATYPE_INTEGER)
    public int e_comment_id;

    @SerializedName("event_id")
    @TableField(name = "event_id",datatype = Entity.DATATYPE_INTEGER)
    public int event_id;


    @SerializedName("create_at")
    @TableField(name = "create_at",datatype = Entity.DATATYPE_STRING)
    public String create_at;

    @SerializedName("user_id")
    @TableField(name = "user_id",datatype = Entity.DATATYPE_STRING)
    public String user_id;

    @SerializedName("user_type")
    @TableField(name = "user_type",datatype = Entity.DATATYPE_STRING)
    public String user_type;

    @SerializedName("comments")
    @TableField(name = "comments",datatype = Entity.DATATYPE_STRING)
    public String comments;

    @SerializedName("api_date")
    @TableField(name = "api_date",datatype = Entity.DATATYPE_STRING)
    public String api_date;

    @SerializedName("img_url")
    @TableField(name = "img_url",datatype = Entity.DATATYPE_STRING)
    public String img_url;

    @SerializedName("user_name")
    @TableField(name = "user_name",datatype = Entity.DATATYPE_STRING)
    public String user_name;
}
//"e_comment_id": 9,
//        "event_id": 1,
//        "create_at": "2017-08-24 00:31:18",
//        "user_id": 1,
//        "user_type": "parent",
//        "comments": "Nice Events......",
//        "api_date": "2017-08-24",
//        "img_url": "http://school2parent.com/superadmin/uploads/parent_image/1.jpg",
//        "user_name": "Sunny Patel"
