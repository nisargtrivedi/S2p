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

@Table(name="exam_result")
public class ExamList extends Entity implements Serializable {

    @TableField(name = "exam_id",datatype = Entity.DATATYPE_STRING)
    public String exam_id;

    @TableField(name = "name",datatype = Entity.DATATYPE_STRING)
    public String name;

    @TableField(name = "date",datatype = Entity.DATATYPE_STRING)
    public String date;

    @TableField(name = "comment",datatype = Entity.DATATYPE_STRING)
    public String comment;

    @TableField(name = "class_id",datatype = Entity.DATATYPE_STRING)
    public String class_id;


    @TableField(name = "total_marks",datatype = Entity.DATATYPE_STRING)
    public String total_marks;

    @TableField(name = "times",datatype = Entity.DATATYPE_STRING)
    public String times;

public  ArrayList<Exam_Schedule> list=new ArrayList<>();
    public ArrayList<ExamList> ReponseData(JSONArray object){

        ArrayList<ExamList> list=new ArrayList<>();
        try {
            for(int i=0;i<object.length();i++) {
                ExamList model=new ExamList();
                model.date = object.getJSONObject(i).getString("exam_date").toString();
                model.name = object.getJSONObject(i).getString("exam_name").toString();
                model.total_marks = object.getJSONObject(i).getString("total_marks").toString();
                model.exam_id = object.getJSONObject(i).getString("exam_id").toString();
                //model.to_time = object.getJSONObject(i).getString("totime").toString();
                //model.classes = object.getJSONObject(i).getString("classroom").toString();
                ReponseExamSchedule(object.getJSONObject(i).getJSONArray("examschedule"));
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public ArrayList<Exam_Schedule> ReponseExamSchedule(JSONArray object){

        //ArrayList<Exam_Schedule> list=new ArrayList<>();
        try {
            for(int i=0;i<object.length();i++) {
                Exam_Schedule model=new Exam_Schedule();
                model.exam_date =object.getJSONObject(i).getString("day").toString()+", "+ object.getJSONObject(i).getString("date").toString();
                model.subject = object.getJSONObject(i).getString("subject").toString();
                model.from_time = object.getJSONObject(i).getString("fromtime").toString();
                model.to_time = object.getJSONObject(i).getString("totime").toString();
                model.classes = object.getJSONObject(i).getString("classroom").toString();
                model.class_id = object.getJSONObject(i).getString("class_id").toString();
                model.exam_id = object.getJSONObject(i).getString("exam_id").toString();
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
// "exam_id":1,
//         "subject_id":6,
//         "day":"Thursday",
//         "date":"2017-09-14",
//         "subject":"Science",
//         "classroom":"A",
//         "fromtime":"10:00",
//         "totime":"11:30",
//         "class_id":1,
//         "exam_schdule_id":1
