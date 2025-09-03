package com.pixeldev.composetv

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.Modifier
import com.pixeldev.composetv.graph.AppNavHost
import com.pixeldev.composetv.ui.theme.ComposeTVYTTheme
import com.pixeldev.composetv.ui.theme.backgroundDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeTVYTTheme {
                Column(Modifier.background(backgroundDark)) {
                    AppNavHost()
                }
            }
        }
    }
}
