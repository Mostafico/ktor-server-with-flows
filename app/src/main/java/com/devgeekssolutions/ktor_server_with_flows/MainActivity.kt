package com.devgeekssolutions.ktor_server_with_flows

import ItemState
import MyServer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.devgeekssolutions.ktor_server_with_flows.ui.theme.KtorserverwithflowsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val server = MyServer()
        server.server.start(wait = false)
        lifecycleScope.launch {
            server.flow.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED).collect {
                when (it) {
                    is ItemState.Initial -> println("Initial")
                    is ItemState.ItemReceived -> println("Order Received ${it.item}")
                    else -> {}
                }
            }
        }
        setContent {
            KtorserverwithflowsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier,
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KtorserverwithflowsTheme {
        Greeting("Android")
    }
}
