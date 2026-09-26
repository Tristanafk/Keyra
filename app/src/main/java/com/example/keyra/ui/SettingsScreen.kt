package com.example.keyra.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BgDark = Color(0xFF0F1115)
private val CardBg = Color(0xFF1A1D23)
private val DividerColor = Color(0xFF2A2F3A)
private val TextMuted = Color(0xFF6B7280)
private val DangerRed = Color(0xFFFCA5A5)

@Composable
fun SettingsScreen(
    biometricEnabled: Boolean = false,
    onBiometricChange: (Boolean) -> Unit = {},
    onHomeClick: () -> Unit = {},
    onSecurityClick: () -> Unit = {},
    onAccountClick: () -> Unit = {},
    onBiometricClick: (Boolean) -> Unit = {},
    onThemeClick: () -> Unit = {},
    onLanguageClick: () -> Unit = {},
    onBackupClick: () -> Unit = {},
    onAboutClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(2) }
    // sinkron dari onboarding: jika onBiometricClick lama dipakai, teruskan ke onBiometricChange
    val onToggle: (Boolean) -> Unit = { v ->
        onBiometricChange(v)
        onBiometricClick(v)
    }

    Scaffold(
        containerColor = BgDark,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E293B),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                windowInsets = NavigationBarDefaults.windowInsets
            ) {
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(text = "Home", fontSize = 12.sp) },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                        onHomeClick()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155)
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Security",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(text = "Security", fontSize = 12.sp) },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                        onSecurityClick()
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155)
                    )
                )
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings",
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    label = { Text(text = "Settings", fontSize = 12.sp) },
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8),
                        indicatorColor = Color(0xFF334155).copy(alpha = 0.7f)
                    )
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Setting",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Person,
                    title = "Account",
                    onClick = onAccountClick
                )
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                SettingsToggleItem(
                    icon = Icons.Default.Fingerprint,
                    title = "Kunci dengan Biometrik",
                    checked = biometricEnabled,
                    onCheckedChange = onToggle
                )
            }

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Palette,
                    title = "Tema",
                    onClick = onThemeClick
                )
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                SettingsItem(
                    icon = Icons.Default.Language,
                    title = "Bahasa",
                    onClick = onLanguageClick
                )
            }

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Download,
                    title = "Backup & Restore",
                    onClick = onBackupClick
                )
                HorizontalDivider(color = DividerColor, thickness = 1.dp)
                SettingsItem(
                    icon = Icons.Default.Info,
                    title = "Tentang Aplikasi",
                    onClick = onAboutClick
                )
            }

            SettingsCard {
                SettingsItem(
                    icon = Icons.Default.Logout,
                    title = "Keluar Akun",
                    titleColor = DangerRed,
                    isDanger = true,
                    onClick = onLogoutClick
                )
            }
        }
    }
}

@Composable
fun SettingsCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            content = content
        )
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    titleColor: Color = Color.White,
    isDanger: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = titleColor,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                color = titleColor,
                fontSize = 15.sp,
                fontWeight = if (isDanger) FontWeight.Bold else FontWeight.Medium
            )
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
fun SettingsToggleItem(
    icon: ImageVector,
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = title,
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF22C55E),
                checkedBorderColor = Color(0xFF22C55E),
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF334155),
                uncheckedBorderColor = Color(0xFF334155)
            )
        )
    }
}