package com.example.keyra.ui

import android.content.ClipboardManager
import android.content.ClipData
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import java.security.SecureRandom
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDark = Color(0xFF0F172A)
private val SurfaceDark = Color(0xFF1E293B)
private val PrimaryBlue = Color(0xFF3B82F6)
private val TextMuted = Color(0xFF94A3B8)

private fun genPw(len: Int, up: Boolean, low: Boolean, num: Boolean, sym: Boolean): String {
    val pool = buildString {
        if (up) append("ABCDEFGHIJKLMNOPQRSTUVWXYZ")
        if (low) append("abcdefghijklmnopqrstuvwxyz")
        if (num) append("0123456789")
        if (sym) append("!@#$%^&*()-_=+[]{}|;:,.<>?")
    }.ifEmpty { "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" }
    val r = SecureRandom()
    return (0 until len.coerceIn(8, 32)).map { pool[r.nextInt(pool.length)] }.joinToString("")
}

private val wordList = listOf("apple","river","stone","forest","cloud","silver","amber","winter","crown","harbor","ember","thunder","velvet","cipher","garden","mirror","ocean","bridge","crystal","hunter","falcon","meadow","summit","whisper")
private fun genPassPhrase(caps: Boolean, useNum: Boolean, sep: String, wc: Int): String {
    val r = SecureRandom()
    val picked = (0 until wc.coerceIn(3, 6)).map { wordList[r.nextInt(wordList.size)] }
    val base = picked.joinToString(if (sep == "Space") " " else sep.ifEmpty { "-" })
    val cap = if (caps) base.split(if (sep == "Space") " " else sep.ifEmpty { "-" }).joinToString(if (sep == "Space") " " else sep.ifEmpty { "-" }) { it.replaceFirstChar(Char::uppercaseChar) } else base
    return if (useNum) "$cap${if (sep == "Space") " " else sep.ifEmpty { "-" }}${r.nextInt(90) + 10}" else cap
}

@Composable
fun GeneratePasswordScreen(
    onBackClick: () -> Unit = {},
    onUsePassword: (String) -> Unit = {},
    onCopyPassword: (String) -> Unit = {}
) {
    var generatedPassword by remember { mutableStateOf(genPw(16, true, true, true, true)) }
    var passwordType by remember { mutableStateOf("Password") }
    var length by remember { mutableStateOf(16f) }
    var words by remember { mutableStateOf(4f) }
    var uppercase by remember { mutableStateOf(true) }
    var lowercase by remember { mutableStateOf(true) }
    var numbers by remember { mutableStateOf(true) }
    var symbols by remember { mutableStateOf(true) }
    var passCap by remember { mutableStateOf(true) }
    var passNum by remember { mutableStateOf(false) }
    var sep by remember { mutableStateOf("-") }
    var typeExpanded by remember { mutableStateOf(false) }
    var visible by remember { mutableStateOf(true) }
    val ctx = LocalContext.current
    fun regen() {
        generatedPassword = if (passwordType == "Passphrase") genPassPhrase(passCap, passNum, sep, words.toInt())
        else genPw(length.toInt(), uppercase, lowercase, numbers, symbols)
    }
    LaunchedEffect(length, words, uppercase, lowercase, numbers, symbols, passCap, passNum, sep, passwordType) { regen() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = BgDark
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
        ) {
            // Bagian Atas: Header & Generator Card (scrollable biar muat di layar kecil)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header (Back button & Title)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Generator Password",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Password Generated Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceDark)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "PASSWORD GENERATED",
                            color = TextMuted,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = if (visible) generatedPassword else "•".repeat(generatedPassword.length), color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Visibility.let { if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility }, contentDescription = "Toggle", tint = TextMuted, modifier = Modifier.size(20.dp).clickable { visible = !visible })
                                Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = TextMuted, modifier = Modifier.size(20.dp).clickable { regen() })
                            }
                        }

                        val s = calcStrength(generatedPassword)
                        val (sc, st) = strengthLabelAndColor(generatedPassword, s)
                        LinearProgressIndicator(progress = { s }, modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)), color = sc, trackColor = Color(0xFF334155))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Strength:", color = TextMuted, fontSize = 12.sp)
                            Text(text = st, color = sc, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "type", color = Color.White, fontSize = 16.sp)
                    Box {
                        Box(
                            modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(SurfaceDark).clickable { typeExpanded = !typeExpanded }.padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = passwordType, color = Color.White, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                            }
                        }
                        DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }, modifier = Modifier.background(SurfaceDark)) {
                            DropdownMenuItem(text = { Text("Password", color = Color.White) }, onClick = { passwordType = "Password"; typeExpanded = false })
                            DropdownMenuItem(text = { Text("Passphrase", color = Color.White) }, onClick = { passwordType = "Passphrase"; typeExpanded = false })
                        }
                    }
                }

                if (passwordType == "Password") {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Length", color = Color.White, fontSize = 14.sp)
                            Text(text = "${length.toInt()} karakter", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(value = length, onValueChange = { length = it }, valueRange = 8f..32f, colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = PrimaryBlue, inactiveTrackColor = SurfaceDark))
                    }
                    Text(text = "Character Settings", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
                    SettingToggleItem(label = "Huruf Besar (A-Z)", checked = uppercase) { uppercase = it }
                    SettingToggleItem(label = "Huruf Kecil (a-z)", checked = lowercase) { lowercase = it }
                    SettingToggleItem(label = "Angka (0-9)", checked = numbers) { numbers = it }
                    SettingToggleItem(label = "Simbol (!@#$%^&*)", checked = symbols) { symbols = it }
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "Words", color = Color.White, fontSize = 14.sp)
                            Text(text = "${words.toInt()} kata", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        }
                        Slider(value = words, onValueChange = { words = it }, valueRange = 3f..6f, steps = 2, colors = SliderDefaults.colors(thumbColor = Color.White, activeTrackColor = PrimaryBlue, inactiveTrackColor = SurfaceDark))
                    }
                    Text(text = "Passphrase Settings", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 4.dp, bottom = 4.dp))
                    SettingToggleItem(label = "Capitalize", checked = passCap) { passCap = it }
                    SettingToggleItem(label = "Number", checked = passNum) { passNum = it }
                    Text(text = "Separator", color = TextMuted, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth().padding(top = 6.dp)) {
                        listOf("-", ".", "_", "!", "?", "Space").forEach { s ->
                            FilterChip(selected = sep == s, onClick = { sep = s }, label = { Text(if (s == "Space") "␣" else s, fontSize = 13.sp) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = PrimaryBlue, selectedLabelColor = Color.White, containerColor = SurfaceDark, labelColor = TextMuted))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }

            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp).navigationBarsPadding(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(onClick = { onUsePassword(generatedPassword) }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)) {
                    Text(text = "Use Password", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
                Button(onClick = {
                    val cm = ctx.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    cm.setPrimaryClip(ClipData.newPlainText("password", generatedPassword))
                    android.widget.Toast.makeText(ctx, "Copied", android.widget.Toast.LENGTH_SHORT).show()
                    onCopyPassword(generatedPassword)
                }, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = SurfaceDark)) {
                    Text(text = "Copy Password", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SettingToggleItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically // Pastikan ini di dalam Row
    ) {
        Text(text = label, color = TextMuted, fontSize = 14.sp)
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = PrimaryBlue,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = SurfaceDark
            )
        )
    }
}