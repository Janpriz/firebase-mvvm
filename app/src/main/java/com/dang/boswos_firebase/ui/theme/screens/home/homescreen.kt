package com.dang.boswos_firebase.ui.theme.screens.home




import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.Navigation
import androidx.navigation.compose.rememberNavController
import com.dang.boswos_firebase.R
import com.dang.boswos_firebase.navigation.ROUTE_ADD_PRODUCT
import com.dang.boswos_firebase.navigation.ROUTE_HOME
import com.dang.boswos_firebase.navigation.ROUTE_PROFILE
import com.dang.boswos_firebase.navigation.ROUTE_VIEW_PRODUCT
import com.dang.boswos_firebase.navigation.ROUTE_VIEW_UPLOAD



@Composable
fun Homescreen(navController: NavHostController) {
    var selectedTab by remember { mutableStateOf("Home") }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab -> selectedTab = tab },
                navController= navController

            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Apply padding to prevent overlap with the bottom bar
            contentAlignment = Alignment.Center
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.house), // Replace with your image resource
//                contentDescription = "Background Image",
//                modifier = Modifier.fillMaxSize(),
//                contentScale = ContentScale.Crop
//            )
            when (selectedTab) {
                "Home" -> Text(text = "Welcome to the Home Screen!")
                "Profile" -> Text(text = "This is your Profile Page!")
            }
        }
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .paint(painter = painterResource(R.drawable.back), contentScale = ContentScale.FillBounds)
            ,
            horizontalAlignment = Alignment.CenterHorizontally) {
            var context= LocalContext.current
//        var productdata=productviewmodel(navController,context)

            Spacer(modifier = Modifier.height(50.dp))

            Text(text = "Welcome to Home page",
                color = Color.Cyan,
                fontFamily = FontFamily.Cursive,
                fontSize = 30.sp)
            Spacer(modifier = Modifier.height(50.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray),

                border = BorderStroke(1.dp, Color.Blue),
//                modifier = paddingModifier
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),  // Optional padding inside the Box

                    contentAlignment = Alignment.Center // This will center the content (buttons)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),

                    // Space between buttons
                    ){
                        Button(onClick = {
                            navController.navigate(ROUTE_ADD_PRODUCT)
                        },modifier = Modifier.fillMaxWidth()) {
                            Text(text = "Add House")
                        }
                    }
                }
            }


            Spacer(modifier = Modifier.height(50.dp))
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray),

                border = BorderStroke(1.dp, Color.Blue),
//                modifier = paddingModifier
                elevation = CardDefaults.cardElevation(8.dp)
            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),  // Optional padding inside the Box
                    contentAlignment = Alignment.Center // This will center the content (buttons)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)  // Space between buttons
                    ){
                        Button(onClick = {
                            navController.navigate(ROUTE_VIEW_PRODUCT)
                        },modifier = Modifier.fillMaxWidth()) {
                            Text(text = "View House")
                        }
                    }

                }
            }


            Spacer(modifier = Modifier.height(50.dp))
            Card (


                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Gray),

                border = BorderStroke(1.dp, Color.Blue),
//                modifier = paddingModifier
                elevation = CardDefaults.cardElevation(8.dp)

            ){
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),  // Optional padding inside the Box
                    contentAlignment = Alignment.Center // This will center the content (buttons)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)  // Space between buttons
                    ){
                        Button(
                            onClick = {
                                navController.navigate(ROUTE_VIEW_UPLOAD)
                            },modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = "View houses")
                        }
                    }
                }

            }




        }
    }

}

@Composable
fun BottomNavigationBar(selectedTab: String, onTabSelected: (String) -> Unit,
                        navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = selectedTab == "Home",
            onClick = { navController.navigate(ROUTE_HOME) },
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )
        NavigationBarItem(
            selected = selectedTab == "Profile",
            onClick = { navController.navigate(ROUTE_PROFILE)},
            label = { Text("Profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Homescreen(rememberNavController())
}