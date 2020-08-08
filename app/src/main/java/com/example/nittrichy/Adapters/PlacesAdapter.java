package com.example.nittrichy.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.Models.Places;
import com.example.nittrichy.R;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {
    private Context context;
    private List<Places> places;

    public PlacesAdapter(Context context, List<Places> places) {
        this.context = context;
        this.places = places;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.place_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.shopNo.setText(places.get(position).getShopNo());
        holder.shopName.setText(places.get(position).getShopName());
        holder.shopTime.setText(places.get(position).getShopTime());
        holder.shopAddress.setText(places.get(position).getShopAddress());
        String shopContact =places.get(position).getShopContact();
        if(shopContact.equals("")){
            Log.i("nothing","ese hi");
        }else {
            holder.shopContact.setText("Contact: "+ shopContact);
        }

    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView shopNo, shopName , shopTime , shopAddress , shopContact;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            shopNo = itemView.findViewById(R.id.shopno);
            shopName = itemView.findViewById(R.id.shopname);
            shopTime = itemView.findViewById(R.id.shoptime);
            shopAddress = itemView.findViewById(R.id.shopaddress);
            shopContact = itemView.findViewById(R.id.shopcontact);

        }
    }
}
