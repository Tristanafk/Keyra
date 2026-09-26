package com.example.keyra.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.keyra.R

@Composable
fun ForgotPasswordScreen(
    onBackClick: () -> Unit = {},
    onResetClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }

    fun isValidEmail(target: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F1115))
            .systemBarsPadding()
            .imePadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.keyra),
                    contentDescription = "Logo Keyra",
                    modifier = Modifier.width(95.dp).height(105.dp)
                )
                Text(text = "Keyra", fontSize = 33.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = "Reset Password", fontSize = 20.sp, color = Color.White)
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Masukkan email Anda untuk mereset password master",
                    fontSize = 14.sp,
                    color = Color(0xFF94A3B8),
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Email", fontSize = 15.sp, color = Color.White)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("name@gmail.com", color = Color(0x99FFFFF9), fontSize = 15.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color(0xFF334155),
                            unfocusedContainerColor = Color(0xFF334155),
                            focusedBorderColor = Color(0xFF4E8CF7),
                            unfocusedBorderColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = {
                        if (email.isBlank()) {
                            Toast.makeText(context, "Email tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                        } else if (!isValidEmail(email)) {
                            Toast.makeText(context, "Format email salah!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Link reset telah dikirim ke email Anda", Toast.LENGTH_SHORT).show()
                            onResetClick()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E8CF7))
                ) {
                    Text(text = "Kirim Link Reset", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }

                TextButton(onClick = onBackClick) {
                    Text(text = "Kembali ke Login", fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}