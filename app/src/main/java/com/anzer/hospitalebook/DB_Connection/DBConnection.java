package com.anzer.hospitalebook.DB_Connection;

import android.os.StrictMode;
import android.util.Log;

import com.anzer.hospitalebook.MainActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


/**
 * Created by David on 5/9/2018.
 */

public class DBConnection {
    public static Connection getConnectionClass (String dbusername, String dbpassword, String database, String serverip){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL;
        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + serverip + ":" + MainActivity.DB_port + "/" + database + ";user=" + dbusername + ";password=" + dbpassword;
            connection = DriverManager.getConnection (ConnectionURL);
        }catch (SQLException se){
            Log.e("error here 1" , se.getMessage());
        }
        catch (ClassNotFoundException e){
            Log.e("error here 2" , e.getMessage());
        }
        catch (Exception ex){
            Log.e("error here  3" , ex.getMessage());
        }
        return connection;
    }
}
