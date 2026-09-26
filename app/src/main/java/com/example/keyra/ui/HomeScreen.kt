package com.example.keyra.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

data class AccountItem(
    val id: String,
    val name: String,
    val email: String,
    val category: String,
    val isFavorite: Boolean = false,
    val iconUri: String? = null,
    val password: String = "SamplePassword123!",
    val url: String = "",
    val notes: String = ""
)

@Composable
fun HomeScreen(
    accounts: List<AccountItem>,
    onAddPasswordClick: () -> Unit = {},
    onGeneratePasswordClick: () -> Unit = {},
    onSettingsClick: () -> Unit,
    onSecurityClick: () -> Unit = {},
    onLockAppClick: () -> Unit = {},
    onEditAccountClick: (AccountItem) -> Unit = {},
    onDeleteAccount: (AccountItem) -> Unit = {},
    onToggleFavorite: (AccountItem) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedTab by remember { mutableIntStateOf(0) }
    var showAddMenu by remember { mutableStateOf(false) }

    val filteredList = accounts.filter { item: AccountItem ->
        item.name.contains(searchQuery, ignoreCase = true) || item.email.contains(searchQuery, ignoreCase = true)
    }

    val sortedList = filteredList.sortedByDescending { it.isFavorite }

    Scaffold(
        containerColor = Color(0xFF0F1115),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E293B),
                modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home", modifier = Modifier.size(24.dp)) },
                    label = { Text("Home", fontSize = 12.sp) },
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155).copy(alpha = 0.7f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Security, contentDescription = "Security", modifier = Modifier.size(24.dp)) },
                    label = { Text("Security", fontSize = 12.sp) },
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1; onSecurityClick() },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings", modifier = Modifier.size(24.dp)) },
                    label = { Text("Settings", fontSize = 12.sp) },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2; onSettingsClick() },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155)
                    )
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Column(modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp, vertical = 10.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column { Text(text = "Welcome back,", fontSize = 14.sp, color = Color(0xFF94A3B8)); Text(text = "Keyra Vault", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White) }
                    IconButton(onClick = onLockAppClick, modifier = Modifier.background(Color(0xFF334155), CircleShape).size(40.dp)) {
                        Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock App", tint = Color(0xFF4E8CF7), modifier = Modifier.size(20.dp))
                    }
                }

                OutlinedTextField(value = searchQuery, onValueChange = { searchQuery = it }, placeholder = { Text("Cari akun atau password...", color = Color(0xFF94A3B8), fontSize = 14.sp) }, leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color(0xFF94A3B8)) }, modifier = Modifier.fillMaxWidth().height(54.dp), shape = RoundedCornerShape(14.dp), colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color(0xFF1E293B), unfocusedContainerColor = Color(0xFF1E293B), focusedBorderColor = Color(0xFF4E8CF7), unfocusedBorderColor = Color.Transparent, focusedTextColor = Color.White, unfocusedTextColor = Color.White), singleLine = true)

                Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                    Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) { Text(text = "Status Keamanan", fontSize = 13.sp, color = Color(0xFF94A3B8)); Spacer(modifier = Modifier.height(4.dp)); Text(text = "Semua sandi aman & terenkripsi", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White) }
                        Box(modifier = Modifier.background(Color(0xFF4E8CF7).copy(alpha = 0.2f), RoundedCornerShape(8.dp)).padding(horizontal = 10.dp, vertical = 6.dp)) { Text(text = "Aman", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF4E8CF7)) }
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text(text = "Akun Tersimpan (${sortedList.size})", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White) }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.weight(1f).fillMaxWidth(), contentPadding = PaddingValues(bottom = 80.dp)) {
                    items(sortedList) { item ->
                        var showItemMenu by remember { mutableStateOf(false) }
                        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))) {
                            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                    Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF0F1115)).border(1.dp, Color(0xFF4E8CF7).copy(alpha = 0.2f), CircleShape), contentAlignment = Alignment.Center) {
                                        if (item.iconUri != null) AsyncImage(model = item.iconUri, contentDescription = null, modifier = Modifier.fillMaxSize().clip(CircleShape), contentScale = ContentScale.Crop)
                                        else Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF4E8CF7), modifier = Modifier.size(20.dp))
                                    }
                                    Column { Text(text = item.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White); Spacer(modifier = Modifier.height(2.dp)); Text(text = item.email, fontSize = 13.sp, color = Color(0xFF94A3B8)) }
                                }
                                Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.wrapContentWidth()) {
                                    IconButton(onClick = { onToggleFavorite(item) }, modifier = Modifier.size(40.dp)) {
                                        Icon(imageVector = if (item.isFavorite) Icons.Default.Star else Icons.Default.StarOutline, contentDescription = "Favorite", tint = if (item.isFavorite) Color(0xFFFFC107) else Color(0xFF94A3B8), modifier = Modifier.size(24.dp))
                                    }
                                    Box {
                                        IconButton(onClick = { showItemMenu = true }) { Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Opsi", tint = Color(0xFF94A3B8)) }
                                        DropdownMenu(expanded = showItemMenu, onDismissRequest = { showItemMenu = false }, modifier = Modifier.background(Color(0xFF1E293B))) {
                                            DropdownMenuItem(text = { Text("Edit", color = Color.White) }, onClick = { showItemMenu = false; onEditAccountClick(item) }, leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White) })
                                            DropdownMenuItem(text = { Text("Delete", color = Color.Red) }, onClick = { onDeleteAccount(item); showItemMenu = false }, leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) })
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            Box(modifier = Modifier.fillMaxSize().padding(end = 20.dp, bottom = 16.dp), contentAlignment = Alignment.BottomEnd) {
                Box(modifier = Modifier.align(Alignment.BottomEnd).padding(bottom = 64.dp, end = 4.dp), contentAlignment = Alignment.BottomEnd) {
                    AnimatedVisibility(visible = showAddMenu, enter = scaleIn(animationSpec = tween(durationMillis = 200), transformOrigin = TransformOrigin(1f, 1f)) + fadeIn(animationSpec = tween(durationMillis = 200)), exit = scaleOut(animationSpec = tween(durationMillis = 150), transformOrigin = TransformOrigin(1f, 1f)) + fadeOut(animationSpec = tween(durationMillis = 150))) {
                        Surface(modifier = Modifier.width(200.dp).clip(RoundedCornerShape(14.dp)), color = Color(0xFF1E293B), shadowElevation = 8.dp) {
                            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                                DropdownMenuItem(text = { Text("Add password", color = Color.White) }, onClick = { showAddMenu = false; onAddPasswordClick() }, leadingIcon = { Icon(Icons.Default.Folder, contentDescription = null, tint = Color.White) })
                                DropdownMenuItem(text = { Text("Generate password", color = Color.White) }, onClick = { showAddMenu = false; onGeneratePasswordClick() }, leadingIcon = { Icon(Icons.Default.Casino, contentDescription = null, tint = Color.White) })
                            }
                        }
                    }
                }
                FloatingActionButton(onClick = { showAddMenu = !showAddMenu }, containerColor = Color(0xFF4E8CF7), contentColor = Color.White, shape = CircleShape) { Icon(imageVector = Icons.Default.Add, contentDescription = "Menu Tambah", modifier = Modifier.size(28.dp)) }
            }
        }
    }
}
