package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import com.example.vitality.R;
import com.example.vitality.adapter.SimpleSpinnerAdapter;
import com.example.vitality.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodFragment extends Fragment {

    private TextView tvSelTime;
    private TextView tvGenerate;
    private EditText etIntake;
    private Map<String, Float> map;
    private float calorie = -1f;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_food, container, false);
//        食物类型：
//        米饭（1.1卡路里每克），
//        肉类（5卡路里每克），
//        鱼肉（0.6卡路里每克），
//        蔬菜（0.6卡路里每克），
//        蛋糕（2卡路里每克），
//        饮料（1.5卡路里每克）
        map = new HashMap<>();
        map.put("米饭", 1.1f);
        map.put("肉类", 5f);
        map.put("鱼肉", 0.6f);
        map.put("蔬菜", 0.6f);
        map.put("蛋糕", 2f);
        map.put("饮料", 1.5f);


        AppCompatSpinner spinner = view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        //食物添加
        list.add("米饭");
        list.add("肉类");
        list.add("鱼肉");
        list.add("蔬菜");
        list.add("蛋糕");
        list.add("饮料");


        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(requireContext(), list);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = list.get(position);
                calorie = map.get(item);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etIntake = view.findViewById(R.id.etIntake);
        tvGenerate = view.findViewById(R.id.tvGenerate);
        tvSelTime = view.findViewById(R.id.tvSelTime);
        tvSelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //日期和时间选择弹窗
                dateDialog();
            }
        });


        view.findViewById(R.id.btnGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String duration = etIntake.getText().toString().trim();
                int d = -1;
                try {
                    d = Integer.parseInt(duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (d < 0 || calorie < 0) {
                    Toast.makeText(getContext(), "data error", Toast.LENGTH_SHORT).show();
                    return;
                }

                tvGenerate.setText("" + d * calorie);
            }
        });


        view.findViewById(R.id.btnReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回上一级
//                Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main).navigateUp();
                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }

    private void dateDialog() {
        DateTimeUtils.dateTimeDialog(requireContext(), new DateTimeUtils.OnDateTimeListener() {
            @Override
            public void onDateTime(String dateTime) {
                tvSelTime.setText(dateTime);
            }
        });
    }
}