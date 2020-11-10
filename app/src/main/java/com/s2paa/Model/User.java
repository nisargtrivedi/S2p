package com.s2paa.Model;

import com.mobandme.ada.Entity;
import com.mobandme.ada.annotations.Table;
import com.mobandme.ada.annotations.TableField;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by admin on 7/4/2017.
 */

@Table(name="Login")
public class User extends Entity implements Serializable {


    @TableField(name="teacher_id",datatype = Entity.DATATYPE_STRING)
    public String teacher_id;

    @TableField(name="student_id",datatype = Entity.DATATYPE_STRING)
    public String student_id;

    @TableField(name="name",datatype = Entity.DATATYPE_STRING)
    public String name;

    @TableField(name="profile_img",datatype = Entity.DATATYPE_STRING)
    public String profile_img;

    @TableField(name="email",datatype = Entity.DATATYPE_STRING)
    public String email;

    @TableField(name="authentication_key",datatype = Entity.DATATYPE_STRING)
    public String authentication_key;

    @TableField(name="sex",datatype = Entity.DATATYPE_STRING)
    public String sex;

    @TableField(name="rollno",datatype = Entity.DATATYPE_STRING)
    public String rollno;

    @TableField(name="address",datatype = Entity.DATATYPE_STRING)
    public String address;

    @TableField(name="phone",datatype = Entity.DATATYPE_STRING)
    public String phone;

    @TableField(name="login_type",datatype = Entity.DATATYPE_STRING)
    public String login_type;

    @TableField(name="status",datatype = Entity.DATATYPE_STRING)
    public String status;

    @TableField(name="class_id",datatype = Entity.DATATYPE_STRING)
    public String class_id;

    @TableField(name="class_name",datatype = Entity.DATATYPE_STRING)
    public String class_name;

    @TableField(name="section_id",datatype = Entity.DATATYPE_STRING)
    public String section_id;

    @TableField(name="section_name",datatype = Entity.DATATYPE_STRING)
    public String section_name;

    @TableField(name="gr_no",datatype = Entity.DATATYPE_STRING)
    public String gr_no;

    @TableField(name="last_name",datatype = Entity.DATATYPE_STRING)
    public String last_name;

    public String mother_image;

    public ArrayList<subUserDetails> list=new ArrayList<>();

    subUserDetails subUserDetails=new subUserDetails();


