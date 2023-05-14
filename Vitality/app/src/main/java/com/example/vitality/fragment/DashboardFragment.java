package com.example.vitality.fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.vitality.databinding.ActivityDashboardBinding;
import com.example.vitality.databinding.ActivityDietLogBinding;
import com.example.vitality.entity.Diet;
import com.example.vitality.entity.Food;
import com.example.vitality.viewmodel.DietViewModel;
import com.example.vitality.viewmodel.FoodViewModel;
import com.example.vitality.viewmodel.SharedViewModel;

import java.util.List;

public class DashboardFragment extends Fragment{
    private ActivityDashboardBinding binding;
    private DietViewModel dietViewModel;

    private FoodViewModel foodViewModel;
    public DashboardFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the View for this fragment using the binding
        binding = ActivityDashboardBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedViewModel model = new
                ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Get the ViewModel instance using AndroidViewModelFactory with the Application context
        //dietViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DietViewModel.class);
        foodViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(FoodViewModel.class);

        //observe all food in the database using LiveData and update the UI when there is a change
        foodViewModel.getAllFoods().observe(getViewLifecycleOwner(), new Observer<List<Food>>() {
            @Override
            public void onChanged(List<Food> foods) {
                String allFood = "";
                // Loop through each food and add their details to the string
                for (Food temp : foods) {
                    String foodName = "Food name: " + temp.foodName;
                    String foodCalories = "Food calories: " + temp.calories;
                    String foodDate = "Food date: " + temp.date;
                    String combinedMessage = foodName + "\n" + foodCalories + "\n" + foodDate;
                    String foodDetails = (combinedMessage);
                    allFood = allFood + System.getProperty("line.separator") + foodDetails;
                }
                // Set the text of the text view to show all the food details
                binding.textViewRead.setText(allFood);
            }
        });

        //Observe all diet in the database using LiveData and update the UI when there is a change
//        dietViewModel.getAllDiets().observe(getActivity(), new Observer<List<Diet>>() {
//            @Override
//            public void onChanged(@Nullable final List<Diet> diet) {
//                // Create a string to hold all the diet details
//                String allDiet = "";
//                // Loop through each diet and add their details to the string
//                for (Diet temp : diet) {
//                    String daymessage = "Day number: " + temp.dayNum;
//                    String cakeMessage = "Cake today: " + temp.cakeNum;
//                    String beefMessage = "Beef today: " + temp.beefNum;
//                    String combinedMessage = daymessage + "\n" + cakeMessage + "\n" + beefMessage;
//                    String dietDetails = (combinedMessage);
//                    allDiet = allDiet + System.getProperty("line.separator") + dietDetails;
//                }
//                // Set the text of the text view to show all the diet details
//                binding.textViewRead.setText("All data: " + allDiet);
//            }
//        });


        model.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                binding.textMessage.setText(s);
            }
        });




        //return to main page
        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigateUp(); // Navigate up to the previous destination
            }
        });

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
