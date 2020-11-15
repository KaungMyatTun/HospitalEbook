package com.anzer.hospitalebook.Package_Validation;


import com.anzer.hospitalebook.MainActivity;

/**
 * Created by David on 5/9/2018.
 */

public class Validation {
    public static boolean login_validation(){
        boolean login_validate = false;
        if(MainActivity.login_username2.getText().toString().length() == 0 && MainActivity.login_user_password2.getText().toString().length() == 0){
            login_validate = false;
            MainActivity.login_username2.setError("Required !");
            MainActivity.login_user_password2.setError("Required !");
            MainActivity.login_user_password.setPasswordVisibilityToggleEnabled(false);
        }
        else if(MainActivity.login_username2.getText().toString().length() == 0 && MainActivity.login_user_password2.getText().toString().length() != 0){
            login_validate = false;
            MainActivity.login_username2.setError("Required !");
        }
        else if(MainActivity.login_username2.getText().toString().length() != 0 && MainActivity.login_user_password2.getText().toString().length() == 0){
            login_validate = false;
            MainActivity.login_user_password2.setError("Required !");
            MainActivity.login_user_password.setPasswordVisibilityToggleEnabled(false);
        }
        else{
            login_validate = true;
            MainActivity.login_user_password.setPasswordVisibilityToggleEnabled(false);
        }

        return login_validate;
    }

    public static boolean sendFeedbackChecker() {
        boolean check = false;
        if(MainActivity.msg.getText().toString().length() == 0 && MainActivity.sender_name.getText().toString().length() == 0){
            MainActivity.msg.setError("Required !");
            MainActivity.sender_name.setError("Required !");
            check = false;
        }
        else if(MainActivity.msg.getText().toString().length() == 0 && MainActivity.sender_name.getText().toString().length() != 0){
            check = false;
            MainActivity.msg.setError("Required !");
        }
        else if(MainActivity.sender_name.getText().length() == 0 && MainActivity.msg.getText().toString().length() != 0){
            check = false;
            MainActivity.sender_name.setError("Required !");
        }
        else{
            check = true;
        }
        return check;
    }

}
