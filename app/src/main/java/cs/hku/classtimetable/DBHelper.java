package cs.hku.classtimetable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "testdb.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table CourseTeacher(course TEXT primary key, teacher TEXT)");
        sqLiteDatabase.execSQL("create Table EventDate(event TEXT primary key, date TEXT)");
        sqLiteDatabase.execSQL("create Table AllCourse(id TEXT primary key, name TEXT)");
        sqLiteDatabase.execSQL("create Table AllSession(id_name_session TEXT primary key, date TEXT, time TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("drop Table if exists CourseTeacher");
        sqLiteDatabase.execSQL("drop Table if exists EventDate");
        sqLiteDatabase.execSQL("drop Table if exists AllCourse");
        sqLiteDatabase.execSQL("drop Table if exists AllSession");
    }

    public void clearOldTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        this.onUpgrade(sqLiteDatabase, 0, 1);
        this.onCreate(sqLiteDatabase);
    }

    public void clearSessionTable() {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("drop Table if exists AllSession");
        sqLiteDatabase.execSQL("create Table AllSession(id_name_session TEXT primary key, date TEXT, time TEXT)");
    }

    public Boolean insertCourseData(String course, String teacher)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("course", course);
        contentValues.put("teacher", teacher);
        long result=sqLiteDatabase.insert("CourseTeacher", null, contentValues);
        return result != -1;
    }

    public Boolean insertEventData(String event, String date)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("date", date);
        long result=sqLiteDatabase.insert("EventDate", null, contentValues);
        return result != -1;
    }

    public Boolean insertAllCourseData(String id, String name) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        long result=sqLiteDatabase.insert("AllCourse", null, contentValues);
        return result != -1;
    }

    public Boolean insertAllSessionData(String id_name_session, String date, String time) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_name_session", id_name_session);
        contentValues.put("date", date);
        contentValues.put("time",time);
        long result=sqLiteDatabase.insert("AllSession", null, contentValues);
        return result != -1;
    }

    public Boolean updateCourseData(String course, String teacher)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("course", course);
        contentValues.put("teacher", teacher);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from CourseTeacher where course = ?", new String[]{course});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.update("CourseTeacher", contentValues, "course=?", new String[]{course});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateEventData(String event, String date)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("date", date);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from EventDate where event = ?", new String[]{event});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.update("EventDate", contentValues, "event=?", new String[]{event});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateAllCourseData(String id, String name)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id", id);
        contentValues.put("name", name);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from AllCourse where id = ?", new String[]{id});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.update("AllCourse", contentValues, "id=?", new String[]{id});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public Boolean updateAllSessionData(String id_name_session, String date, String time)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_name_session", id_name_session);
        contentValues.put("date", date);
        contentValues.put("time",time);
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from AllSession where id_name_session = ?", new String[]{id_name_session});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.update("AllSession", contentValues, "id_name_session=?", new String[]{id_name_session});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deleteSession (String id_name_session)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from AllSession where id_name_session = ?", new String[]{id_name_session});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.delete("AllSession", "id_name_session=?", new String[]{id_name_session});
            return result != -1;
        } else {
            return false;
        }
    }

    public Boolean deleteEvent (String event) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from EventDate where event = ?", new String[]{event});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.delete("EventDate", "event=?", new String[]{event});
            return result != -1;
        } else {
            return false;
        }
    }

    public Cursor getCourseData ()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from CourseTeacher", null);
        return cursor;
    }

    public Cursor getEventData ()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from EventDate", null);

        return cursor;
    }

    public ArrayList<ArrayList<String>> getMyCourse()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from CourseTeacher", null);
        ArrayList<String> myCourseId = new ArrayList<String>();
        ArrayList<String> myCourseName = new ArrayList<String>();
        while(cursor.moveToNext()){
            myCourseId.add(cursor.getString(0));
        }
        ArrayList<String> valueCourseId = new ArrayList<String>();
        ArrayList<String> valueCourseName = new ArrayList<String>();
        for(int i = 0; i < myCourseId.size(); i++) {
            String courseId = myCourseId.get(i).substring(0, 8);
            int idx = myCourseId.get(i).indexOf("Section");
            if(idx == -1) continue;
            courseId += myCourseId.get(i).substring(idx + 9, idx + 10);
            Cursor res = sqLiteDatabase.rawQuery("Select * from AllCourse where id = ?", new String[]{courseId});
            if(res.getCount() != 0) {
                valueCourseId.add(courseId);
                res.moveToNext();
                valueCourseName.add(res.getString(1));
            }
        }
        ArrayList<ArrayList<String>> course_id_name = new ArrayList<ArrayList<String>>();
        course_id_name.add(valueCourseId);
        course_id_name.add(valueCourseName);
        return course_id_name;
    }

    public ArrayList<ArrayList<String>> getCourseOfDay(String date) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ArrayList<String> courseInfoList = new ArrayList<String>();
        ArrayList<String> courseTimeList = new ArrayList<String>();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from AllSession where date = ?", new String[]{date});
        while(cursor.moveToNext()){
            courseInfoList.add(cursor.getString(0));
            courseTimeList.add(cursor.getString(2));
        }
        ArrayList<ArrayList<String>> courseInfoTime = new ArrayList<ArrayList<String>>();
        courseInfoTime.add(courseInfoList);
        courseInfoTime.add(courseTimeList);
        return courseInfoTime;
    }
}