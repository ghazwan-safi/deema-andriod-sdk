package me.deema.sdk

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import me.deema.sdk.data.AppData
import me.deema.sdk.data.di.AppNetworkModule
import me.deema.sdk.data.di.AppNetworkModuleImpl
import me.deema.sdk.data.domian.models.PurchaseOrderRequest
import me.deema.sdk.ui.theme.DeemaSDKAndroidTheme
import me.deema.sdk.ui.view.WebMerchantView
import me.deema.sdk.util.Event
import me.deema.sdk.util.EventBus

data class ErrorUiState(
    val isError: Boolean = false,
    val title: String? = null,
    val message: String? = null,
)

class DeemaActivity : ComponentActivity() {

    companion object {
        lateinit var appModule: AppNetworkModule
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appModule = AppNetworkModuleImpl(DeemaActivity@this)

        enableEdgeToEdge()

        setContent {
            var errorUiState by remember {
                mutableStateOf<ErrorUiState>(ErrorUiState())
            }

            val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current.lifecycle
            LaunchedEffect(key1 = lifecycleOwner) {
                lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    EventBus.events.collect { event ->
                        when (event) {
                            is Event.Error -> {
                                errorUiState = errorUiState.copy(isError = true, message = event.message)
                            }
                        }
                    }
                }
            }

            DeemaSDKAndroidTheme {
                WebMerchantView(
                    request = PurchaseOrderRequest(
                        merchantOrderId = AppData.getInstance().getSharedData().merchantOrderId!!,
                        amount = AppData.getInstance().getSharedData().purchaseAmount!!,
                        currencyCode = AppData.getInstance().getSharedData().currency!!
                    )
                )
            }

            /// handle error events
            if (errorUiState.isError) {
                MainActivity@this.setResult(RESULT_OK, Intent().apply {
                    putExtra("status", "failure")
                    putExtra("message", errorUiState.message)
                })
                MainActivity@this.finish()
            }
        }
    }
}
