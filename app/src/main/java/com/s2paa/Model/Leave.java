package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 8/21/2017.
 */

@Table(name = "leave")
public class Leave extends Entity implements Serializable {

    @TableField(name="leave_application_id",datatype = Entity.DATATYPE_STRING)
    public String id;

    @TableField(name="title",datatype = Entity.DATATYPE_STRING)
    public  String title;

    @TableField(name="details",datatype = Entity.DATATYPE_STRING)
    public  String details;

    @TableField(name="create_at",datatype = Entity.DATATYPE_STRING)
    public String create_at;

    @TableField(name="leave_date",datatype = Entity.DATATYPE_STRING)
    public String leave_date;

    @TableField(name="leave_status",datatype = Entity.DATATYPE_STRING)
    public String leave_status;

    public ArrayList<Leave> getLeave(JSONArray array){

        ArrayList<Leave> list=new ArrayList<>();
        try {
            for(int i=0;i<array.length();i++) {
                Leave model=new Leave();
                model.id=array.getJSONObject(i).getString("leave_application_id").toString();
                model.title=array.getJSONObject(i).getString("title").toString();
                model.details=array.getJSONObject(i).getString("details").toString();
                model.leave_date=array.getJSONObject(i).getString("leave_date").toString();
                model.leave_status=array.getJSONObject(i).getString("leave_status").toString();
//                model.date =object.getJSONObject(i).getString("home_work_date").toString();
//                model.subject_name = object.getJSONObject(i).getString("subject").toString();
//                model.subject_details = object.getJSONObject(i).getString("homework").toString();
                //model.to_time = object.getJSONObject(i).getString("totime").toString();
                //model.classes = object.getJSONObject(i).getString("classroom").toString();
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

}

// "leave_application_id": "3",
//         "title": "Picnic Leave",
//         "details": "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged.",
//         "student_id": "20",
//         "create_at": "2017-08-01 04:22:26",
//         "status": "1",
//         "parent_id": "1",
//         "day_of_no": "2",
//         "comments": "",
//         "c_user_id": "",
//         "leave_date": "08/02/2017",
//         "parent_name": "Parent One",
//         "student_name": "RAKSHANA"
