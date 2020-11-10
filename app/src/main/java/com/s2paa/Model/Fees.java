package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;
import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

/**
 * Created by nisarg on 09/11/17.
 */

@Table(name = "fees")
public class Fees extends Entity {



    @SerializedName("payment_term")
    @TableField(name = "title",datatype = Entity.DATATYPE_STRING)
    public String title;

    @SerializedName("student_id")
    @TableField(name = "student_id",datatype = Entity.DATATYPE_STRING)
    public String student_id;

    @SerializedName("fees_status")
    @TableField(name = "fees_status",datatype = Entity.DATATYPE_STRING)
    public String fees_status;

//"id": 8,
//        "student_id": 1,
//        "amount": "1500",
//        "term_id": 1,
//        "payment_type": "Cash",
//        "create_at": "2017-11-08 05:03:20",
//        "checkNumber": "",
//        "status": "Paid",
//        "checkDetail": "",
//        "api_date": "2017-11-08",
//        "payment_term": "May to Aug Payment",
//        "student_name": "Student"

}

