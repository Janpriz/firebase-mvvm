package com.dang.boswos_firebase.ui.theme.screens.products

import House
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.dang.boswos_firebase.data.productviewmodel
import com.dang.boswos_firebase.navigation.ROUTE_HOME
import com.dang.boswos_firebase.navigation.ROUTE_UPDATE_PRODUCT

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewProductsScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White) // Set background color
    ) {
        val context = LocalContext.current
        val productRepository = productviewmodel(navController, context)
        val emptyProductState = remember { mutableStateOf(House("", "", "", "", "")) } // Added imageUrl as an empty string
        val emptyProductsListState = remember { mutableStateListOf<House>() }

        val products = productRepository.viewProducts(emptyProductState, emptyProductsListState)

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = "All Houses") },
                    navigationIcon = {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back Arrow",
                            modifier = Modifier.clickable {
                                // Navigate back to the home screen
                                navController.navigate(ROUTE_HOME) {
                                    popUpTo(navController.graph.startDestinationId) { inclusive = true }
                                }
                            }
                        )
                    }
                )
            },
            content = { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))

                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) {
                            ProductCard(
                                 name = it.name,
                                description = it.description,
                                price = it.price,
                                imageUrl = it.imageUrl,
                                id = it.id,
                                navController = navController,
                                productRepository = productRepository
                            )
                        }
                    }
                }
            }
        )
    }
}

@Composable
fun ProductCard(
    name: String,
    description: String,
    price: String,
    imageUrl: String,
    id: String,
    navController: NavHostController,
    productRepository: productviewmodel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Name: $name", fontSize = 18.sp, color = Color.Black)
            Text(text = "Description: $description", fontSize = 16.sp, color = Color.DarkGray)
            Text(text = "Price: $price", fontSize = 16.sp, color = Color.Green)

            // Display the product image
            if (imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Product Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { productRepository.deleteProduct(id) }) {
                    Text(text = "Delete")
                }
                Button(onClick = { navController.navigate(ROUTE_UPDATE_PRODUCT + "/$id") }) {
                    Text(text = "Update")
                }
            }
        }
    }
}

@Preview
@Composable
fun view() {
    ViewProductsScreen(rememberNavController())
}