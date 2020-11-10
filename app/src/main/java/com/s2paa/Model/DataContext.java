package com.s2paa.Model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;


import com.mobandme.ada.ObjectContext;
import com.mobandme.ada.ObjectSet;
import com.mobandme.ada.exceptions.AdaFrameworkException;
import com.s2paa.Utils.AppLogger;
import com.s2paa.Utils.ExceptionsHelper;

import java.io.File;

public class DataContext extends ObjectContext {

    final static String DATABASE_FOLDER  = "%s/SMS/";
    final static String DATABASE_NAME    = "sms.db";
    final static int    DATABASE_VERSION = 4;

    public ObjectSet<User> UserSet;
    public ObjectSet<ClassActivity> classSet;
    public ObjectSet<Standard> standardObjectSet;
    public ObjectSet<Division> divisionObjectSet;
    public ObjectSet<TimeTable_Model> timeTable_modelObjectSet;
    public ObjectSet<AttendanceModel> attendanceModelObjectSet;
    public ObjectSet<ExamList> examListObjectSet;
    public ObjectSet<Remark> remarkObjectSet;
    public ObjectSet<EventObjects> eventObjectSet;
    public ObjectSet<EventGallery> eventGallerieset;
    public ObjectSet<Exam_Schedule> examScheduleObjectSet;
    public ObjectSet<ExamResult> examResultObjectSet;
    public ObjectSet<Subject> subjectObjectSet;
    public ObjectSet<ParentStudent> parentStudentObjectSet;
    public ObjectSet<School> schoolObjectSet;
    public ObjectSet<Comment> commentObjectSet;
    public ObjectSet<Fees>  feesObjectSet;



    public DataContext(Context pContext) {
      super(pContext, DATABASE_NAME, DATABASE_VERSION);
       // super(pContext, String.format("%s%s", getDataBaseFolder(), DATABASE_NAME), DATABASE_VERSION);
        initializeContext();
    }

    @Override
    protected void onPopulate(SQLiteDatabase pDatabase, int action) {
        try {
            AppLogger.info("On DB Populate:" + action);
        }
        catch (Exception e) {
            ExceptionsHelper.manage(getContext(), e);
        }
    }

    @Override
    protected void onError(Exception pException) {
        ExceptionsHelper.manage(getContext(), pException);
    }

    private void initializeContext() {
        try {
            initializeObjectSets();

            //Enable DataBase Transactions to be used by the Save process.
            this.setUseTransactions(true);

            //Enable the creation of DataBase table indexes.
            this.setUseTableIndexes(true);

            //Enable LazyLoading capabilities.
            //this.useLazyLoading(true);

            //Set a custom encryption algorithm.
            this.setEncryptionAlgorithm("AES");

            //Set a custom encryption master pass phrase.
            this.setMasterEncryptionKey("com.sms.app.items");

            //Initialize ObjectSets instances.
//            initializeObjectSets();

        } catch (Exception e) {
            ExceptionsHelper.manage(e);
        }
    }

    private static String getDataBaseFolder() {
        String folderPath = "";
        try {
            folderPath = String.format(DATABASE_FOLDER, Environment.getExternalStorageDirectory().getAbsolutePath());
            File dbFolder = new File(folderPath);
            if (!dbFolder.exists()) {
                dbFolder.mkdirs();
            }
        } catch (Exception e) {
            ExceptionsHelper.manage(e);
        }
        return folderPath;
    }

    private void initializeObjectSets() throws AdaFrameworkException {
        UserSet = new ObjectSet<User>(User.class,this);
        classSet=new ObjectSet<ClassActivity>(ClassActivity.class,this);
        standardObjectSet=new ObjectSet<Standard>(Standard.class,this);
        timeTable_modelObjectSet=new ObjectSet<TimeTable_Model>(TimeTable_Model.class,this);
        divisionObjectSet=new ObjectSet<Division>(Division.class,this);
        attendanceModelObjectSet=new ObjectSet<AttendanceModel>(AttendanceModel.class,this);
        examListObjectSet=new ObjectSet<ExamList>(ExamList.class,this);
        remarkObjectSet=new ObjectSet<Remark>(Remark.class,this);
        eventObjectSet=new ObjectSet<EventObjects>(EventObjects.class,this);
        eventGallerieset=new ObjectSet<EventGallery>(EventGallery.class,this);
        examScheduleObjectSet=new ObjectSet<Exam_Schedule>(Exam_Schedule.class,this);
        examResultObjectSet=new ObjectSet<ExamResult>(ExamResult.class,this);
        subjectObjectSet=new ObjectSet<Subject>(Subject.class,this);
        parentStudentObjectSet=new ObjectSet<ParentStudent>(ParentStudent.class,this);
        schoolObjectSet=new ObjectSet<School>(School.class,this);
        commentObjectSet=new ObjectSet<Comment>(Comment.class,this);
    feesObjectSet=new ObjectSet<Fees>(Fees.class,this);
    }
}

