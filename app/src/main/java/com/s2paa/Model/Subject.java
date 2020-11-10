package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import java.io.Serializable;

/**
 * Created by admin on 9/12/2017.
 */

@Table(name = "subject")
public class Subject extends Entity implements Serializable {
    @SerializedName("subject_id")
    @TableField(name = "subject_id",datatype = Entity.DATATYPE_INTEGER)
    public int subject_id;
    @SerializedName("name")
    @TableField(name = "name",datatype = Entity.DATATYPE_STRING)
    public String name;
    @SerializedName("teacher_name")
    @TableField(name = "teacher_name",datatype = Entity.DATATYPE_STRING)
    public String teacher_name;
}
// "Response": [
//         {
//         "subject_id": 16,
//         "name": "PT",
//         "teacher_name": ""
//         },
//         {
//         "subject_id": 13,
//         "name": "Hindi",
//         "teacher_name": ""
//         },
//         {
//         "subject_id": 6,
//         "name": "Science",
//         "teacher_name": ""
//         },
//         {
//         "subject_id": 3,
//         "name": "Drawing",
//         "teacher_name": ""
//         },
//         {
//         "subject_id": 1,
//         "name": "English",
//         "teacher_name": ""
//         }
//         ],
//         "status": "Success"
