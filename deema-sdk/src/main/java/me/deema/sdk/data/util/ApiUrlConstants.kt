package me.deema.sdk.data.util

import me.deema.sdk.Environment
import me.deema.sdk.data.AppData

internal object BaseUrl {
    const val BASE_SANDBOX_URL = "https://sandbox-api.deema.me"
    const val BASE_PROD_URL = "https://api.deema.me"
}

internal object EndPoint {
    const val createPurchase = "/api/merchant/v1/purchase"
}

fun baseUrl():String{
    return if (AppData.getInstance().getSharedData().environment == Environment.Sandbox) BaseUrl.BASE_SANDBOX_URL else BaseUrl.BASE_PROD_URL
}
