package com.example.keyra.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun calcStrength(pw: String): Float {
    if (pw.isEmpty()) return 0f
    var s = pw.length / 32f
    if (pw.any { it.isUpperCase() }) s += 0.15f
    if (pw.any { it.isLowerCase() }) s += 0.15f
    if (pw.any { it.isDigit() }) s += 0.15f
    if (pw.any { !it.isLetterOrDigit() }) s += 0.2f
    return s.coerceIn(0f, 1f)
}

fun strengthLabelAndColor(pw: String, strength: Float): Pair<Color, String> = when {
    pw.isEmpty() -> Color(0xFF334155) to ""
    pw.length < 12 -> Color(0xFFEF4444) to "Weak"
    strength > 0.85f -> Color(0xFF22C55E) to "Very Strong"
    strength > 0.6f -> Color(0xFF22C55E) to "Strong"
    strength < 0.45f -> Color(0xFFEF4444) to "Weak"
    else -> Color(0xFFF59E0B) to "Medium"
}

@Composable
fun StrengthBar(password: String, modifier: Modifier = Modifier) {
    if (password.isEmpty()) return
    val s = calcStrength(password)
    val (c, label) = strengthLabelAndColor(password, s)
    Column(modifier = modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        LinearProgressIndicator(
            progress = { s },
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = c,
            trackColor = Color(0xFF334155)
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "Strength:", color = Color(0xFF94A3B8), fontSize = 12.sp)
            Text(text = label, color = c, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
