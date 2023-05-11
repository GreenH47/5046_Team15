package com.example.vitality.fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.vitality.MainActivity;
import com.example.vitality.databinding.ActivityDashboardBinding;
import com.example.vitality.databinding.ActivityMainBinding;
import com.example.vitality.databinding.ActivityRetrofitBinding;
import com.example.vitality.databinding.ActivityWorkoutLogBinding;
import com.example.vitality.retrofit.Items;
import com.example.vitality.retrofit.RetrofitClient;
import com.example.vitality.retrofit.RetrofitInterface;
import com.example.vitality.retrofit.SearchResponse;
import com.example.vitality.viewmodel.SharedViewModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class RetrofitFragment  extends Fragment{

    private ActivityRetrofitBinding binding;
    private static final String API_KEY = "";
    private static final String SEARCH_ID_cx = "";
    private String keyword;

    private RetrofitInterface retrofitInterface;
    public RetrofitFragment(){}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
// Inflate the View for this fragment using the binding
        binding = ActivityRetrofitBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        SharedViewModel model = new
                ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // search method
        retrofitInterface = RetrofitClient.getRetrofitService();

        binding.btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = binding.editText.getText().toString();
                Call<SearchResponse> callAsync = retrofitInterface.customSearch(API_KEY, SEARCH_ID_cx, keyword);

                // Make an async request and invoke the callback methods when the response returns
                callAsync.enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                        if (response.isSuccessful()) {
                            List<Items> list = response.body().getItems();
                            // Get the snippet from the object in position 0
                            String result = list.get(0).getSnippet();
                            binding.tvResult.setText(result);
                        } else {
                            Log.i("Error ", "Response failed");
                        }
                    }
                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        //Toast.makeText(RetrofitFragment.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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
