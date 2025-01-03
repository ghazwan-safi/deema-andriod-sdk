package com.deema.sdkandroid

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deema.sdkandroid.ui.theme.DeemaSDKAndroidTheme
import me.deema.sdk.DeemaSDK
import me.deema.sdk.DeemaSDKResult
import me.deema.sdk.Environment
import me.deema.sdk.PaymentStatus

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        print("Environment ${Environment.Sandbox}")

        val deemaLauncher = registerForActivityResult(DeemaSDKResult()) { result ->
            println("result: $result")
            when(result) {
                is PaymentStatus.Success -> Toast.makeText(MainActivity@this, "Success", Toast.LENGTH_LONG).show()

                is PaymentStatus.Canceled -> Toast.makeText(MainActivity@this, "Canceled", Toast.LENGTH_LONG).show()

                is PaymentStatus.Failure -> Toast.makeText(MainActivity@this, "Failure: ${result.message}", Toast.LENGTH_LONG).show()

                is PaymentStatus.Unknown -> Toast.makeText(MainActivity@this, "Unknown: ${result.message}", Toast.LENGTH_LONG).show()
            }
        }

        setContent {
            var customerPhone by remember { mutableStateOf("96599123444") }
            var currency by remember { mutableStateOf("kwd") }
            var purchaseAmount by remember { mutableStateOf("20") }
            var sdkKey by remember { mutableStateOf("sk_test_d5gntxxdoRNGkAweKjWZMr8iocXd3oNO1Wz5VJuW_65") }
            var merchantOrderId by remember { mutableStateOf("1726") }

            DeemaSDKAndroidTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Box(modifier = Modifier.fillMaxWidth().height(48.dp).background(Color(0xffF18E9B)), contentAlignment = Alignment.Center) {
                            Text("DeemaSDKV1 Merchant Example", color = Color.White)
                        }
                    },
                ) {
                    innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {

                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 28.dp, vertical = 20.dp), horizontalAlignment =Alignment.CenterHorizontally) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = customerPhone,
                                onValueChange = { customerPhone = it },
                                label = { Text("Customer Phone") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = currency,
                                onValueChange = { currency = it },
                                label = { Text("Currency: (kwd or bhd)") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = purchaseAmount,
                                onValueChange = { purchaseAmount = it },
                                label = { Text("Purchase Amount") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = sdkKey,
                                onValueChange = { sdkKey = it },
                                label = { Text("SDK Key") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = merchantOrderId,
                                onValueChange = { merchantOrderId = it },
                                label = { Text("Merchant Order Id") }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(onClick = {
                                DeemaSDK.launch(
                                    environment = Environment.Sandbox,
                                    sdkKey = sdkKey,
                                    currency = currency.uppercase(),
                                    purchaseAmount = purchaseAmount,
                                    merchantOrderId = merchantOrderId,
                                    launcher = deemaLauncher,
                                )
                            }) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeemaSDKAndroidTheme {
    }
}