package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by admin on 8/8/2017.
 */

@Table(name="subUserDetails")
public class subUserDetails extends Entity {

    @TableField(name="parent_id",datatype = Entity.DATATYPE_STRING)
    public String parent_id;

    @TableField(name="name",datatype = Entity.DATATYPE_STRING)
    public String name;

    @TableField(name="email",datatype = Entity.DATATYPE_STRING)
    public String email;

    @TableField(name="phone",datatype = Entity.DATATYPE_STRING)
    public String phone;

    @TableField(name="profession",datatype = Entity.DATATYPE_STRING)
    public String profession;

    @TableField(name="mother_profession",datatype = Entity.DATATYPE_STRING)
    public String mother_profession;

    @TableField(name="mother_name",datatype = Entity.DATATYPE_STRING)
    public String mother_name;

    @TableField(name="profile_img",datatype = Entity.DATATYPE_STRING)
    public String profile_img;

    @TableField(name="mother_img",datatype = Entity.DATATYPE_STRING)
    public String mother_img;

}
