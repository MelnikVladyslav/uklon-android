package com.example.uklon_android.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.PlaceViewHolder> {

    private List<String> city;

    public CityAdapter(List<String> city) {
        this.city = city;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        String placeName = city.get(position);
        holder.placeNameTextView.setText(placeName);
    }

    @Override
    public int getItemCount() {
        return city.size();
    }

    public void setPlaces(List<String> placeNames) {
        city = placeNames;
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView placeNameTextView;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.placeNameTextView);
        }
    }
}

