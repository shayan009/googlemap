package com.debasish.demomap.ui.fragment.map;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.debasish.demomap.IntentServiceResult;
import com.debasish.demomap.LocationInfoDialog;
import com.debasish.demomap.R;
import com.debasish.demomap.ui.activity.home.MainActivity;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaOptions;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.CustomCap;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.maps.android.ui.IconGenerator;
import com.rp.basefiles.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;


public class MapFragment extends BaseFragment implements OnMapReadyCallback, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMyLocationButtonClickListener,MapMVP.IView {

    public static final String TAG = MapFragment.class.getName();
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
    private String address1, address2, city, state, country, county, PIN;
    private static final String LOG_TAG = MapFragment.class.getSimpleName();
    ArrayList<LatLng> intentServiceResults=new ArrayList<>();
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
    private MarkerOptions markerOptions;
    private ProgressDialog progressDialog;
    private SupportMapFragment mapFragment;
    List<Location> locationList=new ArrayList<>();
    private Marker mPositionMarker;
    private Circle circle;

    public MapFragment () {
        // Required empty public constructor
    }

    MapMVP.IPresenter presenter;
    CompositeDisposable compositeDisposable=new CompositeDisposable();
               Context context;
    @Override
    public void onViewCreated(View rootView, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(rootView, savedInstanceState);
        context=getBaseContext();
        ButterKnife.bind(this,rootView);
        initializeSnackBar(rootView);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        mapFragment = (SupportMapFragment)
                this.getChildFragmentManager()
                        .findFragmentById(R.id.map);

        presenter=new MapPresenter(compositeDisposable);
        presenter.onAttach(this);

        presenter.checkLocationSettings();



    }
    @Override
    public int getLayoutRes () {
        return R.layout.fragment_map_show;
    }



    private List<Marker> destinationMarkers = new ArrayList<>();

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {

            // googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.getUiSettings().setCompassEnabled(true);

            googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
           // googleMap.getUiSettings().setMapToolbarEnabled(true);
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
      /*  Toast.makeText(getActivity(),"latitude="+event.getLatitude()+",longitude="+event.getLongitude(),Toast.LENGTH_SHORT).show();*/
      

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
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        updateLocationUI();
        presenter.getLatestLocation();


       /* LatLng latLng=new LatLng(19.06121897964199, 72.863322294041);

        googleMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                .title("Start")
                .position(latLng));
         markerOptions = new MarkerOptions();
        // Setting the position for the marker
        markerOptions.position(latLng);
        // Setting the title for the marker.
        // This will be displayed on taping the marker
        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
        // Clears the previously touched position
        googleMap.clear();
        // Animating to the touched position
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        // Placing a marker on the touched position
      Marker marker=  googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.equals(marker))
                {
                 setPanoroma(marker.getPosition());
                }
                return false;
            }
        });
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                markerOptions.position(latLng);

                // Setting the title for the marker.
                // This will be displayed on taping the marker
                markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                googleMap.addMarker(markerOptions);
                new DownloadRawData().execute(latLng);
            }
        });*/
// Store a data object with the polyline, used here to indicate an arbitrary type.



        //getDeviceLocation();
    }

