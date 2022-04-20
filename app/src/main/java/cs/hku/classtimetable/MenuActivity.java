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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

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
                ArrayList<ArrayList<String>> course_id_name = MainActivity.DB.getMyCourse();
                ArrayList<String> course_id = course_id_name.get(0);
                ArrayList<String> course_name = course_id_name.get(1);
                if(course_id.size() == 0) {
                    Toast.makeText(MenuActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
                    return;
                }

                ArrayList<Courseinfo> courseinfoArrayList = new ArrayList<Courseinfo>();
                for(int i = 0; i < course_id.size(); i++) {
                    String c_id = course_id.get(i);
                    String c_name = course_name.get(i);
                    Courseinfo courseinfo = new Courseinfo(c_id, c_name);
                    courseinfo.parseSession();
                    courseinfoArrayList.add(courseinfo);
                }

                StringBuffer buffer = new StringBuffer();
                for(int i = 0; i < course_id.size(); i++) {
                    buffer.append("Id: " + course_id.get(i) + "\n");
                    buffer.append("Name: "+ course_name.get(i) + "\n");
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

    private class Courseinfo {
        public Courseinfo(String id, String name) {
            this.id = id;
            this.name = name;
            s_id = new ArrayList<String>();
            s_date = new ArrayList<String>();
            s_time = new ArrayList<String>();
            s_venue = new ArrayList<String>();
            s_remark = new ArrayList<String>();
        }
        public void parseSession() {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        URL url_course = new URL("https://msccs.cs.hku.hk/public/courses/2021/" + id);
                        HttpsURLConnection uc_course = (HttpsURLConnection) url_course.openConnection();
                        BufferedReader reader_course = new BufferedReader(new InputStreamReader(url_course.openStream(), "utf-16"));
                        String htmlPage_course = "";
                        String line_course = "";
                        while(line_course != null) {
                            line_course = reader_course.readLine();
                            htmlPage_course += line_course;
                            htmlPage_course += "\n";
                        }

                        Pattern p_sessions = Pattern.compile("<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<.*?>(.*?)</td>");
                        Matcher m_sessions = p_sessions.matcher(htmlPage_course);
                        while(m_sessions.find()) {
                            String sid = m_sessions.group(1);
                            String date = m_sessions.group(2);
                            String time = m_sessions.group(3);
                            String venue = m_sessions.group(4);
                            String remark = m_sessions.group(5);
                            s_id.add(sid);
                            s_date.add(date);
                            s_time.add(time);
                            s_venue.add(venue);
                            s_remark.add(remark);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        }
        public String id;
        public String name;
        public ArrayList<String> s_id;
        public ArrayList<String> s_date;
        public ArrayList<String> s_time;
        public ArrayList<String> s_venue;
        public ArrayList<String> s_remark;
    }
}