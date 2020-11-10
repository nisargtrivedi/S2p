package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 7/31/2017.
 */

@Table(name="timetable")
public class TimeTable_Model extends Entity implements Serializable {

    @TableField(name = "start_time",datatype = Entity.DATATYPE_STRING)
    public String start_time;

    @TableField(name = "end_time",datatype = Entity.DATATYPE_STRING)
    public String end_time;

    @TableField(name = "teacher_name",datatype = Entity.DATATYPE_STRING)
    public String  teacher_name;

    @TableField(name = "subject_name",datatype = Entity.DATATYPE_STRING)
    public String subject_name;

    @TableField(name = "day",datatype = Entity.DATATYPE_STRING)
    public String day;

    @TableField(name = "clas",datatype = Entity.DATATYPE_STRING)
    public String clas;

    @TableField(name = "section",datatype = Entity.DATATYPE_STRING)
    public String section;

//             "day": "Monday",
//             "time": "10:30 - 11:30",
//             "class": "Nursery",
//             "section": "Nursery A",
//             "subject": "English",
//             "teacher": "Teacher One"
    public  ArrayList<TimeTable_Model> ReponseData(JSONArray object){

        ArrayList<TimeTable_Model> list=new ArrayList<>();
           try {
               for(int i=0;i<object.length();i++) {
                   TimeTable_Model model=new TimeTable_Model();
                   model.day = object.getJSONObject(i).getString("day").toString();
                   model.start_time = object.getJSONObject(i).getString("start_time").toString();
                   model.end_time = object.getJSONObject(i).getString("end_time").toString();
                   model.clas = object.getJSONObject(i).getString("class").toString();
                   model.section = object.getJSONObject(i).getString("section").toString();
                   model.subject_name = object.getJSONObject(i).getString("subject").toString();
                   model.teacher_name = object.getJSONObject(i).getString("teacher").toString();

                   list.add(model);
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
        return list;
    }
}
