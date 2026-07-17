package com.lastwagon.feature.routing

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Build
import android.os.CancellationSignal
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.lastwagon.core.model.GeoPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/** A route endpoint the user has explicitly resolved to coordinates. */
internal data class AddressSelection(val label: String, val point: GeoPoint)

internal val AddressSelectionSaver = listSaver<AddressSelection?, Any>(
    save = { value ->
        if (value == null) emptyList()
        else listOf(value.label, value.point.latitude, value.point.longitude)
    },
    restore = { saved ->
        if (saved.size < 3) null
        else AddressSelection(saved[0] as String, GeoPoint(saved[1] as Double, saved[2] as Double))
    },
)

internal sealed interface AutocompleteState {
    data object Idle : AutocompleteState
    data object Loading : AutocompleteState
    data object NoMatches : AutocompleteState
    data class Suggestions(val suggestions: List<GeocodeSuggestion>) : AutocompleteState
    data class Failed(val message: String) : AutocompleteState
}

/**
 * Debounced search-as-you-type lookups: each keystroke restarts the timer and cancels any
 * in-flight request, so at most one geocoding request fires per typing pause — see the rate
 * limits noted on [AUTOCOMPLETE_DEBOUNCE_MILLIS].
 */
internal class AddressAutocompleteController(
    private val geocoder: GeocodingProvider,
    private val scope: CoroutineScope,
    private val debounceMillis: Long = AUTOCOMPLETE_DEBOUNCE_MILLIS,
) {
    private val _state = MutableStateFlow<AutocompleteState>(AutocompleteState.Idle)
    val state: StateFlow<AutocompleteState> = _state
    private var job: Job? = null

    fun onQueryChanged(text: String, focus: GeoPoint? = null) {
        job?.cancel()
        val query = text.trim()
        if (query.length < AUTOCOMPLETE_MIN_CHARS) {
            _state.value = AutocompleteState.Idle
            return
        }
        job = scope.launch {
            delay(debounceMillis)
            _state.value = AutocompleteState.Loading
            val result = geocoder.autocomplete(query, focus)
            ensureActive()
            _state.value = when (result) {
                is GeocodeLookupResult.Failure -> AutocompleteState.Failed(result.message)
                is GeocodeLookupResult.Success ->
                    if (result.suggestions.isEmpty()) AutocompleteState.NoMatches
                    else AutocompleteState.Suggestions(result.suggestions)
            }
        }
    }

    fun clear() {
        job?.cancel()
        _state.value = AutocompleteState.Idle
    }
}

internal fun Context.hasCoarseLocationPermission() =
    ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
        PackageManager.PERMISSION_GRANTED

/**
 * One-shot approximate device position via the platform LocationManager (no Play Services,
 * so sideloaded builds keep working). Returns null when permission, an enabled provider,
 * a fix, or the timeout fails — callers fall back to manual address entry.
 */
@SuppressLint("MissingPermission")
internal suspend fun Context.currentApproximateLocation(timeoutMillis: Long = 20_000L): GeoPoint? {
    if (!hasCoarseLocationPermission()) return null
    val manager = getSystemService(Context.LOCATION_SERVICE) as? LocationManager ?: return null
    val candidates = buildList {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) add(LocationManager.FUSED_PROVIDER)
        add(LocationManager.NETWORK_PROVIDER)
    }
    val provider = candidates.firstOrNull { candidate ->
        runCatching { candidate in manager.allProviders && manager.isProviderEnabled(candidate) }
            .getOrDefault(false)
    } ?: return null
    return withTimeoutOrNull(timeoutMillis) {
        suspendCancellableCoroutine { continuation ->
            val signal = CancellationSignal()
            continuation.invokeOnCancellation { signal.cancel() }
            try {
                LocationManagerCompat.getCurrentLocation(
                    manager,
                    provider,
                    signal,
                    ContextCompat.getMainExecutor(this@currentApproximateLocation),
                ) { location ->
                    val point = location?.let { GeoPoint(it.latitude, it.longitude) }
                    if (continuation.isActive) continuation.resume(point?.takeIf { it.isValid })
                }
            } catch (_: Exception) {
                if (continuation.isActive) continuation.resume(null)
            }
        }
    }
}

@Composable
internal fun AddressSearchField(
    label: String,
    text: String,
    resolved: Boolean,
    supportingText: String,
    searchState: AutocompleteState,
    onTextChange: (String) -> Unit,
    onSuggestionSelected: (GeocodeSuggestion) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text(label) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = if (!resolved) null else {
                { Icon(Icons.Rounded.CheckCircle, "Location set", tint = MaterialTheme.colorScheme.tertiary) }
            },
            supportingText = {
                Text(
                    supportingText,
                    color = if (resolved) MaterialTheme.colorScheme.tertiary
                    else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            },
        )
        when (searchState) {
            AutocompleteState.Idle -> Unit
            AutocompleteState.Loading -> LinearProgressIndicator(Modifier.fillMaxWidth())
            AutocompleteState.NoMatches -> Text(
                "No matching address found. Check the spelling or add a city and state.",
                color = MaterialTheme.colorScheme.error,
            )
            is AutocompleteState.Failed -> Text(searchState.message, color = MaterialTheme.colorScheme.error)
            is AutocompleteState.Suggestions -> OutlinedCard(Modifier.fillMaxWidth()) {
                Column {
                    searchState.suggestions.forEachIndexed { index, suggestion ->
                        if (index > 0) HorizontalDivider()
                        Text(
                            suggestion.label,
                            Modifier
                                .fillMaxWidth()
                                .clickable { onSuggestionSelected(suggestion) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                        )
                    }
                }
            }
        }
    }
}
