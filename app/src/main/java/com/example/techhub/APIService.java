package com.example.techhub;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAA2AbRODs:APA91bG6bkuj-e34eXtYjF-GBZ8wYyVu2kVOtxejfvKK4oMgqdK8gv4fhZDZtXIuqrlSSkuCUkwwgFyVwhPp273-TASwXsQvsZkYibYuT5SEjp5jwuoWfOwQT2v6_svfUsVEcXbors4G" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
