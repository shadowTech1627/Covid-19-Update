package com.shadowtech.covid_update.repository;

import androidx.lifecycle.MutableLiveData;

import com.shadowtech.covid_update.model.CountryDataModel;
import com.shadowtech.covid_update.network.APIService;
import com.shadowtech.covid_update.network.NetworkModule;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CountryWiseDataRepo {
    private static CountryWiseDataRepo countryWiseDataRepo;
    private APIService apiService;
    private static String TAG = CountryWiseDataRepo.class.getSimpleName();
    private MutableLiveData<List<CountryDataModel>> countryDataModelMutableLiveData;

    public CountryWiseDataRepo() {
        apiService = NetworkModule.getInstance().getAPIService();
    }

    public static CountryWiseDataRepo getIntance() {
        if (countryWiseDataRepo == null) {
            countryWiseDataRepo = new CountryWiseDataRepo();
        }
        return countryWiseDataRepo;
    }

    public MutableLiveData<List<CountryDataModel>> getCountryWiseData() {
        if (countryDataModelMutableLiveData == null)
            countryDataModelMutableLiveData = new MutableLiveData<>();

        apiService.getCountryWiseData().enqueue(new Callback<List<CountryDataModel>>() {
            @Override
            public void onResponse(Call<List<CountryDataModel>> call, Response<List<CountryDataModel>> response) {
                if (response.isSuccessful()) {
                    countryDataModelMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<CountryDataModel>> call, Throwable t) {
            }
        });

        return countryDataModelMutableLiveData;
    }
}
