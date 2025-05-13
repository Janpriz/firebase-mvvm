package com.dang.boswos_firebase.ui.theme.screens.products

import android.content.Context
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.dang.boswos_firebase.data.productviewmodel
import com.dang.boswos_firebase.navigation.ROUTE_VIEW_PRODUCT

import com.dang.boswos_firebase.navigation.ROUTE_VIEW_UPLOAD
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

@Composable
fun AddProductsScreen(navController: NavHostController) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current
        Text(
            text = "Add Product",
            fontSize = 30.sp,
            modifier = Modifier.padding(20.dp)
        )

        var productName by remember { mutableStateOf("") }
        var productDescription by remember { mutableStateOf("") }
        var productPrice by remember { mutableStateOf("") }

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text(text = "Product Name *") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = productDescription,
            onValueChange = { productDescription = it },
            label = { Text(text = "Description *") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = productPrice,
            onValueChange = { productPrice = it },
            label = { Text(text = "Price *") },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        ImagePicker(
            context = context,
            navController = navController,
            productName = productName,
            productDescription = productDescription,
            productPrice = productPrice
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Add Save Button to Save Product
        Button(onClick = {
            if (productName.isBlank() || productDescription.isBlank() || productPrice.isBlank()) {
                Toast.makeText(context, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else {
                saveProduct(
                    context = context,
                    navController = navController,
                    name = productName.trim(),
                    description = productDescription.trim(),
                    price = productPrice.trim()
                )
            }
        }) {
            Text(text = "Save Product")
        }
    }
}

@Composable
fun ImagePicker(
    context: Context,
    navController: NavHostController,
    productName: String,
    productDescription: String,
    productPrice: String
) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var hasImage by remember { mutableStateOf(false) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri
            hasImage = uri != null
        }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (hasImage && imageUri != null) {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, imageUri!!))
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
            Image(bitmap = bitmap.asImageBitmap(), contentDescription = "Selected Image")
        }

        Button(
            onClick = { imagePicker.launch("image/*") },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = "Select Image")
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            if (productName.isBlank() || productDescription.isBlank() || productPrice.isBlank() || imageUri == null) {
                Toast.makeText(context, "All fields and image are required!", Toast.LENGTH_SHORT).show()
            } else {
                uploadProduct(
                    context = context,
                    navController = navController,
                    name = productName.trim(),
                    description = productDescription.trim(),
                    price = productPrice.trim(),
                    imageUri = imageUri!!
                )
            }
        }) {
            Text(text = "Upload")
        }
    }
}

fun saveProduct(
    context: Context,
    navController: NavHostController,
    name: String,
    description: String,
    price: String
) {
    val productRepo = productviewmodel(navController, context)
    MainScope().launch {
        try {
            productRepo.saveProduct(name, description, price)
            Toast.makeText(context, "Product saved successfully!", Toast.LENGTH_SHORT).show()
            navController.navigate(ROUTE_VIEW_PRODUCT) // Navigate to saved products screen
        } catch (e: Exception) {
            Toast.makeText(context, "Save failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

fun uploadProduct(
    context: Context,
    navController: NavHostController,
    name: String,
    description: String,
    price: String,
    imageUri: Uri
) {
    val productRepo = productviewmodel(navController, context)
    MainScope().launch {
        try {
            productRepo.saveProductWithImage(name, description, price, imageUri)
            Toast.makeText(context, "Product uploaded successfully!", Toast.LENGTH_SHORT).show()
            navController.navigate(ROUTE_VIEW_UPLOAD)
        } catch (e: Exception) {
            Toast.makeText(context, "Upload failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}