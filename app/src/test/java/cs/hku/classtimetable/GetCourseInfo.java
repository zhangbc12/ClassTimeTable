package cs.hku.classtimetable;

import org.junit.Test;
import org.w3c.dom.Document;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class GetCourseInfo {
    @Test
    public void getCourseInfo() throws IOException {

        try {
            URL url_all_course = new URL("https://msccs.cs.hku.hk/public/courses/2021/modules-msccs.htm");
            HttpsURLConnection uc = (HttpsURLConnection) url_all_course.openConnection();
            BufferedReader reader = new BufferedReader(new InputStreamReader(url_all_course.openStream(), "utf-16"));

            String line = "";
            String htmlPage= "";
            while(line != null) {
                line = reader.readLine();
                htmlPage += line;
                htmlPage += "\n";
            }
            Pattern p_courses = Pattern.compile("<td class=\"modulelistcontent\" style=\"text-align:center;\">(.*?)</td>\\n\\s*<td class=\"modulelistcontent\"><a href=\"(.*?)\">(.*?)</a></td>\\n\\s*<td class=\"modulelistcontent modulelistdaytime\" style=\"text-align:center;\">(.*?)</td>\\n\\s*<td class=\"modulelistcontent\">(.*?)</td>");
            Matcher m_courses = p_courses.matcher(htmlPage);
            ArrayList<String> c_id = new ArrayList<String>();
            ArrayList<String> c_url = new ArrayList<String>();
            ArrayList<String> c_name = new ArrayList<String>();
            ArrayList<String> c_sem = new ArrayList<String>();
            ArrayList<String> c_teacher = new ArrayList<String>();
            ArrayList<ArrayList<String>> c_session = new ArrayList<ArrayList<String>>();
            while(m_courses.find()) {
                String id = m_courses.group(1);
                String url = m_courses.group(2);
                String name = m_courses.group(3);
                String sem = m_courses.group(4);
                String teacher = m_courses.group(5);
                c_id.add(id);
                c_url.add(url);
                c_name.add(name);
                c_sem.add(sem);
                c_teacher.add(teacher);
                getCourseSession(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCourseSession(String url) {
        try {
            URL url_course = new URL("https://msccs.cs.hku.hk/public/courses/2021/" + url);
            HttpsURLConnection uc_course = (HttpsURLConnection) url_course.openConnection();
            BufferedReader reader_course = new BufferedReader(new InputStreamReader(url_course.openStream(), "utf-16"));

            String line_course = "";
            String htmlPage_course = "";
            while(line_course != null) {
                line_course = reader_course.readLine();
                htmlPage_course += line_course;
                htmlPage_course += "\n";
            }
            Pattern p_sessions = Pattern.compile("<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<.*?>(.*?)</td>");
            Matcher m_sessions = p_sessions.matcher(htmlPage_course);
            ArrayList<String> s_id = new ArrayList<String>();
            ArrayList<String> s_date = new ArrayList<String>();
            ArrayList<String> s_time = new ArrayList<String>();
            ArrayList<String> s_venue = new ArrayList<String>();
            ArrayList<String> s_remark = new ArrayList<String>();
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
}
