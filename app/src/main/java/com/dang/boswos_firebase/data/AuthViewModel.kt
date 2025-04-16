package com.dang.boswos_firebase.data

import android.R.id.progress
import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.collection.intFloatMapOf
import androidx.navigation.NavHostController
import com.dang.boswos_firebase.model.User
import com.dang.boswos_firebase.navigation.ROUTE_HOME
import com.dang.boswos_firebase.navigation.ROUTE_LOGIN
import com.dang.boswos_firebase.navigation.ROUTE_REGISTER
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class AuthViewModel (
    var navController: NavHostController,
    var context:Context
){
    var mAuth: FirebaseAuth
    val progress: ProgressDialog

    init {
        mAuth= FirebaseAuth.getInstance()
        progress=ProgressDialog(context)
        progress.setTitle("Loading")
        progress.setMessage("PLease Wait.....")

    }
    fun signup(email:String,pass:String,confpass:String){
        if (email.isBlank()||pass.isBlank()||confpass.isBlank()){
            Toast.makeText(context,"Please email and password cannot be "+ "blank", Toast.LENGTH_LONG).show()
            return
        }else if(pass!=confpass){
            Toast.makeText(context, "Password does not match",Toast.LENGTH_LONG).show()
            return

        }else{
            mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener{
                if (it.isSuccessful){
                    val userData= User(email,pass,mAuth.currentUser!!.uid)
                    val regRef= FirebaseDatabase.getInstance().getReference()
                        .child("Users/"+mAuth.currentUser!!.uid)
                    regRef.setValue(userData).addOnCompleteListener {
                        if (it.isSuccessful){
                            Toast.makeText(context, "Registered Succesfully", Toast.LENGTH_SHORT).show()
                            navController.navigate(ROUTE_LOGIN)

                        }else{
                            Toast.makeText(context, "$(it.exception!!.message)", Toast.LENGTH_SHORT).show()
                            navController.navigate(ROUTE_LOGIN)

                        }
                    }
                }else{
                    navController.navigate(ROUTE_REGISTER)
                }
            }
        }
        fun login(email: String,pass: String){
            progress.show()

            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener {
                progress.dismiss()
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

    }

    }

