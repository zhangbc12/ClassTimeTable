package cs.hku.classtimetable;

import static cs.hku.classtimetable.MainActivity.DB;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Locale;

import soup.neumorphism.NeumorphButton;

public class EventEditActivity extends AppCompatActivity {
    private EditText eventNameET;



    private DatePickerDialog datePickerDialog;
    private NeumorphButton dateButton;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        initWidgets();

        initDatePicker();
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this,style, dateSetListener, year, month, day);
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "Jan";
        if (month == 2)
            return "Feb";
        if (month == 3)
            return "Mar";
        if (month == 4)
            return "Apr";
        if (month == 5)
            return "May";
        if (month == 6)
            return "Jun";
        if (month == 7)
            return "Jul";
        if (month == 8)
            return "Aug";
        if (month == 9)
            return "Sep";
        if (month == 10)
            return "Oct";
        if (month == 11)
            return "Nov";
        if (month == 12)
            return "Dec";
        return "Jan";
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initWidgets() {
        eventNameET = findViewById(R.id.eventNameET);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getDate());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String getDate() {
        LocalDate date = CalendarUtils.selectedDate;
        int y = date.getYear();
        int m = date.getMonthValue();
        int d = date.getDayOfMonth();
        return makeDateString(d, m, y);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void saveEventAction(View view) {
        String eventName = eventNameET.getText().toString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy", Locale.US);
        String eventDate = (String) dateButton.getText();
        //convert String to LocalDate
        LocalDate localDate = LocalDate.parse(eventDate, formatter);

        int dayOfYear = localDate.getDayOfYear()-1;
        int year = localDate.getYear();
        String date = year+":"+dayOfYear;

        Boolean checkInsertData = DB.insertEventData(eventName, date);
        if(!checkInsertData) {
            DB.updateEventData(eventName, date);
        }
        finish();
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}