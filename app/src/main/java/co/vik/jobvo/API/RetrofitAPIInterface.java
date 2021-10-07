package co.vik.jobvo.API;


import com.google.gson.JsonObject;

import co.vik.jobvo.pojo.Forgotmodel;
import co.vik.jobvo.pojo.HistoryModel;
import co.vik.jobvo.pojo.Loginmodel;
import co.vik.jobvo.pojo.MyqrModel;
import co.vik.jobvo.pojo.Notipojo;
import co.vik.jobvo.pojo.OrgainizationModel;
import co.vik.jobvo.pojo.Otpmodel;
import co.vik.jobvo.pojo.ProfileModel;
import co.vik.jobvo.pojo.RegisterModel;
import co.vik.jobvo.pojo.ResendOtpModel;
import co.vik.jobvo.pojo.ScannedModel;
import co.vik.jobvo.pojo.ThankuModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitAPIInterface {

    @POST("login.php")
    Call<Loginmodel> login(@Body JsonObject submitdetails);

    @POST("forgot_password.php")
    Call<Forgotmodel> forgotpass(@Body JsonObject submitdetails);

    @POST("otp_verify.php")
    Call<Otpmodel> otpverify(@Body JsonObject submitdetails);

    @POST("register.php")
    Call<RegisterModel> newregister(@Body JsonObject submitdetails);

    @POST("resend_otp.php")
    Call<ResendOtpModel> resendotp(@Body JsonObject submitdetails);

    @POST("rozerpay_payment.php")
    Call<ThankuModel> thankuorder(@Body JsonObject submitdetails);


    @Multipart
    @POST("update_profile.php")
    Call<ProfileModel> updateprofilepic(@Part MultipartBody.Part file,
                                        @Part MultipartBody.Part userid,
                                        @Part MultipartBody.Part name,
                                        @Part MultipartBody.Part gender,
                                        @Part MultipartBody.Part dob,
                                        @Part MultipartBody.Part mobile,
                                        @Part MultipartBody.Part email,
                                        @Part MultipartBody.Part city,
                                        @Part MultipartBody.Part state,
                                        @Part MultipartBody.Part distict,
                                        @Part MultipartBody.Part address,
                                        @Part MultipartBody.Part pincode,
                                        @Part MultipartBody.Part profileurl);

    @POST("myorderhistory.php")
    Call<HistoryModel> gethistory(@Body JsonObject submitdetails);

    @POST("scan_user.php")
    Call<JsonObject> insertscan(@Body JsonObject submitdetails);


    @POST("view_qr_code.php")
    Call<MyqrModel> getmyinfo(@Body JsonObject submitdetails);

    @POST("organisation_list.php")
    Call<OrgainizationModel> getorgainization(@Body JsonObject submitdetails);

    @POST("staff_scan_history.php")
    Call<ScannedModel> getscandata(@Body JsonObject submitdetails);

    @POST("notification.php")
    Call<Notipojo> getnotification(@Body JsonObject submitdetails);



}









