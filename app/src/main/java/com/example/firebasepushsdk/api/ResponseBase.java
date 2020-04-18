package com.example.firebasepushsdk.api;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseBase {
    @SerializedName("result")
    @Expose
    public Integer result;

    @SerializedName("now_dt")
    @Expose
    public String nowDt;

    @SerializedName("data")
    @Expose
    public @Nullable
    Object data;

    @SerializedName("data/user_info")
    @Expose
    public @Nullable
    Object dataInfo;

    @SerializedName("err_cd")
    @Expose
    public Object errCd;

    @SerializedName("err_msg")
    @Expose
    public Object errMsg;

    @SerializedName("version")
    @Expose
    public @Nullable
    Object version;

    @SerializedName("book_time")
    @Expose
    public String bookTime;


    public ResponseBase() {
    }
}
