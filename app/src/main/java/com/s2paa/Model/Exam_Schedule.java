package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by admin on 8/1/2017.
 */

@Table(name = "Exam_schedule")
public class Exam_Schedule extends Entity {
   @TableField(name = "exam_id",datatype = Entity.DATATYPE_STRING)
   public String exam_id;
   @TableField(name = "subject_id",datatype = Entity.DATATYPE_STRING)
   public String subject_id;
   @TableField(name = "day",datatype = Entity.DATATYPE_STRING)
   public String day;
   @TableField(name = "date",datatype = Entity.DATATYPE_STRING)
   public String exam_date;
   @TableField(name = "subject",datatype = Entity.DATATYPE_STRING)
   public String subject;
   @TableField(name = "classroom",datatype = Entity.DATATYPE_STRING)
   public String classes;
   @TableField(name = "fromtime",datatype = Entity.DATATYPE_STRING)
   public String from_time;
   @TableField(name = "totime",datatype = Entity.DATATYPE_STRING)
   public String to_time;
   @TableField(name = "class_id",datatype = Entity.DATATYPE_STRING)
   public String class_id;
   @TableField(name = "exam_schdule_id",datatype = Entity.DATATYPE_STRING)
   public String exam_schdule_id;

//   public String exam_date,subject,from_time,to_time,classes;

   public ArrayList<Exam_Schedule> ReponseData(JSONArray object){

      ArrayList<Exam_Schedule> list=new ArrayList<>();
      try {
         for(int i=0;i<object.length();i++) {
            Exam_Schedule model=new Exam_Schedule();
            model.exam_date =object.getJSONObject(i).getString("day").toString()+", "+ object.getJSONObject(i).getString("date").toString();
            model.subject = object.getJSONObject(i).getString("subject").toString();
            model.from_time = object.getJSONObject(i).getString("fromtime").toString();
            model.to_time = object.getJSONObject(i).getString("totime").toString();
            model.classes = object.getJSONObject(i).getString("classroom").toString();
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
