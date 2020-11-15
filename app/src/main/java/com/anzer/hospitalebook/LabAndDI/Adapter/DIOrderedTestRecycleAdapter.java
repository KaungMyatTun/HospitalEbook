package com.anzer.hospitalebook.LabAndDI.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.listeners.OnItemClickListener;
import com.anzer.hospitalebook.vo.DI_OrderedTestsModel;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DIOrderedTestRecycleAdapter extends RecyclerView.Adapter<DIOrderedTestRecycleAdapter.RecyclerViewHolder>{
    static ArrayList<DI_OrderedTestsModel> arraylist = new ArrayList<DI_OrderedTestsModel>();

    public OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public DIOrderedTestRecycleAdapter(ArrayList<DI_OrderedTestsModel> arraylist) {
        this.arraylist = arraylist;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerViewHolder recyclerViewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ordered_di_test, parent, false);
        recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.test_id.setText(arraylist.get(position).di_item_name);
        holder.test_target_date.setText(arraylist.get(position).di_item_target_date);

        if(arraylist.get(position).di_item_urgent_flag){
            holder.test_u_flag.setImageResource(R.drawable.checkbox_selected);
        }else{
            holder.test_u_flag.setImageResource(R.drawable.checkbox_normal2);
        }

        holder.test_target_date.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                    onItemClickListener.onItemClick(v,position);
            }
        });
        holder.test_u_flag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.e("size", String.valueOf(arraylist.size()));
        return arraylist.size();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView test_id, test_target_date;
        ImageView test_u_flag;
        public View container;

        public RecyclerViewHolder(View view) {
            super(view);
            test_id = view.findViewById(R.id.ordered_di_name);
            test_target_date = view.findViewById(R.id.ordered_di_target_date);
            test_u_flag = view.findViewById(R.id.ordered_checkbox);
            container = view;
        }
    }
}
