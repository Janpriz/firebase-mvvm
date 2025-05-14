package com.dang.boswos_firebase.ui.theme.screens.products

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dang.boswos_firebase.data.productviewmodel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun UpdateProductsScreen(navController: NavHostController, id: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val context = LocalContext.current

        // States to hold product details
        var name by remember { mutableStateOf("") }
        var description by remember { mutableStateOf("") }
        var price by remember { mutableStateOf("") }

        // Firebase reference to the specific product
        val currentDataRef = FirebaseDatabase.getInstance().getReference("Products/$id")

        // Load the current product data
        LaunchedEffect(Unit) {
            currentDataRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val product = snapshot.getValue(House::class.java)
                    if (product != null) {
                        name = product.name
                        description = product.description
                        price = product.price
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })
        }

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Update Product",
            fontSize = 30.sp,
            fontFamily = FontFamily.Monospace,
            color = Color.Blue,
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Text fields for editing product information
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text(text = "House Name *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text(text = "House Description *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text(text = "House Price *") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Update and Delete buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    // Update product logic
                    val productRepository = productviewmodel(navController, context)
                    productRepository.updateProduct(
                        name.trim(),
                        description.trim(),
                        price.trim(),
                        id
                    )
                    Toast.makeText(context, "House updated successfully", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            ) {
                Text(text = "Update")
            }

//            Button(
//                onClick = {
//                    // Delete product logic
//                    try {
//                        currentDataRef.removeValue().addOnCompleteListener { task ->
//                            if (task.isSuccessful) {
//                                Toast.makeText(context, "House deleted successfully", Toast.LENGTH_SHORT).show()
//                                navController.popBackStack() // Navigate back after deletion
//                            } else {
//                                Toast.makeText(context, "Failed to delete house", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
//                    }
//                },
//                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
//            ) {
//                Text(text = "Delete", color = Color.White)
//            }
        }
    }
}

@Preview
@Composable
fun PreviewUpdateProductsScreen() {
    UpdateProductsScreen(rememberNavController(), id = "sampleId")
}