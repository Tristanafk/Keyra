package com.example.keyra.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SecurityScreen(
    accounts: List<AccountItem> = emptyList(),
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onEditClick: (AccountItem) -> Unit = {}
) {
    var detailType by remember { mutableStateOf<String?>(null) }
    
    val weakAccounts = accounts.filter { acc -> calcStrength(acc.password) < 0.45f }
    val mediumAccounts = accounts.filter { acc ->
        val s = calcStrength(acc.password)
        s >= 0.45f && s <= 0.6f
    }
    val strongAccounts = accounts.filter { acc -> calcStrength(acc.password) > 0.6f }
    
    val weak = weakAccounts.size
    val medium = mediumAccounts.size
    val strong = strongAccounts.size
    val total = accounts.size

    Scaffold(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        containerColor = Color(0xFF0F1115),
        topBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.background(Color(0xFF1E293B), CircleShape).size(40.dp).clickable { 
                        if (detailType != null) detailType = null else onBackClick() 
                    },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Kembali",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = detailType ?: "Security",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1E293B),
                modifier = Modifier.fillMaxWidth().navigationBarsPadding()
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = false,
                    onClick = onHomeClick,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Security, contentDescription = "Security") },
                    label = { Text("Security") },
                    selected = true,
                    onClick = { detailType = null },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF4E8CF7),
                        selectedTextColor = Color(0xFF4E8CF7),
                        indicatorColor = Color(0xFF334155).copy(alpha = 0.7f)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = onSettingsClick,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    )
                )
            }
        }
    ) { innerPadding ->
        if (detailType == null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.weight(1.2f),
                            contentAlignment = Alignment.Center
                        ) {
                            DonutChart(weak = weak, medium = medium, strong = strong)
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text(text = "Total", fontSize = 11.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Medium)
                                Text(text = "$total", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text(text = "Password", fontSize = 10.sp, color = Color(0xFF94A3B8))
                            }
                        }
                        Column(modifier = Modifier.weight(0.8f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            LegendItem(color = Color(0xFF22C55E), label = "Kuat", count = strong)
                            LegendItem(color = Color(0xFFF59E0B), label = "Sedang", count = medium)
                            LegendItem(color = Color(0xFFEF4444), label = "Lemah", count = weak)
                        }
                    }
                }

                Text(text = "Ringkasan Kategori", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(top = 8.dp))

                CategoryItem(
                    icon = Icons.Default.ErrorOutline,
                    iconBg = Color(0xFFEF4444).copy(alpha = 0.2f),
                    iconTint = Color(0xFFEF4444),
                    title = "Password Lemah",
                    count = weak,
                    detail = "Perlu tindakan segera",
                    onClick = { if (weak > 0) detailType = "Password Lemah" }
                )
                CategoryItem(
                    icon = Icons.Default.WarningAmber,
                    iconBg = Color(0xFFF59E0B).copy(alpha = 0.2f),
                    iconTint = Color(0xFFF59E0B),
                    title = "Password Sedang",
                    count = medium,
                    detail = "Bisa ditingkatkan",
                    onClick = { if (medium > 0) detailType = "Password Sedang" }
                )
                CategoryItem(
                    icon = Icons.Default.CheckCircleOutline,
                    iconBg = Color(0xFF22C55E).copy(alpha = 0.2f),
                    iconTint = Color(0xFF22C55E),
                    title = "Password Kuat",
                    count = strong,
                    detail = "Keamanan optimal",
                    onClick = { if (strong > 0) detailType = "Password Kuat" }
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        } else {
            val listToShow = when(detailType) {
                "Password Lemah" -> weakAccounts
                "Password Sedang" -> mediumAccounts
                else -> strongAccounts
            }
            val isEditable = detailType != "Password Kuat"
            
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(listToShow) { acc ->
                    AccountSmallItem(
                        acc = acc,
                        isEditable = isEditable,
                        onEditClick = { if (isEditable) onEditClick(acc) }
                    )
                }
            }
        }
    }
}

@Composable
fun DonutChart(weak: Int, medium: Int, strong: Int) {
    val total = (weak + medium + strong).toFloat()
    androidx.compose.foundation.Canvas(modifier = Modifier.size(130.dp)) {
        val strokeWidth = 14.dp.toPx()
        val innerSize = size.minDimension - strokeWidth
        
        if (total == 0f) {
            drawArc(
                color = Color(0xFF334155),
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                size = androidx.compose.ui.geometry.Size(innerSize, innerSize),
                topLeft = androidx.compose.ui.geometry.Offset(strokeWidth/2, strokeWidth/2)
            )
        } else {
            var startAngle = -90f
            
            // Draw Strong (Green)
            val sweepStrong = (strong / total) * 360f
            if (strong > 0) {
                drawArc(
                    color = Color(0xFF22C55E),
                    startAngle = startAngle,
                    sweepAngle = sweepStrong,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    size = androidx.compose.ui.geometry.Size(innerSize, innerSize),
                    topLeft = androidx.compose.ui.geometry.Offset(strokeWidth/2, strokeWidth/2)
                )
                startAngle += sweepStrong
            }
            
            // Draw Medium (Orange)
            val sweepMedium = (medium / total) * 360f
            if (medium > 0) {
                drawArc(
                    color = Color(0xFFF59E0B),
                    startAngle = startAngle,
                    sweepAngle = sweepMedium,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    size = androidx.compose.ui.geometry.Size(innerSize, innerSize),
                    topLeft = androidx.compose.ui.geometry.Offset(strokeWidth/2, strokeWidth/2)
                )
                startAngle += sweepMedium
            }
            
            // Draw Weak (Red)
            val sweepWeak = (weak / total) * 360f
            if (weak > 0) {
                drawArc(
                    color = Color(0xFFEF4444),
                    startAngle = startAngle,
                    sweepAngle = sweepWeak,
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth, cap = androidx.compose.ui.graphics.StrokeCap.Round),
                    size = androidx.compose.ui.geometry.Size(innerSize, innerSize),
                    topLeft = androidx.compose.ui.geometry.Offset(strokeWidth/2, strokeWidth/2)
                )
            }
        }
    }
}

@Composable
fun AccountSmallItem(
    acc: AccountItem,
    isEditable: Boolean = true,
    onEditClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isEditable) { onEditClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFF0F1115)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF4E8CF7), modifier = Modifier.size(18.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = acc.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text(text = acc.email, fontSize = 12.sp, color = Color(0xFF94A3B8))
            }
            if (isEditable) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = "Edit",
                    tint = Color(0xFF4E8CF7),
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String, count: Int, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.size(10.dp).clip(CircleShape).background(color)
        )
        Column {
            Text(text = label, fontSize = 11.sp, color = Color(0xFF94A3B8))
            Text(text = "$count Password", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun CategoryItem(icon: ImageVector, iconBg: Color, iconTint: Color, title: String, count: Int, detail: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp)).background(iconBg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = title, tint = iconTint, modifier = Modifier.size(22.dp))
                }
                Column {
                    Text(text = title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = Color.White)
                    Text(text = detail, fontSize = 12.sp, color = Color(0xFF94A3B8))
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "$count", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                    contentDescription = null,
                    tint = Color(0xFF94A3B8),
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

