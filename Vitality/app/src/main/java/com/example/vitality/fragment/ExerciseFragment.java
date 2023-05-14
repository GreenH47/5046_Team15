package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.vitality.R;
import com.example.vitality.utils.DateTimeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExerciseFragment extends Fragment {


    private TextView tvSelTime;
    private TextView tvGenerate;
    private EditText etDuration;

    private Map<String, Integer> map;
    private int calorie = -1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_exercise, container, false);
        map = new HashMap<>();

        map.put("run", 11);
        map.put("Jump rope", 10);
        map.put("badminton", 7);
        map.put("cycling", 10);
        map.put("aerobics", 8);


        AppCompatSpinner spinner = view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();

        list.add("run");
        list.add("Jump rope");
        list.add("badminton");
        list.add("cycling");
        list.add("aerobics");


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(arrayAdapter);

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


        etDuration = view.findViewById(R.id.etDuration);
        tvGenerate = view.findViewById(R.id.tvGenerate);
        tvSelTime = view.findViewById(R.id.tvSelTime);
        tvSelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateDialog();
            }
        });


        view.findViewById(R.id.btnGenerate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String duration = etDuration.getText().toString().trim();
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