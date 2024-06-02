package com.santhi.collegenetwork.businessLogic.notification

import com.santhi.collegenetwork.businessLogic.notification.Constants.Companion.CONTENT_TYPE
import com.santhi.collegenetwork.businessLogic.notification.Constants.Companion.SERVER_KEY
import retrofit2.Response
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface  NotificationAPI {
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotifications
    ): Response<ResponseBody>
}