package com.example.myapplication3.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.myapplication3.firebase.FirestoreInstance
import com.example.myapplication3.state.AuthState
import com.example.myapplication3.state.Clip
import com.example.myapplication3.state.ClipboardState
import java.util.ArrayList
import java.util.Date

data class MenuItem(
    val id: Int,
    val label: String,
    val icon: @Composable () -> Unit,
    val onClick: () -> Unit
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(context: Context, navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }


    val menuItems = listOf(
        MenuItem(
            id = 1,
            label = "Configuration",
            icon = { Icon(Icons.Default.Settings, contentDescription = null) }, // Example icon
            onClick = { /* Handle Configuration click */ }
        ),
        MenuItem(
            id = 2,
            label = "Settings",
            icon = { Icon(Icons.Default.Settings, contentDescription = null) }, // Example icon
            onClick = {
                navController.navigate("settings")
            }
        ),
        MenuItem(
            id = 3,
            label = "Logout",
            icon = { Icon(Icons.Default.ExitToApp, contentDescription = null) }, // Example icon
            onClick = {
                AuthState.clearAuthInfo()
                navController.navigate("login")
            }
        )
    )


    TopAppBar(
        title = {
            Text(text = "Clip", color = Color.White)
        },
        navigationIcon = {
            IconButton(onClick = { showMenu = !showMenu }) {
                Text(
                    AuthState.auth?.username?.substring(0, 2).toString().uppercase(),
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )


//                Image(
//                    painter = painterResource(id = R.drawable.user_icon), // Replace with your user icon
//                    contentDescription = "User Icon",
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clip(MaterialTheme.shapes.medium)
//                )
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(Color(0xFFA9A1B9))
            ) {

                menuItems.forEach { item ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                item.icon() // Display the icon
                                Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
                                Text(item.label)
                            }
                        },
                        onClick = {
                            item.onClick() // Handle the click event
                            showMenu = false // Optionally dismiss the menu
                        },
                        modifier = Modifier.background(Color(0xfffff))
                    )
                }
            }
        },

        actions = {
            IconButton(onClick = { navController.navigate("settings") }) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Settings Icon",
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.secondary)
    )
}


@Composable
fun ClipListScreen(navController: NavHostController) {
    val date = Date()

    val context = LocalContext.current
    val firestore = FirestoreInstance.getInstance
    val clipsCollection = firestore.collection("clips")
    val text = remember { mutableStateOf(date.time.toString()) }

    LaunchedEffect(Unit) {
        clipsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                val clipList = arrayListOf<Clip>()  // Initialize array list correctly
                querySnapshot.documents.mapNotNull { document ->
                    val clip = Clip(
                        content = document.data?.get("content")?.toString() ?: "",
                        userId = document.data?.get("userId")?.toString() ?: "",
                        timestamp = document.get("timestamp") as? com.google.firebase.Timestamp
                            ?: com.google.firebase.Timestamp.now(),
                        sharedWith = document.get("sharedWith") as? ArrayList<String>
                            ?: arrayListOf(),
                        type = "text"
                    )
                    clipList.add(clip)
                }
                ClipboardState.setClipboardInfo(clipList, context)
            }
            .addOnFailureListener { e ->
                println("Error getting documents: $e")

            }

    }

    fun handleAddText() {
        val clipData = hashMapOf(
            "userId" to "userId1", // Replace with actual user ID
            "content" to "This is a clipboard content.", // Replace with actual clipboard content
            "timestamp" to System.currentTimeMillis(),
            "sharedWith" to listOf("linux_pc", "windows pc") // Replace with actual shared devices
        )

        clipsCollection.add(clipData)
            .addOnSuccessListener { documentReference ->
                // After adding, fetch the document data
                documentReference.get()
                    .addOnSuccessListener { documentSnapshot ->
                        if (documentSnapshot.exists()) {
                            val content = documentSnapshot.getString("content") ?: ""
                            val userId = documentSnapshot.getString("userId") ?: ""
                            val timestamp = documentSnapshot.getLong("timestamp") ?: 0L
                            val sharedWith =
                                documentSnapshot.get("sharedWith") as? List<String> ?: emptyList()

                            // Show success toast
                            Toast.makeText(
                                context,
                                "Document added successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Show failure toast
                        Toast.makeText(
                            context,
                            "Failed to retrieve document: $e",
                            Toast.LENGTH_SHORT
                        ).show()
                        println("Failed to retrieve document: $e")
                    }
            }
            .addOnFailureListener { e ->
                // Show error toast
                Toast.makeText(context, "Error adding document: $e", Toast.LENGTH_SHORT).show()
                println("Error adding document: $e")
            }
    }



    Scaffold(
        topBar = {
            AppBar(context, navController)
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                val clipItems = listOf(
                    ClipItem(
                        type = "Image",
                        time = "2h",
                        content = "https://example.com/image1.jpg"
                    ),
                    ClipItem(
                        type = "Text",
                        time = "20m",
                        content = "https://rileytestut.com/blog/2020/06/17/introducing-clip"
                    ),
                    ClipItem(
                        type = "Text",
                        time = "1h",
                        content = "Apple paid 0 tributes to Warren Buffet's Paper Wizard"
                    )
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BasicTextField(
                        value = text.value,
                        onValueChange = { it -> text.value = it },
                        textStyle = TextStyle(color = Color.White)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { handleAddText() }, colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE06690)
                        )
                    ) {
                        Text("Add")
                    }
                }

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                ) {
                    items(ClipboardState.clips.value) { clip ->
                        ClipCard(clipItem = clip)
                    }
                }


            }
        }
    )
}

@Composable
fun ClipCard(clipItem: Clip) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondary)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = clipItem.type, style = MaterialTheme.typography.bodyLarge)
                Text(
                    text = clipItem.timestamp.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            when (clipItem.type) {
//                "Image" -> ImageCard(imageUrl = clipItem.content)
                "Text" -> TextCard(text = clipItem.content)
            }
        }
    }
}

//@Composable
//fun ImageCard(imageUrl: String) {
//    Image(
//        painter = rememberImagePainter(data = imageUrl),
//        contentDescription = null,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .clip(RoundedCornerShape(8.dp)),
//        contentScale = ContentScale.Crop
//    )
//}

@Composable
fun TextCard(text: String) {
    ClickableText(
        text = AnnotatedString(text),
        onClick = {},
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    )
}

data class ClipItem(val type: String, val time: String, val content: String)
