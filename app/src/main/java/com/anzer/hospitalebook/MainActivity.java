package com.anzer.hospitalebook;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anzer.hospitalebook.NurseDashboard.NurseDashboard;
import com.anzer.hospitalebook.Package_Validation.Validation;
import com.anzer.hospitalebook.SQLiteOpenHelper.SQLiteHelper;
import com.anzer.hospitalebook.api.ApiClient;
import com.anzer.hospitalebook.api.ApiInterface;
import com.anzer.hospitalebook.models.adapters.Item;
import com.anzer.hospitalebook.vo.DI_OrderedTestsModel;
import com.anzer.hospitalebook.vo.DocLoginData;
import com.anzer.hospitalebook.vo.LAB_OrderedTestsModel;
import com.anzer.hospitalebook.vo.NurseLoginData;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import javax.mail.Authenticator;
//import javax.mail.BodyPart;
//import javax.mail.Message;
//import javax.mail.MessagingException;
//import javax.mail.Multipart;
//import javax.mail.PasswordAuthentication;
//import javax.mail.Session;
//import javax.mail.Transport;
//import javax.mail.internet.InternetAddress;
//import javax.mail.internet.MimeBodyPart;
//import javax.mail.internet.MimeMessage;
//import javax.mail.internet.MimeMultipart;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    static Button login_button, change_password;
    static CheckBox remember_me_checkbox;
    static TextInputLayout login_username;
    public static TextInputLayout login_user_password;
    public static TextInputEditText login_username2;
    public static TextInputEditText login_user_password2;
    static TextView institution_field;
    static String copy_username;

    private static SQLiteHelper dbhelper;
    private TextInputEditText admin_password;
    private TextInputLayout admin_password_layout;
    private Boolean saveLogin;
    public static String copy_doctor_code, copy_doctor_name;
    public static String DB_username, DB_password, DB_NAME, Server_ip, DB_port;
    public static ArrayList<Item> image_array;
    public static ArrayList<Uri> image_uri_array;
    public static ArrayList<Item> image_array_clone;
    public static ArrayList<Uri> image_array_clone_uri;
    public static String insti_code;
    public static int success;
    public static Dialog feedback_Dialog;
    public static EditText msg, sender_name;
    public static String textMessage, txt_sender_name;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginPrefsEditor;
    ProgressDialog pd;

    Connection con;
    String doctor_code, doctor_name;
    public static String nurse_code, nurse_name;
    RelativeLayout password_dialog_body, dialog_body, txt_reqeust_admin_password;
    Button cancel, submit, send, password_submit;
    Dialog dbSetting_Dialog, insti_Dialog, exit_Dialog, contact_us_Dialog, about_Dialog;
    DrawerLayout drawer;
    Spinner insti_spinner;
    TextView txt_institution;
    Boolean saveInsti_value;
    Boolean saveIPAddress;
    String txt_insti_value;
    Boolean NullInstiField;
    ProgressDialog pdialog = null;
    Context context = null;
    String copytextMessage, recipient;
    private boolean saveSessionTimeout_value;
    public static Integer DISCONNECT_TIMEOUT;
    Integer check_item_for_SessionTimeout = -1;
    String IP_address, first_part, second_part, third_part, fourth_part;
    TextView textViewFirstPart, textViewSecondPart, textViewThirdPart, textViewFourthPart;
    SwitchCompat switchButton;
    Boolean _isDoctorLoginFlag;
    public static ArrayList<DI_OrderedTestsModel> DI_OrderedTest;
    public static ArrayList<LAB_OrderedTestsModel> LAB_OrderedTest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DISCONNECT_TIMEOUT = 300000;   ///Default session time out 5 mins

        // demo server connection
        DB_username = "sa";
        DB_password = "Anz20SQL17#June19";
        DB_NAME = "MM_OFFICE_HIS_test";
        DB_port = "1433";
        _isDoctorLoginFlag = true;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_button = findViewById(R.id.login_button);
        login_username = findViewById(R.id.login_username);
        login_user_password = findViewById(R.id.login_password);
        login_username2 = findViewById(R.id.login_username2);
        login_user_password2 = findViewById(R.id.login2_password);
        remember_me_checkbox = findViewById(R.id.remember_username);
        txt_institution = findViewById(R.id.txt_institution_field);
        switchButton = findViewById(R.id.switch_button);


        image_array = new ArrayList<Item>();
        image_array_clone = new ArrayList<Item>();                                  ///// Need to redo
        image_uri_array = new ArrayList<>();
        image_array_clone_uri = new ArrayList<>();
        DI_OrderedTest = new ArrayList<>();
        LAB_OrderedTest = new ArrayList<>();

        View v = View.inflate(MainActivity.this, R.layout.dialog_for_db_connection, null);
        textViewFirstPart = v.findViewById(R.id.txt_first);
        textViewSecondPart = v.findViewById(R.id.txt_second);
        textViewThirdPart = v.findViewById(R.id.txt_third);
        textViewFourthPart = v.findViewById(R.id.txt_fourth);

        login_button.setOnClickListener(this);

        login_user_password2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0)
                    login_user_password.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveIPAddress = loginPreferences.getBoolean("saveIPAddress", false);
        if (saveIPAddress == true) {
            textViewFirstPart.setText(loginPreferences.getString("first_part_IP", ""));
            Log.e("F > ", loginPreferences.getString("first_part_IP", ""));
            textViewSecondPart.setText(loginPreferences.getString("second_part_IP", ""));
            Log.e("S > ", loginPreferences.getString("second_part_IP", ""));
            textViewThirdPart.setText(loginPreferences.getString("third_part_IP", ""));
            Log.e("T > ", loginPreferences.getString("third_part_IP", ""));
            textViewFourthPart.setText(loginPreferences.getString("fourth_part_IP", ""));
            Log.e("Fo > ", loginPreferences.getString("fourth_part_IP", ""));
            Server_ip = loginPreferences.getString("IP_value", "");
            Log.e("Server ip >  ", Server_ip);
        }

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveLogin = loginPreferences.getBoolean("saveLogin", false);
        if (saveLogin == true) {
            login_username2.setText(loginPreferences.getString("username", ""));
            login_user_password2.setText(loginPreferences.getString("password", ""));
            remember_me_checkbox.setChecked(true);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveInsti_value = loginPreferences.getBoolean("saveInsti_value", false);
        if (saveInsti_value == true) {
            txt_institution.setText(loginPreferences.getString("insti_value", ""));
        }

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveSessionTimeout_value = loginPreferences.getBoolean("saveSessionTimeout_value", false);
        if (saveSessionTimeout_value == true) {
            DISCONNECT_TIMEOUT = loginPreferences.getInt("sessionTimeout_value", 0);
            check_item_for_SessionTimeout = loginPreferences.getInt("check_item_index", -1);
            Log.e("copy disconnect timeout", String.valueOf(DISCONNECT_TIMEOUT));
        }
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchButton.isChecked()) {
                    _isDoctorLoginFlag = false;
                } else {
                    _isDoctorLoginFlag = true;
                }
                Log.e("switch button", _isDoctorLoginFlag.toString());
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_button: {
                Log.e("success status", String.valueOf(success));
                if (remember_me_isChecked() && Validation.login_validation() && isConnected() && check_institution_field()) {
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                } else if (remember_me_isChecked() == false && Validation.login_validation() && isConnected() && check_institution_field()) {
                    DoLogin doLogin = new DoLogin();
                    doLogin.execute("");
                }
                break;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        if (id == R.id.nav_db_setting) {
//            // Handle db connection setting
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.isDrawerOpen(GravityCompat.START);
//            setup_DB_Connection();
////            Log.e("Server ip >  ", Server_ip);
//        } else
        if (id == R.id.nav_institution) {
            // Handle institution action
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.isDrawerOpen(GravityCompat.START);
            setup_Insti_Dialog();
        } else if (id == R.id.session_expire_time) {
            // Handle session setting action
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.isDrawerOpen(GravityCompat.START);
            session_setting_Dialog();
        }
//        else if (id == R.id.nav_send_feedback) {
//            // Handle send feedback action
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            drawer.isDrawerOpen(GravityCompat.START);
//            setup_feedback_Dialog();
//        }
        else if (id == R.id.nav_about) {
            // Handle about action
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.isDrawerOpen(GravityCompat.START);
            setup_About_Dialog();
        } else if (id == R.id.nav_contact_us) {
            // Handle contact us action
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.isDrawerOpen(GravityCompat.START);
            setup_contact_us_Dialog();
        } else if (id == R.id.nav_exit) {
            // Handle app exit
            setup_Exit_Dialog();
        }
        return true;
    }

    // setup db connection setting
    private void setup_DB_Connection() {
        dbSetting_Dialog = new Dialog(MainActivity.this);
        dbSetting_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dbSetting_Dialog.setContentView(R.layout.dialog_for_db_connection);

        submit = (Button) dbSetting_Dialog.findViewById(R.id.submit);
        submit.setEnabled(true);

        TextView tvfirst = dbSetting_Dialog.findViewById(R.id.txt_first);
        TextView tvsecond = dbSetting_Dialog.findViewById(R.id.txt_second);
        TextView tvthird = dbSetting_Dialog.findViewById(R.id.txt_third);
        TextView tvfourth = dbSetting_Dialog.findViewById(R.id.txt_fourth);

        loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();
        saveIPAddress = loginPreferences.getBoolean("saveIPAddress", false);
        if (saveIPAddress == true) {
            tvfirst.setText(loginPreferences.getString("first_part_IP", ""));
            Log.e("F > ", loginPreferences.getString("first_part_IP", ""));
            tvsecond.setText(loginPreferences.getString("second_part_IP", ""));
            Log.e("S > ", loginPreferences.getString("second_part_IP", ""));
            tvthird.setText(loginPreferences.getString("third_part_IP", ""));
            Log.e("T > ", loginPreferences.getString("third_part_IP", ""));
            tvfourth.setText(loginPreferences.getString("fourth_part_IP", ""));
            Log.e("Fo > ", loginPreferences.getString("fourth_part_IP", ""));
            Server_ip = loginPreferences.getString("IP_value", "");
        }
        submit.setOnClickListener(v -> {
            first_part = tvfirst.getText().toString().trim();
            Log.e("First > ", first_part);

            second_part = tvsecond.getText().toString().trim();
            Log.e("Second > ", second_part);

            third_part = tvthird.getText().toString().trim();
            Log.e("Third > ", third_part);

            fourth_part = tvfourth.getText().toString().trim();
            Log.e("Fourth > ", fourth_part);

            IP_address = first_part + "." + second_part + "." + third_part + "." + fourth_part;
            Log.e("IP Address > ", IP_address);

            saveIPAddress();
            dbSetting_Dialog.dismiss();
        });
        dbSetting_Dialog.setCanceledOnTouchOutside(false);
        dbSetting_Dialog.show();

    }

    //set up session time out
    private void session_setting_Dialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = View.inflate(this, R.layout.dialog_for_session_expire_setting, null);
        String[] session_timeout_value = getResources().getStringArray(R.array.session_timeout_values);

        builder.setTitle("Session Timeout");
        builder.setCustomTitle(v);
        builder.setSingleChoiceItems(R.array.session_timeout, check_item_for_SessionTimeout, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                DISCONNECT_TIMEOUT = Integer.parseInt(session_timeout_value[i]);
                check_item_for_SessionTimeout = i;
                saveSession_Timeout();
                dialog.dismiss();
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });

        AlertDialog mdialog = builder.create();
        mdialog.show();
    }

    //save session timeout
    protected boolean saveSession_Timeout() {
        boolean check = false;
        loginPrefsEditor.putBoolean("saveSessionTimeout_value", true);
        loginPrefsEditor.putInt("sessionTimeout_value", DISCONNECT_TIMEOUT);
        loginPrefsEditor.putInt("check_item_index", check_item_for_SessionTimeout);
        loginPrefsEditor.commit();
        check = true;
        return check;
    }

    //save connection ip address
    protected boolean saveIPAddress() {
        boolean check = false;
        loginPrefsEditor.putBoolean("saveIPAddress", true);
        loginPrefsEditor.putString("first_part_IP", first_part);
        loginPrefsEditor.putString("second_part_IP", second_part);
        loginPrefsEditor.putString("third_part_IP", third_part);
        loginPrefsEditor.putString("fourth_part_IP", fourth_part);
        loginPrefsEditor.putString("IP_value", IP_address);
        loginPrefsEditor.commit();
        check = true;
        return check;
    }

    // save institution code & description
    protected boolean saveInti_isChecked() {
        boolean check = false;
        txt_insti_value = insti_spinner.getSelectedItem().toString();
        loginPrefsEditor.putBoolean("saveInsti_value", true);
        loginPrefsEditor.putString("insti_value", txt_insti_value);
        loginPrefsEditor.commit();
        check = true;
        return check;
    }

    // institution set up Dialgo
    public void setup_Insti_Dialog() {
        insti_Dialog = new Dialog(MainActivity.this);
        insti_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        insti_Dialog.setContentView(R.layout.institution_dialog);

        dialog_body = insti_Dialog.findViewById(R.id.dialog_body);
        password_dialog_body = insti_Dialog.findViewById(R.id.password_dialog_body);
        txt_reqeust_admin_password = insti_Dialog.findViewById(R.id.txt_password_request_alert);

        insti_spinner = insti_Dialog.findViewById(R.id.insti_spinner_item);
        admin_password = insti_Dialog.findViewById(R.id.sa_password_field);
        admin_password_layout = insti_Dialog.findViewById(R.id.sa_password);
        password_submit = (Button) insti_Dialog.findViewById(R.id.password_submit);
        cancel = (Button) insti_Dialog.findViewById(R.id.cancel);
        submit = (Button) insti_Dialog.findViewById(R.id.insit_submit);

        password_submit.setEnabled(true);
        cancel.setEnabled(true);
        submit.setEnabled(true);

        dbhelper = new SQLiteHelper(this, "HW_APP", null, 1);
        File database = this.getDatabasePath(SQLiteHelper.DATABASE_NAME);
        if (database.exists() == false) {
            dbhelper.copyDatabase(this);
        }

        admin_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                    admin_password_layout.setPasswordVisibilityToggleEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        password_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (admin_password.getText().toString().equals("")) {
                    admin_password.setError("Invalid Password !!!");
                    admin_password_layout.setPasswordVisibilityToggleEnabled(false);
                } else {
                    dbhelper.openDatabase();
                    SQLiteDatabase db = dbhelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT Name FROM HW_ADMIN_TABLE WHERE Password = '" + admin_password.getText().toString() + "'", null);
                    if (cursor.getCount() > 0) {
                        password_submit.setVisibility(View.INVISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        password_dialog_body.setVisibility(View.INVISIBLE);
                        dialog_body.setVisibility(View.VISIBLE);
                        txt_reqeust_admin_password.setVisibility(View.INVISIBLE);
                    } else {
                        admin_password.setError("Wrong Password !!!");
                        admin_password.setText("");
                        admin_password_layout.setPasswordVisibilityToggleEnabled(false);
                    }
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (insti_spinner.getSelectedItem().toString().equals("City Hospital Mandalay")) {
                    txt_institution.setText(insti_spinner.getSelectedItem().toString());
                    saveInti_isChecked();
                    insti_Dialog.cancel();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                } else if (insti_spinner.getSelectedItem().toString().equals("Nyein")) {
                    txt_institution.setText(insti_spinner.getSelectedItem().toString());
                    saveInti_isChecked();
                    insti_Dialog.cancel();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    txt_institution.setText(insti_spinner.getSelectedItem().toString());
                    saveInti_isChecked();
                    insti_Dialog.cancel();
                    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                }

                Toast.makeText(MainActivity.this, insti_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insti_Dialog.cancel();
            }
        });

        insti_Dialog.setCanceledOnTouchOutside(false);
        insti_Dialog.show();
    }

    // exit Dialg
    public void setup_Exit_Dialog() {
        exit_Dialog = new Dialog(MainActivity.this);
        exit_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        exit_Dialog.setContentView(R.layout.exit_dialog);

        cancel = (Button) exit_Dialog.findViewById(R.id.cancel);
        submit = (Button) exit_Dialog.findViewById(R.id.submit);
        cancel.setEnabled(true);
        submit.setEnabled(true);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit_Dialog.cancel();
            }
        });
        exit_Dialog.setCanceledOnTouchOutside(false);
        exit_Dialog.show();
    }

