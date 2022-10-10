package com.diamond.future.utility;

import com.diamond.future.model.AboutUsModel;
import com.diamond.future.model.ItemsSender;
import com.diamond.future.model.LoginModel;
import com.diamond.future.model.UserProfile;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RequestApi {
    @FormUrlEncoded
    @POST("login")
    Call<LoginModel> getlogin(@Field("email") String email, @Field("password") String password, @Field("deviceid") String deviceid, @Field("devicename") String devicename);

    @FormUrlEncoded
    @POST("registration")
    Call<LoginModel> getSignup(@Field("user_name") String name, @Field("user_email") String email
            , @Field("user_phonenumber") String phone, @Field("user_password") String password,
                               @Field("confirm_password") String password_confirmation,
                               @Field("devicename") String device_name, @Field("deviceVersion") String device_version, @Field("deviceid") String deviceid);

    @FormUrlEncoded
    @POST("socialLogin")
    Call<LoginModel> getSocilLoginGmail(@Field("dataId") String dataId,
                                        @Field("user_name") String user_name,
                                        @Field("user_email") String user_email,
                                        @Field("user_phonenumber") String user_phonenumber,
                                        @Field("deviceid") String deviceid,
                                        @Field("devicename") String devicename,
                                        @Field("deviceVersion") String deviceVersion,
                                        @Field("user_profilepic") String user_profilepic);

    @FormUrlEncoded
    @POST("fbLoginAPIcheck")
    Call<LoginModel> getSocilLoginFacebook(@Field("dataId") String dataId,
                                        @Field("user_name") String user_name,
                                        @Field("user_email") String user_email,
                                        @Field("user_phonenumber") String user_phonenumber,
                                        @Field("deviceid") String deviceid,
                                        @Field("devicename") String devicename,
                                        @Field("deviceVersion") String deviceVersion,
                                        @Field("user_profilepic") String user_profilepic);


    @FormUrlEncoded
    @POST("forgetPassword")
    Call<LoginModel> getEmailVerify(@Field("user_email") String email);

    @POST("aboutus")
    Call<AboutUsModel> getAboutus();

    @POST("price")
    Call<AboutUsModel> getPriceSetting();

    @POST("terms")
    Call<AboutUsModel> getTermsAndCondition();

    @POST("privacy")
    Call<AboutUsModel> getPrivacyPolicy();

    @POST("viewProfile")
    Call<UserProfile> getEditProfile(@Header("AuthKey") String AuthKey);

    @POST("itemsSender")
    Call<ItemsSender> getSentItems(@Header("AuthKey") String AuthKey);

    @POST("itemsReceiver")
    Call<ItemsSender> getReceiveItems(@Header("AuthKey") String AuthKey);

    @FormUrlEncoded
    @POST("deleteitemAfterView")
    Call<ItemsSender> getdeleteItems(@Header("AuthKey") String AuthKey, @Field("itemId") String itemId);

    @Multipart
    @POST("changePassword")
    Call<LoginModel> getchangepass(@Part("oldpassword") RequestBody oldpassword, @Part("newpassword") RequestBody newpassword,
                                   @Part("confirmpassword") RequestBody confirmpassword, @Header("AuthKey") String AuthKey);

    @Multipart
    @POST("editProfile")
    Call<UserProfile> setEditProfile(@Part MultipartBody.Part user_profilepic,
                                     @Part("user_name") RequestBody user_name,
                                     @Part("user_phonenumber") RequestBody user_phonenumber,
                                     @Part("deviceid") RequestBody deviceid,
                                     @Part("devicename") RequestBody devicename,
                                     @Part("deviceVersion") RequestBody deviceVersion,
                                     @Part("aboutus") RequestBody aboutus,
                                     @Part("location") RequestBody location,
                                     @Header("AuthKey") String AuthKey);

    @Multipart
    @POST("uploads")
    Call<UserProfile> uploadVideo(@Part MultipartBody.Part video,
                                  @Part("price") RequestBody price,
                                  @Part("latitude") RequestBody latitude,
                                  @Part("longitude") RequestBody longitude,
                                  @Part("price_token") RequestBody price_token,
                                  @Part("reciverEmailId1") RequestBody reciverEmailId1,
                                  @Part("reciverEmailId2") RequestBody reciverEmailId2,
                                  @Part("reciverEmailId3") RequestBody reciverEmailId3,
                                  @Part("reciveTime") RequestBody reciveTime,
                                  @Part("deviceId") RequestBody deviceId,
                                  @Part("reciveDate") RequestBody reciveDate,
                                  @Part("senderTimeZone") RequestBody senderTimeZone,
                                  @Header("AuthKey") String AuthKey);

    @Multipart
    @POST("editProfile")
    Call<UserProfile> setEditProfile(
            @Part("user_name") RequestBody user_name,
            @Part("user_phonenumber") RequestBody user_phonenumber,
            @Part("deviceid") RequestBody deviceid,
            @Part("devicename") RequestBody devicename,
            @Part("deviceVersion") RequestBody deviceVersion,
            @Part("aboutus") RequestBody aboutus,
            @Header("AuthKey") String AuthKey);

    @POST("deleteAccount")
    Call<UserProfile> deleteAccount(@Header("AuthKey") String AuthKey);

}
