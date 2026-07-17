package com.cian.orderbook.data.remote.bitget

import com.cian.orderbook.core.dispatcher.DispatcherProvider
import com.cian.orderbook.data.remote.bitget.connection.HeartbeatMonitor
import com.cian.orderbook.data.remote.bitget.connection.ReconnectPolicy
import com.cian.orderbook.data.remote.bitget.dto.BitgetChannelArgDto
import com.cian.orderbook.data.remote.bitget.dto.BitgetSubscribeRequestDto
import com.cian.orderbook.data.remote.bitget.message.BitgetMessageParser
import com.cian.orderbook.data.remote.bitget.message.BitgetSocketMessage
import com.cian.orderbook.domain.model.ConnectionState
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

@Singleton
class BitgetWebSocketClientImpl @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val json: Json,
    private val messageParser: BitgetMessageParser,
    private val heartbeatMonitor: HeartbeatMonitor,
    private val reconnectPolicy: ReconnectPolicy,
    dispatcherProvider: DispatcherProvider
) : BitgetWebSocketClient {

    // Single-threaded so connection-state mutation never races across OkHttp's callback threads
    private val clientScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + dispatcherProvider.io.limitedParallelism(1))

    private val _messages = MutableSharedFlow<BitgetSocketMessage>(extraBufferCapacity = EXTRA_BUFFER_CAPACITY)
    private val _connectionState = MutableStateFlow<ConnectionState>(ConnectionState.Idle)

    private var activeSocket: WebSocket? = null
    private var subscriptions: List<BitgetChannelArgDto> = emptyList()
    private var reconnectAttempt = 0
    private var intentionalDisconnect = false
    private var reconnectJob: Job? = null

    override val messages: Flow<BitgetSocketMessage> = _messages.asSharedFlow()
    override val connectionState: StateFlow<ConnectionState> = _connectionState.asStateFlow()

    override fun connect(subscriptions: List<BitgetChannelArgDto>) {
        this.subscriptions = subscriptions
        intentionalDisconnect = false
        reconnectJob?.cancel()
        openSocket()
    }

    override fun disconnect() {
        intentionalDisconnect = true
        reconnectJob?.cancel()
        heartbeatMonitor.stop()
        activeSocket?.close(NORMAL_CLOSURE_CODE, "client disconnect")
        activeSocket = null
        _connectionState.value = ConnectionState.Disconnected(reason = null)
    }

    private fun openSocket() {
        _connectionState.value = ConnectionState.Connecting
        val request = Request.Builder().url(BitgetEndpoints.PUBLIC_WS_URL).build()
        activeSocket = okHttpClient.newWebSocket(request, listener)
    }

    private val listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            clientScope.launch {
                reconnectAttempt = 0
                _connectionState.value = ConnectionState.Connected
                sendSubscribeFrame(webSocket)
                heartbeatMonitor.start(
                    scope = clientScope,
                    sendPing = { webSocket.send(PING_PAYLOAD) },
                    onTimeout = { webSocket.close(ABNORMAL_CLOSURE_CODE, "heartbeat timeout") }
                )
            }
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            clientScope.launch {
                if (text == PONG_PAYLOAD) {
                    heartbeatMonitor.acknowledgePong()
                    _messages.emit(BitgetSocketMessage.Pong)
                    return@launch
                }
                messageParser.parse(text)?.let { _messages.emit(it) }
            }
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            webSocket.close(code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            clientScope.launch { handleDisconnection(reason.ifBlank { "closed ($code)" }) }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            clientScope.launch { handleDisconnection(t.message ?: "connection failure") }
        }
    }

    private fun sendSubscribeFrame(webSocket: WebSocket) {
        if (subscriptions.isEmpty()) return
        val request = BitgetSubscribeRequestDto(op = "subscribe", args = subscriptions)
        webSocket.send(json.encodeToString(BitgetSubscribeRequestDto.serializer(), request))
    }

    private fun handleDisconnection(reason: String) {
        heartbeatMonitor.stop()
        activeSocket = null
        if (intentionalDisconnect) {
            _connectionState.value = ConnectionState.Disconnected(reason)
            return
        }
        scheduleReconnect(reason)
    }

    private fun scheduleReconnect(reason: String) {
        reconnectAttempt += 1
        val delayMs = reconnectPolicy.nextDelayMillis(reconnectAttempt)
        _connectionState.value = ConnectionState.Reconnecting(attempt = reconnectAttempt, delayMs = delayMs)
        reconnectJob = clientScope.launch {
            delay(delayMs)
            if (!intentionalDisconnect) openSocket()
        }
    }

    private companion object {
        const val PING_PAYLOAD = "ping"
        const val PONG_PAYLOAD = "pong"
        const val NORMAL_CLOSURE_CODE = 1000
        const val ABNORMAL_CLOSURE_CODE = 1001
        const val EXTRA_BUFFER_CAPACITY = 64
    }
}
