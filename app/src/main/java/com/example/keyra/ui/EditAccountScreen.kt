package com.example.keyra.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import com.example.keyra.ui.StrengthBar

@Composable
fun EditAccountScreen(
    account: AccountItem,
    onBackClick: () -> Unit = {},
    onSaveClick: (AccountItem) -> Unit = {}
) {
    var accountName by remember { mutableStateOf(account.name) }
    var username by remember { mutableStateOf(account.email) }
    var password by remember { mutableStateOf(account.password) }
    var websiteUrl by remember { mutableStateOf(account.url) }
    var notes by remember { mutableStateOf(account.notes) }
    var errorMsg by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val favUrl = remember(websiteUrl) {
        val raw = websiteUrl.trim()
        val dom = raw.substringAfter("://").substringBefore("/").substringBefore("?").substringBefore("#")
        if (dom.contains(".")) "https://www.google.com/s2/favicons?domain=$dom&sz=128" else null
    }
    
    fun validateAndSave() {
        when {
            accountName.trim().isEmpty() -> errorMsg = "Nama akun wajib diisi"
            password.trim().isEmpty() -> errorMsg = "Password wajib diisi"
            else -> {
                errorMsg = ""
                onSaveClick(account.copy(name = accountName, email = username, password = password, url = websiteUrl, notes = notes, iconUri = favUrl ?: account.iconUri))
            }
        }
    }

    val isFormValid = accountName.trim().isNotEmpty() && password.trim().isNotEmpty()

    Scaffold(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        containerColor = Color(0xFF0F1115),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.background(Color(0xFF1E293B), CircleShape).size(40.dp)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Kembali", tint = Color.White, modifier = Modifier.size(20.dp))
                }
                Text(text = "Edit Akun", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                IconButton(
                    onClick = { validateAndSave() },
                    modifier = Modifier.background(
                        if (isFormValid) Color(0xFF3B82F6) else Color(0xFF334155),
                        CircleShape
                    ).size(40.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = "Simpan", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)) {
                val displayUrl = favUrl ?: account.iconUri
                Box(
                    modifier = Modifier.size(84.dp).shadow(8.dp, CircleShape).clip(CircleShape).background(Color(0xFF1E293B), CircleShape).border(1.dp, Color(0xFF4E8CF7).copy(alpha = 0.25f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    if (displayUrl != null) AsyncImage(model = displayUrl, contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                    else Icon(Icons.Default.Key, contentDescription = "Ikon Kunci", tint = Color(0xFF4E8CF7), modifier = Modifier.size(36.dp))
                }
                Text(text = "Pilih Ikon", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF94A3B8))
            }
            if (errorMsg.isNotEmpty()) {
                Text(text = errorMsg, fontSize = 12.sp, color = Color(0xFFFF6B9D), modifier = Modifier.padding(horizontal = 4.dp))
            }
            EditFieldGroup(label = "Nama Akun", value = accountName, onValueChange = { accountName = it }, placeholder = "Contoh: Instagram", isError = errorMsg.contains("Nama akun"))
            EditFieldGroup(label = "Username", value = username, onValueChange = { username = it }, placeholder = "nama_pengguna")
            EditFieldGroup(label = "Password", value = password, onValueChange = { password = it }, placeholder = "", isPassword = true, passwordVisible = passwordVisible, onPasswordVisibilityToggle = { passwordVisible = !passwordVisible }, isError = errorMsg.contains("Password"))
            StrengthBar(password = password)
            EditFieldGroup(label = "URL Website", value = websiteUrl, onValueChange = { websiteUrl = it }, placeholder = "https://example.com")
            EditFieldGroup(label = "Catatan", value = notes, onValueChange = { notes = it }, placeholder = "Tambahkan detail tambahan...", singleLine = false)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EditFieldGroup(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onPasswordVisibilityToggle: () -> Unit = {},
    singleLine: Boolean = true,
    isError: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = label, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFFE2E8F0))
        TextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { if (placeholder.isNotEmpty()) Text(text = placeholder, color = Color(0xFF64748B), fontSize = 14.sp) },
            modifier = Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 52.dp)
                .imePadding(),
            shape = RoundedCornerShape(12.dp),
            textStyle = TextStyle(fontSize = 15.sp, color = Color.White),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color(0xFF1E293B),
                unfocusedContainerColor = Color(0xFF1E293B),
                disabledContainerColor = Color(0xFF1E293B),
                errorContainerColor = Color(0xFF3A1E2E),
                focusedIndicatorColor = Color(0xFF4E8CF7),
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color(0xFFFF6B9D),
                cursorColor = Color(0xFF4E8CF7),
                errorCursorColor = Color(0xFFFF6B9D),
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                disabledTextColor = Color.White.copy(alpha = 0.6f),
                errorTextColor = Color.White
            ),
            visualTransformation = if (isPassword && !passwordVisible && value.isNotEmpty()) PasswordVisualTransformation() else VisualTransformation.None,
            singleLine = singleLine,
            isError = isError,
            trailingIcon = if (isPassword && value.isNotEmpty()) {
                {
                    IconButton(onClick = onPasswordVisibilityToggle, modifier = Modifier.size(36.dp)) {
                        Icon(imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff, contentDescription = null, tint = Color(0xFF94A3B8), modifier = Modifier.size(20.dp))
                    }
                }
            } else null
        )
    }
}
