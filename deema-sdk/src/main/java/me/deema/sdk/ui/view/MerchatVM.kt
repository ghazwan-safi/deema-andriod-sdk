package me.deema.sdk.ui.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import me.deema.sdk.data.util.DataState
import me.deema.sdk.data.domian.models.PurchaseDetails
import me.deema.sdk.data.domian.models.PurchaseOrderRequest
import me.deema.sdk.data.repository.RemoteDataSource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

enum class ApiResponse {None, Loading, Success, Error, NoInternet, UnAuthorize }

data class UiState(
    val merchantRequestResponseData: PurchaseDetails?= null,
    val status: Int = 0,
    val errorMessage: String? = null,
    val apiResponse: ApiResponse = ApiResponse.None,
    )


class MerchantVM(private val repository: RemoteDataSource) : ViewModel(){

    var uiState = MutableStateFlow(UiState())
        private set

    fun changePaymentStatus(value: Int) {
        Timber.i("changePaymentStatus $value")
        viewModelScope.launch {
            delay(500)
            uiState.update { it.copy(status = value) }
        }
    }

    fun getPurchaseOrder(request: PurchaseOrderRequest) {
        Timber.i("getPurchaseOrder $request")

        viewModelScope.launch {
            repository.getPurchaseOrder(request).collect { state ->
                when (state) {
                    is DataState.Success -> {
                        state.data?.let { response ->
                            uiState.update { it.copy(merchantRequestResponseData = response.data, errorMessage = null, apiResponse = ApiResponse.Success) }
                        }
                    }

                    is DataState.Error -> {
                        val errorMessage = state.toString()
                        uiState.update { it.copy(errorMessage = errorMessage, apiResponse = ApiResponse.Error) }
                    }

                    is DataState.Loading -> {
                        uiState.update { it.copy(apiResponse = ApiResponse.Loading) }
                    }
                }
            }
        }
    }
}
