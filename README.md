# Deema SDK for BNPL Payment Service

## Introduction

The **Deema SDK** enables seamless integration with our Buy Now, Pay Later (BNPL) payment service. It provides a streamlined API for developers to launch payment flows, handle statuses, and manage transactions effectively.

---

## Prerequisites

- **Minimum Requirements**:
    - Android API Level 21 or higher.

---

## Installation


To include the Deema SDK in your Android project:

**1-** Add it in your root `build.gradle` at the end of repositories:
```gradle
	dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
**2-** Add the following dependency to your `build.gradle`:

```gradle
dependencies {
	implementation 'com.github.ghazwan-safi:deema-andriod-sdk:Tag'
}
```

---

## Quick Start

### Step 1: Initialize the SDK
Configure the SDK with your `sdkKey`, environment, currency, purchase amount, and merchant order ID.

```kotlin
val launcher = registerForActivityResult(DeemaSDKResult()) { status ->
    when (status) {
        is PaymentStatus.Success -> {
            // Handle payment success
        }
        is PaymentStatus.Failure -> {
            // Handle failure with the provided message
        }
        is PaymentStatus.Canceled -> {
            // Handle cancellation
        }
        is PaymentStatus.Unknown -> {
            // Handle unknown status
        }
    }
}

DeemaSDK.launch(
    environment = Environment.Sandbox, // Use Sandbox for testing
    currency = "KWD",
    purchaseAmount = "50.00",
    sdkKey = "your-sdk-key",
    merchantOrderId = "12345",
    launcher = launcher
)
```

### Step 2: Handle Results
The result of the payment flow is captured using an `ActivityResultLauncher`. The possible statuses include:
- `PaymentStatus.Success`: Payment was successful.
- `PaymentStatus.Failure`: Payment failed with a message.
- `PaymentStatus.Canceled`: User canceled the payment.
- `PaymentStatus.Unknown`: An unknown status occurred.

---

## Example Integration

Here's a complete example of integrating the Deema SDK into an Android app:

```kotlin
class PaymentActivity : AppCompatActivity() {

    private val paymentLauncher = registerForActivityResult(DeemaSDKResult()) { status ->
        when (status) {
            is PaymentStatus.Success -> {
                Toast.makeText(this, "Payment successful!", Toast.LENGTH_SHORT).show()
            }
            is PaymentStatus.Failure -> {
                Toast.makeText(this, "Payment failed: ${status.message}", Toast.LENGTH_SHORT).show()
            }
            is PaymentStatus.Canceled -> {
                Toast.makeText(this, "Payment canceled.", Toast.LENGTH_SHORT).show()
            }
            is PaymentStatus.Unknown -> {
                Toast.makeText(this, "Unknown status: ${status.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)

        findViewById<Button>(R.id.payButton).setOnClickListener {
            DeemaSDK.launch(
                environment = Environment.Production,
                currency = "KWD",
                purchaseAmount = "100.00",
                sdkKey = "your-production-sdk-key",
                merchantOrderId = "order123",
                launcher = paymentLauncher
            )
        }
    }
}
```

Here’s how to integrate the **Deema SDK** into a **Jetpack Compose** Android application:

---

### Example Integration with Jetpack Compose

```kotlin
@Composable
fun PaymentScreen() {
    val context = LocalContext.current

    // Remember ActivityResultLauncher for DeemaSDK
    val paymentLauncher = rememberLauncherForActivityResult(DeemaSDKResult()) { status ->
        when (status) {
            is PaymentStatus.Success -> {
                Toast.makeText(context, "Payment successful!", Toast.LENGTH_SHORT).show()
            }
            is PaymentStatus.Failure -> {
                Toast.makeText(context, "Payment failed: ${status.message}", Toast.LENGTH_SHORT).show()
            }
            is PaymentStatus.Canceled -> {
                Toast.makeText(context, "Payment canceled.", Toast.LENGTH_SHORT).show()
            }
            is PaymentStatus.Unknown -> {
                Toast.makeText(context, "Unknown status: ${status.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // UI content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Deema Payment",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(onClick = {
            DeemaSDK.launch(
                environment = Environment.Production,
                currency = "KWD",
                purchaseAmount = "100.00",
                sdkKey = "your-production-sdk-key",
                merchantOrderId = "order123",
                launcher = paymentLauncher
            )
        }) {
            Text(text = "Pay Now")
        }
    }
}

```

---

### Explanation

1. **Compose Lifecycle Awareness**:
    - `rememberLauncherForActivityResult`: Used to initialize the `ActivityResultLauncher` in a Compose-aware way.

2. **UI Components**:
    - `Column`: Organizes the UI vertically.
    - `Button`: Triggers the SDK payment flow on click.

3. **Payment Launch**:
    - `DeemaSDK.launch`: Initiates the SDK payment flow with the provided parameters.

4. **Result Handling**:
    - The `ActivityResultLauncher` handles results as per the defined `PaymentStatus` outcomes.

---

## API Reference

### Enums
- `Environment`: Specifies the SDK environment.
    - `Sandbox`: Testing environment.
    - `Production`: Live environment.

### Classes
- `DeemaSDK`
    - `launch`: Initializes and launches the payment flow.
        - Parameters:
            - `environment`: `Environment` (required) - Sandbox or Production.
            - `currency`: `String` (required) - The currency code (KWD or BHD).
            - `purchaseAmount`: `String` (required) - The total purchase amount.
            - `sdkKey`: `String` (required) - Your SDK API key.
            - `merchantOrderId`: `String` (required) - Merchant's unique order identifier.
            - `launcher`: `ActivityResultLauncher<String>` - Launcher to handle results.

- `DeemaSDKResult`
    - Extends `ActivityResultContract<String, PaymentStatus>` to handle payment results.

### Payment Status
- `PaymentStatus.Success`: Payment completed successfully.
- `PaymentStatus.Canceled`: Payment process was canceled by the user.
- `PaymentStatus.Failure`: Payment failed with an optional message.
- `PaymentStatus.Unknown`: Unexpected status with an optional message.

---
