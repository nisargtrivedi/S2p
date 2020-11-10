package com.s2paa.Model;

import android.util.Log;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 8/2/2017.
 */

@Table(name = "AttendanceModel")
public class AttendanceModel extends Entity {

    @TableField(name = "date",datatype = Entity.DATATYPE_STRING)
    public String date;

    @TableField(name = "standard",datatype = Entity.DATATYPE_STRING)
    public String standard;

    @TableField(name = "division",datatype = Entity.DATATYPE_STRING)
    public String division;


    @TableField(name = "present_absent",datatype = Entity.DATATYPE_STRING)
    public String present_absent;

    @TableField(name = "student_id",datatype = Entity.DATATYPE_STRING)
    public String student_id;

    @TableField(name = "student_name",datatype = Entity.DATATYPE_STRING)
    public String student_name;

    @TableField(name = "image_url",datatype = Entity.DATATYPE_STRING)
    public String image_url;

    @TableField(name = "student_status",datatype = Entity.DATATYPE_STRING)
    public String student_status;

    @TableField(name = "attendance_id",datatype = Entity.DATATYPE_STRING)
    public String attendance_id;

    public ArrayList<AttendanceModel> arrayList=new ArrayList<>();
    public ArrayList<AttendanceModel> getAttendanceData(JSONArray array){

        for(int i=0;i<array.length();i++){
            AttendanceModel attendanceModel=new AttendanceModel();
            try {
                attendanceModel.student_id=array.getJSONObject(i).getString("student_id");
                attendanceModel.student_name=array.getJSONObject(i).getString("name");
                attendanceModel.image_url=array.getJSONObject(i).getString("image_url");
                JSONObject object=array.getJSONObject(i).getJSONObject("attendance_details");
                Log.i("attendance_details",object.toString());
               /// if(object.length()>0) {
               //     for(int j=0;j<object.length();j++)
                        attendanceModel.present_absent = object.getString("student_status");
              //  }
                arrayList.add(attendanceModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
    public ArrayList<AttendanceModel> SaveAttendanceData(JSONArray array){

        for(int i=0;i<array.length();i++){
            AttendanceModel attendanceModel=new AttendanceModel();
            try {
                attendanceModel.student_id=array.getJSONObject(i).getString("student_id");
                attendanceModel.student_status=array.getJSONObject(i).getString("student_status");
                attendanceModel.attendance_id=array.getJSONObject(i).getString("attendance_id");
                arrayList.add(attendanceModel);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return arrayList;
    }
}
//                            "attendance_id" = 1;
//                            date = "2017-08-23";
//                            status = 2;
//                            "student_id" = 2;
//                            "student_status" = Absent;
//                        },
//{
//        "Response": [
//        {
//        "student_id": "1",
//        "name": "Student",
//        "birthday": "10/13/2009",
//        "gender": "male",
//        "address": "Sector 14, India House, New Delhi",
//        "phone": "9955996699",
//        "email": "student@student.com",
//        "roll": "1",
//        "class": "Nursery",
//        "section": "Nursery A",
//        "parent_name": "Parent One",
//        "parent_phone": "9966332211",
//        "parent_email": "parent@parent.com",
//        "parent_address": "Address of parent one",
//        "parent_profession": "Doctor",
//        "image_url": "http://school2parent.com/dev/superadmin/uploads/student_image/1.jpg"
//        },
