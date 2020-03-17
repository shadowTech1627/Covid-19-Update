package com.shadowtech.covid_update.ui.map.countryList;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.shadowtech.covid_update.R;
import com.shadowtech.covid_update.databinding.ItemCountryBinding;
import com.shadowtech.covid_update.model.CountryDataModel;

import java.util.List;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> implements Filterable {

    private List<CountryDataModel> countryDataModelList;
    private List<CountryDataModel> filteredList;
    private Map<String, String> countryMap;

    public CountryAdapter(Context mContext, List<CountryDataModel> countryDataModelList, Map<String, String> countryMap) {
        this.countryDataModelList = countryDataModelList;
        this.filteredList = countryDataModelList;
        this.countryMap = countryMap;
        World.init(mContext);
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCountryBinding itemCountryBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()), R.layout.item_country, parent, false);
        return new CountryViewHolder(itemCountryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        CountryDataModel countryDataModel = filteredList.get(position);
        String countryISO = countryMap.get(countryDataModel.getCountry());
        if (!TextUtils.isEmpty(countryISO)) {
            holder.itemCountryBinding.ivFlag.setImageResource(World.getFlagOf(countryISO));
        }

        holder.countryItemViewModel.cases.set(countryDataModel.getCases());
        holder.countryItemViewModel.country.set(countryDataModel.getCountry());
        holder.countryItemViewModel.critical.set(countryDataModel.getCritical());
        holder.countryItemViewModel.deaths.set(countryDataModel.getDeaths());
        holder.countryItemViewModel.recovered.set(countryDataModel.getRecovered());
        holder.countryItemViewModel.todayCases.set(countryDataModel.getTodayCases());
        holder.countryItemViewModel.todayDeaths.set(countryDataModel.getTodayDeaths());
    }

    @Override
    public int getItemCount() {
        return filteredList != null ? filteredList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        return new CountryFilter(this, countryDataModelList);
    }

    public void setList(List<CountryDataModel> values) {
        filteredList = values;
        notifyDataSetChanged();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        private ItemCountryBinding itemCountryBinding;
        public CountryItemViewModel countryItemViewModel;

        public CountryViewHolder(@NonNull ItemCountryBinding itemCountryBinding) {
            super(itemCountryBinding.getRoot());
            this.itemCountryBinding = itemCountryBinding;
            countryItemViewModel = new CountryItemViewModel();
            itemCountryBinding.setViewModel(countryItemViewModel);
        }
    }

    public List<CountryDataModel> getFilteredList() {
        return filteredList;
    }
}
