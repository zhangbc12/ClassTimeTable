package cs.hku.classtimetable;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ListActivity;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;

import android.os.Bundle;
import android.widget.Toast;

public class MenuActivity extends AppCompatActivity {
    Button btn_Course, btn_Event, btn_Test_Course, btn_Test_Event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        btn_Course = findViewById(R.id.btn_course);
        btn_Event = findViewById(R.id.btn_event);
        btn_Test_Course = findViewById(R.id.btn_test_course);
        btn_Test_Event = findViewById(R.id.btn_test_event);

        btn_Course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CourseActivity.class);
                startActivity(intent);
            }
        });
        btn_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), EventActivity.class);
                startActivity(intent);
            }
        });
        btn_Test_Course.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = MainActivity.DB.getCourseData();
                if(res.getCount()==0){
                    Toast.makeText(MenuActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Course :"+res.getString(0)+"\n");
                    buffer.append("Teacher :"+res.getString(1)+"\n");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
        btn_Test_Event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = MainActivity.DB.getEventData();
                if(res.getCount()==0){
                    Toast.makeText(MenuActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()){
                    buffer.append("Event :"+res.getString(0)+"\n");
                    buffer.append("Date :"+res.getString(1)+"\n");
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setCancelable(true);
                builder.setTitle("User Entries");
                builder.setMessage(buffer.toString());
                builder.show();
            }
        });
    }
}