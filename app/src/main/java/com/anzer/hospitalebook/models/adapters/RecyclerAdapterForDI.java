package com.anzer.hospitalebook.models.adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzer.hospitalebook.Objects.ListProviderForDI;
import com.anzer.hospitalebook.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by David on 9/13/2018.
 */

public class RecyclerAdapterForDI extends RecyclerView.Adapter<RecyclerAdapterForDI.RecyclerViewHolder> {
    private static final String DI_Order = "DI";
    static ArrayList<ListProviderForDI> di_arraylist = new ArrayList<ListProviderForDI>();
    private RecyclerAdapter.OnItemClickListener mOnItemClickListener;
    public static ArrayList<ListProviderForDI> selectedItemList;

    static String orderType;

    public RecyclerAdapterForDI(ArrayList<ListProviderForDI> arraylist, String orderType, RecyclerAdapter.OnItemClickListener onItemClickListener){
        this.di_arraylist = arraylist;
        this.orderType = orderType;
        mOnItemClickListener = onItemClickListener;
    }
    public RecyclerAdapterForDI(){}

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerViewHolder recyclerViewHolder;
        if(orderType.equals(DI_Order)){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.di_list_layout, parent, false);
            recyclerViewHolder = new RecyclerViewHolder(view);
            return recyclerViewHolder;
        }
        return null;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        final ListProviderForDI listProviderForDI = di_arraylist.get(position);
        if(orderType.equals(DI_Order)){
//            holder.di_id.setText(listProviderForDI.getId());
            holder.di_date_time.setText(listProviderForDI.getDate_time());
            holder.di_ordering_md.setText(listProviderForDI.getOrdering_md());
            holder.di_procedure.setText(listProviderForDI.getProcedures());
            holder.di_urgent_flag.setText(listProviderForDI.getUrgent());
            holder.di_department.setText(listProviderForDI.getDepartment());
            holder.di_exam_date_time.setText(listProviderForDI.getExam_date_time());
            holder.di_comment.setText(listProviderForDI.getComment());

            if (listProviderForDI.getOrder_status().equals("Registered")) {
                holder.di_date_time.setBackgroundResource(R.color.reg_color);
                holder.di_ordering_md.setBackgroundResource(R.color.reg_color);
                holder.di_procedure.setBackgroundResource(R.color.reg_color);
                holder.di_urgent_flag.setBackgroundResource(R.color.reg_color);
                holder.di_department.setBackgroundResource(R.color.reg_color);
                holder.di_exam_date_time.setBackgroundResource(R.color.reg_color);
                holder.di_comment.setBackgroundResource(R.color.reg_color);
            }

            else if(listProviderForDI.getOrder_status().equals("Completed")) {
                holder.di_date_time.setBackgroundResource(R.color.completed_color);
                holder.di_ordering_md.setBackgroundResource(R.color.completed_color);
                holder.di_procedure.setBackgroundResource(R.color.completed_color);
                holder.di_urgent_flag.setBackgroundResource(R.color.completed_color);
                holder.di_department.setBackgroundResource(R.color.completed_color);
                holder.di_exam_date_time.setBackgroundResource(R.color.completed_color);
                holder.di_comment.setBackgroundResource(R.color.completed_color);
            }

            else if(listProviderForDI.getOrder_status().equals("Sent")) {
                holder.di_date_time.setBackgroundResource(R.color.sent_color);
                holder.di_ordering_md.setBackgroundResource(R.color.sent_color);
                holder.di_procedure.setBackgroundResource(R.color.sent_color);
                holder.di_urgent_flag.setBackgroundResource(R.color.sent_color);
                holder.di_department.setBackgroundResource(R.color.sent_color);
                holder.di_exam_date_time.setBackgroundResource(R.color.sent_color);
                holder.di_comment.setBackgroundResource(R.color.sent_color);

                if (listProviderForDI.getOrder_status().equals("Edited")) {
                    holder.di_date_time.setTextColor(R.color.edited_color);
                    holder.di_ordering_md.setTextColor(R.color.edited_color);
                    holder.di_procedure.setTextColor(R.color.edited_color);
                    holder.di_urgent_flag.setTextColor(R.color.edited_color);
                    holder.di_department.setTextColor(R.color.edited_color);
                    holder.di_exam_date_time.setTextColor(R.color.edited_color);
                    holder.di_comment.setTextColor(R.color.edited_color);
                }
            }

            else if(listProviderForDI.getOrder_status().equals("Stopped")) {
                holder.di_date_time.setBackgroundResource(R.color.stopped_color);
                holder.di_ordering_md.setBackgroundResource(R.color.stopped_color);
                holder.di_procedure.setBackgroundResource(R.color.stopped_color);
                holder.di_urgent_flag.setBackgroundResource(R.color.stopped_color);
                holder.di_department.setBackgroundResource(R.color.stopped_color);
                holder.di_exam_date_time.setBackgroundResource(R.color.stopped_color);
                holder.di_comment.setBackgroundResource(R.color.stopped_color);
            }

            else if(listProviderForDI.getOrder_status().equals("Held")) {
                holder.di_date_time.setBackgroundResource(R.color.held_color);
                holder.di_ordering_md.setBackgroundResource(R.color.held_color);
                holder.di_procedure.setBackgroundResource(R.color.held_color);
                holder.di_urgent_flag.setBackgroundResource(R.color.held_color);
                holder.di_department.setBackgroundResource(R.color.held_color);
                holder.di_exam_date_time.setBackgroundResource(R.color.held_color);
                holder.di_comment.setBackgroundResource(R.color.held_color);
            }

            else if(listProviderForDI.getOrder_status().equals("Reactivated")) {
                //holder.container.setBackgroundColor(Color.parseColor("Red"));
            }
            else if(listProviderForDI.getOrder_status().equals("Scheduled")) {
                holder.di_date_time.setBackgroundResource(R.color.scheduled_color);
                holder.di_ordering_md.setBackgroundResource(R.color.scheduled_color);
                holder.di_procedure.setBackgroundResource(R.color.scheduled_color);
                holder.di_urgent_flag.setBackgroundResource(R.color.scheduled_color);
                holder.di_department.setBackgroundResource(R.color.scheduled_color);
                holder.di_exam_date_time.setBackgroundResource(R.color.scheduled_color);
                holder.di_comment.setBackgroundResource(R.color.scheduled_color);
            }

            else if(listProviderForDI.getOrder_status().equals("Incomplete")) {
                //holder.container.setBackgroundColor(Color.parseColor("#4adkfl"));
            }
        }

        // set the listener class
