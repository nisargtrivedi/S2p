package com.s2paa.Model;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by admin on 8/1/2017.
 */

public class Home_Work implements Serializable {
    public String subject_name,subject_character,subject_details,title,homework;
    public Date date;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    public ArrayList<Home_Work> StudentReponseData(JSONArray object){

        ArrayList<Home_Work> list=new ArrayList<>();
        try {
            for(int i=0;i<object.length();i++) {
                Home_Work model=new Home_Work();

                model.date =sdf.parse(object.getJSONObject(i).getString("homework_date").toString());
                model.subject_name = object.getJSONObject(i).getString("title").toString();
                model.subject_details = object.getJSONObject(i).getString("homework").toString();
                //model.to_time = object.getJSONObject(i).getString("totime").toString();
                //model.classes = object.getJSONObject(i).getString("classroom").toString();
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
    public ArrayList<Home_Work> ReponseData(JSONArray object){
        ArrayList<Home_Work> list=new ArrayList<>();
        try {
            for(int i=0;i<object.length();i++) {
                Home_Work model=new Home_Work();
                model.date =sdf.parse(object.getJSONObject(i).getString("homework_date").toString());
                model.subject_name = object.getJSONObject(i).getString("class_name").toString();
                model.subject_details = object.getJSONObject(i).getString("homework").toString();
                //model.to_time = object.getJSONObject(i).getString("totime").toString();
                //model.classes = object.getJSONObject(i).getString("classroom").toString();
                list.add(model);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return list;
    }
//    "class_name": "NurseryA",
//            "teacher_name": "Nisharg",
//            "homework": "my test",
//            "home_work_date": "2017-06-23"
}
