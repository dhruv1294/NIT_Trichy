package com.example.nittrichy.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nittrichy.Models.Contacts;
import com.example.nittrichy.R;

import java.util.List;

public class ContactsAdapter extends  RecyclerView.Adapter<ContactsAdapter.ViewHolder>{
    Context context;
   List<Contacts> contacts;

    public ContactsAdapter(Context context, List<Contacts> contacts) {
        this.context = context;
        this.contacts = contacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(context).inflate(R.layout.contact_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.desig.setText(contacts.get(position).getDesig());
        holder.name.setText(contacts.get(position).getName());
        holder.phoneNo.setText(contacts.get(position).getPhoneNo());


    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView desig,name,phoneNo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            desig = itemView.findViewById(R.id.designation);
            name = itemView.findViewById(R.id.name);
            phoneNo = itemView.findViewById(R.id.phoneNo);

        }
    }
}
