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

public class GetCourseDetails {
    @Test
    public void getCourseDetails() throws IOException {
        try {
            String pathname = "D:\\HKU\\Sem_2\\App\\ClassTimeTable\\app\\src\\main\\assets\\FITE7410B.txt";
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
            Pattern p_sessions = Pattern.compile("<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<.*?>(.*?)</td>");
            Matcher m_sessions = p_sessions.matcher(htmlPage);
            ArrayList<String> s_id = new ArrayList<String>();
            ArrayList<String> s_date = new ArrayList<String>();
            ArrayList<String> s_time = new ArrayList<String>();
            ArrayList<String> s_venue = new ArrayList<String>();
            ArrayList<String> s_remark = new ArrayList<String>();
            while(m_sessions.find()) {
                String id = m_sessions.group(1);
                String date = m_sessions.group(2);
                String time = m_sessions.group(3);
                String venue = m_sessions.group(4);
                String remark = m_sessions.group(5);
                s_id.add(id);
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
