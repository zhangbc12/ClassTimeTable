package cs.hku.classtimetable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "testdb.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create Table CourseTeacher(course TEXT primary key, teacher TEXT)");
        sqLiteDatabase.execSQL("create Table EventDate(event TEXT primary key, date TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int ii) {
        sqLiteDatabase.execSQL("drop Table if exists CourseTeacher");
        sqLiteDatabase.execSQL("drop Table if exists EventDate");
    }

    public Boolean insertCourseData(String course, String teacher)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("course", course);
        contentValues.put("teacher", teacher);
        long result=sqLiteDatabase.insert("CourseTeacher", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
    }
    public Boolean insertEventData(String event, String date)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("event", event);
        contentValues.put("date", date);
        long result=sqLiteDatabase.insert("EventDate", null, contentValues);
        if(result==-1){
            return false;
        }else{
            return true;
        }
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
    public Boolean deleteData (String course)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from CourseTeacher where course = ?", new String[]{course});
        if (cursor.getCount() > 0) {
            long result = sqLiteDatabase.delete("CourseTeacher", "course=?", new String[]{course});
            if (result == -1) {
                return false;
            } else {
                return true;
            }
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
}