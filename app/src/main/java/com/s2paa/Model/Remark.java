package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by admin on 9/1/2017.
 */

@Table(name = "remark")
public class Remark extends Entity {

    @TableField(name = "remarks_id",datatype = Entity.DATATYPE_STRING)
    public String remarks_id;
    @TableField(name = "title",datatype = Entity.DATATYPE_STRING)
    public String title;
    @TableField(name = "details",datatype = Entity.DATATYPE_STRING)
    public String details;

    @TableField(name = "api_date",datatype = Entity.DATATYPE_STRING)
    public String api_date;

    @TableField(name = "class_id",datatype = Entity.DATATYPE_STRING)
    public String class_id;

    @TableField(name = "status",datatype = Entity.DATATYPE_STRING)
    public String status;

    @TableField(name = "remarks_status",datatype = Entity.DATATYPE_STRING)
    public String remarks_status;

    @TableField(name = "class_name",datatype = Entity.DATATYPE_STRING)
    public String class_name;

    public ArrayList<Remark> getRemarks(JSONArray array) {
        ArrayList<Remark> list=new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Remark remark = new Remark();

            try {
                remark.title = array.getJSONObject(i).getString("title");
                remark.remarks_id = array.getJSONObject(i).getString("remarks_id");
                remark.class_id = array.getJSONObject(i).getString("class_id");
                remark.class_name = array.getJSONObject(i).getString("class_name");
                remark.details = array.getJSONObject(i).getString("details");
                remark.api_date = array.getJSONObject(i).getString("api_date");

                list.add(remark);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }
}
//"remarks_id":3,
//        "title":"Second Remarks",
//        "details":"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
//        "class_id":1,
//        "create_at":"2017-08-03 00:49:37",
//        "section_id":1,
//        "student_id":1,
//        "api_date":"2017-08-03",
//        "class_name":"Nursery",
//        "section_name":"A",
//        "student_name":"Student"
