package com.example.vitality.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

    private FragmentFoodBinding binding;
    private FoodViewModel foodViewModel;
    public FoodFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the fragment_food layout and set it as the view for this fragment
        //View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_food, container, false);
        binding = FragmentFoodBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedViewModel sharemodel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        // get the ViewModel instance using AndroidViewModelFactory with the Application context
        foodViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(FoodViewModel.class);

        // observe all food in the database using LiveData and update the UI when there is a change
        foodViewModel.getAllFoods().observe(getActivity(), new Observer<List<Food>>() {
            @Override
            public void onChanged(@NonNull final List<Food> food) {
                // Create a string to hold all the food details
                String allFood = "";
                // Loop through each food and add their details to the string
                for (Food temp : food) {
                    String daymessage = "Day number: " + temp.date;
                    String foodMessage = "Food today: " + temp.foodName;
                    String foodIntake = "Food intake: " + temp.foodIntake;
                    String foodCalories = "Food calories: " + temp.calories;
                    String combinedMessage = daymessage + "\n" + foodMessage + "\n" + foodIntake + "\n" + foodCalories;
                    //String combinedMessage = daymessage + "\n" + foodMessage;
                    String foodDetails = (combinedMessage);
                    allFood = allFood + System.getProperty("line.separator") + foodDetails;
                }
                // Set the text of the text view to show all the food details
                binding.textViewRead.setText("All Food data: " + allFood);
            }
        });

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
//        view.findViewById(R.id.btnGenerate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String duration = etIntake.getText().toString().trim();
//                int d = -1;
//                try {
//                    d = Integer.parseInt(duration);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (d < 0 || calorie < 0) {
//                    Toast.makeText(getContext(), "data error", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                tvGenerate.setText("" + d * calorie);
//            }
//        });

        etIntake.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }

            @Override
            public void afterTextChanged(Editable s) {
                String duration = s.toString().trim();
                int d = -1;
                try {
                    d = Integer.parseInt(duration);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (d >= 0 && calorie >= 0) {
                    float totalCalories = d * calorie;
                    tvGenerate.setText(String.valueOf(totalCalories));
                }
            }
        });


        // Save the food details to the database
        view.findViewById(R.id.addButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user input from text fields
                String foodName = binding.spinner.getSelectedItem().toString();
                float foodIntake = Float.parseFloat(etIntake.getText().toString().trim());
                float calories = Float.parseFloat(tvGenerate.getText().toString().trim());
                String date = tvSelTime.getText().toString().trim();

                // check if any of the fields are empty
                if (TextUtils.isEmpty(foodName) || TextUtils.isEmpty(etIntake.getText().toString().trim()) || TextUtils.isEmpty(tvGenerate.getText().toString().trim()) || TextUtils.isEmpty(date)) {
                    Toast.makeText(getContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // create a new food object
                Food food = new Food(foodName, foodIntake, calories, date);
                // add the food to the database
                foodViewModel.insert(food);
                String message = "Food added: days: " + date + " food: " + foodName + " intake: " + foodIntake + "g calories: " + calories + "kcal";
                // display a toast message to show that the food has been added to the database
                // Toast.makeText(getContext(), "Food added", Toast.LENGTH_SHORT).show();
                binding.textViewRead.setText(message);

                sharemodel.setMessage(message);
            }
        });

        // delete all food from the database
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodViewModel.deleteAll();
                binding.textViewDelete.setText("All food deleted");
                //Toast.makeText(getContext(), "All food deleted", Toast.LENGTH_SHORT).show();
            }
        });

        //clear the text view
        binding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.spinner.setSelection(0);
                etIntake.setText("");
                tvGenerate.setText("");
                tvSelTime.setText("");
//                binding.textViewRead.setText("");
//                binding.textViewDelete.setText("");
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
