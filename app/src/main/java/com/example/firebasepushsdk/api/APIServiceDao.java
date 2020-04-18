package com.example.firebasepushsdk.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIServiceDao {
    @POST(ConstantApi.API_GET_INFO)
    @FormUrlEncoded
    Call<ResponseBase> getInfoToken(@Field("deviceName") String deviceName,
                                    @Field("serialNumber") String serialNumber,
                                    @Field("operationSystem") String operationSystem,
                                    @Field("versionCode") String versionCode,
                                    @Field("versionBuild") String versionBuild,
                                    @Field("deviceType") String deviceType,
                                    @Field("bundleID") String bundleID,
                                    @Field("fcmToken") String fcmToken);

    @POST(ConstantApi.API_UPDATE_TOKEN)
    @FormUrlEncoded
    Call<ResponseBase> updateToken(@Field("serialNumber") String serialNumber,
                                   @Field("fcmToken") String fcmToken);
}
