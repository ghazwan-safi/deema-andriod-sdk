package me.deema.sdk.data.domian.models
import com.google.gson.annotations.SerializedName


data class PurchaseRequestResponseModel (
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: PurchaseDetails? = null,
)


data class PurchaseDetails (
    @SerializedName("purchase_id")
    val purchaseId: String,
    @SerializedName("redirect_link")
    val redirectLink: String,
    @SerializedName("order_reference")
    val orderReference: String,
)


data class PurchaseOrderRequest(
    @SerializedName("merchant_order_id")
    val merchantOrderId: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("currency_code")
    val currencyCode: String,
)
