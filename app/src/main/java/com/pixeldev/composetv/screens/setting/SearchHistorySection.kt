package com.pixeldev.composetv.screens.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.ListItem
import androidx.tv.material3.ListItemDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import com.pixeldev.composetv.data.local.SearchHistory
import com.pixeldev.composetv.screens.home.JetStreamCardShape
import com.pixeldev.composetv.screens.login.UserViewModel

@Composable
fun SearchHistorySection(userViewModel: UserViewModel= hiltViewModel()) {
    with(StringConstants.Composable.Placeholders) {
        val searchHistory by userViewModel.searchHistory.collectAsState(initial = SearchHistory(emptyList()))

        LazyColumn(modifier = Modifier.padding(horizontal = 72.dp)) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Top 5 $SearchHistorySectionTitle",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Button(onClick = { userViewModel.clearSearchHistory() }) {
                        Text(text = SearchHistoryClearAll)
                    }
                }
            }
            items( searchHistory.searchTerms.take(5).size) { index ->
                ListItem(
                    modifier = Modifier.padding(top = 8.dp),
                    selected = false,
                    onClick = {},
                    headlineContent = {
                        Text(
                            text = searchHistory.searchTerms[index],
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    shape = ListItemDefaults.shape(shape = JetStreamCardShape)
                )
            }
        }
    }
}
