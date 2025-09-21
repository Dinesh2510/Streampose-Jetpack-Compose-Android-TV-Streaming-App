/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixeldev.composetv.utlis

import android.content.Context
import android.view.KeyEvent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pixeldev.composetv.R
import com.pixeldev.composetv.data.remote.response.MovieResponse

/**
 * Handles horizontal (Left & Right) D-Pad Keys and consumes the event(s) so that the focus doesn't
 * accidentally move to another element.
 * */
fun Modifier.handleDPadKeyEvents(
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onEnter: (() -> Unit)? = null
) = onPreviewKeyEvent {
    fun onActionUp(block: () -> Unit) {
        if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) block()
    }

    when (it.nativeKeyEvent.keyCode) {
        KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT -> {
            onLeft?.apply {
                onActionUp(::invoke)
                return@onPreviewKeyEvent true
            }
        }

        KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT -> {
            onRight?.apply {
                onActionUp(::invoke)
                return@onPreviewKeyEvent true
            }
        }

        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> {
            onEnter?.apply {
                onActionUp(::invoke)
                return@onPreviewKeyEvent true
            }
        }
    }

    false
}

/**
 * Handles all D-Pad Keys
 * */
fun Modifier.handleDPadKeyEvents(
    onLeft: (() -> Unit)? = null,
    onRight: (() -> Unit)? = null,
    onUp: (() -> Unit)? = null,
    onDown: (() -> Unit)? = null,
    onEnter: (() -> Unit)? = null
) = onKeyEvent {

    if (it.nativeKeyEvent.action == KeyEvent.ACTION_UP) {
        when (it.nativeKeyEvent.keyCode) {
            KeyEvent.KEYCODE_DPAD_LEFT, KeyEvent.KEYCODE_SYSTEM_NAVIGATION_LEFT -> {
                onLeft?.invoke().also { return@onKeyEvent true }
            }

            KeyEvent.KEYCODE_DPAD_RIGHT, KeyEvent.KEYCODE_SYSTEM_NAVIGATION_RIGHT -> {
                onRight?.invoke().also { return@onKeyEvent true }
            }

            KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_SYSTEM_NAVIGATION_UP -> {
                onUp?.invoke().also { return@onKeyEvent true }
            }

            KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_SYSTEM_NAVIGATION_DOWN -> {
                onDown?.invoke().also { return@onKeyEvent true }
            }

            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                onEnter?.invoke().also { return@onKeyEvent true }
            }
        }
    }
    false
}

/**
 * Fills max available size and only utilizes the content size for the composable. Useful for
 * cases when you need to quickly center the item on the available area.
 * */
fun Modifier.occupyScreenSize() = this
    .fillMaxSize()
    .wrapContentSize()

/**
 * This modifier can be used to gain focus on a focusable component when it becomes visible
 * for the first time.
 * */
@Composable
fun Modifier.focusOnInitialVisibility(isVisible: MutableState<Boolean>): Modifier {
    val focusRequester = remember { FocusRequester() }

    return focusRequester(focusRequester)
        .onPlaced {
            if (!isVisible.value) {
                focusRequester.requestFocus()
                isVisible.value = true
            }
        }
}

/**
 * [FocusRequesterModifiers] defines a set of modifiers which can be used for restoring focus and
 * specifying the initially focused item.
 *
 * @param [parentModifier] is added to the parent container.
 * @param [childModifier] is added to the item that needs to first gain focus.
 *
 * For example, if you want the item at index 0 to get focus for the first time,
 * you can do the following:
 *
 * LazyRow(modifier.then(modifiers.parentModifier) {
 *   item1(modifier.then(modifiers.childModifier) {...}
 *   item2 {...}
 *   item3 {...}
 *   ...
 * }
 */
data class FocusRequesterModifiers(
    val parentModifier: Modifier,
    val childModifier: Modifier
)

/**
 * Returns a set of modifiers [FocusRequesterModifiers] which can be used for restoring focus and
 * specifying the initially focused item.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun createInitialFocusRestorerModifiers(): FocusRequesterModifiers {
    val focusRequester = remember { FocusRequester() }
    val childFocusRequester = remember { FocusRequester() }

    val parentModifier = Modifier
        .focusRequester(focusRequester)
        .focusProperties {
            exit = {
                focusRequester.saveFocusedChild()
                FocusRequester.Default
            }
            enter = {
                if (focusRequester.restoreFocusedChild()) FocusRequester.Cancel
                else childFocusRequester
            }
        }

    val childModifier = Modifier.focusRequester(childFocusRequester)

    return FocusRequesterModifiers(parentModifier, childModifier)
}

/**
 * Used to apply modifiers conditionally.
 */
