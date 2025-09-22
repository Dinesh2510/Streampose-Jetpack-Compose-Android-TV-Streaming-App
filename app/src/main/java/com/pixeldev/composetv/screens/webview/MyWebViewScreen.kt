package com.pixeldev.composetv.screens.webview

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.BlurredEdgeTreatment

import androidx.compose.ui.viewinterop.AndroidView

import androidx.compose.ui.draw.blur
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton

@Composable
fun MyWebViewScreen(webTitle: String, webUrl: String, navController: NavHostController) {
    val context = LocalContext.current

    FullScreenWebView(
        url = webUrl,
        title = webTitle,
        onBack = {
            navController.popBackStack()
        }
    )
}



@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TvTopBarWithBlur(
    title: String,
    onBackClick: () -> Unit
) {
    // The blur effect is applied to a Box, which acts as the background.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp) // Set a fixed height for the top bar
            .blur(
                radius = 16.dp, // Adjust the blur radius as needed
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            )
            .background(Color.Black.copy(alpha = 0.5f)), // Semi-transparent overlay
        contentAlignment = Alignment.CenterStart // Align all content to the start
    ) {
        // Place the content (icon and title) directly inside the Box
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Back Button
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.padding(end = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }

            // Title Text
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
@Composable
fun FullScreenWebView(
    url: String,
    title: String,
    onBack: () -> Unit
) {
    var isLoading by remember { mutableStateOf(true) }
    var webView: WebView? = remember { null }
    Box(
        modifier = Modifier.fillMaxSize()
    ) {


    Box(
        modifier  = Modifier.fillMaxSize()
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    // ⚙️ Configure WebView settings
                    settings.apply {
                        // Enable JavaScript
                        javaScriptEnabled = true

                        // Enable DOM storage, which is required for many modern websites
                        domStorageEnabled = true

                        // Enable support for multiple windows, useful for pop-ups
                        setSupportMultipleWindows(true)

                        // Set the user agent to a standard desktop agent for better compatibility
                        userAgentString = WebSettings.getDefaultUserAgent(context)

                        // Enable content to be stored in the cache
                        cacheMode = WebSettings.LOAD_DEFAULT
                    }

                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            super.onPageStarted(view, url, favicon)
                            isLoading = true
                        }
                        override fun onPageFinished(view: WebView?, url: String?) {
                            super.onPageFinished(view, url)
                            isLoading = false
                        }
                    }
                    webView = this
                    loadUrl(url)
                }
            }
        )
        TvTopBarWithBlur(title, {

        })
        if (isLoading) {
            TVGradientLoadingIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }

    // Handle the system back button
    BackHandler(onBack = onBack)
}}