package com.diamond.future.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.diamond.future.MainActivity;
import com.diamond.future.R;
import com.ozcanalasalvar.library.view.timePicker.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class TimeFragment extends Fragment {
    View v;
    Calendar myCalendar;
    TextView title;
    //private TimePicker timePicker1;
    LinearLayout llNext;
    String format;
    ImageView ivBack;
    int hr;
    int min;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.time_fragment, container, false);
        initView();
        return v;
    }

    private void initView() {
        title=v.findViewById(R.id.title);

       // timePicker1 = v. findViewById(R.id.timePicker1);
        myCalendar = Calendar.getInstance();
        title.setText("Select Time");
        llNext=v.findViewById(R.id.llNext);
       /* int hour = timePicker1.getCurrentHour();
        int min = timePicker1.getCurrentMinute();*/

        TimePicker timePicker = v.findViewById(R.id.timepicker);
        timePicker.setOffset(2);
        timePicker.setTextSize(19);
       /* timePicker.setHour(*//*hour*//*);
        timePicker.setMinute(*//*minute*//*);*/
        timePicker.setTimeSelectListener(new TimePicker.TimeSelectListener() {
            @Override
            public void onTimeSelected(int hour, int minute) {
                hr=hour;
                min=minute;
                DateFragment.uploadingModel.setTime(""+new StringBuilder().append(hour).append(":").append(minute)
                        .append(" ").append(format));
                DateFragment.uploadingModel.setTimeToUpload(hour+":"+minute);

            }
        });
        llNext.setOnClickListener(V->{
            showTime(timePicker.getHour(),timePicker.getMinute());
            DateFragment.uploadingModel.setTime(""+new StringBuilder().append(timePicker.getHour()).append(":").append(timePicker.getMinute())
                    .append(" ").append(format));
            DateFragment.uploadingModel.setTimeToUpload(timePicker.getHour()+":"+timePicker.getMinute());

            ((MainActivity) getContext()).setFrag("receiveremail");
        });
        ivBack = v.findViewById(R.id.ivBack);
        ivBack.setOnClickListener(V->{
            ((MainActivity) getContext()).setFrag("datefrag");
        });
    }
    public void showTime(int hour, int min) {
        if (hour == 0) {
            hour += 12;
            format = "AM";
        } else if (hour == 12) {
            format = "PM";
        } else if (hour > 12) {
            hour -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
DateFragment.uploadingModel.setTime(""+new StringBuilder().append(hour).append(" : ").append(min)
        .append(" ").append(format));
        /*Toast.makeText(getContext(),new StringBuilder().append(hour).append(" : ").append(min)
                .append(" ").append(format),Toast.LENGTH_LONG).show();*/
    }


}
