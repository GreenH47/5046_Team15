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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.vitality.R;
import com.example.vitality.adapter.SimpleSpinnerAdapter;
import com.example.vitality.databinding.FragmentFoodBinding;
import com.example.vitality.entity.Diet;
import com.example.vitality.entity.Food;
import com.example.vitality.utils.DateTimeUtils;
import com.example.vitality.viewmodel.DietViewModel;
import com.example.vitality.viewmodel.FoodViewModel;
import com.example.vitality.viewmodel.SharedViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FoodFragment extends Fragment {

    // Define necessary global variables
    private TextView tvSelTime;
    private TextView tvGenerate;
    private EditText etIntake;
    private Map<String, Float> map;
    private float calorie = -1f;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the fragment_food layout and set it as the view for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_food, container, false);

        // Define a map to store food calories
        map = new HashMap<>();
        map.put("米饭", 1.1f);
        map.put("肉类", 5f);
        map.put("鱼肉", 0.6f);
        map.put("蔬菜", 0.6f);
        map.put("蛋糕", 2f);
        map.put("饮料", 1.5f);

        // Set up the spinner with a list of food items
        AppCompatSpinner spinner = view.findViewById(R.id.spinner);
        List<String> list = new ArrayList<>();
        list.add("米饭");
        list.add("肉类");
        list.add("鱼肉");
        list.add("蔬菜");
        list.add("蛋糕");
        list.add("饮料");
        SimpleSpinnerAdapter adapter = new SimpleSpinnerAdapter(requireContext(), list);
        spinner.setAdapter(adapter);

        // Set the calorie value based on the selected food item
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

        // Set up the UI elements for selecting the time and generating the calorie intake
        etIntake = view.findViewById(R.id.etIntake);
        tvGenerate = view.findViewById(R.id.tvGenerate);
        tvSelTime = view.findViewById(R.id.tvSelTime);
        tvSelTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the date and time picker dialog
                dateDialog();
            }
        });

        // Calculate the calorie intake and display it in the UI
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

        // Navigate back to the previous screen
        view.findViewById(R.id.returnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp();
            }
        });

        return view;
    }

    // Display the date and time picker dialog
    private void dateDialog() {
        DateTimeUtils.dateTimeDialog(requireContext(), new DateTimeUtils.OnDateTimeListener() {
            @Override
            public void onDateTime(String dateTime) {
                tvSelTime.setText(dateTime);
            }
        });
    }
}
