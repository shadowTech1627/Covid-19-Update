package com.shadowtech.covid_update.ui.map.countryList;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

public class CountryItemViewModel extends ViewModel {

    public ObservableField<Integer> cases = new ObservableField<>();
    public ObservableField<Integer> deaths = new ObservableField<>();
    public ObservableField<Integer> todayCases = new ObservableField<>();
    public ObservableField<Integer> todayDeaths = new ObservableField<>();
    public ObservableField<Integer> recovered = new ObservableField<>();
    public ObservableField<Integer> critical = new ObservableField<>();
    public ObservableField<String> country = new ObservableField<>();


    public CountryItemViewModel() {
    }
}
