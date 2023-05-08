package com.example.vitality.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.vitality.MainActivity;
import com.example.vitality.databinding.ActivityDietLogBinding;
import com.example.vitality.viewmodel.SharedViewModel;



public class DietlogFragment extends Fragment {
    private ActivityDietLogBinding addBinding;
    public DietlogFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        addBinding = ActivityDietLogBinding.inflate(inflater, container, false);
        View view = addBinding.getRoot();
        SharedViewModel model = new
                ViewModelProvider(getActivity()).get(SharedViewModel.class);

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
                int day=0, cake = 0, beef = 0;
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
                int cakeEnergy = cake * 2000;
                int beefEnergy = beef * 2000;
                String daymessage = "Day number: " + day;
                String cakeMessage = "Cake energy today: " + cakeEnergy + " KJ";
                String beefMessage = "Beef energy today: " + beefEnergy + " KJ";
                String combinedMessage = daymessage + "\n" + cakeMessage + "\n" + beefMessage;
                model.setMessage(combinedMessage);
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