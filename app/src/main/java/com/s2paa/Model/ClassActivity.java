package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by admin on 8/3/2017.
 */

@Table(name = "class_activity")
public class ClassActivity extends Entity implements Serializable {

    @TableField(name = "class_activity_id",datatype = Entity.DATATYPE_STRING)
    public String class_activity_id;

    @TableField(name = "title",datatype = Entity.DATATYPE_STRING)
    public String title;

    @TableField(name = "details",datatype = Entity.DATATYPE_STRING)
    public  String details;
    @TableField(name = "class_id",datatype = Entity.DATATYPE_STRING)
    public  String class_id;

    @TableField(name = "create_at",datatype = Entity.DATATYPE_DATE)
    public Date create_at;

    @TableField(name = "status",datatype = Entity.DATATYPE_STRING)
    public  String status;

    @TableField(name = "class_name",datatype = Entity.DATATYPE_STRING)
    public String class_name;

}

