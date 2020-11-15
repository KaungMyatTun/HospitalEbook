package com.anzer.hospitalebook.LabAndDI.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.listeners.OnItemClickListener;
import com.anzer.hospitalebook.vo.Lab_Available_Test;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LabAvailableTestRecycleAdapter extends RecyclerView.Adapter<LabAvailableTestRecycleAdapter.RecyclerViewHolder>{
    static ArrayList<Lab_Available_Test> arraylist = new ArrayList<Lab_Available_Test>();
    public OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public LabAvailableTestRecycleAdapter(ArrayList<Lab_Available_Test> arraylist) {
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public LabAvailableTestRecycleAdapter.RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LabAvailableTestRecycleAdapter.RecyclerViewHolder recyclerViewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_available_lab_item, parent, false);
        recyclerViewHolder = new LabAvailableTestRecycleAdapter.RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LabAvailableTestRecycleAdapter.RecyclerViewHolder holder, int position) {
        holder.test_name.setText(arraylist.get(position).labTestName);
        if(arraylist.get(position).isSelected){
            holder.selected.setImageResource(R.drawable.checkbox_selected);
        }else{
            holder.selected.setImageResource(R.drawable.checkbox_normal2);
        }
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arraylist.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView test_name;
        ImageView selected;
        public View container;

        public RecyclerViewHolder(View view) {
            super(view);
            test_name = view.findViewById(R.id.ordered_lab_name);
            selected = view.findViewById(R.id.ordered_checkbox);
            container = view;
        }
    }
}
