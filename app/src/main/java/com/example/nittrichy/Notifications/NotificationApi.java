package com.example.nittrichy.Notifications;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface NotificationApi {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAc6bZe-g:APA91bGNQQd39MwfPpVISbd50TP9T8kAlsdL_Js8VcezwODy7AQVsljbbQqfm03aQBY3sR_iFyC-L-bSqeG250xp2R32foqz4l-2dsqsnljDAmTsO7yWFIwrz98lmFAWDfYVwsZ-H03_"
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotificattion(@Body PushNotification body);
}
