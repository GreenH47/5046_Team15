package com.example.vitality.utils;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

public class DateTimeUtils {


    public static void dateTimeDialog(Context context,OnDateTimeListener onDateTimeListener) {
        mOnDateTimeListener = onDateTimeListener;
        Calendar calendar = Calendar.getInstance();
        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                context, // 上下文
                new DatePickerDialog.OnDateSetListener() {
                    // 日期选择的回调方法
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 处理选择的日期
                        timeDialog(context, calendar, year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                },
                y, // 初始年份
                m, // 初始月份
                d // 初始日期
        );
        datePickerDialog.show();
    }

    private static void timeDialog(Context context, Calendar calendar, String dateStr) {
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                if(mOnDateTimeListener!=null){
                    mOnDateTimeListener.onDateTime(dateStr + " " + hourOfDay + ":" + minute);
                }
            }
        },
                hourOfDay,// 初始小时
                minute,//初始分钟
                true //24小时显示
        ).show();
    }

    private static OnDateTimeListener mOnDateTimeListener;


   public static interface OnDateTimeListener {
        void onDateTime(String dateTime);
    }
}
