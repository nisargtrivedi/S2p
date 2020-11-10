package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by admin on 8/1/2017.
 */

@Table(name = "Annoucement")
public class Annoucement extends Entity {

    @TableField(name = "message",datatype = Entity.DATATYPE_STRING)
    public String details;

    @TableField(name = "class",datatype = Entity.DATATYPE_STRING)
    public String  class_name_with_section;

    @TableField(name = "create_timestamp",datatype = Entity.DATATYPE_LONG)
    public String create_timestamp;

//     "message": "Annual Parent Meeting",
//             "create_timestamp": "1500641844",
//             "class": "Nursery - A"
}
