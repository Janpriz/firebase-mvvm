package com.dang.boswos_firebase.ui.theme.screens.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter

import com.dang.boswos_firebase.model.Upload

@Composable
fun ProductItem(upload: Upload,navControler: NavHostController) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp), // Adjust padding between items
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Load the image from URL or resource
            Image(
                painter = rememberAsyncImagePainter(upload.imageUrl), // If you have an image URL
                contentDescription = upload.name,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
                    .clip(CircleShape), // Making the image round
                contentScale = ContentScale.Crop // To crop the image inside the circle
            )
            // Product Name
            Text(text = upload.name, modifier = Modifier.weight(1f))
        }
    }
}

//@Preview
//@Composable
//private fun producpre() {
//    ProductItem(rememberNavController())
//
//}