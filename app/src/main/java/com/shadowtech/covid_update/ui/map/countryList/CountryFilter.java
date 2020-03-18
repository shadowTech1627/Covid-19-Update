package com.shadowtech.covid_update.ui.map.countryList;

import android.text.TextUtils;
import android.widget.Filter;

import com.shadowtech.covid_update.model.CountryDataModel;

import java.util.ArrayList;
import java.util.List;

public class CountryFilter extends Filter {
    private CountryAdapter countryAdapter;
    private List<CountryDataModel> origList;
    private List<CountryDataModel> filteredList;

    public CountryFilter(CountryAdapter countryAdapter, List<CountryDataModel> origList) {
        this.countryAdapter = countryAdapter;
        this.origList = origList;
        filteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        filteredList.clear();
        final FilterResults filterResults = new FilterResults();
        if (constraint.length() == 0) {
            filteredList.addAll(origList);
        } else {
            if (TextUtils.isEmpty(constraint)) {
                filteredList.addAll(origList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                for (CountryDataModel item : origList) {
                    if (item.getCountry().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
        }

        filterResults.values = filteredList;
        filterResults.count = filteredList.size();
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        countryAdapter.setList((List<CountryDataModel>)results.values);
        countryAdapter.notifyDataSetChanged();
    }
}