//    // feedback dialog
//    public void setup_feedback_Dialog(){
//        context = this;
//        feedback_Dialog = new Dialog( MainActivity.this );
//        feedback_Dialog.requestWindowFeature( Window.FEATURE_NO_TITLE );
//        feedback_Dialog.setContentView( R.layout.feedback_dialog );
//        msg = feedback_Dialog.findViewById(R.id.textMessage);
//        sender_name = feedback_Dialog.findViewById(R.id.sender_name);
//
//        textMessage = msg.getText().toString();
//        txt_sender_name = sender_name.getText().toString();
//
//        cancel = (Button)feedback_Dialog.findViewById(R.id.btn_cancel);
//        send = (Button)feedback_Dialog.findViewById(R.id.btn_send);
//        cancel.setEnabled(true);
//        send.setEnabled(true);
//
//        send.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                    recipient = "minzaw@anzer.com";
//                    Properties props = new Properties();
//                    props.put("mail.smtp.host", "smtp.gmail.com");
//                    props.put("mail.smtp.socketFactory.port", "465");
//                    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//                    props.put("mail.smtp.auth", "true");
//                    props.put("mail.smtp.port", "465");
//                    session = Session.getDefaultInstance(props, new Authenticator() {
//                        protected PasswordAuthentication getPasswordAuthentication() {
//                            return new PasswordAuthentication("kmyatun@anzer.com", "anzerkaungmyat1994@");
//                        }
//                    });
//                Log.e("text message", msg.getText().toString() );
//                Log.e("sender message", sender_name.getText().toString());
//
//                    if(Validation.sendFeedbackChecker()) {
//                        if(haveNetworkConnection()) {
//                            RetreiveFeedTask task = new RetreiveFeedTask();
//                            task.execute();
//                        }
//                        else{
//                            Toast.makeText(MainActivity.this, "Connection isn't available", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//            }
//        } );
//
//        cancel.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                feedback_Dialog.cancel();
//            }
//        } );
//        feedback_Dialog.setCanceledOnTouchOutside(false);
//        feedback_Dialog.show();
//    }

    // contact dialog
    public void setup_contact_us_Dialog() {
        contact_us_Dialog = new Dialog(MainActivity.this);
        contact_us_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        contact_us_Dialog.setContentView(R.layout.contact_us_dialog);
        contact_us_Dialog.setCanceledOnTouchOutside(true);
        contact_us_Dialog.show();
    }

    public void setup_About_Dialog() {
        about_Dialog = new Dialog(MainActivity.this);
        about_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        about_Dialog.setContentView(R.layout.about_dialog);
        about_Dialog.setTitle("About Dialog");
        about_Dialog.setCanceledOnTouchOutside(true);
        about_Dialog.show();
    }

    // do login AsyncTask
    public class DoLogin extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(MainActivity.this);
            pd.setMessage("Please wait ... ");
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String login_username = login_username2.getText().toString();
            String login_password = login_user_password2.getText().toString();
            Log.e("username and password", login_password + "-" + login_username);
            // get data using retrofit
            ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
            if (_isDoctorLoginFlag) {
                Call<ArrayList<DocLoginData>> call = apiInterface.DoctorLogin(login_username, login_password, 1);
//                Log.e("call result", call.toString());
                call.enqueue(new Callback<ArrayList<DocLoginData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<DocLoginData>> call, Response<ArrayList<DocLoginData>> response) {
                        Log.e("onResponse", "Done this class");
                        if (response.isSuccessful() && response.body().size() != 0) {
                            z = "Login successful";
                            isSuccess = true;
                            Log.e("result", String.valueOf(response.body().get(0).getDocSurname()));
                            doctor_code = response.body().get(0).getDocCode();
                            doctor_name = response.body().get(0).getDocSurname();
                            copy_doctor_code = response.body().get(0).getDocCode();
                            copy_doctor_name = response.body().get(0).getDocSurname();
                        } else {
                            isSuccess = false;
                            pd.dismiss();
                            z = "Invalid username and password";
                        }
//                        Log.e("doctor_code", doctor_code);
                        Toast.makeText(MainActivity.this, z, Toast.LENGTH_LONG).show();
                        if (isSuccess && _isDoctorLoginFlag) {
                            Intent myIntent = new Intent(MainActivity.this, Doctor_Dashboard.class);
                            myIntent.putExtra("doctor_code", doctor_code);
                            myIntent.putExtra("doctor_name", doctor_name);
                            startActivityForResult(myIntent, 0);
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<DocLoginData>> call, Throwable t) {
                        Log.e("onFailure", "Done this class");
                        pd.dismiss();
                        z = call.toString();

                        Log.e("OnFailure Error", z);
                    }
                });
            } else {
                Call<ArrayList<NurseLoginData>> call = apiInterface.NurseLogin(login_username, login_password, 2);
                call.enqueue(new Callback<ArrayList<NurseLoginData>>() {
                    @Override
                    public void onResponse(Call<ArrayList<NurseLoginData>> call, Response<ArrayList<NurseLoginData>> response) {
                        if (response.isSuccessful() && response.body().size() != 0) {
                            z = "Login successful";
                            isSuccess = true;
                            nurse_code = response.body().get(0).getUserID();
                            nurse_name = response.body().get(0).getUserName();
                        } else {
                            isSuccess = false;
                            pd.dismiss();
                            z = "Invalid Username and Password";
                        }
                        Toast.makeText(MainActivity.this, z, Toast.LENGTH_LONG).show();
                        if (isSuccess && (!_isDoctorLoginFlag)) {
                            Intent myIntent = new Intent(MainActivity.this, NurseDashboard.class);
                            myIntent.putExtra("nurse_code", nurse_code);
                            myIntent.putExtra("nurse_name", nurse_name);
                            startActivityForResult(myIntent, 0);
                            pd.dismiss();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<NurseLoginData>> call, Throwable t) {
                        pd.dismiss();
                        Log.e("error", call.toString());
                        z = call.toString();
                    }
                });
            }
            return z;
        }

        @Override
        protected void onPostExecute(String s) {
            if (!isSuccess) {
                login_user_password2.getText().clear();
            }
        }
    }

    // check network is available or not
    public boolean isConnected() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(MainActivity.this, "Check Your WiFi Connection", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    // check remember_me checkbox is checked or not . Save username
    protected boolean remember_me_isChecked() {
        boolean check = false;
        remember_me_checkbox = findViewById(R.id.remember_username);
        if (remember_me_checkbox.isChecked()) {
            copy_username = login_username2.getText().toString();
            loginPrefsEditor.putBoolean("saveLogin", true);
            loginPrefsEditor.putString("username", copy_username);
            loginPrefsEditor.commit();
            check = true;
        }
        return check;
    }

    protected boolean check_institution_field() {
        NullInstiField = false;
        institution_field = findViewById(R.id.txt_institution_field);

        List<String> items = Arrays.asList(getResources().getStringArray(R.array.institution_values));

        Log.e("value of institution", institution_field.getText().toString());
        if (institution_field.getText().toString().equals("")) {
            Dialog dialog = new Dialog(MainActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_with_one_button);

            TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
            dialogBody.setText("You need to setup institution value !!! ");

            Button ok_btn = dialog.findViewById(R.id.ok_btn);

            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            NullInstiField = false;
        } else {
            if (institution_field.getText().toString().equals("City Hospital Mandalay")) {
                insti_code = items.get(0);
            } else if (institution_field.getText().toString().equals("Nyein")) {
                insti_code = items.get(1);
            } else {
                insti_code = items.get(2);
            }
            NullInstiField = true;
        }
        return NullInstiField;
    }

    //send feedback asynctask
    class RetreiveFeedTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            pdialog = ProgressDialog.show(context, "", "Sending Mail...", true);
        }

        @Override
        protected String doInBackground(String... params) {
//                try {
//                        Date c = Calendar.getInstance().getTime();
//                        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
//                        String formattedTodayDate = df.format(c);
//                        Message message = new MimeMessage(session);
//                        message.setFrom(new InternetAddress("kmyatun@anzer.com"));
//                        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
//                        message.setSubject("Feedback");
//                        copytextMessage = sender_name.getText().toString() + System.getProperty ("line.separator") + msg.getText().toString();
//                        Log.e("feedback string >>> ", copytextMessage);
//
//                        BodyPart messageBodyPart = new MimeBodyPart();
//                        Multipart multipart = new MimeMultipart();
//
//                        messageBodyPart.setText(sender_name.getText().toString() + "\n" + formattedTodayDate +"\n \n" + "Dear Anzer's Android Team," + "\n\n" + msg.getText().toString() + "\n\n" );
//
//                        multipart.addBodyPart(messageBodyPart);
//                        message.setContent(multipart);
//
//                        Transport.send(message);
//                } catch (MessagingException e) {
//                    e.printStackTrace();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            msg.setText("");
            sender_name.setText("");
            feedback_Dialog.dismiss();
            Toast.makeText(getApplicationContext(), "Message sent successfully", Toast.LENGTH_LONG).show();
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            setup_Exit_Dialog();
        }
    }
}
