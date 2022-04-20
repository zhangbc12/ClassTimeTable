package cs.hku.classtimetable;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class NetWorkHelper {
    DBHelper DB;
    NetWorkHelper() {
        DB = MainActivity.DB;
    }
    private void parse_msc_HTML_to_course(String htmlPage) {
        try {
            Pattern p_courses = Pattern.compile("<td class=\"modulelistcontent\" style=\"text-align:center;\">(.*?)</td>\\n\\s*<td class=\"modulelistcontent\"><.*?>(.*?)</a></td>");
            Matcher m_courses = p_courses.matcher(htmlPage);
            ArrayList<String> c_id = new ArrayList<String>();
            ArrayList<String> c_name = new ArrayList<String>();
            while(m_courses.find()) {
                String id = m_courses.group(1);
                String name = m_courses.group(2);
                c_id.add(id);
                c_name.add(name);

                Boolean checkInsertData = DB.insertAllCourseData(id, name);
                if(!checkInsertData) {
                    DB.updateAllCourseData(id, name);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getSessionSource() {
        ArrayList<String> coursePageList = new ArrayList<String>();
        try {
            ArrayList<String> courseIdList = DB.getMyCourse().get(0);
            String prefix = "https://msccs.cs.hku.hk/public/courses/2021/";

            for(int i = 0; i < courseIdList.size(); i++) {
                String coursePage = getHTMLSourceFromUrl(prefix + courseIdList.get(i));
                coursePageList.add(coursePage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return coursePageList;
    }

        public static String getHTMLSourceFromUrl(String urlStr) {
            try {
                URL url = new URL(urlStr);
                HttpsURLConnection uc = (HttpsURLConnection) url.openConnection();
                BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), "utf-16"));

                String line = "";
                String htmlPage = "";
                while (line != null) {
                    line = reader.readLine();
                    htmlPage += line;
                    htmlPage += "\n";
                }
                return htmlPage;
            } catch (Exception e) {
                return "Fail to get html page";
            }
    }

    private void parse_HTML_Source_to_Session(ArrayList<String> courseContentList) {
        ArrayList<String> course_id_list = DB.getMyCourse().get(0);
        ArrayList<String> course_name_list = DB.getMyCourse().get(1);
        Pattern p_sessions = Pattern.compile("<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<td class=\"modulesummarytimetable\">(.*?)</td>\\n\\s*<.*?>(.*?)</td>");
        for(int i = 0; i < courseContentList.size(); i++) {
            Matcher m_sessions = p_sessions.matcher(courseContentList.get(i));
            while(m_sessions.find()) {
                String sid = m_sessions.group(1);
                String date = m_sessions.group(2);
                String time = m_sessions.group(3);
//                    String venue = m_sessions.group(4);
//                    String remark = m_sessions.group(5);
                if(date.charAt(1) == ' ') date = '0' + date;
                String newDate = date.substring(7, 11) + "-" + MainActivity.monthMap.get(date.substring(3, 6)) + "-" + date.substring(0, 2);

                String id_name_session = course_id_list.get(i) + " " + course_name_list.get(i) + " " + sid;
                Boolean checkInsertSession = DB.insertAllSessionData(id_name_session, newDate, time);
                if(!checkInsertSession) {
                    DB.updateAllSessionData(id_name_session, newDate, time);
                }
            }
        }
    }

    public void processCourseMsc(){
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            String msc_url = "https://msccs.cs.hku.hk/public/courses/2021/modules-msccs.htm";
            String mscPageContent;
            @Override
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                mscPageContent = getHTMLSourceFromUrl(msc_url);

                if( mscPageContent.equals("Fail to login") )
                    success = false;
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                if (success) {
                    // 解析msc课程页面，获取全部课程id
                    parse_msc_HTML_to_course(mscPageContent);
                }
            }
        }.execute("");
    }

    public void importCourseData() {
        AsyncTask<String, Void, String> task = new AsyncTask<String, Void, String>() {
            boolean success;
            ArrayList<String> courseContentList = new ArrayList<String>();
            @Override
            protected String doInBackground(String... arg0) {
                // TODO Auto-generated method stub
                success = true;
                courseContentList = getSessionSource();

                if( courseContentList.size() == 0 )
                    success = false;
                return null;
            }
            @Override
            protected void onPostExecute(String result) {
                if (success) {
                    // 解析html页面，获取用户所有课程的session
                    parse_HTML_Source_to_Session( courseContentList );
                }
            }
        }.execute("");
    }
}
