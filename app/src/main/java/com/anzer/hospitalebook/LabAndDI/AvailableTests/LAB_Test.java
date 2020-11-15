package com.anzer.hospitalebook.LabAndDI.AvailableTests;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.anzer.hospitalebook.LabAndDI.Adapter.LabAvailableTestRecycleAdapter;
import com.anzer.hospitalebook.LabAndDI.OrderdTests.Ordered_LAB_Test;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.listeners.OnItemClickListener;
import com.anzer.hospitalebook.vo.LAB_OrderedTestsModel;
import com.anzer.hospitalebook.vo.LabJson;
import com.anzer.hospitalebook.vo.Lab_Available_Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LAB_Test extends Fragment {
    ProgressBar mProgressBar;
    ArrayList<LabJson> lab_tests;
    ArrayList<Lab_Available_Test> lab_available_test;
    ArrayList<Lab_Available_Test> copy_lab_available_test;
    ArrayList<Lab_Available_Test> search_result_lab_available_test;
    View view;
    public static RecyclerView recyclerView;
    public static LabAvailableTestRecycleAdapter adapter;
    public static RecyclerView.LayoutManager layoutManager;
    SearchView txt_searchView;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    ArrayList<String> alreadySelectedItemCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_available_lab_test, container, false);
        mProgressBar = view.findViewById(R.id.progress_bar);
        txt_searchView = view.findViewById(R.id.lab_txt_search);

        lab_tests = new ArrayList<>();
        lab_available_test = new ArrayList<Lab_Available_Test>();
        alreadySelectedItemCode = new ArrayList<>();

        if (MainActivity.LAB_OrderedTest.size() > 0) {
            for (int index = 0; index < MainActivity.LAB_OrderedTest.size(); index++) {
                alreadySelectedItemCode.add(MainActivity.LAB_OrderedTest.get(index).Lab_code);
            }
        }

        // declare date format
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        // get all lab test
        GetAllLabTest();

        // initiate the available lab test adapter
        recyclerView = view.findViewById(R.id.recyclerViewLabOrdered);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new LabAvailableTestRecycleAdapter(lab_available_test);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (lab_available_test.get(position).isSelected) {
                    lab_available_test.get(position).isSelected = false;
                    for (int index = 0; index < MainActivity.LAB_OrderedTest.size(); index++) {
                        if (MainActivity.LAB_OrderedTest.get(index).Lab_code.equals(lab_available_test.get(position).labCode)) {
                            MainActivity.LAB_OrderedTest.remove(index);
                            reloadView();
                        }
                    }
                } else {
                    lab_available_test.get(position).isSelected = true;

                    // add item to ordered list
                    LAB_OrderedTestsModel lab_orderedTestsModel = new LAB_OrderedTestsModel();
                    lab_orderedTestsModel.Lab_code = lab_available_test.get(position).labCode;
                    lab_orderedTestsModel.lab_test_name = lab_available_test.get(position).labTestName;
                    // get current date
                    calendar = Calendar.getInstance();
                    lab_orderedTestsModel.lab_test_target_date = dateFormat.format((calendar.getTime()));
                    lab_orderedTestsModel.urgent_flag = false;

                    MainActivity.LAB_OrderedTest.add(lab_orderedTestsModel);
                    reloadView();
                }
                adapter.notifyDataSetChanged();
            }
        });

        // for search function
        txt_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() != 0) {
                    copy_lab_available_test = new ArrayList<>();
                    search_result_lab_available_test = new ArrayList<>();
                    copy_lab_available_test.clear();
                    search_result_lab_available_test.clear();
                    copy_lab_available_test = lab_available_test;

                    for (int index = 0; index < copy_lab_available_test.size(); index++) {
                        if (copy_lab_available_test.get(index).labTestName.toLowerCase().contains(s)) {
                            search_result_lab_available_test.add(copy_lab_available_test.get(index));
                        }
                    }
                    Log.e("search result list size", String.valueOf(search_result_lab_available_test.size()));
                    adapter = new LabAvailableTestRecycleAdapter(search_result_lab_available_test);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (search_result_lab_available_test.get(position).isSelected) {
                                search_result_lab_available_test.get(position).isSelected = false;
                                for (int index = 0; index < MainActivity.LAB_OrderedTest.size(); index++) {
                                    if (MainActivity.LAB_OrderedTest.get(index).Lab_code.equals(search_result_lab_available_test.get(position).labCode)) {
                                        MainActivity.LAB_OrderedTest.remove(index);
                                        reloadView();
                                    }
                                }
                            } else {
                                search_result_lab_available_test.get(position).isSelected = true;
                                // add item to ordered list
                                LAB_OrderedTestsModel lab_orderedTestsModel = new LAB_OrderedTestsModel();
                                lab_orderedTestsModel.Lab_code = search_result_lab_available_test.get(position).labCode;
                                lab_orderedTestsModel.lab_test_name = search_result_lab_available_test.get(position).labTestName;
                                // get current date
                                calendar = Calendar.getInstance();
                                lab_orderedTestsModel.lab_test_target_date = dateFormat.format((calendar.getTime()));
                                lab_orderedTestsModel.urgent_flag = false;
                                MainActivity.LAB_OrderedTest.add(lab_orderedTestsModel);
                                reloadView();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                } else if (s.length() == 0) {
                    adapter = new LabAvailableTestRecycleAdapter(lab_available_test);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (lab_available_test.get(position).isSelected) {
                                lab_available_test.get(position).isSelected = false;
                                for (int index = 0; index < MainActivity.LAB_OrderedTest.size(); index++) {
                                    if (MainActivity.LAB_OrderedTest.get(index).Lab_code.equals(lab_available_test.get(position).labCode)) {
                                        MainActivity.LAB_OrderedTest.remove(index);
                                        reloadView();
                                    }
                                }
                            } else {
                                lab_available_test.get(position).isSelected = true;
                                // add item to ordered list
                                LAB_OrderedTestsModel lab_orderedTestsModel = new LAB_OrderedTestsModel();
                                lab_orderedTestsModel.Lab_code = lab_available_test.get(position).labCode;
                                lab_orderedTestsModel.lab_test_name = lab_available_test.get(position).labTestName;
                                // get current date
                                calendar = Calendar.getInstance();
                                lab_orderedTestsModel.lab_test_target_date = dateFormat.format((calendar.getTime()));
                                lab_orderedTestsModel.urgent_flag = false;

                                MainActivity.LAB_OrderedTest.add(lab_orderedTestsModel);
                                reloadView();
                            }
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                return false;
            }
        });

        return view;
    }

    // check item is already Selected or not
    private boolean checkItem(String item) {
        boolean alreadySelected = false;
        if (alreadySelectedItemCode.contains(item)) {
            alreadySelected = true;
        } else {
            alreadySelected = false;
        }
        return alreadySelected;
    }

    // get all lab test
    void GetAllLabTest() {
        mProgressBar.setVisibility(View.VISIBLE);
        // get all di group
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<LabJson>> call = apiInterface.GetLABTest();
        call.enqueue(new Callback<ArrayList<LabJson>>() {
            @Override
            public void onResponse(Call<ArrayList<LabJson>> call, Response<ArrayList<LabJson>> response) {
                lab_tests.clear();
                lab_tests = response.body();
                mProgressBar.setVisibility(View.INVISIBLE);
                initAvailableLabTests();
            }

            @Override
            public void onFailure(Call<ArrayList<LabJson>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // initialize the available lab tests
    void initAvailableLabTests() {
        for (int index = 0; index < lab_tests.size(); index++) {
            Lab_Available_Test lab_avl_tests = new Lab_Available_Test();
            lab_avl_tests.labCode = lab_tests.get(index).getLabCode();
            lab_avl_tests.labTestName = lab_tests.get(index).getLabTestName();
            if (checkItem(lab_avl_tests.labCode)) {
                lab_avl_tests.isSelected = true;
            } else {
                lab_avl_tests.isSelected = false;
            }

            Log.e("Name", lab_avl_tests.labTestName);

            lab_available_test.add(lab_avl_tests);
        }
        Log.e("Available lab test", lab_available_test.get(0).labTestName);
    }

    // reload the review
    public void reloadView() {
        Ordered_LAB_Test ordered_lab_test = new Ordered_LAB_Test();
        ordered_lab_test.buildAdapter();
    }
}