package me.deema.sdk

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import me.deema.sdk.data.AppData

enum class Environment{
    Sandbox, Production
}

sealed interface PaymentStatus {
    data object Success : PaymentStatus
    data object Canceled : PaymentStatus
    data class Failure(val message: String?) : PaymentStatus
    data class Unknown(val message: String?) : PaymentStatus
}


open class DeemaSDK{

    companion object{
        fun launch(environment: Environment, currency: String, purchaseAmount: String, sdkKey: String, merchantOrderId: String, launcher: ActivityResultLauncher<String>){
            val appData = AppData.getInstance().getSharedData()
            appData.environment = environment
            appData.sdkKey = sdkKey ?: ""
            appData.currency = currency ?: "KWD"
            appData.purchaseAmount = purchaseAmount ?: "0.0"
            appData.merchantOrderId = merchantOrderId ?: ""

            launcher.launch("Deema")
        }
    }
}


open class DeemaSDKResult: ActivityResultContract<String, PaymentStatus>(){
    override fun createIntent(context: Context, input: String): Intent {
        return Intent(context, DeemaActivity::class.java).putExtra("input", input)
    }
    override fun parseResult(resultCode: Int, intent: Intent?): PaymentStatus {
        if (resultCode == Activity.RESULT_OK) {

            return when(intent?.getStringExtra("status")?.lowercase()){
                "success" -> PaymentStatus.Success
                "failure" -> PaymentStatus.Failure(intent.getStringExtra("message"))
                "canceled" -> PaymentStatus.Canceled
                else -> PaymentStatus.Unknown(intent?.getStringExtra("message"))
            }
        }
        if (resultCode == Activity.RESULT_CANCELED) {
            return PaymentStatus.Canceled
        }
        return PaymentStatus.Unknown("Unknown error")
    }
}