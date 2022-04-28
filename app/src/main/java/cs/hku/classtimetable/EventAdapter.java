package cs.hku.classtimetable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {
    private Context context;


    public EventAdapter(@NonNull Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.event_cell, parent, false);
        TextView eventCellTV = convertView.findViewById(R.id.eventCellTV);

        // String eventTitle = event.getName() + " " + CalendarUtils.formattedTime(event.getTime());
        String eventTitle = event.getName();
        eventCellTV.setText(eventTitle);
//        eventCellTV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                String eventName = (String) eventCellTV.getText();
//                deleteEvent(eventName);
//                System.out.println(eventName);
//
//            }
//        });

        return convertView;
    }

    private AlertDialog alert = null;
    private AlertDialog.Builder builder = null;

    public void deleteEvent(String event) {
        alert = null;
        builder = new AlertDialog.Builder(context);
        alert = builder.setTitle("Delete Event")
                .setMessage("Are you sure to delete this event?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        MainActivity.DB.deleteEvent(event);
                    }
                }).create();
        alert.show();
    }


}
