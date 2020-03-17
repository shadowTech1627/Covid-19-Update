package com.shadowtech.covid_update.repository;

import androidx.lifecycle.MutableLiveData;

import com.shadowtech.covid_update.model.SummaryDataModel;
import com.shadowtech.covid_update.network.APIService;
import com.shadowtech.covid_update.network.NetworkModule;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SummaryDataRepo {
    private static SummaryDataRepo summaryDataRepo;
    private APIService apiService;
    private MutableLiveData<SummaryDataModel> summaryData;
    public SummaryDataRepo() {
        apiService = NetworkModule.getInstance().getAPIService();
    }

    public static SummaryDataRepo getIntance() {
        if (summaryDataRepo == null) {
            summaryDataRepo = new SummaryDataRepo();
        }
        return summaryDataRepo;
    }

    public MutableLiveData<SummaryDataModel> getSummaryData() {
        if (summaryData == null) {
            summaryData = new MutableLiveData<>();
        }
        apiService.getSummaryData().enqueue(new Callback<SummaryDataModel>() {
            @Override
            public void onResponse(Call<SummaryDataModel> call, Response<SummaryDataModel> response) {
                if (response.isSuccessful()) {
                    summaryData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SummaryDataModel> call, Throwable t) {

            }
        });

        return summaryData;
    }
}
