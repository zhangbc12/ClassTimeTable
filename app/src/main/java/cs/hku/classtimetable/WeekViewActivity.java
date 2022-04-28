package cs.hku.classtimetable;

import static cs.hku.classtimetable.CalendarUtils.daysInMonthArray;
import static cs.hku.classtimetable.CalendarUtils.daysInWeekArray;
import static cs.hku.classtimetable.CalendarUtils.monthYearFromDate;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.util.ArrayList;

import soup.neumorphism.NeumorphTextView;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener{
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private NeumorphTextView emptyTV;
    private ListView eventListView;

    private  AlertDialog alert = null;
    private  AlertDialog.Builder builder = null;

    public void deleteEvent(String event) {
        alert = null;
        builder = new AlertDialog.Builder(WeekViewActivity.this);
        alert = builder.setTitle("Delete Event")
                .setMessage("Are you sure to delete this event?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(WeekViewActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.DB.deleteEvent(event);
                        finish();
                        startActivity(getIntent());
                    }
                }).create();
        alert.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);

        initWidgets();
        // Event.retrieveEvents();
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        CalendarAdapter calendarAdapter = new CalendarAdapter(days, this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);

        setEventAdapter();
    }



    private void initWidgets() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        emptyTV = findViewById(R.id.emptyTV);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void previousWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void nextWeekAction(View view) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    // click a date
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
        Event.retrieveEvents();
        setEventAdapter();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setEventAdapter() {

        // get events list for selected date
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        // auto hide "empty" textview
        if (!dailyEvents.isEmpty())
            emptyTV.setVisibility(View.GONE);
        else
            emptyTV.setVisibility(View.VISIBLE);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final TextView event = view.findViewById(R.id.eventCellTV);
                String eventName = (String)event.getText();
                event.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        System.out.println(eventName);
                        deleteEvent(eventName);
                    }
                });
            }
        });
    }

    public void newEventAction(View view) {
        startActivity(new Intent(this, EventEditActivity.class));
    }
}