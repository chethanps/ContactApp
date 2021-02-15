package com.example.roomcontactdb.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.roomcontactdb.R;
import com.example.roomcontactdb.db.entity.Contact;

import java.util.ArrayList;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    public interface ItemClickListener {
        void onItemClicked(Contact contact);
    }

    private final ArrayList<Contact> contactArrayList;
    private final ItemClickListener itemClickListener;

    public ContactAdapter(ItemClickListener itemClickListener, ArrayList<Contact> contacts) {
        this.itemClickListener = itemClickListener;
        contactArrayList = contacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.ivIcon.setImageResource(R.drawable.ic_contact_mail_24);
        holder.tvName.setText(contactArrayList.get(position).getName());
        holder.tvMail.setText(contactArrayList.get(position).getEmail());
        holder.itemView.setOnClickListener(view -> itemClickListener.onItemClicked(contactArrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return contactArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivIcon;
        private final TextView tvName;
        private final TextView tvMail;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
            tvMail = itemView.findViewById(R.id.tv_email);
        }
    }
}
