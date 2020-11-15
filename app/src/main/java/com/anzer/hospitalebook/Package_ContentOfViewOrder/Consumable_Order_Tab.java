package com.anzer.hospitalebook.Package_ContentOfViewOrder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Objects.ListProviderForConsumable;
import com.anzer.hospitalebook.Package_SketchFragment.SketchFragment;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.CustomSpinnerAdapterForVisitSpinner;
import com.anzer.hospitalebook.models.adapters.RecyclerAdapter;
import com.anzer.hospitalebook.models.adapters.RecyclerAdapterForConsumable;
import com.anzer.hospitalebook.vo.ConsumableItem;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 8/21/2018.
 */

public class Consumable_Order_Tab extends Fragment implements AdapterView.OnItemSelectedListener{
    Spinner visit_filter;
    CircleProgressBar progressBar;
    public static RecyclerView recyclerViewForConsumableItem;
    public static RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static ArrayList<ListProviderForConsumable> ConsumableOrderArraylist = new ArrayList<ListProviderForConsumable>();

    public Consumable_Order_Tab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.consumable_order_tab, container, false);

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setColorSchemeResources(android.R.color.holo_blue_bright);

        recyclerViewForConsumableItem = (RecyclerView) view.findViewById(R.id.recyclerViewForConsumableItem);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForConsumableItem.setLayoutManager(layoutManager);
        recyclerViewForConsumableItem.setHasFixedSize(true);

        visit_filter = view.findViewById(R.id.visit_spinner_item);
        CustomSpinnerAdapterForVisitSpinner customSpinnerAdapter = new CustomSpinnerAdapterForVisitSpinner(view.getContext(), Pharmacy_Order_Tab.visit_filter_spinner_Item);
        visit_filter.setAdapter(customSpinnerAdapter);

        visit_filter.setOnItemSelectedListener(this);
        return view;
    }

    // spinner on item select listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ConsumableOrderArraylist.clear();
        startLoadData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {}

    // load data with progress bar
    public void startLoadData(){
        progressBar.setVisibility(View.VISIBLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new LoadDataForAllTask().execute(0);
    }

    // load with asynctask
    class LoadDataForAllTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try{
                Thread.sleep(500);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.INVISIBLE);
            LoadDataForAllOrderTab();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {}
    }

    public void LoadDataForAllOrderTab() {
        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<ConsumableItem>> call = apiInterface.GetConsumableOrderItem(SketchFragment.patient_cpi, MainActivity.insti_code, visit_filter.getSelectedItem().toString(), "IN");
        call.enqueue(new Callback<ArrayList<ConsumableItem>>() {
            @Override
            public void onResponse(Call<ArrayList<ConsumableItem>> call, Response<ArrayList<ConsumableItem>> response) {
                if(response.isSuccessful()){
                    for(int index = 0; index < response.body().size(); index++){
                        ListProviderForConsumable getConsumableData = new ListProviderForConsumable(
                                String.valueOf(index+1),
                                response.body().get(index).getMaterial(),
                                response.body().get(index).getOrderedBy(),
                                response.body().get(index).getOrderDate(),
                                response.body().get(index).getUnits(),
                                response.body().get(index).getComment(),
                                response.body().get(index).getOrderStatus()
                        );
                        ConsumableOrderArraylist.add(getConsumableData);
                    }
                    adapter = new RecyclerAdapterForConsumable(ConsumableOrderArraylist, "Consumable", new RecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }
                    });

                    recyclerViewForConsumableItem.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ConsumableItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });

//        Connection con;
//        //      order items for consumables       //
//        try {
//            con = DBConnection.getConnectionClass(MainActivity.DB_username, MainActivity.DB_password, MainActivity.DB_NAME, MainActivity.Server_ip);
//            CallableStatement stmt = con.prepareCall("{call dbo.usp_app_OE_GetOrderItems(?,?,?,?)}");
//            stmt.setString(1, SketchFragment.patient_cpi);
//            stmt.setString(2, MainActivity.insti_code);
//            stmt.setString(3, visit_filter.getSelectedItem().toString());
//            stmt.setString(4, "IN");
//            ResultSet rs = stmt.executeQuery();
//            int count = 0;
//
//            while (rs.next()) {
//                ListProviderForConsumable getConsumableData = new ListProviderForConsumable(String.valueOf(count + 1), rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
//                ConsumableOrderArraylist.add(getConsumableData);
//                count++;
//            }
//        } catch (SQLException se) {
//            Log.e("error here 1", se.getMessage());
//        } catch (Exception ex) {
//            Log.e("error here  3", ex.getMessage());
//        }
//
//        adapter = new RecyclerAdapterForConsumable(ConsumableOrderArraylist, "Consumable", new RecyclerAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        });
//
//        recyclerViewForConsumableItem.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
