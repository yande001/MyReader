package com.example.darren.myreader.screens.splash



import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.darren.myreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Preview
@Composable
fun ReaderSplashScreen(
    navController: NavHostController = rememberNavController()
) {
    val scale = remember{
        Animatable(0f)
    }

    LaunchedEffect(key1 = true){
        scale.animateTo(
            targetValue = 0.9f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(8f)
                        .getInterpolation(it)
                }
            )
        )
        delay(1500)
        if(FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()){
            navController.navigate(ReaderScreens.LoginScreen.name)
        } else{
            navController.navigate(ReaderScreens.ReaderHoneScreen.name)
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(330.dp)
                .scale(scale.value),
            shape = CircleShape,
            color = Color.White,
            border = BorderStroke(
                width = 2.dp,
                color = Color.LightGray
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "My Reader",
                    style = MaterialTheme.typography.h3,
                    color = Color.Red.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"Read. Change. Yourself.\"",
                    style = MaterialTheme.typography.subtitle1,
                    color = Color.Gray.copy(alpha = 0.6f)
                )
            }

        }

    }
}