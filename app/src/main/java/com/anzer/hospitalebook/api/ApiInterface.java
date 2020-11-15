package com.anzer.hospitalebook.api;

import com.anzer.hospitalebook.vo.ConsumableItem;
import com.anzer.hospitalebook.vo.DIGroupModel;
import com.anzer.hospitalebook.vo.DIItem;
import com.anzer.hospitalebook.vo.DIProceduresModel;
import com.anzer.hospitalebook.vo.DocLoginData;
import com.anzer.hospitalebook.vo.Doctor_Dashboard_Data;
import com.anzer.hospitalebook.vo.HWImage;
import com.anzer.hospitalebook.vo.HWPtInfo;
import com.anzer.hospitalebook.vo.ImageCount;
import com.anzer.hospitalebook.vo.LabItem;
import com.anzer.hospitalebook.vo.LabJson;
import com.anzer.hospitalebook.vo.NurseDashboardData;
import com.anzer.hospitalebook.vo.NurseLoginData;
import com.anzer.hospitalebook.vo.PHItem;
import com.anzer.hospitalebook.vo.PtInfoDetail;
import com.anzer.hospitalebook.vo.PtVisitHistory;
import com.anzer.hospitalebook.vo.VisitItem;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("OPDWaitList")
    Call<ArrayList<NurseDashboardData>> getOPDWaitList(@Query("hospInsti") String hospitalInstitution, @Query("filterDate") String filterDate);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @POST("InsertPtInfo")
    Call<Integer> insertPatientInfo(@Body HWPtInfo hwPtInfo);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("getLastRowOfHWPtInfo")
    Call<Integer> getLastRowOfHWPtInfo();

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @POST("insertHWImage")
    Call<String> insertHWImage(@Body HWImage hwImage);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("getInsertedImageCount")
    Call<ArrayList<ImageCount>> getImgCount(@Query("id") Integer img);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @POST("Login")
    Call<ArrayList<DocLoginData>> DoctorLogin(@Query("userID") String userID, @Query("password") String userPassword, @Query("LoginType") Integer type);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @POST("Login")
    Call<ArrayList<NurseLoginData>> NurseLogin(@Query("userID") String userID, @Query("password") String userPassword, @Query("LoginType") Integer type);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("DocDashboard")
    Call<ArrayList<Doctor_Dashboard_Data>> GetDoctorDashboardData(@Query("docCode") String docCode, @Query("hwInsti") String hwInsti);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetAllVisit")
    Call<ArrayList<VisitItem>> GetVisitItemByCPI(@Query("ptCpi") String cpi);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetOrderItem")
    Call<ArrayList<PHItem>> GetPHOrderItem(@Query("ptCPI") String ptCPI, @Query("hwInsti") String hwInsti, @Query("visit") String visit, @Query("category") String category);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetOrderItem")
    Call<ArrayList<ConsumableItem>> GetConsumableOrderItem(@Query("ptCPI") String ptCPI, @Query("hwInsti") String hwInsti, @Query("visit") String visit, @Query("category") String category);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetOrderItem")
    Call<ArrayList<LabItem>> GetLabOrderItem(@Query("ptCPI") String ptCPI, @Query("hwInsti") String hwInsti, @Query("visit") String visit, @Query("category") String category);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetOrderItem")
    Call<ArrayList<DIItem>> GetDIOrderItem(@Query("ptCPI") String ptCPI, @Query("hwInsti") String hwInsti, @Query("visit") String visit, @Query("category") String category);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetPatientInfo")
    Call<ArrayList<PtInfoDetail>> GetPtInfoDetail(@Query("ptCPI") String ptCPI, @Query("hwInsti") String hwInsti, @Query("ptReg") String ptReg);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetPatientVisit")
    Call<ArrayList<PtVisitHistory>> GetPtVisitHistory(@Query("ptCPI") String ptCPI, @Query("hwInsti") String hwInsti);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @POST("CreateProgressNote")
    Call<String> CreateProgressNote(@Query("patientID") Integer patientID, @Query("docCode") String docCode, @Query("regNum") String regNum, @Query("hwInsti") String hwInsti);

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetDIProcedureGroup")
    Call<ArrayList<DIGroupModel>> GetAllDIGroup();

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetDIProcedures")
    Call<ArrayList<DIProceduresModel>> GetAllDIProcedures();

    @Headers({"APIKey:" + "Anzer_HW_App_2020"})
    @GET("GetLabTest")
    Call<ArrayList<LabJson>> GetLABTest();

}
