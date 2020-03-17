package com.shadowtech.covid_update.network;

import com.shadowtech.covid_update.model.CountryDataModel;
import com.shadowtech.covid_update.model.SummaryDataModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface APIService {
    @GET("all")
    Call<SummaryDataModel> getSummaryData();

    @GET("countries")
    Call<List<CountryDataModel>> getCountryWiseData();
}
