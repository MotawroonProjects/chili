package com.chili_driver.services;

import com.chili_driver.models.MyOrderDataModel;
import com.chili_driver.models.OrderCountModel;
import com.chili_driver.models.OrderModel;
import com.chili_driver.models.RestaurantSettingModel;
import com.chili_driver.models.SingleOrderModel;
import com.chili_driver.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface Service {

    @FormUrlEncoded
    @POST("api/login_driver")
    Call<UserModel> login(@Field("email") String email,
                          @Field("password") String password

    );

    @FormUrlEncoded
    @POST("api/register_driver")
    Call<UserModel> signUpWithoutImage(@Field("name") String name,
                                       @Field("phone_code") String phone_code,
                                       @Field("phone") String phone,
                                       @Field("email") String email,
                                       @Field("password") String password
    );

    @Multipart
    @POST("api/register_driver")
    Call<UserModel> signUpWithImage(@Part("name") RequestBody name,
                                    @Part("phone_code") RequestBody phone_code,
                                    @Part("phone") RequestBody phone,
                                    @Part("email") RequestBody email,
                                    @Part("password") RequestBody password,
                                    @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/restaurant-update-profile")
    Call<UserModel> updateProfileWithoutImage(@Header("Authorization") String bearer_token,
                                              @Field("user_id") String user_id,
                                              @Field("name") String name,
                                              @Field("email") String email,
                                              @Field("password") String password
    );

    @Multipart
    @POST("api/restaurant-update-profile")
    Call<UserModel> updateProfileWithImage(@Header("Authorization") String bearer_token,
                                           @Part("user_id") RequestBody user_id,
                                           @Part("name") RequestBody name,
                                           @Part("email") RequestBody email,
                                           @Part("password") RequestBody password,
                                           @Part MultipartBody.Part logo


    );

    @FormUrlEncoded
    @POST("api/logout_driver")
    Call<UserModel> logout(@Field("client_id") String client_id,
                           @Field("token") String token
    );

    @FormUrlEncoded
    @POST("api/inser_driver_token")
    Call<UserModel> updatePhoneToken(@Field("phone_token") String phone_token,
                                     @Field("user_id") int user_id,
                                     @Field("type") String software_type
    );

    @GET("api/driver_new_orders")
    Call<MyOrderDataModel> getNewOrder(@Query("driver_id") int driver_id,
                                       @Query("driver_lat") double driver_lat,
                                       @Query("driver_long") double driver_long
    );

    @GET("api/driver_current_orders")
    Call<MyOrderDataModel> getCurrentOrder(@Query("driver_id") int driver_id,
                                           @Query("driver_lat") double driver_lat,
                                           @Query("driver_long") double driver_long
    );

    @GET("api/driver_ended_orders")
    Call<MyOrderDataModel> getPreviousOrder(@Query("driver_id") int driver_id,
                                            @Query("driver_lat") double driver_lat,
                                            @Query("driver_long") double driver_long
    );


    @GET("api/one_order")
    Call<SingleOrderModel> getOrderById(@Query("order_id") int order_id,
                                        @Query("driver_id") int driver_id

    );

    @FormUrlEncoded
    @POST("api/accept_order")
    Call<OrderModel> acceptOrder(@Field("order_id") int order_id,
                                 @Field("driver_id") int driver_id
    );

    @FormUrlEncoded
    @POST("api/refuse_order")
    Call<ResponseBody> refuseOrder(@Field("order_id") int order_id,
                                   @Field("driver_id") int driver_id
    );

    @FormUrlEncoded
    @POST("api/end_order")
    Call<ResponseBody> endOrder(@Field("order_id") int order_id,
                                 @Field("driver_id") int driver_id
    );
}