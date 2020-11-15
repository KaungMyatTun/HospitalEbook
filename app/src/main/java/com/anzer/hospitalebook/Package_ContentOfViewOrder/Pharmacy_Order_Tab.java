package com.anzer.hospitalebook.Package_ContentOfViewOrder;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.anzer.hospitalebook.Doctor_Dashboard;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.Objects.ListProviderForPharmacy;
import com.anzer.hospitalebook.Package_SketchFragment.SketchFragment;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.CustomSpinnerAdapterForVisitSpinner;
import com.anzer.hospitalebook.models.adapters.RecyclerAdapter;
import com.anzer.hospitalebook.vo.PHItem;
import com.anzer.hospitalebook.vo.VisitItem;
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

public class Pharmacy_Order_Tab extends Fragment implements AdapterView.OnItemSelectedListener {
    CircleProgressBar progressBar;
    Spinner visit_filter;
    public static ArrayList<String> visit_filter_spinner_Item;

    public static RecyclerView recyclerViewForPharmacyItem;
    public static RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    public static ArrayList<ListProviderForPharmacy> PHOrderArraylist = new ArrayList<ListProviderForPharmacy>();
    public static ArrayList<ListProviderForPharmacy> PHSelectedItem = new ArrayList<ListProviderForPharmacy>();

    public Pharmacy_Order_Tab() {
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
        View view = inflater.inflate(R.layout.ph_order_tab, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setColorSchemeResources(android.R.color.holo_blue_bright);

        recyclerViewForPharmacyItem = (RecyclerView) view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerViewForPharmacyItem.setLayoutManager(layoutManager);
        recyclerViewForPharmacyItem.setHasFixedSize(true);

        // Retrieve visit spinner item from DB
        RetrieveVisitSpinnerItem();

        visit_filter = view.findViewById(R.id.visit_spinner_item);
        CustomSpinnerAdapterForVisitSpinner customSpinnerAdapter = new CustomSpinnerAdapterForVisitSpinner(view.getContext(), visit_filter_spinner_Item);
        visit_filter.setAdapter(customSpinnerAdapter);
        visit_filter.setOnItemSelectedListener(this);

        return view;
    }

    // spinner on item select listener
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        PHOrderArraylist.clear();
        startLoadData();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //****START****//
    //Retrieve visit spinner item from DB
    public void RetrieveVisitSpinnerItem() {
//        Connection con;
        visit_filter_spinner_Item = new ArrayList<>();
        visit_filter_spinner_Item.add(Doctor_Dashboard.Pt_CurrentReg);          // add current reg into visit spinner

        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<VisitItem>> call = apiInterface.GetVisitItemByCPI(Doctor_Dashboard.Pt_CPI);
        call.enqueue(new Callback<ArrayList<VisitItem>>() {
            @Override
            public void onResponse(Call<ArrayList<VisitItem>> call, Response<ArrayList<VisitItem>> response) {
                if (response.isSuccessful()) {
                    for (int index = 0; index < response.body().size(); index++) {
                        if (!(Doctor_Dashboard.Pt_CurrentReg).equals(response.body().get(index).getVisit())) {
                            visit_filter_spinner_Item.add(response.body().get(index).getVisit());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<VisitItem>> call, Throwable t) {
                Toast.makeText(getContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }
    //****End****//

    // load data with progress bar
    public void startLoadData() {
        progressBar.setVisibility(View.VISIBLE);
//        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new LoadDataForAllTask().execute(0);
    }

    // load with asynctask
    class LoadDataForAllTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
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
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    public void LoadDataForAllOrderTab() {
        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<PHItem>> call = apiInterface.GetPHOrderItem(SketchFragment.patient_cpi, MainActivity.insti_code, visit_filter.getSelectedItem().toString(), "PH");
        call.enqueue(new Callback<ArrayList<PHItem>>() {
            @Override
            public void onResponse(Call<ArrayList<PHItem>> call, Response<ArrayList<PHItem>> response) {
                if (response.isSuccessful()) {
                    for (int index = 0; index < response.body().size(); index++) {
                        // for blank duration patients
                        String ph_duration;
                        if(response.body().get(index).getDuration() == null){
                            ph_duration = "";
                        }else{
                            ph_duration = response.body().get(index).getDuration().toString();
                        }
                        ListProviderForPharmacy getPHData = new ListProviderForPharmacy(
                                String.valueOf(index + 1),
                                response.body().get(index).getStartDate(),
                                response.body().get(index).getDrug(),
                                response.body().get(index).getRoute(),
                                response.body().get(index).getDosage(),
                                response.body().get(index).getUnit().toString(),
                                response.body().get(index).getFrequency(),
                                ph_duration,
                                response.body().get(index).getOrderBy(),
                                response.body().get(index).getComment(),
                                response.body().get(index).getOrderStatus()
                        );
                        PHOrderArraylist.add(getPHData);
                    }
                    Log.e("result size", String.valueOf(PHOrderArraylist.size()));
                    adapter = new RecyclerAdapter(PHOrderArraylist, "PH", new RecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {

                        }
                    });
                    recyclerViewForPharmacyItem.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<PHItem>> call, Throwable t) {
                Log.e("error", "do this class");
                Log.e("error",call.toString());
                Toast.makeText(getContext(), call.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
