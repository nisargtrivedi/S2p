package com.s2paa.Application;

import android.app.Application;
import android.content.Context;
import android.preference.Preference;
import android.widget.Toast;

import com.s2paa.Utils.AppPreferences;

/**
 * Created by admin on 7/4/2017.
 */

public class SMS   {


    public  static  String  url;
    Context context;
    public SMS(String url,Context context){
        this.url=url;
        this.context=context;
        //Toast.makeText(context,url,Toast.LENGTH_LONG).show();
    }


    public static String ENDPOINT="http://school2parent.com/superadmin/index.php?mobile/";
   // public static String ENDPOINT="http://school2parent.com/srimirambika/index.php?mobile/";
   // public static String ENDPOINT=url;

    public static String FEES=ENDPOINT+"get_student_payment";
    public static String CLASS_ACTIVITY=  ENDPOINT + "get_class_activity";
    public static String SAVE_CLASS_ACTIVITY=ENDPOINT + "classActivitysave";
    public static String LOGIN= ENDPOINT+"login";
    public static String GCM=ENDPOINT+"update_gcm";
    public static String STANDARD=ENDPOINT +"get_class";
    public static String TIME_TABLE=ENDPOINT +"get_timetable";
    public static String EXAM_SCHEDULE=ENDPOINT +"get_exam_schedule";
    public static String FORGOT_PASSWORD=ENDPOINT +"reset_password";
    public static String HOME_WORK=ENDPOINT+"getDailyHomeworkForUserId";
    public static String HOME_WORK_TEACHER=ENDPOINT+"getDailyHomework";
    public static String CHANGE_PASSWORD=ENDPOINT+"change_password";
    public static String GET_STUDENT_FOR_CLASS= ENDPOINT+"get_students_of_class_Android";
    public static String SAVE=ENDPOINT+"saveAttendance";
    public static String SAVE_HOMEWORK=ENDPOINT+"saveHomework";
    public static String GET_ATTENDANCE=ENDPOINT+"getDailyAttandanceForUserId";
    public static String STUDENT_LEAVE=ENDPOINT+"get_student_leavList";
    public static String SAVE_LEAVE=ENDPOINT+"saveLeave";
    public static String PARENTS_LEAVE=ENDPOINT+"get_all_leavList";
    public static String EXAM_LIST=ENDPOINT+"get_exam_list";
    public static String ANNOUNCEMENT=ENDPOINT+"announcement";
    public static String REMARKS=ENDPOINT+"get_remarkByStd";
    public static String SAVE_REMARKS=ENDPOINT+"saveRemark";
    public static String GET_EVENT=ENDPOINT+"allEvents";
    public static String LIKES_EVENT=ENDPOINT+"eventLikes";
    public static String GET_MARKS_BY_SUBJECT=ENDPOINT+"get_marks_by_class_subject";
    public static String GET_SUBJECT_OF_CLASS=ENDPOINT+"get_subject_of_class";
    public static String GET_MARKS_BY_STUDENT=ENDPOINT+"get_marks_by_std";
    public static String LEAVE_APPROVE_BY_TEACHER=ENDPOINT+"leaveApproveByTeacher";
    public static String MAIN_SCHOOL="http://school2parent.com/superadmin/index.php?mobile/school_manage";
    public static String GET_COMMENT=ENDPOINT + "eventCommentsbyID";
    public static String EVENT_COMMENT=ENDPOINT+"eventComments";
    public static String ADD_RESULT=ENDPOINT+"addupdateResult";
    public static String UPDATE_RESULT=ENDPOINT+"saveResult";
    public static String GET_CALENDAR_DATA=ENDPOINT+"allCalender";
    public static String FEED_BACK=ENDPOINT+"send_feedback";
    public static String HOLIDAY_LEAVE=ENDPOINT+"allHolidayLeave";
    public static String GALLARY=ENDPOINT+"allGallery";


}
