package com.anzer.hospitalebook;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.anzer.hospitalebook.Package_HandwritingActivity.HandwritingActivity;
import com.anzer.hospitalebook.Package_Scanner.Scanner;
import com.anzer.hospitalebook.SQLiteOpenHelper.SQLiteHelper;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.CustomSpinnerAdapter;
import com.anzer.hospitalebook.models.adapters.Item;
import com.anzer.hospitalebook.models.views.CircleView;
import com.anzer.hospitalebook.models.views.ScannerView;
import com.anzer.hospitalebook.models.views.ShakingBell;
import com.anzer.hospitalebook.vo.Doctor_Dashboard_Data;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import androidx.appcompat.widget.Toolbar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by David on 5/14/2018.
 */

public class Doctor_Dashboard extends BaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private TableLayout mTablelayout;
    public static SearchView searchView;
    public static String input_text = "";
    public static Boolean fromDashboardToFragmentDetail = false;
    public static Boolean fromNotification = false;
    public static String doctor_code, doctor_name, Pt_CPI, Pt_CurrentReg;
    public static SQLiteHelper sqLiteHelper;
    public static CircleView circleView;
    private String patient_name, patient_cpi, patient_visit_no, patient_reg_date;
    public static ShakingBell shakingBell;
    private ArrayList<Item> notSave_images;
    private boolean mPermissionGranted;
    private static final int CAMERA_PERMISSION_REQUEST = 0xabc;
    public static String search_view_query = "";
    public View row;

    ArrayList<Doctor_Dashboard_Data> copy_patient_data;
    ArrayList<Doctor_Dashboard_Data> vip_emergency_patient;
    ArrayList<Doctor_Dashboard_Data> emergency_patient;
    ArrayList<Doctor_Dashboard_Data> normal_condition_patient;
    ArrayList<Doctor_Dashboard_Data> real_patient_list_for_dashboard;
    ArrayList<Doctor_Dashboard_Data> copy_patient_data_for_search = new ArrayList<>();
    ArrayList<String> spinnerItem = new ArrayList<>();

    //    Get_Data_For_Doctor_Dashboard getDataForDoctorDashboard;
    Spinner registerType;
    ProgressDialog progress;
    CustomSpinnerAdapter customSpinnerAdapter;
    Handler mHandler;
    Bitmap bmap;
    Button btn_barcode_scanner;
    ScannerView mScanner;
    Dialog addPrnote_dialog;
    Button add_Prnote, dismiss_toAddPrnote;
    CircleProgressBar mProgressBar;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_dashboard);
        mTablelayout = findViewById(R.id.patient_table);
        mTablelayout.setStretchAllColumns(true);
        searchView = findViewById(R.id.cpi_search);
        registerType = findViewById(R.id.register_type_spinner);
        btn_barcode_scanner = findViewById(R.id.scan_barcode_btn);
        copy_patient_data = new ArrayList<>();

        //progress bar
        mProgressBar = findViewById(R.id.progress_bar);
        mProgressBar.setColorSchemeResources(android.R.color.holo_blue_bright);
        mProgressBar.setClickable(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        shakingBell = (ShakingBell) findViewById(R.id.shakeBell);
        circleView = (CircleView) findViewById(R.id.circleView);

        notSave_images = new ArrayList<Item>();

        doctor_code = MainActivity.copy_doctor_code;
        doctor_name = MainActivity.copy_doctor_name;

        btn_barcode_scanner.setOnClickListener(this);
        int searchCloseButtonId = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);

        this.mHandler = new Handler();
        m_Runnable.run();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.e("message >>>> ", "onQueryTextSubmit: ");
                input_text = query;
                filter(input_text);
                startLoadDataForFilter();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                input_text = newText;
                filter(input_text);
                startLoadDataForFilter();
                return false;
            }
        });

        registerType.setOnItemSelectedListener(this);

        ImageView closeButton = this.searchView.findViewById(searchCloseButtonId);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_view_query = "";
                searchView.setQuery("", false);
            }
        });

        try {
            sqLiteHelper = new SQLiteHelper(this, "HW_APP", null, 1);
            SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT adCODE,adNAME FROM DOCTOR_TABLE where adCODE = '" + doctor_code + "' and adNAME = '" + doctor_name + "'", null);

            while (cursor.moveToNext()) {
                Log.e("retrieve  >>>> ", String.valueOf(cursor.getColumnCount()));
                Log.e("retrieve  >>>> ", cursor.getString(0) + cursor.getString(1));
                Log.e("retrieve >>>> ", String.valueOf(cursor.getColumnIndex("adCODE")));
            }
            // set noti when doctor left images in sqlite db
            if (cursor.getCount() >= 1) {
                shakingBell.shake(1);
            }
        } catch (RuntimeException ex) {
            ex.printStackTrace();
        }

        //// set on click listener when noti is available
        if (circleView.getMessageNum() == 1)
            shakingBell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fromNotification = true;
                    mHandler.removeCallbacks(m_Runnable);
