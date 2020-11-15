package com.anzer.hospitalebook;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by David on 8/30/2018.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resetDisconnectTimer();
//        Log.e("Time for session Timeout ", String.valueOf(MainActivity.COPY_DISCONNECT_TIMEOUT) );
//        Toast.makeText(this, String.valueOf(MainActivity.DISCONNECT_TIMEOUT), Toast.LENGTH_SHORT).show();
    }

//    public static  long DISCONNECT_TIMEOUT = 30000; // 30 sec = 30 * 1000 ms

    private Handler disconnectHandler = new Handler() {
        public void handleMessage(Message msg) {
        }
    };

    private Runnable disconnectCallback = new Runnable() {
        @Override
        public void run() {
            View v = View.inflate(BaseActivity.this, R.layout.dialog_for_session_expire, null);

            Dialog dialog = new Dialog(BaseActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_for_session_expire);
            dialog.setCancelable(false);

            TextView dialogBody = dialog.findViewById(R.id.dialog_body_txt);
            dialogBody.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            dialogBody.setText("Your Session has expired !" + System.lineSeparator() + "Please exit application and login again to proceed.");


            Button ok_btn = dialog.findViewById(R.id.ok_btn);

            ok_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BaseActivity.this,
                                MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
                                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, 0);
                        dialog.cancel();
                        Doctor_Dashboard doctor_dashboard = new Doctor_Dashboard();
                        doctor_dashboard.finish();
                }
            });

            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
    };

    public void resetDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
        disconnectHandler.postDelayed(disconnectCallback, Integer.valueOf(MainActivity.DISCONNECT_TIMEOUT));
    }

    public void stopDisconnectTimer() {
        disconnectHandler.removeCallbacks(disconnectCallback);
    }

    @Override
    public void onUserInteraction() {
        resetDisconnectTimer();
    }

    @Override
    public void onStop() {
        super.onStop();
        stopDisconnectTimer();
    }
}
