//package com.dang.boswos_firebase.ui.theme.screens.products
//
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.mutableStateListOf
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.rememberNavController
//import coil.compose.rememberAsyncImagePainter
//import com.dang.boswos_firebase.data.productviewmodel
//import com.dang.boswos_firebase.model.Upload
//import com.dang.boswos_firebase.navigation.ROUTE_UPDATE_PRODUCT
//
//
//@Composable
//fun ViewUploadsScreen(navController:NavHostController) {
//    Column(modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally) {
//
//        var context = LocalContext.current
//        var productRepository = productviewmodel(navController, context)
//
//
//        val emptyUploadState = remember { mutableStateOf(Upload("","","","","")) }
//        var emptyUploadsListState = remember { mutableStateListOf<Upload>() }
//
//        var uploads = productRepository.viewUploads(emptyUploadState, emptyUploadsListState)
//
//
//        Column(
//            modifier = Modifier
//                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text(text = "All uploads",
//                fontSize = 30.sp,
//                fontFamily = FontFamily.Cursive,
//                color = Color.Red)
//
//            Spacer(modifier = Modifier.height(20.dp))
//
//            LazyColumn(){
//                items(uploads){
//                    UploadItem(
//                        name = it.name,
//                        description = it.description,
//                        price = it.price,
//                        imageUrl = it.imageUrl,
//                        id = it.id,
//                        navController = navController,
//                        productRepository = productRepository
//                    )
//                }
//            }
//        }
//    }
//}
//
//
//@Composable
//fun UploadItem(name:String, description:String, price:String, imageUrl:String, id:String,
//               navController:NavHostController, productRepository:productviewmodel) {
//
//    Column(modifier = Modifier.fillMaxWidth()) {
//        Text(text = name)
//        Text(text = description)
//        Text(text = price)
//        Image(
//            painter = rememberAsyncImagePainter(imageUrl),
//            contentDescription = null,
//            modifier = Modifier.size(128.dp)
//        )
//        Button(onClick = {
//            productRepository.deleteProduct(id)
//        }) {
//            Text(text = "Delete")
//        }
//        Button(onClick = {
//            navController.navigate(ROUTE_UPDATE_PRODUCT+"/$id")
//        }) {
//            Text(text = "Update")
//        }
//    }
//}
//
//@Preview
//@Composable
//private fun preview() {
//    ViewProductsScreen(rememberNavController())
//
//}
import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.dang.boswos_firebase.model.House
//import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.transform.CircleCropTransformation
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class Product(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val price: Double = 0.0,
    val picture: String = "" // Ensures we have an image URL field
)

@Composable
fun ViewUploadScreen(userId: String) {
    val products = remember { mutableStateListOf<Product>() }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch products from Firestore
    LaunchedEffect(Unit) {
        try {
            val db = FirebaseFirestore.getInstance()
            val querySnapshot = db.collection("products")
                .whereEqualTo("userId", userId) // Fetch products uploaded by the user
                .get()
                .await()

            val fetchedProducts = querySnapshot.documents.map { document ->
                Product(
                    id = document.id,
                    name = document.getString("name") ?: "",
                    description = document.getString("description") ?: "",
                    price = document.getDouble("price") ?: 0.0,
                    picture = document.getString("imageUrl") ?: "" // Ensure imageUrl is fetched
                )
            }
            products.addAll(fetchedProducts)
            loading = false
        } catch (e: Exception) {
            error = e.message
            loading = false
        }
    }

    // UI Rendering
    if (loading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "Error: $error")
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(products) { product ->
                ProductItem(product = product)
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            AsyncImage(
                model = product.picture,
                contentDescription = "Product Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                placeholder = rememberAsyncImagePainter(R.drawable.presence_away), // Add placeholder drawable
                error = rememberAsyncImagePainter(R.drawable.ic_menu_agenda), // Add error drawable
                contentScale = ContentScale.Crop // Ensures proper cropping for images
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = product.name, style = MaterialTheme.typography.headlineSmall)
            Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
            Text(
                text = String.format("$%.2f", product.price),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}