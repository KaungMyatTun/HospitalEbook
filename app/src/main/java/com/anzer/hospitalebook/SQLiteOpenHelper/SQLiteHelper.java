package com.anzer.hospitalebook.SQLiteOpenHelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by David on 6/12/2018.
 */

public class SQLiteHelper extends SQLiteOpenHelper {
    public static String DATABASE_NAME = "HW_APP";
    private static String DBLocation = "/data/data/com.anzer.hospitalebook/databases/";
    private SQLiteDatabase mdatabase;
    private static final int DATABASE_VERSION = 1;
    private Context mcontext;
    private static final String SP_Key_DB_Ver = "db_ver";


    public SQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, version);
        this.mcontext = context;

    }

    public void openDatabase() {
        String dbpath = mcontext.getDatabasePath(DATABASE_NAME).getPath();
        if(mdatabase != null && mdatabase.isOpen()){
            return;
        }
        mdatabase = SQLiteDatabase.openDatabase(dbpath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public void closeDB(){
        if (mdatabase != null) {
            mdatabase.close();
        }
    }

    public boolean copyDatabase(Context context){
        try{
            InputStream inputStream = context.getAssets().open(SQLiteHelper.DATABASE_NAME);
            String ourFileName = SQLiteHelper.DBLocation + SQLiteHelper.DATABASE_NAME;
            OutputStream outputStream = new FileOutputStream(ourFileName);
            byte [] buff = new byte[1024];
            int length = 0;
            while( (length = inputStream.read(buff)) > 0){
                outputStream.write(buff, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            Log.e("copy database", "copy database" );
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public void queryData(String sql){
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL(sql);
    }

    public void insertDoctorTable(String docCode, String doc_name){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO DOCTOR_TABLE VALUES(NULL, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, docCode);
        statement.bindString(2, doc_name);

        statement.executeInsert();
    }

    public Cursor getData(String sql){
        SQLiteDatabase database = getReadableDatabase();
        return database.rawQuery(sql, null);
    }

    public void insertHandwritingImg (String ad_code, String patient_name, String patient_cpi, String patient_visit_no, String patient_reg_date, String handwriting_img, String isPhotoORNot){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO HANDWRITING_IMG_TABLE VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, ad_code);
        statement.bindString(2, patient_name);
        statement.bindString(3, patient_cpi);
        statement.bindString(4, patient_visit_no);
        statement.bindString(5, patient_reg_date);
        statement.bindString(6, handwriting_img);
        statement.bindString(7, isPhotoORNot);

        statement.executeInsert();
    }

    public void insertPatientInfo (String ad_code, String patient_name, String patient_cpi, String patient_visit_no, String patient_reg_date){
        SQLiteDatabase database = getWritableDatabase();
        String sql = "INSERT INTO PATIENT_INFO_TABLE VALUES (NULL, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();

        statement.bindString(1, ad_code);
        statement.bindString(2, patient_name);
        statement.bindString(3, patient_cpi);
        statement.bindString(4, patient_visit_no);
        statement.bindString(5, patient_reg_date);

        statement.executeInsert();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    public void initialize(){
        File database = mcontext.getDatabasePath(SQLiteHelper.DATABASE_NAME);
        if(database.exists()){
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mcontext);
            int DB_version = pref.getInt(SP_Key_DB_Ver, 1);
            Log.e("Database version" , String.valueOf(DB_version));
            if(DATABASE_VERSION == DB_version){
                Log.e("Database version" , String.valueOf(DB_version));
                File dbFile = mcontext.getDatabasePath(DATABASE_NAME);
                if(!dbFile.delete()){
                    Log.e("testing", "initialize: ");
                }
            }
        }
    }
}
