package com.pixeldev.composetv.screens.setting

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.pixeldev.composetv.data.local.UserDetails
import com.pixeldev.composetv.graph.Screen
import com.pixeldev.composetv.screens.login.UserViewModel
import com.pixeldev.composetv.utlis.FullScreenDialog
import com.pixeldev.composetv.utlis.rememberChildPadding

@Immutable
data class AccountsSectionData(
    val title: String,
    val value: String? = null,
    val onClick: () -> Unit = {}
)

@Composable
fun AccountsSection(navHostController: NavHostController,userViewModel: UserViewModel = hiltViewModel()) {
    val childPadding = rememberChildPadding()
    var showDeleteDialog by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    // Collect user details as a state
    val userDetails by userViewModel.userDetails.collectAsState(
        initial = UserDetails(
            "", "", "", "", ""
        )
    )

    // Log for debugging user email
    Log.d("TAG_Android", "AccountsSection: ${userDetails.email}")

    // Create the accounts section list items, with userDetails.email dynamically set
    val accountsSectionListItems = remember(userDetails.email) {
        listOf(
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders
                    .AccountsSelectionSwitchAccountsTitle,
                value = userDetails.email.takeIf { it.isNotBlank() }
                    ?: "Email not available" // Handle empty email
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders.AccountsSelectionLogOut,
                value = userDetails.email.takeIf { it.isNotBlank() }
                    ?: "Email not available", // Handle empty email
                onClick = {
                    // Clear user details and navigate to login screen
                    userViewModel.clearUserDetails()
                    // Navigate to login screen and clear all previous screens
                    navHostController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.Splash.route) {
                            inclusive = true
                        } // Clear everything above SplashScreen
                    }
                }
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders
                    .AccountsSelectionChangePasswordTitle,
                value = StringConstants.Composable.Placeholders.AccountsSelectionChangePasswordValue
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders.AccountsSelectionAddNewAccountTitle,
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders
                    .AccountsSelectionViewSubscriptionsTitle
            ),
            AccountsSectionData(
                title = StringConstants.Composable.Placeholders.AccountsSelectionDeleteAccountTitle,
                onClick = { showDeleteDialog = true }
            )
        )
    }

    LazyVerticalGrid(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = childPadding.start),
        columns = GridCells.Fixed(2),
        content = {
            items(accountsSectionListItems.size) { index ->
                AccountsSelectionItem(
                    modifier = Modifier.focusRequester(focusRequester),
                    key = index,
                    accountsSectionData = accountsSectionListItems[index]
                )
            }
        }
    )

     AccountsSectionDeleteDialog(
         showDialog = showDeleteDialog,
         onDismissRequest = { showDeleteDialog = false },
         modifier = Modifier.width(428.dp)
     )

}

@Composable
fun onClickLogout() {
    var showDialog by remember { mutableStateOf(false) }
    var context = LocalContext.current
    if (showDialog) {
        FullScreenDialog(
            title = "Clear search history?",
            description = "Are you sure you want to clear your search history?",
            confirmButtonText = "Clear search history",
            cancelButtonText = "Cancel",
            onConfirm = { /* clear history */ },
            onCancel = { /* close dialog */ }
        )

    }

    // Trigger dialog for demo
    LaunchedEffect(Unit) {
        showDialog = true
    }
}
