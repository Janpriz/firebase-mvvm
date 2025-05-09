package com.dang.boswos_firebase.ui.theme.screens.products

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

@Composable
fun UploadScreen(userId: NavHostController) {

    val db = FirebaseFirestore.getInstance()
    val storage = FirebaseStorage.getInstance()
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var price by remember { mutableStateOf(TextFieldValue("")) }
    var picture by remember { mutableStateOf<Uri?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadMessage by remember { mutableStateOf<String?>(null) }

    // Image picker launcher
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        picture = uri

    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Upload Product", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Product Name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Product Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Product Price") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { launcher.launch("image/*") }) {
            Text(text = "Pick an Image")
        }
        Spacer(modifier = Modifier.height(8.dp))

        picture?.let {
            Text(text = "Image selected: ${it.lastPathSegment}")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                isUploading = true
                uploadMessage = null
                uploadProduct(
                    db = db,
                    storage = storage,
                    userId = userId.toString(),
                    name = name.text,
                    description = description.text,
                    price = price.text.toDoubleOrNull() ?: 0.0,
                    imageUri = picture,
                    onSuccess = {
                        isUploading = false
                        uploadMessage = "Product uploaded successfully!"
                        name = TextFieldValue("")
                        description = TextFieldValue("")
                        price = TextFieldValue("")
                        picture = null
                    },
                    onError = { error ->
                        isUploading = false
                        uploadMessage = "Error: $error"
                    }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isUploading
        ) {
            if (isUploading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            } else {
                Text(text = "Upload Product")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        uploadMessage?.let {
            Text(text = it, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

fun uploadProduct(
    db: FirebaseFirestore,
    storage: FirebaseStorage,
    userId: String,
    name: String,
    description: String,
    price: Double,
    imageUri: Uri?,
    onSuccess: () -> Unit,
    onError: (String) -> Unit
) {
    if (imageUri == null) {
        onError("Please select an image before uploading.")
        return
    }

    val imageRef = storage.reference.child("product_images/${UUID.randomUUID()}")

    // Start uploading the image to Firebase Storage
    imageRef.putFile(imageUri)
        .addOnSuccessListener {
            // If image upload succeeds, get the download URL
            imageRef.downloadUrl
                .addOnSuccessListener { uri ->
                    // Add product details to Firestore with the image URL
                    val product = hashMapOf(
                        "userId" to userId,
                        "name" to name,
                        "description" to description,
                        "price" to price,
                        "imageUrl" to uri.toString()
                    )
                    db.collection("products")
                        .add(product)
                        .addOnSuccessListener {
                            onSuccess()
                        }
                        .addOnFailureListener { e ->
                            onError("Failed to upload product details: ${e.message}")
                        }
                }
                .addOnFailureListener { e ->
                    onError("Failed to retrieve image URL: ${e.message}")
                }
        }
        .addOnFailureListener { e ->
            // Handle image upload failure
            onError("Failed to upload image: ${e.message}")
        }
}