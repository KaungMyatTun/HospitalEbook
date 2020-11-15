package com.anzer.hospitalebook.NurseDashboard;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.anzer.hospitalebook.MainActivity;
import com.anzer.hospitalebook.NurseVitalSketch.NurseVitalSketch;
import com.anzer.hospitalebook.Package_Scanner.Scanner;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.vo.NurseDashboardData;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NurseDashboard extends AppCompatActivity implements View.OnClickListener {
    private TableLayout mTablelayout;
    static ArrayList<NurseDashboardData> copy_patient_data;
    static ArrayList<NurseDashboardData> copy_patient_data_for_search = new ArrayList<>();

    Handler mHandler;
    CircleProgressBar mProgressBar;
    public static SearchView searchView;
    public static String input_text = "";
    Dialog addVital_dialog;
    Button add_Prnote, dismiss_toAddPrnote;
    TextView txt_vital_confirmation;
    String currentDate;
    public static String search_view_query = "";
    Button btn_barcode_scanner;
    String nurseCode, nurseName;
    String str_RegAttDoc_Code;
    String str_RegAttDoc_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse_dashboard);
        mTablelayout = findViewById(R.id.patient_table);
        mTablelayout.setStretchAllColumns(true);
        nurseCode = MainActivity.nurse_code;
        nurseName = MainActivity.nurse_name;

        //progress bar
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setColorSchemeResources(android.R.color.holo_blue_bright);
        mProgressBar.setClickable(false);

        //search view
        searchView = findViewById(R.id.cpi_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                input_text = query;
                filter(input_text);
                Log.e("input value", input_text);
                startLoadDataForFilter();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                input_text = newText;
                filter(input_text);
                Log.e("input value", input_text);
                startLoadDataForFilter();
                return false;
            }
        });
        btn_barcode_scanner = findViewById(R.id.scan_barcode_btn);
        btn_barcode_scanner.setOnClickListener(this);

        int searchCloseButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView closeButton = this.searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_view_query = "";
                searchView.setQuery("", false);
            }
        });
        // initialize the array
        copy_patient_data = new ArrayList<>();

        this.mHandler = new Handler();
        m_Runnable.run();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_barcode_btn:
                search_view_query = "";
                Intent intent = new Intent(getApplicationContext(), Scanner.class);
                intent.putExtra("PageFrom", "ND");
                startActivityForResult(intent, 0);
                mHandler.removeCallbacks(m_Runnable);
                finish();

        }
    }

    // auto refresh doctor dashboard every 1 min
    private final Runnable m_Runnable = new Runnable() {
        public void run() {
            data_retrieve_from_DB();
            NurseDashboard.this.mHandler.postDelayed(m_Runnable, 50000);
        }
    };

    // load data with progress bar for cpi filter
    public void startLoadDataForFilter() {
        mProgressBar.setVisibility(View.VISIBLE);
        NurseDashboard.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new NurseDashboard.LoadDataTaskForFilter().execute(0);
    }

    // load with asynctask for cpi filter
    class LoadDataTaskForFilter extends AsyncTask<Integer, Integer, String> {
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
            LoadData();
            mProgressBar.setVisibility(View.INVISIBLE);
            NurseDashboard.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    // load data with progress bar    *****PATIENT DATA ON  DASHBOARD*****
    public void startLoadData() {
        NurseDashboard.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new NurseDashboard.LoadDataTask().execute(0);
    }

    // load with asynctask             *****PATIENT DATA ON DASHBOARD******
    class LoadDataTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... integers) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String s) {
            mProgressBar.setVisibility(View.INVISIBLE);
            NurseDashboard.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            LoadData();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }

    // get patient data from database
    public void data_retrieve_from_DB() {
        copy_patient_data.clear();

        Log.e("Get Data Using Retrofit", "Starting .....");
        mProgressBar.setVisibility(View.VISIBLE);

        // get current upload images date time
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        currentDate = df.format(Calendar.getInstance().getTime());
        // parse the String saveDateAndTime to a java.util.Date object
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy/MM/dd").parse(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // format the java.util.Date object to the desired format
        String formattedDate = new SimpleDateFormat("yyyy/MM/dd").format(date);

        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<NurseDashboardData>> call = apiInterface.getOPDWaitList("AMM0000", formattedDate);
        call.enqueue(new Callback<ArrayList<NurseDashboardData>>() {
            @Override
            public void onResponse(Call<ArrayList<NurseDashboardData>> call, Response<ArrayList<NurseDashboardData>> response) {
                ArrayList<NurseDashboardData> responseDataList = new ArrayList<>();
                String ptName, ptCPI, ptRegNum, ptRegAttDoc, ptRegAttDocName, ptQueueNum, ptRoom, ptRegDate;
                Integer ptEWS;
                Boolean ptImgIsSaved;

                for (int i = 0; i < response.body().size(); i++) {
                    ptName = response.body().get(i).getPatientName();
                    ptCPI = response.body().get(i).getPatientCPI();
                    ptRegNum = response.body().get(i).getRegNumber();
                    ptRegAttDoc = response.body().get(i).getRegAttDoctor();
                    ptQueueNum = response.body().get(i).getQueueNumber();
                    ptRegAttDocName = response.body().get(i).getRegAttDoctorName();
                    if (response.body().get(i).getPatientRoom() == null) {
                        ptRoom = "";
                    } else {
                        ptRoom = response.body().get(i).getPatientRoom();
                    }
                    ptRegDate = response.body().get(i).getRegDate();
                    if (response.body().get(i).getEWSScore() == null) {
                        ptEWS = -1;
                    } else {
                        ptEWS = response.body().get(i).getEWSScore();
                    }
                    if (response.body().get(i).getIsImageSaved() == null) {
                        ptImgIsSaved = false;
                    } else {
                        ptImgIsSaved = true;
                    }

                    NurseDashboardData tempData = new NurseDashboardData(ptName, ptCPI, ptRegNum, ptRegAttDoc, ptRegAttDocName, ptQueueNum, ptRoom, ptRegDate, ptEWS, ptImgIsSaved);
                    copy_patient_data.add(tempData);
                }
                copy_patient_data_for_search.clear();
                copy_patient_data_for_search.addAll(copy_patient_data);
                if (!(search_view_query.equals(""))) {
                    Log.e("search view ", "done this class");
                    searchView.setQuery(search_view_query, true);
                }
                startLoadData();
            }

            @Override
            public void onFailure(Call<ArrayList<NurseDashboardData>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
//                call.cancel();
//                LoadData();
                Toast.makeText(getApplicationContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }


    // load retrieved data to ui table
    public void LoadData() {
        int leftRowMargin = 0;
        int topRowMargin = 0;
        int rightRowMargin = 0;
        int bottomRowMargin = 0;
        int textSize = 0, smallTextSize = 0, mediumTextSize = 0;
        textSize = (int) getResources().getDimension(R.dimen.font_size_verysmall);
        smallTextSize = (int) getResources().getDimension(R.dimen.font_size_small);
        mediumTextSize = (int) getResources().getDimension(R.dimen.font_size_medium);

        TextView textSpacer = null;
        mTablelayout.removeAllViews();

        for (int i = -1; i < copy_patient_data.size(); i++) {

            // # column in table layout
            final TextView tv = new TextView(this);
            tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv.setGravity(Gravity.LEFT);
            tv.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv.setText("# ");
                tv.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv.setTextColor((Color.parseColor("#000000")));
            } else {
                tv.setText(String.valueOf(i + 1));
                tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // CPI column in table layout
            final TextView tv2 = new TextView(this);
            tv2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv2.setGravity(Gravity.LEFT);
            tv2.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv2.setText("CPI");
                tv2.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv2.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv2.setTextColor((Color.parseColor("#000000")));
                tv.setHorizontallyScrolling(false);
            } else {
                tv2.setText(copy_patient_data.get(i).getPatientCPI());
                tv2.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv2.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // Patient Name column in table layout
            final TextView tv3 = new TextView(this);
            tv3.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv3.setGravity(Gravity.LEFT);
            tv3.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv3.setText("Patient Name");
                tv3.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv3.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv3.setTextColor((Color.parseColor("#000000")));
                tv.setHorizontallyScrolling(false);
            } else {
                tv3.setText(copy_patient_data.get(i).getPatientName());
                tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv3.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // Visit Number column in table layout
            final TextView tv4 = new TextView(this);
            tv4.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv4.setGravity(Gravity.LEFT);
            tv4.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv4.setText("Visit #");
                tv4.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv4.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv4.setTextColor((Color.parseColor("#000000")));
            } else {
                tv4.setText(copy_patient_data.get(i).getRegNumber());
                tv4.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv4.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // Queue column in table layout
            final TextView tv5 = new TextView(this);
            tv5.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv5.setGravity(Gravity.LEFT);
            tv5.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv5.setText("Queue");
                tv5.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv5.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv5.setTextColor((Color.parseColor("#000000")));
            } else {
                tv5.setText(copy_patient_data.get(i).getQueueNumber());
                tv5.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv5.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // Room No column in table layout
            final TextView tv6 = new TextView(this);
            tv6.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv6.setGravity(Gravity.LEFT);
            tv6.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv6.setText("Room No");
                tv6.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv6.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv6.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv6.setTextColor((Color.parseColor("#000000")));
            } else {
                tv6.setText(copy_patient_data.get(i).getPatientRoom());
                tv6.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv6.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // Reg# data / time  column in table layout
            final TextView tv7 = new TextView(this);
            tv7.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv7.setGravity(Gravity.LEFT);
            tv7.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv7.setText("Reg # Date / Time ");
                tv7.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv7.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv7.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv7.setTextColor((Color.parseColor("#000000")));
            } else {
                tv7.setText(copy_patient_data.get(i).getRegDate());
                tv7.setBackgroundColor(Color.parseColor("#f8f8f8"));
                tv7.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            }

            // EWS  column in table layout
            final TextView tv8 = new TextView(this);
            tv8.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            tv8.setGravity(Gravity.CENTER);
            tv8.setPadding(5, 30, 0, 30);
            if (i == -1) {
                tv8.setText("NEWS ");
                tv8.setBackgroundColor((Color.parseColor("#8FBC8F")));
                tv8.setTextSize(TypedValue.COMPLEX_UNIT_PX, smallTextSize);
                tv8.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv8.setTextColor((Color.parseColor("#000000")));
            } else {
                String ews_score = copy_patient_data.get(i).getEWSScore().toString();
                if (ews_score == String.valueOf(-1)) {
                    tv8.setText("");
                } else {
                    tv8.setText(ews_score);
                }
                if (Integer.parseInt(ews_score) >= 1 && Integer.parseInt(ews_score) <= 5) {
                    tv8.setBackgroundColor(Color.YELLOW);
                } else if (Integer.parseInt(ews_score) >= 6 && Integer.parseInt(ews_score) <= 7) {
                    tv8.setBackgroundColor(Color.parseColor("#ffa500"));
                } else if (Integer.parseInt(ews_score) >= 8 && Integer.parseInt(ews_score) <= 9) {
                    tv8.setBackgroundColor(Color.RED);
                } else if (Integer.parseInt(ews_score) >= 10) {
                    tv8.setBackgroundColor(Color.parseColor("#3F51B5"));
                } else {
                    tv8.setBackgroundColor(Color.parseColor("#f8f8f8"));
                }
                tv8.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                tv8.setTypeface(tv.getTypeface(), Typeface.BOLD);
                tv8.setTextColor((Color.parseColor("#000000")));
            }

            final TableRow tr = new TableRow(this);
            tr.setId(i + 1);
            TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
            trParams.setMargins(leftRowMargin, topRowMargin, rightRowMargin, bottomRowMargin);
            tr.setLayoutParams(trParams);

            if (i == -1) {
                tr.setPadding(0, 8, 0, 0);
            } else if (i > -1) {
                tr.setPadding(0, 8, 0, 8);
            }

            if (i == 0) {
                tr.setPadding(0, 16, 0, 8);
            }
            if (i > -1) {
                tr.setBackgroundColor(Color.parseColor("#8FBC8F"));
            }
            if (i == copy_patient_data.size() - 1) {
                tr.setPadding(0, 8, 0, 16);
            }
            if (i == copy_patient_data.size() - 1 && i == 0) {
                tr.setPadding(0, 16, 0, 16);
            }
            tr.setSelected(true);
            tr.addView(tv);
            tr.addView(tv2);
            tr.addView(tv3);
            tr.addView(tv4);
            tr.addView(tv5);
            tr.addView(tv6);
            tr.addView(tv7);
            tr.addView(tv8);

            // set on click on table row
            if (i > -1) {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        for (int i = 1; i < mTablelayout.getChildCount(); i++) {
                            View row = mTablelayout.getChildAt(i);
                            if (row == view) {
                                int _clickedRowIndex = row.getId();
                                str_RegAttDoc_Code = copy_patient_data.get(_clickedRowIndex - 1).getRegAttDoctor();
                                str_RegAttDoc_Name = copy_patient_data.get(_clickedRowIndex - 1).getRegAttDoctorName();
                                Log.e("Doctor ", copy_patient_data.get(_clickedRowIndex - 1).getRegAttDoctor());
                                row.setBackgroundColor(Color.parseColor("#5c6bc0"));
                            } else {
                                row.setBackgroundColor(Color.parseColor("#8FBC8F"));
                            }

                        }
                        TextView patient_cpi = (TextView) tr.getChildAt(1);
                        final String str_patient_cpi = patient_cpi.getText().toString();

                        TextView patient_name = (TextView) tr.getChildAt(2);
                        final String str_patient_name = patient_name.getText().toString();

                        TextView patient_visit_number = (TextView) tr.getChildAt(3);
                        final String str_patient_visit_number = patient_visit_number.getText().toString();

                        TextView patient_reg_date = (TextView) tr.getChildAt(6);
                        final String str_patient_reg_date = patient_reg_date.getText().toString();
                        Log.e("cpi", str_patient_cpi);
                        Log.e("cpi", str_patient_name);
                        Log.e("cpi", str_patient_visit_number);
                        Log.e("cpi", str_patient_reg_date);

                        addVital_dialog = new Dialog(NurseDashboard.this);
                        addVital_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        addVital_dialog.setContentView(R.layout.confirmdialog_of_prnote);


                        add_Prnote = addVital_dialog.findViewById(R.id.add_progressNote);
                        dismiss_toAddPrnote = addVital_dialog.findViewById(R.id.cancel);
                        txt_vital_confirmation = addVital_dialog.findViewById(R.id.txt_content);

                        txt_vital_confirmation.setText("You will add handwritten vital note for");

                        TextView txt_patient_name = addVital_dialog.findViewById(R.id.txt_patient_name);
                        TextView txt_patient_cpi = addVital_dialog.findViewById(R.id.txt_patient_cpi);
                        TextView txt_patient_visit = addVital_dialog.findViewById(R.id.txt_patient_vistiNo);
                        txt_patient_name.setText(":   " + str_patient_name);
                        txt_patient_cpi.setText(":   " + str_patient_cpi);
                        txt_patient_visit.setText(":   " + str_patient_visit_number);

                        addVital_dialog.setCanceledOnTouchOutside(false);
                        addVital_dialog.show();

                        dismiss_toAddPrnote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                for (int i = 1; i < mTablelayout.getChildCount(); i++) {
                                    View row = mTablelayout.getChildAt(i);
                                    row.setBackgroundColor(Color.parseColor("#8FBC8F"));
                                }
                                addVital_dialog.dismiss();
                            }
                        });

                        add_Prnote.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent myIntent = new Intent(NurseDashboard.this, NurseVitalSketch.class);
                                myIntent.putExtra("PATIENT_CPI", str_patient_cpi);
                                myIntent.putExtra("PATIENT_NAME", str_patient_name);
                                myIntent.putExtra("PATIENT_VISIT_NUMBER", str_patient_visit_number);
                                myIntent.putExtra("PATIENT_REG_DATE", str_patient_reg_date);
                                myIntent.putExtra("ATTENDING_DOCTOR_CODE", str_RegAttDoc_Code);
                                myIntent.putExtra("ATTENDING_DOCTOR_NAME", str_RegAttDoc_Name);
                                myIntent.putExtra("NURSE_CODE", nurseCode);
                                Log.e("nurse code", nurseCode);
                                myIntent.putExtra("NURSE_NAME", nurseName);
                                mHandler.removeCallbacks(m_Runnable);
                                startActivityForResult(myIntent, 0);
                            }
                        });
                    }
                });
            }
            mTablelayout.addView(tr, trParams);
        }
    }

    // filter function
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        copy_patient_data.clear();
        if (charText.length() == 0) {
            copy_patient_data.addAll(copy_patient_data_for_search);
            Log.e("data in filter", String.valueOf(copy_patient_data.size()));
        } else {
            for (NurseDashboardData wp : copy_patient_data_for_search) {
                if (wp.getPatientCPI().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getPatientName().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getRegNumber().toLowerCase(Locale.getDefault()).contains(charText)) {
                    copy_patient_data.add(wp);
                }
            }
        }
        Log.e("total patient", String.valueOf(copy_patient_data.size()));
    }

    // on back pressed
    @Override
    public void onBackPressed() {
        final Dialog dialog = new Dialog(NurseDashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_with_two_buttons);

        TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
        dialogBody.setText("Are you sure want to quit? ");

        Button ok_btn = dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent go_to_mainActivity = new Intent(NurseDashboard.this, MainActivity.class);
                startActivity(go_to_mainActivity);
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mHandler.removeCallbacks(m_Runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}