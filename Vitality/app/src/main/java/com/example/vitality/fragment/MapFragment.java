package com.example.vitality.fragment;

import static androidx.databinding.DataBindingUtil.findBinding;
import static androidx.databinding.DataBindingUtil.setContentView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.example.vitality.R;
import com.example.vitality.databinding.ActivityDashboardBinding;
import com.example.vitality.databinding.ActivityMapBinding;
import com.example.vitality.databinding.ActivityWorkoutLogBinding;
import com.example.vitality.viewmodel.SharedViewModel;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.location.LocationComponent;
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.maps.Style;

import androidx.annotation.NonNull;

import java.security.Permissions;
import java.util.List;

public class MapFragment extends Fragment implements
        OnMapReadyCallback, PermissionsListener {

    //private ActivityWorkoutLogBinding binding;
    private PermissionsManager permissionsManager;
    private MapboxMap mapboxMap;
    private MapView mapView;

    private ActivityMapBinding binding;
    public MapFragment(){}
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//// Inflate the View for this fragment using the binding
//        binding = ActivityMapBinding.inflate(inflater, container, false);
//        View view = binding.getRoot();
//        SharedViewModel model = new
//                ViewModelProvider(requireActivity()).get(SharedViewModel.class);
//
////        super.onCreate(savedInstanceState);
////        Mapbox.getInstance(requireContext(),getString(R.string.mapbox_access_token));
////        //setContentView(R.layout.fragment_map);
////        setContentView(R.layout.activity_map);
////
////        mapView = findViewById(R.id.mapView);
////        mapView.onCreate(savedInstanceState);
//
//        //return to main page
//        binding.returnButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Navigation.findNavController(view).navigateUp(); // Navigate up to the previous destination
//            }
//        });
//
//        return view;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Mapbox.getInstance(requireContext(),getString(R.string.mapbox_access_token));

        // Inflate the View for this fragment using the binding
        binding = ActivityMapBinding.inflate(inflater, container, false);
        SharedViewModel model = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        return binding.getRoot();
}

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = binding.mapView;
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);//使用异步加载

        //return to main page
        binding.returnButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
    }

    @Override
    public void onMapReady(@NonNull final MapboxMap mapboxMap) {
        MapFragment.this.mapboxMap = mapboxMap;

        mapboxMap.setStyle(Style.OUTDOORS, new Style.OnStyleLoaded() {
            @Override
            public void onStyleLoaded(@NonNull Style style) {
                enableLocationComponent(style);
            }
        });
        // 在这里可以对地图进行初始化操作，例如设置地图的中心点和缩放级别

        // Set up the zoom in button
        Button zoomInButton = binding.zoomInButton;
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapboxMap.easeCamera(CameraUpdateFactory.zoomIn(), 300);
            }
        });

        // Set up the zoom out button
        Button zoomOutButton = binding.zoomOutButton;
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapboxMap.easeCamera(CameraUpdateFactory.zoomOut(), 300);
            }
        });

        //加个标记，标记1-3
        //option
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(35.123456,-7.123456))
                .title("marker one"));
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(36.123456,-7.123456))
                .title("marker two"));
        mapboxMap.addMarker(new MarkerOptions()
                .position(new LatLng(37.123456,-7.123456))
                .title("marker three"));

    }
    @SuppressWarnings({"MissingPermission"})//没给权限
    private void enableLocationComponent(@NonNull Style loadedMapStyle){
        if(PermissionsManager.areLocationPermissionsGranted(getActivity())){
            LocationComponent locationComponent = mapboxMap.getLocationComponent();

            locationComponent.activateLocationComponent(
                    LocationComponentActivationOptions.builder(getActivity(),loadedMapStyle).build());

            locationComponent.setLocationComponentEnabled(true);

            locationComponent.setCameraMode(CameraMode.TRACKING);

            locationComponent.setRenderMode(RenderMode.COMPASS);
        }else{
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(getActivity());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain){
        Toast.makeText(getActivity(),"to use map, we need to your location information",Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPermissionResult(boolean granted){
        if(granted){
            mapboxMap.getStyle(new Style.OnStyleLoaded() {
                @Override
                public void onStyleLoaded(@NonNull Style style) {
                    enableLocationComponent(style);
                }
            });
        }else{
            Toast.makeText(getActivity(),"Error",Toast.LENGTH_LONG).show();
            //finish();
            //binding.returnButton.setOnClickListener(v -> Navigation.findNavController(view).navigateUp());
            Navigation.findNavController(getView()).navigateUp();
            //如果拒绝，返回home界面
        }
    }

    @Override
    @SuppressWarnings({"MissPermission"})
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}
