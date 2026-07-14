package com.trkrhlpr.core.designsystem

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable fun IndustrialBackdrop(modifier: Modifier = Modifier, content: @Composable BoxScope.() -> Unit) {
    Box(modifier.background(MaterialTheme.colorScheme.background)) {
        Canvas(Modifier.matchParentSize().semantics { hideFromAccessibility() }) {
            drawRect(Brush.verticalGradient(listOf(Color.Transparent, TrkrColors.Graphite800.copy(alpha = .28f))))
            var y = -40f
            while (y < size.height) {
                drawLine(TrkrColors.MarkerAmber.copy(alpha = .1f), Offset(size.width * .86f, y),
                    Offset(size.width * .86f, y + 28f), 3f)
                y += 52f
            }
        }
        content()
    }
}

@Composable fun TrkrCard(
    modifier: Modifier = Modifier, onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    val base = modifier.clip(MaterialTheme.shapes.medium)
        .background(Brush.linearGradient(listOf(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.colorScheme.surface)))
        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = .28f), MaterialTheme.shapes.medium)
    Column((if (onClick == null) base else base.clickable(onClick = onClick)).padding(TrkrSpacing.md),
        verticalArrangement = Arrangement.spacedBy(TrkrSpacing.sm), content = content)
}

@Composable fun FeatureTile(
    title: String, subtitle: String, icon: ImageVector, accent: Color, modifier: Modifier = Modifier,
    enabled: Boolean = true, badge: String? = null, onClick: () -> Unit,
) {
    TrkrCard(modifier.heightIn(min = 156.dp), if (enabled) onClick else null) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Box(Modifier.size(44.dp).background(accent.copy(alpha = .14f), MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = if (enabled) accent else MaterialTheme.colorScheme.outline)
            }
            badge?.let {
                Surface(color = accent.copy(alpha = .12f), shape = CircleShape) {
                    Text(it.uppercase(), Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                        style = MaterialTheme.typography.labelMedium, color = accent)
                }
            }
        }
        Spacer(Modifier.weight(1f))
        Text(title, style = MaterialTheme.typography.titleLarge,
            color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline)
        Text(subtitle, style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}

@Composable fun SectionHeader(eyebrow: String, title: String, supporting: String? = null) {
    Column(verticalArrangement = Arrangement.spacedBy(TrkrSpacing.xs)) {
        Text(eyebrow.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        Text(title, style = MaterialTheme.typography.headlineLarge)
        supporting?.let { Text(it, style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant) }
    }
}

@Composable fun TrkrProgress(value: Float, label: String, modifier: Modifier = Modifier) {
    val animated by animateFloatAsState(
        value.coerceIn(0f, 1f),
        animationSpec = if (LocalReduceMotion.current) snap() else tween(180),
        label = "progress",
    )
    Column(modifier, verticalArrangement = Arrangement.spacedBy(TrkrSpacing.xs)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(((animated * 100).toInt()).toString() + "%", style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary)
        }
        LinearProgressIndicator({ animated }, Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = MaterialTheme.colorScheme.primary, trackColor = MaterialTheme.colorScheme.surfaceVariant)
    }
}

@Composable fun ChecklistRow(
    title: String, supporting: String, complete: Boolean, expanded: Boolean,
    onToggleComplete: () -> Unit, onToggleExpanded: () -> Unit, modifier: Modifier = Modifier,
) {
    val border by animateColorAsState(
        if (complete) TrkrColors.SignalGreen else MaterialTheme.colorScheme.outline.copy(alpha = .25f),
        animationSpec = if (LocalReduceMotion.current) snap() else tween(160),
        label = "checklistBorder")
    Column(modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium).border(1.dp, border, MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.surface)
        .animateContentSize(if (LocalReduceMotion.current) snap() else tween(180))) {
        Row(Modifier.fillMaxWidth().clickable(onClick = onToggleExpanded).padding(TrkrSpacing.md),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.sm)) {
            IconButton(onToggleComplete, Modifier.size(48.dp).semantics {
                stateDescription = if (complete) "Complete" else "Not complete"
            }) {
                Icon(if (complete) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    if (complete) "Mark incomplete" else "Mark complete",
                    tint = if (complete) TrkrColors.SignalGreen else MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(if (complete) "Complete" else "Tap to review", style = MaterialTheme.typography.labelMedium,
                    color = if (complete) TrkrColors.SignalGreen else MaterialTheme.colorScheme.primary)
            }
            Icon(if (expanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore, "Show details")
        }
        if (expanded) {
            HorizontalDivider(color = border.copy(alpha = .5f))
            Text(supporting, Modifier.padding(TrkrSpacing.md), style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable fun AnswerOption(
    label: String, selected: Boolean, correct: Boolean? = null, enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val color = when (correct) {
        true -> TrkrColors.SignalGreen; false -> TrkrColors.BrakeRed
        null -> if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    }
    Row(Modifier.fillMaxWidth().heightIn(min = 56.dp).clip(MaterialTheme.shapes.small)
        .border(if (selected || correct != null) 2.dp else 1.dp, color.copy(alpha = .8f), MaterialTheme.shapes.small)
        .clickable(enabled = enabled, onClick = onClick).padding(TrkrSpacing.md),
        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(TrkrSpacing.sm)) {
        Icon(if (selected) Icons.Rounded.RadioButtonChecked else Icons.Rounded.RadioButtonUnchecked, null, tint = color)
        Text(label, Modifier.weight(1f), style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable fun StatePanel(
    title: String, message: String, icon: ImageVector, tint: Color = MaterialTheme.colorScheme.secondary,
    actionLabel: String? = null, onAction: (() -> Unit)? = null,
) {
    TrkrCard(Modifier.fillMaxWidth()) {
        Icon(icon, null, tint = tint, modifier = Modifier.size(32.dp))
        Text(title, style = MaterialTheme.typography.titleLarge)
        Text(message, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        if (actionLabel != null && onAction != null) {
            Button(onAction, Modifier.heightIn(min = 52.dp)) { Text(actionLabel) }
        }
    }
}

@Composable fun LoadingPanel(label: String = "Loading") {
    TrkrCard(Modifier.fillMaxWidth().semantics { contentDescription = label }) {
        LinearProgressIndicator(Modifier.fillMaxWidth())
        Text(label, style = MaterialTheme.typography.bodyMedium)
    }
}