    public void setLocationNew(Location location){
        if (location == null)
            return;
        LatLng currentLatLon = new LatLng(location.getLatitude(), location.getLongitude());
        //drawMapCircle(googleMap,currentLatLon,circle);

        mPositionMarker = googleMap.addMarker(new MarkerOptions()
                .flat(false)
                .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.meeting_point))
                .anchor(0.5f, 1f)
                .position(currentLatLon));
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(currentLatLon);

     LatLngBounds latLngBounds=   builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(latLngBounds, 0);
        animateMarker(mPositionMarker, location); // Helper method for smooth
        // animation
        googleMap.animateCamera(cu);
        googleMap.setOnCameraChangeListener(cameraPosition -> {
            double radiusInMeters = 100.0;
            int strokeColor = 0xffff0000; //red outline
            int shadeColor = 0x44ff0000; //opaque red fill
            float zoom = cameraPosition.zoom;
            Log.d(TAG, "setLocationNew: " + zoom);
            if (zoom > 15) {
                if (circle == null) {
                    Log.i(TAG, "setLocationNew1: "+mPositionMarker.getPosition());
                    circle = googleMap.addCircle(new CircleOptions()
                            .center(mPositionMarker.getPosition())
                            .radius(radiusInMeters)
                            .strokeWidth(0)
                            .strokeColor(strokeColor)
                            .fillColor(shadeColor)
                    );
                } else {
                    circle.setVisible(true);
                    Log.i(TAG, "setLocationNew2: "+mPositionMarker.getPosition());
                    circle.setCenter(mPositionMarker.getPosition());
                }

            } else {
                Log.i(TAG, "setLocationNew3: "+mPositionMarker.getPosition());
                if(circle!=null) {
                    circle.setVisible(false);
                }

            }
        });
       

    }
    public Circle drawMapCircle(GoogleMap googleMap,LatLng latLng,Circle currentCircle) {

        // get 2 of the visible diagonal corners of the map (could also use farRight and nearLeft)
        LatLng topLeft = googleMap.getProjection().getVisibleRegion().farLeft;
        LatLng bottomRight = googleMap.getProjection().getVisibleRegion().nearRight;

        // use the Location class to calculate the distance between the 2 diagonal map points
        float results[] = new float[4]; // probably only need 3
        Location.distanceBetween(topLeft.latitude,topLeft.longitude,bottomRight.latitude,bottomRight.longitude,results);
        float diagonal = results[0];

        // use 5% of the diagonal for the radius (gives a 10% circle diameter)
        float radius = diagonal / 20;

        Circle circle = currentCircle;

            circle.setRadius(radius);


        return circle;
    }
    public void animateMarker(final Marker marker, final Location location) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        final LatLng startLatLng = marker.getPosition();
        final double startRotation = marker.getRotation();
        final long duration = 500;

        final Interpolator interpolator = new LinearInterpolator();

        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);

                double lng = t * location.getLongitude() + (1 - t)
                        * startLatLng.longitude;
                double lat = t * location.getLatitude() + (1 - t)
                        * startLatLng.latitude;

                float rotation = (float) (t * location.getBearing() + (1 - t)
                        * startRotation);

                marker.setPosition(new LatLng(lat, lng));
                marker.setRotation(rotation);

                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                }
            }
        });
    }
    public void setPanoroma(LatLng latLng){
        options = new StreetViewPanoramaOptions();
        Log.d("Loc", String.valueOf(latLng));
        options.position(latLng);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
/*
        Bundle mStreetViewBundle = outState.getBundle(STREETVIEW_BUNDLE_KEY);
        if (mStreetViewBundle == null) {
            mStreetViewBundle = new Bundle();
            outState.putBundle(STREETVIEW_BUNDLE_KEY, mStreetViewBundle);
        }

        mStreetViewPanoramaView.onSaveInstanceState(mStreetViewBundle);*/
    }
    private void setLocation(double lat,double lon,String text) {

        LatLng loc = new LatLng(lat, lon);
        intentServiceResults.add(loc);
        if(intentServiceResults.size()>0){




            Polyline polyline= googleMap.addPolyline(new PolylineOptions()
                    .clickable(true).addAll(intentServiceResults));
            polyline.setTag("B");

            stylePolyline(polyline);
        }
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
    public void onRefresh () {

    }

    @Override
    public void finishActivity () {
        ((MainActivity)context).finish();
    }

    @Override
    public void saveLocation (Address address) {
        Location location = new Location(LocationManager.GPS_PROVIDER);
        location.setLatitude(address.getLatitude());
        location.setLongitude(address.getLongitude());
        locationList.add(location);
        if(locationList.size()==1)
            setLocationNew(locationList.get(0));
    }

    @Override
    public void initiateGoogleMap () {
        if(mapFragment!=null)
        mapFragment.getMapAsync(this);
    }


    private class DownloadRawData extends AsyncTask<LatLng, Void, ArrayList<String>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
              progressDialog=new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading........");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected ArrayList<String> doInBackground(LatLng... latLng) {
            ArrayList<String> strings=retrieveData(latLng[0].latitude,latLng[0].longitude);
            return strings;
        }

        @Override
        protected void onPostExecute(ArrayList<String> s) {
            super.onPostExecute(s);
            if(progressDialog!=null)
            progressDialog.dismiss();
            LocationInfoDialog successRechargeDialog=new LocationInfoDialog(getActivity(),s);
            successRechargeDialog.show();
            successRechargeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            successRechargeDialog.setCancelable(false);
        }
    }
    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }
    private void init() {
        address1 = "";
        address2 = "";
        city = "";
        state = "";
        country = "";
        county = "";
        PIN = "";
    }
    private String createUrl(double latitude, double longitude) throws UnsupportedEncodingException {
        init();
        return "https://maps.googleapis.com/maps/api/geocode/json?" + "latlng=" + latitude + "," + longitude + "&key=" + getActivity().getResources().getString(R.string.map_apiid);
    }
    private URL buildUrl(double latitude, double longitude) {

        try {
            Log.w(TAG, "buildUrl: "+createUrl(latitude,longitude));
            return new URL(createUrl(latitude,longitude));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "can't construct location object");
            return null;
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");
            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public String getAddress1() { return address1; }

    public String getAddress2() { return address2; }

    public String getCity() { return city; }

    public String getState() { return state; }

    public String getCountry() { return country; }

    public String getCounty() { return county; }

    public String getPIN() { return PIN; }
    private ArrayList<String> retrieveData(double latitude, double longitude) {
        ArrayList<String> strings=new ArrayList<>();
        try {
            String responseFromHttpUrl = getResponseFromHttpUrl(buildUrl(latitude, longitude));
            JSONObject jsonResponse = new JSONObject(responseFromHttpUrl);
            String status = jsonResponse.getString("status");
            if (status.equalsIgnoreCase("OK")) {
                JSONArray results = jsonResponse.getJSONArray("results");
                JSONObject zero = results.getJSONObject(0);
                JSONArray addressComponents = zero.getJSONArray("address_components");
                String formatadd= zero.getString("formatted_address");

                for (int i = 0; i < addressComponents.length(); i++) {
                    JSONObject zero2 = addressComponents.getJSONObject(i);
                    String longName = zero2.getString("long_name");
                    JSONArray types = zero2.getJSONArray("types");
                    String type = types.getString(0);


                    if (!TextUtils.isEmpty(longName)) {
                        if (type.equalsIgnoreCase("street_number")) {
                            address1 = longName + " ";

                        } else if (type.equalsIgnoreCase("route")) {
                            address1 = address1 + longName;
                        } else if (type.equalsIgnoreCase("sublocality")) {
                            address2 = longName;
                        } else if (type.equalsIgnoreCase("locality")) {
                            // address2 = address2 + longName + ", ";
                            city = longName;
                        } else if (type.equalsIgnoreCase("administrative_area_level_2")) {
                            county = longName;
                        } else if (type.equalsIgnoreCase("administrative_area_level_1")) {
                            state = longName;
                        } else if (type.equalsIgnoreCase("country")) {
                            country = longName;
                        } else if (type.equalsIgnoreCase("postal_code")) {
                            PIN = longName;
                        }
                    }
                }
                strings.add(formatadd);
                strings.add(address1);
                strings.add(address2);
                strings.add(city);
                strings.add(county);
                strings.add(state);
                strings.add(country);

                strings.add(PIN);


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return  strings;
    }

}
