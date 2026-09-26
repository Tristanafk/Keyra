package com.example.keyra.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyra.R

@Composable
fun UnlockScreen(
    email: String,
    onUnlockWithPassword: (String) -> Unit,
    onUnlockWithBiometric: () -> Unit,
    onUseOtherAccount: () -> Unit,
    biometricEnabled: Boolean
) {
    val ctx = LocalContext.current
    var password by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF0F1115)).systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().widthIn(max = 360.dp).padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(painterResource(R.drawable.keyra), "Logo", Modifier.width(95.dp).height(105.dp))
            Text("Selamat Datang Kembali", fontSize = 22.sp, color = Color.White, fontWeight = FontWeight.Bold)
            // email label di atas — sesuai desain vault
            Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFF1E293B)) {
                Text("Masuk sebagai $email", color = Color(0xFF94A3B8), fontSize = 13.sp, modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp))
            }
            OutlinedTextField(
                value = password, onValueChange = { password = it },
                placeholder = { Text("Password Master", color = Color(0x99FFFFF9)) },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true, modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(10.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF334155), unfocusedContainerColor = Color(0xFF334155),
                    focusedBorderColor = Color(0xFF4E8CF7), unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = Color.White, unfocusedTextColor = Color.White
                )
            )
            Button(
                onClick = {
                    if (password.isBlank()) Toast.makeText(ctx,"Password tidak boleh kosong",Toast.LENGTH_SHORT).show()
                    else onUnlockWithPassword(password)
                },
                modifier = Modifier.fillMaxWidth().height(60.dp), shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E8CF7))
            ) { Text("Masuk", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White) }

            if (biometricEnabled) {
                OutlinedButton(
                    onClick = onUnlockWithBiometric,
                    modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp)
                ) { Text("Masuk dengan Sidik Jari", color = Color.White) }
            }
            TextButton(onClick = onUseOtherAccount) { Text("Gunakan akun lain", color = Color(0xFF94A3B8)) }
        }
    }
}
