package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 8/4/2017.
 */

@Table(name = "standard")
public class Standard extends Entity implements Serializable {

    @TableField(name = "class_id",datatype = Entity.DATATYPE_STRING)
    public String class_id;

    @TableField(name = "name",datatype = Entity.DATATYPE_STRING)
    public String name;

    @TableField(name = "teacher_id",datatype = Entity.DATATYPE_STRING)
    public String teacher_id;

    @TableField(name = "name_numeric",datatype = Entity.DATATYPE_STRING)
    public String name_numeric;

    ArrayList<Standard> list=new ArrayList<>();
   public ArrayList<Division> divisionArrayList=new ArrayList<>();
   public String clas_id;

    public ArrayList<Standard> getStandardDetails(JSONArray object){

        if(object.length()>0) {
            for (int i = 0; i < object.length(); i++){
                try {
                    Standard standard=new Standard();
                    standard.class_id = object.getJSONObject(i).getString("class_id");
                    standard.name = object.getJSONObject(i).getString("name");
                    standard.teacher_id = object.getJSONObject(i).getString("teacher_id");
                    clas_id=standard.class_id;
                    standard.name_numeric = object.getJSONObject(i).getString("name_numeric");
                    divisionArrayList= getDivision(object.getJSONObject(i).getJSONArray("division"));
                    list.add(standard);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        return list;
    }
    public ArrayList<Division> getDivision(JSONArray array){


        for(int i=0;i<array.length();i++) {
            try {
                Division division=new Division();
                division.section_id = array.getJSONObject(i).getString("section_id");
                division.name = array.getJSONObject(i).getString("name");
                division.class_id=clas_id;
                divisionArrayList.add(division);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return divisionArrayList;
    }
}
//{
//        "Response": [
//        {
//        "class_id": "1",
//        "name": "Nursery",
//        "name_numeric": "N",
//        "teacher_id": "1",
//        "division": [
//        {
//        "section_id": "1",
//        "name": "Nursery A"
//        },
//        {
//        "section_id": "2",
//        "name": "Nursery B"
//        }
//        ]
//        },
//        {
//        "class_id": "2",
//        "name": "KG",
//        "name_numeric": "k",
//        "teacher_id": "2",
//        "division": [
//        {
//        "section_id": "3",
//        "name": "KGA"
//        },
//        {
//        "section_id": "4",
//        "name": "KGB"
//        },
//        {
//        "section_id": "9",
//        "name": "KGC"
//        }
//        ]
//        },
//        {
//        "class_id": "3",
//        "name": "First",
//        "name_numeric": "1",
//        "teacher_id": "1",
//        "division": [
//        {
//        "section_id": "5",
//        "name": "First A"
//        },
//        {
//        "section_id": "6",
//        "name": "First B"
//        }
//        ]
//        },
//        {
//        "class_id": "4",
//        "name": "Second",
//        "name_numeric": "2",
//        "teacher_id": "2",
//        "division": [
//        {
//        "section_id": "7",
//        "name": "Second A"
//        },
//        {
//        "section_id": "8",
//        "name": "Second B"
//        }
//        ]
//        },
//        {
//        "class_id": "5",
//        "name": "senior KG",
//        "name_numeric": "SK",
//        "teacher_id": "3",
//        "division": []
//        }
//        ],
//        "status": "Success"
//        }