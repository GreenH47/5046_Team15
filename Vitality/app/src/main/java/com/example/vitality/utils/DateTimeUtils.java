package com.example.vitality.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {


    public static String timeFormat(long time) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(new Date(time));
    }

    public static long parseTime(String time) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            return sdf.parse(time).getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static void dateTimeDialog(Context context, OnDateTimeListener onDateTimeListener) {
        mOnDateTimeListener = onDateTimeListener;
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                timeDialog(context, calendar, year, month, dayOfMonth);
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, onDateSetListener, y, m, d);
        datePickerDialog.show();
    }
    //选择时间控件
    private static void timeDialog(Context context, Calendar calendar, int year, int month, int dayOfMonth) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if (mOnDateTimeListener != null) {
                    Calendar cale = Calendar.getInstance();
                    cale.set(Calendar.YEAR, year);
                    cale.set(Calendar.MONTH, month);
                    cale.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    cale.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    cale.set(Calendar.MINUTE, minute);
                    cale.set(Calendar.SECOND, 0);


                    mOnDateTimeListener.onDateTime(timeFormat(cale.getTimeInMillis()));
                }
            }
        },
                hourOfDay,
                minute,
                true //24小时
        ).show();
    }
    //选择时间控件用的接口
    private static OnDateTimeListener mOnDateTimeListener;

    //选择时间控件用的接口
    public static interface OnDateTimeListener {
        void onDateTime(String dateTime);
    }
}