    public boolean isValid(User user){
                if(user.status.toString().equalsIgnoreCase("failed"))
                        return false;
                else if(user.status.toString().equalsIgnoreCase("Wrong Username or Password"))
                        return false;
                else
                        return true;
        }


//    address = thaltej;
//    "authentication_key" = 525269a3844d8493ef412c60c52c5067;
//    birthday = "28-7-1991";
//    "blood_group" = "";
//    "class_name" = "<null>";
//    email = "teacher@teacher.com";
//    "last_name" = "";
//    "login_type" = teacher;
//    "login_user_id" = 1;
//    name = "Teacher One";
//    password = teacher;
//    phone = 1234567890;
//    profession = "";
//    "profession_address" = "";
//    "profile_img" = "http://school2parent.com/dev/superadmin/uploads/teacher_image/1.jpg (1MB) ";
//    religion = "";
//    "section_name" = "<null>";
//    sex = male;
//    status = success;
//    subUserDetails =     (
//            );
//    "teacher_id" = 1;
//}
        public JSONObject StoreData(JSONObject jobject){
            JSONObject object=new JSONObject();
                try {
                    object = jobject.getJSONObject("Response");
                    status = object.getString("status");
                    if (status.toString().equalsIgnoreCase("Wrong Username or Password")) {
                        return object;
                    } else {
                        login_type = object.getString("login_type");

                        if(login_type.equalsIgnoreCase("parent"))
                        {
                            mother_image=object.getString("mother_img");
                        }
                        if (login_type.equalsIgnoreCase("student"))
                            student_id = object.getString("student_id");
                        else if (login_type.equalsIgnoreCase("teacher"))
                            teacher_id = object.getString("teacher_id");

                        name = object.getString("name");
                        authentication_key = object.getString("authentication_key");
                        email = object.getString("email");
                        address = object.getString("address");
                        phone = object.getString("phone");

                        if (login_type.equalsIgnoreCase("student"))
                            rollno = object.getString("roll");

                        profile_img = object.getString("profile_img");

                        if (login_type.equalsIgnoreCase("student"))
                            class_id = object.getString("class_id");

                        if (login_type.equalsIgnoreCase("student") && login_type.equalsIgnoreCase("teacher")) {
                            section_id = object.getString("section_id");
                            last_name = object.getString("last_name");
                        }

                        if (login_type.equalsIgnoreCase("student"))
                            gr_no = object.getString("gr_no");

                        if (login_type.equalsIgnoreCase("student"))
                            class_name = object.getString("class_name");

                        if (login_type.equalsIgnoreCase("student"))
                            section_name = object.getString("section_name");
                        // JSONArray subData=object.getJSONArray("subUserDetails");
//                    if(subData.length()>0) {
//                        list=StudentParents(subData);
//                    }
                    }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                return object;
        }
    public ArrayList<subUserDetails> StudentParents(JSONArray jsonArray)
    {
        ArrayList<subUserDetails> subUserDetailses=new ArrayList<>();
        try{
            for(int i=0;i<jsonArray.length();i++) {
                subUserDetails.name = jsonArray.getJSONObject(i).getString("name").toString();
                subUserDetails.email = jsonArray.getJSONObject(i).getString("email").toString();
                subUserDetails.phone = jsonArray.getJSONObject(i).getString("phone").toString();
                subUserDetails.profession = jsonArray.getJSONObject(i).getString("profession").toString();
                subUserDetails.mother_name = jsonArray.getJSONObject(i).getString("mother_name").toString();
                subUserDetails.profile_img = jsonArray.getJSONObject(i).getString("profile_img").toString();
                subUserDetails.mother_img = jsonArray.getJSONObject(i).getString("mother_img").toString();

                subUserDetailses.add(subUserDetails);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return subUserDetailses;
    }

}
//"student_id":"1",
//        "name":"Student",
//        "birthday":"10\/13\/2009",
//        "sex":"male",
//        "religion":"Hindu",
//        "blood_group":"",
//        "address":"Sector 14, India House, New Delhi",
//        "phone":"9955996699",
//        "email":"student@student.com",
//        "password":"student",
//        "father_name":"",
//        "mother_name":"",
//        "class_id":"1",
//        "section_id":"1",
//        "parent_id":"1",
//        "roll":"1",
//        "transport_id":"1",
//        "dormitory_id":"1",
//        "dormitory_room_number":"",
//        "authentication_key":"81bcb2f7ca6eb7f45107e1e31402ee7b",
//        "gr_no":"234",
//        "last_name":"One",
//        "active_status":"1",
//        "profile_img":"http:\/\/school2parent.com\/superadmin\/uploads\/student_image\/1.jpg"

//"parent_id": "1",
//        "name": "Parent One",
//        "email": "parent@parent.com",
//        "password": "parent",
//        "phone": "9966332211",
//        "address": "Address of parent one",
//        "profession": "Doctor",
//        "authentication_key": "9fc95161fe3faa936768cccf5d956798",
//        "profession_address": "Test Address",
//        "mother_profession": "House Wife",
//        "mother_name": "My Mother",
//        "nationality": "Indian",
//        "profile_img": "http://school2parent.com/dev/superadmin/uploads/parent_image/1.jpg",
//        "mother_img": "http://school2parent.com/dev/superadmin/uploads/mother_image/1.jpg"

//        address = "Sector 14, India House, New Delhi";
//        "authentication_key" = 3debb8ccebf9f4a1ea9a7393791e42e6;
//        birthday = "10/13/2009";
//        "blood_group" = "";
//        "class_id" = 1;
//        "dormitory_id" = 1;
//        "dormitory_room_number" = "";
//        email = "student@student.com";
//        "father_name" = "";
//        "gr_no" = 234;
//        "last_name" = One;
//        "mother_name" = "";
//        name = Student;
//        "parent_id" = 1;
//        password = student;
//        phone = 9955996699;
//        "profile_img" = "http://school2parent.com/dev/superadmin/uploads/student_image/1.jpg (8kB) ";
//        religion = Hindu;
//        roll = 1;
//        "section_id" = 1;
//        sex = male;
//        "student_id" = 1;
//        "transport_id" = 1;