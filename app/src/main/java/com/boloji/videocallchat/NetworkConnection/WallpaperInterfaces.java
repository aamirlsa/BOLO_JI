package com.boloji.videocallchat.NetworkConnection;

import com.boloji.videocallchat.ChatsModelClass.StrangerMeetModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WallpaperInterfaces {
    @FormUrlEncoded
    @POST("login.php")
    Call<StrangerMeetModel> getLoginInformation(@Field("user_device_id") String str, @Field("user_name") String str2);
}
