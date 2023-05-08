package com.example.vitality.viewmodel;


import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.vitality.entity.Diet;
import com.example.vitality.repository.DietRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DietViewModel extends AndroidViewModel{

    // Repository instance to access the database operations
    private DietRepository dRepository;

    //LiveData to hold a list of all diets in the database
    private LiveData<List<Diet>> allDiets;

    //constructor to initialize the repository and fetch all diets from the database
    public DietViewModel (Application application) {
        super(application);
        // Get the repository instance
        dRepository = new DietRepository(application);
        // Set the LiveData to receive all the diets from the database
        allDiets = dRepository.getAllDiets();
    }

    // Getter method to get a specific customer by their ID asynchronously
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Diet> findByIDFuture(final int dietId) {
        // Return a completable future to get customer by ID from the database asynchronously
        return dRepository.findByIDFuture(dietId);
    }

    // Getter method to get all diets from the database using LiveData
    public LiveData<List<Diet>> getAllDiets() {
        // Return the LiveData containing all the diets in the database
        return allDiets;
    }

    // Method to insert a diet to the database
    public void insert(Diet diet) {
        // Call the insert method of the repository to insert diet to the database
        dRepository.insert(diet);
    }

    // Method to delete all diets from the database
    public void deleteAll() {
        // Call the deleteAll method of the repository to delete all diets from the database
        dRepository.deleteAll();
    }

    // Method to update a diet in the database
    public void update(Diet diet) {
        // Call the update method of the repository to update a diet in the database
        dRepository.updateDiet(diet);
    }

}
