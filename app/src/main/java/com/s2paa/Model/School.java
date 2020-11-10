package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

/**
 * Created by admin on 9/22/2017.
 */

@Table(name = "school")
public class School extends Entity implements Serializable {


    @TableField(name = "school_id",datatype = Entity.DATATYPE_INTEGER)
    public int school_id;

    @TableField(name = "school_name",datatype = Entity.DATATYPE_STRING)
    public String school_name;

    @TableField(name = "school_url",datatype = Entity.DATATYPE_STRING)
    public String school_url;

    @TableField(name = "school_code",datatype = Entity.DATATYPE_STRING)
    public String school_code;

}
