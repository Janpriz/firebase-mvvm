package com.dang.boswos_firebase.data

import House
import android.app.ProgressDialog
import androidx.lifecycle.viewModelScope

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.navigation.NavHostController


import com.dang.boswos_firebase.model.Upload
import com.dang.boswos_firebase.navigation.ROUTE_LOGIN
import com.firebaseone.data.AuthViewModel

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID


class productviewmodel(var navController: NavHostController, var context: Context) {
    var authRepository: AuthViewModel
    var progress: ProgressDialog

    init {
        authRepository = AuthViewModel(navController, context)
        if (!authRepository.isloggedin()) {
            navController.navigate(ROUTE_LOGIN)
        }
        progress = ProgressDialog(context)
        progress.setTitle("Loading")
        progress.setMessage("Please wait...")
    }


    fun saveProduct(houseName: String, houseDescription: String, housePrice: String) {
        var id = System.currentTimeMillis().toString()
        var houseData = House(houseName, houseDescription, housePrice, id)
        var houseRef = FirebaseDatabase.getInstance().getReference()
            .child("Houses/$id")
        progress.show()
        houseRef.setValue(houseData).addOnCompleteListener {
            progress.dismiss()
            if (it.isSuccessful) {
                Toast.makeText(context, "Saving successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "ERROR: ${it.exception!!.message}", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    fun viewProducts(
        house: MutableState<House>,
        houses: SnapshotStateList<House>
    ): SnapshotStateList<House> {
        var ref = FirebaseDatabase.getInstance().getReference().child("Houses")

        progress.show()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.dismiss()
                houses.clear()
                for (snap in snapshot.children) {
                    val value = snap.getValue(House::class.java)
                    house.value = value!!
                    houses.add(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        return houses
    }
    fun deleteProduct(id: String) {
        var delRef = FirebaseDatabase.getInstance().getReference()
            .child("House/$id")
        progress.show()
        delRef.removeValue().addOnCompleteListener {
            progress.dismiss()
            if (it.isSuccessful) {
                Toast.makeText(context, "House deleted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

//    fun deleteProduct(id: String) {
//        var delRef = FirebaseDatabase.getInstance().getReference()
//            .child("Houses/$id")
//        progress.show()
//        delRef.removeValue().addOnCompleteListener {
//            progress.dismiss()
//            if (it.isSuccessful) {
//                Toast.makeText(context, "House deleted", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    fun updateProduct(name: String, description: String, price: String, id: String) {
        var updateRef = FirebaseDatabase.getInstance().getReference()
            .child("Houses/$id")
        progress.show()
        var updateData = House(name, description, price, id)
        updateRef.setValue(updateData).addOnCompleteListener {
            progress.dismiss()
            if (it.isSuccessful) {
                Toast.makeText(context, "Update successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, it.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun saveProductWithImage(name: String, description: String, price: String, imageUri: Uri) {
        val storageRef = FirebaseStorage.getInstance().reference.child("products/${UUID.randomUUID()}.jpg")
        val uploadTask = storageRef.putFile(imageUri)

        uploadTask.addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                // Save product details along with the image URL
                val product = mapOf(
                    "name" to name,
                    "description" to description,
                    "price" to price,
                    "imageUrl" to uri.toString()
                )

                FirebaseFirestore.getInstance().collection("house")
                    .add(product)
                    .addOnSuccessListener {
                        // Successfully saved product
                    }
                    .addOnFailureListener { e ->
                        Log.e("Error", "Failed to save house: ${e.message}")
                    }
            }
        }.addOnFailureListener { e ->
            Log.e("Error", "Failed to upload image: ${e.message}")
        }
    }


    fun viewUploads(upload:MutableState<Upload>, uploads:SnapshotStateList<Upload>): SnapshotStateList<Upload> {
        var ref = FirebaseDatabase.getInstance().getReference().child("Uploads")

        progress.show()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                progress.dismiss()
                uploads.clear()
                for (snap in snapshot.children){
                    val value = snap.getValue(Upload::class.java)
                    upload.value = value!!
                    uploads.add(value)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
        return uploads
    }
    suspend fun getProducts(): List<House> {
        val db = FirebaseFirestore.getInstance()
        val productList = mutableListOf<House>()

        try {
            // Fetch data from the "houses" collection in Firestore
            val snapshot = db.collection("house").get().await()
            for (document in snapshot.documents) {
                val house = document.toObject(House::class.java)
                if (house != null) {
                    house.id = document.id // Assign document ID as the house ID
                    productList.add(house)
                }
            }
        } catch (e: Exception) {
            // Log or handle the error
            println("Error fetching products: ${e.message}")
        }

        return productList
    }




}