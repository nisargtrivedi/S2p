package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by admin on 9/14/2017.
 */

@Table(name = "ParentStudent")
public class ParentStudent extends Entity {


    @SerializedName("student_id")
    @TableField(name ="student_id",datatype = Entity.DATATYPE_STRING)
    public String student_id;

    @SerializedName("name")
    @TableField(name ="name",datatype = Entity.DATATYPE_STRING)
    public String name;

    @SerializedName("last_name")
    @TableField(name ="last_name",datatype = Entity.DATATYPE_STRING)
    public String last_name;

    @SerializedName("address")
    @TableField(name ="address",datatype = Entity.DATATYPE_STRING)
    public String address;

    @SerializedName("phone")
    @TableField(name ="phone",datatype = Entity.DATATYPE_STRING)
    public String phone;

    @SerializedName("email")
    @TableField(name ="email",datatype = Entity.DATATYPE_STRING)
    public String email;

    @SerializedName("class_id")
    @TableField(name ="class_id",datatype = Entity.DATATYPE_STRING)
    public String class_id;

    @SerializedName("section_id")
    @TableField(name ="section_id",datatype = Entity.DATATYPE_STRING)
    public String section_id;

    @SerializedName("section_name")
    @TableField(name ="section_name",datatype = Entity.DATATYPE_STRING)
    public String section_name;

    @SerializedName("class_name")
    @TableField(name ="class_name",datatype = Entity.DATATYPE_STRING)
    public String class_name;

    @SerializedName("parent_id")
    @TableField(name ="parent_id",datatype = Entity.DATATYPE_STRING)
    public String parent_id;

    @SerializedName("profile_img")
    @TableField(name ="profile_img",datatype = Entity.DATATYPE_STRING)
    public String profile_img;

    @SerializedName("father_name")
    @TableField(name ="father_name",datatype = Entity.DATATYPE_STRING)
    public String father_name;

    @SerializedName("mother_name")
    @TableField(name ="mother_name",datatype = Entity.DATATYPE_STRING)
    public String mother_name;

    @SerializedName("mother_img")
    @TableField(name ="mother_img",datatype = Entity.DATATYPE_STRING)
    public String mother_img;


    @SerializedName("roll")
    @TableField(name ="roll",datatype = Entity.DATATYPE_STRING)
    public String roll;

}
//"student_id":"1",
//        "name":"Student",
//        "birthday":"10\/13\/2009",
//        "sex":"male",
//        "religion":"Hindu",
//        "blood_group":"",
//        "address":"Sector 14, India House, New Delhi",
//        "phone":"9955996699",
//        "email":"student@student.com",
//        "password":"student",
//        "father_name":"",
//        "mother_name":"",
//        "class_id":"1",
//        "section_id":"1",
//        "parent_id":"1",
//        "roll":"1",
//        "transport_id":"1",
//        "dormitory_id":"1",
//        "dormitory_room_number":"",
//        "authentication_key":"81bcb2f7ca6eb7f45107e1e31402ee7b",
//        "gr_no":"234",
//        "last_name":"One",
//        "active_status":"1",
//        "profile_img":"http:\/\/school2parent.com\/superadmin\/uploads\/student_image\/1.jpg"