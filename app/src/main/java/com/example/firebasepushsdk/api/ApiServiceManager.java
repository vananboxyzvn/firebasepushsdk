package com.example.firebasepushsdk.api;

import com.example.firebasepushsdk.api.callback.CallBackInfo;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiServiceManager {
    private static ApiServiceManager INSTANCE;
    private APIServiceDao mApiServiceDao;

    private ApiServiceManager() {
        mApiServiceDao = new RetrofitClient().getAPIServiceBase();
    }

    public static ApiServiceManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ApiServiceManager();
        }
        return INSTANCE;
    }

    public void getInfoAppToken(final String deviceName,
                                final String serialNumber,
                                final String operationSystem,
                                final String versionCode,
                                final String versionBuild,
                                final String bundleID,
                                final String fcmToken,
                                final CallBackInfo callBack) {
        mApiServiceDao.getInfoToken(deviceName, serialNumber, operationSystem, versionCode,
                versionBuild, "0", bundleID, fcmToken).enqueue(new Callback<ResponseBase>() {
            @Override
            public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {
                if (response.errorBody() != null) {
                    callBack.onError();
                } else {
                    callBack.onComplete();

                }
            }

            @Override
            public void onFailure(Call<ResponseBase> call, Throwable t) {
                t.printStackTrace();
                callBack.onError();
            }
        });
    }

    public void updateToken(final String serialNumber,
                            final String fcmToken,
                            final CallBackInfo callBack) {
        mApiServiceDao.updateToken(serialNumber, fcmToken).enqueue(new Callback<ResponseBase>() {
            @Override
            public void onResponse(Call<ResponseBase> call, Response<ResponseBase> response) {
                if (response.errorBody() != null) {
                    callBack.onError();
                } else {
                    callBack.onComplete();

                }
            }

            @Override
            public void onFailure(Call<ResponseBase> call, Throwable t) {
                t.printStackTrace();
                callBack.onError();
            }
        });
    }
}
