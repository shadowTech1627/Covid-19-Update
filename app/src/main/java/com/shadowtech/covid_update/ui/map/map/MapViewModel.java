package com.shadowtech.covid_update.ui.map.map;

import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.shadowtech.covid_update.model.CountryDataModel;
import com.shadowtech.covid_update.model.SummaryDataModel;
import com.shadowtech.covid_update.repository.CountryWiseDataRepo;
import com.shadowtech.covid_update.repository.SummaryDataRepo;

import java.util.List;

public class MapViewModel extends ViewModel {

    private SummaryDataRepo summaryDataRepo;
    private MutableLiveData<SummaryDataModel> summaryData;
    private CountryWiseDataRepo countryWiseDataRepo ;
    private MutableLiveData<List<CountryDataModel>> countryData;

    public ObservableField<Integer> confirmedCases = new ObservableField<>();
    public ObservableField<Integer> recoveredCases = new ObservableField<>();
    public ObservableField<Integer> deathsCases = new ObservableField<>();


    public MapViewModel() {
        checkForSummaryData();
        checkForCountryWiseData();
    }

    public void refresh() {
        checkForSummaryData();
        checkForCountryWiseData();
    }
    private void checkForCountryWiseData() {
        countryWiseDataRepo = CountryWiseDataRepo.getIntance();
        countryData = countryWiseDataRepo.getCountryWiseData();

    }

    private void checkForSummaryData() {
        summaryDataRepo = SummaryDataRepo.getIntance();
        summaryData = summaryDataRepo.getSummaryData();
    }

    public LiveData<SummaryDataModel> getSummaryData() {
        return summaryData;
    }

    public MutableLiveData<List<CountryDataModel>> getCountryData() {
        return countryData;
    }

}
