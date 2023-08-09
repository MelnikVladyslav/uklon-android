package com.example.uklon_android.interfaces;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Types;

import java.util.List;

public class CarCardsAdapter extends RecyclerView.Adapter<CarCardsAdapter.PlaceViewHolder> {

    private List<Types> carTypes;
    private Types curType;

    public CarCardsAdapter(List<Types> carTypes) {
        this.carTypes = carTypes;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_cart, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, int position) {
        String carName = carTypes.get(position).name;
        holder.placeNameTextView.setText(carName);
        float carPrice = carTypes.get(position).price;
        holder.TextPrice.setText((int) carPrice);

        holder.enterCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curType = carTypes.get(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return carTypes.size();
    }

    public void setCars(List<Types> carTypes) {
        this.carTypes = carTypes;
    }

    public Types getType(){return curType;}

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView placeNameTextView;
        TextView TextPrice;
        Button enterCar;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.placeNameTextView);
            TextPrice = itemView.findViewById(R.id.Price);
            enterCar = itemView.findViewById(R.id.Enter);
        }
    }
}

