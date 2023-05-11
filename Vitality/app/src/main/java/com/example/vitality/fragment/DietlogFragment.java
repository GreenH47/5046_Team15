package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.vitality.databinding.ActivityDietLogBinding;
import com.example.vitality.entity.Diet;
import com.example.vitality.viewmodel.DietViewModel;
import com.example.vitality.viewmodel.SharedViewModel;

import java.util.List;


public class DietlogFragment extends Fragment {
    private ActivityDietLogBinding addBinding;

    // ViewModel instance to access the database operations
    private DietViewModel dietViewModel;

    public DietlogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addBinding = ActivityDietLogBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();

        SharedViewModel sharemodel = new ViewModelProvider(getActivity()).get(SharedViewModel.class);

        // Get the ViewModel instance using AndroidViewModelFactory with the Application context
        dietViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(DietViewModel.class);

        //Observe all diet in the database using LiveData and update the UI when there is a change
        dietViewModel.getAllDiets().observe(getActivity(), new Observer<List<Diet>>() {
            @Override
            public void onChanged(@Nullable final List<Diet> diet) {
                // Create a string to hold all the diet details
                String allDiet = "";
                // Loop through each diet and add their details to the string
                for (Diet temp : diet) {
                    String daymessage = "Day number: " + temp.dayNum;
                    String cakeMessage = "Cake today: " + temp.cakeNum;
                    String beefMessage = "Beef today: " + temp.beefNum;
                    String combinedMessage = daymessage + "\n" + cakeMessage + "\n" + beefMessage;
                    String dietDetails = (combinedMessage);
                    allDiet = allDiet + System.getProperty("line.separator") + dietDetails;
                }
                // Set the text of the text view to show all the diet details
                addBinding.textViewRead.setText("All data: " + allDiet);
            }
        });

        //add message to the list
        addBinding.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String message = addBinding.editText.getText().toString();
//                if (!message.isEmpty() ) {
//                    model.setMessage(message);
//                }
                String dayText = addBinding.editText1.getText().toString();
                String cakeText = addBinding.editText2.getText().toString();
                String beefText = addBinding.editText3.getText().toString();
                int day = 0, cake = 0, beef = 0;
                try {
                    if (!dayText.equals("")) {
                        day = Integer.parseInt(dayText);
                    }

                    if (!cakeText.equals("")) {
                        cake = Integer.parseInt(cakeText);
                    }
                    if (!beefText.equals("")) {
                        beef = Integer.parseInt(beefText);
                    }


                } catch (NumberFormatException e) {
                    // Display error message if input is not an integer
                    Toast.makeText(getContext(), "Input must be an integer", Toast.LENGTH_SHORT).show();
                }

                // check if same day number already exists and throw error message exception
                List<Diet> dietList = dietViewModel.getAllDiets().getValue();
                for (Diet temp : dietList) {
                    if (temp.dayNum == day) {
                        Toast.makeText(getContext(), "Day number already exists", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }


                // Create a new diet object with the details
                Diet diet = new Diet(day, cake, beef);
                // Insert the diet into the database
                dietViewModel.insert(diet);
                // Set the text of the text view to show the added diet details
                addBinding.textViewRead.setText("Added: " + "day: " + day + " cake: " + cake + " beef: " + beef);

                int cakeEnergy = cake * 2000;
                int beefEnergy = beef * 2000;
                String daymessage = "Day number: " + day;
                String cakeMessage = "Cake energy today: " + cakeEnergy + " KJ";
                String beefMessage = "Beef energy today: " + beefEnergy + " KJ";
                String combinedMessage = daymessage + "\n" + cakeMessage + "\n" + beefMessage;
                sharemodel.setMessage(combinedMessage);
            }
        });

        //Delete button click listener to delete all the diet in the database
        addBinding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete all the diet in the database
                dietViewModel.deleteAll();
                // Set the text of the text view to show that the database is empty
                addBinding.textViewRead.setText("All data deleted");
            }
        });

        //clear the message
        addBinding.clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBinding.editText1.setText("");
                addBinding.editText2.setText("");
                addBinding.editText3.setText("");
            }
        });

        //return to main page

        addBinding.returnButton.setOnClickListener(new View.OnClickListener() {
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
        addBinding = null;
    }
}
