package com.pixeldev.composetv.screens.setting

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.surfaceColorAtElevation
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.screens.home.JetStreamCardShape
import com.pixeldev.composetv.utlis.openEmailClient

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HelpAndSupportSection(navHostController: NavHostController) {
    val context = LocalContext.current

    with(StringConstants.Composable.Placeholders) {
        Column(modifier = Modifier.padding(horizontal = 72.dp)) {
            Text(
                text = HelpAndSupportSectionTitle,
                color = Color.White, style = MaterialTheme.typography.headlineSmall
            )
            /*HelpAndSupportSectionItem(title = HelpAndSupportSectionFAQItem){
                val title = "FAQ"
                val url = "https://pixeldev.in/privacy_policy.php"

                val encodedTitle = Uri.encode(title)
                val encodedUrl = Uri.encode(url)

                navHostController.navigate("webView/$encodedTitle/$encodedUrl")

            }*/
            HelpAndSupportSectionItem(title = HelpAndSupportSectionPrivacyItem){
                val title = "Privacy Policy"
                val url = "https://pixeldev.in/privacy_policy.php"

                val encodedTitle = Uri.encode(title)
                val encodedUrl = Uri.encode(url)

                navHostController.navigate("webView/$encodedTitle/$encodedUrl")
            }
            HelpAndSupportSectionItem(
                title = HelpAndSupportSectionContactItem,
                value = HelpAndSupportSectionContactValue
            ){
                val emailAddress = HelpAndSupportSectionContactValue
                val subject = "Jetpack Compose TV "
                val body = "Hi, Lets collab..."
                openEmailClient(context, emailAddress, subject, body)
            }
        }
    }
}

@Composable
private fun HelpAndSupportSectionItem(
    title: String,
    value: String? = null,
    onClickItem:()-> Unit
) {
    ListItem(
        modifier = Modifier.padding(top = 16.dp),
        selected = false,
        onClick = {
          onClickItem()
        },
        trailingContent = {
            value?.let { nnValue ->
                Text(
                    text = nnValue,
                    style = MaterialTheme.typography.titleMedium
                )
            } ?: run {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowForwardIos,
                    modifier = Modifier.size(ListItemDefaults.IconSizeDense),
                    contentDescription = StringConstants
                        .Composable
                        .Placeholders
                        .HelpAndSupportSectionListItemIconDescription
                )
            }
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp),
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContentColor = MaterialTheme.colorScheme.surfaceVariant
        ),
        shape = ListItemDefaults.shape(shape = JetStreamCardShape)
    )
}
