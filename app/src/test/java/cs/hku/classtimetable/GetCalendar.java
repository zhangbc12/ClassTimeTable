package cs.hku.classtimetable;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GetCalendar {
    @Test
    public void getCalendar() throws IOException {
        try {
            String pathname = "D:\\HKU\\Sem_2\\App\\ClassTimeTable\\app\\src\\main\\assets\\courses.txt";
            File filename = new File(pathname);
            InputStreamReader reader  = new InputStreamReader (new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);
            String line = "";
            String htmlPage= "";
            while(line != null) {
                line = br.readLine();
                htmlPage += line;
                htmlPage += "\n";
            }
            Pattern p_date = Pattern.compile("<a id=\"calendar-day-popover-link-1-(\\d+)-(\\d+)-.*?\\n<div class=\"hidden\">\\n\\n(\\s*<div data-popover-eventtype-course=\"1\">\\n\\n\\n.*?\\n\\s*(.*?): (.*?)\\n\\s*</div>\\n)*");
            Matcher m_date = p_date.matcher(htmlPage);
            ArrayList<String> c_year = new ArrayList<String>();
            ArrayList<String> c_yday = new ArrayList<String>();
            ArrayList<ArrayList<String>> c_course = new ArrayList<ArrayList<String>>();
            ArrayList<ArrayList<String>> c_event = new ArrayList<ArrayList<String>>();
            while(m_date.find()) {
                String year = m_date.group(1);
                String yday = m_date.group(2);
                c_year.add(year);
                c_yday.add(yday);

                Pattern p_events = Pattern.compile("\\s*<div data-popover-eventtype-course=\"1\">\\n\\n\\n.*?\\n\\s*(.*?): (.*?)\\n\\s*</div>\\n");
                Matcher m_events = p_events.matcher(m_date.group(0));
                ArrayList<String> courses = new ArrayList<String>();
                ArrayList<String> events = new ArrayList<String>();
                while(m_events.find()) {
                    String course_name = m_events.group(1);
                    String event_describe = m_events.group(2);
                    courses.add(course_name);
                    events.add(event_describe);
                }
                c_course.add(courses);
                c_event.add(events);
            }
            String as = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
