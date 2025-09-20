package com.pixeldev.composetv.utlis

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.tv.material3.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import com.pixeldev.composetv.R

@Composable
fun FullScreenDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    val confirmButtonFocusRequester = remember { FocusRequester() }

    // Automatically request focus when dialog appears
    LaunchedEffect(Unit) {
        confirmButtonFocusRequester.requestFocus()
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .width(500.dp)
                .background(Color.Black, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 🔍 Icon
            Icon(
                painter = painterResource(id = R.drawable.ic_logo), // Replace with your icon
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color.Unspecified
            )

            // 📝 Title (from parameter)
            Text(
                text = title,
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            // 📄 Description (from parameter)
            Text(
                text = description,
                color = Color.LightGray,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )

            // 🧭 Buttons (focusable)
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item {
                    Button(
                        onClick = onConfirm,
                        colors = ButtonDefaults.colors( containerColor = Color.DarkGray, contentColor = Color.White ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 120.dp, vertical = 8.dp)
                            .height(56.dp)
                            .focusRequester(confirmButtonFocusRequester)
                            .clip(RoundedCornerShape(28.dp)) // Pill shape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = confirmButtonText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.colors( containerColor = Color.DarkGray, contentColor = Color.White ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 120.dp, vertical = 8.dp)
                            .height(56.dp)
                            .clip(RoundedCornerShape(28.dp)) // Pill shape
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cancelButtonText,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

