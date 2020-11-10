package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by admin on 9/12/2017.
 */

@Table(name = "result")
public class ExamResult extends Entity {

    @SerializedName("class_id")
    @TableField(name = "class_id",datatype = Entity.DATATYPE_INTEGER)
    public int class_id;

    @SerializedName("comment")
    @TableField(name = "comment",datatype = Entity.DATATYPE_STRING)
    public String comment;

    @SerializedName("exam_id")
    @TableField(name = "exam_id",datatype = Entity.DATATYPE_INTEGER)
    public int exam_id;

    @SerializedName("grade")
    @TableField(name = "grade",datatype = Entity.DATATYPE_STRING)
    public String grade;

    @SerializedName("mark_obtained")
    @TableField(name = "mark_obtained",datatype = Entity.DATATYPE_INTEGER)
    public int mark_obtained;

    @SerializedName("mark_total")
    @TableField(name = "mark_total",datatype = Entity.DATATYPE_INTEGER)
    public int mark_total;

    @SerializedName("section_id")
    @TableField(name = "section_id",datatype = Entity.DATATYPE_INTEGER)
    public int section_id;

    @SerializedName("student_img")
    @TableField(name = "student_img",datatype = Entity.DATATYPE_STRING)
    public String student_img;

    @SerializedName("student_name")
    @TableField(name = "student_name",datatype = Entity.DATATYPE_STRING)
    public String student_name;

    @SerializedName("student_roll")
    @TableField(name = "student_roll",datatype = Entity.DATATYPE_INTEGER)
    public int student_roll;

    @SerializedName("subject")
    @TableField(name = "subject",datatype = Entity.DATATYPE_STRING)
    public String subject;

}
// "class_id" = 1;
//         comment = "";
//         "exam_id" = 1;
//         grade = A;
//         "mark_obtained" = 0;
//         "mark_total" = 100;
//         "section_id" = 2;
//         "student_id" = 144;
//         "student_img" = "http://school2parent.com/superadmin/uploads/student_image/144.jpg (10kB)
//
//         ";
//         "student_name" = Rinkal;
//         "student_roll" = 5;
//         subject = Science;