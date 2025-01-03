package me.deema.sdk.data.di

import android.content.Context
import me.deema.sdk.data.AppData
import me.deema.sdk.data.AppConstants
import me.deema.sdk.data.domian.models.ApiResponseModel
import me.deema.sdk.data.extensions.fromPrettyJson
import me.deema.sdk.util.Event
import me.deema.sdk.util.EventBus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException
import timber.log.Timber

class ApiInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        requestBuilder.addHeader("source", "sdk")
        requestBuilder.addHeader("Authorization", "Basic ${AppData.getInstance().getSharedData().sdkKey}")

        try {
            val response = chain.proceed(requestBuilder.build())
            Timber.i("API Response Code -> ${response.code}")

            if(response.code != 200){
                handleError(response)
            }
            return response
        }catch (e: IOException){
            Timber.d("Exception: $e")
            handleIOError(e)
            throw e
        }
    }

    private fun handleError(response: Response) {
        try {
            var errMessage = AppConstants.generalError
            val responseBodyCopy = response.peekBody(Long.MAX_VALUE);
            val jsonStr = responseBodyCopy.string()

            if(!jsonStr.isNullOrEmpty()){
                val apiResponse = jsonStr?.fromPrettyJson<ApiResponseModel>()
                if(apiResponse != null){
                    errMessage = apiResponse?.message ?: (apiResponse?.error ?: AppConstants.generalError)
                }
            }

            Timber.d("API Error: $errMessage")

            CoroutineScope(Dispatchers.IO).launch{
                EventBus.sendEvent(Event.Error(message = errMessage))
            }
        }catch (e: Exception){
            Timber.d("Exception: $e")
            throw e
        }

    }

    private fun handleIOError(e: IOException) {
        var errMessage = AppConstants.generalError

        CoroutineScope(Dispatchers.Default).launch{
            EventBus.sendEvent(Event.Error(message = errMessage))
        }
    }
}