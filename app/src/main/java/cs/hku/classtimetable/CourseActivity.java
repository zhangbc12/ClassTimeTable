package cs.hku.classtimetable;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CourseActivity extends AppCompatActivity {
    DBHelper DB = MainActivity.DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);
    }
}