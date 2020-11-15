package com.anzer.hospitalebook.models.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzer.hospitalebook.Objects.ListProviderForConsumable;
import com.anzer.hospitalebook.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by David on 9/13/2018.
 */

public class RecyclerAdapterForConsumable extends RecyclerView.Adapter<RecyclerAdapterForConsumable.RecyclerViewHolder> {

    private static final String Consumable_Order = "Consumable";
    static ArrayList<ListProviderForConsumable> con_arraylist = new ArrayList<ListProviderForConsumable>();
    private RecyclerAdapter.OnItemClickListener mOnItemClickListener;
    public static ArrayList<ListProviderForConsumable> selectedItemList;

    static String orderType;

    public RecyclerAdapterForConsumable(ArrayList<ListProviderForConsumable> arraylist, String orderType, RecyclerAdapter.OnItemClickListener onItemClickListener){
        this.con_arraylist = arraylist;
        this.orderType = orderType;
        mOnItemClickListener = onItemClickListener;
    }
    public RecyclerAdapterForConsumable(){}

//    public interface OnItemClickListener {
//        public void onItemClick(View view, int position);
//    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerViewHolder recyclerViewHolder;

        if(orderType.equals(Consumable_Order)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.consumable_list_layout, parent, false);
            recyclerViewHolder = new RecyclerViewHolder(view);
            return recyclerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        final ListProviderForConsumable listProviderForConsumable = con_arraylist.get(position);
        if(orderType.equals(Consumable_Order)){
//            holder.con_id.setText(listProviderForConsumable.getId());
            holder.con_material.setText(listProviderForConsumable.getMaterial());
            holder.con_orderedBy.setText(listProviderForConsumable.getOrderedBy());
            holder.con_date.setText(listProviderForConsumable.getDate());
            holder.con_unit.setText(listProviderForConsumable.getUnits());
            holder.con_comment.setText(listProviderForConsumable.getComment());

            if (listProviderForConsumable.getOrder_status().equals("Registered")) {
                holder.con_material.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.con_orderedBy.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.con_date.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.con_unit.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.con_comment.setBackgroundColor(Color.parseColor("#90EE90"));
            }

            else if(listProviderForConsumable.getOrder_status().equals("Completed")) {
                holder.con_material.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.con_orderedBy.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.con_date.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.con_unit.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.con_comment.setBackgroundColor(Color.parseColor("#90EE91"));
            }

            else if(listProviderForConsumable.getOrder_status().equals("Sent")) {
                holder.con_material.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.con_orderedBy.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.con_date.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.con_unit.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.con_comment.setBackgroundColor(Color.parseColor("#ADD8E6"));
            }

            else if(listProviderForConsumable.getOrder_status().equals("Stopped")) {
                holder.con_material.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.con_orderedBy.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.con_date.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.con_unit.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.con_comment.setBackgroundColor(Color.parseColor("#ffb6c1"));
            }

            else if(listProviderForConsumable.getOrder_status().equals("Held")) {
                //holder.container.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.con_material.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.con_orderedBy.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.con_date.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.con_unit.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.con_comment.setBackgroundColor(Color.parseColor("#FFFFE0"));
            }

            else if(listProviderForConsumable.getOrder_status().equals("Reactivated")) {
                //holder.container.setBackgroundColor(Color.parseColor("Red"));
            }
            else if(listProviderForConsumable.getOrder_status().equals("Scheduled")) {
                //holder.container.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.con_material.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.con_orderedBy.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.con_date.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.con_unit.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.con_comment.setBackgroundColor(Color.parseColor("#8FBC8B"));
            }
            else if (listProviderForConsumable.getOrder_status().equals("Edited")) {
                //holder.container.setBackgroundColor(Color.parseColor("#fffaaa"));
            }
            else if(listProviderForConsumable.getOrder_status().equals("Incomplete")) {
                //holder.container.setBackgroundColor(Color.parseColor("#4adkfl"));
            }
        }
    }

    @Override
    public int getItemCount() {
        int Arr_size = 0;
        if(orderType.equals(Consumable_Order))
            Arr_size =  con_arraylist.size();
        return Arr_size;
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView con_id, con_material, con_orderedBy, con_date, con_unit, con_comment;
        public View container;
        public RecyclerViewHolder (View view){
            super(view);
              if(orderType.equals(Consumable_Order)){
                  con_material = view.findViewById(R.id.con_material);
                  con_orderedBy = view.findViewById(R.id.con_orderedBy);
                  con_date = view.findViewById(R.id.con_date);
                  con_unit = view.findViewById(R.id.con_units);
                  con_comment = view.findViewById(R.id.con_comment);
              }
              container = view;
        }
    }
}
