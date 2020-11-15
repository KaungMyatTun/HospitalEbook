package com.anzer.hospitalebook.LabAndDI.AvailableTests;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import com.anzer.hospitalebook.LabAndDI.Adapter.DIExpandableListViewAdapter;
import com.anzer.hospitalebook.LabAndDI.OrderdTests.Ordered_DI_Test;
import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.vo.DIGroupModel;
import com.anzer.hospitalebook.vo.DIGroupTitleModel;
import com.anzer.hospitalebook.vo.DIProceduresModel;
import com.anzer.hospitalebook.vo.DISubItemModel;
import com.anzer.hospitalebook.vo.DI_OrderedTestsModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DI_Test extends Fragment {
    ProgressBar mProgressBar;
    public static ArrayList<DIProceduresModel> _DIProcedureList;
    public static ArrayList<DIGroupModel> _DIGroupList;
    public static ArrayList<DIGroupTitleModel> _DIGroupWithAssociatedChildItem;
    public static ArrayList<DIGroupTitleModel> _copyDIGroupWithAssociatedChildItem;
    public static ArrayList<DIGroupTitleModel> _search_result_DIGroupWithAssociatedChildItem;
    public static ArrayList<String> _already;
    ExpandableListView DIExpandableListView;
    DIExpandableListViewAdapter adapter;
    SearchView txt_searchView;
    BottomSheetBehavior bottomSheetBehavior;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private String date;
    public static ArrayList<DI_OrderedTestsModel> orderedItem;
    boolean alreadySelected;
    ArrayList<String> alreadySelectedItemCode;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _DIProcedureList = new ArrayList<>();
        _DIGroupList = new ArrayList<>();
        _DIGroupWithAssociatedChildItem = new ArrayList<>();
        orderedItem = new ArrayList<>();
        alreadySelectedItemCode = new ArrayList<>();

        if(MainActivity.DI_OrderedTest.size() > 0){
            for(int index = 0; index < MainActivity.DI_OrderedTest.size(); index++){
                alreadySelectedItemCode.add(MainActivity.DI_OrderedTest.get(index).di_item_code);
            }
        }

        // declare date format
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_available_di_test, container, false);
        mProgressBar = view.findViewById(R.id.progress_bar);
        txt_searchView = view.findViewById(R.id.txt_search);

        View view2 = inflater.inflate(R.layout.fragment_lab_n_di, container, false);
        LinearLayout linearLayout = view2.findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(linearLayout);

        // call get all di group function
        GetAllDIGroup();

        for(int i = 0; i < MainActivity.DI_OrderedTest.size(); i++){
            Log.e("Code", MainActivity.DI_OrderedTest.get(i).di_item_code);
            Log.e("Name", MainActivity.DI_OrderedTest.get(i).di_item_name);
        }

        DIExpandableListView = view.findViewById(R.id.di_expandableListView);

        // search function
        txt_searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() != 0) {
                    _copyDIGroupWithAssociatedChildItem = new ArrayList<>();
                    _search_result_DIGroupWithAssociatedChildItem = new ArrayList<>();
                    _copyDIGroupWithAssociatedChildItem.clear();
                    _search_result_DIGroupWithAssociatedChildItem.clear();
                    _copyDIGroupWithAssociatedChildItem = _DIGroupWithAssociatedChildItem;

                    // search process
                    DIGroupTitleModel search_diGroupTitleModel = new DIGroupTitleModel();
                    search_diGroupTitleModel.groupTitleCode = String.valueOf(-1);
                    search_diGroupTitleModel.groupTitleName = "Search Result";
                    for (int groupPos = 0; groupPos < _copyDIGroupWithAssociatedChildItem.size(); groupPos++) {
                        Log.e("Group Position", String.valueOf(groupPos));
                        if (_copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.size() != 0) {
                            for (int childPos = 0; childPos < _copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.size(); childPos++) {
                                DISubItemModel search_result_subItemModel = new DISubItemModel();
                                if (_copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.get(childPos).itemName.toLowerCase().contains(s)) {
                                    search_result_subItemModel.itemCode = _copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.get(childPos).itemCode;
                                    search_result_subItemModel.itemName = _copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.get(childPos).itemName;
                                    search_result_subItemModel.itemParentGroupCode = _copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.get(childPos).itemParentGroupCode;
                                    search_result_subItemModel.itemParentGroupName = _copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.get(childPos).itemParentGroupName;
                                    search_result_subItemModel.selected = _copyDIGroupWithAssociatedChildItem.get(groupPos).sub_item.get(childPos).selected;
                                    search_diGroupTitleModel.sub_item.add(search_result_subItemModel);
                                }
                            }
                        }
                    }
                    _search_result_DIGroupWithAssociatedChildItem.add(search_diGroupTitleModel);
                    mProgressBar.setVisibility(View.VISIBLE);
                    adapter = new DIExpandableListViewAdapter(getActivity(), DIExpandableListView, _search_result_DIGroupWithAssociatedChildItem);
                    DIExpandableListView.setAdapter(adapter);
                    DIExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
//                            Log.e("Group position :: " + groupPosition, " &&   Child position :: " + childPosition);
                            if (_search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected) {
                                _search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected = false;
                                for (int groupId = 0; groupId < _DIGroupWithAssociatedChildItem.size(); groupId++) {
                                    for (int childId = 0; childId < _DIGroupWithAssociatedChildItem.get(groupId).sub_item.size(); childId++) {
                                        if (_DIGroupWithAssociatedChildItem.get(groupId).sub_item.get(childId).itemCode.equals(
                                                _search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode)) {
                                            _DIGroupWithAssociatedChildItem.get(groupId).sub_item.get(childId).selected = false;
                                        }
                                    }
                                }
                                for (int index = 0; index < MainActivity.DI_OrderedTest.size(); index++) {
                                    if (MainActivity.DI_OrderedTest.get(index).di_item_code.equals(_search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode)) {
                                        MainActivity.DI_OrderedTest.remove(index);
                                        reloadView();
                                    }
                                }
                            } else {
                                _search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected = true;
                                // add item on ordered list (searched result)
                                DI_OrderedTestsModel orderedTestsModel = new DI_OrderedTestsModel();
                                orderedTestsModel.di_item_code = _search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode;
                                orderedTestsModel.di_item_name = _search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemName;
                                // get current date
                                calendar = Calendar.getInstance();
                                orderedTestsModel.di_item_target_date = dateFormat.format(calendar.getTime());
                                orderedTestsModel.di_item_urgent_flag = false;
                                MainActivity.DI_OrderedTest.add(orderedTestsModel);
                                reloadView();
                                for (int groupId = 0; groupId < _DIGroupWithAssociatedChildItem.size(); groupId++) {
                                    for (int childId = 0; childId < _DIGroupWithAssociatedChildItem.get(groupId).sub_item.size(); childId++) {
                                        if (_DIGroupWithAssociatedChildItem.get(groupId).sub_item.get(childId).itemCode.equals(
                                                _search_result_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode)) {
                                            _DIGroupWithAssociatedChildItem.get(groupId).sub_item.get(childId).selected = true;
                                        }
                                    }
                                }
                            }
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                    });
                    mProgressBar.setVisibility(View.INVISIBLE);
                    DIExpandableListView.expandGroup(0);
                } else if (s.length() == 0) {
                    // attach adapter with original list
                    mProgressBar.setVisibility(View.VISIBLE);
                    adapter = new DIExpandableListViewAdapter(getActivity(), DIExpandableListView, _DIGroupWithAssociatedChildItem);
                    DIExpandableListView.setAdapter(adapter);
                    DIExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                        @Override
                        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                            Log.e("Group position :: " + groupPosition, " &&   Child position :: " + childPosition);
                            if (_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected) {
                                _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected = false;
                                for (int index = 0; index < MainActivity.DI_OrderedTest.size(); index++) {
                                    if (MainActivity.DI_OrderedTest.get(index).di_item_code.equals(_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode)) {
                                        MainActivity.DI_OrderedTest.remove(index);
                                        reloadView();
                                    }
                                }
//                                Log.e("Ordered DI Tests ...", String.valueOf(orderedItem.size()));
                            } else {
                                _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected = true;
                                DI_OrderedTestsModel orderedTestsModel = new DI_OrderedTestsModel();
                                orderedTestsModel.di_item_code = _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode;
                                orderedTestsModel.di_item_name = _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemName;
                                // get current date
                                calendar = Calendar.getInstance();
                                orderedTestsModel.di_item_target_date = dateFormat.format(calendar.getTime());
                                orderedTestsModel.di_item_urgent_flag = false;
                                MainActivity.DI_OrderedTest.add(orderedTestsModel);
                                reloadView();
                            }
                            adapter.notifyDataSetChanged();
                            return true;
                        }
                    });
                    mProgressBar.setVisibility(View.INVISIBLE);
                    for (int groupPos = 0; groupPos < adapter.getGroupCount(); groupPos++) {
                        DIExpandableListView.collapseGroup(groupPos);
                    }
                }
                return false;
            }
        });
        return view;
    }

    // get all di group name
    public void GetAllDIGroup() {
        mProgressBar.setVisibility(View.VISIBLE);

        // get all di group
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<DIGroupModel>> call = apiInterface.GetAllDIGroup();
        call.enqueue(new Callback<ArrayList<DIGroupModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DIGroupModel>> call, Response<ArrayList<DIGroupModel>> response) {
                _DIGroupList.clear();
                _DIGroupList = response.body();
                mProgressBar.setVisibility(View.INVISIBLE);
                GetAllDIProcedures();
            }

            @Override
            public void onFailure(Call<ArrayList<DIGroupModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // get all di procedure
    public void GetAllDIProcedures() {
        mProgressBar.setVisibility(View.VISIBLE);
        // get all di group
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<DIProceduresModel>> call = apiInterface.GetAllDIProcedures();
        call.enqueue(new Callback<ArrayList<DIProceduresModel>>() {
            @Override
            public void onResponse(Call<ArrayList<DIProceduresModel>> call, Response<ArrayList<DIProceduresModel>> response) {
                _DIProcedureList.clear();
                _DIProcedureList = response.body();

                mProgressBar.setVisibility(View.INVISIBLE);
                MatchDIGroupAndChildItem();
            }

            @Override
            public void onFailure(Call<ArrayList<DIProceduresModel>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    // check item is already Selected or not
    private boolean checkItem(String item){
        boolean alreadySelected = false;
        if(alreadySelectedItemCode.contains(item)){
            alreadySelected = true;
        }else{
            alreadySelected = false;
        }
        return alreadySelected;
    }

    // match DI group and child item
    public void MatchDIGroupAndChildItem() {
        mProgressBar.setVisibility(View.VISIBLE);
        _DIGroupWithAssociatedChildItem.clear();
        for (int groupIndex = 0; groupIndex < _DIGroupList.size(); groupIndex++) {
            DIGroupTitleModel diGroupTitleModel = new DIGroupTitleModel();
            diGroupTitleModel.groupTitleCode = String.valueOf(_DIGroupList.get(groupIndex).getGroupId());
            diGroupTitleModel.groupTitleName = _DIGroupList.get(groupIndex).getGroupCaption();
            for (int childItemIndex = 0; childItemIndex < _DIProcedureList.size(); childItemIndex++) {
                DISubItemModel subItemModel = new DISubItemModel();
                if (_DIGroupList.get(groupIndex).getGroupId() == _DIProcedureList.get(childItemIndex).getPrcGroupId()) {
                    subItemModel.itemCode = _DIProcedureList.get(childItemIndex).getProcedureCode();
                    subItemModel.itemName = _DIProcedureList.get(childItemIndex).getProcedureDesc();
                    subItemModel.itemParentGroupCode = String.valueOf(_DIProcedureList.get(childItemIndex).getPrcGroupId());
                    subItemModel.itemParentGroupName = _DIProcedureList.get(childItemIndex).getPrcGroupDesc();
                    subItemModel.selected = false;
                    diGroupTitleModel.sub_item.add(subItemModel);
                }
            }
            _DIGroupWithAssociatedChildItem.add(diGroupTitleModel);
        }

        for(int groupIndex = 0; groupIndex < _DIGroupWithAssociatedChildItem.size(); groupIndex++){
            for(int childIndex = 0; childIndex < _DIGroupWithAssociatedChildItem.get(groupIndex).sub_item.size(); childIndex++){
                if(checkItem(_DIGroupWithAssociatedChildItem.get(groupIndex).sub_item.get(childIndex).itemCode)) {
                    _DIGroupWithAssociatedChildItem.get(groupIndex).sub_item.get(childIndex).selected = true;
                    Log.e("already selected item", _DIGroupWithAssociatedChildItem.get(groupIndex).sub_item.get(childIndex).itemName);
                }
            }
        }

        // set data to adapter
        adapter = new DIExpandableListViewAdapter(this.getActivity(), DIExpandableListView, _DIGroupWithAssociatedChildItem);
        DIExpandableListView.setAdapter(adapter);
        DIExpandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Log.e("Group position :: " + groupPosition, " &&   Child position :: " + childPosition );
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                if (_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected) {
                    Log.e("Do this class", "done");
                    _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected = false;
                    for (int index = 0; index < MainActivity.DI_OrderedTest.size(); index++) {
                        if (MainActivity.DI_OrderedTest.get(index).di_item_code.equals(_DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode)) {
                            MainActivity.DI_OrderedTest.remove(index);
                            reloadView();
                        }
                    }
                } else {
                    _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).selected = true;
                    DI_OrderedTestsModel orderedTestsModel = new DI_OrderedTestsModel();
                    orderedTestsModel.di_item_code = _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemCode;
                    orderedTestsModel.di_item_name = _DIGroupWithAssociatedChildItem.get(groupPosition).sub_item.get(childPosition).itemName;
                    // get current date
                    calendar = Calendar.getInstance();
                    orderedTestsModel.di_item_target_date = dateFormat.format(calendar.getTime());
                    orderedTestsModel.di_item_urgent_flag = false;
                    MainActivity.DI_OrderedTest.add(orderedTestsModel);
                    reloadView();
                }
                adapter.notifyDataSetChanged();
                return true;
            }
        });
        mProgressBar.setVisibility(View.INVISIBLE);

        DIExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int position, long l) {
                view = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.di_group_layout, null);
                parent.smoothScrollToPosition(position);
                if (parent.isGroupExpanded(position)) {
                    ImageView gp_icon_imageView = view.findViewById(R.id.di_group_prefix_icon);
                    gp_icon_imageView.setImageDrawable(getResources().getDrawable(R.drawable.plus_icon));
                } else {
                    ImageView gp_icon_imageView = view.findViewById(R.id.di_group_prefix_icon);
                    gp_icon_imageView.setImageDrawable(getResources().getDrawable(R.drawable.minus_icon));
                }
                return false;
            }
        });
    }

    // reload the review
    public void reloadView() {
        Ordered_DI_Test ordered_di_test = new Ordered_DI_Test();
        ordered_di_test.buildAdapter();
    }
}