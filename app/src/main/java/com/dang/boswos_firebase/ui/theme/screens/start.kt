package com.dang.boswos_firebase.ui.theme.screens
import com.dang.boswos_firebase.R

import android.graphics.Color.blue
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.R
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dang.boswos_firebase.navigation.ROUTE_LOGIN

@Composable
fun StartScreen(navController: NavController){

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){

        Text(
            text = "Nyumbani",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(R.drawable.home),
            contentDescription = "home",
            modifier = Modifier
                .size(300.dp)
                .clip(shape = CircleShape),
            contentScale = ContentScale.Crop
        )

        //End

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Find a house!!!",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Nyumbani housing enables you to look for a house of your choice." +
                    "The houses are affordable and will suit you and your family ",
            textAlign = TextAlign.Center,
            fontSize = 18.sp


        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate(ROUTE_LOGIN)
        },
//            colors = ButtonDefaults.buttonColors(blue()),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp)

        ) {
            Text(text = "Get started!")
        }

    }


}

@Preview(showBackground = true)
@Composable
fun StartScreenPreview(){
    StartScreen(rememberNavController())

}