package cs.hku.classtimetable;

import static cs.hku.classtimetable.CalendarUtils.daysInMonthArray;
import static cs.hku.classtimetable.CalendarUtils.monthYearFromDate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;

    private Button btn_menu, btn_select;
    private DrawerLayout drawerLayout;
    private LinearLayout left;
    private TextView reset_tv, import_tv, logout_tv;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        initWidgets();
        initWidgetsFunction();
        CalendarUtils.selectedDate = LocalDate.now();
        CalendarUtils.currentDate = LocalDate.now();
        Event.retrieveEvents();
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        Event.retrieveEvents();
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setMonthView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> daysInMonth = daysInMonthArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(daysInMonth, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
    }

    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        btn_menu = (Button)findViewById(R.id.menu_button);
        btn_select = (Button)findViewById(R.id.select_button);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        left = (LinearLayout) findViewById(R.id.menu_user);
        reset_tv = (TextView) findViewById(R.id.reset_tv);
        import_tv = (TextView) findViewById(R.id.import_tv);
        logout_tv = (TextView) findViewById(R.id.logout_tv);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1);
        setMonthView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextMonthAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1);
        setMonthView();
    }

    // click a date
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        // in case have empty days in month view
        if (date != null) {
            CalendarUtils.selectedDate = date;
            setMonthView();
            startActivity(new Intent(this, WeekViewActivity.class));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void currentAction(View view) {
            if (CalendarUtils.selectedDate.equals(LocalDate.now()) )
                Toast.makeText(EventActivity.this, "Selected", Toast.LENGTH_SHORT).show();
            else
            {
                CalendarUtils.selectedDate = LocalDate.now();
                setMonthView();
            }

    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void initWidgetsFunction() {
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(left);
            }
        });

        btn_select.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(EventActivity.this, btn_select);
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

        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), MainActivity.class));
            }
        });
    }
}