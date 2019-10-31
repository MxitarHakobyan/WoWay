package com.mino.woway.ui.activities;

import android.annotation.SuppressLint;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.mino.woway.R;
import com.mino.woway.listeners.OnUserLocationUpdatedListener;
import com.mino.woway.location_providers.SingleShotLocationProvider;
import com.mino.woway.map_components.ChangeableClusterManager;
import com.mino.woway.users_model.Owner;
import com.mino.woway.utils.GoogleServicesAvailability;
import com.mino.woway.utils.OwnerClient;

import java.util.Arrays;
import java.util.Objects;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.mino.woway.utils.Constants.COUNTRY_ARMENIA;

@RuntimePermissions
public class MapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        OnUserLocationUpdatedListener {

    private static final String TAG = "MapActivity";

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    private AutocompleteSupportFragment mAutoCFragment;
    private SingleShotLocationProvider mLocationProvider;
    private ChangeableClusterManager mChangeableClusterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (GoogleServicesAvailability.isServicesOk(MapActivity.this)) {
            init();
            initPlaces();
            settingUpListenerOnAutoCom();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        MapActivityPermissionsDispatcher.requestLocationWithPermissionCheck(MapActivity.this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        MapActivityPermissionsDispatcher.settingUpMapUiWithPermissionCheck(MapActivity.this);
        mChangeableClusterManager = new ChangeableClusterManager(MapActivity.this, mMap);
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(value = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    protected void settingUpMapUi() {
        mMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mMap.getUiSettings();
        uiSettings.setMapToolbarEnabled(false);
        ImageView imageView = findViewById(R.id.imageView2);
        Glide.with(this).load(((OwnerClient) getApplicationContext()).getOwner().getAvatarUrl()).into(imageView);
    }

    private void init() {
        mLocationProvider = SingleShotLocationProvider.newInstance(getApplicationContext(), this);

        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mAutoCFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        Objects.requireNonNull(mAutoCFragment).setHint("Endpoint");
        if (mMapFragment != null) {
            mMapFragment.getMapAsync(this);
        }
    }

    private void initPlaces() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_maps_api_key));
        }
    }

    private void settingUpListenerOnAutoCom() {
        mAutoCFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        mAutoCFragment.setCountry(COUNTRY_ARMENIA);
        mAutoCFragment.setTypeFilter(TypeFilter.ADDRESS);
        mAutoCFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                LatLng endpoint = place.getLatLng();
                Log.d(TAG, "Place: " + place.getName() + ", " + place.getId() + ", " + endpoint);
                updateOwnerEndpoint(endpoint);
//                clearOnCameraChangeListener();
                changeCameraDirection(endpoint);
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(MapActivity.this, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateOwnerEndpoint(LatLng endpoint) {
        OwnerClient ownerClient = ((OwnerClient) getApplicationContext());
        Owner owner = ownerClient.getOwner();
        owner.setEndLatLng(endpoint);
        ownerClient.setOwner(owner);
//        mChangeableClusterManager.drawEndpointMarker();
    }

    private void clearOnCameraChangeListener() {
        mMap.setOnCameraMoveListener(null);
    }


    //TODO Changable markers only
//    private void settingUpListenerOnClusterMarker() {
//        mMarkerClusterManager.setOnClusterItemClickListener(clusterItem -> {
//            new UserContactDialog().show(getSupportFragmentManager(), "userContact");
//            return true;
//        });
//    }
    @NeedsPermission(value = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    protected void requestLocation() {
        if (mLocationProvider != null) {
            mLocationProvider.requestSingleUpdate();
        }
    }

    @Override
    public void onLocationUpdated(Location updatedLocation) {
        LatLng updatedLatLng = new LatLng(updatedLocation.getLatitude(), updatedLocation.getLongitude());
        updateOwnerStartPoint(updatedLatLng);
        changeCameraDirection(updatedLatLng);
    }

    private void updateOwnerStartPoint(LatLng updatedStartLatLng) {
        OwnerClient ownerClient = ((OwnerClient) getApplicationContext());
        Owner owner = ownerClient.getOwner();
        owner.setStartLatLng(updatedStartLatLng);
        ownerClient.setOwner(owner);
//        mChangeableClusterManager.drawStartPointMarker();
        settingUpOnCameraChangeListener();
    }

    private void settingUpOnCameraChangeListener() {
        mMap.setOnCameraMoveListener(() -> {
//            updateOwnerStartPoint(mMap.getCameraPosition().target);
            Log.d(TAG, "loc = " + mMap.getCameraPosition().target.toString());
        });
    }

    private void changeCameraDirection(LatLng latLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
    }

    public void onSearchFabClicked(View view) {
        //TODO
    }

    public void onLocationSearchingClicked(View view) {
        MapActivityPermissionsDispatcher.requestLocationWithPermissionCheck(MapActivity.this);
    }

    @OnPermissionDenied(value = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    protected void onLocationDenied() {
        Toast.makeText(this, getResources().getString(R.string.permission_denied_message), Toast.LENGTH_LONG).show();
        MapActivityPermissionsDispatcher.requestLocationWithPermissionCheck(MapActivity.this);
    }

    @OnNeverAskAgain(value = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    void onNeverAskAgain() {
        Toast.makeText(this, getResources().getString(R.string.never_ask_again_toast), Toast.LENGTH_LONG).show();
    }

    @OnShowRationale(value = {ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION})
    void showRationaleForLocation(final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.no_location_permission_title))
                .setMessage(getResources().getString(R.string.no_location_permission_message))
                .setPositiveButton(getResources().getString(R.string.positive_button_dialog), (dialog, which) -> request.proceed())
                .setNegativeButton(getResources().getString(R.string.negative_button_dialog), (dialog, which) -> request.cancel())
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        MapActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
