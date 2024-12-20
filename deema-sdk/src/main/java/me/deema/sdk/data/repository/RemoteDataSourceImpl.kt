package me.deema.sdk.data.repository


import me.deema.sdk.data.util.DataState
import me.deema.sdk.data.domian.models.PurchaseOrderRequest
import me.deema.sdk.data.domian.models.PurchaseRequestResponseModel
import me.deema.sdk.data.retrofit.DeemaApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class RemoteDataSourceImpl(private val api: DeemaApi) :
    RemoteDataSource {

    override suspend fun getPurchaseOrder(request: PurchaseOrderRequest): Flow<DataState<PurchaseRequestResponseModel>> = flow {
        emit(DataState.Loading)
        try {
            val response = api.getPurchaseOrder(request)
            Timber.d("Response: $response")
            emit(DataState.Success(response))
        } catch (e: Exception){
            e.stackTrace
            Timber.d("Exception: ${e.message}")
            emit(DataState.Error(e, message = e.localizedMessage))
        }
    }
}