//        holder.container.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
//                listProviderForDI.setSelected(!listProviderForDI.isSelected());
//                //holder.di_id.setBackgroundColor(listProviderForDI.isSelected() ? Color.CYAN : Color.WHITE);
//                if(listProviderForDI.isSelected())
//                    holder.checkBox.setChecked(true);
//                else
//                    holder.checkBox.setChecked(false);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        int Arr_size = 0;
        if(orderType.equals(DI_Order))
            Arr_size = di_arraylist.size();
        return Arr_size;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        //variables for di list
        TextView di_id,di_date_time, di_ordering_md, di_procedure, di_urgent_flag, di_department,di_exam_date_time, di_comment;
        public View container;
        public RecyclerViewHolder (View view){
            super(view);
            if(orderType.equals(DI_Order)) {
                di_date_time = view.findViewById(R.id.di_date_time);
                di_ordering_md = view.findViewById(R.id.di_ordering_md);
                di_procedure = view.findViewById(R.id.di_procedure);
                di_urgent_flag = view.findViewById(R.id.di_urgent_flag);
                di_department = view.findViewById(R.id.di_department);
                di_exam_date_time = view.findViewById(R.id.di_exam_date_time);
                di_comment = view.findViewById(R.id.di_Comment);
            }
            container = view;
        }
    }

    // get the selected items
//    public ArrayList getSelectedItem() {
//        selectedItemList = new ArrayList<ListProviderForDI>();
//        Log.e("array size", String.valueOf(di_arraylist.size()));
//        for (int i = 0; i < di_arraylist.size(); i++) {
//            ListProviderForDI itemlist = di_arraylist.get(i);
//            if (itemlist.isSelected()) {
//                selectedItemList.add(itemlist);
//                Log.e("name ", itemlist.getProcedures());
//            }
//        }
//        Log.e("selected item : >> ", String.valueOf(selectedItemList.size()));
//        return selectedItemList;
//
//    }

}
