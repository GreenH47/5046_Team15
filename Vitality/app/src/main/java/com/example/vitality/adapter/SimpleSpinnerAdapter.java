package com.example.vitality.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class SimpleSpinnerAdapter extends ArrayAdapter {

    private Context mContext;
    private List<String> mStringArray;

    public SimpleSpinnerAdapter(Context context, List<String> stringArray) {
        super(context, android.R.layout.simple_spinner_item, stringArray);
        mContext = context;
        mStringArray = stringArray;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        //修改Spinner展开后的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray.get(position));


        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 修改Spinner选择后结果的字体颜色
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        //此处text1是Spinner默认的用来显示文字的TextView
        TextView tv = (TextView) convertView.findViewById(android.R.id.text1);
        tv.setText(mStringArray.get(position));
        return convertView;
    }

    @Override
    public int getCount() {
        //返回数据的统计数量，大于0项则减去1项，从而不显示最后一项
        int i = super.getCount();
        return i > 0 ? i - 1 : i;
    }

    public void setNewData(List<String> list) {
        mStringArray.clear();
        if (list != null) {
            mStringArray.addAll(list);
        }
        notifyDataSetChanged();
    }
}