//                    Toast.makeText(Doctor_Dashboard.this, "Hi", Toast.LENGTH_SHORT).show();
                    sqLiteHelper = new SQLiteHelper(Doctor_Dashboard.this, "temp_db", null, 1);
                    SQLiteDatabase db = sqLiteHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT * FROM PATIENT_INFO_TABLE WHERE AD_CODE = '" + Doctor_Dashboard.doctor_code + "'", null);
                    while (cursor.moveToNext()) {
                        patient_name = cursor.getString(2);
                        patient_cpi = cursor.getString(3);
                        patient_visit_no = cursor.getString(4);
                        patient_reg_date = cursor.getString(5);
                    }

                    Cursor cursor1 = db.rawQuery("SELECT HANDWRITING_IMG,PHOTO_TYPE FROM HANDWRITING_IMG_TABLE WHERE AD_CODE = '" + Doctor_Dashboard.doctor_code + "' AND PATIENT_NAME = '" + patient_name + "' AND PATIENT_CPI = '" + patient_cpi + "' AND PATIENT_VISIT_NO = '" + patient_visit_no + "' AND PATIENT_REG_DATE = '" + patient_reg_date + "'", null);
                    while (cursor1.moveToNext()) {
//                        Log.e("Photo = 2 OR PRO = 1 OR HW = 0  >>> ",cursor1.getString(1));
                        if (cursor1.getString(1).equals(String.valueOf(1))) {
                            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
                            Bitmap bmp;
//                            Log.e("images uri without provider", String.valueOf(cursor1.getString(0)));
                            Uri uri = Uri.parse(cursor1.getString(0));
                            MainActivity.image_uri_array.add(uri);
                            try {
                                InputStream input = getContentResolver().openInputStream(uri);
                                bmp = BitmapFactory.decodeStream(input, null, bitmapOptions);
                                notSave_images.add(new Item(bmp, "Progress Note"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else if (cursor1.getString(1).equals(String.valueOf(0))) {
                            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
                            Bitmap bmp;
//                            Log.e("images uri without provider", String.valueOf(cursor1.getString(0)));
                            Uri uri = Uri.parse(cursor1.getString(0));
                            MainActivity.image_uri_array.add(uri);
                            try {
                                InputStream input = getContentResolver().openInputStream(uri);
                                bmp = BitmapFactory.decodeStream(input, null, bitmapOptions);
                                notSave_images.add(new Item(bmp, "Handwriting Order"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {
                            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//
                            Bitmap bmp;
//                            Log.e("images uri with provider", String.valueOf(cursor1.getString(0)));
                            Uri uri = Uri.parse(cursor1.getString(0));
                            MainActivity.image_uri_array.add(uri);
                            try {
                                InputStream input = getContentResolver().openInputStream(uri);
                                bmp = BitmapFactory.decodeStream(input, null, bitmapOptions);
                                Bitmap scalebitmap = Bitmap.createScaledBitmap(bmp, 1110, 800, true);
                                Matrix matrix = new Matrix();
                                matrix.postRotate(90);
                                Bitmap rotatedBitmap = Bitmap.createBitmap(scalebitmap, 0, 0, scalebitmap.getWidth(), scalebitmap.getHeight(), matrix, true);
                                notSave_images.add(new Item(rotatedBitmap, "Photo"));
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    MainActivity.image_array.addAll(notSave_images);  ///// Need to redo
                    Toast.makeText(Doctor_Dashboard.this, "Uri Array Size >> " + String.valueOf(MainActivity.image_uri_array.size()), Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Doctor_Dashboard.this, HandwritingActivity.class);
                    startActivity(myIntent);
                }
            });
            /// remove onclick listener when noti isn't available
        else
            shakingBell.setOnClickListener(null);
        fromNotification = false;
    }

    /// auto refresh doctor dashboard every 1 min
    private final Runnable m_Runnable = new Runnable() {
        public void run() {
//            Toast.makeText(Doctor_Dashboard.this, "Auto Refresh run !!!", Toast.LENGTH_SHORT).show();
            data_retrieve_from_DB();
            if (registerType.getSelectedItem().toString().equals("All Register Type")) {
                copy_patient_data.clear();
                copy_patient_data_for_search.clear();
                copy_patient_data_for_search.addAll(real_patient_list_for_dashboard);
            } else if (registerType.getSelectedItem().toString().equals("Outpatient")) {
                copy_patient_data.clear();
                copy_patient_data_for_search.clear();
                for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                    if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("o")) {
                        copy_patient_data_for_search.add(wp);
                    }
                }
            } else if (registerType.getSelectedItem().toString().equals("Inpatient")) {
                copy_patient_data.clear();
                copy_patient_data_for_search.clear();
                for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                    if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("a*")) {
                        copy_patient_data_for_search.add(wp);
                    }
                }
            } else if (registerType.getSelectedItem().toString().equals("Emergency")) {
                copy_patient_data.clear();
                copy_patient_data_for_search.clear();
                for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                    if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("e*")) {
                        copy_patient_data_for_search.add(wp);
                    }
                }
            } else {
                copy_patient_data.clear();
                copy_patient_data_for_search.clear();
                for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                    if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("d*")) {
                        copy_patient_data_for_search.add(wp);
                    }
                }
            }

            copy_patient_data.addAll(copy_patient_data_for_search);
            Doctor_Dashboard.this.mHandler.postDelayed(m_Runnable, 50000);
        }
    };

    // retrieve data from database
    public void data_retrieve_from_DB() {
        mProgressBar.setVisibility(View.VISIBLE);
        Log.e("MESSAGE", "DATA ARE RETRIEVED");
        emergency_patient = new ArrayList<>();
        normal_condition_patient = new ArrayList<>();
        real_patient_list_for_dashboard = new ArrayList<>();
        vip_emergency_patient = new ArrayList<>();

        copy_patient_data.clear();
        real_patient_list_for_dashboard.clear();

        Log.e("before insert", String.valueOf(copy_patient_data.size()));
        ArrayList<Doctor_Dashboard_Data> dashboard_data = new ArrayList<>();
        dashboard_data.clear();
        Log.e("Dashboard Data", String.valueOf(dashboard_data.size()));

        // get data using retrofit
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ArrayList<Doctor_Dashboard_Data>> call = apiInterface.GetDoctorDashboardData(doctor_code, MainActivity.insti_code);
        call.enqueue(new Callback<ArrayList<Doctor_Dashboard_Data>>() {
            @Override
            public void onResponse(Call<ArrayList<Doctor_Dashboard_Data>> call, Response<ArrayList<Doctor_Dashboard_Data>> response) {
                if (response.isSuccessful()) {
                    dashboard_data.clear();
                    dashboard_data.addAll(response.body());
                    for (int cpi = 0; cpi < dashboard_data.size(); cpi++) {
                        if (dashboard_data.get(cpi).getPatient_ews_score() == null) {
                            dashboard_data.get(cpi).setPatient_ews_score(String.valueOf(-1));
                        }
                    }

                    for (int cpi = 0; cpi < dashboard_data.size(); cpi++) {
                        if (dashboard_data.get(cpi).getImg_isSaved() == null) {
                            dashboard_data.get(cpi).setImg_isSaved(String.valueOf(-1));
                        }
                    }

                    for (int index = 0; index < dashboard_data.size(); index++) {
                        if (Integer.parseInt(dashboard_data.get(index).getPatient_ews_score()) >= 10) {
                            vip_emergency_patient.add(dashboard_data.get(index));
                        } else if (Integer.parseInt(dashboard_data.get(index).getPatient_ews_score()) >= 8 &&
                                Integer.parseInt(dashboard_data.get(index).getPatient_ews_score()) <= 9) {
                            Log.e("emergency", dashboard_data.get(index).getPatient_ews_score());
                            emergency_patient.add(dashboard_data.get(index));
                        } else {
                            normal_condition_patient.add(dashboard_data.get(index));
                        }
                    }
                    Log.e("vip emergency patient", String.valueOf(vip_emergency_patient.size()));
                    Log.e("emergency patient", String.valueOf(emergency_patient.size()));
                    Log.e("normal patient", String.valueOf(normal_condition_patient.size()));
                    Collections.sort(vip_emergency_patient, PtEWSScoreComparator);
                    Collections.sort(emergency_patient, PtEWSScoreComparator);
                    copy_patient_data.clear();
                    copy_patient_data.addAll(vip_emergency_patient);
                    copy_patient_data.addAll(emergency_patient);
                    copy_patient_data.addAll(normal_condition_patient);

                    real_patient_list_for_dashboard.clear();
                    real_patient_list_for_dashboard.addAll(copy_patient_data);

                    vip_emergency_patient.clear();
                    emergency_patient.clear();
                    normal_condition_patient.clear();

                    Log.e("All Data", String.valueOf(copy_patient_data.size()));

                    // load data
                    startLoadData();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Doctor_Dashboard_Data>> call, Throwable t) {
                mProgressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Something wrong!", Toast.LENGTH_LONG).show();
            }
        });
    }

    //Compare EWS Score for vip emergency and emergency
    public static Comparator<Doctor_Dashboard_Data> PtEWSScoreComparator = new Comparator<Doctor_Dashboard_Data>() {
        public int compare(Doctor_Dashboard_Data s1, Doctor_Dashboard_Data s2) {
            String EWS1 = s1.getPatient_ews_score().toUpperCase();
            String EWS2 = s2.getPatient_ews_score().toUpperCase();

            //ascending order
//            return StudentName1.compareTo(StudentName2);

            //descending order
            return EWS2.compareTo(EWS1);
        }
    };

    /// load data to tablelayout in doctor dashboard
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

        Log.e("MESSAGE", "LOAD DATA ON TABLEVIEW");

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
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv.setTextColor((Color.parseColor("#000000")));
                    tv.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                tv2.setText(copy_patient_data.get(i).getPatient_cpi());
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv2.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv2.setTextColor((Color.parseColor("#000000")));
                    tv2.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                tv3.setText(copy_patient_data.get(i).getPatient_name());
//                Log.e("get data", copy_patient_data.get(i).getImg_isSaved());
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv3.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv3.setTextColor((Color.parseColor("#000000")));
                    tv3.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                tv4.setText(copy_patient_data.get(i).getPatient_visit_number());
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv4.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv4.setTextColor((Color.parseColor("#000000")));
                    tv4.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                tv5.setText(copy_patient_data.get(i).getPatient_queue_number());
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv5.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv5.setTextColor((Color.parseColor("#000000")));
                    tv5.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                tv6.setText(copy_patient_data.get(i).getPatient_room_number());
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv6.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv6.setTextColor((Color.parseColor("#000000")));
                    tv6.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                tv7.setText(copy_patient_data.get(i).getPatient_reg_date_time());
                if (copy_patient_data.get(i).getImg_isSaved().equals("-1") || copy_patient_data.get(i).getImg_isSaved().equals("0"))
                    tv7.setBackgroundColor(Color.parseColor("#f8f8f8"));
                else {
                    tv7.setTextColor((Color.parseColor("#000000")));
                    tv7.setBackgroundColor(Color.parseColor("#D6F0DF"));
                }
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
                String ews_score = copy_patient_data.get(i).getPatient_ews_score();
                if (ews_score == String.valueOf(-1)) {
                    tv8.setText("");
                } else {
                    tv8.setText(ews_score);
                }
                if (Integer.parseInt(ews_score) >= 1 && Integer.parseInt(ews_score) <= 5) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(tv8, "backgroundColor", Color.parseColor("#f8f8f8"), Color.YELLOW, Color.parseColor("#f8f8f8"));
                    anim.setDuration(1500);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.start();
                } else if (Integer.parseInt(ews_score) >= 6 && Integer.parseInt(ews_score) <= 7) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(tv8, "backgroundColor", Color.parseColor("#f8f8f8"), Color.parseColor("#ffa500"), Color.parseColor("#f8f8f8"));
                    anim.setDuration(1500);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.start();
                } else if (Integer.parseInt(ews_score) >= 8 && Integer.parseInt(ews_score) <= 9) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(tv8, "backgroundColor", Color.parseColor("#f8f8f8"), Color.RED, Color.parseColor("#f8f8f8"));
                    anim.setDuration(1500);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.start();
                } else if (Integer.parseInt(ews_score) >= 10) {
                    ObjectAnimator anim = ObjectAnimator.ofInt(tv8, "backgroundColor", Color.parseColor("#f8f8f8"), Color.parseColor("#3F51B5"), Color.parseColor("#f8f8f8"));
                    anim.setDuration(1500);
                    anim.setEvaluator(new ArgbEvaluator());
                    anim.setRepeatCount(Animation.INFINITE);
                    anim.start();
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

            if (i > -1) {
                tr.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (circleView.getMessageNum() == 1) {
                            Dialog dialog = new Dialog(Doctor_Dashboard.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.dialog_with_one_button);

                            TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
                            dialogBody.setText("You have unsuccessful status with previous handwriting order's images !!! PLEASE DO IT FIRST !!! ");

                            Button ok_btn = dialog.findViewById(R.id.ok_btn);

                            ok_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                            dialog.setCanceledOnTouchOutside(false);
                            dialog.show();
                        } else {
                            for (int i = 1; i < mTablelayout.getChildCount(); i++) {
                                View row = mTablelayout.getChildAt(i);
                                if (row == view) {
                                    row.setBackgroundColor(Color.parseColor("#5c6bc0"));
                                } else {
                                    row.setBackgroundColor(Color.parseColor("#8FBC8F"));
                                }
                            }
                            TextView patient_cpi = (TextView) tr.getChildAt(1);
                            String str_patient_cpi = patient_cpi.getText().toString();

                            TextView patient_name = (TextView) tr.getChildAt(2);
                            String str_patient_name = patient_name.getText().toString();

                            TextView patient_visit_number = (TextView) tr.getChildAt(3);
                            String str_patient_visit_number = patient_visit_number.getText().toString();

                            TextView patient_reg_date = (TextView) tr.getChildAt(6);
                            String str_patient_reg_date = patient_reg_date.getText().toString();
                            Log.e("cpi", str_patient_cpi);
                            Log.e("cpi", str_patient_name);
                            Log.e("cpi", str_patient_visit_number);
                            Log.e("cpi", str_patient_reg_date);

                            addPrnote_dialog = new Dialog(Doctor_Dashboard.this);
                            addPrnote_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            addPrnote_dialog.setContentView(R.layout.confirmdialog_of_prnote);

                            add_Prnote = addPrnote_dialog.findViewById(R.id.add_progressNote);
                            dismiss_toAddPrnote = addPrnote_dialog.findViewById(R.id.cancel);

                            TextView txt_patient_name = addPrnote_dialog.findViewById(R.id.txt_patient_name);
                            TextView txt_patient_cpi = addPrnote_dialog.findViewById(R.id.txt_patient_cpi);
                            TextView txt_patient_visit = addPrnote_dialog.findViewById(R.id.txt_patient_vistiNo);
                            txt_patient_name.setText(":   " + str_patient_name);
                            txt_patient_cpi.setText(":   " + str_patient_cpi);
                            txt_patient_visit.setText(":   " + str_patient_visit_number);

                            addPrnote_dialog.setCanceledOnTouchOutside(false);
                            addPrnote_dialog.show();

                            dismiss_toAddPrnote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    for (int i = 1; i < mTablelayout.getChildCount(); i++) {
                                        View row = mTablelayout.getChildAt(i);
                                        row.setBackgroundColor(Color.parseColor("#8FBC8F"));
                                    }
                                    addPrnote_dialog.dismiss();
                                }
                            });

                            add_Prnote.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Pt_CPI = str_patient_cpi;
                                    Pt_CurrentReg = str_patient_visit_number;

                                    Intent myIntent = new Intent(Doctor_Dashboard.this, HandwritingActivity.class);
                                    fromDashboardToFragmentDetail = true;
                                    myIntent.putExtra("PATIENT_CPI", str_patient_cpi);
                                    myIntent.putExtra("PATIENT_NAME", str_patient_name);
                                    myIntent.putExtra("PATIENT_VISIT_NUMBER", str_patient_visit_number);
                                    myIntent.putExtra("PATIENT_REG_DATE", str_patient_reg_date);
                                    myIntent.putExtra("From_Dashboard_To_FragDetail", fromDashboardToFragmentDetail);
                                    MainActivity.image_array.clear();                         ///// Need to redo
                                    MainActivity.image_uri_array.clear();
                                    mHandler.removeCallbacks(m_Runnable);
                                    search_view_query = "";
                                    startActivityForResult(myIntent, 0);
                                }
                            });
                        }
                    }
                });
            }
            mTablelayout.addView(tr, trParams);
        }
    }

    // load data with progress bar for cpi filter
    public void startLoadDataForFilter() {
        mProgressBar.setVisibility(View.VISIBLE);
        Doctor_Dashboard.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new LoadDataTaskForFilter().execute(0);
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
            Log.e("filter load", "done this class");
            LoadData();
            mProgressBar.setVisibility(View.INVISIBLE);
            Doctor_Dashboard.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        }

        @Override
        protected void onPreExecute() {
        }
    }

    // load data with progress bar    *****PATIENT DATA ON  DASHBOARD*****
    public void startLoadData() {
        mProgressBar.setVisibility(View.VISIBLE);
        Doctor_Dashboard.this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        new LoadDataTask().execute(0);
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
            Doctor_Dashboard.this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            LoadData();
        }

        @Override
        protected void onPreExecute() {

        }
    }

    /// search function
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        copy_patient_data.clear();
        if (charText.length() == 0) {
            copy_patient_data.addAll(copy_patient_data_for_search);
        } else {
            for (Doctor_Dashboard_Data wp : copy_patient_data_for_search) {
                if (wp.getPatient_cpi().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getPatient_name().toLowerCase(Locale.getDefault()).contains(charText) ||
                        wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains(charText)) {
                    copy_patient_data.add(wp);
                }
            }
        }
        Log.e("total patient", String.valueOf(copy_patient_data.size()));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 1) {
            copy_patient_data.clear();
            copy_patient_data_for_search.clear();
            for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("o")) {
                    copy_patient_data.add(wp);
                    copy_patient_data_for_search.add(wp);
                }
            }
            startLoadData();
            if (!(search_view_query.equals(""))) {
                searchView.setQuery(search_view_query, true);
                startLoadData();
            }
        }
        if (position == 2) {
            copy_patient_data.clear();
            copy_patient_data_for_search.clear();
            for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("a*")) {
                    copy_patient_data.add(wp);
                    copy_patient_data_for_search.add(wp);
                }
            }
            startLoadData();
            if (!(search_view_query.equals(""))) {
                searchView.setQuery(search_view_query, true);
                startLoadData();
            }
        }
        if (position == 3) {
            copy_patient_data.clear();
            copy_patient_data_for_search.clear();
            for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("e*")) {
                    copy_patient_data.add(wp);
                    copy_patient_data_for_search.add(wp);
                }
            }
            startLoadData();
            if (!(search_view_query.equals(""))) {
                searchView.setQuery(search_view_query, true);
                startLoadData();
            }
        }

        if (position == 4) {
            copy_patient_data.clear();
            copy_patient_data_for_search.clear();
            for (Doctor_Dashboard_Data wp : real_patient_list_for_dashboard) {
                if (wp.getPatient_visit_number().toLowerCase(Locale.getDefault()).contains("d*")) {
                    copy_patient_data.add(wp);
                    copy_patient_data_for_search.add(wp);
                }
            }
            startLoadData();
            if (!(search_view_query.equals(""))) {
                searchView.setQuery(search_view_query, true);
                startLoadData();
            }
        }

        if (position == 0) {
            copy_patient_data.clear();
            copy_patient_data_for_search.clear();
            copy_patient_data.addAll(real_patient_list_for_dashboard);
            copy_patient_data_for_search.addAll(real_patient_list_for_dashboard);
            startLoadData();
            if (!(search_view_query.equals(""))) {
                searchView.setQuery(search_view_query, true);
                startLoadData();
            }
            Log.e("david >>> ", String.valueOf(real_patient_list_for_dashboard.size()));
            Log.e("david >>> ", String.valueOf(copy_patient_data.size()));
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scan_barcode_btn:
                search_view_query = "";
                Intent intent = new Intent(this, Scanner.class);
                intent.putExtra("PageFrom", "DD");
                startActivityForResult(intent, 0);
                mHandler.removeCallbacks(m_Runnable);
                finish();
        }
    }

    private void goBack() {
        Intent go_to_mainactivity = new Intent(this, MainActivity.class);
        startActivity(go_to_mainactivity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_Runnable.run();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("on pause", "onPause : ");
    }

    @Override
    public void onDestroy() {
        Log.e("destroy", "onDestroy: ");
        super.onDestroy();
        Runtime.getRuntime().gc();                      /// testing for OOM error
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new Dialog(Doctor_Dashboard.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_with_two_buttons);

        TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
        dialogBody.setText("Are you sure want to quit? ");

        Button ok_btn = dialog.findViewById(R.id.ok_btn);
        Button cancel_btn = dialog.findViewById(R.id.cancel_btn);

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
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
        search_view_query = "";
    }
}




