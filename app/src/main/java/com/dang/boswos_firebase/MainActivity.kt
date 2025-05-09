package com.dang.boswos_firebase


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier

import com.dang.boswos_firebase.navigation.AppNavHost
import com.dang.boswos_firebase.ui.theme.BOSWOS_FIREBASETheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BOSWOS_FIREBASETheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->


                    AppNavHost()
//
//                )
                }
            }
        }

    }
//    }




