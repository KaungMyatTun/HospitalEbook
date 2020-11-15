package com.anzer.hospitalebook;

/**
 * Created by David on 5/15/2018.
 */

//public class Get_Data_For_Doctor_Dashboard {
//    public static ArrayList<Doctor_Dashboard_Data> dashboard_data;
//    public static ArrayList<Doctor_Dashboard_Data> getDashboardData(){
//        Connection con;
//        dashboard_data = new ArrayList<>();
//        String A ="AAA";
//        String queue_number, room_number;
//        String doctor_code = Doctor_Dashboard.doctor_code;
//
//        Log.e("doctor code", doctor_code);
//        try {
//            con = DBConnection.getConnectionClass(MainActivity.DB_username, MainActivity.DB_password, MainActivity.DB_NAME, MainActivity.Server_ip);
//            CallableStatement stmt = con.prepareCall("{call dbo.usp_HW_GetDoctorPatients(?, ?)}");
//            stmt.setString(1,doctor_code);
//            stmt.setString(2, MainActivity.insti_code);
//            ResultSet rs = stmt.executeQuery();
//            while (rs.next()){
//                Doctor_Dashboard_Data getDashboardData = new Doctor_Dashboard_Data(rs.getString(1) , rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8));
//                dashboard_data.add(getDashboardData);
//            }
//        }catch (SQLException se){
//            Log.e("get doctor exception" , se.getMessage());
//        }
//        catch (Exception ex){
//            Log.e("error here  3" , ex.getMessage());
//        }
//        return dashboard_data;
//    }
//}
