package com.anzer.hospitalebook.models.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anzer.hospitalebook.Objects.Visit_History_Data;
import com.anzer.hospitalebook.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by David on 9/13/2018.
 */

public class RecyclerAdapterForVisitHistory extends RecyclerView.Adapter<RecyclerAdapterForVisitHistory.RecyclerViewHolder> {
    static ArrayList<Visit_History_Data> arraylist = new ArrayList<Visit_History_Data>();
    public static ArrayList<Visit_History_Data> selectItem = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;
    public static ArrayList<Visit_History_Data> selectedItemList;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public RecyclerAdapterForVisitHistory(ArrayList<Visit_History_Data> arraylist, OnItemClickListener onItemClickListener) {
        this.arraylist = arraylist;
        mOnItemClickListener = onItemClickListener;
    }

    public RecyclerAdapterForVisitHistory() {
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerViewHolder recyclerViewHolder;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.visit_history_item_layout, parent, false);
        recyclerViewHolder = new RecyclerViewHolder(view);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        final Visit_History_Data visitHistorydata = arraylist.get(position);

        holder.regNum.setText(visitHistorydata.getRegNum());
        holder.attDoc.setText(visitHistorydata.getRegAttDoc());
        holder.regDate.setText(visitHistorydata.getRegDate());
        holder.queueNum.setText(visitHistorydata.getRegQueueNum());
        holder.dischargeDate.setText(visitHistorydata.getRegDispDate());
        holder.regCancelCmt.setText(visitHistorydata.getRegCancelComment());

        if(!(holder.regCancelCmt.getText().toString().equals(""))){
            holder.regNum.setTextColor(Color.RED);
            holder.attDoc.setTextColor(Color.RED);
            holder.regDate.setTextColor(Color.RED);
            holder.queueNum.setTextColor(Color.RED);
            holder.dischargeDate.setTextColor(Color.RED);
            holder.regCancelCmt.setTextColor(Color.RED);
        }

        // set the listener class
//        holder.container.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
//                listProviderForPharmacy.setSelected(!listProviderForPharmacy.isSelected());
//                if(listProviderForPharmacy.isSelected())
//                    holder.checkBox.setChecked(true);
//                else
//                    holder.checkBox.setChecked(false);
                //holder.id.setBackgroundColor(listProviderForPharmacy.isSelected() ? Color.CYAN : Color.WHITE);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        int Arr_size = 0;
        Arr_size = arraylist.size();

        return Arr_size;
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView id, regNum, regDate, attDoc, dischargeDate, regCancelCmt, queueNum;
        public View container;
        public RecyclerViewHolder(View view) {
            super(view);
            regNum = view.findViewById(R.id.txt_reg);
            regDate = view.findViewById(R.id.txt_reg_date);
            attDoc = view.findViewById(R.id.txt_adDoctor);
            dischargeDate = view.findViewById(R.id.txt_discharge_date);
            regCancelCmt = view.findViewById(R.id.txt_reg_cancelCmt);
            queueNum = view.findViewById(R.id.txt_queue);
            container = view;
        }
    }

    // get the selected items
//    public ArrayList getSelectedItem() {
//        selectedItemList = new ArrayList<Visit_History_Data>();
//        Log.e("array size", String.valueOf(arraylist.size()));
//        for (int i = 0; i < arraylist.size(); i++) {
//            Visit_History_Data itemlist = arraylist.get(i);
//            if (itemlist.isSelected()) {
//                selectedItemList.add(itemlist);
//                Log.e("name ", itemlist.getDrug());
//            }
//        }
//        Log.e("selected item : >> ", String.valueOf(selectedItemList.size()));
//        return selectedItemList;
//    }
}
