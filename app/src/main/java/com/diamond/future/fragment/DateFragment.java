package com.diamond.future.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.diamond.future.MainActivity;
import com.diamond.future.R;
import com.diamond.future.model.UploadingModel;
import com.diamond.future.utility.PreManager;
import com.ozcanalasalvar.library.view.datePicker.DatePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class DateFragment extends Fragment {
    View v;
    Calendar myCalendar;
    TextView title, tvSelectDate;
    LinearLayout llNext;
    PreManager preManager;
    ImageView ivBack;
    Calendar currentCalendar = Calendar.getInstance();
    public static UploadingModel uploadingModel = new UploadingModel();
  /*  DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.date_fragment, container, false);
        initView();
        return v;
    }

    private void initView() {
        preManager = new PreManager(getContext());
        llNext = v.findViewById(R.id.llNext);
        title = v.findViewById(R.id.title);
        ivBack = v.findViewById(R.id.ivBack);
        tvSelectDate = v.findViewById(R.id.tvSelectDate);
        myCalendar = Calendar.getInstance();
        title.setText("Select Date");
       /* new DatePickerDialog(getContext(), R.style.TimePickerTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();*/
        DatePicker datePicker = v.findViewById(R.id.datepicker);
        datePicker.setOffset(3);
        datePicker.setTextSize(19);
        datePicker.setMaxDate(16756569000000L);
        datePicker.setDarkModeEnabled(true);
        datePicker.setPickerMode(DatePicker.DAY_ON_FIRST);
        /* datePicker.setMaxDate(*//*long time*//*);
        datePicker.setDate(*//*long time*//*);
        datePicker.setMinDate(*//*long time*//*);*/

        datePicker.setDataSelectListener(new DatePicker.DataSelectListener() {

            @Override
            public void onDateSelected(long date, int day, int month, int year) {
                if (month <= 9) {
                    uploadingModel.setDate(day + "-0" + month + "-" + year);
                } else {
                    uploadingModel.setDate(day + "-" + month + "-" + year);
                }
            }
        });
        llNext.setOnClickListener(V -> {
            String x = datePicker.getDate() + "";
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            long milliSeconds = Long.parseLong(x);
            System.out.println(milliSeconds);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            if (calendar.before(currentCalendar)||calendar.equals(currentCalendar)) {
                Toast.makeText(getContext(), "Please choose a date in the future, as the current date is not forward-looking.", Toast.LENGTH_SHORT).show();
            } else {
                uploadingModel.setDate(formatter.format(calendar.getTime()) + "");
                ((MainActivity) getContext()).setFrag("timeFrag");
            }
           /* Log.e("selected date", datePicker.getDate() + "");
            String x = datePicker.getDate() + "";
            DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
            long milliSeconds = Long.parseLong(x);
            System.out.println(milliSeconds);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            System.out.println(formatter.format(calendar.getTime()));
            Log.e("selected datenew", formatter.format(calendar.getTime()) + "");
            uploadingModel.setDate(formatter.format(calendar.getTime()) + "");
            ((MainActivity) getContext()).setFrag("timeFrag");*/
        });
        ivBack.setOnClickListener(V -> {
            ((MainActivity) getContext()).setFrag("video");
        });
    }

    private void updateLabel() {
        String myFormat = "MM-dd-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        tvSelectDate.setText("Selected Date:" + sdf.format(myCalendar.getTime()));
        uploadingModel.setDate(sdf.format(myCalendar.getTime()));
        //Toast.makeText(getContext(), sdf.format(myCalendar.getTime()), Toast.LENGTH_LONG).show();
    }
}
