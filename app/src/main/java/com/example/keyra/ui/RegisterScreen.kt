package com.example.keyra.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
fun RegisterScreen(
    onRegisterClick: (String, String) -> Unit = { _, _ -> },
    onLoginRedirectClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var masterPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 360.dp)
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
                Text(text = "Password Manager", fontSize = 20.sp, color = Color.White)
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

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Password Master", fontSize = 15.sp, color = Color.White)
                    OutlinedTextField(
                        value = masterPassword,
                        onValueChange = { masterPassword = it },
                        placeholder = { Text("Enter Password Master", color = Color(0x99FFFFF9), fontSize = 15.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp),
                        visualTransformation = PasswordVisualTransformation(),
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

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Confirm Password Master", fontSize = 15.sp, color = Color.White)
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = { Text("Enter Confirm Password", color = Color(0x99FFFFF9), fontSize = 15.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp),
                        singleLine = true,
                        textStyle = TextStyle(fontSize = 18.sp),
                        visualTransformation = PasswordVisualTransformation(),
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
                        if (!isValidEmail(email)) {
                            Toast.makeText(context, "Format email tidak valid!", Toast.LENGTH_SHORT).show()
                        } else if (masterPassword.length < 6) {
                            Toast.makeText(context, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show()
                        } else if (masterPassword != confirmPassword) {
                            Toast.makeText(context, "Konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show()
                        } else {
                            onRegisterClick(email, masterPassword)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4E8CF7))
                ) {
                    Text(text = "Next", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                }

                TextButton(onClick = onLoginRedirectClick) {
                    Text(text = "Already have an account? Login", fontSize = 14.sp, color = Color.White)
                }
            }
        }
    }
}