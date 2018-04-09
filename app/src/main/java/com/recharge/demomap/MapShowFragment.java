package com.recharge.demomap;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,   LocationListener
public class MapShowFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener {

    public static final String TAG = MapShowFragment.class.getName();
    private static final String ARG_LAT = "lat";
    private static final String ARG_LONG = "long";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final float DEFAULT_ZOOM = 20;
    private final static int REQUEST_CHECK_SETTINGS_GPS=0x1;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS=0x2;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 15;
    private static final String STREETVIEW_BUNDLE_KEY = "StreetViewBundleKey";
    private double lat;
    private double longg;
    ArrayList<LatLng> intentServiceResults=new ArrayList<>();
    private RelativeLayout mLayout;
    private GoogleMap googleMap;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private GoogleApiClient googleApiClient;
    private Polyline polyline;
    private String mLastUpdateTime;
    private Marker mapMarker;
    private StreetViewPanoramaView mStreetViewPanoramaView;
    private StreetViewPanorama mPanorama;
    private StreetViewPanoramaOptions options;
    private Bundle savedInstanceState;

    public MapShowFragment() {
        // Required empty public constructor
    }


    public static MapShowFragment newInstance(double param1, double param2) {
        MapShowFragment fragment = new MapShowFragment();
        Bundle args = new Bundle();
        args.putDouble(ARG_LAT, param1);
        args.putDouble(ARG_LONG, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lat = getArguments().getDouble(ARG_LAT);
            longg = getArguments().getDouble(ARG_LONG);

            intentServiceResults.add(new LatLng(lat,longg));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_show, container, false);

        return view;
    }


    private List<Marker> destinationMarkers = new ArrayList<>();

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {

            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            googleMap.getUiSettings().setMapToolbarEnabled(true);
            googleMap.setBuildingsEnabled(true);
            //googleMap.setIndoorEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(this);
            googleMap.setOnMyLocationClickListener(this);

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private void addMarker(IntentServiceResult intentServiceResult) {
        mapMarker.remove();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        MarkerOptions options = new MarkerOptions();
Location location=new Location("gps");
location.setLatitude(intentServiceResult.getLatitude());
location.setLongitude(intentServiceResult.getLongitude());
        // following four lines requires 'Google Maps Android API Utility Library'
        // https://developers.google.com/maps/documentation/android/utility/
        // I have used this to display the time as title for location markers
        // you can safely comment the following four lines but for this info
        IconGenerator iconFactory = new IconGenerator(getActivity());
        iconFactory.setStyle(IconGenerator.STYLE_PURPLE);
        options.icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(mLastUpdateTime)));
        options.anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        LatLng currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        options.position(currentLatLng);
         mapMarker = googleMap.addMarker(options);
        long atTime = location.getTime();
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date(atTime));
        mapMarker.setTitle(mLastUpdateTime);
       // destinationMarkers.add(mapMarker);
        Log.d(TAG, "Marker added.............................");
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng,
                13));
        Log.d(TAG, "Zoom done.............................");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
           //getDeviceLocation();
        }
        return true;
    }
  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void messageEventFromService(IntentServiceResult event){
       Toast.makeText(getActivity(),event.getmMessage(),Toast.LENGTH_SHORT).show();
    }*/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(IntentServiceResult event){

        //addMarker(event);
        Toast.makeText(getActivity(),"latitude="+event.getLatitude()+",longitude="+event.getLongitude(),Toast.LENGTH_SHORT).show();
        intentServiceResults.add(new LatLng(event.getLatitude(),event.getLongitude()));
if(intentServiceResults.size()>0){




    Polyline polyline= googleMap.addPolyline(new PolylineOptions()
            .clickable(true).addAll(intentServiceResults));
    polyline.setTag("B");

    stylePolyline(polyline);
}


       /* if(intentServiceResults.size()>0) {

            setLocation(intentServiceResults.get(intentServiceResults.size() - 1).getLatitude(), intentServiceResults.get(intentServiceResults.size() - 1).getLongitude(), "Current Location");
        }*/
        }
    /*@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onEvent(IntentServiceResult event) {

    }*/
    private void stylePolyline(Polyline polyline) {
        String type = "";
        // Get the data object stored with the polyline.
        if (polyline.getTag() != null) {
            type = polyline.getTag().toString();
        }

        switch (type) {
            // If no type is given, allow the API to use the default.
            case "A":
                // Use a custom bitmap as the cap at the start of the line.
                polyline.setStartCap(
                        new CustomCap(
                                BitmapDescriptorFactory.fromResource(R.drawable.ic_directions_walk_black_24dp), 10));
                break;
            case "B":
                // Use a round cap at the start of the line.
                polyline.setStartCap(new RoundCap());
                break;
        }

        polyline.setEndCap(new RoundCap());
        polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
        polyline.setColor(Color.RED);
        polyline.setJointType(JointType.ROUND);
    }
    private void getDeviceLocation() {
    /*
     * Get the best and most recent location of the device, which may be null in rare
     * cases when a location is not available.
     */
        //  Snackbar.make(mLayout,"location access"+mLocationPermissionGranted,Snackbar.LENGTH_SHORT).show();
        try {

            Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
            locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {

                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        mLastKnownLocation = task.getResult();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.");
                        Log.e(TAG, "Exception: %s", task.getException());
                        googleMap.moveCamera(CameraUpdateFactory
                                .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

                    }
                }
            });

        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
       this.savedInstanceState=savedInstanceState;
        super.onViewCreated(rootView, savedInstanceState);
        mLayout = (RelativeLayout) rootView.findViewById(R.id.layoutmap);
        // Snackbar.make(mLayout,"location access1 "+mLocationPermissionGranted,Snackbar.LENGTH_SHORT).show();
     mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        SupportMapFragment mapFragment = (SupportMapFragment)
                this.getChildFragmentManager()
                        .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        options = new StreetViewPanoramaOptions();
         final LatLng SYDNEY = new LatLng(-33.87365, 151.20689);
        options.position(SYDNEY);
        mStreetViewPanoramaView = new StreetViewPanoramaView(getActivity(), options);
        getActivity().addContentView(mStreetViewPanoramaView,
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        // *** IMPORTANT ***
        // StreetViewPanoramaView requires that the Bundle you pass contain _ONLY_
        // StreetViewPanoramaView SDK objects or sub-Bundles.
        Bundle mStreetViewBundle = null;
        if (savedInstanceState != null) {
            mStreetViewBundle = savedInstanceState.getBundle(STREETVIEW_BUNDLE_KEY);
        }
        mStreetViewPanoramaView.onCreate(mStreetViewBundle);

    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateLocationUI();
        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                .title("Start")
                .position(new LatLng(lat,longg)));

// Store a data object with the polyline, used here to indicate an arbitrary type.



        //getDeviceLocation();
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mStreetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY);
        if (mStreetViewBundle == null) {
            mStreetViewBundle = new Bundle();
            outState.putBundle(STREETVIEW_BUNDLE_KEY, mStreetViewBundle);
        }

        mStreetViewPanoramaView.onSaveInstanceState(mStreetViewBundle);
    }
    private void setLocation(double lat,double lon,String text) {

        LatLng loc = new LatLng(lat, lon);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(loc).zoom(DEFAULT_ZOOM).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, DEFAULT_ZOOM));

        googleMap.addMarker(new MarkerOptions().position(loc)
                .title(text));

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }


}
