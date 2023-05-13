package com.example.vitality;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.View;

import com.example.vitality.databinding.ActivityDrawerBinding;
import com.example.vitality.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityDrawerBinding drawerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Set the toolbar as the action bar
        //setSupportActionBar(binding.appBar.toolbar);

        // Set up the navigation drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home_fragment,
                R.id.nav_exercise_fragment,
                R.id.nav_food_fragment,
                R.id.nav_dashboard_fragment
                )
                // To display the Navigation button as a drawer symbol,
                // not being shown as an Up button
                .setOpenableLayout(binding.drawerLayout)
                .build();

        // Get the NavController from the NavHostFragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        NavHostFragment navHostFragment = (NavHostFragment)
                fragmentManager.findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();

        // Set up the NavigationView with the NavController
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Set up the Toolbar with the NavController and AppBarConfiguration
        NavigationUI.setupWithNavController(binding.appBar.toolbar, navController,
                mAppBarConfiguration);

    }
}
