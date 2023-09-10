package com.example.uklon_android.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlaceViewHolder> {

    private List<String> places;

    private String nameCurPlace;
    private OnPlaceClickListener placeClickListener;

    public PlacesAdapter(List<String> places, OnPlaceClickListener listener) {
        this.places = places;
        this.placeClickListener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        String placeName = places.get(position);
        holder.placeNameTextView.setText(placeName);

        holder.placeNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Витягнути вміст TextView
                String content = holder.placeNameTextView.getText().toString();

                nameCurPlace = content;
                // Тепер ви можете зробити те, що потрібно зі змінною savedContent
                // Наприклад, вивести її на екран, обробити її тощо.
            }
        });

        holder.btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameCurPlace = places.get(position);
                if (placeClickListener != null) {
                    placeClickListener.onPlaceClick(nameCurPlace);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public void setPlaces(List<String> placeNames) {
        places = placeNames;
    }

    public boolean isCurName()
    {
        if(nameCurPlace != null)
        {
            return true;
        }
        else{
            return false;
        }
    }

    public String getCurName()
    {
        return nameCurPlace;
    }

    public interface OnPlaceClickListener
    {
        void onPlaceClick(String selectedPlace);
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView placeNameTextView;
        ImageButton btnClick;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.placeNameTextView);
            btnClick = itemView.findViewById(R.id.click);
        }
    }
}

