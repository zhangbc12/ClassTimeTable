package cs.hku.classtimetable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphFloatingActionButton;

public class CourseActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout left;
    private TextView date_tv, reset_tv, import_tv, logout_tv;
    private Button btn_user, btn_select;
    private Integer week_ptr;
    private NeumorphFloatingActionButton btn_prev_week, btn_next_week, btn_add;
    private NeumorphButton btn_current;

    DBHelper DB = MainActivity.DB;

    ArrayList<Integer> courseColorList;
    ArrayList<TextView> weekDayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course);

        initColors();

        initView();
        initDateCourse();
        initViewFunction();
    }

    private void initColors() {
        courseColorList = new ArrayList<Integer>();
        courseColorList.add(0xFFFF9D36); // DKGRAY
        courseColorList.add(0xFFC783FC); // GRAY
        courseColorList.add(0xFFFF7E84); // RED
        courseColorList.add(0xFF00AFD5); // BLUE
        courseColorList.add(0xFF36A0FF); // YELLOW
        courseColorList.add(0xFF57C393); // CYAN
        courseColorList.add(0xFFA6ACC2); // MAGENTA
    }

    private ConstraintLayout getParentLayout(Integer i) {
        if(i == 1) {
            return findViewById(R.id.day1_course);
        }
        if(i == 2) {
            return findViewById(R.id.day2_course);
        }
        if(i == 3) {
            return findViewById(R.id.day3_course);
        }
        if(i == 4) {
            return findViewById(R.id.day4_course);
        }
        if(i == 5) {
            return findViewById(R.id.day5_course);
        }
        if(i == 6) {
            return findViewById(R.id.day6_course);
        }
        if(i == 7) {
            return findViewById(R.id.day7_course);
        }
        return findViewById(R.id.day7_course);
    }

    private void initDateCourse() {
        week_ptr = 0;

        String[] weekDays = getWeek(week_ptr);
        initWeekDays(weekDays);
        initCourse(weekDays);
    }

    @NonNull
    private String[] getWeek(int n) {
        String[] date = new String[7];
        try {
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            calendar.add(Calendar.DATE, (calendar.getFirstDayOfWeek() - day + 7 * n));
            date[0] = sdf.format(calendar.getTime());
            for (int i = 1; i < 7; i++) {
                calendar.add(Calendar.DATE, 1);
                date[i] = sdf.format(calendar.getTime());
            }

        } catch (Exception e) {
            e.printStackTrace();

            throw new RuntimeException(e);
        }

        return date;
    }

    private void initWeekDays(String[] weekDays) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd");
        String today = sdfTime.format(calendar.getTime());

        date_tv = (TextView) findViewById(R.id.date);
        int month = Integer.parseInt(weekDays[0].substring(5, 7));
        int year = Integer.parseInt(weekDays[0].substring(0, 4));
        String monthString = new DateFormatSymbols().getMonths()[month-1];
        String year_date = monthString + " " + Integer.toString(year);
        date_tv.setText(year_date);
        for(int i = 0; i < 7; i++) {
            TextView temp = weekDayList.get(i);
            temp.setText(weekDays[i].substring(8));
            if(weekDays[i].equals(today)) {
                temp.setTextColor(0xFF0088FF);
            }
            else {
                temp.setTextColor(Color.BLACK);
            }
        }
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        left = (LinearLayout) findViewById(R.id.menu_user);
        btn_user = (Button)findViewById(R.id.menu_button);
        btn_select = (Button)findViewById(R.id.select_button);
        btn_prev_week = (NeumorphFloatingActionButton) findViewById(R.id.prev_week_btn);
        btn_next_week = (NeumorphFloatingActionButton) findViewById(R.id.next_week_btn);
        btn_add = (NeumorphFloatingActionButton) findViewById(R.id.add_button);
        btn_current = (NeumorphButton) findViewById(R.id.current_btn);
        reset_tv = (TextView) findViewById(R.id.reset_tv);
        import_tv = (TextView) findViewById(R.id.import_tv);
        logout_tv = (TextView) findViewById(R.id.logout_tv);
        weekDayList = new ArrayList<TextView>();
        weekDayList.add(findViewById(R.id.day1));
        weekDayList.add(findViewById(R.id.day2));
        weekDayList.add(findViewById(R.id.day3));
        weekDayList.add(findViewById(R.id.day4));
        weekDayList.add(findViewById(R.id.day5));
        weekDayList.add(findViewById(R.id.day6));
        weekDayList.add(findViewById(R.id.day7));
    }

    private void initViewFunction() {
        btn_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(left);
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(CourseActivity.this, btn_select);
                popup.getMenuInflater().inflate(R.menu.menu_select, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.classPage:
                                startActivity(new Intent(getBaseContext(), CourseActivity.class));
                                break;
                            case R.id.ddlPage:
                                startActivity(new Intent(getBaseContext(), EventActivity.class));
                                break;
                        }

                        return true;
                    }
                });
                popup.show();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            String courseInfo = "";
            String courseDate = "";
            String courseStart = "";
            String courseEnd = "";
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");

                builder.setCancelable(true);
                builder.setTitle("Add Course");

                LinearLayout layout = new LinearLayout(CourseActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText inputCourse = new EditText(CourseActivity.this);
                final EditText inputDate = new EditText(CourseActivity.this);
                final EditText inputStartTime = new EditText(CourseActivity.this);
                final EditText inputEndTime = new EditText(CourseActivity.this);

                inputCourse.setHint("Course Info");
                inputDate.setHint("Date");
                inputDate.setClickable(true);
                inputDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePicker(layout.getContext(), inputDate);
                    }
                });

                inputStartTime.setHint("Start Time");
                inputStartTime.setClickable(true);
                inputStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePicker(layout.getContext(), inputStartTime);
                    }
                });

                inputEndTime.setHint("End Time");
                inputEndTime.setClickable(true);
                inputEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePicker(layout.getContext(), inputEndTime);
                    }
                });

                layout.addView(inputCourse);
                layout.addView(inputDate);
                layout.addView(inputStartTime);
                layout.addView(inputEndTime);

                builder.setView(layout);

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        courseInfo = inputCourse.getText().toString();
                        courseDate = inputDate.getText().toString();
                        courseStart = inputStartTime.getText().toString();
                        courseEnd = inputEndTime.getText().toString();

                        if(courseInfo.equals("") || courseDate.equals("") || courseStart.equals("") || courseEnd.equals("") || courseStart.equals(courseEnd)) {
                            Toast.makeText(CourseActivity.this, "Add Fail!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Boolean checkInsertSession = DB.insertAllSessionData(courseInfo, courseDate, courseStart + " - " + courseEnd);
                        if(!checkInsertSession) {
                            DB.updateAllSessionData(courseInfo, courseDate, courseStart + " - " + courseEnd);
                        }
                        String[] weekDays = getWeek(week_ptr);
                        initWeekDays(weekDays);
                        initCourse(weekDays);
                        Toast.makeText(CourseActivity.this, "Course is added!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CourseActivity.this, "Add cancel!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });

        btn_prev_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                week_ptr -= 1;
                String[] weekDays = getWeek(week_ptr);
                removeOldCourses();
                initWeekDays(weekDays);
                initCourse(weekDays);
            }
        });

        btn_next_week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                week_ptr += 1;
                String[] weekDays = getWeek(week_ptr);
                removeOldCourses();
                initWeekDays(weekDays);
                initCourse(weekDays);
            }
        });

        btn_current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeOldCourses();
                initDateCourse();
            }
        });

        reset_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DB.clearSessionTable();
                String[] weekDays = getWeek(week_ptr);
                removeOldCourses();
                initWeekDays(weekDays);
                initCourse(weekDays);
            }
        });

        import_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.netWorkHelper.processCourseMsc();
                MainActivity.netWorkHelper.importCourseData();
                String[] weekDays = getWeek(week_ptr);
                removeOldCourses();
                initWeekDays(weekDays);
                initCourse(weekDays);
            }
        });

        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
    }

    public void initCourse(String[] weekDays) {

        for(int i = 0; i < 7; i++) {
            String day = weekDays[i];
            ArrayList<ArrayList<String>> courseInfoTime = DB.getCourseOfDay(day);
            ArrayList<String> courseInfo = courseInfoTime.get(0);
            ArrayList<String> courseTime = courseInfoTime.get(1);

            int n = courseInfo.size();

            for(int j = 0; j < n; j++) {
                generateButton(i, getParentLayout(i + 1), courseInfo.get(j), courseTime.get(j));
            }
        }
    }

    private void removeOldCourses() {
        for(int i = 1; i <= 7; i++) {
            getParentLayout(i).removeAllViews();
        }
    }

    private void generateButton(int idx, ConstraintLayout layout, String info, String time) {
        Button course_btn = new Button(this);

        int minsOfADay = 840;
        String timeNode1 = time.substring(0, time.indexOf(' '));
        String timeNode2 = time.substring(time.indexOf('-') + 2);
        int timeStamp1 = getTimeStamp(timeNode1);
        int timeStamp2 = getTimeStamp(timeNode2);

        int layoutHeight = layout.getHeight();
        int courseHeight = (timeStamp2 - timeStamp1) * layoutHeight / minsOfADay;
        int courseTranslationY = timeStamp1 * layoutHeight / minsOfADay;

        course_btn.setText(info);
        course_btn.setTextSize(9);
        course_btn.setTextColor(courseColorList.get(idx));
        course_btn.setHeight(courseHeight);
        course_btn.setTranslationY(courseTranslationY);
        course_btn.setBackgroundColor(courseColorList.get(idx));
        course_btn.getBackground().setAlpha(50);

        course_btn.setOnClickListener(new View.OnClickListener() {
            String courseInfo = "";
            String courseDate = "";
            String courseStart = "";
            String courseEnd = "";
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CourseActivity.this);
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                String timeNow = sdfTime.format(calendar.getTime());
                String dateNow = sdfDate.format(calendar.getTime());
                builder.setCancelable(true);
                builder.setTitle("Modify");
                LinearLayout layout = new LinearLayout(CourseActivity.this);
                layout.setOrientation(LinearLayout.VERTICAL);

                final EditText inputCourse = new EditText(CourseActivity.this);
                final EditText inputDate = new EditText(CourseActivity.this);
                final EditText inputStartTime = new EditText(CourseActivity.this);
                final EditText inputEndTime = new EditText(CourseActivity.this);

                inputCourse.setHint("Course Info");
                inputCourse.setText(info);

                inputDate.setHint("Date");
                inputDate.setClickable(true);
                inputDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showDatePicker(layout.getContext(), inputDate);
                    }
                });

                inputStartTime.setHint("Start Time");
                inputStartTime.setClickable(true);
                inputStartTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePicker(layout.getContext(), inputStartTime);
                    }
                });

                inputEndTime.setHint("End Time");
                inputEndTime.setClickable(true);
                inputEndTime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        showTimePicker(layout.getContext(), inputEndTime);
                    }
                });

                layout.addView(inputCourse);
                layout.addView(inputDate);
                layout.addView(inputStartTime);
                layout.addView(inputEndTime);

                builder.setView(layout);

                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DB.deleteSession(info);
                        String[] weekDays = getWeek(week_ptr);
                        removeOldCourses();
                        initWeekDays(weekDays);
                        initCourse(weekDays);
                        Toast.makeText(CourseActivity.this, "Delete Success!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        courseInfo = inputCourse.getText().toString();
                        courseDate = inputDate.getText().toString();
                        courseStart = inputStartTime.getText().toString();
                        courseEnd = inputEndTime.getText().toString();

                        if(courseInfo.equals("") || courseDate.equals("") || courseStart.equals("") || courseEnd.equals("")) {
                            Toast.makeText(CourseActivity.this, "Modify Fail!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if(!info.equals(courseInfo)) {
                            DB.deleteSession(info);
                        }
                        Boolean checkInsertSession = DB.insertAllSessionData(courseInfo, courseDate, courseStart + " - " + courseEnd);
                        if(!checkInsertSession) {
                            DB.updateAllSessionData(courseInfo, courseDate, courseStart + " - " + courseEnd);
                        }
                        String[] weekDays = getWeek(week_ptr);
                        removeOldCourses();
                        initWeekDays(weekDays);
                        initCourse(weekDays);
                        Toast.makeText(CourseActivity.this, "Course is modified!", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(CourseActivity.this, "Modify cancel!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.show();
            }
        });

        layout.addView(course_btn);
    }

    public int getTimeStamp(String time) {
        int hour = Integer.parseInt(time.substring(0, time.indexOf(':')));
        time = time.substring(time.indexOf(':') + 1);
        int minute = Integer.parseInt(time.substring(0, 2));
        if(time.length() > 2 && hour != 12 && time.substring(2).equals("pm")) hour += 12;
        int timeStamp = (hour - 9) * 60 + minute;
        return timeStamp;
    }

    public static void showDatePicker(Context context, EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        View datePickerView = LayoutInflater.from(context).inflate(R.layout.date_picker_dialog, null);
        DatePicker datePicker = datePickerView.findViewById(R.id.date_picker);
        datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                editText.setText(format.format(calendar.getTime()));
            }
        });
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setView(datePickerView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public static void showTimePicker(Context context, EditText editText) {
        View datePickerView = LayoutInflater.from(context).inflate(R.layout.time_picker_dialog, null);
        TimePicker timePicker = datePickerView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hourStr = Integer.toString(hourOfDay);
                if(hourOfDay < 10) hourStr = "0" + hourStr;
                String minuteStr = Integer.toString(minute);
                if(minute < 10) minuteStr = "0" + minuteStr;
                editText.setText(hourStr + ":" + minuteStr);
            }
        });
        final AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setView(datePickerView);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}