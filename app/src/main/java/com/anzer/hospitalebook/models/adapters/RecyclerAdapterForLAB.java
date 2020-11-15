package com.anzer.hospitalebook.models.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzer.hospitalebook.Objects.ListProviderForLAB;
import com.anzer.hospitalebook.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by David on 9/13/2018.
 */

public class RecyclerAdapterForLAB extends RecyclerView.Adapter<RecyclerAdapterForLAB.RecyclerViewHolder> {
    private static final String LAB_Order = "LAB";
    static ArrayList<ListProviderForLAB> lab_arraylist = new ArrayList<ListProviderForLAB>();
    private RecyclerAdapter.OnItemClickListener mOnItemClickListener;
    public static ArrayList<ListProviderForLAB> selectedItemList;

    static String orderType;

    public RecyclerAdapterForLAB(ArrayList<ListProviderForLAB> arraylist, String orderType, RecyclerAdapter.OnItemClickListener onItemClickListener){
        this.lab_arraylist = arraylist;
        this.orderType = orderType;
        mOnItemClickListener = onItemClickListener;
    }
    public RecyclerAdapterForLAB(){}

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerViewHolder recyclerViewHolder;
        if(orderType.equals(LAB_Order)){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lab_list_layout, parent, false);
            recyclerViewHolder = new RecyclerViewHolder(view);
            return recyclerViewHolder;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {
        final ListProviderForLAB listProviderForLAB = lab_arraylist.get(position);;
        if(orderType.equals(LAB_Order)){
//            holder.lab_id.setText(listProviderForLAB.getId());
            holder.lab_date_time.setText(listProviderForLAB.getDate_time());
            holder.lab_ordering_md.setText(listProviderForLAB.getOrdering_md());
            holder.lab_procedure.setText(listProviderForLAB.getProcedures());
            holder.lab_urgent_flag.setText(listProviderForLAB.getUrgent());
            holder.lab_exam_date_time.setText(listProviderForLAB.getExam_date_time());
            holder.lab_comment.setText(listProviderForLAB.getComment());

            if (listProviderForLAB.getOrder_status().equals("Registered")) {
                holder.lab_date_time.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.lab_ordering_md.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.lab_procedure.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.lab_urgent_flag.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.lab_exam_date_time.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.lab_comment.setBackgroundColor(Color.parseColor("#90EE90"));
            }

            else if(listProviderForLAB.getOrder_status().equals("Completed")) {
                holder.lab_date_time.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.lab_ordering_md.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.lab_procedure.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.lab_urgent_flag.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.lab_exam_date_time.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.lab_comment.setBackgroundColor(Color.parseColor("#90EE91"));
            }

            else if(listProviderForLAB.getOrder_status().equals("Sent")) {
                holder.lab_date_time.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.lab_ordering_md.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.lab_procedure.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.lab_urgent_flag.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.lab_exam_date_time.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.lab_comment.setBackgroundColor(Color.parseColor("#ADD8E6"));
            }

            else if(listProviderForLAB.getOrder_status().equals("Stopped")) {
                holder.lab_date_time.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.lab_ordering_md.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.lab_procedure.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.lab_urgent_flag.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.lab_exam_date_time.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.lab_comment.setBackgroundColor(Color.parseColor("#ffb6c1"));
            }

            else if(listProviderForLAB.getOrder_status().equals("Held")) {
                //holder.container.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.lab_date_time.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.lab_ordering_md.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.lab_procedure.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.lab_urgent_flag.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.lab_exam_date_time.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.lab_comment.setBackgroundColor(Color.parseColor("#FFFFE0"));
            }

            else if(listProviderForLAB.getOrder_status().equals("Reactivated")) {
                //holder.container.setBackgroundColor(Color.parseColor("Red"));
            }
            else if(listProviderForLAB.getOrder_status().equals("Scheduled")) {
                //holder.container.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.lab_date_time.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.lab_ordering_md.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.lab_procedure.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.lab_urgent_flag.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.lab_exam_date_time.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.lab_comment.setBackgroundColor(Color.parseColor("#8FBC8B"));
            }
            else if (listProviderForLAB.getOrder_status().equals("Edited")) {
                //holder.container.setBackgroundColor(Color.parseColor("#fffaaa"));
            }
            else if(listProviderForLAB.getOrder_status().equals("Incomplete")) {
                //holder.container.setBackgroundColor(Color.parseColor("#4adkfl"));
            }
        }

        // set the listener class
//        holder.container.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
//                listProviderForLAB.setSelected(!listProviderForLAB.isSelected());
//                //holder.di_id.setBackgroundColor(listProviderForLAB.isSelected() ? Color.CYAN : Color.WHITE);
//                if(listProviderForLAB.isSelected())
//                    holder.checkBox.setChecked(true);
//                else
//                    holder.checkBox.setChecked(false);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        int Arr_size = 0;
        if(orderType.equals(LAB_Order))
            Arr_size = lab_arraylist.size();

        return Arr_size;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        // variables for lab list
        TextView lab_id, lab_date_time, lab_ordering_md, lab_procedure, lab_urgent_flag, lab_exam_date_time, lab_comment;

        public View container;
        public RecyclerViewHolder (View view){
            super(view);
            if(orderType.equals(LAB_Order)){
                lab_date_time = view.findViewById(R.id.lab_date_time);
                lab_ordering_md = view.findViewById(R.id.lab_ordering_md);
                lab_procedure = view.findViewById(R.id.lab_procedure);
                lab_urgent_flag = view.findViewById(R.id.lab_urgent_flag);
                lab_exam_date_time = view.findViewById(R.id.lab_exam_date_time);
                lab_comment = view.findViewById(R.id.lab_comment);
            }
            container = view;
        }
    }

    // get the selected items
//    public ArrayList getSelectedItem() {
//        selectedItemList = new ArrayList<ListProviderForLAB>();
//        Log.e("array size", String.valueOf(lab_arraylist.size()));
//        for (int i = 0; i < lab_arraylist.size(); i++) {
//            ListProviderForLAB itemlist = lab_arraylist.get(i);
//            if (itemlist.isSelected()) {
//                selectedItemList.add(itemlist);
//                Log.e("name ", itemlist.getProcedures());
//            }
//        }
//        Log.e("selected item : >> ", String.valueOf(selectedItemList.size()));
//        return selectedItemList;
//    }
}
