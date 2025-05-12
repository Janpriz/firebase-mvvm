package com.dang.boswos_firebase.ui.theme.screens.profiles

//noinspection SuspiciousImport

import android.widget.Toast
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Button
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.MaterialTheme
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Text
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.dang.boswos_firebase.R
import com.dang.boswos_firebase.data.UserProfile

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore



@Composable
fun ProfileScreen(navController: NavHostController) {
    val auth = FirebaseAuth.getInstance()
    val db = FirebaseFirestore.getInstance()

    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var isEditing by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    val user = auth.currentUser

    val context = LocalContext.current
//    Toast.makeText(Context, "Error loading User data: ${exception.message}",
//        Toast.LENGTH_SHORT).show()

    // Fetch user data from Firestore when the profile screen is loaded
    LaunchedEffect(user?.uid) {
        user?.uid?.let {
            db.collection("users")
                .document(it)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val userData = document.toObject(UserProfile::class.java)
                        userProfile = userData
                        name = userData?.name ?: ""
                        email = userData?.email ?: ""
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Error loading user data: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        userProfile?.profilePictureUrl?.let { imageUrl ->
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
                    .clip(CircleShape)
            )
        } ?: run {
            // Placeholder image if there's no profile picture URL
            Image(
                painter = painterResource(id = (R.drawable.home)), // Replace with your image
                contentDescription = "Profile Placeholder",
                modifier = Modifier
                    .size(120.dp)
                    .padding(bottom = 16.dp)
                    .clip(CircleShape)
            )
        }

        // Name
        if (isEditing) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(text = name, style = MaterialTheme.typography.h5)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Email
        Text(text = email, style = MaterialTheme.typography.body1)

        Spacer(modifier = Modifier.height(16.dp))

        // Edit/Save Button
        Button(
            onClick = {
                if (isEditing) {
                    // Save the updated profile information to Firestore
                    user?.let { currentUser ->
                        val updatedProfile = UserProfile(name = name, email = email)
                        db.collection("users").document(currentUser.uid)
                            .set(updatedProfile)
                            .addOnSuccessListener {
                                Toast.makeText(context, "Profile updated successfully",Toast.LENGTH_SHORT).show()
                                isEditing = false
                            }
                            .addOnFailureListener {
                                Toast.makeText(
                                    context,
                                    "Error updating profile",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    }
                } else {
                    isEditing = true
                }
            }
        ) {
            Text(text = if (isEditing) "Save" else "Edit")
        }
    }

}

@Preview
@Composable
private fun Profilepreview() {
    ProfileScreen(rememberNavController())

}