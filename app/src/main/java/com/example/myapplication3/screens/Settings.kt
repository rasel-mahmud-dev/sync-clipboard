package com.example.myapplication3.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication3.state.AuthState

@Composable
fun SettingsScreen(navHostController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile section
        ProfileSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Dark Mode
        DarkModeToggle()

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Options
        SettingsSection(title = "Profile") {
            SettingsItem(icon = Icons.Default.Person, title = "Edit Profile")
//            SettingsItem(icon = Icons.Default.VpnKey, title = "Change Password")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notifications
        SettingsSection(title = "Notifications") {
            NotificationsToggle()
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Regional Settings
        SettingsSection(title = "Regional") {
            SettingsItem(icon = Icons.Default.Settings, title = "Language")
            SettingsItem(icon = Icons.Default.ExitToApp, title = "Logout")
        }

        Spacer(modifier = Modifier.weight(1f))

        // App version
        Text(
            text = "App ver 2.0.1",
            modifier = Modifier.align(Alignment.CenterHorizontally),
            color = Color.Gray,
            fontSize = 12.sp
        )
    }
}


@Composable
fun ProfileSection() {
    Row(verticalAlignment = Alignment.CenterVertically) {
//        Image(
//            painter = rememberImagePainter(data = "https://example.com/profile.jpg"),
//            contentDescription = "Profile Image",
//            modifier = Modifier
//                .size(64.dp)
//                .clip(CircleShape),
//            contentScale = ContentScale.Crop
//        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(text = "Kapil Mohan", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "Edit personal details", color = Color.Gray, fontSize = 14.sp)
        }
    }
}

@Composable
fun DarkModeToggle() {
    var isDarkModeEnabled by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.FavoriteBorder, contentDescription = "Dark Mode Icon")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Dark Mode", modifier = Modifier.weight(1f))
        Switch(
            checked = isDarkModeEnabled,
            onCheckedChange = { isDarkModeEnabled = it }
        )
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
fun SettingsItem(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = title, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
    }
}

@Composable
fun NotificationsToggle() {
    var isNotificationsEnabled by remember { mutableStateOf(true) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Notifications, contentDescription = "Notifications Icon")
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Notifications", modifier = Modifier.weight(1f))
        Switch(
            checked = isNotificationsEnabled,
            onCheckedChange = { isNotificationsEnabled = it }
        )
    }
}