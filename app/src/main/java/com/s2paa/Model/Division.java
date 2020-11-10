package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

/**
 * Created by admin on 8/18/2017.
 */

@Table(name = "division")
public class Division extends Entity implements Serializable {

    @TableField(name = "section_id",datatype = Entity.DATATYPE_STRING)
    public String section_id;

    @TableField(name = "class_id",datatype = Entity.DATATYPE_STRING)
    public String class_id;

    @TableField(name = "name",datatype = Entity.DATATYPE_STRING)
    public String name;

}
