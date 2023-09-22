package com.example.uklon_android.interfaces;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uklon_android.R;
import com.example.uklon_android.classes.Types;

import java.util.ArrayList;
import java.util.List;

public class CarCardsAdapter extends RecyclerView.Adapter<CarCardsAdapter.PlaceViewHolder> {

    private List<Types> carTypes = new ArrayList<>();
    private Types curType;
    private OnCarClickListener carClickListener;

    public CarCardsAdapter(List<Types> carTypes, OnCarClickListener listener) {
        this.carTypes = carTypes;
        this.carClickListener = listener;
    }

    @NonNull
    @Override
    public PlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_cart, parent, false);
        return new PlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaceViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String carName = carTypes.get(position).name;
        holder.placeNameTextView.setText(carName);
        float carPrice = carTypes.get(position).price;
        holder.TextPrice.setText(String.valueOf(carPrice) + " ₴ (5 min)");

        holder.enterCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curType = carTypes.get(position);
                if (carClickListener != null) {
                    carClickListener.onCarClick(curType);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(carTypes != null) {
            return carTypes.size();
        }
        else {
            return 0;
        }
    }

    public void setCars(List<Types> carTypes) {
        this.carTypes = carTypes;
    }

    public Types getType(){return curType;}

    public interface OnCarClickListener {
        void onCarClick(Types curType);
    }

    static class PlaceViewHolder extends RecyclerView.ViewHolder {

        TextView placeNameTextView;
        TextView TextPrice;
        LinearLayout enterCar;

        public PlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            placeNameTextView = itemView.findViewById(R.id.placeNameTextView);
            TextPrice = itemView.findViewById(R.id.Price);
            enterCar = itemView.findViewById(R.id.typeEnter);
        }
    }
}

