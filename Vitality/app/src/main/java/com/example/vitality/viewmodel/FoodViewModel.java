package com.example.vitality.viewmodel;

import android.app.Application;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProvider;

import com.example.vitality.dao.FoodDAO;
import com.example.vitality.database.FoodDatabase;
import com.example.vitality.entity.Diet;
import com.example.vitality.entity.Food;
import com.example.vitality.repository.DietRepository;
import com.example.vitality.repository.FoodRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

public class FoodViewModel extends AndroidViewModel{
    // Repository instance to access the database operations
    private FoodRepository fRepository;
    //LiveData to hold a list of all foods in the database
    private LiveData<List<Food>> allFoods;
    private FoodDAO foodDao;


    //constructor to initialize the repository and fetch all foods from the database
    public FoodViewModel (Application application) {
        super(application);
        // Get the repository instance
        fRepository = new FoodRepository(application);
        // Set the LiveData to receive all the foods from the database
        allFoods = fRepository.getAllFoods();
    }

    // Getter method to get a specific food by their ID asynchronously
    @RequiresApi(api = Build.VERSION_CODES.N)
    public CompletableFuture<Food> findByIDFuture(final int foodId) {
        // Return a completable future to get food by ID from the database asynchronously
        return fRepository.findByIDFuture(foodId);
    }

    // Getter method to get all foods from the database using LiveData
    public LiveData<List<Food>> getAllFoods() {
        // Return the LiveData containing all the foods in the database
        return allFoods;
    }

    //get food data range
    public LiveData<String> getDataRange() {
        MutableLiveData<String> minDate = new MutableLiveData<>();

        // Execute getMinDate query on a background thread using Room's query method
        Executors.newSingleThreadExecutor().execute(() -> {
            // Get the database instance using the application context
            FoodDatabase foodDatabase = FoodDatabase.getInstance(getApplication().getApplicationContext());
            // Get the FoodDao instance from the database
            FoodDAO foodDao = foodDatabase.foodDao();
            // Execute the query using FoodDao query method and get the result string
            String minDateString = foodDao.getDateRange();
            // Post the result string to the minDate LiveData object
            minDate.postValue(minDateString);

        });



        return minDate;
    }


    // get food data between two dates
//    public LiveData<List<Food>> getFoodBetweenDates(String startDate, String endDate) {
//        LiveData<List<Food>> allFoods = fRepository.getAllFoods();
//        LiveData<List<Food>> filteredFoods = Transformations.switchMap(allFoods, foods -> {
//            MutableLiveData<List<Food>> filteredList = new MutableLiveData<>();
//            List<Food> list = new ArrayList<>();
//            for (Food food : foods) {
//                String foodDate = food.getDate();
//                if (foodDate.compareTo(startDate) >= 0 && foodDate.compareTo(endDate) <= 0) {
//                    list.add(food);
//                }
//            }
//            filteredList.setValue(list);
//            return filteredList;
//        });
//        return filteredFoods;
//    }
    public LiveData<List<Food>> getFoodBetweenDates(String startDate, String endDate) {
        return fRepository.getFoodBetweenDates(startDate, endDate);
    }






    // Method to insert a food to the database
    public void insert(Food food) {
        // Call the insert method of the repository to insert food to the database
        fRepository.insert(food);
    }

    // Method to delete all foods from the database
    public void deleteAll() {
        // Call the deleteAll method of the repository to delete all foods from the database
        fRepository.deleteAll();
    }

//    public void setMessage(String message) {
//        mText.setValue(message);
//    }
}