fun Modifier.ifElse(
    condition: () -> Boolean,
    ifTrueModifier: Modifier,
    ifFalseModifier: Modifier = Modifier
): Modifier = then(if (condition()) ifTrueModifier else ifFalseModifier)

/**
 * Used to apply modifiers conditionally.
 */
fun Modifier.ifElse(
    condition: Boolean,
    ifTrueModifier: Modifier,
    ifFalseModifier: Modifier = Modifier
): Modifier = ifElse({ condition }, ifTrueModifier, ifFalseModifier)

@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 8.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}
val ParentPadding = PaddingValues(vertical = 16.dp, horizontal = 58.dp)



@Composable
fun TitleValueText(
    modifier: Modifier = Modifier,
    title: String,
    value: String
) {
    Column(modifier = modifier) {
        Text(
            modifier = Modifier.alpha(0.75f),
            text = title,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),color = Color.White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Normal),color = Color.White,
            maxLines = 3
        )
    }
}
// ---------- Utils ----------
fun Context.loadJSONFromAsset(fileName: String): String =
    assets.open(fileName).bufferedReader().use { it.readText() }

fun Context.getDummyMovies(): List<MovieResponse> {
    val json = loadJSONFromAsset("movies.json")
    val type = object : TypeToken<List<MovieResponse>>() {}.type
    return Gson().fromJson(json, type)
}

@Composable
fun SectionHeader(title: String, modifier: Modifier = Modifier) {
    Text(
        text = title,
        style =
            MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.Medium,
                fontSize = 25.sp
            ),
        color = Color.White,
        modifier = modifier.padding(horizontal = 16.dp, vertical = 12.dp)
    )
}

data class GenreWithSubtitle(
    val id: Int,
    val name: String,
    val subtitle: String
)

val tvGenresWithSubtitles = listOf(
    GenreWithSubtitle(10759, "Action & Adventure", "High-octane series & heroic quests"),
    GenreWithSubtitle(16, "Animation", "Colorful stories for all ages"),
    GenreWithSubtitle(35, "Comedy", "Laugh-out-loud TV moments"),
    GenreWithSubtitle(80, "Crime", "Mystery, law, and gripping investigations"),
    GenreWithSubtitle(99, "Documentary", "True stories that educate and inspire"),
    GenreWithSubtitle(18, "Drama", "Emotional and intense storytelling"),
    GenreWithSubtitle(10751, "Family", "Fun content for all generations"),
    GenreWithSubtitle(10762, "Kids", "Entertaining content for young minds"),
    GenreWithSubtitle(9648, "Mystery", "Unravel secrets and unexpected twists"),
    GenreWithSubtitle(10763, "News", "Current events and breaking updates"),
    GenreWithSubtitle(10764, "Reality", "Unscripted and unpredictable entertainment"),
    GenreWithSubtitle(10765, "Sci-Fi & Fantasy", "Otherworldly adventures and tech wonders"),
    GenreWithSubtitle(10766, "Soap", "Melodrama and daily relationship sagas"),
    GenreWithSubtitle(10767, "Talk", "Interviews, discussions, and debates"),
    GenreWithSubtitle(10768, "War & Politics", "Power struggles and wartime drama"),
    GenreWithSubtitle(37, "Western", "Cowboys, duels, and dusty trails")
)
fun formatToMillions(number: Long): String {
    return when {
        number >= 1_000_000 -> {
            val millions = number / 1_000_000.0
            String.format("%.1fM", millions).removeSuffix(".0M") + "M"
        }
        number >= 1_000 -> {
            val thousands = number / 1_000.0
            String.format("%.1fK", thousands).removeSuffix(".0K") + "K"
        }
        else -> number.toString()
    }
}

@Composable
fun CommonImageLoader(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Crop,
    placeholderRes: Int? = R.drawable.logo_trans,  // Optional placeholder resource
    errorRes: Int? = R.drawable.custom_image_with_padding         // Optional error resource
) {
    // Coil Image Painter
    val painter = rememberAsyncImagePainter(
        ImageRequest.Builder(LocalContext.current)
            .data(imageUrl)
            .crossfade(true) // Optional: Adds fade effect on loading
            .apply {
                placeholderRes?.let { placeholder(it) }
                errorRes?.let { error(it) }
            }
            .build()
    )

    // Using AsyncImage with appropriate placeholders
    AsyncImage(
        model = imageUrl,
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        placeholder = painter, // Default to 0 if null
        error =painter // Default to 0 if null
    )
}
