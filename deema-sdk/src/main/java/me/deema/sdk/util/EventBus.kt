package me.deema.sdk.util

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

object EventBus {
    private val _events = Channel<Any>()
    val events = _events.receiveAsFlow()

    suspend fun sendEvent(event: Any) {
        _events.send(event)
    }
}

sealed interface Event {
    data class Error(val message: String) : Event
}

