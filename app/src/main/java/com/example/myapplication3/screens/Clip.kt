package com.example.myapplication3.screens
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Text(text = "Clip", color = Color.White)
        },
        navigationIcon = {
            IconButton(onClick = { /* Handle user icon click */ }) {
                Text("HI")
//                Image(
//                    painter = painterResource(id = R.drawable.user_icon), // Replace with your user icon
//                    contentDescription = "User Icon",
//                    modifier = Modifier
//                        .size(32.dp)
//                        .clip(MaterialTheme.shapes.medium)
//                )
            }
        },
        actions = {
            IconButton(onClick = { /* Handle settings click */ }) {
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
    Scaffold(
        topBar = {
            AppBar()
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

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(16.dp)
                ) {
                    items(clipItems) { clip ->
                        ClipCard(clipItem = clip)
                    }
                }
            }
        }
    )
}

@Composable
fun ClipCard(clipItem: ClipItem) {
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
                Text(text = clipItem.time, style = MaterialTheme.typography.bodySmall)
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
