package com.example.darren.myreader.components

import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.darren.myreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth
import com.example.darren.myreader.R

@Composable
fun ReaderAppBar(
    title: String,
    icon: ImageVector? = null,
    showProfile: Boolean = true,
    navController: NavHostController,
    onBackArrowClicked: () -> Unit = {}
){
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (showProfile){
                    Icon(
                        imageVector = Icons.Filled.MenuBook,
                        contentDescription = ""
                    )
                }
                if (icon != null){
                    Icon(
                        imageVector = icon,
                        contentDescription = "",
                        modifier = Modifier
                            .clickable {
                                onBackArrowClicked.invoke()
                            },
                        tint = Color.Red.copy(alpha = 0.7f)
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = title,
                    color = Color.Red.copy(alpha = 0.9f),
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }

        },
        actions = {
            IconButton(
                onClick = {
                    FirebaseAuth.getInstance().signOut().run{
                        navController.navigate(ReaderScreens.LoginScreen.name)
                    }
                }
            ) {
                if(showProfile){
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = "",
                    )
                } else{
                    Box() {
                        
                    }
                }

            }

        },
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    )
}

@ExperimentalComposeUiApi
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onPressRating: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }

    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )

    Row(
        modifier = Modifier.width(280.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        for (i in 1..5) {
            Icon(
                painter = painterResource(id = R.drawable.ic_baseline_star_24),
                contentDescription = "star",
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onPressRating(i)
                                ratingState = i
                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color(0xFFFFD700) else Color(0xFFA2ADB1)
            )
        }
    }
}