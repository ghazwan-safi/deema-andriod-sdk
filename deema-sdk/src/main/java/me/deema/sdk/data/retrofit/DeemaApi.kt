package me.deema.sdk.data.retrofit

import me.deema.sdk.data.util.EndPoint
import me.deema.sdk.data.domian.models.PurchaseOrderRequest
import me.deema.sdk.data.domian.models.PurchaseRequestResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface DeemaApi {
    @POST(EndPoint.merchant)
    suspend fun getPurchaseOrder(
        @Body request: PurchaseOrderRequest
    ): PurchaseRequestResponseModel
}