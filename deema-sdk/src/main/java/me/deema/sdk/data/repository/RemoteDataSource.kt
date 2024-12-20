package me.deema.sdk.data.repository


import me.deema.sdk.data.domian.models.PurchaseOrderRequest
import me.deema.sdk.data.util.DataState
import me.deema.sdk.data.domian.models.PurchaseRequestResponseModel
import kotlinx.coroutines.flow.Flow

interface RemoteDataSource {
    suspend fun getPurchaseOrder(request: PurchaseOrderRequest): Flow<DataState<PurchaseRequestResponseModel>>
}