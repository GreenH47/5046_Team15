package com.example.vitality.fragment;

import android.os.Bundle;
import android.text.TextUtils;
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
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.vitality.R;
import com.example.vitality.adapter.ExerciseAdapter;
import com.example.vitality.dao.ExerciseDao;
import com.example.vitality.database.DietDatabase;
import com.example.vitality.entity.ExerciseEntity;
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
    private int uid = -1;

    private ExerciseDao exerciseDao;
    private AppCompatSpinner spinner;

    private ExerciseAdapter exerciseAdapter;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_exercise, container, false);
        exerciseDao = DietDatabase.getInstance(requireContext()).exerciseDao();
        map = new HashMap<>();

        map.put("Running", 11);
        map.put("Ropeskipping", 10);
        map.put("Badminton", 7);
        map.put("Riding", 10);
        map.put("Bodybuilding", 8);


        spinner = view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        //add sport type
        list.add("Running");
        list.add("Ropeskipping");
        list.add("Badminton");
        list.add("Riding");
        list.add("Bodybuilding");


        //spinner adapter
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, list);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            //Click and select event
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
                //Date and time selection popup
                dateDialog();
            }
        });

//Generate calories and judge
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

//Return to previous page
        view.findViewById(R.id.btnReturn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Return to upper level
                Navigation.findNavController(view).navigateUp();
            }
        });

//Clear data
        view.findViewById(R.id.btnDel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uid = 0;
                etDuration.setText("");
                tvSelTime.setText("");
                tvGenerate.setText("");
            }
        });
        view.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add data
                uid = 0;

                DietDatabase.databaseWriteExecutor.execute(new Runnable() {
                    @Override
                    public void run() {
                        ExerciseEntity exerciseEntity = getExerciseEntity();
                        if (exerciseEntity != null) {
                            exerciseDao.insert(exerciseEntity);
                        }
                    }
                });


            }
        });

//Update the data and validate
        view.findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExerciseEntity exerciseEntity = getExerciseEntity();
                if (uid <= 0) {
                    Toast.makeText(getContext(), "Select Edit data first", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (exerciseEntity != null) {
                    exerciseEntity.uid = uid;
                    DietDatabase.databaseWriteExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            exerciseDao.update(exerciseEntity);
                        }
                    });

                }
            }
        });


        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        exerciseAdapter = new ExerciseAdapter();
        recyclerView.setAdapter(exerciseAdapter);
//List control click event
        exerciseAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.btnEdit) {
                    //edit button
                    ExerciseEntity exerciseEntity = exerciseAdapter.getItem(position);
                    uid = exerciseEntity.uid;

                    tvSelTime.setText(DateTimeUtils.timeFormat(exerciseEntity.selTime));
                    etDuration.setText("" + exerciseEntity.duration);
                }
            }
        });


        exerciseDao.getAll().observe(getViewLifecycleOwner(), new Observer<List<ExerciseEntity>>() {
            @Override
            public void onChanged(List<ExerciseEntity> exerciseEntities) {
                exerciseAdapter.setNewInstance(exerciseEntities);
            }
        });
        return view;
    }

    //Call time popover
    private void dateDialog() {
        DateTimeUtils.dateTimeDialog(requireContext(), new DateTimeUtils.OnDateTimeListener() {
            @Override
            public void onDateTime(String dateTime) {
                tvSelTime.setText(dateTime);
            }
        });
    }

    //Get the data to add and construct the entity class
    private ExerciseEntity getExerciseEntity() {
        String duration = etDuration.getText().toString().trim();
        String generate = tvGenerate.getText().toString().trim();
        String selTime = tvSelTime.getText().toString();
        float calc = -1f;
        int d = -1;
        try {
            d = Integer.parseInt(duration);
            calc = Float.parseFloat(generate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (d < 0 || calorie < 0 || calc < 0 || TextUtils.isEmpty(selTime)) {
            requireActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getContext(), "data error", Toast.LENGTH_SHORT).show();
                }
            });
            return null;
        }
        ExerciseEntity exerciseEntity = new ExerciseEntity();
        exerciseEntity.calc = "" + calc;
        exerciseEntity.duration = d;
        exerciseEntity.type = spinner.getSelectedItem().toString();
        exerciseEntity.createTime = System.currentTimeMillis();
        exerciseEntity.selTime = DateTimeUtils.parseTime(selTime);
        return exerciseEntity;
    }
}