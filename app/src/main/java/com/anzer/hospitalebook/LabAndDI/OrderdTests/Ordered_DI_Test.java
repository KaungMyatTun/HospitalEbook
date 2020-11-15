package com.anzer.hospitalebook.LabAndDI.OrderdTests;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.anzer.hospitalebook.LabAndDI.Adapter.DIOrderedTestRecycleAdapter;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.listeners.OnItemClickListener;

import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Ordered_DI_Test extends Fragment implements DatePickerDialog.OnDateSetListener {
    View view;
    public static RecyclerView recyclerView;
    public static DIOrderedTestRecycleAdapter adapter;
    public static RecyclerView.LayoutManager layoutManager;
    String date;
    int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_ordered_di_test_tab, container, false);
        recyclerView = view.findViewById(R.id.recyclerViewDIOrderedTab);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new DIOrderedTestRecycleAdapter(MainActivity.DI_OrderedTest);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (view.getId() == R.id.ordered_di_target_date) {
                    Log.e("Click", "Position : " + position + "in target field");
                    showDatePickerDialog(position);
                }
                if (view.getId() == R.id.ordered_checkbox) {
                    if (MainActivity.DI_OrderedTest.get(position).di_item_urgent_flag)
                        MainActivity.DI_OrderedTest.get(position).di_item_urgent_flag = false;
                    else
                        MainActivity.DI_OrderedTest.get(position).di_item_urgent_flag = true;
                }

                buildAdapter();
            }
        });
        recyclerView.setAdapter(adapter);
        return view;
    }

    // show the calendar dialog
    private void showDatePickerDialog(int position) {
        pos = position;
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.MyDatePickerDialogTheme, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = (month + 1) + "/" + dayOfMonth + "/" + year;
        MainActivity.DI_OrderedTest.get(pos).di_item_target_date = date;
        buildAdapter();
        date = "";
    }


    // reload the adapter
    public void buildAdapter() {
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}