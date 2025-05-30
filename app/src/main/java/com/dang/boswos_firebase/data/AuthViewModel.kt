package com.firebaseone.data
import android.R.attr.progress
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.navigation.NavHostController
import androidx.navigation.navOptions
import com.dang.boswos_firebase.model.User
import com.dang.boswos_firebase.navigation.ROUTE_HOME
import com.dang.boswos_firebase.navigation.ROUTE_LOGIN
import com.dang.boswos_firebase.navigation.ROUTE_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.rpc.context.AttributeContext.Auth


class AuthViewModel(var navController:NavHostController,var context:Context){

    var mAuth: FirebaseAuth


    init {
        mAuth= FirebaseAuth.getInstance()
    }
    fun signup(email:String,pass:String,confpass:String){


        if (email.isBlank() || pass.isBlank() ||confpass.isBlank()){

            Toast.makeText(context,"Please email and password cannot be blank",Toast.LENGTH_LONG).show()
            return
        }else if (pass != confpass){
            Toast.makeText(context,"Password do not match",Toast.LENGTH_LONG).show()
            return
        }else{
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener {
                if (it.isSuccessful){
                    val userdata= User(email,pass,mAuth.currentUser!!.uid)
                    val regRef= FirebaseDatabase.getInstance().getReference()
                        .child("Users/"+mAuth.currentUser!!.uid)
                    regRef.setValue(userdata).addOnCompleteListener {

                        if (it.isSuccessful){
                            Toast.makeText(context,"Registered Successfully",Toast.LENGTH_LONG).show()
                            navController.navigate(ROUTE_LOGIN)

                        }else{
                            Toast.makeText(context,"${it.exception!!.message}",Toast.LENGTH_LONG).show()
                            navController.navigate(ROUTE_LOGIN)
                        }
                    }
                }else{
                    navController.navigate(ROUTE_REGISTER)
                }

            } }

    }
    fun login(email: String,pass: String){


        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {

            if (it.isSuccessful){
                Toast.makeText(context,"Succeffully Logged in",Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_HOME)
//                navController.navigate(ROUTE_REGISTER)TO TAKE YOU TO A DIIFFERNT PAGE
            }else{
                Toast.makeText(context,"${it.exception!!.message}",Toast.LENGTH_LONG).show()
                navController.navigate(ROUTE_LOGIN)
            }
        }

    }
    fun logout(){
        mAuth.signOut()
        navController.navigate(ROUTE_LOGIN)
    }
    fun isloggedin():Boolean{
        return mAuth.currentUser !=null
    }
//    fun updateProfile(email: String, username: String, contact: String, location: String) {
//
//        val currentUser = Auth.currentUser
//        if (currentUser == null) {
//            Toast.makeText(context, "No user logged in.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        val userId = currentUser.uid
//
//        val userUpdates = mapOf(
//            "email" to email,
//            "username" to username,
//            "contact" to contact,
//            "location" to location
//        )
//
//        // Update Firestore database with new profile details
//        db.collection("users").document(userId)
//            .update(userUpdates)
//            .addOnSuccessListener {
//                Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
//            }
//            .addOnFailureListener { e ->
//                Toast.makeText(context, "Failed to update profile: ${e.message}", Toast.LENGTH_SHORT).show()
//            }
//    }

}