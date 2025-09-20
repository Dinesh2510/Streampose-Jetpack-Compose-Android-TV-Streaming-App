package com.pixeldev.composetv.screens.login

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.compose.runtime.getValue
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import androidx.tv.material3.*
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.pixeldev.composetv.utlis.SectionHeader
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.local.SearchHistory
import com.pixeldev.composetv.data.local.UserDetails
import com.pixeldev.composetv.data.remote.response.GenreResponse
import com.pixeldev.composetv.data.remote.response.MovieResponse
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.models.Genre
import com.pixeldev.composetv.models.Movies
import com.pixeldev.composetv.screens.categories.ErrorScreen
import com.pixeldev.composetv.screens.login.UserViewModel
import com.pixeldev.composetv.utlis.Constants.Companion.BASE_BACKDROP_IMAGE_URL_300
import com.pixeldev.composetv.utlis.MovieState
import com.pixeldev.composetv.utlis.TVGradientLoadingIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    navHostController: NavHostController,
    userViewModel: UserViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Move isLoading state here so it controls both content and logic
    var isLoading by remember { mutableStateOf(false) }

    LoginScreenContent(
        isLoading = isLoading,
        onLoginClick = { email, pass ->
            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(context, "Please fill in both fields.", Toast.LENGTH_SHORT).show()
            } else {
                isLoading = true
                // Simulate login and navigate to Dashboard
                userViewModel.saveUserDetails(
                    UserDetails(
                        "Pixel",
                        "Developer",
                        "123456789",
                        "support@pixeldev.in",
                        "123456"
                    )
                )
                coroutineScope.launch {
                    delay(1500)
                    navHostController.navigate(Screen.DashBoardScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
        }
    )
}


@Composable
fun LoginScreenContent(
    isLoading: Boolean,
    onLoginClick: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val emailFocusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        emailFocusRequester.requestFocus()
    }

    Row(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // LEFT: Login Form
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(40.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Logo
            Icon(
                painter = painterResource(id = R.drawable.logo_trans),
                contentDescription = "App Logo",
                tint = Color.Unspecified,
                modifier = Modifier
                    .width(300.dp)
                    .height(200.dp)
                    .padding(bottom = 0.dp)
            )

            FocusableInputField(
                value = email,
                onValueChange = { email = it },
                placeholder = "Email"
            )

            Spacer(modifier = Modifier.height(12.dp))

            FocusableInputField(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                isPassword = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    focusManager.clearFocus()
                    onLoginClick(email, password)
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(top = 16.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    TVGradientLoadingIndicator() // 🔄 your custom loader
                } else {
                    Text("Login", modifier = Modifier.padding(horizontal = 8.dp), fontSize = 22.sp)
                }
            }
        }

        // Middle Divider
        Box(
            modifier = Modifier
                .width(1.dp)
                .fillMaxHeight()
                .background(Color.Gray)
        )

        // RIGHT: QR Code & Info
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.qr_code),
                contentDescription = "Scan QR Code",
                modifier = Modifier
                    .size(200.dp)
                    .padding(16.dp)
            )

            Text(
                text = "Scan this code with your mobile app to login instantly.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}


@Composable
fun FocusableInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }

    // Animate scale on focus
    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.05f else 1f,
        label = "FocusScale"
    )

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused }
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .border(
                width = 2.dp,
                color = if (isFocused) MaterialTheme.colorScheme.primary else Color.Gray,
                shape = RoundedCornerShape(8.dp)
            )
            .background(
                color = if (isFocused)
                    MaterialTheme.colorScheme.inverseOnSurface.copy(alpha = 0.15f)
                else
                    MaterialTheme.colorScheme.surface,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .focusable(),
        textStyle = LocalTextStyle.current.copy(
            color = Color.White, // 🔥 user-entered text in white
            fontSize = 18.sp
        ),
        decorationBox = { innerTextField ->
            Box(Modifier.fillMaxWidth()) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        color = Color.Gray, // placeholder color
                        fontSize = 18.sp
                    )
                }
                innerTextField()
            }
        }
    )
}


@Preview(device = Devices.TV_1080p)
@Composable
fun LoginScreenPreview() {
}

@Composable
fun UserDetailsScreen(viewModel: UserViewModel = hiltViewModel()) {
    val userDetails by viewModel.userDetails.collectAsState(
        initial = UserDetails(
            "",
            "",
            "",
            "",
            ""
        )
    )
    val searchHistory by viewModel.searchHistory.collectAsState(initial = SearchHistory(emptyList()))

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "User Details")
        Text(text = "First Name: ${userDetails.firstName}")
        Text(text = "Last Name: ${userDetails.lastName}")
        Text(text = "Phone: ${userDetails.phoneNumber}")
        Text(text = "Email: ${userDetails.email}")

        // Button to save or update user details
        Button(onClick = {
            viewModel.saveUserDetails(
                UserDetails(
                    "John",
                    "Doe",
                    "123456789",
                    "john@example.com",
                    "password"
                )
            )
        }) {
            Text("Save User Details")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Search History")
        searchHistory.searchTerms.forEach {
            Text(text = it)
        }

        // Button to add search term
        Button(onClick = { viewModel.addSearchTerm("New Movie Search Term") }) {
            Text("Add Search Term")
        }

        // Button to clear search history
        Button(onClick = { viewModel.clearSearchHistory() }) {
            Text("Clear Search History")
        }
    }
}