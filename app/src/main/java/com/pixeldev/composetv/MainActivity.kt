package com.pixeldev.composetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.pixeldev.composetv.graph.AppNavHost
import com.pixeldev.composetv.ui.theme.ComposeTVYTTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTVYTTheme {
                AppNavHost()
            }
        }
    }
}
