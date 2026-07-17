package com.cian.orderbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.cian.orderbook.navigation.CianNavHost
import com.cian.orderbook.ui.theme.CianTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { CianApp() }
    }
}

@Composable
private fun CianApp() {
    CianTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            val navController = rememberNavController()
            CianNavHost(navController = navController)
        }
    }
}
