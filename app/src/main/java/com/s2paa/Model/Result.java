package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by admin on 9/4/2017.
 */

@Table(name = "result")
public class Result extends Entity {


    @SerializedName("mark_id")
    @TableField(name = "mark_id",datatype = Entity.DATATYPE_INTEGER)
    public int mark_id;

    @SerializedName("class_id")
    @TableField(name = "class_id",datatype = Entity.DATATYPE_INTEGER)
    public int class_id;

    @SerializedName("exam_id")
    @TableField(name = "exam_id",datatype = Entity.DATATYPE_INTEGER)
    public int exam_id;

    @SerializedName("mark_total")
    @TableField(name = "mark_total",datatype = Entity.DATATYPE_INTEGER)
    public int mark_total;

    @SerializedName("exam_subject_id")
    @TableField(name = "exam_subject_id",datatype = Entity.DATATYPE_INTEGER)
    public int exam_subject_id;

    @SerializedName("mark_obtained")
    @TableField(name = "mark_obtained",datatype = Entity.DATATYPE_STRING)
    public String mark_obtained;

    @SerializedName("subject")
    @TableField(name = "subject",datatype = Entity.DATATYPE_STRING)
    public String subject;

    @SerializedName("student_id")
    @TableField(name = "student_id",datatype = Entity.DATATYPE_INTEGER)
    public String student_id;

    @SerializedName("student_name")
    @TableField(name = "student_name",datatype = Entity.DATATYPE_STRING)
    public String student_name;

    @SerializedName("student_roll")
    @TableField(name = "student_rollno",datatype = Entity.DATATYPE_STRING)
    public String student_rollno;

    @SerializedName("student_img")
    @TableField(name = "student_img",datatype = Entity.DATATYPE_STRING)
    public String student_img;



//    mark_id": 1,
//            "class_id": 1,
//            "exam_id": 4,
//            "mark_total": 50,
//            "exam_subject_id": 12,
//            "mark_obtained": "30",
//            "comment": "",
//            "subject": "English",
//            "student_id": 1,
//            "student_name": "Student",
//            "student_rollno": "1",
//            "student_img": "http://school2parent.com/superadmin/uploads/student_image/1.jpg"

}
