package com.chili_driver.services;

import com.chili_driver.models.MyOrderDataModel;
import com.chili_driver.models.OrderCountModel;
import com.chili_driver.models.RestaurantSettingModel;
import com.chili_driver.models.SingleOrderModel;
import com.chili_driver.models.UserModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    @POST("api/restaurant-login")
    Call<UserModel> login(@Field("email") String email,
                          @Field("password") String password

    );

    @FormUrlEncoded
    @POST("api/restaurant-register")
    Call<UserModel> signUpWithoutImage(@Field("name") String name,
                                       @Field("email") String email,
                                       @Field("password") String password
    );

    @Multipart
    @POST("api/restaurant-register")
    Call<UserModel> signUpWithImage(@Part("name") RequestBody name,
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
    @POST("api/restaurant-logout")
    Call<UserModel> logout(@Header("Authorization") String user_token,
                              @Field("user_id") String user_id,
                              @Field("phone_token") String phone_token
    );
    @FormUrlEncoded
    @POST("api/firebase-tokens")
    Call<UserModel> updatePhoneToken(@Header("Authorization") String user_token,
                                        @Field("phone_token") String phone_token,
                                        @Field("user_id") int user_id,
                                        @Field("software_type") String software_type
    );
    @GET("api/orders")
    Call<MyOrderDataModel> getMyOrder(@Header("Authorization") String user_token,
                                      @Query("restaurant_id") int restaurant_id,
                                      @Query("order_status") String order_status,
                                      @Query("orderBy") String orderBy
    );

    @GET("api/GetRestaurantOrdersCount")
    Call<OrderCountModel> getOrdersCount(@Header("Authorization") String user_token,
                                         @Query("restaurant_id") int restaurant_id
    );

    @FormUrlEncoded
    @POST("api/restaurant-create-order")
    Call<SingleOrderModel> sendOrder(@Header("Authorization") String user_token,
                                     @Field("restaurant_id") String restaurant_id

    );

    @FormUrlEncoded
    @POST("api/restaurant-delete-order")
    Call<SingleOrderModel> skipOrder(@Header("Authorization") String user_token,
                                       @Field("order_id") String order_id,
                                       @Field("barcode") String barcode,
                                       @Field("barcode_image") String barcode_image


    );
    @FormUrlEncoded
    @POST("api/restaurant-finish-order")
    Call<SingleOrderModel> finishOrder(@Header("Authorization") String user_token,
                                     @Field("order_id") String order_id


    );
    @GET("api/restaurant-setting")
    Call<RestaurantSettingModel> getRestaurantSetting(@Header("Authorization") String user_token,
                                                      @Query("restaurant_id") int restaurant_id
    );
    @FormUrlEncoded
    @POST("api/restaurant-order-settings")
    Call<RestaurantSettingModel> changeRestaurantTime(@Header("Authorization") String user_token,
                                                      @Field("restaurant_id") int restaurant_id,
                                                      @Field("order_time_preparing") String order_time_preparing,
                                                      @Field("order_first_num") String order_first_num


    );
}