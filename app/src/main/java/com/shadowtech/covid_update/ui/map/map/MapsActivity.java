package com.shadowtech.covid_update.ui.map.map;

import android.animation.Animator;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.GsonBuilder;
import com.shadowtech.covid_update.R;
import com.shadowtech.covid_update.databinding.ActivityMapsBinding;
import com.shadowtech.covid_update.model.CountryDataModel;
import com.shadowtech.covid_update.model.CountryLatLngModel;
import com.shadowtech.covid_update.model.SummaryDataModel;
import com.shadowtech.covid_update.ui.map.countryList.CountryAdapter;
import com.shadowtech.covid_update.utility.Utils;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getName();
    private MapViewModel mapViewModel;
    private ActivityMapsBinding mapsBinding;
    private GoogleMap mMap;
    private Map<String, String> countryMap;
    private Map<String, LatLng> latLngMap;
    private Locale currentLocale;
    private CountryAdapter countryAdapter;
    private List<CountryDataModel> countryDataModelList;
    private int selectedPos = -1;
    private LinearLayoutManager linearLayoutManager;
    private Marker previousMarker;
    private InterstitialAd mInterstitialAd;
    private int interstialCountShow = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mapsBinding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        createMapForISO();
        initView();
        observeData();
        loadAd();
        
    }


    private void createMapForISO() {
        currentLocale = Utils.getCurrentLocale(this);
        String[] isoCountryCodes = Locale.getISOCountries();
        countryMap = new HashMap<>();
        Locale locale;
        String name;

        // Iterate through all country codes:
        for (String code : isoCountryCodes) {
            // Create a locale using each country code
            locale = new Locale("", code);
            // Get country name for each code.
            name = locale.getDisplayCountry();
            // Map all country names and codes in key - value pairs.
            countryMap.put(name, code);
        }

        String json = loadJSONFromAsset();
        latLngMap = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                CountryLatLngModel countryLatLngModel = new GsonBuilder().create()
                        .fromJson(jsonArray.get(i).toString()
                                , CountryLatLngModel.class);
                latLngMap.put(countryLatLngModel.getName(), new LatLng(countryLatLngModel.getLatitude(), countryLatLngModel.getLongitude()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("countrylatLng.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void observeData() {
        mapViewModel.getSummaryData().observe(this, new Observer<SummaryDataModel>() {
            @Override
            public void onChanged(SummaryDataModel summaryDataModel) {
                if (summaryDataModel != null) {
                    mapViewModel.deathsCases.set(summaryDataModel.getDeaths());
                    mapViewModel.confirmedCases.set(summaryDataModel.getCases());
                    mapViewModel.recoveredCases.set(summaryDataModel.getRecovered());
                }
            }
        });

        mapViewModel.getCountryData().observe(this, new Observer<List<CountryDataModel>>() {
            @Override
            public void onChanged(List<CountryDataModel> countryDataModel) {
                if (countryDataModel != null && countryDataModel.size() > 0) {
                    List<CountryDataModel> currentCountryDataModelList = countryDataModel.stream()
                            .filter(p -> p.getCountry().equalsIgnoreCase(currentLocale.getDisplayCountry()))
                            .collect(Collectors.toList());
                    if (currentCountryDataModelList != null && currentCountryDataModelList.size() > 0) {
                        countryDataModel.remove(currentCountryDataModelList.get(0));
                        countryDataModel.add(0, currentCountryDataModelList.get(0));
                    }

                    countryDataModelList.addAll(countryDataModel);
                    countryAdapter.notifyDataSetChanged();
                    setupSearch();
                }


            }
        });
    }

    private void initView() {
        mapViewModel = new MapViewModel();
        mapsBinding.setMapViewModel(mapViewModel);
        mapsBinding.cvMain.setCardBackgroundColor(ContextCompat.getColor(this, R.color.black_transparent_70));
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        countryDataModelList = new ArrayList<>();
        countryAdapter = new CountryAdapter(this, countryDataModelList, countryMap);
        linearLayoutManager = new LinearLayoutManager(this,
                RecyclerView.HORIZONTAL, false);
        mapsBinding.rvCountry.setLayoutManager(linearLayoutManager);
        mapsBinding.rvCountry.setAdapter(countryAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mapsBinding.rvCountry);
        initAddView();
        mapsBinding.fabInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfoPopUP();
            }
        });

        mapsBinding.fabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapViewModel.refresh();
                interstialCountShow++;
                if (interstialCountShow >= 2 && mInterstitialAd != null && mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    loadAd();
                    interstialCountShow = 0;
                }
            }
        });
    }

    private void initAddView() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        mapsBinding.adView.loadAd(adRequest);
    }

    private void setScrollListener() {
        mapsBinding.rvCountry.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                if (pos != -1) {
                    if (selectedPos == -1) {
                        selectedPos = pos;
                    } else if (selectedPos > -1 && selectedPos != pos) {
                        selectedPos = pos;
                    }

                    setMarkerOnMap();
                }
            }
        });
    }

    private void setMarkerOnMap() {
        if (selectedPos > countryAdapter.getFilteredList().size()) {
            return;
        }
        CountryDataModel countryLatLngModel = countryAdapter.getFilteredList().get(selectedPos);
        LatLng country = latLngMap.get(countryLatLngModel.getCountry());
        if (previousMarker != null) {
            previousMarker.remove();
        }
        if (country != null) {
            previousMarker = mMap.addMarker(new MarkerOptions().position(country).title("Marker in" + countryLatLngModel.getCountry()));
            previousMarker.setIcon(Utils.bitmapDescriptorFromVector(this, R.drawable.ic_marker));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(country));
        } else {
            Toast.makeText(this, "Location not found!", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera

        LatLng sydney = latLngMap.get(currentLocale.getDisplayCountry());

        if (previousMarker != null) {
            previousMarker.remove();
        }
        previousMarker = mMap.addMarker(new MarkerOptions().position(sydney).title(currentLocale.getDisplayCountry()));
        previousMarker.setIcon(Utils.bitmapDescriptorFromVector(this, R.drawable.ic_marker));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        setUpMapStyle();
        setScrollListener();
    }

    private void setUpMapStyle() {
        MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.mapstyle_night);
        mMap.setMapStyle(style);

    }

    private void setupSearch() {

        mapsBinding.fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int centerX = (mapsBinding.fabSearch.getLeft() + mapsBinding.fabSearch.getRight()) / 2;
                int centerY = (mapsBinding.fabSearch.getTop() + mapsBinding.fabSearch.getBottom()) / 2;

                int startRadius = 0;
// get the final radius for the clipping circle
                int endRadius = Math.max(mapsBinding.cvMain.getWidth(), mapsBinding.cvMain.getHeight());

// create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(mapsBinding.searchView, centerX, centerY, startRadius, endRadius);

// make the view visible and start the animation
                mapsBinding.searchView.setVisibility(View.VISIBLE);
                mapsBinding.searchInputText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(mapsBinding.searchInputText, InputMethodManager.SHOW_IMPLICIT);
                anim.start();
                mapsBinding.fabSearch.setVisibility(View.INVISIBLE);
            }
        });


        mapsBinding.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapsBinding.searchView.setVisibility(View.GONE);
                mapsBinding.fabSearch.setVisibility(View.VISIBLE);
                countryAdapter.getFilter().filter("");
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mapsBinding.searchInputText.getWindowToken(), 0);
            }
        });

        mapsBinding.searchInputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                countryAdapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    void showInfoPopUP() {
        final LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.dialog_info, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        AlertDialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setView(dialogView);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        Button btn = dialogView.findViewById(R.id.btn_ok);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
    }


    private void loadAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.test_interstitial_ad_unit_id));
        AdRequest adRequestInter = new AdRequest.Builder().build();
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }
        });
        mInterstitialAd.loadAd(adRequestInter);
    }

}
