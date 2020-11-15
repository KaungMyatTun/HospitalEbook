package com.anzer.hospitalebook.models.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anzer.hospitalebook.Objects.ListProviderForPharmacy;
import com.anzer.hospitalebook.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by David on 9/13/2018.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>{

    private static final String PH_Order = "PH";

    static ArrayList<ListProviderForPharmacy> arraylist = new ArrayList<ListProviderForPharmacy>();

    static String orderType;
    public static ArrayList<ListProviderForPharmacy> selectItem = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;
    public static ArrayList<ListProviderForPharmacy> selectedItemList;

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }


    public RecyclerAdapter(ArrayList<ListProviderForPharmacy> arraylist, String orderType, OnItemClickListener onItemClickListener) {
        this.arraylist = arraylist;
        this.orderType = orderType;
        mOnItemClickListener = onItemClickListener;

    }

    public RecyclerAdapter() {
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        RecyclerViewHolder recyclerViewHolder;

        if (orderType.equals(PH_Order)) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ph_list_layout, parent, false);
            recyclerViewHolder = new RecyclerViewHolder(view);
            return recyclerViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewHolder holder, final int position) {

        final ListProviderForPharmacy listProviderForPharmacy = arraylist.get(position);

        if (orderType.equals(PH_Order)) {
            holder.start_date.setText(listProviderForPharmacy.getStartDate());
            holder.drug.setText(listProviderForPharmacy.getDrug());
            holder.route.setText(listProviderForPharmacy.getRoute());
            holder.dosage.setText(listProviderForPharmacy.getDossage());
            holder.units.setText(listProviderForPharmacy.getUnits());
            holder.frequency.setText(listProviderForPharmacy.getFrequency());
            holder.duration.setText(listProviderForPharmacy.getDuration());
            holder.orderby.setText(listProviderForPharmacy.getOrderedby());
            holder.comment.setText(listProviderForPharmacy.getComment());
            holder.order_status.setText(listProviderForPharmacy.getOrder_status());

            if(listProviderForPharmacy.getOrder_status().equals("Registered"))
            {
                //holder.container.setBackgroundColor(Color.parseColor("green"));
                holder.start_date.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.drug.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.route.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.dosage.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.units.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.frequency.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.duration.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.orderby.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.comment.setBackgroundColor(Color.parseColor("#90EE90"));
                holder.order_detail_panel.setBackgroundColor(Color.parseColor("#90EE90"));
            }

            else if(listProviderForPharmacy.getOrder_status().equals("Completed"))
            {
                //holder.container.setBackgroundColor(Color.parseColor("green"));
                holder.start_date.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.drug.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.route.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.dosage.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.units.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.frequency.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.duration.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.orderby.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.comment.setBackgroundColor(Color.parseColor("#90EE91"));
                holder.order_detail_panel.setBackgroundColor(Color.parseColor("#90EE91"));
            }

            else if(listProviderForPharmacy.getOrder_status().equals("Sent")) {
                holder.start_date.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.drug.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.route.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.dosage.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.units.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.frequency.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.duration.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.orderby.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.comment.setBackgroundColor(Color.parseColor("#ADD8E6"));
                holder.order_detail_panel.setBackgroundColor(Color.parseColor("#ADD8E6"));
            }

            else if(listProviderForPharmacy.getOrder_status().equals("Stopped")) {
                holder.start_date.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.drug.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.route.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.dosage.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.units.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.frequency.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.duration.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.orderby.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.comment.setBackgroundColor(Color.parseColor("#ffb6c1"));
                holder.order_detail_panel.setBackgroundColor(Color.parseColor("#ffb6c1"));
            }

            else if(listProviderForPharmacy.getOrder_status().equals("Held")) {
                //holder.container.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.start_date.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.drug.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.route.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.dosage.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.units.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.frequency.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.duration.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.orderby.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.comment.setBackgroundColor(Color.parseColor("#FFFFE0"));
                holder.order_detail_panel.setBackgroundColor(Color.parseColor("#FFFFE0"));
            }

            else if(listProviderForPharmacy.getOrder_status().equals("Reactivated")) {
                //holder.container.setBackgroundColor(Color.parseColor("Red"));
            }
            else if(listProviderForPharmacy.getOrder_status().equals("Scheduled")) {
                //holder.container.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.start_date.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.drug.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.route.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.dosage.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.units.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.frequency.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.duration.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.orderby.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.comment.setBackgroundColor(Color.parseColor("#8FBC8B"));
                holder.order_detail_panel.setBackgroundColor(Color.parseColor("#8FBC8B"));
            }
            else if (listProviderForPharmacy.getOrder_status().equals("Edited")) {
                //holder.container.setBackgroundColor(Color.parseColor("#fffaaa"));
            }
            else if(listProviderForPharmacy.getOrder_status().equals("Incomplete")) {
                //holder.container.setBackgroundColor(Color.parseColor("#4adkfl"));
            }
        }

        // set up the listener class
//        holder.container.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mOnItemClickListener.onItemClick(v, position);
//                listProviderForPharmacy.setSelected(!listProviderForPharmacy.isSelected());
//                if(listProviderForPharmacy.isSelected())
//                    holder.checkBox.setChecked(true);
//                else
//                    holder.checkBox.setChecked(false);
//                //holder.id.setBackgroundColor(listProviderForPharmacy.isSelected() ? Color.CYAN : Color.WHITE);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        int Arr_size = 0;
        if (orderType.equals(PH_Order))
            Arr_size = arraylist.size();

        return Arr_size;
    }


    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView id, start_date, drug, route, dosage, units, frequency, duration, orderby, comment, order_status;
        LinearLayout order_detail_panel;
        CheckBox checkBox;
        public View container;

        public RecyclerViewHolder(View view) {
            super(view);
            if (orderType.equals(PH_Order)) {
                start_date = view.findViewById(R.id.startDate);
                drug = view.findViewById(R.id.Drug);
                route = view.findViewById(R.id.Route);
                dosage = view.findViewById(R.id.Dosage);
                units = view.findViewById(R.id.Units);
                frequency = view.findViewById(R.id.Frequency);
                duration = view.findViewById(R.id.Duration);
                orderby = view.findViewById(R.id.Orderby);
                comment = view.findViewById(R.id.Comment);
                order_detail_panel = view.findViewById(R.id.order_detail_panel);
                order_status = view.findViewById(R.id.order_status);
            }

            container = view;
        }
    }

    // get selected items
//    public ArrayList getSelectedItem() {
//        selectedItemList = new ArrayList<ListProviderForPharmacy>();
//        Log.e("array size", String.valueOf(arraylist.size()));
//        for (int i = 0; i < arraylist.size(); i++) {
//            ListProviderForPharmacy itemlist = arraylist.get(i);
//            if (itemlist.isSelected()) {
//                selectedItemList.add(itemlist);
//                Log.e("name ", itemlist.getDrug());
//            }
//        }
//        Log.e("selected item : >> ", String.valueOf(selectedItemList.size()));
//        return selectedItemList;
//    }
}
