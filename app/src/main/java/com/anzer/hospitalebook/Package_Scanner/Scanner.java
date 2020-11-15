package com.anzer.hospitalebook.Package_Scanner;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.anzer.hospitalebook.Doctor_Dashboard;
import com.anzer.hospitalebook.NurseDashboard.NurseDashboard;
import com.anzer.hospitalebook.R;
import com.anzer.hospitalebook.models.views.OnDecodedCallback;
import com.anzer.hospitalebook.models.views.ScannerView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class Scanner extends AppCompatActivity implements OnDecodedCallback {
    private static final int CAMERA_PERMISSION_REQUEST = 0xabc;
    private ScannerView mScanner;
    private boolean mPermissionGranted;
    public static Intent myIntent;
    String PageFrom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scanner_activity);

        mScanner = (ScannerView) findViewById(R.id.scanner);
        FrameLayout fl = findViewById(R.id.container);
        mScanner.setOnDecodedCallback(this);

        // page from = 2, it is from nurse dashboard. Otherwise, it is from doctor dashboard
        PageFrom = getIntent().getStringExtra("PageFrom");

        // get permission
        int cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        mPermissionGranted = cameraPermission == PackageManager.PERMISSION_GRANTED;
        if (!mPermissionGranted) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_PERMISSION_REQUEST && grantResults.length > 0) {
            mPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    @Override
    public void onDecoded(String decodedData) {
        Toast.makeText(this, decodedData, Toast.LENGTH_SHORT).show();
        if (decodedData != null) {
            mScanner.stopScanBarcode();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(400);
            showAlertDialog(decodedData);
        }
    }

    private void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Barcode Scan Result")
                .setCancelable(false)
                .setMessage("Result : " + message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onBackPressed();
                        if (PageFrom.equals("ND")) {
                            NurseDashboard.search_view_query = message;
                        } else
                            Doctor_Dashboard.search_view_query = message;
                    }
                })
                .setNegativeButton("Re-Scan", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mScanner.startScanning();
                    }
                })
                .setIcon(R.drawable.barcode_noti_alert)
                .show();
    }

    @Override
    public void onBackPressed() {
        finish();
        if (PageFrom.equals("ND")) {
            Intent myIntent = new Intent(this, NurseDashboard.class);
            startActivityForResult(myIntent, 0);
        } else {
            Intent myIntent = new Intent(this, Doctor_Dashboard.class);
            startActivityForResult(myIntent, 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPermissionGranted) {
            mScanner.startScanning();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScanner.stopScanning();
    }
}
