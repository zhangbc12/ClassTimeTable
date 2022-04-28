package cs.hku.classtimetable;

import android.database.Cursor;
import android.os.Build;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Year;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Event {
    // store all events
    public static ArrayList<Event> eventsList = new ArrayList<>();

    // whether retrieved from DB before
    private static boolean flag = false;

    public static Set<LocalDate> datesWithDDL = new HashSet<>();

    // return all events for a day
    public static ArrayList<Event> eventsForDate(LocalDate date) {
        ArrayList<Event> events = new ArrayList<>();

        for (Event event: eventsList) {
            if (event.getDate().equals(date))
                events.add(event);
        }

        return events;
    }

    // get events from DB
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void retrieveEvents() {
        Cursor res = MainActivity.DB.getEventData();
        eventsList.clear();
        datesWithDDL.clear();
        if(res.getCount()==0){
            return;
        }
        while(res.moveToNext()){
            String title = res.getString(0);
            // string to LocalDate
            String[] dateArray = res.getString(1).split(":");
            int daysOfYear = Integer.parseInt(dateArray[1]);
            Year y = Year.of(Integer.parseInt(dateArray[0]));
            LocalDate date = y.atDay(daysOfYear + 1);
            datesWithDDL.add(date);
            LocalTime time = LocalTime.now();
            eventsList.add(new Event(title, date));
        }

    }

    private String name;
    private LocalDate date;
    private LocalTime time;

    public Event(String name, LocalDate date, LocalTime time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Event(String name, LocalDate date) {
        this.name = name;
        this.date = date;
        this.time = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
