package com.dang.boswos_firebase.ui.theme.screens.products

import House
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.dang.boswos_firebase.data.productviewmodel

import com.dang.boswos_firebase.navigation.ROUTE_HOME
import androidx.core.net.toUri

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewUploadScreen(navController: NavHostController) {
    var productList by remember { mutableStateOf<List<House>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val productRepo = productviewmodel(navController, context) // Ensure this is implemented correctly
        try {
            val products = productRepo.getProducts() // Fetch products from the repository
            productList = products
        } catch (e: Exception) {
            Toast.makeText(context, "Failed to load houses: ${e.message}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Uploaded Houses")
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back Arrow",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Navigate back to the home screen
                                navController.navigate(ROUTE_HOME) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }
                    )
                },
                actions = {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable {
                                // Open the phone dialer with a specific phone number
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:0704585732") // Replace with your phone number
                                }
                                context.startActivity(intent)
                            }
                    )
                },
                colors = androidx.compose.material3.TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFFA500), // Orange color
                    titleContentColor = Color.White, // Title color
                    navigationIconContentColor = Color.White, // Back arrow color
                    actionIconContentColor = Color.White // Phone icon color
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Loading houses...")
                }
            } else if (productList.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "No houses available.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(productList) { product ->
                        ProductCard(product)
                    }
                }
            }
        }
    }
}

//@Composable
//fun ProductCard(product: House) {
//    androidx.compose.material3.Card(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // Display the product name
//            Text(text = product.name, fontSize = 20.sp)
//
//            // Display the product description
//            Text(text = "Description: ${product.description}")
//
//            // Display the product price
//            Text(text = "Price: ${product.price}", fontSize = 16.sp)
//
//            // Display the product image if the URL is not null or empty
//            if (product.imageUrl.isNotEmpty()) {
//                Image(
//                    painter = rememberAsyncImagePainter(product.imageUrl),
//                    contentDescription = "Product Image",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(200.dp),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//    }
//}
@Composable
fun ProductCard(product: House) {
    androidx.compose.material3.Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = androidx.compose.material3.CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Display the product image if the URL is not null or empty
            if (product.imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(product.imageUrl),
                    contentDescription = "House Image",
                    modifier = Modifier
                        .size(100.dp) // Fixed size for the image
                        .aspectRatio(1f), // Maintain square aspect ratio
                    contentScale = ContentScale.Crop
                )
            }

            // Display the product details in a Column beside the image
            Column(
                modifier = Modifier.weight(1f), // Take up remaining space
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Display the product name
                Text(text = product.name, fontSize = 20.sp)

                // Display the product description
                Text(text = "Description: ${product.description}")

                // Display the product price
                Text(text = "Price: ${product.price}", fontSize = 16.sp)
            }
        }
    }
}